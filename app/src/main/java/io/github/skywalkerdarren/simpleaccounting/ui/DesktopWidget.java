package io.github.skywalkerdarren.simpleaccounting.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import org.joda.time.DateTime;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.AppRepository;
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillStats;
import io.github.skywalkerdarren.simpleaccounting.ui.activity.BillEditActivity;
import io.github.skywalkerdarren.simpleaccounting.ui.activity.MainActivity;
import io.github.skywalkerdarren.simpleaccounting.util.AppExecutors;
import io.github.skywalkerdarren.simpleaccounting.util.view.FormatUtil;
import kotlin.Unit;

/**
 * 桌面小部件
 *
 * @author darren
 * @date 2018/3/16
 */

public class DesktopWidget extends AppWidgetProvider {
    public static final String EXTRA_ACTION_UP = "android.appwidget.action.APPWIDGET_UPDATE";
    private static final int REQUEST_BILL = 0;
    private Context mContext;

    private RemoteViews mRemoteViews;

    public static void refresh(Context context) {
        Intent intent = new Intent(EXTRA_ACTION_UP);
        context.sendBroadcast(intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        refreshData(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        refreshData(context);
    }

    private void refreshData(Context context) {
        mContext = context;
        AppRepository repository = AppRepository.getInstance(new AppExecutors(), context);
        Intent addIntent = BillEditActivity.newIntent(mContext, null, 0, 0);
        PendingIntent pendingAddIntent = PendingIntent.getActivity(mContext, REQUEST_BILL,
                addIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent iconIntent = MainActivity.newIntent(mContext);
        PendingIntent pendingIconIntent = PendingIntent.getActivity(mContext, REQUEST_BILL,
                iconIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mRemoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_bill);
        mRemoteViews.setOnClickPendingIntent(R.id.add_bill_text_view, pendingAddIntent);
        mRemoteViews.setOnClickPendingIntent(R.id.icon, pendingIconIntent);

        repository.getBillsAnnualStats(DateTime.now().getYear(), billsStats -> {
            BillStats billStats = billsStats.get(DateTime.now().getMonthOfYear() - 1);
            mRemoteViews.setTextViewText(R.id.income_text_view, FormatUtil.getNumeric(billStats.getIncome()));
            mRemoteViews.setTextViewText(R.id.expense_text_view, FormatUtil.getNumeric(billStats.getExpense()));
            mRemoteViews.setTextViewText(R.id.total_text_view, FormatUtil.getNumeric(billStats.getSum()));
            ComponentName name = new ComponentName(mContext, DesktopWidget.class);
            AppWidgetManager.getInstance(mContext).updateAppWidget(name, mRemoteViews);
            return Unit.INSTANCE;
        });

        ComponentName name = new ComponentName(mContext, DesktopWidget.class);
        AppWidgetManager.getInstance(mContext).updateAppWidget(name, mRemoteViews);
    }
}

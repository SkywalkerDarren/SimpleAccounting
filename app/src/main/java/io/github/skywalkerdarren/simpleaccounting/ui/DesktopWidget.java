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
import io.github.skywalkerdarren.simpleaccounting.model.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.StatsLab;

/**
 * 桌面小部件
 *
 * @author darren
 * @date 2018/3/16
 */

public class DesktopWidget extends AppWidgetProvider {
    private static final int REQUEST_BILL = 0;
    public static final String EXTRA_ACTION_UP = "android.appwidget.action.APPWIDGET_UPDATE";
    private ComponentName mComponentName;

    public DesktopWidget() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        refreshData(context, AppWidgetManager.getInstance(context));
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        refreshData(context, appWidgetManager);
    }

    private void refreshData(Context context, AppWidgetManager appWidgetManager) {
        Intent intent = BillEditActivity.newIntent(context, new Bill(), 0, 0);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, REQUEST_BILL,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        StatsLab.Stats stats = StatsLab.getInstance(context)
                .getAnnualStats(DateTime.now().getYear())
                .get(DateTime.now().getMonthOfYear() - 1);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_bill);
        views.setOnClickPendingIntent(R.id.add_bill_text_view, pendingIntent);
        views.setTextViewText(R.id.income_text_view, stats.getIncome().toString());
        views.setTextViewText(R.id.expense_text_view, stats.getExpense().toString());
        views.setTextViewText(R.id.total_text_view, stats.getSum().toString());
        mComponentName = new ComponentName(context, DesktopWidget.class);
        appWidgetManager.updateAppWidget(mComponentName, views);
    }

}

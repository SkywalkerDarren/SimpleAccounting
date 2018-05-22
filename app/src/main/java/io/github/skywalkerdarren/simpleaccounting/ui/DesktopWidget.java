package io.github.skywalkerdarren.simpleaccounting.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import org.joda.time.DateTime;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.StatsLab;
import io.github.skywalkerdarren.simpleaccounting.ui.activity.BillEditActivity;
import io.github.skywalkerdarren.simpleaccounting.util.FormatUtil;

/**
 * 桌面小部件
 *
 * @author darren
 * @date 2018/3/16
 */

public class DesktopWidget extends AppWidgetProvider {
    public static final String EXTRA_ACTION_UP = "android.appwidget.action.APPWIDGET_UPDATE";
    private static final int REQUEST_BILL = 0;

    public DesktopWidget() {
        super();
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

    public static void refresh(Context context) {
        Intent intent = new Intent(DesktopWidget.EXTRA_ACTION_UP);
        context.sendBroadcast(intent);
    }

    private void refreshData(Context context) {
        StatsLab.BillStats stats = StatsLab.getInstance(context)
                .getAnnualStats(DateTime.now().getYear())
                .get(DateTime.now().getMonthOfYear() - 1);

        Intent intent = BillEditActivity.newIntent(context, new Bill(), 0, 0);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, REQUEST_BILL,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_bill);
        views.setOnClickPendingIntent(R.id.add_bill_text_view, pendingIntent);
        views.setTextViewText(R.id.income_text_view, FormatUtil.getNumberic(stats.getIncome()));
        views.setTextViewText(R.id.expense_text_view, FormatUtil.getNumberic(stats.getExpense()));
        views.setTextViewText(R.id.total_text_view, FormatUtil.getNumberic(stats.getSum()));

        ComponentName name = new ComponentName(context, DesktopWidget.class);
        AppWidgetManager.getInstance(context).updateAppWidget(name, views);
    }

}

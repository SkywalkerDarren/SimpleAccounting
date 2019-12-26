package io.github.skywalkerdarren.simpleaccounting.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import org.joda.time.DateTime;

import io.github.skywalkerdarren.simpleaccounting.R;

/**
 * 月份选择对话框
 *
 * @author darren
 * @date 2018/2/22
 */

public class MonthPickerDialog extends DialogFragment {
    static final String EXTRA_DATE = "io.github.skywalkerdarren.simpleaccounting.datetime";
    private final static String ARG_DATE_TIME = "datetime";
    private NumberPicker mYearPicker;
    private NumberPicker mMonthPicker;

    /**
     * 构造对话框构造
     *
     * @return 对话框
     */
    public static MonthPickerDialog newInstance() {
        Bundle args = new Bundle();
        MonthPickerDialog fragment = new MonthPickerDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DateTime dateTime = DateTime.now();
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        View view = LayoutInflater.from(requireActivity()).inflate(R.layout.month_picker, null);

        mMonthPicker = view.findViewById(R.id.month_picker);
        mYearPicker = view.findViewById(R.id.year_picker);

        mYearPicker.setMinValue(1900);
        mYearPicker.setMaxValue(2100);
        mMonthPicker.setMaxValue(12);
        mMonthPicker.setMinValue(1);
        setYearPicker(dateTime.getYear());
        setMonthPicker(dateTime.getMonthOfYear());

        builder.setTitle("选择日期")
                .setPositiveButton("确认", (dialogInterface, i) -> {
                    DateTime newDate = new DateTime(mYearPicker.getValue(), mMonthPicker.getValue(),
                            1, 0, 0);
                    newDate.plusMonths(1);
                    sendResult(Activity.RESULT_OK, newDate);
                })
                .setNegativeButton("取消", (dialogInterface, i) -> {
                    sendResult(Activity.RESULT_CANCELED, dateTime);
                    dialogInterface.cancel();
                })
                .setView(view);
        return builder.create();
    }

    /**
     * 设定年份
     *
     * @param year 年份
     */
    public void setYearPicker(@IntRange(from = 1900, to = 2100) int year) {
        if (mYearPicker == null) {
            return;
        }
        mYearPicker.setValue(year);
    }

    /**
     * 设定月份
     *
     * @param month 月份
     */
    public void setMonthPicker(@IntRange(from = 1, to = 12) int month) {
        if (mMonthPicker == null) {
            return;
        }
        mMonthPicker.setValue(month);
    }

    /**
     * 打包发送数据
     *
     * @param resultCode 结果响应代码
     * @param dateTime   日期
     */
    private void sendResult(int resultCode, DateTime dateTime) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, dateTime);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}

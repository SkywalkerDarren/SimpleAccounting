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
    private int year;
    private int month;

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
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        View view = LayoutInflater.from(requireActivity()).inflate(R.layout.month_picker, null);

        NumberPicker yearPicker = view.findViewById(R.id.year_picker);
        NumberPicker monthPicker = view.findViewById(R.id.month_picker);

        yearPicker.setMinValue(1900);
        yearPicker.setMaxValue(2100);
        monthPicker.setMaxValue(12);
        monthPicker.setMinValue(1);

        yearPicker.setValue(year);
        monthPicker.setValue(month);

        builder.setTitle("选择日期")
                .setPositiveButton("确认", (dialogInterface, i) -> {
                    DateTime newDate = new DateTime(yearPicker.getValue(), monthPicker.getValue(),
                            1, 0, 0);
                    newDate.plusMonths(1);
                    sendResult(newDate);
                })
                .setNegativeButton("取消", (dialogInterface, i) -> {
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
    public void setYear(@IntRange(from = 1900, to = 2100) int year) {
        this.year = year;
    }

    /**
     * 设定月份
     *
     * @param month 月份
     */
    public void setMonth(@IntRange(from = 1, to = 12) int month) {
        this.month = month;
    }

    /**
     * 打包发送数据
     *
     * @param dateTime   日期
     */
    private void sendResult(DateTime dateTime) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, dateTime);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }
}

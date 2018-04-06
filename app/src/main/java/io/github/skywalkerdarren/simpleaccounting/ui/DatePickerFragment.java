package io.github.skywalkerdarren.simpleaccounting.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.DatePicker;

import org.joda.time.DateTime;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.databinding.DialogDateBinding;

/**
 * 日期选择对话框
 *
 * @author darren
 * @date 2018/1/26
 */

public class DatePickerFragment extends DialogFragment {
    public static final String EXTRA_DATE =
            "io.github.skywalkerdarren.simpleaccounting.date";

    private static final String ARG_DATE = "date";
    private DatePicker mDatePicker;

    /**
     * 封装到datePicker的newInstance
     *
     * @param date 初始日期
     * @return 带参数的fragment
     */
    public static DatePickerFragment newInstance(DateTime date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 发送结果
     *
     * @param resultCode 结果代码：决定下一步该采取什么行动
     * @param date 日期
     */
    private void sendResult(int resultCode, DateTime date) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        // Intent：包含extra数据
        intent.putExtra(EXTRA_DATE, date);
        // 通过请求代码，为当前请求返回结果代码，并传递包含返回消息的intent
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 从args中获取消息
        DateTime date = (DateTime) getArguments().getSerializable(ARG_DATE);
        int year = date != null ? date.getYear() : DateTime.now().getYear();
        int month = date != null ? date.getMonthOfYear() - 1 : DateTime.now().getMonthOfYear();
        int day = date != null ? date.getDayOfMonth() : DateTime.now().getDayOfMonth();

        DialogDateBinding binding = DialogDateBinding.inflate(LayoutInflater.from(getActivity()));
        mDatePicker = binding.dialogDatePicker;
        mDatePicker.init(year, month, day, null);

        return new AlertDialog.Builder(getActivity())
                // 设定布局
                .setView(binding.getRoot())
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok,
                        (dialog, which) -> {
                            DateTime datetime = new DateTime(mDatePicker.getYear(),
                                    mDatePicker.getMonth() + 1,
                                    mDatePicker.getDayOfMonth(),
                                    date != null ? date.getHourOfDay() : 0,
                                    date != null ? date.getMinuteOfHour() : 0);
                            // 返回结果：成功，日期
                            sendResult(Activity.RESULT_OK, datetime);
                        })
                .create();
    }
}

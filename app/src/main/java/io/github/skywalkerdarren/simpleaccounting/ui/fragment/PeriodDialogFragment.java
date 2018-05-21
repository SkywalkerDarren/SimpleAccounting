package io.github.skywalkerdarren.simpleaccounting.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.DatePicker;

import org.joda.time.DateTime;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.databinding.PeriodDialogFragmentBinding;

/**
 * @author darren
 * @date 2018/3/25
 */

public class PeriodDialogFragment extends DialogFragment {

    public static final String EXTRA_END_DATE = "io.github.skywalkerdarren.simpleaccounting.ui.fragment.PeriodDialogFragment.EXTRA_END_DATE";
    public static final String EXTRA_START_DATE = "io.github.skywalkerdarren.simpleaccounting.ui.fragment.PeriodDialogFragment.EXTRA_START_DATE";
    private static final String ARG_START = "start";
    private static final String ARG_END = "end";
    private DatePicker mStartDatePicker;
    private DatePicker mEndDatePicker;

    public static PeriodDialogFragment newInstance(DateTime start, DateTime end) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_START, start);
        args.putSerializable(ARG_END, end);
        PeriodDialogFragment fragment = new PeriodDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 发送结果
     *
     * @param resultCode 结果代码：决定下一步该采取什么行动
     * @param start      起始日期
     * @param end        结束日期
     */
    private void sendResult(int resultCode, DateTime start, DateTime end) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        // Intent：包含extra数据
        intent.putExtra(EXTRA_START_DATE, start);
        intent.putExtra(EXTRA_END_DATE, end);
        // 通过请求代码，为当前请求返回结果代码，并传递包含返回消息的intent
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DateTime startDateTime = (DateTime) getArguments().getSerializable(ARG_START);
        DateTime endDateTime = (DateTime) getArguments().getSerializable(ARG_END);
        DateTime now = DateTime.now();

        PeriodDialogFragmentBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()),
                R.layout.period_dialog_fragment, null, false);
        mStartDatePicker = binding.startDatePicker;
        mEndDatePicker = binding.endDatePicker;

        mStartDatePicker.init(startDateTime != null ? startDateTime.getYear() : now.getYear(),
                startDateTime != null ? startDateTime.getMonthOfYear() - 1 : now.getMonthOfYear(),
                startDateTime != null ? startDateTime.getDayOfMonth() : now.getDayOfMonth(),
                null);
        mEndDatePicker.init(endDateTime != null ? endDateTime.getYear() : now.getYear(),
                endDateTime != null ? endDateTime.getMonthOfYear() - 1 : now.getMonthOfYear(),
                endDateTime != null ? endDateTime.getDayOfMonth() : now.getDayOfMonth(),
                null);

        return new AlertDialog.Builder(getContext())
                .setView(binding.getRoot())
                .setTitle(getString(R.string.set_date))
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    DateTime start = new DateTime(mStartDatePicker.getYear(),
                            mStartDatePicker.getMonth() + 1,
                            mStartDatePicker.getDayOfMonth(),
                            0, 0);
                    DateTime end = new DateTime(mEndDatePicker.getYear(),
                            mEndDatePicker.getMonth() + 1,
                            mEndDatePicker.getDayOfMonth(),
                            23, 59);
                    if (start.isAfter(end)) {
                        sendResult(Activity.RESULT_CANCELED, start, end);
                    } else {
                        sendResult(Activity.RESULT_OK, start, end);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }
}

package io.github.skywalkerdarren.simpleaccounting.ui.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;

import org.joda.time.DateTime;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.base.BaseFragment;
import io.github.skywalkerdarren.simpleaccounting.databinding.FragmentChartBinding;
import io.github.skywalkerdarren.simpleaccounting.util.ViewModelFactory;
import io.github.skywalkerdarren.simpleaccounting.util.view.CustomTypefaceSpan;
import io.github.skywalkerdarren.simpleaccounting.util.view.FormatUtil;
import io.github.skywalkerdarren.simpleaccounting.view_model.ChartViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link PieChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PieChartFragment extends BaseFragment {
    private static final String ARG_START = "start";
    private static final String ARG_END = "end";
    private static final String ARG_IS_EXPENSE = "isExpense";
    private DateTime mStartDateTime;
    private DateTime mEndDateTime;
    private PieChart mPieChart;
    private boolean mIsExpense;
    private ChartViewModel mViewModel;
    private FragmentChartBinding mBinding;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PieChartFragment.
     * @
     */
    public static PieChartFragment newInstance(DateTime start, DateTime end, boolean isExpense) {
        PieChartFragment fragment = new PieChartFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_START, start);
        args.putSerializable(ARG_END, end);
        args.putBoolean(ARG_IS_EXPENSE, isExpense);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_chart, container, false);
        mPieChart = mBinding.pieChart;
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            mStartDateTime = (DateTime) getArguments().getSerializable(ARG_START);
            mEndDateTime = (DateTime) getArguments().getSerializable(ARG_END);
            mIsExpense = getArguments().getBoolean(ARG_IS_EXPENSE);
        } else {
            mStartDateTime = DateTime.now().minusMonths(1);
            mEndDateTime = DateTime.now();
            mIsExpense = true;
        }

        ViewModelFactory factory = ViewModelFactory.getInstance(requireActivity().getApplication());
        mViewModel = ViewModelProviders.of(this, factory).get(ChartViewModel.class);
        mBinding.setChart(mViewModel);
        mBinding.setLifecycleOwner(this);

        configChartStyle();
    }

    private void configChartStyle() {
        // 关闭描述
        Description description = new Description();
        description.setEnabled(false);
        mPieChart.setDescription(description);

        mViewModel.getBillStats().observe(this, billStats -> {
            String s = FormatUtil.getNumeric(mIsExpense ? billStats.getExpense() : billStats.getIncome());
            mPieChart.setCenterText(generateCenterText(s));
        });
        mPieChart.setCenterTextSize(24);
        mPieChart.setEntryLabelColor(Color.BLACK);
        mPieChart.setHoleRadius(70f);
        mPieChart.setTransparentCircleRadius(75f);
        mPieChart.setUsePercentValues(true);
        mPieChart.animateY(1000, Easing.EaseInOutExpo);

        // 关闭图注
        Legend legend = mPieChart.getLegend();
        legend.setEnabled(false);
    }

    private SpannableString generateCenterText(String sum) {
        String str = mIsExpense ? getString(R.string.expense) : getString(R.string.income);
        SpannableString s = new SpannableString(str + "\n" + sum);
        s.setSpan(new RelativeSizeSpan(2f), 0, str.length(), 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), str.length(), s.length(), 0);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Typeface typeface = getResources().getFont(R.font.lalezar);
            s.setSpan(new CustomTypefaceSpan("", typeface), str.length(), s.length(), 0);
        }
        return s;
    }

    @Override
    protected void updateUI() {
        mViewModel.start(mStartDateTime, mEndDateTime, mIsExpense, requireContext());

        mViewModel.getPieData().observe(this, pieData -> {
            mPieChart.setData(pieData);
            mPieChart.invalidate();
        });
    }


}

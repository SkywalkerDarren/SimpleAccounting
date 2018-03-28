package io.github.skywalkerdarren.simpleaccounting.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.joda.time.DateTime;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.BillLab;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStartDateTime = (DateTime) getArguments().getSerializable(ARG_START);
            mEndDateTime = (DateTime) getArguments().getSerializable(ARG_END);
            mIsExpense = getArguments().getBoolean(ARG_IS_EXPENSE);
        } else {
            mStartDateTime = DateTime.now().minusMonths(1);
            mEndDateTime = DateTime.now();
            mIsExpense = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        mPieChart = view.findViewById(R.id.pie_chart);
        configChartStyle();
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void configChartStyle() {
        Description description = new Description();
        description.setEnabled(false);
        mPieChart.setDescription(description);

        BillLab.Stats stats = BillLab.getInstance(getContext())
                .getStats(mStartDateTime, mEndDateTime);
        String s = mIsExpense ? stats.getExpense().toString() : stats.getIncome().toString();
        mPieChart.setCenterText(generateCenterText(s));

        mPieChart.setCenterTextSize(24);
        mPieChart.setEntryLabelColor(Color.BLACK);
        mPieChart.setHoleRadius(70f);
        mPieChart.setTransparentCircleRadius(75f);
        mPieChart.setUsePercentValues(true);
        mPieChart.animateY(1000, Easing.EasingOption.EaseInOutExpo);
        // TODO: 2018/3/28 暂无法解决view pager与图表滑动的冲突
        //        mPieChart.setTouchEnabled(false);


        Legend legend = mPieChart.getLegend();
        legend.setEnabled(false);
    }

    private SpannableString generateCenterText(String sum) {
        String str = mIsExpense ? getString(R.string.expense) : getString(R.string.income);
        SpannableString s = new SpannableString(str + "\n" + sum);
        s.setSpan(new RelativeSizeSpan(2f), 0, str.length(), 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), str.length(), s.length(), 0);
        return s;
    }

    @Override
    protected void updateUI() {
        List<BillLab.TypeStats> typeStats = BillLab.getInstance(getContext())
                .getTypeStats(mStartDateTime, mEndDateTime, mIsExpense);

        List<PieEntry> pieEntries = new ArrayList<>(10);
        List<Integer> colorList = new ArrayList<>(10);
        if (typeStats != null) {
            for (BillLab.TypeStats stats : typeStats) {
                PieEntry entry = new PieEntry(stats.getSum().floatValue(), stats.getType().getName());
                colorList.add(stats.getType().getColorId());
                pieEntries.add(entry);
            }
        }
        PieDataSet pieDataSet = getPieDataSet(pieEntries, colorList);
        pieDataSet.notifyDataSetChanged();

        PieData pieData = new PieData();
        pieData.addDataSet(pieDataSet);
        mPieChart.setData(pieData);

        mPieChart.invalidate();
    }

    @NonNull
    private PieDataSet getPieDataSet(List<PieEntry> pieEntries, List<Integer> colorList) {
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(colorList);
        pieDataSet.setValueTextColors(colorList);
        pieDataSet.setValueLineColor(Color.rgb(0xf5, 0x7f, 0x17));
        pieDataSet.setValueFormatter((value, entry, dataSetIndex, viewPortHandler) -> {
            DecimalFormat format = new DecimalFormat("##0.00");
            return format.format(value) + " %";
        });
        pieDataSet.setValueLinePart1OffsetPercentage(55f);
        pieDataSet.setValueLinePart1Length(0.1f);
        pieDataSet.setValueLinePart2Length(0.5f);
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setValueTextSize(12f);

        return pieDataSet;
    }
}

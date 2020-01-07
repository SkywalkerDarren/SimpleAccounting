package io.github.skywalkerdarren.simpleaccounting.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.adapter.StatsAdapter;
import io.github.skywalkerdarren.simpleaccounting.base.BaseFragment;
import io.github.skywalkerdarren.simpleaccounting.databinding.FragmentJournalBinding;
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillStats;
import io.github.skywalkerdarren.simpleaccounting.util.ViewModelFactory;
import io.github.skywalkerdarren.simpleaccounting.view_model.JournalViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JournalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JournalFragment extends BaseFragment implements View.OnClickListener {

    private LineChart mLineChart;
    private StatsAdapter mStatsAdapter;
    private JournalViewModel mViewModel;
    private boolean mShowIncome = true;
    private boolean mShowExpense = true;
    private boolean mShowBalance = true;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment JournalFragment.
     */
    public static JournalFragment newInstance() {
        JournalFragment fragment = new JournalFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentJournalBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_journal, container, false);
        ViewModelFactory factory = ViewModelFactory.getInstance(requireActivity().getApplication());
        mViewModel = ViewModelProviders.of(this, factory).get(JournalViewModel.class);
        binding.setJournal(mViewModel);
        binding.setLifecycleOwner(this);

        mLineChart = binding.statsLineChart;

        binding.statsRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.expenseCardView.setOnClickListener(this);
        binding.incomeCardView.setOnClickListener(this);
        binding.balanceCardView.setOnClickListener(this);
        mStatsAdapter = new StatsAdapter();
        binding.statsRecyclerView.setAdapter(mStatsAdapter);
        // TODO 动态年份
        configChartStyle(mLineChart);
        return binding.getRoot();
    }

    /**
     * 更新数据
     */
    private void updateLineDataSets(List<BillStats> statsList, boolean showIncome, boolean showExpense, boolean showSum) {
        List<Entry> income = new ArrayList<>();
        List<Entry> expense = new ArrayList<>();
        List<Entry> sum = new ArrayList<>();
        for (int i = 0; i < statsList.size(); i++) {
            BillStats stats = statsList.get(i);
            expense.add(new Entry(i, stats.getExpense().floatValue()));
            income.add(new Entry(i, stats.getIncome().floatValue()));
            sum.add(new Entry(i, stats.getSum().floatValue()));
        }
        final LineDataSet incomeSet = new LineDataSet(income, "");
        final LineDataSet balanceSet = new LineDataSet(sum, "");
        final LineDataSet expenseSet = new LineDataSet(expense, "");
        configDataSet(incomeSet, R.color.income);
        configDataSet(balanceSet, R.color.balance);
        configDataSet(expenseSet, R.color.expense);
        incomeSet.setVisible(showIncome);
        balanceSet.setVisible(showSum);
        expenseSet.setVisible(showExpense);
        List<ILineDataSet> lineDataSets = new ArrayList<>(3);
        lineDataSets.add(incomeSet);
        lineDataSets.add(balanceSet);
        lineDataSets.add(expenseSet);
        mLineChart.setData(new LineData(lineDataSets));
        mLineChart.notifyDataSetChanged();
        mLineChart.animateY(300, Easing.EaseOutCirc);
        mLineChart.invalidate();
    }

    /**
     * 放一些需要经常更新的方法
     */
    @Override
    public void updateUI() {
        mViewModel.getStats().observe(this, billStats -> {
            mStatsAdapter.setNewData(billStats);
            mStatsAdapter.notifyDataSetChanged();
            updateLineDataSets(billStats, true, true, true);
        });
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.income_card_view:
                mShowIncome = !mShowIncome;
                break;
            case R.id.expense_card_view:
                mShowExpense = !mShowExpense;
                break;
            case R.id.balance_card_view:
                mShowBalance = !mShowBalance;
                break;
            default:
                break;
        }
        mViewModel.getStats().observe(this, billStats ->
                updateLineDataSets(billStats, mShowIncome, mShowExpense, mShowBalance));
    }

    /**
     * 配置图表样式
     */
    private void configChartStyle(LineChart lineChart) {
        Description description = new Description();
        description.setEnabled(false);
        lineChart.setDescription(description);
        lineChart.setScaleEnabled(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.setMarker(new TopHighLight(requireActivity()));

        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(12, true);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return (int) (value + 1) + "月";
            }
        });

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setGridColor(ContextCompat.getColor(requireContext(), R.color.grey300));
        yAxis.setAxisLineColor(ContextCompat.getColor(requireContext(), R.color.transparent));
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxis.setLabelCount(5, false);
    }

    /**
     * 配置曲线样式
     *
     * @param colorId 颜色
     */
    private void configDataSet(LineDataSet set, @ColorRes int colorId) {
        int color = ContextCompat.getColor(requireContext(), colorId);
        set.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        set.setDrawFilled(true);
        set.setDrawValues(false);
        set.setHighlightEnabled(true);
        set.setColor(color);
        set.setLineWidth(1.5f);
        set.setCircleColor(color);
        set.setCircleRadius(2.5f);
        set.setFillColor(color);
        set.setCircleHoleRadius(1.5f);
        set.setHighLightColor(color);
    }

    /**
     * 顶部标志
     */
    private class TopHighLight extends MarkerView {

        DecimalFormat mFormat;
        TextView mMarkerTextView;

        public TopHighLight(Context context) {
            super(context, R.layout.marker_view);
            mMarkerTextView = findViewById(R.id.marker_text_view);
            mFormat = new DecimalFormat("###,###,##0.##");
        }

        @Override
        public void refreshContent(Entry e, Highlight highlight) {
            mMarkerTextView.setText(mFormat.format(e.getY()));
            mMarkerTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.orange800));
            super.refreshContent(e, highlight);
        }

        @Override
        public MPPointF getOffset() {
            return new MPPointF(10, -40);
        }
    }

}

package io.github.skywalkerdarren.simpleaccounting.ui;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.BillLab;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private BillLab mBillLab;

    private PieChart mPieChart;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StatsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatsFragment newInstance() {
        StatsFragment fragment = new StatsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBillLab = BillLab.getInstance(getActivity());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        mPieChart = view.findViewById(R.id.stats_pie_chart);
        // TODO test Chart
        List<PieEntry> data = new ArrayList<>();
        List<Bill> bills = mBillLab.getsBills(DateTime.now().getYear(), DateTime.now().getMonthOfYear());
        PieData pieData = new PieData();
        for (int i = 0; i < bills.size(); i++) {
            Bill bill = bills.get(i);
            data.add(new PieEntry(bill.getBalance().floatValue()));
        }

        PieDataSet dataSet = configDataSet(data, R.color.amber400);
        pieData.addDataSet(dataSet);
        mPieChart.setData(pieData);
        mPieChart.animateY(1000, Easing.EasingOption.EaseOutCirc);
        updateUI();
        return view;
    }

    public void updateUI() {
        mPieChart.invalidate();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("test", "onResume: ");
        updateUI();
    }

    private PieDataSet configDataSet(List<PieEntry> data, @ColorRes int colorId) {
        PieDataSet set = new PieDataSet(data, "测试");
        set.setColor(getResources().getColor(colorId));
        set.setSelectionShift(20f);
        return set;
    }

}

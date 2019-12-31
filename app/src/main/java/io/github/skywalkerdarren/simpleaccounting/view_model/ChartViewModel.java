package io.github.skywalkerdarren.simpleaccounting.view_model;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.joda.time.DateTime;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.model.AppRepository;
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillStats;
import io.github.skywalkerdarren.simpleaccounting.model.entity.TypeStats;

public class ChartViewModel extends ViewModel {
    private AppRepository mRepository;
    private MutableLiveData<BillStats> mBillStats = new MutableLiveData<>();
    private MutableLiveData<PieData> mPieData = new MutableLiveData<>();

    public ChartViewModel(AppRepository repository) {
        mRepository = repository;
    }

    public LiveData<BillStats> getBillStats() {
        return mBillStats;
    }

    public LiveData<PieData> getPieData() {
        return mPieData;
    }

    public void start(DateTime startDateTime, DateTime endDateTime, boolean isExpense) {
        List<PieEntry> pieEntries = new ArrayList<>(10);
        List<Integer> colorList = new ArrayList<>(10);
        mRepository.getTypesStats(startDateTime, endDateTime, isExpense, typesStats -> {
            for (TypeStats stats : typesStats) {
                mRepository.getType(stats.getTypeId(), type -> {
                    PieEntry entry = new PieEntry(stats.getBalance().floatValue(), type.getName());
                    colorList.add(type.getColorId());
                    pieEntries.add(entry);
                    PieDataSet pieDataSet = getPieDataSet(pieEntries, colorList);
                    pieDataSet.notifyDataSetChanged();
                    PieData pieData = new PieData();
                    pieData.addDataSet(pieDataSet);
                    mPieData.setValue(pieData);
                });
            }
        });
        mRepository.getBillStats(startDateTime, endDateTime, billStats ->
                mBillStats.setValue(billStats));
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

package io.github.skywalkerdarren.simpleaccounting.view_model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.joda.time.DateTime;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.model.AppRepository;
import io.github.skywalkerdarren.simpleaccounting.model.datasource.CurrencyDataSource;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Currency;
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyInfo;

public class DiscoveryViewModel extends ViewModel {
    private final AppRepository mRepository;
    private final MutableLiveData<String> cumulativeDays = new MutableLiveData<>();
    private final MutableLiveData<String> monthOfAccountingCounts = new MutableLiveData<>();
    private final MutableLiveData<String> sumOfAccountingCounts = new MutableLiveData<>();
    private final MutableLiveData<String> monthReport = new MutableLiveData<>();
    private final MutableLiveData<String> yearReport = new MutableLiveData<>();
    private final MutableLiveData<String> weekReport = new MutableLiveData<>();
    private final MutableLiveData<String> currentCurrency = new MutableLiveData<>();
    private final MutableLiveData<String> currentCurrencyPic = new MutableLiveData<>();
    private final MutableLiveData<List<Currency>> favoriteCurrencies = new MutableLiveData<>();

    public DiscoveryViewModel(AppRepository repository) {
        String defCurrency = "CNY";
        mRepository = repository;
        mRepository.getBillsCount(count -> sumOfAccountingCounts.setValue(count + "单"));
        DateTime dateTime = new DateTime();
        mRepository.getBillsCount(dateTime.getYear(), dateTime.getMonthOfYear(),
                count -> monthOfAccountingCounts.setValue(count + "单"));
        monthReport.setValue(dateTime.getMonthOfYear() + "月报表");
        yearReport.setValue(dateTime.getYear() + "年报表");
        weekReport.setValue("第" + dateTime.getWeekOfWeekyear() + "周报表");
        mRepository.getCurrencyInfo(defCurrency, new CurrencyDataSource.LoadCurrencyInfoCallback() {
            @Override
            public void onCurrencyInfoLoaded(CurrencyInfo info) {
                currentCurrency.setValue(info.getFullNameCN());
                currentCurrencyPic.setValue(info.getFlagLocation());
            }

            @Override
            public void onDataUnavailable() {

            }
        });
        mRepository.getFavouriteCurrenciesExchangeRate(defCurrency, new CurrencyDataSource.LoadExchangeRatesCallback() {
            @Override
            public void onExchangeRatesLoaded(List<Currency> currencies) {
                favoriteCurrencies.setValue(currencies);
            }

            @Override
            public void onDataUnavailable() {

            }
        });
    }

    public MutableLiveData<String> getCurrentCurrency() {
        return currentCurrency;
    }

    public MutableLiveData<String> getCurrentCurrencyPic() {
        return currentCurrencyPic;
    }

    public MutableLiveData<String> getCumulativeDays() {
        return cumulativeDays;
    }

    public void setCumulativeDays(String days) {
        cumulativeDays.setValue(days);
    }

    public MutableLiveData<String> getMonthOfAccountingCounts() {
        return monthOfAccountingCounts;
    }

    public MutableLiveData<String> getSumOfAccountingCounts() {
        return sumOfAccountingCounts;
    }

    public MutableLiveData<String> getMonthReport() {
        return monthReport;
    }

    public MutableLiveData<String> getYearReport() {
        return yearReport;
    }

    public MutableLiveData<String> getWeekReport() {
        return weekReport;
    }

    public LiveData<List<Currency>> getFavoriteCurrencies() {
        return favoriteCurrencies;
    }
}

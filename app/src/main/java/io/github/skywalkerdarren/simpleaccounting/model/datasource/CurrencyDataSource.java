package io.github.skywalkerdarren.simpleaccounting.model.datasource;

import android.content.Context;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.model.entity.Currency;
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyInfo;

public interface CurrencyDataSource {

    void getCurrency(String name, LoadExchangeRateCallback callback);

    void getCurrencyExchangeRate(String from, String to, LoadExchangeRateCallback callback);

    void getCurrenciesExchangeRate(String from, LoadExchangeRatesCallback callback);

    void getFavouriteCurrenciesExchangeRate(String from, LoadExchangeRatesCallback callback);

    void getCurrencyInfo(String name, LoadCurrencyInfoCallback callback);

    void getFavouriteCurrenciesInfo(LoadCurrenciesInfoCallback callback);

    void updateCurrencies(Context context, UpdateCallback callback);

    void initCurrenciesAndInfos(Context context);

    void changeCurrencyPosition(Currency currencyA, Currency currencyB);

    interface LoadExchangeRateCallback {
        void onExchangeRateLoaded(Currency currency);

        void onDataUnavailable();
    }

    interface LoadExchangeRatesCallback {
        void onExchangeRatesLoaded(List<Currency> currencies);

        void onDataUnavailable();
    }

    interface LoadCurrencyInfoCallback {
        void onCurrencyInfoLoaded(CurrencyInfo info);

        void onDataUnavailable();
    }

    interface LoadCurrenciesInfoCallback {
        void onCurrenciesInfoLoaded(List<CurrencyInfo> infos);

        void onDataUnavailable();
    }

    interface UpdateCallback {
        void connectFailed(String msg);

        void updated();
    }
}

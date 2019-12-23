package io.github.skywalkerdarren.simpleaccounting.model.database;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.model.entity.AccountStats;
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillStats;
import io.github.skywalkerdarren.simpleaccounting.model.entity.TypeStats;

public interface StatsDataSource {
    void getBillsAnnualStats(int year, LoadBillsStatsCallBack callBack);

    void getBillsMonthStats(int year, int month, LoadBillsStatsCallBack callBack);

    void getBillStats(DateTime start, DateTime end, LoadBillStatsCallBack callBack);

    void getTypesStats(DateTime start, DateTime end, boolean isExpense, LoadTypesStatsCallBack callBack);

    void getTypeStats(DateTime start, DateTime end, UUID typeId, LoadTypeStatsCallBack callBack);

    void getTypeAverage(DateTime start, DateTime end, UUID typeId, LoadTypeStatsCallBack callBack);

    List<BillStats> getAnnualStats(int year);
    List<BillStats> getMonthStats(int year, int month);
    List<TypeStats> getTypesStats(DateTime start, DateTime end, boolean isExpense);
    BigDecimal getTypeStats(DateTime start, DateTime end, UUID typeId);
    List<AccountStats> getAccountStats(UUID accountId, int year);
    AccountStats getAccountStats(UUID accountId, DateTime start, DateTime end);
    BillStats getBillStats(DateTime start, DateTime end);
    BigDecimal getTypeAverage(DateTime start, DateTime end, UUID typeId);

    void getAccountsStats(UUID accountId, int year, LoadAccountsStatsCallBack callBack);

    void getAccountStats(UUID accountId, DateTime start, DateTime end, LoadAccountStatsCallBack callBack);

    interface LoadBillStatsCallBack {
        void onBillStatsLoaded(BillStats billStats);
    }

    interface LoadBillsStatsCallBack {
        void onBillStatsLoaded(List<BillStats> billsStats);
    }

    interface LoadTypeStatsCallBack {
        void onTypeStatsLoaded(TypeStats typeStats);
    }

    interface LoadTypesStatsCallBack {
        void onTypesStatsLoaded(List<TypeStats> typesStats);
    }

    interface LoadAccountStatsCallBack {
        void onAccountStatsLoaded(AccountStats accountStats);
    }

    interface LoadAccountsStatsCallBack {
        void onAccountsStatsLoaded(List<AccountStats> accountsStats);
    }
}

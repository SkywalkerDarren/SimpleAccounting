package io.github.skywalkerdarren.simpleaccounting.model.database;

import org.joda.time.DateTime;

import java.util.List;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.model.entity.AccountStats;
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillStats;
import io.github.skywalkerdarren.simpleaccounting.model.entity.TypeStats;

public interface StatsDataSource {
    void getBillsAnnualStats(int year, LoadBillsStatsCallBack callBack);

    void getBillStats(DateTime start, DateTime end, LoadBillStatsCallBack callBack);

    void getTypesStats(DateTime start, DateTime end, boolean isExpense, LoadTypesStatsCallBack callBack);

    void getTypeStats(DateTime start, DateTime end, UUID typeId, LoadTypeStatsCallBack callBack);

    void getTypeAverage(DateTime start, DateTime end, UUID typeId, LoadTypeStatsCallBack callBack);

    @Deprecated
    List<TypeStats> getTypesStats(DateTime start, DateTime end, boolean isExpense);

    @Deprecated
    BillStats getBillStats(DateTime start, DateTime end);

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
}

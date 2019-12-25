package io.github.skywalkerdarren.simpleaccounting.model.database;

import java.util.List;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillInfo;

public interface BillDataSource {

    void getBillInfoList(int year, int month, LoadBillsInfoCallBack callBack);

    void getBill(UUID id, LoadBillCallBack callBack);

    void getsBills(int year, int month, LoadBillsCallBack callBack);

    void addBill(Bill bill);

    void delBill(UUID id);

    void updateBill(Bill bill);

    void clearBill();

    interface LoadBillCallBack {
        void onBillLoaded(Bill bill);
    }

    interface LoadBillsCallBack {
        void onBillsLoaded(List<Bill> bills);
    }

    interface LoadBillsInfoCallBack {
        void onBillsInfoLoaded(List<BillInfo> billsInfo);
    }
}

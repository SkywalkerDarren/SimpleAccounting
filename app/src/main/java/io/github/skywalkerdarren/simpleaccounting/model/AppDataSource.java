package io.github.skywalkerdarren.simpleaccounting.model;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;
import io.github.skywalkerdarren.simpleaccounting.model.entity.AccountStats;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillStats;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Type;
import io.github.skywalkerdarren.simpleaccounting.model.entity.TypeStats;

public interface AppDataSource {

    Account getAccount(UUID uuid);
    List<Account> getAccounts();
    void updateAccountId(UUID uuid, Integer id);
    void delAccount(UUID uuid);
    void changePosition(Account a, Account b);

    Bill getBill(UUID id);
    List<Bill> getsBills(int year, int month);
    void addBill(Bill bill);
    void delBill(UUID id);
    void updateBill(Bill bill);
    void clearBill();

    Type getType(UUID uuid);
    List<Type> getTypes(boolean isExpense);
    void delType(UUID uuid);

    List<BillStats> getAnnualStats(int year);
    List<BillStats> getMonthStats(int year, int month);
    List<TypeStats> getTypesStats(DateTime start, DateTime end, boolean isExpense);
    BigDecimal getTypeStats(DateTime start, DateTime end, UUID typeId);
    List<AccountStats> getAccountStats(UUID accountId, int year);
    AccountStats getAccountStats(UUID accountId, DateTime start, DateTime end);
    BillStats getBillStats(DateTime start, DateTime end);
    BigDecimal getTypeAverage(DateTime start, DateTime end, UUID typeId);
}

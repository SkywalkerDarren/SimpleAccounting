package io.github.skywalkerdarren.simpleaccounting.model.database;

import java.util.List;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;

public interface AccountDataSource {

    @Deprecated
    List<Account> getAccounts();

    void getAccount(UUID uuid, LoadAccountCallBack callBack);

    void getAccounts(LoadAccountsCallBack callBack);

    void updateAccountId(UUID uuid, Integer id);

    void delAccount(UUID uuid);

    void changePosition(Account a, Account b);

    interface LoadAccountCallBack {
        void onAccountLoaded(Account account);
    }

    interface LoadAccountsCallBack {
        void onAccountsLoaded(List<Account> accounts);
    }
}

package io.github.skywalkerdarren.simpleaccounting.model.datasource;

import java.util.List;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;

public interface AccountDataSource {


    void getAccount(UUID uuid, LoadAccountCallBack callBack);

    void getAccounts(LoadAccountsCallBack callBack);

    void delAccount(UUID uuid);

    void changePosition(Account a, Account b);

    interface LoadAccountCallBack {
        void onAccountLoaded(Account account);
    }

    interface LoadAccountsCallBack {
        void onAccountsLoaded(List<Account> accounts);
    }
}

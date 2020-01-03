package io.github.skywalkerdarren.simpleaccounting.view_model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import org.joda.time.DateTime;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.model.AppRepository;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;
import io.github.skywalkerdarren.simpleaccounting.util.FormatUtil;

/**
 * 账户页vm
 *
 * @author darren
 * @date 2018/4/4
 */

public class AccountViewModel extends ViewModel {
    private final AppRepository mRepository;
    private final MutableLiveData<String> nav = new MutableLiveData<>();
    private final MutableLiveData<String> liability = new MutableLiveData<>();
    private final MutableLiveData<String> totalAssets = new MutableLiveData<>();
    private final MutableLiveData<List<Account>> accounts = new MutableLiveData<>();
    private final LiveData<String> accountSize = Transformations.map(accounts, input -> String.valueOf(input.size()));

    public AccountViewModel(AppRepository repository) {
        mRepository = repository;
    }

    public void start() {
        mRepository.getAccounts((this.accounts::setValue));
        mRepository.getBillStats(new DateTime(0), DateTime.now(), billStats -> {
            nav.setValue(FormatUtil.getNumeric(billStats.getSum()));
            liability.setValue(FormatUtil.getNumeric(billStats.getExpense()));
            totalAssets.setValue(FormatUtil.getNumeric(billStats.getIncome()));
        });
    }

    /**
     * @return 净资产
     */
    public MutableLiveData<String> getNav() {
        return nav;
    }

    /**
     * @return 负债
     */
    public MutableLiveData<String> getLiability() {
        return liability;
    }

    /**
     * @return 总资产
     */
    public MutableLiveData<String> getTotalAssets() {
        return totalAssets;
    }

    /**
     * @return 账户数目
     */
    public LiveData<String> getAccountSize() {
        return accountSize;
    }

    public void changePosition(Account a, Account b) {
        mRepository.changePosition(a, b);
    }

    public LiveData<List<Account>> getAccounts() {
        return accounts;
    }
}

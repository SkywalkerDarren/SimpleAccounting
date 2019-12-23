package io.github.skywalkerdarren.simpleaccounting.view_model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.joda.time.DateTime;

import io.github.skywalkerdarren.simpleaccounting.model.AppRepositry;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;
import io.github.skywalkerdarren.simpleaccounting.util.FormatUtil;

/**
 * 账户页vm
 *
 * @author darren
 * @date 2018/4/4
 */

public class AccountViewModel extends ViewModel {
    private AppRepositry mRepositry;
    private MutableLiveData<String> nav = new MutableLiveData<>();
    private MutableLiveData<String> liability = new MutableLiveData<>();
    private MutableLiveData<String> totalAssets = new MutableLiveData<>();
    private MutableLiveData<String> accountSize = new MutableLiveData<>();

    public AccountViewModel(AppRepositry repositry) {
        mRepositry = repositry;
    }

    public void start() {
        mRepositry.getAccounts(accounts -> accountSize.setValue(String.valueOf(accounts.size())));
        mRepositry.getBillStats(new DateTime(0), DateTime.now(), billStats -> {
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
    public MutableLiveData<String> getAccountSize() {
        return accountSize;
    }

    public void changePosition(Account a, Account b) {
        mRepositry.changePosition(a, b);
    }
}

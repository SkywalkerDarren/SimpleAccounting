package io.github.skywalkerdarren.simpleaccounting.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account
import io.github.skywalkerdarren.simpleaccounting.model.repository.AppRepository
import io.github.skywalkerdarren.simpleaccounting.util.view.FormatUtil
import org.joda.time.DateTime

/**
 * 账户页vm
 *
 * @author darren
 * @date 2018/4/4
 */
class AccountViewModel(private val mRepository: AppRepository) : ViewModel() {
    /**
     * @return 净资产
     */
    val nav = MutableLiveData<String>()
    /**
     * @return 负债
     */
    val liability = MutableLiveData<String>()
    /**
     * @return 总资产
     */
    val totalAssets = MutableLiveData<String>()
    private val accounts = MutableLiveData<List<Account>>()
    /**
     * @return 账户数目
     */
    val accountSize = Transformations.map(accounts) { input: List<Account> -> input.size.toString() }

    fun start() {
        mRepository.getAccounts { accounts: List<Account>? ->
            this@AccountViewModel.accounts.value = accounts
        }
        mRepository.getBillStats(DateTime(0), DateTime.now()) { billStats ->
            nav.value = FormatUtil.getNumeric(billStats?.sum)
            liability.value = FormatUtil.getNumeric(billStats?.expense)
            totalAssets.value = FormatUtil.getNumeric(billStats?.income)
        }
    }

    fun changePosition(a: Account, b: Account) {
        mRepository.changePosition(a, b)
    }

    fun getAccounts(): LiveData<List<Account>> {
        return accounts
    }

}
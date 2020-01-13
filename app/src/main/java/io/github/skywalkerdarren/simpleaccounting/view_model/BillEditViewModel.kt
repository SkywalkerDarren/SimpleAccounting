package io.github.skywalkerdarren.simpleaccounting.view_model

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import io.github.skywalkerdarren.simpleaccounting.model.AppRepository
import io.github.skywalkerdarren.simpleaccounting.model.datasource.AccountDataSource.LoadAccountCallBack
import io.github.skywalkerdarren.simpleaccounting.model.datasource.BillDataSource.LoadBillCallBack
import io.github.skywalkerdarren.simpleaccounting.model.datasource.TypeDataSource.LoadTypeCallBack
import io.github.skywalkerdarren.simpleaccounting.model.datasource.TypeDataSource.LoadTypesCallBack
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account
import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill
import io.github.skywalkerdarren.simpleaccounting.model.entity.Type
import org.joda.time.DateTime
import java.math.BigDecimal

/**
 * @author darren
 * @date 2018/4/4
 */
class BillEditViewModel(private val mRepository: AppRepository) : ViewModel() {

    val isExpense = MutableLiveData<Boolean>(true)
    val types = Transformations.switchMap(isExpense) {
        if (it) {
            return@switchMap expenseTypes
        } else {
            incomeTypes
        }
    }
    val balance = MutableLiveData<String?>()
    val bill = MutableLiveData<Bill>()
    var type = MutableLiveData<Type>()
        set(value) {
            field = value
            bill.value?.typeId = type.value?.uuid ?: return
        }
    var account = MutableLiveData<Account>()
        set(value) {
            field = value
            bill.value?.accountId = type.value?.uuid ?: return
        }
    private val expenseTypes = MutableLiveData<List<Type>>()
    private val incomeTypes = MutableLiveData<List<Type>>()
    private val accounts = MutableLiveData<List<Account>>()

    fun setBill(b: Bill?) {
        // empty bill
        if (b == null) {
            mRepository.getAccounts {
                val account = it?.get(0) ?: return@getAccounts
                this@BillEditViewModel.account.value = account
                mRepository.getTypes(true, object : LoadTypesCallBack {
                    override fun onTypesLoaded(types: List<Type>?) {
                        val type = types?.get(0) ?: return
                        this@BillEditViewModel.type.value = type
                        bill.value = Bill(type.uuid, account.uuid, DateTime(), type.name)
                    }
                })
            }
        } else {
            bill.value = b
            mRepository.getAccount(b.accountId, object : LoadAccountCallBack {
                override fun onAccountLoaded(account: Account) {
                    this@BillEditViewModel.account.value = account
                }
            })
            mRepository.getType(b.typeId, object : LoadTypeCallBack {
                override fun onTypeLoaded(type: Type?) {
                    this@BillEditViewModel.type.value = type ?: return
                }
            })
            balance.value = if (b.balance == null) null else b.balance.toString()
        }
    }

    /**
     * 保存账单
     */
    fun saveBill(failed: SaveFailed): Boolean {
        val finalBill = bill.value
        val finalAccount = account.value
        val finalType = type.value

        if (TextUtils.isEmpty(balance.value) ||
                finalBill == null ||
                finalAccount == null ||
                finalType == null) {
            failed.saveFailed("账单不能为空")
            return false
        }

        finalBill.accountId = finalAccount.uuid
        finalBill.typeId = finalType.uuid
        return try {
            val r = BigDecimal(balance.value)
            if (r == BigDecimal.ZERO) {
                failed.saveFailed("账单不能为0")
                return false
            }
            finalBill.balance = r

            // 刷新账单数据库
            mRepository.getBill(finalBill.uuid, object : LoadBillCallBack {
                override fun onBillLoaded(bill: Bill?) {
                    if (bill == null) {
                        mRepository.addBill(finalBill)
                    } else {
                        finalBill.id = bill.id
                        finalBill.uuid = bill.uuid
                        mRepository.updateBill(finalBill)
                    }
                }
            })
            true
        } catch (e: Exception) {
            failed.saveFailed("表达式错误")
            false
        }
    }

    fun getTypes(b: Boolean): LiveData<List<Type>> {
        return if (b) expenseTypes else incomeTypes
    }

    fun getAccounts(): LiveData<List<Account>> {
        return accounts
    }

    interface SaveFailed {
        fun saveFailed(msg: String?)
    }

    init {
        mRepository.getTypes(true, object : LoadTypesCallBack {
            override fun onTypesLoaded(types: List<Type>?) {
                expenseTypes.value = types
            }
        })
        mRepository.getTypes(false, object : LoadTypesCallBack {
            override fun onTypesLoaded(types: List<Type>?) {
                incomeTypes.value = types
            }
        })
        mRepository.getAccounts {
            this@BillEditViewModel.accounts.value = it
        }
    }
}
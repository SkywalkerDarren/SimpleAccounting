package io.github.skywalkerdarren.simpleaccounting.model.datasource

import io.github.skywalkerdarren.simpleaccounting.model.entity.Account
import java.util.*

interface AccountDataSource {
    fun getAccount(uuid: UUID, callBack: LoadAccountCallBack)
    fun getAccounts(callBack: LoadAccountsCallBack)
    fun delAccount(uuid: UUID)
    fun changePosition(a: Account, b: Account)
    interface LoadAccountCallBack {
        fun onAccountLoaded(account: Account)
    }

    interface LoadAccountsCallBack {
        fun onAccountsLoaded(accounts: List<Account>?)
    }
}
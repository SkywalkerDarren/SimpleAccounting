package io.github.skywalkerdarren.simpleaccounting.adapter.diff;

import com.chad.library.adapter.base.diff.BaseQuickDiffCallback
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account

class AccountDiff(newList: List<Account>?) : BaseQuickDiffCallback<Account>(newList) {
    override fun areItemsTheSame(oldItem: Account, newItem: Account) = oldItem.uuid == newItem.uuid

    override fun areContentsTheSame(oldItem: Account, newItem: Account) = oldItem == newItem
}

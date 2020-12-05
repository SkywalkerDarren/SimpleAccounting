package io.github.skywalkerdarren.simpleaccounting.adapter

import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import io.github.skywalkerdarren.simpleaccounting.R
import io.github.skywalkerdarren.simpleaccounting.base.BaseDataBindingAdapter
import io.github.skywalkerdarren.simpleaccounting.databinding.MenuAccountItemBinding
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account

/**
 * @author darren
 * @date 2018/4/13
 */
class AccountMenuAdapter(accounts: List<Account>?)
    : BaseDataBindingAdapter<Account, MenuAccountItemBinding>(R.layout.menu_account_item, accounts) {
    override fun convert(holder: BaseDataBindingHolder<MenuAccountItemBinding>, item: Account) {
        holder.dataBinding?.account = item
    }
}
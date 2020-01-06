package io.github.skywalkerdarren.simpleaccounting.ui.activity

import android.content.Context
import android.content.Intent
import io.github.skywalkerdarren.simpleaccounting.base.BaseFragmentActivity
import io.github.skywalkerdarren.simpleaccounting.ui.fragment.MainFragment

/**
 * 主界面
 *
 * @author darren
 * @date 2018/2/21
 */
class MainActivity : BaseFragmentActivity() {
    override fun createFragment() = MainFragment()

    companion object {
        @JvmStatic
        fun newIntent(context: Context?) = Intent(context, MainActivity::class.java)
    }
}
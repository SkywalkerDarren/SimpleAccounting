package io.github.skywalkerdarren.simpleaccounting.ui.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import io.github.skywalkerdarren.simpleaccounting.R
import io.github.skywalkerdarren.simpleaccounting.model.repository.AppRepository.Companion.getInstance
import io.github.skywalkerdarren.simpleaccounting.ui.DesktopWidget
import io.github.skywalkerdarren.simpleaccounting.util.AppExecutors
import kotlinx.android.synthetic.main.fragment_my_account.*

class MyAccountFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        about_us.setOnClickListener {
            val aboutFragment = AboutFragment.newInstance()
            gotoFragment(aboutFragment)
        }
        setting.setOnClickListener {
            val settingsFragment = SettingsFragment.newInstance()
            gotoFragment(settingsFragment)
        }
        back.setOnClickListener { requireActivity().finish() }
        feedback_layout.setOnClickListener {
            val feedBackFragment = FeedBackFragment.newInstance()
            gotoFragment(feedBackFragment)
        }
        delete_all_card_view.setOnClickListener {
            AlertDialog.Builder(requireContext())
                    .setCancelable(true)
                    .setMessage("是否删除所有账单，删除后的账单将无法恢复！")
                    .setTitle("警告")
                    .setPositiveButton(android.R.string.ok) { _: DialogInterface?, _: Int ->
                        getInstance(AppExecutors(), requireContext()).clearBill()
                        DesktopWidget.refresh(requireContext())
                    }
                    .create()
                    .show()
        }
    }

    private fun gotoFragment(fragment: Fragment) {
        requireFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, null)
                .addToBackStack(tag)
                .commit()
    }

    companion object {
        @JvmStatic
        fun newInstance(): MyAccountFragment {
            val fragment = MyAccountFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
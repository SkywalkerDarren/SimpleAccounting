package io.github.skywalkerdarren.simpleaccounting.ui.fragment

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import io.github.skywalkerdarren.simpleaccounting.R
import io.github.skywalkerdarren.simpleaccounting.databinding.FragmentAboutBinding
import io.github.skywalkerdarren.simpleaccounting.model.Demo
import io.github.skywalkerdarren.simpleaccounting.ui.DesktopWidget
import org.joda.time.DateTime

class AboutFragment : Fragment() {
    companion object {
        const val TAG = "AboutFragment"
        fun newInstance() = AboutFragment()
    }

    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_about, container, false)
        binding = FragmentAboutBinding.bind(root)
        binding.back.setOnClickListener {
            requireFragmentManager().beginTransaction().apply {
                replace(R.id.fragment_container, MyAccountFragment.newInstance())
                commit()
            }
        }
        val versionName = requireActivity().packageManager.getPackageInfo(
                requireActivity().packageName, PackageManager.GET_CONFIGURATIONS).versionName
        val debug = requireActivity().packageManager.getApplicationInfo(
                requireActivity().packageName, PackageManager.GET_META_DATA).metaData.getBoolean("DEBUG")

        binding.version.text = versionName
        binding.iv1.isLongClickable = debug
        binding.iv1.setOnLongClickListener {
            val demo = Demo(context)
            val cnt = 400
            val now = DateTime.now()
            demo.createRandomBill(cnt, now.minusMonths(6), now)
            DesktopWidget.refresh(requireActivity().applicationContext)
            Toast.makeText(context, "增加了" + cnt + "个演示数据", Toast.LENGTH_SHORT).show()
            false
        }

        binding.sourceCodeLayout.setOnClickListener {
            val intent = Intent()
            intent.data = Uri.parse("https://github.com/SkywalkerDarren/SimpleAccounting")
            intent.action = Intent.ACTION_VIEW
            startActivity(intent)
        }

        binding.myRepoLayout.setOnClickListener {
            val intent = Intent()
            intent.data = Uri.parse("https://github.com/SkywalkerDarren")
            intent.action = Intent.ACTION_VIEW
            startActivity(intent)
        }
        return root
    }
}
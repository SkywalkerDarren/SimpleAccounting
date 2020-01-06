package io.github.skywalkerdarren.simpleaccounting.ui.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.github.skywalkerdarren.simpleaccounting.R
import io.github.skywalkerdarren.simpleaccounting.databinding.FragmentBillBinding
import io.github.skywalkerdarren.simpleaccounting.ui.activity.BillEditActivity
import io.github.skywalkerdarren.simpleaccounting.ui.activity.MainActivity

class MainFragment : Fragment() {
    private val mBillListFragment = BillListFragment.newInstance()
    private val mAccountFragment = AccountFragment.newInstance()
    private val mDiscoveryFragment = DiscoveryFragment.newInstance()
    private lateinit var mViewPager: ViewPager
    private lateinit var mAddBillButton: FloatingActionButton
    private lateinit var navigation: BottomNavigationView

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_stats -> {
                mViewPager.currentItem = 0
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_bill -> {
                mViewPager.currentItem = 1
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_discovery -> {
                mViewPager.currentItem = 2
                return@OnNavigationItemSelectedListener true
            }
            else -> {
            }
        }
        false
    }

    private val listener = object : OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        override fun onPageSelected(position: Int) {
            when (position) {
                0 -> {
                    navigation.selectedItemId = R.id.navigation_stats
                    buttonAnimator(mAddBillButton, true).start()
                }
                1 -> {
                    navigation.selectedItemId = R.id.navigation_bill
                    buttonAnimator(mAddBillButton, false).start()
                }
                2 -> {
                    navigation.selectedItemId = R.id.navigation_discovery
                    buttonAnimator(mAddBillButton, true).start()
                }
                else -> {
                }
            }
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }

    fun newIntent(context: Context?): Intent? {
        return Intent(context, MainActivity::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_bill, container, false)

        val binding = FragmentBillBinding.bind(root)
        navigation = binding.navigation
        mViewPager = binding.contentViewPager
        mAddBillButton = binding.addBillFab
        // 点击增加按钮事件
        mAddBillButton.setOnClickListener { view: View ->
            val x = view.x.toInt() + view.width / 2
            val y = view.y.toInt() + view.height / 2
            val intent = BillEditActivity.newIntent(requireContext(), null, x, y)
            startActivity(intent)
        }
        val fragmentManager: FragmentManager = requireFragmentManager()
        mViewPager.adapter = object : FragmentPagerAdapter(fragmentManager,
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            /**
             * 导航栏一共3个页面 固定
             * @return 3
             */
            override fun getCount(): Int {
                return 3
            }

            override fun getItem(position: Int): Fragment {
                return when (position) {
                    0 -> mAccountFragment
                    1 -> mBillListFragment
                    2 -> mDiscoveryFragment
                    else -> throw IllegalArgumentException("no this item")
                }
            }
        }
        mViewPager.currentItem = 1
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        return root
    }

    override fun onStart() {
        super.onStart()
        mViewPager.addOnPageChangeListener(listener)
    }

    override fun onPause() {
        super.onPause()
        mViewPager.removeOnPageChangeListener(listener)
    }

    /**
     * 按钮动画
     *
     * @param view     视图
     * @param disappear 是否消失
     * @return 动画组
     */
    private fun buttonAnimator(view: View?, disappear: Boolean): AnimatorSet {
        val start: Float = if (disappear) 1f else 0f
        val end: Float = if (disappear) 0f else 1f
        val alpha = ObjectAnimator.ofFloat(view, "alpha", start, end)
        val rotation = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f)
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", start, end)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", start, end)
        val set = AnimatorSet()
        set.playTogether(alpha, rotation, scaleX, scaleY)
        set.duration = 250
        if (disappear) {
            set.interpolator = AccelerateInterpolator()
        } else {
            set.interpolator = DecelerateInterpolator()
        }
        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                if (disappear) {
                    view?.visibility = View.INVISIBLE
                }
            }

            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                if (!disappear) {
                    view?.visibility = View.VISIBLE
                }
            }
        })
        return set
    }
}
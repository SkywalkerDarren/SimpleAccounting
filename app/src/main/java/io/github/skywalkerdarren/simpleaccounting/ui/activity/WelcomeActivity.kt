package io.github.skywalkerdarren.simpleaccounting.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import io.github.skywalkerdarren.simpleaccounting.R
import io.github.skywalkerdarren.simpleaccounting.databinding.ActivityWelcomeBinding
import io.github.skywalkerdarren.simpleaccounting.model.AppRepository.Companion.getInstance
import io.github.skywalkerdarren.simpleaccounting.ui.activity.MainActivity.Companion.newIntent
import io.github.skywalkerdarren.simpleaccounting.util.AppExecutors
import io.github.skywalkerdarren.simpleaccounting.util.InjectorUtils.getCurrencyRepo
import io.github.skywalkerdarren.simpleaccounting.util.NotificationWorker.Companion.start
import io.github.skywalkerdarren.simpleaccounting.util.data.PreferenceUtil.RUN_APP_TIMES
import io.github.skywalkerdarren.simpleaccounting.util.data.PreferenceUtil.getString
import io.github.skywalkerdarren.simpleaccounting.util.data.PreferenceUtil.setString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class WelcomeActivity : AppCompatActivity() {
    private lateinit var mViewPager: ViewPager
    private lateinit var mDotLayout: LinearLayout
    private lateinit var mImageViews: MutableList<ImageView>
    private lateinit var mButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var count = getString(this, RUN_APP_TIMES, "0").toInt()
        try {
            if (count == 0) {
                start(this)
                val repository = getInstance(AppExecutors(), applicationContext)
                repository?.initDb()

                val repo = getCurrencyRepo(applicationContext)
                GlobalScope.launch {
                    withContext(Dispatchers.IO) {
                        repo.initCurrenciesAndInfos(applicationContext)
                    }
                }
            } else {
                startMainActivity()
            }
        } catch (e: Exception) {
            Log.e("Exception", "onCreate: repository" + e.message, e)
        } finally {
            count++
            setString(this, RUN_APP_TIMES, count.toString())
        }
        bind()
        setImage()
        mButton.setOnClickListener { view: View? -> startMainActivity() }
        mDotLayout.getChildAt(0).isEnabled = true
        mViewPager.adapter = WelcomeAdapter()
        mViewPager.addOnPageChangeListener(WelcomeListener())
        mViewPager.offscreenPageLimit = 2
    }

    private fun startMainActivity() {
        val intent = newIntent(this@WelcomeActivity)
        startActivity(intent)
        finish()
    }

    private fun bind() {
        val binding = DataBindingUtil.setContentView<ActivityWelcomeBinding>(this, R.layout.activity_welcome)
        mViewPager = binding.welcomePager
        mDotLayout = binding.dotLayout
        mButton = binding.enterButton
    }

    private fun setImage() {
        val productPhotos = intArrayOf(R.drawable.welcome_1,
                R.drawable.welcome_2, R.drawable.welcome_3)
        mImageViews = ArrayList(productPhotos.size)
        for (i in productPhotos) {
            val imageView = ImageView(this)
            imageView.setImageResource(i)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            mImageViews.add(imageView)
            val view = View(this)
            view.setBackgroundResource(R.drawable.indicator)
            view.isEnabled = false
            val params = LinearLayout.LayoutParams(20, 20)
            if (i != productPhotos[0]) {
                params.leftMargin = 10
            }
            mDotLayout.addView(view, params)
        }
    }

    private inner class WelcomeListener : OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        override fun onPageSelected(position: Int) { // 遍历数组让当前选中图片下的小圆点设置颜色
            for (i in mImageViews.indices) {
                mDotLayout.getChildAt(i).isEnabled = false
                mButton.visibility = View.GONE
                if (position == i) {
                    mDotLayout.getChildAt(i).isEnabled = true
                }
            }
            if (position == mImageViews.size - 1) {
                mButton.visibility = View.VISIBLE
            }
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }

    private inner class WelcomeAdapter : PagerAdapter() {
        override fun getCount(): Int {
            return mImageViews.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(mImageViews[position])
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            container.addView(mImageViews[position])
            return mImageViews[position]
        }
    }
}
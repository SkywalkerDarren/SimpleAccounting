package io.github.skywalkerdarren.simpleaccounting.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.databinding.ActivityWelcomeBinding;
import io.github.skywalkerdarren.simpleaccounting.model.AppRepository;
import io.github.skywalkerdarren.simpleaccounting.util.AppExecutors;

public class WelcomeActivity extends Activity {
    private static final String START_UP = "START_UP";
    private static final String RUN_COUNT = "RUN_COUNT";
    private ViewPager mViewPager;
    private LinearLayout mDotLayout;
    private List<ImageView> mImageViews;
    private Button mButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(START_UP, Context.MODE_PRIVATE);
        int count = preferences.getInt(RUN_COUNT, 0);
        SharedPreferences.Editor editor = preferences.edit();
        try {
            if (count == 0) {
                AppRepository repository = AppRepository.getInstance(new AppExecutors(), getApplicationContext());
                repository.initDb();
                repository.initCurrenciesAndInfos(getApplicationContext());
            } else {
                startMainActivity();
            }
        } finally {
            count++;
            editor.putInt(RUN_COUNT, count);
            editor.apply();
        }
        bind();
        setImage();
        mButton.setOnClickListener(view -> startMainActivity());
        mDotLayout.getChildAt(0).setEnabled(true);
        mViewPager.setAdapter(new WelcomeAdapter());
        mViewPager.addOnPageChangeListener(new WelcomeListener());
        mViewPager.setOffscreenPageLimit(2);
    }

    private void startMainActivity() {
        Intent intent = MainActivity.newIntent(WelcomeActivity.this);
        startActivity(intent);
        WelcomeActivity.this.finish();
    }

    private void bind() {
        ActivityWelcomeBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);
        mViewPager = binding.welcomePager;
        mDotLayout = binding.dotLayout;
        mButton = binding.enterButton;
    }

    private void setImage() {
        final int[] productPhotos = {R.drawable.welcome_1,
                R.drawable.welcome_2, R.drawable.welcome_3};
        mImageViews = new ArrayList<>(productPhotos.length);
        for (int i : productPhotos) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(i);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImageViews.add(imageView);

            View view = new View(this);
            view.setBackgroundResource(R.drawable.indicator);
            view.setEnabled(false);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            if (i != productPhotos[0]) {
                params.leftMargin = 10;
            }
            mDotLayout.addView(view, params);
        }
    }

    private class WelcomeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            // 遍历数组让当前选中图片下的小圆点设置颜色
            for (int i = 0; i < mImageViews.size(); i++) {
                mDotLayout.getChildAt(i).setEnabled(false);
                mButton.setVisibility(View.GONE);
                if (position == i) {
                    mDotLayout.getChildAt(i).setEnabled(true);
                }
            }
            if (position == mImageViews.size() - 1) {
                mButton.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private class WelcomeAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mImageViews.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
            container.removeView(mImageViews.get(position));
        }

        @NonNull
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mImageViews.get(position));
            return mImageViews.get(position);
        }
    }
}

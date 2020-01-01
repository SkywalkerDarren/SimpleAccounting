package io.github.skywalkerdarren.simpleaccounting.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;

import org.joda.time.DateTime;

import java.util.ArrayList;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.adapter.ExchangeRateAdapter;
import io.github.skywalkerdarren.simpleaccounting.base.BaseFragment;
import io.github.skywalkerdarren.simpleaccounting.databinding.FragmentDiscoveryBinding;
import io.github.skywalkerdarren.simpleaccounting.ui.activity.MyAccountActivity;
import io.github.skywalkerdarren.simpleaccounting.util.PreferenceUtil;
import io.github.skywalkerdarren.simpleaccounting.util.ViewModelFactory;
import io.github.skywalkerdarren.simpleaccounting.view_model.DiscoveryViewModel;

import static io.github.skywalkerdarren.simpleaccounting.util.PreferenceUtil.CUMULATIVE_DAYS;
import static io.github.skywalkerdarren.simpleaccounting.util.PreferenceUtil.LAST_RUN_DATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscoveryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoveryFragment extends BaseFragment {
    private LinearLayout mDotLayout;
    private ArrayList<ImageView> mImageViews;
    private FragmentDiscoveryBinding mBinding;
    private DiscoveryViewModel mViewModel;
    ExchangeRateAdapter mAdapter;


    public static DiscoveryFragment newInstance() {
        Bundle args = new Bundle();
        DiscoveryFragment fragment = new DiscoveryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_discovery, container, false);
        mBinding.myAccount.setOnClickListener(view -> {
            Intent intent = MyAccountActivity.newIntent(requireContext());
            startActivity(intent);
        });

        ViewPager viewPager = mBinding.showPager;
        mDotLayout = mBinding.dotLayout;
        setImage();
        mDotLayout.getChildAt(0).setEnabled(true);
        viewPager.setAdapter(new DiscoverAdapter());
        viewPager.addOnPageChangeListener(new DiscoverListener());
        viewPager.setOffscreenPageLimit(2);

        mAdapter = new ExchangeRateAdapter();
        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(mBinding.exchangeRateRecyclerView);
        mAdapter.enableDragItem(itemTouchHelper);
        mBinding.exchangeRateRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mBinding.exchangeRateRecyclerView.setAdapter(mAdapter);
        return mBinding.getRoot();
    }

    private void setImage() {
        final int[] productPhotos = {R.drawable.bg1,
                R.drawable.bg3, R.drawable.bg2};
        mImageViews = new ArrayList<>(productPhotos.length);
        for (int i : productPhotos) {
            ImageView imageView = new ImageView(requireContext());
            imageView.setImageResource(i);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImageViews.add(imageView);

            View view = new View(requireContext());
            view.setBackgroundResource(R.drawable.indicator);
            view.setEnabled(false);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(15, 15);
            if (i != productPhotos[0]) {
                params.leftMargin = 10;
            }
            mDotLayout.addView(view, params);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewModelFactory factory = ViewModelFactory.getInstance(requireActivity().getApplication());
        mViewModel = ViewModelProviders.of(this, factory).get(DiscoveryViewModel.class);
        mBinding.setDiscovery(mViewModel);
        mBinding.setLifecycleOwner(this);
        int days = Integer.parseInt(PreferenceUtil.getString(requireContext(), CUMULATIVE_DAYS, "1"));
        String lastRunDate = PreferenceUtil.getString(requireContext(), LAST_RUN_DATE);
        DateTime now = DateTime.now();
        DateTime today = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), 0, 0);
        if (lastRunDate == null) {
            PreferenceUtil.setString(requireContext(), LAST_RUN_DATE, today.toString());
            PreferenceUtil.setString(requireContext(), CUMULATIVE_DAYS, "1");
        } else {
            DateTime lastDate = new DateTime(lastRunDate);
            if (today.isAfter(lastDate) &&
                    lastDate.getYear() != today.getYear() ||
                    lastDate.getMonthOfYear() != today.getMonthOfYear() ||
                    lastDate.getDayOfMonth() != today.getDayOfMonth()) {
                days++;
                PreferenceUtil.setString(requireContext(), LAST_RUN_DATE, today.toString());
                PreferenceUtil.setString(requireContext(), CUMULATIVE_DAYS, String.valueOf(days));
            }
        }
        mViewModel.setCumulativeDays(days + getString(R.string.day));
    }

    @Override
    protected void updateUI() {
        mViewModel.getFavoriteCurrencies().observe(this, currencies -> {
            mAdapter.setNewData(currencies);
            mAdapter.notifyDataSetChanged();
        });
    }

    private class DiscoverListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < mImageViews.size(); i++) {
                mDotLayout.getChildAt(i).setEnabled(false);
                if (position == i) {
                    mDotLayout.getChildAt(i).setEnabled(true);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private class DiscoverAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mImageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mImageViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mImageViews.get(position));
            return mImageViews.get(position);
        }
    }
}

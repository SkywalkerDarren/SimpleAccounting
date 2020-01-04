package io.github.skywalkerdarren.simpleaccounting.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;

import org.joda.time.DateTime;

import java.util.ArrayList;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.adapter.ExchangeRateAdapter;
import io.github.skywalkerdarren.simpleaccounting.base.BaseDialogFragment;
import io.github.skywalkerdarren.simpleaccounting.base.BaseFragment;
import io.github.skywalkerdarren.simpleaccounting.databinding.FragmentDiscoveryBinding;
import io.github.skywalkerdarren.simpleaccounting.model.datasource.CurrencyDataSource;
import io.github.skywalkerdarren.simpleaccounting.ui.activity.MyAccountActivity;
import io.github.skywalkerdarren.simpleaccounting.util.PreferenceUtil;
import io.github.skywalkerdarren.simpleaccounting.util.ViewModelFactory;
import io.github.skywalkerdarren.simpleaccounting.view_model.DiscoveryViewModel;

import static io.github.skywalkerdarren.simpleaccounting.util.PreferenceUtil.CUMULATIVE_DAYS;
import static io.github.skywalkerdarren.simpleaccounting.util.PreferenceUtil.LAST_RUN_DATE;
import static io.github.skywalkerdarren.simpleaccounting.util.PreferenceUtil.LAST_UPDATE_TIMESTAMP;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscoveryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoveryFragment extends BaseFragment {
    private ExchangeRateAdapter mAdapter;
    private LinearLayout mDotLayout;
    private ArrayList<ImageView> mImageViews;
    private FragmentDiscoveryBinding mBinding;
    private DiscoveryViewModel mViewModel;

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

        mBinding.exchangeRateRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        mBinding.currencyMenu.setOnClickListener(v -> createPopupMenu(mBinding.currencyMenu));

        ViewPager viewPager = mBinding.showPager;
        mDotLayout = mBinding.dotLayout;
        setImage();
        mDotLayout.getChildAt(0).setEnabled(true);
        viewPager.setAdapter(new DiscoverAdapter());
        viewPager.addOnPageChangeListener(new DiscoverListener());
        viewPager.setOffscreenPageLimit(2);
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

    private void createPopupMenu(View view) {
        // 实例化PopupMenu对象
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        // 将R.menu.main加载到popupMenu中
        requireActivity().getMenuInflater().inflate(R.menu.currency, popupMenu.getMenu());
        // 为popupMenu菜单的菜单项单机时间绑定时间监听器
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.refresh:
                    mViewModel.refreshCurrency(requireContext(), new CurrencyDataSource.UpdateCallback() {
                        @Override
                        public void connectFailed(@Nullable String msg) {
                            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void updated() {
                            updateUI();
                        }
                    });
                    break;
                case R.id.modify_fav_currency:
                    showMultiAlertDialog();
                    break;
                case R.id.modify_current:
                    showSingleAlertDialog();
                    break;
                default:
                    break;
            }
            return true;
        });
        popupMenu.show();
    }


    private void showMultiAlertDialog() {
        CurrencyFavDialogFragment currencyFavDialogFragment = new CurrencyFavDialogFragment();
        currencyFavDialogFragment.setOnDismissListener(this::updateUI);
        currencyFavDialogFragment.show(requireFragmentManager(), "currencyFavDialogFragment");
    }

    private void showSingleAlertDialog() {
        CurrencySelectDialogFragment currencySelectDialogFragment = new CurrencySelectDialogFragment();
        currencySelectDialogFragment.setOnDismissListener(new BaseDialogFragment.DismissListener() {
            @Override
            public void callback() {
                String current = currencySelectDialogFragment.getCurrencyAdapter().getCurrent();
                mViewModel.setCurrency(current);
                updateUI();
            }
        });
        currencySelectDialogFragment.show(requireFragmentManager(), "currencySelectDialogFragment");
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewModelFactory factory = ViewModelFactory.getInstance(requireActivity().getApplication());
        mViewModel = ViewModelProviders.of(this, factory).get(DiscoveryViewModel.class);
        mBinding.setDiscovery(mViewModel);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
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

        if (mAdapter == null) {
            mAdapter = new ExchangeRateAdapter(requireContext());
            mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        }
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemDragAndSwipeCallback(mAdapter));
        itemTouchHelper.attachToRecyclerView(mBinding.exchangeRateRecyclerView);
        mAdapter.enableDragItem(itemTouchHelper);
        mAdapter.setDuration(100);
        mBinding.exchangeRateRecyclerView.setAdapter(mAdapter);

        mViewModel.getFavoriteCurrencies().observe(getViewLifecycleOwner(), currencies -> {
            if (isResumed()) {
                mAdapter.setNewData(currencies);
            }
        });
    }

    @Override
    protected void updateUI() {
        mViewModel.start();
        long timeStamp = Long.parseLong(PreferenceUtil.getString(requireContext(), LAST_UPDATE_TIMESTAMP));
        DateTime dateTime = new DateTime(timeStamp * 1000);
        mViewModel.setCurrencyDate(dateTime.toString("yyyy-MM-dd"));
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

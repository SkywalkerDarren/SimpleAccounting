package io.github.skywalkerdarren.simpleaccounting.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.databinding.ActivityBillBinding;
import io.github.skywalkerdarren.simpleaccounting.ui.fragment.AccountFragment;
import io.github.skywalkerdarren.simpleaccounting.ui.fragment.BillListFragment;
import io.github.skywalkerdarren.simpleaccounting.ui.fragment.DiscoveryFragment;

/**
 * 主界面
 *
 * @author darren
 * @date 2018/2/21
 */
public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_UPDATE_BILL = 0;
    public BillListFragment mBillListFragment;
    private ViewPager mViewPager;
    private FloatingActionButton mAddBillButton;
    private AccountFragment mAccountFragment;
    private DiscoveryFragment mDiscoveryFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_stats:
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_bill:
                    mViewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_discovery:
                    mViewPager.setCurrentItem(2);
                    return true;
                default:
                    break;
            }
            return false;
        }
    };

    public static Intent newIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        ActivityBillBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_bill);
        final BottomNavigationView navigation = binding.navigation;
        mViewPager = binding.contentViewPager;
        mAddBillButton = binding.addBillFab;

        mAccountFragment = AccountFragment.newInstance();
        mBillListFragment = BillListFragment.newInstance();
        mDiscoveryFragment = DiscoveryFragment.newInstance();

        // 点击增加按钮事件
        mAddBillButton.setOnClickListener(view -> {
            int x = (int) view.getX() + view.getWidth() / 2;
            int y = (int) view.getY() + view.getHeight() / 2;
            Intent intent = BillEditActivity.newIntent(this, null, x, y);
            startActivityForResult(intent, REQUEST_UPDATE_BILL);
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager,
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            /**
             * 导航栏一共3个页面 固定
             * @return 3
             */
            @Override
            public int getCount() {
                return 3;
            }

            @NonNull
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return mAccountFragment;
                    case 1:
                        return mBillListFragment;
                    case 2:
                        return mDiscoveryFragment;
                    default:
                        throw new IllegalArgumentException("no this item");
                }
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        navigation.setSelectedItemId(R.id.navigation_stats);
                        buttonAnimator(mAddBillButton, true).start();
                        break;
                    case 1:
                        navigation.setSelectedItemId(R.id.navigation_bill);
                        buttonAnimator(mAddBillButton, false).start();
                        break;
                    case 2:
                        navigation.setSelectedItemId(R.id.navigation_discovery);
                        buttonAnimator(mAddBillButton, true).start();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(1);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    /**
     * 按钮动画
     *
     * @param view     视图
     * @param disapper 是否消失
     * @return 动画组
     */
    private AnimatorSet buttonAnimator(View view, boolean disapper) {
        float start = disapper ? 1 : 0;
        float end = disapper ? 0 : 1;
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", start, end);
        ObjectAnimator rotation = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", start, end);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", start, end);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, rotation, scaleX, scaleY);
        set.setDuration(250);
        if (disapper) {
            set.setInterpolator(new AccelerateInterpolator());
        } else {
            set.setInterpolator(new DecelerateInterpolator());
        }
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (disapper) {
                    view.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (!disapper) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        });
        return set;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case REQUEST_UPDATE_BILL:
                mAccountFragment.onResume();
                break;
            default:
                break;
        }
    }
}

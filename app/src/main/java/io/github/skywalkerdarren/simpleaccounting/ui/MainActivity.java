package io.github.skywalkerdarren.simpleaccounting.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.Bill;

/**
 * 主界面
 *
 * @author darren
 * @date 2018/2/21
 */
public class MainActivity extends AppCompatActivity {

    private ViewPager sViewPager;

    private FloatingActionButton mAddBillButton;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_stats:
                    sViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_bill:
                    sViewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_discovery:
                    sViewPager.setCurrentItem(2);
                    return true;
                default:
                    break;
            }
            return false;
        }
    };

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        final BottomNavigationView navigation = findViewById(R.id.navigation);
        sViewPager = findViewById(R.id.content_view_pager);
        mAddBillButton = findViewById(R.id.add_bill_fab);

        // 点击增加按钮事件
        mAddBillButton.setOnClickListener(view -> {
            Bill bill = new Bill();
            int x = (int) view.getX() + view.getWidth() / 2;
            int y = (int) view.getY() + view.getHeight() / 2;
            Intent intent = BillEditActivity.newIntent(this, bill, x, y);
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(this);
            startActivity(intent, options.toBundle());
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        sViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            /**
             * 导航栏一共3个页面 固定
             * @return 3
             */
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return AccountFragment.newInstance();
                    case 1:
                        return BillListFragment.newInstance();
                    case 2:
                        return DiscoveryFragment.newInstance();
                    default:
                        return null;
                }
            }
        });
        sViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        sViewPager.setCurrentItem(1);
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

}
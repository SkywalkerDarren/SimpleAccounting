package io.github.skywalkerdarren.simpleaccounting.control;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.BillLab;

public class BillActivity extends AppCompatActivity {

    private static ViewPager sViewPager;

    private FloatingActionButton mAddBillButton;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_statics:
                    Toast.makeText(BillActivity.this, "static", Toast.LENGTH_SHORT).show();
                    mAddBillButton.setVisibility(View.INVISIBLE);
                    sViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_bill:
                    Toast.makeText(BillActivity.this, "bill", Toast.LENGTH_SHORT).show();
                    mAddBillButton.setVisibility(View.VISIBLE);
                    sViewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_discovery:
                    Toast.makeText(BillActivity.this, "discovery", Toast.LENGTH_SHORT).show();
                    mAddBillButton.setVisibility(View.INVISIBLE);
                    sViewPager.setCurrentItem(2);
                    return true;
                default:
                    break;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        final BottomNavigationView navigation = findViewById(R.id.navigation);
        sViewPager = findViewById(R.id.content_view_pager);
        mAddBillButton = findViewById(R.id.add_bill_fab);

        mAddBillButton.setOnClickListener(view -> {
            Bill bill = BillLab.createRandomBill(666);
            BillLab lab = BillLab.getInstance(getApplicationContext());
            lab.addBill(bill);
            for (Fragment fragment :
                    getSupportFragmentManager().getFragments()) {
                fragment.onResume();
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();

        sViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            /**
             * 导航栏一共3个页面 固定
             * @return
             */
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return StaticsFragment.newInstance();
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
                switch (position) {
                    case 0:
                        mAddBillButton.setAlpha(positionOffset);
                        break;
                    case 1:
                        mAddBillButton.setAlpha(1 - positionOffset);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        navigation.setSelectedItemId(R.id.navigation_statics);
                        mAddBillButton.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        navigation.setSelectedItemId(R.id.navigation_bill);
                        mAddBillButton.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        navigation.setSelectedItemId(R.id.navigation_discovery);
                        mAddBillButton.setVisibility(View.INVISIBLE);
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

}

package io.github.skywalkerdarren.simpleaccounting.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.adapter.ClassifyAdapter;
import io.github.skywalkerdarren.simpleaccounting.base.BaseFragment;
import io.github.skywalkerdarren.simpleaccounting.model.StatsLab;

import static io.github.skywalkerdarren.simpleaccounting.ui.PeriodDialogFragment.EXTRA_END_DATE;
import static io.github.skywalkerdarren.simpleaccounting.ui.PeriodDialogFragment.EXTRA_START_DATE;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassifyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassifyFragment extends BaseFragment implements View.OnClickListener {

    private static final int REQUEST_PERIOD = 0;
    private ImageView mBackImageView;
    private ImageView mMoreImageView;
    private ImageView mCustomImageView;
    private TextView mDateTextView;
    private RecyclerView mRecyclerView;
    private ViewPager mViewPager;
    private ImageView mChangeImageView;
    private DateTime mDateTimeStart;
    private DateTime mDateTimeEnd;
    private boolean mIsExpense;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_classify, container, false);
        mBackImageView = view.findViewById(R.id.back_image_view);
        mMoreImageView = view.findViewById(R.id.more_image_view);
        mCustomImageView = view.findViewById(R.id.custom_image_view);
        mDateTextView = view.findViewById(R.id.date_text_view);
        mRecyclerView = view.findViewById(R.id.classify_recycler_view);
        mViewPager = view.findViewById(R.id.classify_view_pager);

        mDateTimeEnd = DateTime.now();
        mDateTimeStart = mDateTimeEnd.minusMonths(1);

        mBackImageView.setOnClickListener(this);
        mMoreImageView.setOnClickListener(this);
        mCustomImageView.setOnClickListener(this);
        mDateTextView.setOnClickListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mIsExpense = true;
        updateUI();

        return view;
    }

    @NonNull
    private String getDateString() {
        return mDateTimeStart.toString("yyyy年MM月dd日") + " - " + mDateTimeEnd.toString("yyyy年MM月dd日");
    }

    private void setStatsData(boolean t, ClassifyAdapter adapter) {
        List<StatsLab.TypeStats> typeStats = StatsLab.getInstance(getContext())
                .getTypeStats(mDateTimeStart, mDateTimeEnd, t);
        adapter.setNewData(typeStats);
        adapter.openLoadAnimation(t ?
                BaseQuickAdapter.SLIDEIN_RIGHT : BaseQuickAdapter.SLIDEIN_LEFT);
        adapter.notifyDataSetChanged();
    }

    private View EmptyView() {
        View v = LayoutInflater.from(getContext())
                .inflate(R.layout.empty_stats, null, false);
        return v;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ClassifyFragment.
     */
    public static ClassifyFragment newInstance() {
        ClassifyFragment fragment = new ClassifyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View view) {
        Period period = new Period(mDateTimeStart, mDateTimeEnd);
        switch (view.getId()) {
            case R.id.back_image_view:
                mDateTimeStart = mDateTimeStart.minus(period);
                mDateTimeEnd = mDateTimeEnd.minus(period);
                updateUI();
                break;
            case R.id.more_image_view:
                mDateTimeStart = mDateTimeStart.plus(period);
                mDateTimeEnd = mDateTimeEnd.plus(period);
                updateUI();
                break;
            case R.id.date_text_view:
            case R.id.custom_image_view:
                PeriodDialogFragment periodDialog = PeriodDialogFragment
                        .newInstance(mDateTimeStart, mDateTimeEnd);
                periodDialog.setTargetFragment(this, REQUEST_PERIOD);
                periodDialog.show(getFragmentManager(), "periodDialog");
            default:
                break;
        }
    }

    @Override
    protected void updateUI() {
        List<StatsLab.TypeStats> typeStats = StatsLab.getInstance(getContext())
                .getTypeStats(mDateTimeStart, mDateTimeEnd, mIsExpense);
        ClassifyAdapter adapter = new ClassifyAdapter(typeStats);
        adapter.setEmptyView(EmptyView());
        mRecyclerView.setAdapter(adapter);

        mViewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return PieChartFragment.newInstance(mDateTimeStart, mDateTimeEnd, false);
                    case 1:
                        return PieChartFragment.newInstance(mDateTimeStart, mDateTimeEnd, true);
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 2;
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
                        setStatsData(false, adapter);
                        mIsExpense = false;
                        break;
                    case 1:
                        setStatsData(true, adapter);
                        mIsExpense = true;
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(mIsExpense ? 1 : 0);
        mDateTextView.setText(getDateString());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_PERIOD:
                mDateTimeStart = (DateTime) data.getSerializableExtra(EXTRA_START_DATE);
                mDateTimeEnd = (DateTime) data.getSerializableExtra(EXTRA_END_DATE);
                updateUI();
                break;
            default:
                break;
        }
    }
}

package io.github.skywalkerdarren.simpleaccounting.ui.fragment;


import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.joda.time.DateTime;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.adapter.ClassifyAdapter;
import io.github.skywalkerdarren.simpleaccounting.base.BaseFragment;
import io.github.skywalkerdarren.simpleaccounting.databinding.EmptyStatsBinding;
import io.github.skywalkerdarren.simpleaccounting.databinding.FragmentClassifyBinding;
import io.github.skywalkerdarren.simpleaccounting.model.StatsLab;
import io.github.skywalkerdarren.simpleaccounting.view_model.ClassifyViewModel;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassifyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassifyFragment extends BaseFragment {

    private static final int REQUEST_PERIOD = 0;
    private ViewPager mViewPager;
    private ClassifyAdapter mClassifyAdapter;
    private ImageView mToIncomeImageView;
    private ImageView mToExpenseImageView;

    ClassifyViewModel mViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentClassifyBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_classify, container, false);
        mViewModel = new ClassifyViewModel(DateTime.now(), getContext());
        mViewModel.setExpense(true);

        mViewPager = binding.classifyViewPager;
        mToIncomeImageView = binding.toIncomeImageView;
        mToExpenseImageView = binding.toExpenseImageView;
        mToIncomeImageView.setVisibility(View.VISIBLE);
        mToExpenseImageView.setVisibility(View.VISIBLE);


        mToIncomeImageView.setOnClickListener(view -> {
            mViewPager.setCurrentItem(0);
        });

        mToExpenseImageView.setOnClickListener(view -> {
            mViewPager.setCurrentItem(1);
        });

        binding.backImageView.setOnClickListener(view -> {
            mViewModel.back();
            setStatsData(mViewPager.getCurrentItem() == 1, mClassifyAdapter);
            updateUI();
        });
        binding.moreImageView.setOnClickListener(view -> {
            mViewModel.more();
            setStatsData(mViewPager.getCurrentItem() == 1, mClassifyAdapter);
            updateUI();
        });
        binding.customImageView.setOnClickListener(view -> customDialog());
        binding.dateTextView.setOnClickListener(view -> customDialog());

        binding.classifyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mClassifyAdapter = new ClassifyAdapter(mViewModel.getStatsList());
        mClassifyAdapter.setDuration(100);
        mClassifyAdapter.setEmptyView(emptyView());
        binding.classifyRecyclerView.setAdapter(mClassifyAdapter);
        binding.setClassify(mViewModel);

        return binding.getRoot();
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

    private void customDialog() {
        PeriodDialogFragment periodDialog = PeriodDialogFragment
                .newInstance(mViewModel.getStart(), mViewModel.getEnd());
        periodDialog.setTargetFragment(this, REQUEST_PERIOD);
        periodDialog.show(getFragmentManager(), "periodDialog");
    }

    @Override
    protected void updateUI() {
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return PieChartFragment.newInstance(
                                mViewModel.getStart(), mViewModel.getEnd(), false);
                    case 1:
                        return PieChartFragment.newInstance(
                                mViewModel.getStart(), mViewModel.getEnd(), true);
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
        mViewPager.clearOnPageChangeListeners();
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        setStatsData(false, mClassifyAdapter);
                        break;
                    case 1:
                        setStatsData(true, mClassifyAdapter);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(mViewModel.isExpense() ? 1 : 0);
        mClassifyAdapter.notifyDataSetChanged();
    }

    /**
     * @param toIncome true则转到收入页，当前为支出页
     */
    private void changeImageView(boolean toIncome) {
        Animator disappear = AnimatorInflater.loadAnimator(getContext(), toIncome ? R.animator.to_left_disappear : R.animator.to_right_disappear);
        Animator appear = AnimatorInflater.loadAnimator(getContext(), toIncome ? R.animator.to_left_appear : R.animator.to_right_appear);
        disappear.setTarget(toIncome ? mToExpenseImageView : mToIncomeImageView);
        appear.setTarget(toIncome ? mToIncomeImageView : mToExpenseImageView);

        AnimatorSet set = new AnimatorSet();
        set.playSequentially(disappear, appear);
        set.start();
    }

    private void setStatsData(boolean t, ClassifyAdapter adapter) {
        changeImageView(t);
        mViewModel.setExpense(t);
        List<StatsLab.TypeStats> list = mViewModel.getStatsList();
        adapter.setNewData(list);
        adapter.openLoadAnimation(t ?
                BaseQuickAdapter.SLIDEIN_RIGHT : BaseQuickAdapter.SLIDEIN_LEFT);
    }

    private View emptyView() {
        return EmptyStatsBinding.inflate(LayoutInflater.from(getContext())).getRoot();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_PERIOD:
                DateTime start = (DateTime) data.getSerializableExtra(PeriodDialogFragment.EXTRA_START_DATE);
                DateTime end = (DateTime) data.getSerializableExtra(PeriodDialogFragment.EXTRA_END_DATE);
                mViewModel.setDate(start, end);
                updateUI();
                break;
            default:
                break;
        }
    }
}

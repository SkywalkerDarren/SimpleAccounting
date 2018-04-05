package io.github.skywalkerdarren.simpleaccounting.ui;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.base.BaseFragment;
import io.github.skywalkerdarren.simpleaccounting.databinding.FragmentBillDetailBinding;
import io.github.skywalkerdarren.simpleaccounting.model.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.BillLab;
import io.github.skywalkerdarren.simpleaccounting.view_model.BillDetailViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BillDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 * 账单细节fragment
 *
 * @author darren
 * @date 2018/2/21
 */
public class BillDetailFragment extends BaseFragment {
    private static final String ARG_BILL = "bill";
    private static final String ARG_CX = "cx";
    private static final String ARG_CY = "cy";
    private static final String ARG_START_COLOR = "startColor";

    private Bill mBill;
    private ActionBar mActionBar;
    private FragmentBillDetailBinding mBinding;
    private static final int REQUEST_DESTROY = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) {
            return;
        }
        setHasOptionsMenu(true);
        mBill = (Bill) getArguments().getSerializable(ARG_BILL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_bill_detail, container, false);
        mActionBar = initToolbar(R.id.toolbar, mBinding.toolbar);

        mActionBar.setTitle(R.string.detail_bill);
        mActionBar.setDisplayHomeAsUpEnabled(true);

        updateUI();

        // 启动动画
        enterViewAnimator(mBinding.getRoot());
        return mBinding.getRoot();
    }

    /**
     * 启动动画
     */
    private void enterViewAnimator(View view) {
        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                v.removeOnLayoutChangeListener(this);
                int cx = getArguments().getInt(ARG_CX);
                int cy = getArguments().getInt(ARG_CY);
                int startColor = getArguments().getInt(ARG_START_COLOR);

                // get the hypothenuse so the radius is from one corner to the other
                int radius = (int) Math.hypot(right, bottom);

                // 圆形动画
                Animator reveal = ViewAnimationUtils
                        .createCircularReveal(v, cx, cy - 45, 0, radius);
                reveal.setInterpolator(new DecelerateInterpolator());
                // 颜色渐变动画
                ObjectAnimator colorChange = new ObjectAnimator();
                colorChange.setIntValues(getResources().getColor(startColor),
                        getResources().getColor(R.color.transparent));
                colorChange.setEvaluator(new ArgbEvaluator());
                colorChange.addUpdateListener(valueAnimator -> view
                        .setBackgroundColor((Integer) valueAnimator.getAnimatedValue()));
                // 动画集合
                AnimatorSet set = new AnimatorSet();
                set.playTogether(reveal, colorChange);
                set.setDuration(350);
                set.setInterpolator(new AccelerateInterpolator());
                set.start();
            }
        });
    }

    /**
     * @param bill 存储的账单
     * @param cx   x中心点
     * @param cy   y中心点
     */
    public static BillDetailFragment newInstance(Bill bill, int cx, int cy, @ColorRes int startColor) {
        BillDetailFragment fragment = new BillDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_BILL, bill);
        args.putInt(ARG_CX, cx);
        args.putInt(ARG_CY, cy);
        args.putInt(ARG_START_COLOR, startColor);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            case R.id.del:
                DeleteBillAlertDialog dialog = DeleteBillAlertDialog.newInstance(mBill.getId());
                dialog.setTargetFragment(this, REQUEST_DESTROY);
                dialog.show(getFragmentManager(), "alertDialog");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void updateUI() {
        mBill = BillLab.getInstance(getActivity()).getBill(mBill.getId());
        mBinding.setDetail(new BillDetailViewModel(mBill, getActivity()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_DESTROY:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getActivity().finish();
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }
}

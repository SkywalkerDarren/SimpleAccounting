package io.github.skywalkerdarren.simpleaccounting.ui;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.BillLab;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BillDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BillDetailFragment extends BaseFragment {
    private static final String ARG_BILL = "bill";
    private static final String ARG_CX = "cx";
    private static final String ARG_CY = "cy";
    private static final String ARG_START_COLOR = "startColor";

    // TODO: Rename and change types of parameters
    private Bill mBill;
    private ImageView mTypeImageView;
    private TextView mTitleTextView;
    private TextView mDateTextView;
    private TextView mBalanceTextView;
    private TextView mRemarkTextView;
    private FloatingActionButton mEditFab;
    private ActionBar mActionBar;
    private static final int REQUEST_DESTROY = 0;

    /**
     * @param bill 存储的账单
     * @param cx x中心点
     *@param cy y中心点
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) {
            return;
        }
        setHasOptionsMenu(true);
        mBill = (Bill) getArguments().getSerializable(ARG_BILL);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill_detail, container, false);
        mTypeImageView = view.findViewById(R.id.type_image_view);
        mBalanceTextView = view.findViewById(R.id.balance_edit_text);
        mDateTextView = view.findViewById(R.id.bill_date_text_view);
        mTitleTextView = view.findViewById(R.id.title_text_view);
        mRemarkTextView = view.findViewById(R.id.bill_remark_text_view);
        mEditFab = view.findViewById(R.id.bill_edit_fab);
        mActionBar = initToolbar(R.id.toolbar, view);

        mActionBar.setTitle(R.string.detail_bill);
        mActionBar.setDisplayHomeAsUpEnabled(true);

        updateUI();

        // 编辑按钮点击事件
        mEditFab.setOnClickListener(view1 -> {
            Intent intent = BillEditActivity.newIntent(getActivity(), mBill);
            int[] location = new int[2];
            view1.getLocationInWindow(location);
            intent.putExtra(BillEditActivity.EXTRA_CENTER_X, (int) view1.getX() + view1.getWidth() / 2);
            intent.putExtra(BillEditActivity.EXTRA_CENTER_Y, (int) view1.getY() + view1.getHeight() / 2);
            intent.putExtra(BillEditActivity.EXTRA_TRANS, BillEditActivity.CIRCLE_UP);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity());
            startActivity(intent, options.toBundle());
        });

        // 启动动画
        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop,
                                       int oldRight, int oldBottom) {
                v.removeOnLayoutChangeListener(this);
                int cx = getArguments().getInt(ARG_CX);
                int cy = getArguments().getInt(ARG_CY);
                int startColor = getArguments().getInt(ARG_START_COLOR);

                // get the hypothenuse so the radius is from one corner to the other
                int radius = (int) Math.hypot(right, bottom);

                Animator reveal = ViewAnimationUtils.createCircularReveal(v, cx, cy - 45, 0, radius);
                reveal.setInterpolator(new DecelerateInterpolator());
                reveal.setDuration(1000);
                ObjectAnimator colorChange = new ObjectAnimator();
                colorChange.setIntValues(getResources().getColor(startColor),
                        getResources().getColor(R.color.transparent));
                colorChange.setEvaluator(new ArgbEvaluator());
                colorChange.addUpdateListener(valueAnimator -> view.setBackgroundColor((Integer) valueAnimator.getAnimatedValue()));
                AnimatorSet set = new AnimatorSet();
                set.playTogether(reveal, colorChange);
                set.setDuration(1000);
                set.setInterpolator(new DecelerateInterpolator());
                set.start();
            }
        });
        return view;
    }

    private void updateUI() {
        mTypeImageView.setImageResource(mBill.getTypeResId());
        mBalanceTextView.setText(mBill.getBalance().toString());
        mBalanceTextView.setTextColor(mBill.isExpense() ?
                Color.rgb(0xFF, 0x45, 0x00) :
                Color.rgb(0xAD, 0xFF, 0x2F));
        mRemarkTextView.setText(mBill.getRemark());
        mTitleTextView.setText(mBill.getName());
        mDateTextView.setText(mBill.getDate().toString("yyyy-MM-dd hh:mm"));
    }

    @Override
    public void onResume() {
        super.onResume();
        mBill = BillLab.getInstance(getActivity()).getBill(mBill.getId());
        updateUI();
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

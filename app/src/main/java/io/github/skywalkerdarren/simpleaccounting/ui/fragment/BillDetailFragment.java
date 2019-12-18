package io.github.skywalkerdarren.simpleaccounting.ui.fragment;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.AppBarLayout;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.base.BaseAppBarStateChangeListener;
import io.github.skywalkerdarren.simpleaccounting.base.BaseFragment;
import io.github.skywalkerdarren.simpleaccounting.databinding.FragmentBillDetailBinding;
import io.github.skywalkerdarren.simpleaccounting.model.AppRepositry;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill;
import io.github.skywalkerdarren.simpleaccounting.util.DpConvertUtils;
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
    private static final String TAG = "BillDetailFragment";

    private static final String ARG_BILL = "bill";
    private static final String ARG_CX = "cx";
    private static final String ARG_CY = "cy";
    private static final String ARG_START_COLOR = "startColor";
    private final static float EXPAND_TYPE_SIZE_DP = 80f;
    private final static float COLLAPSED_TYPE_SIZE_DP = 32f;
    private static final int REQUEST_DESTROY = 0;
    private Bill mBill;
    private Space mSpace;
    private TextView mTypeTitleTextView;
    private AppBarLayout mAppBarLayout;
    private FragmentBillDetailBinding mBinding;
    private BaseAppBarStateChangeListener mAppBarStateChangeListener;
    private int[] mTypePoint = new int[2],
            mSpacePoint = new int[2],
            mToolbarTextPoint = new int[2],
            mTitleTextViewPoint = new int[2];
    private TextView mToolbarTextView;
    private float mTitleTextSize;
    private ImageView mTypeImageView;

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
        mSpace = mBinding.space;
        mTypeTitleTextView = mBinding.titleToolbarTextView;
        mTitleTextSize = mTypeTitleTextView.getTextSize();
        mTypeImageView = mBinding.typeToolbarImageView;
        mToolbarTextView = mBinding.toolbarTitle;
        mAppBarLayout = mBinding.appbar;
        Toolbar toolbar = mBinding.toolbar;
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setToolbarChange();


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

    private void setToolbarChange() {
        mAppBarStateChangeListener = new BaseAppBarStateChangeListener() {

            @Override
            public void onStateChanged(AppBarLayout appBarLayout,
                                       BaseAppBarStateChangeListener.State state) {
            }

            @Override
            public void onOffsetChanged(BaseAppBarStateChangeListener.State state, float offset) {
                translationView(offset);
            }
        };
        mAppBarLayout.addOnOffsetChangedListener(mAppBarStateChangeListener);
    }

    private void translationView(float offset) {
        Log.d(TAG, "translationView() called with: offset = [" + offset + "]");
        Log.d(TAG, "translationView: mTypePoint" + mTypePoint[0] + " " + mTypePoint[1]);
        Log.d(TAG, "translationView: mSpacePoint" + mSpacePoint[0] + " " + mSpacePoint[1]);
        // 设定图像偏移
        float xOffset = -(mTypePoint[0] - mSpacePoint[0]) * offset;
        float yOffset = -(mTypePoint[1] - mSpacePoint[1]) * offset;
        // 设定文字偏移
        float xTitleOffset = -(mTitleTextViewPoint[0] - mToolbarTextPoint[0]) * offset;
        float yTitleOffset = -(mTitleTextViewPoint[1] - mToolbarTextPoint[1]) * offset;
        // 设定新大小
        int newSize = DpConvertUtils.convertDpToPixelSize(
                EXPAND_TYPE_SIZE_DP - (EXPAND_TYPE_SIZE_DP - COLLAPSED_TYPE_SIZE_DP) * offset,
                getContext());
        float newTextSize =
                mTitleTextSize - (mTitleTextSize - mToolbarTextView.getTextSize()) * offset;
        Log.d(TAG, "translationView: xOffset:" + xOffset + "yOffset:" + yOffset + "newSize:" + newSize);
        Log.d(TAG, "translationView: xTitleOffset:" + xTitleOffset + "yTitleOffset:" + yTitleOffset + "newTextSize:" + newTextSize);
        mTypeImageView.getLayoutParams().width = newSize;
        mTypeImageView.getLayoutParams().height = newSize;
        mTypeImageView.setTranslationX(xOffset);
        mTypeImageView.setTranslationY(yOffset);
        mTypeTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
        mTypeTitleTextView.setTranslationX(xTitleOffset);
        mTypeTitleTextView.setTranslationY(yTitleOffset);
    }

    private void clearAnim() {
        mTypeImageView.setTranslationX(0);
        mTypeImageView.setTranslationY(0);
        mTypeTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleTextSize);
        mTypeTitleTextView.setTranslationX(0);
        mTypeTitleTextView.setTranslationY(0);
    }

    public void resetPoints() {
        clearAnim();

        int avatarSize = DpConvertUtils.convertDpToPixelSize(EXPAND_TYPE_SIZE_DP, getContext());
        mTypeImageView.getLocationOnScreen(mTypePoint);
        Log.d(TAG, "resetPoints: mTypePoint" + mTypePoint[0] + " " + mTypePoint[1]);
        mTypePoint[0] -= (avatarSize - mTypeImageView.getWidth()) / 2;
        mSpace.getLocationOnScreen(mSpacePoint);
        Log.d(TAG, "resetPoints: mSpacePoint" + mSpacePoint[0] + " " + mSpacePoint[1]);
        mToolbarTextView.getLocationOnScreen(mToolbarTextPoint);
        Log.d(TAG, "resetPoints: mToolbarTextPoint" + mToolbarTextPoint[0] + " " + mToolbarTextPoint[1]);
        mToolbarTextPoint[0] += DpConvertUtils.convertDpToPixelSize(16, getContext());
        mTypeTitleTextView.post(() -> {
            mTypeTitleTextView.getLocationOnScreen(mTitleTextViewPoint);
            translationView(mAppBarStateChangeListener.getCurrentOffset());
        });
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
                DeleteBillAlertDialog dialog = DeleteBillAlertDialog.newInstance(mBill.getUUID());
                dialog.setTargetFragment(this, REQUEST_DESTROY);
                dialog.show(getFragmentManager(), "alertDialog");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void updateUI() {
        mBill = AppRepositry.getInstance(getActivity()).getBill(mBill.getUUID());
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

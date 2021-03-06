package io.github.skywalkerdarren.simpleaccounting.ui.fragment;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.AppBarLayout;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.base.BaseAppBarStateChangeListener;
import io.github.skywalkerdarren.simpleaccounting.databinding.FragmentBillDetailBinding;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill;
import io.github.skywalkerdarren.simpleaccounting.ui.activity.BillEditActivity;
import io.github.skywalkerdarren.simpleaccounting.util.ViewModelFactory;
import io.github.skywalkerdarren.simpleaccounting.util.view.DpConvertUtils;
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
public class BillDetailFragment extends Fragment {

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
    private BillDetailViewModel mViewModel;

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
        setHasOptionsMenu(true);
        mBill = (Bill) requireArguments().getSerializable(ARG_BILL);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mBinding.billEditFab.setOnClickListener(view -> {
            int[] location = new int[2];
            view.getLocationInWindow(location);
            int x = (int) view.getX() + view.getWidth() / 2;
            int y = (int) view.getY() + view.getHeight() / 2;
            Intent intent = BillEditActivity.newIntent(requireContext(), mBill, x, y);
            //noinspection unchecked
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity());
            startActivity(intent, options.toBundle());
        });
        setToolbarChange();
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
                assert getArguments() != null;
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
                colorChange.setIntValues(ContextCompat.getColor(requireContext(), startColor),
                        ContextCompat.getColor(requireContext(), R.color.transparent));
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
        // 设定图像偏移
        float xOffset = -(mTypePoint[0] - mSpacePoint[0]) * offset;
        float yOffset = -(mTypePoint[1] - mSpacePoint[1]) * offset;
        // 设定文字偏移
        float xTitleOffset = -(mTitleTextViewPoint[0] - mToolbarTextPoint[0]) * offset;
        float yTitleOffset = -(mTitleTextViewPoint[1] - mToolbarTextPoint[1]) * offset;
        // 设定新大小
        int newSize = DpConvertUtils.convertDpToPixelSize(
                EXPAND_TYPE_SIZE_DP - (EXPAND_TYPE_SIZE_DP - COLLAPSED_TYPE_SIZE_DP) * offset,
                requireContext());
        float newTextSize =
                mTitleTextSize - (mTitleTextSize - mToolbarTextView.getTextSize()) * offset;
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

        int avatarSize = DpConvertUtils.convertDpToPixelSize(EXPAND_TYPE_SIZE_DP, requireContext());
        mTypeImageView.getLocationOnScreen(mTypePoint);
        mTypePoint[0] -= (avatarSize - mTypeImageView.getWidth()) / 2;
        mSpace.getLocationOnScreen(mSpacePoint);
        mToolbarTextView.getLocationOnScreen(mToolbarTextPoint);
        mToolbarTextPoint[0] += DpConvertUtils.convertDpToPixelSize(16, requireContext());
        mTypeTitleTextView.post(() -> {
            mTypeTitleTextView.getLocationOnScreen(mTitleTextViewPoint);
            translationView(mAppBarStateChangeListener.getCurrentOffset());
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                requireActivity().onBackPressed();
                return true;
            case R.id.del:
                DeleteBillAlertDialog dialog = DeleteBillAlertDialog.newInstance(mBill.getUuid());
                dialog.setViewModel(mViewModel);
                dialog.setTargetFragment(this, REQUEST_DESTROY);
                dialog.show(requireFragmentManager(), "alertDialog");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelFactory.getInstance(requireActivity().getApplication())
                .obtainViewModel(this, BillDetailViewModel.class);
        mBinding.setDetail(mViewModel);
        mBinding.setLifecycleOwner(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.start(mBill);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_DESTROY) {
            if (resultCode == Activity.RESULT_OK) {
                requireActivity().finish();
            }
        }
    }
}

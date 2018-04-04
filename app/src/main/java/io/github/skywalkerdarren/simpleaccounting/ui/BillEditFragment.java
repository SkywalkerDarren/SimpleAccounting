package io.github.skywalkerdarren.simpleaccounting.ui;


import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.transition.Fade;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.joda.time.DateTime;

import java.util.List;

import co.ceryle.segmentedbutton.SegmentedButtonGroup;
import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.adapter.TypeAdapter;
import io.github.skywalkerdarren.simpleaccounting.databinding.FragmentBillEditBinding;
import io.github.skywalkerdarren.simpleaccounting.model.Account;
import io.github.skywalkerdarren.simpleaccounting.model.AccountLab;
import io.github.skywalkerdarren.simpleaccounting.model.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.Type;
import io.github.skywalkerdarren.simpleaccounting.model.TypeLab;
import io.github.skywalkerdarren.simpleaccounting.view_model.BillEditViewModel;

/**
 * 账单编辑或创建的fragment
 *
 * @author darren
 * @date 2018/2/21
 */
public class BillEditFragment extends BaseFragment {
    private static final String ARG_BILL = "bill";
    private static final int REQUEST_DATE = 0;
    private static final String ARG_CX = "cx";
    private static final String ARG_CY = "cy";
    private Bill mBill;
    private Type mType;
    // TODO: 2018/4/2 使账单可编辑
    private Account mAccount;

    private FragmentBillEditBinding mBinding;
    private BillEditViewModel mViewModel;

    private CardView mAccountTypeCardView;
    private EditText mBalanceEditText;
    private EditText mRemarkEditText;
    private RecyclerView mTypeRecyclerView;
    private SegmentedButtonGroup mTypeSbg;
    private NumPad mNumPad;
    private ImageView mDateImageView;
    private ImageView mTypeImageView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEnterTransition(new Explode());
        setExitTransition(new Fade());
        if (getArguments() != null) {
            mBill = (Bill) getArguments().getSerializable(ARG_BILL);
        }
        setHasOptionsMenu(true);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_bill_edit, container, false);
        mDateImageView = mBinding.dateImageView;
        mRemarkEditText = mBinding.remarkEditText;
        mBalanceEditText = mBinding.balanceEditText;
        mTypeRecyclerView = mBinding.typeListRecyclerView;
        mNumPad = mBinding.numKeyView;
        mTypeSbg = mBinding.typeSbg;
        mAccountTypeCardView = mBinding.accountTypeCardView;
        mTypeImageView = mBinding.typeImageView;

        mViewModel = new BillEditViewModel(mBill, getContext());

        // 自定义导航栏
        ActionBar actionBar = initToolbar(R.id.toolbar, mBinding.getRoot());
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);

        // 配置适配器
        TypeAdapter adapter = new TypeAdapter(null);
        adapter.openLoadAnimation(view14 -> {
                    Animator animator = AnimatorInflater.loadAnimator(getActivity(),
                            R.animator.type_item_appear);
                    animator.setTarget(view14);
                    return new Animator[]{animator};
                }
        );
        adapter.setOnItemClickListener((adapter1, view12, position) -> clickTypeItem(adapter1, position));
        adapter.setOnItemChildClickListener((adapter12, view17, position) -> clickTypeItem(adapter12, position));
        mTypeRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));

        // 配置选择按钮
        mTypeSbg.setOnClickedButtonListener(position -> {
            List<Type> types = TypeLab
                    .getInstance(getContext()).getTypes(position == 1);
            adapter.setNewData(types);
            mType = types.get(0);
            typeImageAnimator();
            adapter.notifyDataSetChanged();
        });

        // 配置账单信息
        configBill(adapter);
        mTypeRecyclerView.setAdapter(adapter);

        if (!TextUtils.isEmpty(mViewModel.getRemark())) {
            mRemarkEditText.setText(mViewModel.getRemark());
        }

        mNumPad.setStrReceiver(mBalanceEditText);
        mBalanceEditText.setOnTouchListener((view16, motionEvent) -> {
            mRemarkEditText.clearFocus();
            mNumPad.hideSysKeyboard();
            new Handler().postDelayed(() -> mNumPad.showKeyboard(), 200);
            return true;
        });
        mBalanceEditText.setSelection(mBalanceEditText.getText().length());
        mBalanceEditText.setOnFocusChangeListener((view15, b) -> {
            if (!b) {
                mNumPad.hideKeyboard();
            }
        });

        mRemarkEditText.setOnClickListener((view1) -> mNumPad.hideKeyboard());

        mDateImageView.setOnClickListener(view13 -> {
            DatePickerFragment datePicker = DatePickerFragment.newInstance(mViewModel.getDate());
            datePicker.setTargetFragment(this, REQUEST_DATE);
            datePicker.show(getFragmentManager(), "datePicker");
        });

        // TODO: 2018/4/2 监听账户点击
        mAccountTypeCardView.setOnClickListener(view18 -> {

        });

        mBinding.setEdit(mViewModel);
        viewEnterAnimation(mBinding.getRoot());
        return mBinding.getRoot();
    }

    /**
     * 配置初始账单，将账单信息绑定到视图
     */
    private void configBill(TypeAdapter adapter) {
        if (mViewModel.getDate() == null) {
            // 创建账单(日期不存在则一定是刚创建的)
            mViewModel.setDate(DateTime.now());
            adapter.setNewData(TypeLab.getInstance(getContext()).getTypes(true));
            mType = adapter.getItem(0);
            mAccount = AccountLab.getInstance(getContext()).getAccounts().get(0);
        } else {
            // 编辑账单
            mType = TypeLab.getInstance(getContext()).getType(mBill.getTypeId());
            mAccount = AccountLab.getInstance(getContext()).getAccount(mBill.getAccountId());
            // 初始化账户到没当前账单时
            if (mType.getExpense()) {
                adapter.setNewData(TypeLab.getInstance(getContext()).getTypes(true));
                mAccount.plusBalance(mBill.getBalance());
            } else {
                mTypeSbg.setPosition(0);
                adapter.setNewData(TypeLab.getInstance(getContext()).getTypes(false));
                mAccount.minusBalance(mBill.getBalance());
            }
            mBalanceEditText.setText(mViewModel.getBalance());
        }
        mViewModel.setType(mType);
        mViewModel.setAccount(mAccount);
    }

    /**
     * 启动动画
     */
    private void viewEnterAnimation(View view) {
        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop,
                                       int oldRight, int oldBottom) {
                v.removeOnLayoutChangeListener(this);
                int cx = getArguments().getInt(ARG_CX);
                int cy = getArguments().getInt(ARG_CY);
                // get the hypothenuse so the radius is from one corner to the other
                int w = v.getWidth();
                int h = v.getHeight();
                int radius = (int) Math.hypot(w, h);
                if (cx == 0 && cy == 0) {
                    cx = w / 2;
                    cy = h / 2;
                }
                Animator reveal = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0, radius);
                reveal.setInterpolator(new FastOutSlowInInterpolator());
                ObjectAnimator colorChange = new ObjectAnimator();
                colorChange.setIntValues(getResources().getColor(R.color.colorPrimary),
                        getResources().getColor(R.color.transparent));
                colorChange.setEvaluator(new ArgbEvaluator());
                colorChange.addUpdateListener(valueAnimator -> view.setBackgroundColor((Integer) valueAnimator.getAnimatedValue()));
                AnimatorSet set = new AnimatorSet();
                set.playTogether(reveal, colorChange);
                set.setDuration(350);
                set.start();
            }
        });
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param bill 要编辑的账单
     * @return A new instance of fragment BillEditFragment.
     */
    public static BillEditFragment newInstance(Bill bill, int centerX, int centerY) {
        BillEditFragment fragment = new BillEditFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_BILL, bill);
        args.putInt(ARG_CX, centerX);
        args.putInt(ARG_CY, centerY);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save_item:
                if (!mViewModel.saveBill(mBalanceEditText.getText().toString(),
                        mRemarkEditText.getText().toString())) {
                    // 保存失败直接返回
                    return true;
                }
            case android.R.id.home:
                mNumPad.setVisibility(View.INVISIBLE);
                getActivity().onBackPressed();
                Intent intent = new Intent(DesktopWidget.EXTRA_ACTION_UP);
                getActivity().sendBroadcast(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 选择类型
     */
    public void clickTypeItem(BaseQuickAdapter adapter1, int position) {
        mType = (Type) adapter1.getData().get(position);
        mViewModel.setType(mType);
        typeImageAnimator();
    }

    /**
     * 类型图片动画
     */
    private void typeImageAnimator() {
        // 缩小消失动画
        Animator disappear = AnimatorInflater.loadAnimator(getActivity(), R.animator.type_disappear);
        disappear.setTarget(mTypeImageView);

        disappear.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                // 更换图标
                mTypeImageView.setImageResource(mType.getTypeId());
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        // 放大显示动画
        Animator appear = AnimatorInflater.loadAnimator(getActivity(), R.animator.type_appear);
        appear.setTarget(mTypeImageView);

        AnimatorSet set = new AnimatorSet();
        set.playSequentially(disappear, appear);
        set.start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_DATE:
                mViewModel.setDate((DateTime) data
                        .getSerializableExtra(DatePickerFragment.EXTRA_DATE));
                break;
            default:
                break;
        }
    }

    @Override
    protected void updateUI() {

    }
}

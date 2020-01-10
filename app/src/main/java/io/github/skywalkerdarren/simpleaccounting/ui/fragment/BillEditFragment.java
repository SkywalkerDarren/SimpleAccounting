package io.github.skywalkerdarren.simpleaccounting.ui.fragment;


import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;

import org.joda.time.DateTime;

import co.ceryle.segmentedbutton.SegmentedButtonGroup;
import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.adapter.AccountMenuAdapter;
import io.github.skywalkerdarren.simpleaccounting.adapter.TypeAdapter;
import io.github.skywalkerdarren.simpleaccounting.base.BaseFragment;
import io.github.skywalkerdarren.simpleaccounting.databinding.FragmentBillEditBinding;
import io.github.skywalkerdarren.simpleaccounting.databinding.MenuAccountBinding;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill;
import io.github.skywalkerdarren.simpleaccounting.ui.DesktopWidget;
import io.github.skywalkerdarren.simpleaccounting.ui.NumPad;
import io.github.skywalkerdarren.simpleaccounting.util.ViewModelFactory;
import io.github.skywalkerdarren.simpleaccounting.util.view.DpConvertUtils;
import io.github.skywalkerdarren.simpleaccounting.view_model.BillEditViewModel;

/**
 * 账单编辑或创建的fragment
 *
 * @author darren
 * @date 2018/2/21
 */
public class BillEditFragment extends BaseFragment {
    private static final String TAG = "BillEditFragment";

    private static final String ARG_BILL = "bill";
    private static final int REQUEST_DATE = 0;
    private static final String ARG_CX = "cx";
    private static final String ARG_CY = "cy";
    private FragmentBillEditBinding mBinding;
    private Bill mBill;
    private DateTime mDateTime;
    private TypeAdapter mAdapter;


    private BillEditViewModel mViewModel;

    private EditText mBalanceEditText;
    private EditText mRemarkEditText;
    private SegmentedButtonGroup mTypeSbg;
    private NumPad mNumPad;
    private ImageView mTypeImageView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param bill 要编辑的账单
     * @return A new instance of fragment BillEditFragment.
     */
    public static BillEditFragment newInstance(@Nullable Bill bill, int centerX, int centerY) {
        BillEditFragment fragment = new BillEditFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_BILL, bill);
        args.putInt(ARG_CX, centerX);
        args.putInt(ARG_CY, centerY);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBill = (Bill) getArguments().getSerializable(ARG_BILL);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewModel = ViewModelFactory.getInstance(requireActivity().getApplication())
                .obtainViewModel(this, BillEditViewModel.class);
        mViewModel.setBill(mBill);
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_bill_edit, container, false);
        mRemarkEditText = mBinding.remarkEditText;
        mBalanceEditText = mBinding.balanceEditText;
        mNumPad = mBinding.numKeyView;
        mTypeSbg = mBinding.typeSbg;
        mTypeImageView = mBinding.typeImageView;
        // 自定义导航栏
        ActionBar actionBar = initToolbar(R.id.toolbar, mBinding.getRoot());
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        mBinding.typeListRecyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 4));
        viewEnterAnimation(mBinding.getRoot());
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBinding.setEdit(mViewModel);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        mViewModel.getBill().observe(getViewLifecycleOwner(), bill -> mDateTime = bill.getDate());
        mViewModel.getTypes().observe(getViewLifecycleOwner(), types -> {
            mAdapter.setNewData(types);
            mAdapter.isOpen = false;
            mViewModel.getType().setValue(types.get(0));
        });
        // 配置适配器
        if (mAdapter == null) {
            mAdapter = new TypeAdapter(mViewModel, mBinding.typeImageView);

        }
        // 配置选择按钮
        mTypeSbg.setOnClickedButtonListener(position -> {
            mViewModel.isExpense().setValue(position == 1);
            typeImageAnimator();
        });

        mBinding.typeListRecyclerView.setAdapter(mAdapter);

        mNumPad.setStrReceiver(mBalanceEditText);
        setBalanceEditText();

        mRemarkEditText.setOnClickListener((view) -> mNumPad.hideKeyboard());

        mBinding.dateImageView.setOnClickListener(view -> {
            if (mDateTime == null) {
                return;
            }
            DatePickerFragment datePicker = DatePickerFragment.newInstance(mDateTime);
            datePicker.setTargetFragment(getTargetFragment(), REQUEST_DATE);
            datePicker.show(requireFragmentManager(), "datePicker");
        });

        // TODO: 2018/4/2 监听账户点击
        mBinding.accountTypeImageView.setOnClickListener(view -> {
            getPopupWindow(mBinding.accountTypeImageView);
        });

    }

    @SuppressLint("ClickableViewAccessibility")
    private void setBalanceEditText() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mBalanceEditText.setSelection(mBalanceEditText.getText().length());
            }
        };
        mBalanceEditText.addTextChangedListener(textWatcher);
        mBalanceEditText.setOnTouchListener((view, motionEvent) -> {
            mRemarkEditText.clearFocus();
            mNumPad.hideSysKeyboard();
            new Handler().postDelayed(() -> mNumPad.showKeyboard(), 200);
            return true;
        });
        mBalanceEditText.setOnFocusChangeListener((view, b) -> {
            if (!b) {
                mNumPad.hideKeyboard();
            }
        });
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
                int cx = requireArguments().getInt(ARG_CX);
                int cy = requireArguments().getInt(ARG_CY);
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save_item:
                if (!mViewModel.saveBill(msg ->
                        Snackbar.make(requireView(), msg, Snackbar.LENGTH_SHORT).show())) {
                    // 保存失败直接返回
                    return true;
                }
                DesktopWidget.refresh(requireContext());
            case android.R.id.home:
                mNumPad.setVisibility(View.INVISIBLE);
                requireActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 类型图片动画
     */
    private void typeImageAnimator() {
        // 放大显示动画
        Animator appear = AnimatorInflater.loadAnimator(requireActivity(), R.animator.type_appear);
        appear.setTarget(mTypeImageView);
        appear.start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            mDateTime = (DateTime) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mViewModel.getBill().getValue().setDate(mDateTime);
        }
    }

    @Override
    protected void updateUI() {

    }

    @Override
    public void onResume() {
        super.onResume();
        //mBalanceEditText.setSelection(mBalanceEditText.getText().length());
    }

    private void getPopupWindow(View view) {
        MenuAccountBinding binding = MenuAccountBinding.inflate(LayoutInflater.from(requireContext()));
        View menu = binding.getRoot();
        binding.accountRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.accountRecyclerView.addItemDecoration(
                new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        mViewModel.getAccounts().observe(this, accounts -> {
            PopupWindow popupWindow = new PopupWindow(menu);
            AccountMenuAdapter adapter = new AccountMenuAdapter(accounts);
            adapter.setOnItemClickListener((adapter1, view1, position) -> {
                Account account = (Account) adapter1.getData().get(position);
                mViewModel.getAccount().setValue(account);
                popupWindow.dismiss();
            });
            binding.accountRecyclerView.setAdapter(adapter);
            setPopupWindow(popupWindow, view);

        });
    }

    private void setPopupWindow(PopupWindow popupWindow, View view) {
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setElevation(DpConvertUtils.convertDpToPixel(2, requireContext()));
        popupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setAnimationStyle(R.style.anim_menu_account);
        int height = popupWindow.getContentView().getMeasuredHeight();
        popupWindow.showAsDropDown(view, 0,
                (int) -(height + view.getHeight() * 1.3), Gravity.TOP);
    }
}

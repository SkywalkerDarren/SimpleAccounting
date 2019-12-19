package io.github.skywalkerdarren.simpleaccounting.ui.fragment;


import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
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
import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import co.ceryle.segmentedbutton.SegmentedButtonGroup;
import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.adapter.AccountMenuAdapter;
import io.github.skywalkerdarren.simpleaccounting.adapter.TypeAdapter;
import io.github.skywalkerdarren.simpleaccounting.base.BaseFragment;
import io.github.skywalkerdarren.simpleaccounting.databinding.FragmentBillEditBinding;
import io.github.skywalkerdarren.simpleaccounting.databinding.MenuAccountBinding;
import io.github.skywalkerdarren.simpleaccounting.model.AppRepositry;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Type;
import io.github.skywalkerdarren.simpleaccounting.ui.NumPad;
import io.github.skywalkerdarren.simpleaccounting.util.DpConvertUtils;
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
    private Bill mBill;

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
        // Inflate the layout for this fragment
        FragmentBillEditBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_bill_edit, container, false);
        mRemarkEditText = binding.remarkEditText;
        mBalanceEditText = binding.balanceEditText;
        mNumPad = binding.numKeyView;
        mTypeSbg = binding.typeSbg;
        mTypeImageView = binding.typeImageView;
        mViewModel = new BillEditViewModel(mBill, getContext());

        // 自定义导航栏
        ActionBar actionBar = initToolbar(R.id.toolbar, binding.getRoot());
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);

        // 配置适配器
        TypeAdapter adapter = new TypeAdapter(null, binding);
        adapter.openLoadAnimation(view -> {
                    Animator animator = AnimatorInflater.loadAnimator(getActivity(),
                            R.animator.type_item_appear);
                    animator.setTarget(view);
                    return new Animator[]{animator};
                }
        );
        binding.typeListRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));

        // 配置选择按钮
        mTypeSbg.setOnClickedButtonListener(position -> {
            List<Type> types = AppRepositry
                    .getInstance(getContext()).getTypes(position == 1);
            for (int i = 0; i < adapter.getItemCount(); i++) {
                adapter.getViewByPosition(binding.typeListRecyclerView,
                        i, R.id.type_item).setAlpha(0);
            }
            adapter.setNewData(types);
            mViewModel.setType(types.get(0));
            typeImageAnimator();
            adapter.notifyDataSetChanged();
        });

        // 配置账单信息
        configBill(adapter);
        binding.typeListRecyclerView.setAdapter(adapter);

        if (!TextUtils.isEmpty(mViewModel.getRemark())) {
            mRemarkEditText.setText(mViewModel.getRemark());
        }

        mNumPad.setStrReceiver(mBalanceEditText);
        setBalanceEditText();

        mRemarkEditText.setOnClickListener((view) -> mNumPad.hideKeyboard());

        binding.dateImageView.setOnClickListener(view -> {
            DatePickerFragment datePicker = DatePickerFragment.newInstance(mViewModel.getDate());
            datePicker.setTargetFragment(this, REQUEST_DATE);
            datePicker.show(getFragmentManager(), "datePicker");
        });

        // TODO: 2018/4/2 监听账户点击
        binding.accountTypeImageView.setOnClickListener(view -> {
            Log.d(TAG, "onCreateView: clickImage");
            getPopupWindow(binding.accountTypeImageView);
        });

        binding.setEdit(mViewModel);
        viewEnterAnimation(binding.getRoot());
        return binding.getRoot();
    }

    private void setBalanceEditText() {
        mBalanceEditText.setOnTouchListener((view, motionEvent) -> {
            mRemarkEditText.clearFocus();
            mNumPad.hideSysKeyboard();
            new Handler().postDelayed(() -> mNumPad.showKeyboard(), 200);
            return true;
        });
        mBalanceEditText.setSelection(mBalanceEditText.getText().length());
        mBalanceEditText.setOnFocusChangeListener((view, b) -> {
            if (!b) {
                mNumPad.hideKeyboard();
            }
        });
    }

    /**
     * 配置初始账单，将账单信息绑定到视图
     */
    private void configBill(TypeAdapter adapter) {
        AppRepositry repositry = AppRepositry.getInstance(getContext());
        Account account;
        if (mViewModel.getDate() == null) {
            // 创建账单(日期不存在则一定是刚创建的)
            mViewModel.setDate(DateTime.now());
            adapter.setNewData(repositry.getTypes(true));
            mViewModel.setType(adapter.getItem(0));
            account = repositry.getAccounts().get(0);
        } else {
            // 编辑账单
            UUID typeId = mViewModel.getTypeId();
            if (typeId == null) {
                mViewModel.setType(repositry.getTypes(true).get(0));
            } else {
                mViewModel.setType(repositry.getType(typeId));
            }
            account = repositry.getAccount(mViewModel.getAccountId());
            // 初始化账户到没当前账单时
            if (mViewModel.getExpense()) {
                adapter.setNewData(repositry.getTypes(true));
                account.plusBalance(new BigDecimal(mViewModel.getBalance()));
            } else {
                mTypeSbg.setPosition(0);
                adapter.setNewData(repositry.getTypes(false));
                account.minusBalance(new BigDecimal(mViewModel.getBalance()));
            }
            mBalanceEditText.setText(mViewModel.getBalance());
        }
        mViewModel.setAccount(account);
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
        Animator appear = AnimatorInflater.loadAnimator(getActivity(), R.animator.type_appear);
        appear.setTarget(mTypeImageView);
        appear.start();
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

    private void getPopupWindow(View view) {
        MenuAccountBinding binding = MenuAccountBinding.inflate(LayoutInflater.from(getContext()));
        View menu = binding.getRoot();
        AccountMenuAdapter adapter = new AccountMenuAdapter(AppRepositry.getInstance(getContext()).getAccounts());
        PopupWindow popupWindow = new PopupWindow(menu);
        adapter.setOnItemClickListener((adapter1, view1, position) -> {
            Account account = (Account) adapter1.getData().get(position);
            mViewModel.setAccount(account);
            popupWindow.dismiss();
        });
        binding.accountRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.accountRecyclerView.setAdapter(adapter);
        binding.accountRecyclerView.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setElevation(DpConvertUtils.convertDpToPixel(2, getContext()));
        popupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setAnimationStyle(R.style.anim_menu_account);
        int height = popupWindow.getContentView().getMeasuredHeight();
        Log.d(TAG, "getPopupWindow() called with: view = [" + height + "]");
        popupWindow.showAsDropDown(view, 0,
                (int) -(height + view.getHeight() * 1.3), Gravity.TOP);
    }
}

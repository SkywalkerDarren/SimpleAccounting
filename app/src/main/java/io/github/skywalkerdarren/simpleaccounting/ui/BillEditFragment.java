package io.github.skywalkerdarren.simpleaccounting.ui;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.transition.Fade;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.ActionBar;
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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.List;

import co.ceryle.segmentedbutton.SegmentedButtonGroup;
import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.adapter.TypeAdapter;
import io.github.skywalkerdarren.simpleaccounting.model.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.BillLab;
import io.github.skywalkerdarren.simpleaccounting.model.Type;
import io.github.skywalkerdarren.simpleaccounting.model.TypeLab;

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
    private ImageView mTypeImageView;
    private ImageView mDateImageView;
    private TextView mTitleTextView;
    private EditText mBalanceEditText;
    private EditText mRemarkEditText;
    private RecyclerView mTypeRecyclerView;
    private DateTime mDateTime;
    private SegmentedButtonGroup mTypeSbg;
    private boolean mIsExpense = true;
    private NumPad mNumPad;


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
        View view = inflater.inflate(R.layout.fragment_bill_edit, container, false);
        mTitleTextView = view.findViewById(R.id.title_text_view);
        mTypeImageView = view.findViewById(R.id.type_image_view);
        mDateImageView = view.findViewById(R.id.date_image_view);
        mRemarkEditText = view.findViewById(R.id.remark_edit_text);
        mBalanceEditText = view.findViewById(R.id.balance_edit_text);
        mTypeRecyclerView = view.findViewById(R.id.type_list_recycler_view);
        mNumPad = view.findViewById(R.id.num_key_view);
        mTypeSbg = view.findViewById(R.id.type_sbg);

        // 自定义导航栏
        ActionBar actionBar = initToolbar(R.id.toolbar, view);
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);

        // 配置适配器
        TypeAdapter adapter = new TypeAdapter(TypeLab.getInstance(getContext()).getTypes(true));
        adapter.openLoadAnimation(view14 -> new Animator[]{
                        ObjectAnimator.ofFloat(view14, "scaleY", 1, 1.1f, 1),
                        ObjectAnimator.ofFloat(view14, "scaleX", 1, 1.1f, 1),
                        ObjectAnimator.ofFloat(view14, "alpha", 0f, 1f)
                }
        );
        adapter.setOnItemClickListener((adapter1, view12, position) -> clickTypeItem(adapter1, position));
        adapter.setOnItemChildClickListener((adapter12, view17, position) -> clickTypeItem(adapter12, position));
        mTypeRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        mTypeRecyclerView.setAdapter(adapter);

        // 配置选择按钮
        mTypeSbg.setOnClickedButtonListener(position -> {
            List<Type> types;
            switch (position) {
                case 0:
                    mIsExpense = false;
                    mBalanceEditText.setTextColor(getResources().getColor(R.color.greenyellow));
                    break;
                case 1:
                    mIsExpense = true;
                    mBalanceEditText.setTextColor(getResources().getColor(R.color.orangered));
                    break;
                default:
                    break;
            }
            types = TypeLab.getInstance(getContext()).getTypes(mIsExpense);
            adapter.setNewData(types);
            mType = types.get(0);
            mTitleTextView.setText(mType.getName());
            mTypeImageView.setImageResource(mType.getTypeId());
            typeImageAnimator();
            adapter.notifyDataSetChanged();
        });

        // 配置账单信息
        if (mBill.getDate() == null) {
            mDateTime = DateTime.now();
            mTitleTextView.setText(adapter.getData().get(0).getName());
            mTypeImageView.setImageResource(adapter.getData().get(0).getTypeId());
            mType = adapter.getItem(0);
        } else {
            mType = TypeLab.getInstance(getContext()).getType(mBill.getTypeId());
            mDateTime = mBill.getDate();
            mTitleTextView.setText(mBill.getName());
            mTypeImageView.setImageResource(mType.getTypeId());
            mBalanceEditText.setText(mBill.getBalance().toString());
        }

        if (!TextUtils.isEmpty(mBill.getRemark())) {
            mRemarkEditText.setText(mBill.getRemark());
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
            DatePickerFragment datePicker = DatePickerFragment.newInstance(mDateTime);
            datePicker.setTargetFragment(this, REQUEST_DATE);
            datePicker.show(getFragmentManager(), "datePicker");
        });

//         启动动画
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
        return view;
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
                if (saveBill()) {
                    Toast.makeText(getActivity(), "点击保存并退出", Toast.LENGTH_SHORT).show();
                } else {
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
     * 保存账单
     */
    private boolean saveBill() {
        if (TextUtils.isEmpty(mBalanceEditText.getText())) {
            return false;
        }
        try {
            BigDecimal r = new BigDecimal(mBalanceEditText.getText().toString());
            mBill.setBalance(r);
        } catch (Exception e) {
            Toast.makeText(getContext(), "表达式错误", Toast.LENGTH_SHORT);
            return false;
        }
        mBill.setName(mTitleTextView.getText().toString());
        mBill.setDate(mDateTime);
        mBill.setRemark(mRemarkEditText.getText().toString());
        mBill.setTypeId(mType.getId());

        BillLab lab = BillLab.getInstance(getActivity());
        if (lab.getBill(mBill.getId()) == null) {
            lab.addBill(mBill);
        } else {
            lab.updateBill(mBill);
        }
        return true;
    }


    /**
     * 选择类型
     */
    public void clickTypeItem(BaseQuickAdapter adapter1, int position) {
        mType = (Type) adapter1.getData().get(position);
        mTitleTextView.setText(mType.getName());
        mTypeImageView.setImageResource(mType.getTypeId());
        typeImageAnimator();
    }

    /**
     * 类型图片动画
     */
    private void typeImageAnimator() {
        ObjectAnimator animatorX1 = ObjectAnimator.ofFloat(mTypeImageView, "scaleX", 0.5f, 0f);
        ObjectAnimator animatorY1 = ObjectAnimator.ofFloat(mTypeImageView, "scaleY", 0.5f, 0f);
        AnimatorSet set1 = new AnimatorSet();
        set1.setDuration(100);
        set1.setInterpolator(new AccelerateInterpolator());
        set1.playTogether(animatorX1, animatorY1);

        ObjectAnimator animatorX2 = ObjectAnimator.ofFloat(mTypeImageView, "scaleX", 0f, 1f);
        ObjectAnimator animatorY2 = ObjectAnimator.ofFloat(mTypeImageView, "scaleY", 0f, 1f);
        AnimatorSet set2 = new AnimatorSet();
        set2.setDuration(200);
        set2.setInterpolator(new DecelerateInterpolator());
        set2.playTogether(animatorX2, animatorY2);

        AnimatorSet set = new AnimatorSet();
        set.playSequentially(set1, set2);
        set.start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_DATE:
                mDateTime = (DateTime) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void updateUI() {

    }
}

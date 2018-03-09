package io.github.skywalkerdarren.simpleaccounting.UI;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.math.BigDecimal;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.adapter.TypeAdapter;
import io.github.skywalkerdarren.simpleaccounting.model.BaseType;
import io.github.skywalkerdarren.simpleaccounting.model.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.BillLab;
import io.github.skywalkerdarren.simpleaccounting.model.IncomeType;

/**
 * 账单编辑或创建的fragment
 */
public class BillEditFragment extends BaseFragment {
    private static final String ARG_BILL = "bill";
    private Bill mBill;
    private ImageView mTypeImageView;
    private TextView mTitleTextView;
    private TextView mBalanceTextView;
    private EditText mRemarkEditText;
    private RecyclerView mTypeRecyclerView;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param bill 要编辑的账单
     * @return A new instance of fragment BillEditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BillEditFragment newInstance(Bill bill) {
        BillEditFragment fragment = new BillEditFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_BILL, bill);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save_item:
                saveBill();
                Toast.makeText(getActivity(), "点击保存并退出", Toast.LENGTH_SHORT).show();
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveBill() {
        mBill.setName(mTitleTextView.getText().toString());
        // TODO 改时间
        mBill.setDate(DateTime.now());
        mBill.setRemark(mRemarkEditText.getText().toString());
        mBill.setBalance(new BigDecimal(mBalanceTextView.getText().toString()));
        mBill.setType(mTitleTextView.getText().toString());
        // TODO 改类型
        for (BaseType type : IncomeType.getTypeList()) {
            if (type.getName().equals(mBill.getTypeName())) {
                mBill.setExpense(type);
            }
        }

        BillLab lab = BillLab.getInstance(getActivity());
        if (lab.getBill(mBill.getId()) == null) {
            lab.addBill(mBill);
        } else {
            lab.updateBill(mBill);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bill_edit, container, false);
        mTitleTextView = view.findViewById(R.id.title_text_view);
        mTypeImageView = view.findViewById(R.id.type_image_view);
        mRemarkEditText = view.findViewById(R.id.remark_edit_text);
        mBalanceTextView = view.findViewById(R.id.balance_text_view);
        mTypeRecyclerView = view.findViewById(R.id.type_list_recycler_view);

        // 自定义导航栏
        ActionBar actionBar = initToolbar(R.id.toolbar, view);
        actionBar.setTitle(R.string.edit_bill);
        actionBar.setDisplayHomeAsUpEnabled(true);

        TypeAdapter adapter = new TypeAdapter(IncomeType.getTypeList());
        adapter.setOnItemClickListener((adapter1, view12, position) -> {
            BaseType type = (BaseType) adapter1.getData().get(position);
            mTitleTextView.setText(type.getName());
            mTypeImageView.setImageResource(type.getTypeId());
        });
        mTypeRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        mTypeRecyclerView.setAdapter(adapter);

        if (!TextUtils.isEmpty(mBill.getName())) {
            mTitleTextView.setText(mBill.getName());
        } else {
            mTitleTextView.setText(adapter.getData().get(0).getName());
        }

        if (!TextUtils.isEmpty(mBill.getTypeName())) {
            mTypeImageView.setImageResource(mBill.getTypeResId());
        } else {
            mTypeImageView.setImageResource(adapter.getData().get(0).getTypeId());
        }

        if (!TextUtils.isEmpty(mBill.getRemark())) {
            mRemarkEditText.setText(mBill.getRemark());
        }

        if (mBill.getBalance() == null) {
            mBill.setBalance(BigDecimal.ZERO);
        }
        mBalanceTextView.setText(mBill.getBalance().toString());
        mBalanceTextView.setOnClickListener(view1 -> {
            Snackbar snackbar;
        });
        return view;
    }

}

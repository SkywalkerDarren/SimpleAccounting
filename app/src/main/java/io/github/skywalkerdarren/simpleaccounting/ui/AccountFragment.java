package io.github.skywalkerdarren.simpleaccounting.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.adapter.AccountAdapter;
import io.github.skywalkerdarren.simpleaccounting.model.Account;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends BaseFragment {
    private TextView mNavTextView;
    private TextView mLiavilityTextView;
    private TextView mTotalAssetsTextView;
    private TextView mAccountCountTextView;
    private RecyclerView mAccountRecyclerView;
    private TextView mLendTextView;
    private TextView mBorrowTextView;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AccountFragment.
     */
    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        mNavTextView = view.findViewById(R.id.nav_text_view);
        mLiavilityTextView = view.findViewById(R.id.liability_text_view);
        mTotalAssetsTextView = view.findViewById(R.id.total_assets_text_view);
        mAccountCountTextView = view.findViewById(R.id.account_count_text_view);
        mAccountRecyclerView = view.findViewById(R.id.account_recycler_view);
        mLendTextView = view.findViewById(R.id.lend_text_view);
        mBorrowTextView = view.findViewById(R.id.borrow_text_view);

        // TODO: 2018/3/24 演示数据
        mNavTextView.setText("0");
        mLiavilityTextView.setText("0");
        mTotalAssetsTextView.setText("0");
        mAccountRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<Account> accounts = new ArrayList<>(3);
        accounts.add(new Account("现金", "现金金额",
                R.drawable.account_cash, getResources().getColor(R.color.amber500)));
        accounts.add(new Account("支付宝", "在线支付余额",
                R.drawable.account_alipay, getResources().getColor(R.color.lightblue500)));
        accounts.add(new Account("微信", "在线支付余额",
                R.drawable.account_wechat, getResources().getColor(R.color.lightgreen500)));
        AccountAdapter adapter = new AccountAdapter(accounts);
        mAccountRecyclerView.setAdapter(adapter);
        mAccountCountTextView.setText(accounts.size() + "");
        mBorrowTextView.setText("0");
        mLendTextView.setText("0");
        return view;
    }

    @Override
    protected void updateUI() {

    }
}

package io.github.skywalkerdarren.simpleaccounting.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.adapter.AccountAdapter;
import io.github.skywalkerdarren.simpleaccounting.model.Account;
import io.github.skywalkerdarren.simpleaccounting.model.AccountLab;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends BaseFragment {
    private AccountLab mAccountLab;
    private TextView mNavTextView;
    private TextView mLiavilityTextView;
    private TextView mTotalAssetsTextView;
    private TextView mAccountCountTextView;
    private RecyclerView mAccountRecyclerView;
    private TextView mLendTextView;
    private TextView mBorrowTextView;
    private AccountAdapter mAdapter;

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
        mAccountLab = AccountLab.getInstance(getActivity());
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
        mAccountRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    protected void updateUI() {
        List<Account> accounts = mAccountLab.getAccounts();
        if (mAdapter == null) {
            mAdapter = new AccountAdapter(accounts);
        } else {
            mAdapter.setNewData(accounts);
            mAdapter.notifyDataSetChanged();
        }
        mAccountRecyclerView.setAdapter(mAdapter);

        // TODO: 2018/3/24 演示数据
        mNavTextView.setText("0");
        mLiavilityTextView.setText("0");
        mTotalAssetsTextView.setText("0");
        mAccountCountTextView.setText(accounts.size() + "");
        mBorrowTextView.setText("0");
        mLendTextView.setText("0");
    }
}

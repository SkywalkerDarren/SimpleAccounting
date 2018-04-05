package io.github.skywalkerdarren.simpleaccounting.ui;


import android.databinding.DataBindingUtil;
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
import io.github.skywalkerdarren.simpleaccounting.base.BaseFragment;
import io.github.skywalkerdarren.simpleaccounting.databinding.FragmentAccountBinding;
import io.github.skywalkerdarren.simpleaccounting.model.Account;
import io.github.skywalkerdarren.simpleaccounting.model.AccountLab;
import io.github.skywalkerdarren.simpleaccounting.view_model.AccountViewModel;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends BaseFragment {
    private AccountLab mAccountLab;
    private RecyclerView mAccountRecyclerView;
    private TextView mLendTextView;
    private TextView mBorrowTextView;
    private AccountAdapter mAdapter;
    FragmentAccountBinding mBinding;

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
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_account, container, false);
        mAccountRecyclerView = mBinding.accountRecyclerView;
        mLendTextView = mBinding.lendTextView;
        mBorrowTextView = mBinding.borrowTextView;
        mAccountRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return mBinding.getRoot();
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

        mBinding.setAccount(new AccountViewModel(getContext()));
        // TODO: 2018/3/24 演示数据
        mBorrowTextView.setText("0");
        mLendTextView.setText("0");
    }
}

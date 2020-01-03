package io.github.skywalkerdarren.simpleaccounting.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;

import java.util.ArrayList;
import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.adapter.AccountAdapter;
import io.github.skywalkerdarren.simpleaccounting.base.BaseFragment;
import io.github.skywalkerdarren.simpleaccounting.databinding.FragmentAccountBinding;
import io.github.skywalkerdarren.simpleaccounting.model.AppRepository;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;
import io.github.skywalkerdarren.simpleaccounting.ui.DesktopWidget;
import io.github.skywalkerdarren.simpleaccounting.ui.activity.MainActivity;
import io.github.skywalkerdarren.simpleaccounting.ui.activity.SettingsActivity;
import io.github.skywalkerdarren.simpleaccounting.util.AppExecutors;
import io.github.skywalkerdarren.simpleaccounting.util.ViewModelFactory;
import io.github.skywalkerdarren.simpleaccounting.view_model.AccountViewModel;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author darren
 */
public class AccountFragment extends BaseFragment {
    private RecyclerView mAccountRecyclerView;
    private AccountAdapter mAdapter;
    private FragmentAccountBinding mBinding;
    private AccountViewModel mViewModel;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_account, container, false);
        mAccountRecyclerView = mBinding.accountRecyclerView;
        mAccountRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        mBinding.settingCardView.setOnClickListener(view -> toSettingActivity());
        mBinding.deleteAllCardView.setOnClickListener(view -> new AlertDialog.Builder(requireContext())
                .setCancelable(true)
                .setMessage("是否删除所有账单，删除后的账单将无法恢复！")
                .setTitle("警告")
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    AppRepository.getInstance(new AppExecutors(), requireContext()).clearBill();
                    DesktopWidget.refresh(requireContext());
                    onResume();
                    MainActivity activity = (MainActivity) requireActivity();
                    BillListFragment fragment = activity.mBillListFragment;
                    fragment.onResume();
                })
                .create()
                .show());
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewModelFactory factory = ViewModelFactory.getInstance(requireActivity().getApplication());
        mViewModel = ViewModelProviders.of(requireActivity(), factory).get(AccountViewModel.class);
        mBinding.setAccount(mViewModel);
        mBinding.setLifecycleOwner(this);
    }

    private void toSettingActivity() {
        Intent intent = SettingsActivity.newIntent(requireContext());
        startActivity(intent);
    }

    private static final List<Account> accountsCache = new ArrayList<>();

    private boolean checkCache(List<Account> accounts) {
        if (accountsCache.size() != accounts.size()) {
            accountsCache.clear();
            accountsCache.addAll(accounts);
            return true;
        }
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            Account cache = accountsCache.get(i);
            if (!account.equals(cache)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void updateUI() {
        if (mAdapter == null) {
            mAdapter = new AccountAdapter(requireContext());
        }
        mViewModel.getAccounts().observe(this, accounts -> {
            if (checkCache(accounts)) {
                mAdapter.setNewData(accounts);
                mAdapter.notifyDataSetChanged();
            }
        });
        mAccountRecyclerView.setAdapter(mAdapter);
        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(mAccountRecyclerView);
        mAdapter.enableDragItem(itemTouchHelper);
        mViewModel.start();
    }
}

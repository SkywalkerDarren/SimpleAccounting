package io.github.skywalkerdarren.simpleaccounting.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.adapter.AccountAdapter;
import io.github.skywalkerdarren.simpleaccounting.databinding.FragmentAccountBinding;
import io.github.skywalkerdarren.simpleaccounting.util.ViewModelFactory;
import io.github.skywalkerdarren.simpleaccounting.view_model.AccountViewModel;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author darren
 */
public class AccountFragment extends Fragment {
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_account, container, false);
        mAccountRecyclerView = mBinding.accountRecyclerView;
        mAccountRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mViewModel == null) {
            mViewModel = ViewModelFactory.getInstance(requireActivity().getApplication())
                    .obtainViewModel(this, AccountViewModel.class);
        }
        mBinding.setAccount(mViewModel);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        if (mAdapter == null) {
            mAdapter = new AccountAdapter(mViewModel);
        }
        mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.SlideInLeft);
        mAdapter.getDraggableModule().setDragOnLongPressEnabled(true);
        mAccountRecyclerView.setAdapter(mAdapter);
        mViewModel.getAccounts().observe(getViewLifecycleOwner(),
                accounts -> mAdapter.setNewList(accounts));
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.start();
    }
}

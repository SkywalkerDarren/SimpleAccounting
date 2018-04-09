package io.github.skywalkerdarren.simpleaccounting.ui;


import android.animation.ObjectAnimator;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;

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
    private AccountAdapter mAdapter;
    private FragmentAccountBinding mBinding;
    private AccountViewModel mViewModel;
    private static final String TAG = "AccountFragment";

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
        mViewModel = new AccountViewModel(getContext());
        mAccountRecyclerView = mBinding.accountRecyclerView;
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
        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(mAccountRecyclerView);
        mAdapter.enableDragItem(itemTouchHelper);
        mAdapter.setOnItemDragListener(new OnItemDragListener() {
            private int mSub;

            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
                float st = viewHolder.itemView.getElevation();
                ObjectAnimator animator = ObjectAnimator.ofFloat(viewHolder.itemView, "elevation", st, st * 2);
                animator.setDuration(50);
                animator.start();
                mSub = pos;
            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {

            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
                float ed = viewHolder.itemView.getElevation();
                ObjectAnimator animator = ObjectAnimator.ofFloat(viewHolder.itemView, "elevation", ed * 2, ed);
                animator.setDuration(50);
                animator.start();
                Log.d(TAG, "onItemDragEnd: " + mSub + " " + pos);
                mViewModel.changePosition(mSub, pos);
            }
        });
        mBinding.setAccount(mViewModel);
    }
}

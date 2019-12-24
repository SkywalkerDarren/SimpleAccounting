package io.github.skywalkerdarren.simpleaccounting.ui.fragment;


import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.adapter.AccountAdapter;
import io.github.skywalkerdarren.simpleaccounting.base.BaseFragment;
import io.github.skywalkerdarren.simpleaccounting.databinding.FragmentAccountBinding;
import io.github.skywalkerdarren.simpleaccounting.model.AppRepositry;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;
import io.github.skywalkerdarren.simpleaccounting.ui.DesktopWidget;
import io.github.skywalkerdarren.simpleaccounting.ui.activity.MainActivity;
import io.github.skywalkerdarren.simpleaccounting.ui.activity.SettingsActivity;
import io.github.skywalkerdarren.simpleaccounting.util.AppExecutors;
import io.github.skywalkerdarren.simpleaccounting.view_model.AccountViewModel;
import io.github.skywalkerdarren.simpleaccounting.view_model.ViewModelFactory;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author darren
 */
public class AccountFragment extends BaseFragment {
    private static final String TAG = "AccountFragment";
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_account, container, false);
        //mViewModel = new AccountViewModel(getContext())
        mAccountRecyclerView = mBinding.accountRecyclerView;
        mAccountRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.settingCardView.setOnClickListener(view -> toSettingActivity());
        // TODO toVM
        mBinding.deleteAllCardView.setOnClickListener(view -> {
            new AlertDialog.Builder(getContext())
                    .setCancelable(true)
                    .setMessage("是否删除所有账单，删除后的账单将无法恢复！")
                    .setTitle("警告")
                    .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                        AppRepositry.getInstance(new AppExecutors(), getContext()).clearBill();
                            DesktopWidget.refresh(getContext());
                            onResume();
                            MainActivity activity = (MainActivity) getActivity();
                            BillListFragment fragment = activity.mBillListFragment;
                            fragment.onResume();
                    })
                    .create()
                    .show();

        });
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewModelFactory factory = ViewModelFactory.getInstance(getActivity().getApplication());
        mViewModel = ViewModelProviders.of(getActivity(), factory).get(AccountViewModel.class);
        mBinding.setAccount(mViewModel);
        mBinding.setLifecycleOwner(this);
    }

    private void toSettingActivity() {
        Intent intent = SettingsActivity.newIntent(getContext());
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach() called");
    }

    @Override
    protected void updateUI() {
        List<Account> accounts = AppRepositry.getInstance(new AppExecutors(), getContext()).getAccounts();
        if (mAdapter == null) {
            mAdapter = new AccountAdapter(accounts, getActivity().getApplication());
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
            private Account accountA;
            private Account accountB;

            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
                float st = viewHolder.itemView.getElevation();
                itemRaiseAnimator(viewHolder.itemView, st, true);
                accountA = mAdapter.getItem(pos);
            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {

            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
                float ed = viewHolder.itemView.getElevation();
                itemRaiseAnimator(viewHolder.itemView, ed, false);
                accountB = mAdapter.getItem(pos);
                mViewModel.changePosition(accountA, accountB);
            }
        });
        mViewModel.start();
        Log.d(TAG, "updateUI: ");
    }

    private void itemRaiseAnimator(View view, final float start, boolean raise) {
        final float end = start * 2;
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "elevation",
                raise ? start : end, raise ? end : start);
        animator.setDuration(50);
        animator.start();
    }
}

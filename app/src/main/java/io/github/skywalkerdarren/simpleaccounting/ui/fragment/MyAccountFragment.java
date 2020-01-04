package io.github.skywalkerdarren.simpleaccounting.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.base.BaseFragment;
import io.github.skywalkerdarren.simpleaccounting.databinding.FragmentMyAccountBinding;
import io.github.skywalkerdarren.simpleaccounting.model.AppRepository;
import io.github.skywalkerdarren.simpleaccounting.ui.DesktopWidget;
import io.github.skywalkerdarren.simpleaccounting.ui.activity.MainActivity;
import io.github.skywalkerdarren.simpleaccounting.util.AppExecutors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyAccountFragment extends BaseFragment {


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MyAccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyAccountFragment newInstance() {
        MyAccountFragment fragment = new MyAccountFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentMyAccountBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_my_account, container, false);
        binding.aboutUs.setOnClickListener(view -> {
            AboutFragment aboutFragment = AboutFragment.Companion.newInstance();
            gotoFragment(aboutFragment);
        });
        binding.setting.setOnClickListener(view -> {

        });
        binding.back.setOnClickListener(view -> requireActivity().finish());
        binding.feedbackLayout.setOnClickListener(v -> {
            FeedBackFragment feedBackFragment = FeedBackFragment.Companion.newInstance();
            gotoFragment(feedBackFragment);
        });

        binding.deleteAllCardView.setOnClickListener(view -> new AlertDialog.Builder(requireContext())
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
        return binding.getRoot();
    }

    private void gotoFragment(BaseFragment fragment) {
        requireFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, null)
                .addToBackStack(getTag())
                .commit();
    }

    @Override
    protected void updateUI() {

    }
}

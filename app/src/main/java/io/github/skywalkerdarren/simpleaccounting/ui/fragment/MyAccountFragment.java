package io.github.skywalkerdarren.simpleaccounting.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.base.BaseFragment;
import io.github.skywalkerdarren.simpleaccounting.databinding.FragmentMyAccountBinding;
import io.github.skywalkerdarren.simpleaccounting.ui.activity.AboutActivity;
import io.github.skywalkerdarren.simpleaccounting.ui.activity.SettingsActivity;

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
            Intent intent = AboutActivity.newIntent(requireContext());
            startActivity(intent);
        });
        binding.setting.setOnClickListener(view -> {
            Intent intent = SettingsActivity.newIntent(requireContext());
            startActivity(intent);
        });
        binding.back.setOnClickListener(view -> requireActivity().finish());
        return binding.getRoot();
    }

    @Override
    protected void updateUI() {

    }
}

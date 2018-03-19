package io.github.skywalkerdarren.simpleaccounting.ui;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.skywalkerdarren.simpleaccounting.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassifyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassifyFragment extends Fragment {


    @SuppressLint("RtlHardcoded")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEnterTransition(new Slide(Gravity.LEFT));
        setExitTransition(new Slide(Gravity.LEFT));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_classify, container, false);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ClassifyFragment.
     */
    public static ClassifyFragment newInstance() {
        ClassifyFragment fragment = new ClassifyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

}

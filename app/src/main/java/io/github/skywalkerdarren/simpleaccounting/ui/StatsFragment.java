package io.github.skywalkerdarren.simpleaccounting.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.ceryle.segmentedbutton.SegmentedButtonGroup;
import io.github.skywalkerdarren.simpleaccounting.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatsFragment extends Fragment {
    private static final String ARG_POSITION = "mPosition";
    private SegmentedButtonGroup mStatsSbg;
    private int mPosition = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            mPosition = 1;
        } else {
            mPosition = savedInstanceState.getInt(ARG_POSITION) - 1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        mStatsSbg = view.findViewById(R.id.stats_sbg);

        FragmentManager fm = getFragmentManager();
        final Fragment journalFragment = JournalFragment.newInstance();
        final Fragment classifyFragment = ClassifyFragment.newInstance();
        FragmentTransaction ft = fm.beginTransaction();
        switch (mPosition) {
            case 1:
                ft.add(R.id.fragment_container, journalFragment).commit();
                break;
            default:
                ft.add(R.id.fragment_container, classifyFragment).commit();
                break;
        }

        mStatsSbg.setOnClickedButtonListener(position -> {
            switch (position) {
                case 0:
                    fm.beginTransaction().replace(R.id.fragment_container, classifyFragment).commit();
                    break;
                case 1:
                    fm.beginTransaction().replace(R.id.fragment_container, journalFragment).commit();
                    break;
                default:
                    break;
            }
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mPosition = mStatsSbg.getPosition();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StatsFragment.
     */
    public static StatsFragment newInstance() {
        StatsFragment fragment = new StatsFragment();
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_POSITION, mStatsSbg.getPosition() + 1);
    }
}

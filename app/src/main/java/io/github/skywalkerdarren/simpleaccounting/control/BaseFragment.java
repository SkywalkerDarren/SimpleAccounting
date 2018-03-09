package io.github.skywalkerdarren.simpleaccounting.control;

import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by darren on 2018/3/8.
 */

public abstract class BaseFragment extends Fragment {
    protected ActionBar initToolbar(@IdRes int id, View view) {
        AppCompatActivity mAppCompatActivity = (AppCompatActivity) getActivity();
        Toolbar toolbar = view.findViewById(id);
        toolbar.setTitleTextColor(Color.WHITE);
        mAppCompatActivity.setSupportActionBar(toolbar);
        return mAppCompatActivity.getSupportActionBar();
    }
}

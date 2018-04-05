package io.github.skywalkerdarren.simpleaccounting.base;

import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * fragment基类
 * 可自定义toolbar
 *
 * @author darren
 * @date 2018/3/8
 */

public abstract class BaseFragment extends Fragment {

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    /**
     * 初始化顶部工具栏
     *
     * @param id   顶部工具栏id
     * @param view 当前布局视图
     * @return 工具栏
     */
    protected ActionBar initToolbar(@IdRes int id, View view) {
        AppCompatActivity mAppCompatActivity = (AppCompatActivity) getActivity();
        Toolbar toolbar = view.findViewById(id);
        toolbar.setTitleTextColor(Color.WHITE);
        mAppCompatActivity.setSupportActionBar(toolbar);
        return mAppCompatActivity.getSupportActionBar();
    }

    /**
     * 更新视图
     * 放一些需要经常更新的动作
     */
    protected abstract void updateUI();

}

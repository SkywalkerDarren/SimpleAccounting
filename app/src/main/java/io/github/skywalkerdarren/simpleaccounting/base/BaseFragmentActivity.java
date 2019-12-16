package io.github.skywalkerdarren.simpleaccounting.base;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import io.github.skywalkerdarren.simpleaccounting.R;

/**
 * 用于包裹fragment的基类activity
 *
 * @author darren
 * @date 2018/2/21
 */

public abstract class BaseFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        // 以代码的方式将fragment添加给activity，需要直接调用activity的FragmentManager
        FragmentManager fm = getSupportFragmentManager();
        // 从容器视图中寻找fragment
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        // 如果没找到则是没创建
        if (fragment == null) {
            // 创建CrimeFragment新视图
            fragment = createFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragment_container, fragment).commit();
        }
    }

    /**
     * 构建一个fragment给fragment manager
     *
     * @return 构建完成的fragment
     */
    public abstract Fragment createFragment();

}

package io.github.skywalkerdarren.simpleaccounting.control;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import io.github.skywalkerdarren.simpleaccounting.R;

public class BillListActivity extends AppCompatActivity {

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
//            fragment = new CrimeFragment();
            fragment = new BillListFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragment_container, fragment).commit();
        }
    }


}

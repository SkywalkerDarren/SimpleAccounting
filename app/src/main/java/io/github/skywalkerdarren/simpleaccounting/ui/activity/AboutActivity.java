package io.github.skywalkerdarren.simpleaccounting.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import org.joda.time.DateTime;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.databinding.ActivityAboutBinding;
import io.github.skywalkerdarren.simpleaccounting.model.Demo;
import io.github.skywalkerdarren.simpleaccounting.ui.DesktopWidget;

public class AboutActivity extends AppCompatActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, AboutActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAboutBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_about);
        binding.back.setOnClickListener(view -> finish());
        binding.iv1.setLongClickable(true);
        String versionName;
        boolean debug = false;
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_CONFIGURATIONS).versionName;
            debug = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA).metaData.getBoolean("DEBUG");

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versionName = getString(R.string.version);
        }
        binding.version.setText(versionName);

        if (debug) {
            binding.iv1.setOnLongClickListener(view -> {
                Demo demo = new Demo(AboutActivity.this);
                int cnt = 400;
                DateTime now = DateTime.now();
                demo.createRandomBill(cnt, now.minusMonths(6), now);
                DesktopWidget.refresh(getApplicationContext());
                Toast.makeText(AboutActivity.this, "增加了" + cnt + "个演示数据", Toast.LENGTH_SHORT).show();
                return false;
            });
        }
    }
}

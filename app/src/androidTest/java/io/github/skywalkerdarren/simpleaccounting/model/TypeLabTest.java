package io.github.skywalkerdarren.simpleaccounting.model;

import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author darren
 * @date 2018/3/31
 */
public class TypeLabTest {
    private Context mContext;
    private static final String TAG = "TypeLabTest";

    @Before
    public void setUp() {
        mContext = ApplicationProvider.getApplicationContext();
    }

    @Test
    public void getTypes() throws Exception {
        List<Type> types = TypeLab.getInstance(mContext).getTypes(false);
        for (Type type : types) {
            Log.d(TAG, type.getName());
        }
    }
}
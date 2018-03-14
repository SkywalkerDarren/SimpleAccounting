package io.github.skywalkerdarren.simpleaccounting.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.math.BigDecimal;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.CalculateUtil;

/**
 * Created by darren on 2018/3/10.
 */

public class NumPad extends LinearLayout {
    private EditText mEditText;
    private Context mContext;
    private final float y = getTranslationY();


    public NumPad(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        LayoutInflater.from(mContext).inflate(R.layout.num_pad, this, true);

        KeyboardView keyboardView = findViewById(R.id.num_keyboard);
        Keyboard keyboard = new Keyboard(mContext, R.xml.keyboard);

        keyboardView.setKeyboard(keyboard);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setOnKeyboardActionListener(new KeyboardView.OnKeyboardActionListener() {
            @Override
            public void onPress(int i) {

            }

            @Override
            public void onRelease(int i) {

            }

            @Override
            public void onKey(int i, int[] ints) {
                Editable editable = mEditText.getText();

                int position = mEditText.getSelectionStart();

                switch (i) {
                    case Keyboard.KEYCODE_DELETE:
                        if (editable != null && editable.length() > 0 && position > 0) {
                            editable.delete(position - 1, position);
                        }
                        break;
                    case Keyboard.KEYCODE_CANCEL:
                        editable.clear();
                        break;
                    case Keyboard.KEYCODE_DONE:
                        BigDecimal result = BigDecimal.ZERO;
                        try {
                            result = CalculateUtil.getResult(() -> processExp());
                        } catch (Exception e) {
                            Toast.makeText(mContext, "计算错误", Toast.LENGTH_SHORT).show();
                        }
                        editable.clear();
                        editable.append(result.toString());
                        mEditText.clearFocus();
                        hideKeyboard();
                        break;
                    case '/':
                        editable.insert(position, "÷");
                        break;
                    case '*':
                        editable.insert(position, "×");
                        break;
                    default:
                        editable.insert(position, String.valueOf((char) i));
                        break;
                }

                if (editable != null) {
                    if (!CalculateUtil.dynamicCheckExperssion(editable.toString())) {
                        int index = editable.length();
                        if (editable != null && editable.length() > 0) {
                            editable.delete(index - 1, index);
                        }
                    }
                }
            }

            @Override
            public void onText(CharSequence charSequence) {

            }

            @Override
            public void swipeLeft() {

            }

            @Override
            public void swipeRight() {

            }

            @Override
            public void swipeDown() {

            }

            @Override
            public void swipeUp() {

            }
        });

    }

    private String processExp() {
        String exp = mEditText.getText().toString()
                .replaceAll("×", "*")
                .replaceAll("÷", "/");
        return exp;
    }

    public void hideKeyboard() {

        int visibility = getVisibility();
        if (visibility == View.VISIBLE) {
            setVisibility(View.GONE);
        }
        setAlpha(1);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(this, "alpha", 1, 0);
        ObjectAnimator slide = ObjectAnimator.ofFloat(this, "translationY", y, y + 100);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, slide);
        set.setDuration(100);
        set.start();
    }


    public void hideSysKeyboard() {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindowToken(), 0);
    }

    public void showKeyboard() {
        int visibility = getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            setVisibility(View.VISIBLE);
        }
        setAlpha(0);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(this, "alpha", 0, 1);
        ObjectAnimator slide = ObjectAnimator.ofFloat(this, "translationY", y + 100, y);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, slide);
        set.setDuration(100);
        set.start();
    }

    public void setStrReceiver(EditText balanceEditText) {
        mEditText = balanceEditText;
    }
}

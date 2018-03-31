package io.github.skywalkerdarren.simpleaccounting.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;

/**
 * 账单适配器
 * 将帐单列表适配到recycler view
 *
 * @author darren
 * @date 2018/2/12
 */

public class BillAdapter extends BaseMultiItemQuickAdapter<BillInfo, BaseViewHolder> implements View.OnTouchListener {

    /**
     * 不带备注的账单
     */
    static final int WITHOUT_REMARK = 0;

    /**
     * 带备注的账单
     */
    static final int WITH_REMARK = 1;

    /**
     * 分隔符
     */
    public static final int HEADER = 2;

    private int mX;
    private int mY;

    /**
     * 将帐单列表适配到适配器中
     *
     * @param bills 含分隔符的账单信息列表
     */
    public BillAdapter(List<BillInfo> bills) {
        super(bills);
        addItemType(WITH_REMARK, R.layout.list_bill_item);
        addItemType(WITHOUT_REMARK, R.layout.list_bill_item_without_remark);
        addItemType(HEADER, R.layout.list_bill_header);
    }

    /**
     * 设置新帐单
     *
     * @param bills 含分隔符的账单信息列表
     */
    public void setBills(List<BillInfo> bills) {
        setNewData(bills);
    }

    @Override
    protected void convert(BaseViewHolder helper, BillInfo item) {
        switch (item.getItemType()) {
            case WITH_REMARK:
                helper.setText(R.id.remark_text_view, item.getRemark());
            case WITHOUT_REMARK:
                helper.setTextColor(R.id.balance_edit_text, item.isExpense() ?
                        Color.rgb(0xff, 0x70, 0x43) :
                        Color.rgb(0x7C, 0xB3, 0x42));
                helper.setImageResource(R.id.type_image_view, item.getBillTypeResId());
                helper.setText(R.id.title_text_view, item.getTitle());
                helper.setText(R.id.balance_edit_text, item.getBalance());
                helper.addOnClickListener(R.id.content_card_view);
                helper.addOnClickListener(R.id.image_card_view);
                helper.addOnLongClickListener(R.id.content_card_view);
                helper.addOnLongClickListener(R.id.image_card_view);
                helper.setAlpha(R.id.bill_item, 0);
                helper.setOnTouchListener(R.id.content_card_view, this);
                helper.setOnTouchListener(R.id.image_card_view, this);
                break;
            case HEADER:
                helper.setText(R.id.bills_date_text_view, item.getDateTime().toString("yyyy-MM-dd"));
                helper.setText(R.id.bill_expense_text_view, item.getExpense());
                helper.setText(R.id.bill_income_text_view, item.getIncome());
                break;
            default:
                break;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        mX = (int) motionEvent.getRawX();
        mY = (int) motionEvent.getRawY();
        return false;
    }

    /**
     * 获得在屏幕中的x坐标
     *
     * @return x坐标
     */
    public int getX() {
        return mX;
    }

    /**
     * 获得在屏幕中的y坐标
     *
     * @return y坐标
     */
    public int getY() {
        return mY;
    }


}

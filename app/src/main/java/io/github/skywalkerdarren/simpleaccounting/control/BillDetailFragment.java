package io.github.skywalkerdarren.simpleaccounting.control;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.Type;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BillDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BillDetailFragment extends Fragment {
    private static final String ARG_BILL = "bill";

    // TODO: Rename and change types of parameters
    private Bill mBill;
    private ImageView mTypeImageView;
    private TextView mTitleTextView;
    private TextView mDateTextView;
    private TextView mBalanceTextView;
    private TextView mRemarkTextView;
    private CardView mTitleCardView;
    private CardView mDetailCardView;
    private CardView mRemarkCardView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param bill 存储的账单
     * @return A new instance of fragment BillDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BillDetailFragment newInstance(Bill bill) {
        BillDetailFragment fragment = new BillDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_BILL, bill);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) {
            return;
        }
        mBill = (Bill) getArguments().getSerializable(ARG_BILL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill_detail, container, false);
        mTypeImageView = view.findViewById(R.id.type_image_view);
        mBalanceTextView = view.findViewById(R.id.balance_text_view);
        mDateTextView = view.findViewById(R.id.bill_date_text_view);
        mTitleTextView = view.findViewById(R.id.title_text_view);
        mRemarkTextView = view.findViewById(R.id.bill_remark_text_view);
        mTitleCardView = view.findViewById(R.id.title_card_view);
        mDetailCardView = view.findViewById(R.id.detail_card_view);
        mRemarkCardView = view.findViewById(R.id.remark_card_view);

        mTypeImageView.setImageResource(Type.getType().get(mBill.getType()));
        mBalanceTextView.setText(mBill.getBalance().toString());
        mBalanceTextView.setTextColor(mBill.isExpense() ? Color.RED : Color.GREEN);
        mRemarkTextView.setText(mBill.getRemark());
        mTitleTextView.setText(mBill.getName());
        mDateTextView.setText(mBill.getDate().toString("yyyy-MM-dd hh:mm"));
        return view;
    }

}

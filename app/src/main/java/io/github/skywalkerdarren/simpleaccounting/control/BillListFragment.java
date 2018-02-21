package io.github.skywalkerdarren.simpleaccounting.control;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;

import org.joda.time.DateTime;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.BillLab;

import static com.chad.library.adapter.base.BaseQuickAdapter.ALPHAIN;
import static io.github.skywalkerdarren.simpleaccounting.control.BillAdapter.HEADER;

/**
 * Created by darren on 2018/1/31.
 */

public class BillListFragment extends Fragment implements View.OnClickListener {
    private RecyclerView mBillListRecyclerView;
    private BillAdapter mBillAdapter;
    private TextView mAddBillTextView;
    BillLab mBillLab;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill_list, container, false);
        mAddBillTextView = view.findViewById(R.id.add_bill_text_view);
        mBillListRecyclerView = view.findViewById(R.id.bill_recycle_view);

        mAddBillTextView.setOnClickListener(this);

        mBillListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        mBillListRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mBillListRecyclerView.addItemDecoration(new PinnedHeaderItemDecoration.Builder(HEADER).create());
        return view;
    }

    public void updateUI() {
        DateTime now = DateTime.now();
        List<Bill> bills = mBillLab.getsBills(now.getYear(), now.monthOfYear().get());
        List<BillAdapter.BillInfo> billInfoList = BillAdapter.BillInfo.getBillInfoList(bills, mBillLab);
        if (billInfoList.size() < 1) {
            mAddBillTextView.setVisibility(View.VISIBLE);
            mBillListRecyclerView.setVisibility(View.INVISIBLE);
            return;
        } else {
            mAddBillTextView.setVisibility(View.GONE);
            mBillListRecyclerView.setVisibility(View.VISIBLE);
        }
        if (mBillAdapter == null) {
            mBillAdapter = new BillAdapter(billInfoList);
            mBillAdapter.openLoadAnimation(ALPHAIN);
            mBillListRecyclerView.setAdapter(mBillAdapter);
        } else {
            mBillAdapter.setBills(billInfoList);
            mBillAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mBillLab = BillLab.getInstance(getActivity());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_bill, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public static Fragment newInstance() {
        return new BillListFragment();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_bill_text_view:
                Bill bill = BillLab.createRandomBill(233);
                mBillLab.addBill(bill);
                updateUI();
                break;
            default:
                break;
        }
    }
}

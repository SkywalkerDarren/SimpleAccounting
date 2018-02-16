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

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import org.joda.time.DateTime;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.BillLab;

/**
 * Created by darren on 2018/1/31.
 */

public class BillListFragment extends Fragment {
    private RecyclerView mBillListRecyclerView;
    private BillAdapter mBillAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill_list, container, false);

        mBillListRecyclerView = view.findViewById(R.id.bill_recycle_view);
        mBillListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        mBillListRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mBillListRecyclerView.addItemDecoration(new StickyRecyclerHeadersDecoration(mBillAdapter));
        return view;
    }

    private void updateUI() {
        BillLab lab = BillLab.getInstance(getActivity());
        DateTime now = DateTime.now();
        List<Bill> bills = lab.getsBills(now.getYear(), now.monthOfYear().get());
        if (mBillAdapter == null) {
            mBillAdapter = new BillAdapter(bills);
            mBillListRecyclerView.setAdapter(mBillAdapter);
        } else {
            mBillAdapter.setBills(bills);
            mBillAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_bill, menu);
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


}

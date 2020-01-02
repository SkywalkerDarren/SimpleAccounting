package io.github.skywalkerdarren.simpleaccounting.adapter;

import android.animation.ObjectAnimator;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.listener.OnItemDragListener;

import org.joda.time.DateTime;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.base.BaseDraggableDataBindingAdapter;
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemAccountBinding;
import io.github.skywalkerdarren.simpleaccounting.model.AppRepository;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;
import io.github.skywalkerdarren.simpleaccounting.util.AppExecutors;
import io.github.skywalkerdarren.simpleaccounting.util.FormatUtil;

/**
 * @author darren
 * @date 2018/3/24
 */

public class AccountAdapter extends BaseDraggableDataBindingAdapter<Account, ItemAccountBinding> {
    private final AppRepository mRepository;

    public AccountAdapter() {
        super(R.layout.item_account, null);
        mRepository = AppRepository.getInstance(new AppExecutors(), mContext);
        setOnItemDragListener(new OnItemDragListener() {

            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
                float st = viewHolder.itemView.getElevation();
                itemRaiseAnimator(viewHolder.itemView, st, true);
            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {
                mRepository.changePosition(getItem(from), getItem(to));

            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
                float ed = viewHolder.itemView.getElevation();
                itemRaiseAnimator(viewHolder.itemView, ed, false);
            }
        });
    }


    private void itemRaiseAnimator(View view, final float start, boolean raise) {
        final float end = start * 2;
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "elevation",
                raise ? start : end, raise ? end : start);
        animator.setDuration(50);
        animator.start();
    }

    @Override
    protected void convert(ItemAccountBinding binding, Account item) {
        binding.setAccount(item);
        mRepository.getAccountStats(item.getUuid(), new DateTime(0), DateTime.now(), accountStats ->
                binding.balanceTextView.setText(FormatUtil.getNumeric(accountStats.getSum())));
    }
}

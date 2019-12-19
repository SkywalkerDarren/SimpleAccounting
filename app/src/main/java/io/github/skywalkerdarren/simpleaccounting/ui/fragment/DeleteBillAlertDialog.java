package io.github.skywalkerdarren.simpleaccounting.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.model.Database.AccountDatabase;
import io.github.skywalkerdarren.simpleaccounting.model.dao.BillDao;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill;
import io.github.skywalkerdarren.simpleaccounting.ui.DesktopWidget;

/**
 * 删除帐单对话框
 *
 * @author darren
 * @date 2018/3/9
 */

public class DeleteBillAlertDialog extends DialogFragment {
    private final static String ARG_BILL_ID = "id";
    private UUID mBillId;
    private static final String TAG = "DeleteBillAlertDialog";

    public static DeleteBillAlertDialog newInstance(UUID billId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_BILL_ID, billId);
        DeleteBillAlertDialog fragment = new DeleteBillAlertDialog();
        fragment.setArguments(args);
        return fragment;
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null) {
            return;
        }
        // 通过请求代码，为当前请求返回结果代码，并传递包含返回消息的intent
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, null);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BillDao dao = AccountDatabase.getInstance(getContext()).billDao();
        mBillId = (UUID) getArguments().getSerializable(ARG_BILL_ID);
        return new AlertDialog.Builder(getActivity())
                .setTitle("确认删除")
                .setMessage("确定永久删除此账单？")
                .setNegativeButton(android.R.string.cancel, (dialogInterface, i) ->
                        sendResult(Activity.RESULT_CANCELED))
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    // 删除帐单
                    dao.deleteBill(new Bill(mBillId));
                    DesktopWidget.refresh(getContext());
                    sendResult(Activity.RESULT_OK);
                })
                .create();
    }
}

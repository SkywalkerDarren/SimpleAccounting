package io.github.skywalkerdarren.simpleaccounting.view_model;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.AppRepository;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Type;

/**
 * @author darren
 * @date 2018/4/4
 */

public class BillEditViewModel extends ViewModel {
    private final AppRepository mRepository;
    private final MutableLiveData<String> typeName = new MutableLiveData<>();
    private final MutableLiveData<String> typeImg = new MutableLiveData<>();
    private final MutableLiveData<String> accountImg = new MutableLiveData<>();
    private final MutableLiveData<Integer> accountColor = new MutableLiveData<>(R.color.black);
    private final MutableLiveData<String> balance = new MutableLiveData<>();
    private final MutableLiveData<DateTime> date = new MutableLiveData<>();
    private final MutableLiveData<String> remark = new MutableLiveData<>();
    private final MutableLiveData<UUID> billId = new MutableLiveData<>();
    private final MutableLiveData<UUID> accountId = new MutableLiveData<>();
    private final MutableLiveData<UUID> typeId = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isExpense = new MutableLiveData<>();
    private final MutableLiveData<List<Type>> expenseTypes = new MutableLiveData<>();
    private final MutableLiveData<List<Type>> incomeTypes = new MutableLiveData<>();
    private final MutableLiveData<List<Account>> accounts = new MutableLiveData<>();
    private boolean isNewBill;


    public BillEditViewModel(AppRepository repository) {
        mRepository = repository;
        mRepository.getTypes(true, list -> expenseTypes.setValue(list));
        mRepository.getTypes(false, list -> incomeTypes.setValue(list));
        mRepository.getAccounts(accountList -> accounts.setValue(accountList));
    }

    public void setBill(@NonNull Bill bill) {
        if (bill.getDate() == null) {
            isNewBill = true;
            bill.setDate(DateTime.now());
        } else {
            isNewBill = false;
        }
        mRepository.getAccount(bill.getAccountId(), this::setAccount);
        mRepository.getType(bill.getTypeId(), this::setType);
        balance.setValue(bill.getBalance() == null ? null : bill.getBalance().toString());
        date.setValue(bill.getDate());
        remark.setValue(bill.getRemark());
        billId.setValue(bill.getUuid());
    }

    /**
     * @param account 设置的账户
     */
    public void setAccount(@NonNull Account account) {
        accountImg.setValue(account.getBitmap());
        accountColor.setValue(account.getColorId());
        accountId.setValue(account.getUuid());
    }

    /**
     * @param type 设置类型
     */
    public void setType(@NonNull Type type) {
        isExpense.setValue(type.getIsExpense());
        typeName.setValue(type.getName());
        typeImg.setValue(Type.FOLDER + type.getAssetsName());
        typeId.setValue(type.getUUID());
    }


    public boolean isNewBill() {
        return isNewBill;
    }

    /**
     * @return 类型名
     */
    public MutableLiveData<String> getTypeName() {
        return typeName;
    }

    /**
     * @return 类型图id
     */
    public MutableLiveData<String> getTypeImg() {
        return typeImg;
    }

    /**
     * @return 账户图id
     */
    public MutableLiveData<String> getAccountImg() {
        return accountImg;
    }

    /**
     * @return 账户背景色值
     */
    public MutableLiveData<Integer> getAccountColor() {
        return accountColor;
    }

    /**
     * @return 账单收支
     */
    public MutableLiveData<String> getBalance() {
        return balance;
    }

    /**
     * @return 账单日期
     */
    public MutableLiveData<DateTime> getDate() {
        return date;
    }

    /**
     * @param date 设置账单日期
     */
    public void setDate(DateTime date) {
        this.date.setValue(date);
    }

    /**
     * @return 账单备注
     */
    public MutableLiveData<String> getRemark() {
        return remark;
    }

    /**
     * 保存账单
     */
    public boolean saveBill() {
        if (TextUtils.isEmpty(balance.getValue())) {
            return false;
        }
        Bill bill = new Bill();
        try {
            BigDecimal r = new BigDecimal(balance.getValue());
            bill.setBalance(r);
            bill.setName(getTypeName().getValue());
            bill.setDate(getDate().getValue());
            bill.setRemark(getRemark().getValue());
            bill.setTypeId(typeId.getValue());
            bill.setAccountId(accountId.getValue());
        } catch (Exception e) {
            //Toast.makeText(mContext, "表达式错误", Toast.LENGTH_SHORT).show();
            return false;
        }
        // 刷新账单数据库
        mRepository.getBill(billId.getValue(), b -> {
            if (b == null) {
                mRepository.addBill(bill);
            } else {
                bill.setId(b.getId());
                bill.setUuid(b.getUuid());
                mRepository.updateBill(bill);
            }
        });
        return true;
    }

    /**
     * @return true为支出类型
     */
    public LiveData<Boolean> getExpense() {
        return isExpense;
    }

    @NonNull
    public LiveData<List<Type>> getTypes(boolean b) {
        return b ? expenseTypes : incomeTypes;
    }

    @NonNull
    public LiveData<List<Account>> getAccounts() {
        return accounts;
    }
}

package io.github.skywalkerdarren.simpleaccounting.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;

@Dao
public interface AccountDao {
    @Query("SELECT * FROM account WHERE uuid == :uuid")
    Account getAccount(UUID uuid);
    @Query("SELECT * FROM account")
    List<Account> getAccounts();
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateAccount(Account account);
    @Delete
    void delAccount(Account account);
    @Insert
    void newAccount(Account account);
}

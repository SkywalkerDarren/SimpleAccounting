package io.github.skywalkerdarren.simpleaccounting.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;

@Dao
public interface AccountDao {
    @Query("SELECT * FROM account WHERE uuid == :uuid")
    Account getAccount(UUID uuid);

    @Query("SELECT * FROM account ORDER BY id")
    List<Account> getAccounts();
    @Query("UPDATE OR REPLACE account SET id = :id WHERE uuid = :uuid")
    void updateAccountId(UUID uuid, Integer id);
    @Query("DELETE FROM account WHERE uuid = :uuid")
    void delAccount(UUID uuid);
    @Insert
    void newAccount(Account account);
}

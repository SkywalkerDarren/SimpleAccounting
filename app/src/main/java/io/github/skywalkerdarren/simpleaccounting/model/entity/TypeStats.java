package io.github.skywalkerdarren.simpleaccounting.model.entity;

import androidx.room.ColumnInfo;

import java.math.BigDecimal;
import java.util.UUID;

public class TypeStats {
    @ColumnInfo(name = "type_id")
    private UUID typeId;
    private BigDecimal balance;

    public UUID getTypeId() {
        return typeId;
    }

    public void setTypeId(UUID typeId) {
        this.typeId = typeId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "TypeStats{" +
                "typeId=" + typeId +
                ", balance=" + balance +
                '}';
    }
}

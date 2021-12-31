package com.hcmus.group14.moneytor.data.model.relation;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

/*
 * Each row in the cross-reference table corresponds
 * to a pairing of a Spending instance and a Relate instance
 * where the referenced relate is included in the referenced spending.
 * */
@Entity(
        tableName = "spending_relate",
        primaryKeys = {"spending_id", "rel_id"})
public class SpendingRelateCrossRef {
    @ColumnInfo(name = "spending_id")
    public int spendingId;
    @ColumnInfo(name = "rel_id")
    public int relateId;

    public SpendingRelateCrossRef(int spendingId, int relateId) {
        this.spendingId = spendingId;
        this.relateId = relateId;
    }

    @Ignore
    public SpendingRelateCrossRef() {
        // Required by Firestore. Do not remove
        this(0, 0);
    }
}

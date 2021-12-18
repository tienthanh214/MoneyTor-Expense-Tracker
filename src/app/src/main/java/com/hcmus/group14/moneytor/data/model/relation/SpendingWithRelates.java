package com.hcmus.group14.moneytor.data.model.relation;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.hcmus.group14.moneytor.data.model.Relate;
import com.hcmus.group14.moneytor.data.model.Spending;

import java.util.List;

public class SpendingWithRelates {
    @Embedded public Spending spending;
    @Relation(
            parentColumn = "spending_id",
            entityColumn = "rel_id",
            associateBy = @Junction(SpendingRelateCrossRef.class)
    )
    public List<Relate> relates;

    @NonNull
    @Override
    public String toString() {
        return "SpendingWithRelates{" +
                "spending=" + spending.toString() +
                ", relates=" + relates.toString() +
                '}';
    }
}

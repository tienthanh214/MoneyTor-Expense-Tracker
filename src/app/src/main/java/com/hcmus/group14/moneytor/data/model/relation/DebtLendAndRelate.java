package com.hcmus.group14.moneytor.data.model.relation;

import com.hcmus.group14.moneytor.data.model.DebtLend;
import com.hcmus.group14.moneytor.data.model.Relate;
import androidx.room.*;

public class DebtLendAndRelate {
    @Embedded public DebtLend debtLend;
    @Relation(
            parentColumn = "target",
            entityColumn = "rel_id"
    ) public Relate relate;
}

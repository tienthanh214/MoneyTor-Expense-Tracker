package com.hcmus.group14.moneytor.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "relate_table")
public class Relate implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rel_id")
    int relateId;
    @ColumnInfo(name = "name")
    String name;
    @ColumnInfo(name = "tel")
    String tel;

    public Relate(String name, String tel) {
        this.name = name;
        this.tel = tel;
    }

    public int getRelateId() {
        return relateId;
    }

    public String getName() {
        return name;
    }

    public String getTel() {
        return tel;
    }

    public void setRelateId(int relateId) {
        this.relateId = relateId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @NonNull
    @Override
    public String toString() {
        return "Relate{" +
                "relateId=" + relateId +
                ", name='" + name + '\'' +
                ", tel='" + tel + '\'' +
                '}';
    }
}

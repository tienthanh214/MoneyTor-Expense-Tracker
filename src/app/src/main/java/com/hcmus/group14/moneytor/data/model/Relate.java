package com.hcmus.group14.moneytor.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "relate_table",
        indices = {@Index(value = "tel", unique = true)})
public class Relate implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rel_id")
    int relateId;
    @ColumnInfo(name = "name")
    String name;
    @ColumnInfo(name = "tel")
    String tel;

    @Ignore
    public Relate() {
        // Required by Firestore. Do not remove
        this("", "");
    }

    public Relate(String name, String tel) {
        this.name = name;
        this.tel = tel;
    }

    public int getRelateId() {
        return relateId;
    }

    public void setRelateId(int relateId) {
        this.relateId = relateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
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

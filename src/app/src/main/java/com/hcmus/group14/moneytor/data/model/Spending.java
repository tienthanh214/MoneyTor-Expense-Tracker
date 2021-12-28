package com.hcmus.group14.moneytor.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "spending_table")
public class Spending implements Note {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "spending_id")
    int spendingId;
    @ColumnInfo(name = "cost")
    long cost;
    @ColumnInfo(name = "title")
    String title;
    @ColumnInfo(name = "category")
    String category;
    @ColumnInfo(name = "description")
    String description;
    @ColumnInfo(name = "date")
    long date;

    @Ignore
    public Spending() {
        // Required by Firestore. Do not remove
        this(-1, "", "", "", -1);
    }

    public Spending(long cost, String title, String category, String description, long date) {
        this.cost = cost;
        this.title = title;
        this.category = category;
        this.description = description;
        this.date = date;
    }

    public int getSpendingId() {
        return spendingId;
    }

    public long getCost() {
        return cost;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public long getDate() {
        return date;
    }

    public void setSpendingId(int spendingId) {
        this.spendingId = spendingId;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @NonNull
    @Override
    public String toString() {
        return "Spending{" +
                "spendingId=" + spendingId +
                ", cost=" + cost +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                '}';
    }
}

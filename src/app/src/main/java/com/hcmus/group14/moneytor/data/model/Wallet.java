package com.hcmus.group14.moneytor.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "wallet_table")
public class Wallet {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "wallet_id")
    int walletId;
    @ColumnInfo(name = "type")
    boolean type; // false if physical wallet
    @ColumnInfo(name = "provider")
    String provider;
    @ColumnInfo(name = "balance")
    long balance;

    @Ignore
    public Wallet() {
        // Required by Firestore. Do not remove
        this(true, "", 0);
    }

    public Wallet(boolean type, String provider, long balance) {
        this.type = type;
        this.provider = provider;
        this.balance = balance;
    }

    // --- getter ---
    public int getWalletId() {
        return walletId;
    }

    // --- setter ---
    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public void addIncome(long value) {
        this.balance += value;
    }

    public void removeOutcome(long value) {
        this.balance -= value;
    }
}

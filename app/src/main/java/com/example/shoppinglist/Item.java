package com.example.shoppinglist;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Object representation of Item
@Entity
public class Item {
    @NonNull
    @PrimaryKey(autoGenerate=true)
    private long id;
    @NonNull
    private String name;
    @NonNull
    private String quantity;
    @NonNull
    private String unit;
    @NonNull
    private String store;

    public Item(@NonNull String name, @NonNull String quantity, @NonNull String unit, @NonNull String store) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.store = store;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }
}

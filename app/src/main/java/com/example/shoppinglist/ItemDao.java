package com.example.shoppinglist;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// Dao to correctly query from the database
@Dao
public interface ItemDao {
    @Insert
    void insertAll(Item items);

    @Delete
    void delete(Item product);

    @Query("SELECT * FROM item ORDER BY id")
    List<Item> getAll();

    /**
     * insert new or update existing products
     * @param product
     */
    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insertProduct(Item product);

    @Update
    void updateProduct(Item product);
}
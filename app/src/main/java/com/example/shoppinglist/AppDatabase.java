package com.example.shoppinglist;

import androidx.room.Database;
import androidx.room.RoomDatabase;

// Direct access to underlying database implementation
@Database(entities = {Item.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ItemDao itemDao();
}

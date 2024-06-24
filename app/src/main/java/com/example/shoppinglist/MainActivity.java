package com.example.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

// IMPORTANT: Deletion of shopping items is not implemented yet
public class MainActivity extends AppCompatActivity {

    // Variable definition
    List<Item> products = new ArrayList<>();
    ArrayAdapter<Item> listAdapter;
    ListView listView;
    AppDatabase db;
    ItemDao dao;
    int ADD_ITEM_CODE = 42;

    // onCreate method (gets executed at the start of this activity)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Create database object with the help of the "itemDao" class
        db = Room.databaseBuilder(this, AppDatabase.class, "shopping")
                .fallbackToDestructiveMigration()
                .build();
        dao = db.itemDao();

        // Get list for the entries and bind a ListAdapter to it
        listView = findViewById(R.id.productList);
        listAdapter = new ArrayAdapter<Item>(this, R.layout.productlist, R.id.productName, products) {
            @Override
            public View getView(int pos, View convertView, ViewGroup parent) {
                View result = super.getView(pos, convertView, parent);
                Item currentItem = products.get(pos);
                TextView productName = result.findViewById(R.id.productName);
                productName.setText(currentItem.getName());

                TextView productQuantity = result.findViewById(R.id.productQuan);
                productQuantity.setText(currentItem.getQuantity());

                TextView productUnit = result.findViewById(R.id.productUnit);
                productUnit.setText(String.valueOf(currentItem.getUnit()));

                TextView productWhere = result.findViewById(R.id.productWhere);
                productWhere.setText(String.valueOf(currentItem.getQuantity()));

                return result;
            }
        };
        listView.setAdapter(listAdapter);
        // Refresh the list
        refreshListView();
    }

    // Cleanup for the end of the activity
    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    // On activityResult method
    // Gets executed if the AddActivity is finished
    // Exact code can be found on line 56 in the AddActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check if the activity ended with a RESULT_OK (Add Product button clicked)
        if (requestCode == ADD_ITEM_CODE && resultCode == RESULT_OK) {
            // Get the JSON from the AddActivity
            String stringData = data.getStringExtra("ITEM").trim();
            if (!stringData.isEmpty()) {
                try {
                    // Parse JSON and insert into the database
                    Gson gson = new Gson();
                    Item newItem = gson.fromJson(stringData, Item.class);
                    insertNewProduct(newItem);
                } catch (Exception ex) {
                    Log.e("", ("an error occured: " + ex.toString()));
                }
            }
        }
    }

    // Start new thread and insert product
    // The thread is getting started to no lock the ui / block user interactions
    public void insertNewProduct(Item product) {
        (new Thread(() -> {
            dao.insertProduct(product);
            refreshListView();
        })).start();
    }

    // Start new thread to get all products in db and tell the listAdapter to update its values
    public void refreshListView(){
        (new Thread(() -> {
            List<Item> newList = dao.getAll();
            runOnUiThread(() -> {
                products.clear();
                products.addAll(newList);
                listAdapter.notifyDataSetChanged();
            });
        })).start();
    }

    // Button to switch to the AddActivity
    public void goToAddItemPage(View v) {
        Intent switchActivity = new Intent(this, AddActivity.class);
        startActivityForResult(switchActivity, ADD_ITEM_CODE);
    }
}
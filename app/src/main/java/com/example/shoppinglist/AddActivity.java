package com.example.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class AddActivity extends AppCompatActivity {
    int RESULT_ERROR = 43;

    // onCreate method gets executed on creation of this activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Cancel the insert
    public void cancelInsert(View v) {
        this.finish();
    }

    // Send the item to the MainActivity where it gets saved in the db
    public void saveButtonClicked(View v) {
        // Get all text fields
        EditText product = findViewById(R.id.productInput);
        EditText quantity = findViewById(R.id.quantityInput);
        EditText unit = findViewById(R.id.unitInput);
        EditText location = findViewById(R.id.locationInput);

        // Check if the given values are valid (used for user feedback)
        isValidProduct();
        isValidQuantity();

        // If the values are valid -> execute following code
        if (isValidProduct() && isValidQuantity()) {
            // Create instance of Item Object with given values
            Item item = new Item(
                    product.getText().toString(),
                    quantity.getText().toString(),
                    unit.getText().toString(),
                    location.getText().toString()
            );

            // Parse the object with GSON (Google JSON library)
            Gson gson = new Gson();
            String jsonItem = gson.toJson(item);
            // Create new intent to send to the MainActivity
            Intent i = new Intent();
            // Include the JSON as an extra in the intent
            i.putExtra("ITEM", jsonItem);
            // Set result of the intent
            setResult(RESULT_OK, i);
            // Finish the activity
            finish();
        }
    }

    // Check if the given quantity is valid
    public boolean isValidQuantity() {
        EditText quantity = findViewById(R.id.quantityInput);
        if (quantity.getText().toString().trim().isEmpty()) {
            quantity.setError("Quantity is empty");
            return false;
        } else {
            quantity.setError(null);
            return true;
        }
    }

    // Check if the given product is valid
    public boolean isValidProduct() {
        EditText product = findViewById(R.id.productInput);
        if (product.getText().toString().trim().length() <= 2) {
            product.setError("Productname is too short");
            return false;
        } else {
            product.setError(null);
            return true;
        }
    }
}

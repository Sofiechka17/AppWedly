package com.example.applicationwedly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        TextView totalPrice = findViewById(R.id.totalPrice);
        totalPrice.setText("₽ 30000");

        Button checkoutBtn = findViewById(R.id.btnCheckout);
        checkoutBtn.setOnClickListener(v -> {
            android.widget.Toast.makeText(this, "Заказ оформлен!", android.widget.Toast.LENGTH_SHORT).show();
        });

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        LinearLayout btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(v -> {
            startActivity(new Intent(CartActivity.this, MainScreenActivity.class));
            finish();
        });

        LinearLayout btnCart = findViewById(R.id.btnCart);
        btnCart.setOnClickListener(v -> {
            // Уже на странице корзины
        });

        LinearLayout btnFavorites = findViewById(R.id.btnFavorites);
        btnFavorites.setOnClickListener(v -> {
            startActivity(new Intent(CartActivity.this, FavoritesActivity.class));
        });

        LinearLayout btnProfile = findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(v -> {
            startActivity(new Intent(CartActivity.this, ProfileActivity.class));
        });
    }
}
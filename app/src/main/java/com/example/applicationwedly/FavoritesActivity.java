package com.example.applicationwedly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        GridView favoritesGrid = findViewById(R.id.favoritesGrid);

        List<Product> favorites = new ArrayList<>();
        favorites.add(new Product("Свадебный торт", "₽ 10000", R.drawable.cake_image, 5.0f));
        favorites.add(new Product("Букет невесты", "₽ 7000", R.drawable.flowers_image, 4.9f));
        favorites.add(new Product("Обручальные кольца", "₽ 30000", R.drawable.rings_image, 5.0f));

        RecommendationAdapter adapter = new RecommendationAdapter(this, favorites);
        favoritesGrid.setAdapter(adapter);

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        LinearLayout btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(v -> {
            startActivity(new Intent(FavoritesActivity.this, MainScreenActivity.class));
            finish();
        });

        LinearLayout btnCart = findViewById(R.id.btnCart);
        btnCart.setOnClickListener(v -> {
            startActivity(new Intent(FavoritesActivity.this, CartActivity.class));
        });

        LinearLayout btnFavorites = findViewById(R.id.btnFavorites);
        btnFavorites.setOnClickListener(v -> {
            // Уже на странице избранного
        });

        LinearLayout btnProfile = findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(v -> {
            startActivity(new Intent(FavoritesActivity.this, ProfileActivity.class));
        });
    }
}
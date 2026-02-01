package com.example.applicationwedly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MensClothingActivity extends AppCompatActivity {

    private GridView productsGrid;
    private List<Product> products = new ArrayList<>();
    private Button btnSuits, btnShoes, btnAccessories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mens_clothing);

        initViews();
        setupTopIcons();
        setupCategoryTabs();
        setupProducts("suits");
        setupBottomNavigation();
    }

    private void initViews() {

        productsGrid = findViewById(R.id.productsGrid);
        btnSuits = findViewById(R.id.btnSuits);
        btnShoes = findViewById(R.id.btnShoes);
        btnAccessories = findViewById(R.id.btnAccessories);
    }

    private void setupTopIcons() {
        RelativeLayout searchIcon = findViewById(R.id.searchIcon);
        searchIcon.setOnClickListener(v -> {
            android.widget.Toast.makeText(this, "Поиск", android.widget.Toast.LENGTH_SHORT).show();
        });

        RelativeLayout bellIcon = findViewById(R.id.bellIcon);
        bellIcon.setOnClickListener(v -> {
            android.widget.Toast.makeText(this, "Уведомления", android.widget.Toast.LENGTH_SHORT).show();
        });
    }

    private void setupCategoryTabs() {
        btnSuits.setOnClickListener(v -> {
            updateActiveTab(btnSuits);
            setupProducts("suits");
        });

        btnShoes.setOnClickListener(v -> {
            updateActiveTab(btnShoes);
            setupProducts("shoes");
        });

        btnAccessories.setOnClickListener(v -> {
            updateActiveTab(btnAccessories);
            setupProducts("accessories");
        });

        updateActiveTab(btnSuits);
    }

    private void updateActiveTab(Button activeButton) {
        btnSuits.setTextColor(getResources().getColor(R.color.gray));
        btnShoes.setTextColor(getResources().getColor(R.color.gray));
        btnAccessories.setTextColor(getResources().getColor(R.color.gray));

        activeButton.setTextColor(getResources().getColor(R.color.primary_blue));
    }

    private void setupProducts(String category) {
        products.clear();

        if (category.equals("suits")) {
            String[] suitSizes = {"46", "48", "50", "52", "54", "56"};
            String suitDescription = "Элегантный мужской костюм для особых случаев.";

            products.add(new Product("Костюм свадебный", "39000", R.drawable.suit1, 4.9f,
                    suitSizes, suitDescription));
            products.add(new Product("Костюм классический", "50000", R.drawable.suit1, 5.0f,
                    suitSizes, suitDescription));
            products.add(new Product("Костюм деловой", "40000", R.drawable.suit1, 5.0f,
                    suitSizes, suitDescription));
            products.add(new Product("Костюм праздничный", "45000", R.drawable.suit1, 5.0f,
                    suitSizes, suitDescription));
        } else if (category.equals("shoes")) {
            String[] shoeSizes = {"38", "39", "40", "41", "42", "43", "44", "45"};
            String shoeDescription = "Стильные классические мужские туфли.";

            products.add(new Product("Туфли классические", "15000", R.drawable.suit1, 5.0f,
                    shoeSizes, shoeDescription));
            products.add(new Product("Обувь мужская", "7000", R.drawable.suit1, 5.0f,
                    shoeSizes, shoeDescription));
            products.add(new Product("Кожаные туфли", "12000", R.drawable.suit1, 4.8f,
                    shoeSizes, shoeDescription));
            products.add(new Product("Спортивная обувь", "9000", R.drawable.suit1, 4.7f,
                    shoeSizes, shoeDescription));
        } else if (category.equals("accessories")) {
            String accessoryDescription = "Элегантный аксессуар.";
            // Для аксессуаров нет размеров
            products.add(new Product("Запонки мужские", "5000", R.drawable.suit1, 5.0f,
                    new String[0], accessoryDescription));
            products.add(new Product("Часы мужские", "20000", R.drawable.suit1, 5.0f,
                    new String[0], accessoryDescription));
            products.add(new Product("Галстук", "2500", R.drawable.suit1, 4.9f,
                    new String[0], accessoryDescription));
            products.add(new Product("Бутоньерка", "10000", R.drawable.suit1, 5.0f,
                    new String[0], accessoryDescription));
        }

        RecommendationAdapter adapter = new RecommendationAdapter(this, products);
        productsGrid.setAdapter(adapter);

        productsGrid.setOnItemClickListener((parent, view, position, id) -> {
            Product product = products.get(position);
            Intent intent;

            // Для колец открываем RingsActivity, для остальных - ProductWithSizeActivity
            if (product.getName().toLowerCase().contains("кольц")) {
                intent = new Intent(MensClothingActivity.this, RingsActivity.class);
            } else if (product.hasSizes()) {
                // Для товаров с размерами
                intent = new Intent(MensClothingActivity.this, ProductWithSizeActivity.class);
                intent.putExtra("available_sizes", product.getAvailableSizes());
            } else {
                // Для товаров без размеров
                intent = new Intent(MensClothingActivity.this, ProductDetailActivity.class);
            }

            intent.putExtra("product_name", product.getName());
            intent.putExtra("product_price", product.getPrice());
            intent.putExtra("product_rating", product.getRating());
            intent.putExtra("product_image", product.getImageRes());
            intent.putExtra("product_description", product.getDescription());
            startActivity(intent);
        });
    }

    private void setupBottomNavigation() {
        LinearLayout btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(MensClothingActivity.this, MainScreenActivity.class);
            startActivity(intent);
            finish();
        });

        LinearLayout btnCart = findViewById(R.id.btnCart);
        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(MensClothingActivity.this, CartActivity.class);
            startActivity(intent);
        });

        LinearLayout btnFavorites = findViewById(R.id.btnFavorites);
        btnFavorites.setOnClickListener(v -> {
            Intent intent = new Intent(MensClothingActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });

        LinearLayout btnProfile = findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MensClothingActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }
}
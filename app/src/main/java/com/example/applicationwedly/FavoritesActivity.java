package com.example.applicationwedly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private GridView favoritesGrid;
    private List<Product> favorites = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        favoritesGrid = findViewById(R.id.favoritesGrid);

        // Создаем товары с размерами и описаниями
        String[] ringSizes = {"15", "15.5", "16", "16.5", "17", "17.5", "18", "18.5"};
        String ringDescription = "Эксклюзивные обручальные кольца с уникальной системой подбора размеров.";

        String[] shoeSizes = {"38", "39", "40", "41", "42", "43", "44", "45"};
        String shoeDescription = "Стильные классические туфли для особых моментов.";

        // Товары без размеров
        String cakeDescription = "Вкусный свадебный торт ручной работы.";
        String flowerDescription = "Красивый букет невесты из свежих цветов.";

        favorites.add(new Product("Свадебный торт", "₽ 10000", R.drawable.cake_image, 5.0f,
                new String[0], cakeDescription));
        favorites.add(new Product("Букет невесты", "₽ 7000", R.drawable.flowers_image, 4.9f,
                new String[0], flowerDescription));
        favorites.add(new Product("Обручальные кольца", "₽ 30000", R.drawable.rings_image, 5.0f,
                ringSizes, ringDescription));

        RecommendationAdapter adapter = new RecommendationAdapter(this, favorites);
        favoritesGrid.setAdapter(adapter);

        // Добавляем обработчик кликов
        favoritesGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = favorites.get(position);
                Intent intent;

                // Для колец открываем RingsActivity
                if (product.getName().toLowerCase().contains("кольц")) {
                    intent = new Intent(FavoritesActivity.this, RingsActivity.class);
                }
                // Для товаров с размерами - ProductWithSizeActivity
                else if (product.hasSizes()) {
                    intent = new Intent(FavoritesActivity.this, ProductWithSizeActivity.class);
                    intent.putExtra("available_sizes", product.getAvailableSizes());
                }
                // Для остальных - ProductDetailActivity
                else {
                    intent = new Intent(FavoritesActivity.this, ProductDetailActivity.class);
                }

                intent.putExtra("product_name", product.getName());
                intent.putExtra("product_price", product.getPrice());
                intent.putExtra("product_rating", product.getRating());
                intent.putExtra("product_image", product.getImageRes());
                intent.putExtra("product_description", product.getDescription());
                startActivity(intent);
            }
        });

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        LinearLayout btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FavoritesActivity.this, MainScreenActivity.class));
                finish();
            }
        });

        LinearLayout btnCart = findViewById(R.id.btnCart);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FavoritesActivity.this, CartActivity.class));
            }
        });

        LinearLayout btnFavorites = findViewById(R.id.btnFavorites);
        btnFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Уже на странице избранного
                Toast.makeText(FavoritesActivity.this, "Вы уже в избранном",
                        Toast.LENGTH_SHORT).show();
            }
        });

        LinearLayout btnProfile = findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FavoritesActivity.this, ProfileActivity.class));
            }
        });
    }
}
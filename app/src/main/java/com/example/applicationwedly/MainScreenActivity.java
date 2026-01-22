package com.example.applicationwedly;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class MainScreenActivity extends AppCompatActivity {

    private ViewPager2 bannerViewPager;
    private LinearLayout indicatorContainer;
    private GridView recommendationsGrid;
    private Handler bannerHandler = new Handler();
    private Runnable bannerRunnable;

    // Список баннеров
    private List<Integer> bannerImages = new ArrayList<Integer>() {{
        add(R.drawable.banner1);
        add(R.drawable.banner2);
        add(R.drawable.banner3);
    }};

    // Список рекомендаций (4 товара)
    private List<Product> recommendations = new ArrayList<Product>() {{
        add(new Product("Свадебный торт", "₽ 10000", R.drawable.cake_image, 5.0f));
        add(new Product("Букет невесты", "₽ 7000", R.drawable.flowers_image, 4.9f));
        add(new Product("Обручальные кольца", "₽ 30000", R.drawable.rings_image, 5.0f));
        add(new Product("Свадебная пиротехника", "₽ 100000", R.drawable.fireworks_image, 5.0f));
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        initViews();
        setupBanner();
        setupCategories();
        setupRecommendations();
        setupBottomNavigation();
    }

    private void initViews() {
        bannerViewPager = findViewById(R.id.bannerViewPager);
        indicatorContainer = findViewById(R.id.indicatorContainer);
        recommendationsGrid = findViewById(R.id.recommendationsGrid);

        // Установка приветствия
        TextView welcomeText = findViewById(R.id.welcomeText);
        welcomeText.setText("Добро пожаловать");
    }

    private void setupBanner() {
        BannerAdapter bannerAdapter = new BannerAdapter(this, bannerImages);
        bannerViewPager.setAdapter(bannerAdapter);

        // Добавляем индикаторы
        addIndicators();
        bannerViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateIndicators(position);
            }
        });

        // Автоматическая прокрутка
        bannerRunnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = bannerViewPager.getCurrentItem();
                int nextItem = (currentItem + 1) % bannerImages.size();
                bannerViewPager.setCurrentItem(nextItem, true);
                bannerHandler.postDelayed(this, 3000);
            }
        };
        bannerHandler.postDelayed(bannerRunnable, 3000);
    }

    private void addIndicators() {
        for (int i = 0; i < bannerImages.size(); i++) {
            ImageView indicator = new ImageView(this);
            indicator.setImageResource(R.drawable.indicator_unselected);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(12, 12);
            params.setMargins(4, 0, 4, 0);
            indicator.setLayoutParams(params);
            indicatorContainer.addView(indicator);
        }
        updateIndicators(0);
    }

    private void updateIndicators(int position) {
        for (int i = 0; i < indicatorContainer.getChildCount(); i++) {
            ImageView indicator = (ImageView) indicatorContainer.getChildAt(i);
            if (i == position) {
                indicator.setImageResource(R.drawable.indicator_selected);
            } else {
                indicator.setImageResource(R.drawable.indicator_unselected);
            }
        }
    }

    private void setupCategories() {
        // Одежда - показываем подкатегории
        findViewById(R.id.categoryClothes).setOnClickListener(v -> {
            showClothingSubcategoriesDialog();
        });

        // Кольца
        findViewById(R.id.categoryRings).setOnClickListener(v -> {
            android.widget.Toast.makeText(this, "Страница колец", android.widget.Toast.LENGTH_SHORT).show();
        });

        // Цветы
        findViewById(R.id.categoryFlowers).setOnClickListener(v -> {
            android.widget.Toast.makeText(this, "Страница цветов", android.widget.Toast.LENGTH_SHORT).show();
        });

        // Торты
        findViewById(R.id.categoryCakes).setOnClickListener(v -> {
            android.widget.Toast.makeText(this, "Страница тортов", android.widget.Toast.LENGTH_SHORT).show();
        });

        // Пиротехника
        findViewById(R.id.categoryFireworks).setOnClickListener(v -> {
            android.widget.Toast.makeText(this, "Страница пиротехники", android.widget.Toast.LENGTH_SHORT).show();
        });

        // Другое
        findViewById(R.id.categoryOther).setOnClickListener(v -> {
            android.widget.Toast.makeText(this, "Страница другое", android.widget.Toast.LENGTH_SHORT).show();
        });

        // Смотреть все категории
        findViewById(R.id.btnViewAllCategories).setOnClickListener(v -> {
            showAllCategoriesDialog();
        });
    }

    private void showClothingSubcategoriesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выберите категорию одежды");

        String[] subcategories = {"Мужская одежда", "Женская одежда"};

        builder.setItems(subcategories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    // Мужская одежда
                    Intent intent = new Intent(MainScreenActivity.this, MensClothingActivity.class);
                    startActivity(intent);
                } else if (which == 1) {
                    // Женская одежда
                    android.widget.Toast.makeText(MainScreenActivity.this, "Женская одежда", android.widget.Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Отмена", null);
        builder.show();
    }

    private void showAllCategoriesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Все категории");

        String[] allCategories = {
                "Одежда", "Кольца", "Цветы", "Торты",
                "Пиротехника", "Другое", "Аксессуары", "Обувь"
        };

        builder.setItems(allCategories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedCategory = allCategories[which];
                android.widget.Toast.makeText(MainScreenActivity.this,
                        "Выбрано: " + selectedCategory, android.widget.Toast.LENGTH_SHORT).show();

                if (selectedCategory.equals("Одежда")) {
                    showClothingSubcategoriesDialog();
                } else if (selectedCategory.equals("Кольца")) {
                    Intent intent = new Intent(MainScreenActivity.this, RingsActivity.class);
                    startActivity(intent);
                }
            }
        });

        builder.setNegativeButton("Отмена", null);
        builder.show();
    }

    private void setupRecommendations() {
        RecommendationAdapter adapter = new RecommendationAdapter(this, recommendations);
        recommendationsGrid.setAdapter(adapter);

        recommendationsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = recommendations.get(position);
                Intent intent = new Intent(MainScreenActivity.this, ProductDetailActivity.class);
                intent.putExtra("product_name", product.getName());
                intent.putExtra("product_price", product.getPrice());
                intent.putExtra("product_rating", product.getRating());
                startActivity(intent);
            }
        });

        // Смотреть все рекомендации
        findViewById(R.id.btnViewAllRecommendations).setOnClickListener(v -> {
            android.widget.Toast.makeText(MainScreenActivity.this, "Все рекомендации", android.widget.Toast.LENGTH_SHORT).show();
        });
    }

    private void setupBottomNavigation() {
        // Главная уже активна
        findViewById(R.id.btnHome).setOnClickListener(v -> {
        });

        // Корзина
        findViewById(R.id.btnCart).setOnClickListener(v -> {
            Intent intent = new Intent(MainScreenActivity.this, CartActivity.class);
            startActivity(intent);
        });

        // Избранное
        findViewById(R.id.btnFavorites).setOnClickListener(v -> {
            Intent intent = new Intent(MainScreenActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });

        // Личный кабинет
        findViewById(R.id.btnProfile).setOnClickListener(v -> {
            Intent intent = new Intent(MainScreenActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bannerHandler.removeCallbacks(bannerRunnable);
    }
}
package com.example.applicationwedly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailActivity extends AppCompatActivity {

    private Product currentProduct;
    private String selectedFemaleSize = "";
    private String selectedMaleSize = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Получаем данные из Intent
        Intent intent = getIntent();
        String productName = intent.getStringExtra("product_name");
        String productPrice = intent.getStringExtra("product_price");
        float productRating = intent.getFloatExtra("product_rating", 0.0f);
        int productImageRes = intent.getIntExtra("product_image", R.drawable.rings_image);
        String productSize = intent.getStringExtra("product_size");
        if (productSize == null) productSize = "-";

        // Создаем массив доступных размеров
        String[] availableSizes = new String[] { productSize }; // Создаем массив с выбранным размером

        // Теперь создаем объект Product с массивом размеров
        currentProduct = new Product(productName, productPrice, productImageRes, productRating, availableSizes, "Описание товара");
        // Находим все View
        ImageView productImage = findViewById(R.id.productImage);
        TextView productNameText = findViewById(R.id.productName);
        TextView productPriceText = findViewById(R.id.productPrice);
        TextView productRatingText = findViewById(R.id.productRating);
        TextView productDescription = findViewById(R.id.productDescription);
        Button btnAddToCart = findViewById(R.id.btnAddToCart);
        ImageView btnBack = findViewById(R.id.btnBack);
        ImageView btnLike = findViewById(R.id.btnLike);
        ImageView starIcon = findViewById(R.id.starIcon);

        // Устанавливаем данные
        productImage.setImageResource(currentProduct.getImageRes());
        productNameText.setText(currentProduct.getName());

        // Исправляем цену
        String price = currentProduct.getPrice();
        if (price.startsWith("P ")) {
            price = "₽" + price.substring(1);
        }
        productPriceText.setText(price);

        // Устанавливаем рейтинг
        productRatingText.setText(String.valueOf(currentProduct.getRating()));

        // Устанавливаем описание
        String description = "Высококачественный товар для вашего торжества. " +
                "Материалы: премиум-класс. Гарантия качества. " +
                "Размер: " + currentProduct.getSelectedSize();
        productDescription.setText(description);

        // Кнопка назад
        btnBack.setOnClickListener(v -> finish());

        // Кнопка лайка (избранное)
        btnLike.setImageResource(
                FavoritesManager.isFavorite(currentProduct)
                        ? R.drawable.heart_filled
                        : R.drawable.heart_icon
        );

        btnLike.setOnClickListener(v -> {
            FavoritesManager.toggle(currentProduct);
            btnLike.setImageResource(
                    FavoritesManager.isFavorite(currentProduct)
                            ? R.drawable.heart_filled
                            : R.drawable.heart_icon
            );
            Toast.makeText(this,
                    FavoritesManager.isFavorite(currentProduct)
                            ? "Добавлено в избранное"
                            : "Удалено из избранного",
                    Toast.LENGTH_SHORT).show();
        });

        btnAddToCart.setOnClickListener(v -> {
            // Проверяем, если это кольцо, то требуем оба размера
            if (currentProduct.getName().toLowerCase().contains("кольц")) {
                if (selectedFemaleSize.isEmpty() || selectedMaleSize.isEmpty()) {
                    Toast.makeText(this, "Выберите оба размера", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            // Для других товаров (например, тортов, цветов) проверка на размер не нужна
            else if (selectedFemaleSize.isEmpty() && selectedMaleSize.isEmpty()) {
                // Если товар не требует размера (например, торт или цветы), пропускаем проверку
            }

            // Создаем копию товара с выбранным размером (если нужно)
            Product productWithSizes = new Product(
                    currentProduct.getName(),
                    currentProduct.getPrice(),
                    currentProduct.getImageRes(),
                    currentProduct.getRating(),
                    currentProduct.getAvailableSizes(),
                    currentProduct.getDescription()
            );

            if (currentProduct.getName().toLowerCase().contains("кольц")) {
                productWithSizes.setSelectedSize(selectedFemaleSize + ", " + selectedMaleSize);
            } else {
                productWithSizes.setSelectedSize("-"); // Или можно оставить пустым, если размер не нужен
            }

            // Добавляем товар в корзину
            CartManager.getInstance().addToCart(productWithSizes, selectedFemaleSize, selectedMaleSize);
            Toast.makeText(this, "Товар добавлен в корзину!", Toast.LENGTH_SHORT).show();
        });
        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        LinearLayout btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(v -> {
            startActivity(new Intent(ProductDetailActivity.this, MainScreenActivity.class));
            finish();
        });

        LinearLayout btnCart = findViewById(R.id.btnCart);
        btnCart.setOnClickListener(v -> {
            startActivity(new Intent(ProductDetailActivity.this, CartActivity.class));
        });

        LinearLayout btnFavorites = findViewById(R.id.btnFavorites);
        btnFavorites.setOnClickListener(v -> {
            startActivity(new Intent(ProductDetailActivity.this, FavoritesActivity.class));
        });

        LinearLayout btnProfile = findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(v -> {
            startActivity(new Intent(ProductDetailActivity.this, ProfileActivity.class));
        });
    }
}
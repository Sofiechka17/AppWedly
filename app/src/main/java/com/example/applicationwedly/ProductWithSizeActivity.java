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

public class ProductWithSizeActivity extends AppCompatActivity {

    private Product currentProduct;
    private String selectedSize = "";
    private String selectedFemaleSize = "";
    private String selectedMaleSize = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_with_size);

        // Получаем данные из Intent
        Intent intent = getIntent();
        String productName = intent.getStringExtra("product_name");
        String productPrice = intent.getStringExtra("product_price");
        float productRating = intent.getFloatExtra("product_rating", 0.0f);
        int productImageRes = intent.getIntExtra("product_image", R.drawable.rings_image);
        String[] availableSizes = intent.getStringArrayExtra("available_sizes");
        String productDescription = intent.getStringExtra("product_description");

        // Если нет описания, используем стандартное
        if (productDescription == null) {
            productDescription = "Высококачественный товар для вашего торжества. " +
                    "Материалы: премиум-класс. Гарантия качества.";
        }

        currentProduct = new Product(productName, productPrice, productImageRes,
                productRating, availableSizes, productDescription);

        // Находим все View
        ImageView productImage = findViewById(R.id.productImage);
        TextView productNameText = findViewById(R.id.productName);
        TextView productPriceText = findViewById(R.id.productPrice);
        TextView productRatingText = findViewById(R.id.productRating);
        TextView productDescriptionText = findViewById(R.id.productDescription);
        ImageView btnBack = findViewById(R.id.btnBack);
        ImageView btnLike = findViewById(R.id.btnLike);
        LinearLayout sizesContainer = findViewById(R.id.sizesContainer);
        Button btnBuyNow = findViewById(R.id.btnBuyNow);

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
        productDescriptionText.setText(currentProduct.getDescription());

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

        // Создаем кнопки размеров, если они есть
        if (currentProduct.hasSizes()) {
            createSizeButtons(sizesContainer, currentProduct.getAvailableSizes());
            selectedSize = currentProduct.getAvailableSizes()[0]; // По умолчанию первый размер
        } else {
            // Если размеров нет, скрываем контейнер
            sizesContainer.setVisibility(View.GONE);
            findViewById(R.id.sizeTitle).setVisibility(View.GONE);
        }

        // Кнопка "Купить сейчас"
        btnBuyNow.setOnClickListener(v -> {
            // Для товаров с размерами проверяем выбор
            if (currentProduct.hasSizes() && selectedSize.isEmpty()) {
                Toast.makeText(this, "Выберите размер", Toast.LENGTH_SHORT).show();
                return;
            }

            // Создаем копию товара с выбранным размером
            Product productToAdd = new Product(
                    currentProduct.getName(),
                    currentProduct.getPrice(),
                    currentProduct.getImageRes(),
                    currentProduct.getRating(),
                    currentProduct.getAvailableSizes(),
                    currentProduct.getDescription()
            );

            if (currentProduct.hasSizes()) {
                productToAdd.setSelectedSize(selectedSize);
            } else {
                productToAdd.setSelectedSize("-");
            }

            // Добавляем в корзину
            CartManager.getInstance().addToCart(productToAdd, selectedFemaleSize, selectedMaleSize);
            Toast.makeText(this, "Товар добавлен в корзину!", Toast.LENGTH_SHORT).show();
        });

        setupBottomNavigation();
    }

    private void createSizeButtons(LinearLayout container, String[] sizes) {
        for (int i = 0; i < sizes.length; i++) {
            String size = sizes[i];
            Button button = new Button(this);
            button.setText(size);
            button.setBackgroundResource(R.drawable.size_button_background);  // Начальное состояние кнопки с обводкой
            button.setTextColor(getResources().getColor(R.color.primary_blue));  // Цвет текста кнопки

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 8, 8);
            button.setLayoutParams(params);

            // Добавляем логику выделения кнопки при нажатии
            button.setOnClickListener(v -> {
                // Снимаем выделение со всех кнопок в контейнере
                for (int j = 0; j < container.getChildCount(); j++) {
                    View child = container.getChildAt(j);
                    if (child instanceof Button) {
                        child.setBackgroundResource(R.drawable.size_button_background);  // Состояние без заливки
                        ((Button) child).setTextColor(getResources().getColor(R.color.primary_blue));  // Текст синий
                    }
                }

                // Выделяем нажатую кнопку (с заливкой)
                button.setBackgroundResource(R.drawable.size_button_background_selected);  // Кнопка с заливкой
                button.setTextColor(getResources().getColor(R.color.white));  // Цвет текста на белый

                selectedSize = size;  // Сохраняем выбранный размер
            });

            container.addView(button);
        }
    }

    private void setupBottomNavigation() {
        LinearLayout btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(v -> {
            startActivity(new Intent(ProductWithSizeActivity.this, MainScreenActivity.class));
            finish();
        });

        LinearLayout btnCart = findViewById(R.id.btnCart);
        btnCart.setOnClickListener(v -> {
            startActivity(new Intent(ProductWithSizeActivity.this, CartActivity.class));
        });

        LinearLayout btnFavorites = findViewById(R.id.btnFavorites);
        btnFavorites.setOnClickListener(v -> {
            startActivity(new Intent(ProductWithSizeActivity.this, FavoritesActivity.class));
        });

        LinearLayout btnProfile = findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(v -> {
            startActivity(new Intent(ProductWithSizeActivity.this, ProfileActivity.class));
        });
    }
}
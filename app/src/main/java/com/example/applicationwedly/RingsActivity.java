package com.example.applicationwedly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.flexbox.FlexboxLayout;  // Импортируем FlexboxLayout

public class RingsActivity extends AppCompatActivity {

    private String selectedFemaleSize = "";
    private String selectedMaleSize = "";
    private Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rings);

        // Получаем данные из Intent
        Intent intent = getIntent();
        String productName = intent.getStringExtra("product_name");
        String productPrice = intent.getStringExtra("product_price");
        float productRating = intent.getFloatExtra("product_rating", 5.0f);
        int productImageRes = intent.getIntExtra("product_image", R.drawable.rings_image);

        currentProduct = new Product(productName, productPrice, productImageRes, productRating);

        // Находим View
        ImageView productImage = findViewById(R.id.productImage);
        TextView productNameText = findViewById(R.id.productName);
        TextView productPriceText = findViewById(R.id.productPrice);
        TextView productRatingText = findViewById(R.id.productRating);
        ImageView btnBack = findViewById(R.id.btnBack);
        ImageView btnLike = findViewById(R.id.btnLike);
        FlexboxLayout femaleSizesContainer = findViewById(R.id.femaleSizesContainer);  // Используем FlexboxLayout
        FlexboxLayout maleSizesContainer = findViewById(R.id.maleSizesContainer);  // Используем FlexboxLayout
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

        // Создаем кнопки размеров для невесты
        String[] femaleSizes = {"15", "15.5", "16", "16.5", "17", "17.5", "18", "18.5"};
        createSizeButtons(femaleSizesContainer, femaleSizes, true);

        // Создаем кнопки размеров для жениха
        String[] maleSizes = {"15", "15.5", "16", "16.5", "17", "17.5", "18", "18.5"};
        createSizeButtons(maleSizesContainer, maleSizes, false);

        // Кнопка "Купить сейчас"
        btnBuyNow.setOnClickListener(v -> {
            // Если товар требует размера (например, кольца), проверяем, что оба размера выбраны
            if (currentProduct.getName().toLowerCase().contains("кольц")) {
                if (selectedFemaleSize.isEmpty() || selectedMaleSize.isEmpty()) {
                    Toast.makeText(this, "Выберите оба размера", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            // Для товаров без размеров или с одним размером
            else if (selectedFemaleSize.isEmpty() && selectedMaleSize.isEmpty()) {
                Toast.makeText(this, "Размер не выбран", Toast.LENGTH_SHORT).show();
                return;
            }

            // Создаем копию товара с размерами
            Product productWithSizes = new Product(
                    currentProduct.getName(),
                    currentProduct.getPrice(),
                    currentProduct.getImageRes(),
                    currentProduct.getRating(),
                    currentProduct.getAvailableSizes(),
                    currentProduct.getDescription()
            );

            // Устанавливаем выбранные размеры
            if (currentProduct.getName().toLowerCase().contains("кольц")) {
                productWithSizes.setSelectedSize(selectedFemaleSize + ", " + selectedMaleSize);
            } else {
                productWithSizes.setSelectedSize(selectedFemaleSize.isEmpty() ? "-" : selectedFemaleSize); // Для других товаров
            }

            // Добавляем в корзину с размерами
            CartManager.getInstance().addToCart(productWithSizes, selectedFemaleSize, selectedMaleSize);
            Toast.makeText(this, "Товар добавлен в корзину!", Toast.LENGTH_SHORT).show();
        });

        setupBottomNavigation();
    }

    private void createSizeButtons(ViewGroup container, String[] sizes, boolean isFemale) {
        for (String size : sizes) {
            Button button = new Button(this);
            button.setText(size);
            button.setBackgroundResource(R.drawable.size_button_background);
            button.setTextColor(getResources().getColor(R.color.primary_blue)); // Голубой цвет для текста

            // Убедимся, что используем правильный тип LayoutParams
            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 8, 8);  // Устанавливаем отступы
            button.setLayoutParams(params);

            // По умолчанию выбираем первую кнопку
            if (container.getChildCount() == 0) {
                button.setBackgroundResource(R.drawable.size_button_background_selected); // Подсвечиваем первую
                button.setTextColor(getResources().getColor(R.color.white)); // Белый цвет текста для выбранной
            }

            button.setOnClickListener(v -> {
                // Снимаем выделение со всех кнопок в контейнере
                for (int i = 0; i < container.getChildCount(); i++) {
                    View child = container.getChildAt(i);
                    if (child instanceof Button) {
                        child.setBackgroundResource(R.drawable.size_button_background); // Обновляем фон
                        ((Button) child).setTextColor(getResources().getColor(R.color.primary_blue)); // Цвет текста для невыбранной
                    }
                }

                // Выделяем нажатую кнопку
                button.setBackgroundResource(R.drawable.size_button_background_selected);
                button.setTextColor(getResources().getColor(R.color.white)); // Цвет текста для выбранной кнопки

                // Устанавливаем размер
                if (isFemale) {
                    selectedFemaleSize = size;
                } else {
                    selectedMaleSize = size;
                }
            });

            container.addView(button);
        }
    }

    private void setupBottomNavigation() {
        LinearLayout btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(v -> {
            startActivity(new Intent(RingsActivity.this, MainScreenActivity.class));
            finish();
        });

        LinearLayout btnCart = findViewById(R.id.btnCart);
        btnCart.setOnClickListener(v -> {
            startActivity(new Intent(RingsActivity.this, CartActivity.class));
        });

        LinearLayout btnFavorites = findViewById(R.id.btnFavorites);
        btnFavorites.setOnClickListener(v -> {
            startActivity(new Intent(RingsActivity.this, FavoritesActivity.class));
        });

        LinearLayout btnProfile = findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(v -> {
            startActivity(new Intent(RingsActivity.this, ProfileActivity.class));
        });
    }
}

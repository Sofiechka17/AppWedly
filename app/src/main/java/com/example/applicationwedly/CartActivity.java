package com.example.applicationwedly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartChangeListener {

    private ListView cartListView;
    private TextView totalPriceTextView;
    private Button checkoutBtn;
    private CartAdapter cartAdapter;
    private List<CartManager.CartItem> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Находим View
        cartListView = findViewById(R.id.cartListView);
        totalPriceTextView = findViewById(R.id.totalPrice);
        checkoutBtn = findViewById(R.id.btnCheckout);

        // Получаем товары из корзины
        cartItems = CartManager.getInstance().getCartItems();

        // Создаем адаптер
        cartAdapter = new CartAdapter(this, cartItems, this);
        cartListView.setAdapter(cartAdapter);

        // Обновляем общую стоимость
        updateTotalPrice();

        // Кнопка оформления заказа
        checkoutBtn.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Корзина пуста", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Заказ оформлен!", Toast.LENGTH_SHORT).show();
                CartManager.getInstance().clearCart();
                cartItems.clear();
                cartAdapter.notifyDataSetChanged();
                updateTotalPrice();
            }
        });

        setupBottomNavigation();
    }

    private void updateTotalPrice() {
        double total = CartManager.getInstance().getTotalPrice();
        totalPriceTextView.setText("₽ " + String.format("%.0f", total));
    }

    @Override
    public void onCartChanged() {
        updateTotalPrice();
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
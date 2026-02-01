package com.example.applicationwedly;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<CartItem> cartItems;

    public static class CartItem {
        public Product product;
        public int quantity;

        public CartItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }
    }

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addToCart(Product product) {
        // Проверяем, есть ли уже такой товар с таким же размером
        for (CartItem item : cartItems) {
            if (item.product.getName().equals(product.getName()) &&
                    item.product.getSelectedSize().equals(product.getSelectedSize())) {
                item.quantity++;
                return;
            }
        }
        // Если нет - добавляем новый
        cartItems.add(new CartItem(product, 1));
    }

    public void updateQuantity(Product product, int quantity) {
        for (CartItem item : cartItems) {
            if (item.product.getName().equals(product.getName()) &&
                    item.product.getSelectedSize().equals(product.getSelectedSize())) {
                if (quantity <= 0) {
                    cartItems.remove(item);
                } else {
                    item.quantity = quantity;
                }
                return;
            }
        }
    }

    public void removeFromCart(Product product) {
        cartItems.removeIf(item ->
                item.product.getName().equals(product.getName()) &&
                        item.product.getSelectedSize().equals(product.getSelectedSize()));
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public double getTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            String priceStr = item.product.getPrice();
            priceStr = priceStr.replace("₽", "").replace("P", "").replace(" ", "");
            try {
                double price = Double.parseDouble(priceStr);
                total += price * item.quantity;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return total;
    }

    public void clearCart() {
        cartItems.clear();
    }
}
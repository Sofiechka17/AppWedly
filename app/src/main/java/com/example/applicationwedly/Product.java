package com.example.applicationwedly;

public class Product {
    private String name;
    private String price;
    private int imageRes;
    private float rating;
    private String selectedSize; // Выбранный пользователем размер
    private String[] availableSizes; // Доступные размеры
    private String description; // Описание товара

    // Конструктор для товаров без выбора размера
    public Product(String name, String price, int imageRes, float rating) {
        this.name = name;
        this.price = price;
        this.imageRes = imageRes;
        this.rating = rating;
        this.selectedSize = "-";
        this.availableSizes = new String[0];
        this.description = "";
    }

    // Конструктор для товаров с размерами
    public Product(String name, String price, int imageRes, float rating,
                   String[] availableSizes, String description) {
        this.name = name;
        this.price = price;
        this.imageRes = imageRes;
        this.rating = rating;
        this.selectedSize = availableSizes.length > 0 ? availableSizes[0] : "-";
        this.availableSizes = availableSizes;
        this.description = description;
    }

    // Геттеры и сеттеры
    public String getName() { return name; }
    public String getPrice() { return price; }
    public int getImageRes() { return imageRes; }
    public float getRating() { return rating; }
    public String getSelectedSize() { return selectedSize; }
    public void setSelectedSize(String size) { this.selectedSize = size; }
    public String[] getAvailableSizes() { return availableSizes; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // Проверяет, есть ли у товара выбор размеров
    public boolean hasSizes() {
        return availableSizes != null && availableSizes.length > 0;
    }
}
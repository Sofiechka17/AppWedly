package com.example.applicationwedly;

public class Product {
    private String name;
    private String price;
    private int imageRes;
    private float rating;

    public Product(String name, String price, int imageRes, float rating) {
        this.name = name;
        this.price = price;
        this.imageRes = imageRes;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public int getImageRes() {
        return imageRes;
    }

    public float getRating() {
        return rating;
    }
}
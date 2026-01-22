package com.example.applicationwedly;

import java.util.ArrayList;
import java.util.List;

public class FavoritesManager {
    public static List<Product> favorites = new ArrayList<>();

    public static boolean isFavorite(Product p) {
        return favorites.contains(p);
    }

    public static void toggle(Product p) {
        if (favorites.contains(p)) favorites.remove(p);
        else favorites.add(p);
    }
}

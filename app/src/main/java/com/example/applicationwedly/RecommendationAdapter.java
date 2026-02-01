package com.example.applicationwedly;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class RecommendationAdapter extends BaseAdapter {

    private Context context;
    private List<Product> products;
    private LayoutInflater inflater;

    public RecommendationAdapter(Context context, List<Product> list) {
        this.context = context;
        this.products = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View v, ViewGroup parent) {
        if (v == null)
            v = inflater.inflate(R.layout.item_recommendation, parent, false);

        Product p = products.get(i);

        ((ImageView)v.findViewById(R.id.productImage)).setImageResource(p.getImageRes());
        ((TextView)v.findViewById(R.id.productName)).setText(p.getName());

        // Исправляем отображение цены - заменяем P на ₽
        String price = p.getPrice();
        if (price.startsWith("P ")) {
            price = "₽" + price.substring(1);
        }
        ((TextView)v.findViewById(R.id.productPrice)).setText(price);
        ((TextView)v.findViewById(R.id.productRating)).setText(String.valueOf(p.getRating()));

        ImageView heart = v.findViewById(R.id.favoriteIcon);

        // Установить голубой цвет для избранных товаров
        if (FavoritesManager.isFavorite(p)) {
            heart.setImageResource(R.drawable.heart_filled);
            heart.setColorFilter(context.getResources().getColor(R.color.primary_blue));
        } else {
            heart.setImageResource(R.drawable.heart_icon);
            heart.setColorFilter(context.getResources().getColor(R.color.gray));
        }

        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isFavoriteBefore = FavoritesManager.isFavorite(p);
                FavoritesManager.toggle(p);
                notifyDataSetChanged();

                // Показать Toast сообщение
                String message = FavoritesManager.isFavorite(p)
                        ? "Товар добавлен в избранное"
                        : "Товар удален из избранного";
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }
}
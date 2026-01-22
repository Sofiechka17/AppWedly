package com.example.applicationwedly;

import android.view.*;
import android.widget.*;

import java.util.List;

public class RecommendationAdapter extends BaseAdapter {

    List<Product> products;
    LayoutInflater inflater;

    public RecommendationAdapter(android.content.Context c, List<Product> list) {
        products = list;
        inflater = LayoutInflater.from(c);
    }

    @Override public int getCount() { return products.size(); }
    @Override public Object getItem(int i) { return products.get(i); }
    @Override public long getItemId(int i) { return i; }

    @Override
    public View getView(int i, View v, ViewGroup parent) {
        if (v == null)
            v = inflater.inflate(R.layout.item_recommendation, parent, false);

        Product p = products.get(i);

        ((ImageView)v.findViewById(R.id.productImage)).setImageResource(p.getImageRes());
        ((TextView)v.findViewById(R.id.productName)).setText(p.getName());
        ((TextView)v.findViewById(R.id.productPrice)).setText(p.getPrice());
        ((TextView)v.findViewById(R.id.productRating)).setText("★ " + p.getRating());

        ImageView heart = v.findViewById(R.id.favoriteIcon);

        heart.setImageResource(
                FavoritesManager.isFavorite(p)
                        ? R.drawable.heart_filled
                        : R.drawable.heart_icon
        );

        heart.setOnClickListener(c -> {
            FavoritesManager.toggle(p);
            notifyDataSetChanged();
        });

        return v;
    }
}

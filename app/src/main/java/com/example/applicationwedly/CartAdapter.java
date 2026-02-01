package com.example.applicationwedly;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CartAdapter extends BaseAdapter {

    private Context context;
    private List<CartManager.CartItem> cartItems;
    private LayoutInflater inflater;
    private OnCartChangeListener listener;

    public interface OnCartChangeListener {
        void onCartChanged();
    }

    public CartAdapter(Context context, List<CartManager.CartItem> cartItems, OnCartChangeListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return cartItems.size();
    }

    @Override
    public Object getItem(int position) {
        return cartItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_cart, parent, false);
            holder = new ViewHolder();

            holder.imgProduct = convertView.findViewById(R.id.imgProduct);
            holder.txtName = convertView.findViewById(R.id.txtName);
            holder.txtSize = convertView.findViewById(R.id.txtSize);
            holder.txtPrice = convertView.findViewById(R.id.txtPrice);
            holder.txtQty = convertView.findViewById(R.id.txtQty);
            holder.btnPlus = convertView.findViewById(R.id.btnPlus);
            holder.btnMinus = convertView.findViewById(R.id.btnMinus);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CartManager.CartItem cartItem = cartItems.get(position);
        Product product = cartItem.product;

        // Заполняем данные
        holder.imgProduct.setImageResource(product.getImageRes());
        holder.txtName.setText(product.getName());
        // В методе getView():
        String size = product.getSelectedSize();
        if (product.getName().toLowerCase().contains("кольц")) {
            // Для колец показываем "Размеры: 16, 18"
            holder.txtSize.setText("Размеры: " + size);
        } else if (size.equals("-")) {
            // Для товаров без размера
            holder.txtSize.setText("Размер: не указан");
        } else {
            // Для остальных товаров
            holder.txtSize.setText("Размер: " + size);
        }

        // Исправляем цену
        String price = product.getPrice();
        if (price.startsWith("P ")) {
            price = "₽" + price.substring(1);
        }
        holder.txtPrice.setText(price);
        holder.txtQty.setText(String.valueOf(cartItem.quantity));

        // Обработчики кнопок +/-
        holder.btnPlus.setOnClickListener(v -> {
            cartItem.quantity++;
            CartManager.getInstance().updateQuantity(product, cartItem.quantity);
            notifyDataSetChanged();
            if (listener != null) listener.onCartChanged();
        });

        holder.btnMinus.setOnClickListener(v -> {
            cartItem.quantity--;
            if (cartItem.quantity <= 0) {
                cartItems.remove(cartItem);
            } else {
                CartManager.getInstance().updateQuantity(product, cartItem.quantity);
            }
            notifyDataSetChanged();
            if (listener != null) listener.onCartChanged();
        });

        return convertView;
    }

    static class ViewHolder {
        ImageView imgProduct;
        TextView txtName;
        TextView txtSize;
        TextView txtPrice;
        TextView txtQty;
        Button btnPlus;
        Button btnMinus;
    }
}
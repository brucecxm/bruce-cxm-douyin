package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> products;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(Product product);

        void onAddToCartClick(Product product);

        void onChatClick(Product product);
    }

    public ProductAdapter() {
        this.products = new ArrayList<>();
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    public void setOnProductClickListener(OnProductClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImageView;
        private TextView productNameTextView;
        private TextView productPriceTextView;
        private TextView productRatingTextView;
        private TextView productStockTextView;
        private TextView productSellerTextView;
        private TextView productConditionTextView;
        private TextView productLocationTextView;
        private TextView productItemTypeTextView;
        private Button addToCartButton;
        private Button chatButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.productImageView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            productPriceTextView = itemView.findViewById(R.id.productPriceTextView);
            productRatingTextView = itemView.findViewById(R.id.productRatingTextView);
            productStockTextView = itemView.findViewById(R.id.productStockTextView);
            productSellerTextView = itemView.findViewById(R.id.productSellerTextView);
            productConditionTextView = itemView.findViewById(R.id.productConditionTextView);
            productLocationTextView = itemView.findViewById(R.id.productLocationTextView);
            productItemTypeTextView = itemView.findViewById(R.id.productItemTypeTextView);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
            chatButton = itemView.findViewById(R.id.chatButton);

            // 商品项点击事件
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onProductClick(products.get(position));
                    }
                }
            });

            addToCartButton.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onAddToCartClick(products.get(position));
                    }
                }
            });

            chatButton.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onChatClick(products.get(position));
                    }
                }
            });
        }

        public void bind(Product product) {
            // 加载商品图片
            Glide.with(itemView.getContext())
                    .load(product.getImageUrl())
                    .placeholder(R.drawable.ic_product_placeholder)
                    .error(R.drawable.ic_product_placeholder)
                    .into(productImageView);

            productNameTextView.setText(product.getName());
            productPriceTextView.setText("¥" + String.format("%.2f", product.getPrice()));
            productRatingTextView.setText("⭐ " + product.getRating());
            productStockTextView.setText("库存: " + product.getStock());
            productSellerTextView.setText("卖家: " + product.getSellerName());
            productConditionTextView.setText(product.getCondition());
            productLocationTextView.setText("位置: " + product.getLocation());
            productItemTypeTextView.setText(product.getItemType());
        }
    }
}

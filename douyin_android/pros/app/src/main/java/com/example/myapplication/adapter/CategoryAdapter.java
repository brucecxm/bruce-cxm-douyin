package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<String> categories;
    private OnCategoryClickListener listener;
    private int selectedPosition = 0;

    public interface OnCategoryClickListener {
        void onCategoryClick(String category);
    }

    public CategoryAdapter() {
        this.categories = new ArrayList<>();
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    public void setOnCategoryClickListener(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        String category = categories.get(position);
        holder.bind(category, position == selectedPosition);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryTextView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        int oldPosition = selectedPosition;
                        selectedPosition = position;
                        notifyItemChanged(oldPosition);
                        notifyItemChanged(selectedPosition);
                        listener.onCategoryClick(categories.get(position));
                    }
                }
            });
        }

        public void bind(String category, boolean isSelected) {
            categoryTextView.setText(category);
            
            Context context = itemView.getContext();
            
            // 设置选中状态的样式
            if (isSelected) {
                categoryTextView.setBackgroundResource(R.color.primary_color);
                categoryTextView.setTextColor(ContextCompat.getColor(context, android.R.color.white));
            } else {
                categoryTextView.setBackgroundResource(android.R.color.transparent);
                categoryTextView.setTextColor(ContextCompat.getColor(context, R.color.primary_color));
            }
        }
    }
}

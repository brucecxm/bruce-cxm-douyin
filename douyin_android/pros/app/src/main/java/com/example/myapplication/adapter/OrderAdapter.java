package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.CartItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单列表适配器
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<OrderItem> orderItems = new ArrayList<>();

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderItem orderItem = orderItems.get(position);
        // 绑定订单数据到UI
        holder.tvOrderNumber.setText("订单号: " + orderItem.getOrderNumber());
        holder.tvOrderStatus.setText("状态: " + orderItem.getStatus());
        holder.tvOrderAmount.setText("金额: ¥" + orderItem.getAmount());
        holder.tvOrderTime.setText("时间: " + orderItem.getTime());
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
        notifyDataSetChanged();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView tvOrderNumber;
        private TextView tvOrderStatus;
        private TextView tvOrderAmount;
        private TextView tvOrderTime;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderNumber = itemView.findViewById(R.id.tv_order_number);
            tvOrderStatus = itemView.findViewById(R.id.tv_order_status);
            tvOrderAmount = itemView.findViewById(R.id.tv_order_amount);
            tvOrderTime = itemView.findViewById(R.id.tv_order_time);
        }
    }

    /**
     * 订单项模型类
     */
    public static class OrderItem {
        private String orderNumber;
        private String status;
        private double amount;
        private String time;

        public OrderItem(String orderNumber, String status, double amount, String time) {
            this.orderNumber = orderNumber;
            this.status = status;
            this.amount = amount;
            this.time = time;
        }

        // Getters and Setters
        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}

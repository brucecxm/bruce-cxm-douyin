package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.CartAdapter;
import com.example.myapplication.data.LocalDataManager;
import com.example.myapplication.databinding.FragmentSecondBinding;
import com.example.myapplication.model.CartItem;
import com.example.myapplication.model.Order;

import java.util.List;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private LocalDataManager dataManager;
    private CartAdapter cartAdapter;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataManager = LocalDataManager.getInstance();
        setupRecyclerView();
        setupCheckoutButton();
        updateCartDisplay();
    }

    private void setupRecyclerView() {
        cartAdapter = new CartAdapter();
        binding.cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.cartRecyclerView.setAdapter(cartAdapter);

        cartAdapter.setOnCartItemClickListener(new CartAdapter.OnCartItemClickListener() {
            @Override
            public void onQuantityChanged(CartItem cartItem, int newQuantity) {
                dataManager.updateCartItemQuantity(cartItem.getProduct().getId(), newQuantity);
                updateCartDisplay();
            }

            @Override
            public void onRemoveItem(CartItem cartItem) {
                dataManager.removeFromCart(cartItem.getProduct().getId());
                updateCartDisplay();
            }
        });
    }

    private void setupCheckoutButton() {
        binding.checkoutButton.setOnClickListener(v -> {
            List<CartItem> cartItems = dataManager.getCartItems();
            if (cartItems.isEmpty()) {
                if (getActivity() != null) {
                    ((MainActivity) getActivity()).showMessage("购物车是空的");
                }
                return;
            }

            // 跳转到订单页面
            NavHostFragment.findNavController(SecondFragment.this)
                    .navigate(R.id.action_SecondFragment_to_OrderActivity);
        });
    }

    private void updateCartDisplay() {
        List<CartItem> cartItems = dataManager.getCartItems();
        cartAdapter.setCartItems(cartItems);

        if (cartItems.isEmpty()) {
            binding.emptyCartLayout.setVisibility(View.VISIBLE);
            binding.cartRecyclerView.setVisibility(View.GONE);
            binding.bottomLayout.setVisibility(View.GONE);
        } else {
            binding.emptyCartLayout.setVisibility(View.GONE);
            binding.cartRecyclerView.setVisibility(View.VISIBLE);
            binding.bottomLayout.setVisibility(View.VISIBLE);
            
            double total = dataManager.getCartTotal();
            binding.totalPriceTextView.setText("¥" + String.format("%.2f", total));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // 当从其他页面返回时，更新购物车显示
        updateCartDisplay();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
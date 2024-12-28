package com.name.foodapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.name.foodapp.Utils.Utils;
import com.name.foodapp.databinding.ItemCartBinding;
import com.name.foodapp.listener.ChangeNumListener;
import com.name.foodapp.model.Cart;

import java.util.List;

import io.paperdb.Paper;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    private Context context;
    private List<Cart> cartList;
    private ChangeNumListener changeNumListener;

    public CartAdapter(Context context, List<Cart> cartList, ChangeNumListener changeNumListener) {
        this.context = context;
        this.cartList = cartList;
        this.changeNumListener = changeNumListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartBinding cartBinding = ItemCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(cartBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Cart cart = cartList.get(position);

        // Đảm bảo MealDetail không null trước khi truy cập thuộc tính
        if (cart.getMealDetail() != null) {
            holder.binding.txtname.setText(cart.getMealDetail().getMeal());
            Glide.with(context).load(cart.getMealDetail().getStrmealthumb()).into(holder.binding.imageCart);
            holder.binding.txtprice.setText(String.valueOf(cart.getMealDetail().getPrice()));
        } else {
            // Xử lý trường hợp MealDetail null (tùy chọn: đặt giá trị mặc định hoặc ẩn các view)
            holder.binding.txtname.setText("Bữa ăn không xác định");
            holder.binding.imageCart.setImageResource(android.R.drawable.alert_dark_frame); // Đặt hình ảnh dự phòng
            holder.binding.txtprice.setText("N/A");
        }

        holder.binding.imgAdd.setOnClickListener(view -> {
            addToCart(holder.getAdapterPosition());
            notifyDataSetChanged();
            changeNumListener.change();
        });

        holder.binding.imgSub.setOnClickListener(view -> {
            subToCart(holder.getAdapterPosition());
            notifyDataSetChanged();
            changeNumListener.change();
        });

        holder.binding.txtamount.setText(String.valueOf(cart.getAmount()));
        // Tính toán tổng giá và đặt vào txtprice2
        if (cart.getMealDetail() != null) {
            holder.binding.txtprice2.setText(String.valueOf(cart.getAmount() * cart.getMealDetail().getPrice()) + "đ");
        } else {
            holder.binding.txtprice2.setText("N/A"); // Xử lý MealDetail null cho tổng giá
        }
    }

    private void subToCart(int adapterPosition) {
        if (Utils.cartList.get(adapterPosition).getAmount() == 1) {
            Utils.cartList.remove(adapterPosition);
        } else {
            Utils.cartList.get(adapterPosition).setAmount(Utils.cartList.get(adapterPosition).getAmount() - 1);
        }
        Paper.book().write("Cart", Utils.cartList);
    }

    private void addToCart(int adapterPosition) {
        Utils.cartList.get(adapterPosition).setAmount(Utils.cartList.get(adapterPosition).getAmount() + 1);
        Paper.book().write("Cart", Utils.cartList);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ItemCartBinding binding;

        public MyViewHolder(ItemCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

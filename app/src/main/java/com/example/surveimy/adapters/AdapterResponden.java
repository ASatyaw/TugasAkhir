package com.example.surveimy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.surveimy.databinding.RowItemRespondenBinding;
import com.example.surveimy.models.RespondenItem;

import java.util.List;

public class AdapterResponden extends RecyclerView.Adapter<AdapterResponden.RespondenViewHolder> {
    private Context mContext;
    private ItemClickListener listener;
    private List<RespondenItem> respondenItems;
    public AdapterResponden(Context context,List<RespondenItem> respondenItemList, ItemClickListener itemClickListener) {
        this.mContext = context;
        this.respondenItems = respondenItemList;
        this.listener = itemClickListener;
    }

    @NonNull
    @Override
    public RespondenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RespondenViewHolder(RowItemRespondenBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RespondenViewHolder holder, int position) {
        holder.bind(respondenItems.get(position));
    }

    @Override
    public int getItemCount() {
        return respondenItems.size();
    }

    public class RespondenViewHolder extends RecyclerView.ViewHolder {
        RowItemRespondenBinding binding;

        public RespondenViewHolder(@NonNull RowItemRespondenBinding rowItemRespondenBinding) {
            super(rowItemRespondenBinding.getRoot());
            binding = rowItemRespondenBinding;
        }

        public void bind(RespondenItem respondenItem) {
            binding.tvName.setText(respondenItem.getUsername());
            binding.tvNim.setText(respondenItem.getNim());
            itemView.setOnClickListener((v)-> listener.onClickItemResponden(respondenItem));
        }
    }

    public interface ItemClickListener {
        void onClickItemResponden(RespondenItem respondenItem);
    }
}

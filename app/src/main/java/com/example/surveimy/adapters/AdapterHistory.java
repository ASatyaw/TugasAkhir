package com.example.surveimy.adapters;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.surveimy.databinding.RowItemHistoryBinding;
import com.example.surveimy.models.HistoryItem;

import java.util.List;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.HistoryViewHolder> {
    private List<HistoryItem> list;

    public AdapterHistory(List<HistoryItem> historyItems) {
        this.list = historyItems;
    }

    @NonNull
    @Override
    public AdapterHistory.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterHistory.HistoryViewHolder(RowItemHistoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHistory.HistoryViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        RowItemHistoryBinding binding;

        public HistoryViewHolder(@NonNull RowItemHistoryBinding rowItemHistoryBinding) {
            super(rowItemHistoryBinding.getRoot());
            binding = rowItemHistoryBinding;
        }

        public void bind(HistoryItem historyItem) {
            binding.tvTitle.setText(historyItem.getTitle());
            binding.tvCreated.setText(historyItem.getCreatedAt());
            String typeHistory = historyItem.getTypeHistory() == 1 ? "+":"-";
            binding.tvKoin.setText(typeHistory+historyItem.getKoin());
        }
    }

}

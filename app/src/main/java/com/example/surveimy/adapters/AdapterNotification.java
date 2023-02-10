package com.example.surveimy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.surveimy.databinding.RowItemNotificationBinding;
import com.example.surveimy.models.NotificationItem;

import java.util.List;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.SurveyViewHolder> {
    private Context mContext;
    private List<NotificationItem> list;
    private ItemClickListener listener;
    public AdapterNotification(Context context, List<NotificationItem> notificationItems,ItemClickListener itemClickListener) {
        this.mContext = context;
        this.list = notificationItems;
        this.listener = itemClickListener;
    }

    @NonNull
    @Override
    public AdapterNotification.SurveyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterNotification.SurveyViewHolder(RowItemNotificationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterNotification.SurveyViewHolder holder, int position) {
        NotificationItem notificationItem = list.get(position);
        holder.bind(notificationItem,position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SurveyViewHolder extends RecyclerView.ViewHolder {
        RowItemNotificationBinding binding;

        public SurveyViewHolder(@NonNull RowItemNotificationBinding rowItemNotificationBinding) {
            super(rowItemNotificationBinding.getRoot());
            binding = rowItemNotificationBinding;
        }

        public void bind(NotificationItem notificationItem,int position) {
            binding.tvTitle.setText(notificationItem.getTitle());
            binding.tvSubTitle.setText(notificationItem.getSubTitle());
            binding.tvCreated.setText(notificationItem.getCreatedAt());
            final String statusRead = notificationItem.getStatus() == 0 ? "Unread" : "Read";
            binding.tvStatus.setText(statusRead);
            itemView.setOnClickListener((v)->{
                listener.onClickItem(notificationItem);
            });
        }
    }
    public interface ItemClickListener {
        void onClickItem(NotificationItem notificationItem);
    }
}
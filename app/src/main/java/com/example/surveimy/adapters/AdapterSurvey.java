package com.example.surveimy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.surveimy.databinding.RowItemSurveyBinding;
import com.example.surveimy.models.SurveyItem;

import java.util.List;

public class AdapterSurvey extends RecyclerView.Adapter<AdapterSurvey.SurveyViewHolder> {
    private Context mContext;
    private List<SurveyItem> list;
    private ItemClickListener listener;

    public AdapterSurvey(Context context, List<SurveyItem> surveyItems, ItemClickListener itemClickListener) {
        this.mContext = context;
        this.list = surveyItems;
        this.listener = itemClickListener;
    }

    @NonNull
    @Override
    public SurveyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SurveyViewHolder(RowItemSurveyBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SurveyViewHolder holder, int position) {
        SurveyItem surveyItem = list.get(position);
        holder.bind(surveyItem);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SurveyViewHolder extends RecyclerView.ViewHolder {
        RowItemSurveyBinding binding;

        public SurveyViewHolder(@NonNull RowItemSurveyBinding rowItemNewBinding) {
            super(rowItemNewBinding.getRoot());
            binding = rowItemNewBinding;
        }

        public void bind(SurveyItem surveyItem) {
            binding.tvTitle.setText(surveyItem.getTitle());
            binding.tvCreated.setText("Mulai saat " + surveyItem.getCreatedAt());
            binding.tvExpired.setText("Berakhir saat " + surveyItem.getExpiredAt());
            binding.tvDes.setText(surveyItem.getDescreption());
            binding.tvReward.setText(String.valueOf(surveyItem.getReward()));
            itemView.setOnClickListener((v) ->  listener.onClickItem(surveyItem));
        }
    }

    public interface ItemClickListener {
        void onClickItem(SurveyItem surveyItem);
    }
}

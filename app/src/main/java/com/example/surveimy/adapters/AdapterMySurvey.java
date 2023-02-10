package com.example.surveimy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.surveimy.databinding.RowItemMysurveyBinding;
import com.example.surveimy.models.SurveyItem;

import java.util.List;

public class AdapterMySurvey extends RecyclerView.Adapter<AdapterMySurvey.SurveyViewHolder> {
    private final Context mContext;
    private List<SurveyItem> list;
    private ItemClickListener listener;

    public AdapterMySurvey(Context context, List<SurveyItem> surveyItems,ItemClickListener itemClickListener) {
        this.mContext = context;
        this.list = surveyItems;
        this.listener = itemClickListener;
    }

    @NonNull
    @Override
    public AdapterMySurvey.SurveyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterMySurvey.SurveyViewHolder(RowItemMysurveyBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMySurvey.SurveyViewHolder holder, int position) {
        SurveyItem surveyItem = list.get(position);
        holder.bind(surveyItem);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SurveyViewHolder extends RecyclerView.ViewHolder {
        RowItemMysurveyBinding binding;

        public SurveyViewHolder(@NonNull RowItemMysurveyBinding rowItemMysurveyBinding) {
            super(rowItemMysurveyBinding.getRoot());
            binding = rowItemMysurveyBinding;
        }

        public void bind(SurveyItem surveyItem) {
            final String statusSurvey = surveyItem.getStatus() ==0 ? "Publish":"Progress";
            binding.tvTitle.setText(surveyItem.getTitle());
            binding.tvStatus.setText("Status : "+statusSurvey);
            binding.tvCreated.setText("Created "+surveyItem.getCreatedAt());
            binding.tvExpired.setText("Expired " + surveyItem.getExpiredAt());
            binding.tvUpdated.setText("Updated " + surveyItem.getUpdatedAt());
            binding.tvTotalResponden.setText(String.valueOf(surveyItem.getResponden()));
            binding.btnPublish.setOnClickListener((v)->listener.onClickPublish(surveyItem));
            binding.tvTotalResponden.setOnClickListener((v)-> listener.onClickResponden(surveyItem));
        }
    }

    public interface ItemClickListener{
        void onClickPublish(SurveyItem surveyItem);
        void onClickResponden(SurveyItem surveyItem);
    }
}
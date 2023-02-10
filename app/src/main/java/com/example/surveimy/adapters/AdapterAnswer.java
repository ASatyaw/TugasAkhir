package com.example.surveimy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.surveimy.databinding.RowItemAnswerBinding;
import com.example.surveimy.models.AnswerItem;

import java.util.List;

public class AdapterAnswer extends RecyclerView.Adapter<AdapterAnswer.ItemViewHolder> {
    private List<AnswerItem> answerItemList;
    private Context mContext;
    public AdapterAnswer(Context context, List<AnswerItem> list){
        this.mContext = context;
        this.answerItemList = list;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterAnswer.ItemViewHolder(RowItemAnswerBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        AnswerItem answerItem = answerItemList.get(position);
        holder.bind(answerItem);
    }

    @Override
    public int getItemCount() {
        return answerItemList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        RowItemAnswerBinding binding;

        public ItemViewHolder(@NonNull RowItemAnswerBinding rowItemAnswerBinding) {
            super(rowItemAnswerBinding.getRoot());
            binding = rowItemAnswerBinding;
        }

        public void bind(AnswerItem answerItem) {
            binding.tvQuestion.setText(answerItem.getQuestion());
            binding.tvNumber.setText(answerItem.getNumberQuestion()+". ");
            binding.tvAnswear.setText(answerItem.getAnswerQuestion());

        }
    }
}

package com.example.surveimy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.surveimy.databinding.RowItemQuestionBinding;
import com.example.surveimy.models.QuestionItem;

import java.util.List;

public class AdapterQuestionSummary extends RecyclerView.Adapter<AdapterQuestionSummary.ItemViewHolder>{

    private List<QuestionItem> questionItems;
    private Context mContext;
    private ItemClickListener listener;

    public AdapterQuestionSummary(Context context, List<QuestionItem> list, ItemClickListener itemClickListener){
        this.mContext = context;
        this.questionItems = list;
        this.listener = itemClickListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterQuestionSummary.ItemViewHolder(RowItemQuestionBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        QuestionItem questionItem = questionItems.get(position);
        holder.bind(questionItem);
    }

    @Override
    public int getItemCount() {
        return questionItems.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        RowItemQuestionBinding binding;

        public ItemViewHolder(@NonNull RowItemQuestionBinding rowItemQuestionBinding) {
            super(rowItemQuestionBinding.getRoot());
            binding = rowItemQuestionBinding;
        }

        public void bind(QuestionItem questionItem) {
            binding.tvQuestion.setText(questionItem.getQuestion());
            binding.tvNumber.setText(questionItem.getNumberQuestion()+". ");
            String typeQuestion  = "Type : Single";
            if(questionItem.getOptionItemList().size() > 0){
                typeQuestion = "Type : Multiple";
            }
            binding.tvType.setText(typeQuestion);
            binding.btnDelete.setOnClickListener((v)->{
                listener.onClickDelete(questionItem);
            });
        }
    }

    public interface ItemClickListener{
        void onClickDelete(QuestionItem questionItem);
    }
}

package com.example.surveimy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.surveimy.R;
import com.example.surveimy.databinding.RowItemOptionBinding;
import com.example.surveimy.models.OptionItem;
import com.example.surveimy.models.QuestionItem;

import java.util.ArrayList;
import java.util.List;

public class AdapterOption extends RecyclerView.Adapter<AdapterOption.OptionViewHolder> {

    private List<OptionItem> optionItemList;
    private ArrayList<QuestionItem> questionItems;
    private ItemClickListener listener;
    private Context mContext;
    private ArrayAdapter spinnerAdapter;

    public AdapterOption(Context context, List<OptionItem> list, ArrayList<QuestionItem> questionItemList, ItemClickListener itemClickListener) {
        this.mContext = context;
        this.questionItems = questionItemList;
        this.optionItemList = list;
        this.listener = itemClickListener;
        spinnerAdapter = new ArrayAdapter(mContext, R.layout.row_item_spinner_question, questionItems);
    }

    @NonNull
    @Override
    public OptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterOption.OptionViewHolder(RowItemOptionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OptionViewHolder holder, int position) {
        holder.bind(optionItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return optionItemList.size();
    }

    public interface ItemClickListener {
        void onClickItem(OptionItem optionItem);

        void onClickDelete(OptionItem optionItem);
    }

    public class OptionViewHolder extends RecyclerView.ViewHolder {
        RowItemOptionBinding binding;

        public OptionViewHolder(@NonNull RowItemOptionBinding rowItemOptionBinding) {
            super(rowItemOptionBinding.getRoot());
            binding = rowItemOptionBinding;
        }

        public void bind(OptionItem optionItem) {
            binding.tvTitle.setText(optionItem.getOptionTitle());
            binding.tvTitle.setOnClickListener((v) -> {
                listener.onClickItem(optionItem);
            });
            //btn remove option
            binding.btnDelete.setOnClickListener((v) -> {
                listener.onClickDelete(optionItem);
            });


        }
    }

}

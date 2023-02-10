package com.example.surveimy.ui.notifications;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.surveimy.R;
import com.example.surveimy.adapters.AdapterNotification;
import com.example.surveimy.databinding.ActivityNotificationBinding;
import com.example.surveimy.datatabase.NotificationTable;
import com.example.surveimy.models.NotificationItem;
import com.example.surveimy.ui.survey.DetailSurveyActivity;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    private ActivityNotificationBinding binding;
    private NotificationTable notificationTable;
    private List<NotificationItem> list = new ArrayList<>();
    private AdapterNotification adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.imgBackHome.setOnClickListener((v) -> onBackPressed());
        notificationTable = new NotificationTable(getApplicationContext());
        adapter = new AdapterNotification(getApplicationContext(), list,(notificationItem)->{
            if(notificationItem.getStatus() == 0){
                for(int i = 0; i< list.size();i++){
                    NotificationItem nf = list.get(i);
                    if(nf.getId() == notificationItem.getId()){
                        nf.setStatus(1);
                        adapter.notifyItemChanged(i,nf);
                        notificationTable.updateStatus(notificationItem.getId(),1);
                        break;
                    }
                }
            }
            Intent intent = new Intent(getApplicationContext(), DetailSurveyActivity.class);
            intent.putExtra("surveyId",notificationItem.getKuesionerId());
            startActivity(intent);
        });
        binding.list.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        binding.list.setAdapter(adapter);
        binding.swipeRefresh.setColorSchemeColors(ContextCompat.getColor(getApplicationContext(), R.color.primary));
        binding.swipeRefresh.setOnRefreshListener(() -> {
            if (list.size() > 0)
                list.clear();
            if (binding.layoutError.getVisibility() == VISIBLE)
                binding.layoutError.setVisibility(GONE);
            if (binding.list.getVisibility() == VISIBLE)
                binding.list.setVisibility(GONE);
            getData();
        });

        binding.btnRefresh.setOnClickListener((v) -> {
            if (list.size() > 0)
                list.clear();
            if (binding.layoutError.getVisibility() == VISIBLE)
                binding.layoutError.setVisibility(GONE);
            if (binding.list.getVisibility() == VISIBLE)
                binding.list.setVisibility(GONE);
            binding.swipeRefresh.setRefreshing(true);
            getData();
        });
        binding.btnDeleteAll.setOnClickListener((v)->{
            notificationTable.clearAll();
            adapter.notifyDataSetChanged();
            showErrorLayout(getString(R.string.empty));
        });

        binding.swipeRefresh.post(() -> {
            binding.swipeRefresh.setRefreshing(true);
            getData();
        });
    }

    private void getData() {
        list.addAll(notificationTable.getAllItems());
        if (list.size() > 0) {
            adapter.notifyDataSetChanged();
            hideErrorLayout();
        } else {
            showErrorLayout(getString(R.string.empty));
        }
    }

    private void hideErrorLayout() {
        try {
            if (binding.layoutError.getVisibility() == VISIBLE)
                binding.layoutError.setVisibility(GONE);
            if (binding.list.getVisibility() == GONE)
                binding.list.setVisibility(VISIBLE);
            if (binding.swipeRefresh.isRefreshing())
                binding.swipeRefresh.setRefreshing(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showErrorLayout(String errorMessage) {
        try {
            binding.titleError.setText(errorMessage);
            if (binding.layoutError.getVisibility() == GONE)
                binding.layoutError.setVisibility(VISIBLE);
            if (binding.list.getVisibility() == VISIBLE)
                binding.list.setVisibility(GONE);
            if (binding.swipeRefresh.isRefreshing())
                binding.swipeRefresh.setRefreshing(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
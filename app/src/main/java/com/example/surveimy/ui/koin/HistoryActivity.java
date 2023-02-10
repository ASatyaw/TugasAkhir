package com.example.surveimy.ui.koin;

import static com.example.surveimy.network.Constant.GET_HISTORY;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.surveimy.App;
import com.example.surveimy.R;
import com.example.surveimy.adapters.AdapterHistory;
import com.example.surveimy.databinding.ActivityHistoryBinding;
import com.example.surveimy.models.HistoryItem;
import com.example.surveimy.network.ApiRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private ActivityHistoryBinding binding;
    private List<HistoryItem> list;
    private AdapterHistory adapterHistory;
    private ApiRequest apiRequest;
    private int currentKoin =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        apiRequest = new ApiRequest(this);
        binding.imgBackHome.setOnClickListener(v -> onBackPressed());
        binding.btnTopUp.setOnClickListener((v)->{
            Intent intent = new Intent(this, TopUpActivity.class);
            myActivityResultLauncher.launch(intent);
        });
        binding.btnWithDraw.setOnClickListener((v)->{
            Intent intent = new Intent(this, WithdrawActivity.class);
            myActivityResultLauncher.launch(intent);
        });
        list = new ArrayList<>();
        adapterHistory = new AdapterHistory(list);
        binding.list.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        binding.list.setAdapter(adapterHistory);
        binding.swipeRefresh.setColorSchemeColors(ContextCompat.getColor(getApplicationContext(), R.color.primary));
        binding.swipeRefresh.setOnRefreshListener(() -> {
            if (list.size() > 0)
                list.clear();
            getData();
        });
        binding.swipeRefresh.setRefreshing(true);
        getData();
    }

    private void getData() {
        try {
            JSONObject jsonparams = new JSONObject();
            jsonparams.put("mahasiswaId", String.valueOf(App.getInstance().getMahasiswaId()));
            final String payload = jsonparams.toString();
            apiRequest.requestDataToServer(GET_HISTORY, payload, new ApiRequest.RequestCallback() {
                @Override
                public void onSuccessRequest(JSONObject response) {
                    try {
                        JSONObject jsonUser = response.getJSONObject("user");
                        if(jsonUser!=null){
                            currentKoin = jsonUser.getInt("koin");
                            binding.tvKoin.setText(String.valueOf(currentKoin));
                        }
                        JSONArray jsonArray = response.getJSONArray("data");
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObj = jsonArray.getJSONObject(i);
                                HistoryItem historyItem = new HistoryItem();
                                historyItem.setId(jsonObj.getInt("id"));
                                historyItem.setTitle(jsonObj.getString("title_history"));
                                historyItem.setTypeHistory(jsonObj.getInt("type_history"));
                                historyItem.setKoin(jsonObj.getInt("jumlah_koin"));
                                historyItem.setCreatedAt(jsonObj.getString("created_history"));
                                list.add(historyItem);
                            }
                            adapterHistory.notifyDataSetChanged();
                        } else {
                            Toast.makeText(HistoryActivity.this, "Empty List", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(HistoryActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                    }
                    binding.swipeRefresh.setRefreshing(false);
                }

                @Override
                public void onFailedRequest(String errorMsg, int errorCode) {
                    binding.swipeRefresh.setRefreshing(false);
                    Toast.makeText(HistoryActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            binding.swipeRefresh.setRefreshing(false);
            Toast.makeText(HistoryActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private ActivityResultLauncher<Intent> myActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            if (list.size() > 0)
                list.clear();
            binding.swipeRefresh.setRefreshing(true);
            getData();
        }
    }));
}

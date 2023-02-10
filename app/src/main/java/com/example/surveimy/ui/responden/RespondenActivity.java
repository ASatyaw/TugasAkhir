package com.example.surveimy.ui.responden;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.surveimy.network.Constant.RESPONDEN_SURVEY_URL;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.surveimy.R;
import com.example.surveimy.adapters.AdapterResponden;
import com.example.surveimy.databinding.ActivityRespondenBinding;
import com.example.surveimy.models.RespondenItem;
import com.example.surveimy.models.SurveyItem;
import com.example.surveimy.network.ApiRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RespondenActivity extends AppCompatActivity {

    private SurveyItem surveyItem;
    private ActivityRespondenBinding binding;
    private List<RespondenItem> respondenItemList;
    private AdapterResponden adapterResponden;
    private ApiRequest apiRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRespondenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        surveyItem = getIntent().getParcelableExtra("surveyObject");

        apiRequest = new ApiRequest(this);
        binding.imgBackHome.setOnClickListener((v) -> {
            onBackPressed();
        });
        respondenItemList = new ArrayList<>();
        adapterResponden = new AdapterResponden(this, respondenItemList, (respondenItem) -> {
           if(respondenItem!=null){
               Intent intent = new Intent(RespondenActivity.this, DetailRespondenActivity.class);
               intent.putExtra("respondenObject", respondenItem);
               startActivity(intent);
           }
        });
        binding.list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.list.setAdapter(adapterResponden);
        binding.btnRefresh.setOnClickListener((v) -> {
            if (respondenItemList.size() > 0)
                respondenItemList.clear();
            if (binding.layoutError.getVisibility() == VISIBLE)
                binding.layoutError.setVisibility(GONE);
            if (binding.list.getVisibility() == VISIBLE)
                binding.list.setVisibility(GONE);
            binding.swipeRefresh.setRefreshing(true);
            getData();
        });
        binding.swipeRefresh.setOnRefreshListener(()->{
            if (respondenItemList.size() > 0)
                respondenItemList.clear();
            if (binding.layoutError.getVisibility() == VISIBLE)
                binding.layoutError.setVisibility(GONE);
            if (binding.list.getVisibility() == VISIBLE)
                binding.list.setVisibility(GONE);
            getData();
        });
        binding.swipeRefresh.post(() -> {
            binding.swipeRefresh.setRefreshing(true);
            getData();
        });
    }

    // get responden data
    private void getData() {
        try {
            JSONObject jsonparams = new JSONObject();
            jsonparams.put("kuesionerId", surveyItem.getId());
            final String payload = jsonparams.toString();
            Log.e("Responden", payload);
            apiRequest.requestDataToServer(RESPONDEN_SURVEY_URL, payload, new ApiRequest.RequestCallback() {
                @Override
                public void onSuccessRequest(JSONObject response) {
                    Log.e("RespondenActivity", response.toString());
                    try {
                        JSONArray jsonArray = response.getJSONArray("data");

                        for(int i=0; i< jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            RespondenItem respondenItem = new RespondenItem();
                            respondenItem.setId(jsonObject.getInt("id"));
                            respondenItem.setMahasiswaId(jsonObject.getInt("id_mahasiswa"));
                            respondenItem.setSurveyId(jsonObject.getInt("id_kuesioner"));
                            respondenItem.setUsername(jsonObject.getString("name"));
                            respondenItem.setNim(jsonObject.getString("nim"));
                            respondenItemList.add(respondenItem);
                        }
                        adapterResponden.notifyDataSetChanged();
                        if(respondenItemList.size()>0){
                            hideErrorLayout();
                        }else{
                            showErrorLayout(getString(R.string.empty));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showErrorLayout(getString(R.string.something_error));
                    }
                }

                @Override
                public void onFailedRequest(String errorMsg, int errorCode) {
                    showErrorLayout(errorMsg);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //hide error state
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

    //show error state
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
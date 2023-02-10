package com.example.surveimy.ui.survey;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.surveimy.network.Constant.LIST_SURVEY_URL;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.surveimy.App;
import com.example.surveimy.R;
import com.example.surveimy.adapters.AdapterMySurvey;
import com.example.surveimy.databinding.ActivityMysurveyBinding;
import com.example.surveimy.models.SurveyItem;
import com.example.surveimy.network.ApiRequest;
import com.example.surveimy.ui.responden.RespondenActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MySurveyActivity extends AppCompatActivity {
    private ActivityMysurveyBinding binding;
    private ApiRequest apiRequest;
    private List<SurveyItem> list;
    private AdapterMySurvey adapterMySurvey;
    private String[] years;
    private String[] provinces;
    private String yearFrom = "", yearEnd = "";
    private String selectedGender = "";
    private String selectedProvince = "";
    private int selectedAge = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMysurveyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.imgBackHome.setOnClickListener((v) -> onBackPressed());
        list = new ArrayList<>();
        adapterMySurvey = new AdapterMySurvey(getApplicationContext(), list, new AdapterMySurvey.ItemClickListener() {
            @Override
            public void onClickPublish(SurveyItem item) {
                Intent intent = new Intent(getApplicationContext(), PublishActivity.class);
                intent.putExtra("surveyId",item.getId());
                myActivityResultLauncher.launch(intent);
            }

            @Override
            public void onClickResponden(SurveyItem item) {
                Intent intent = new Intent(getApplicationContext(), RespondenActivity.class);
                intent.putExtra("surveyObject", item);
                startActivity(intent);
            }
        });
        binding.list.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        binding.list.setAdapter(adapterMySurvey);
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
        apiRequest = new ApiRequest(getApplicationContext());
        binding.swipeRefresh.post(() -> {
            binding.swipeRefresh.setRefreshing(true);
            getData();
        });

    }
    //get detail data
    private void getData() {
        if (apiRequest == null) {
            apiRequest = new ApiRequest(getApplicationContext());
        }
        try {

            JSONObject jsonparams = new JSONObject();
            jsonparams.put("mahasiswaId", String.valueOf(App.getInstance().getMahasiswaId()));
            final String payload = jsonparams.toString();
            apiRequest.requestDataToServer(LIST_SURVEY_URL, payload, new ApiRequest.RequestCallback() {
                @Override
                public void onSuccessRequest(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("data");
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObj = jsonArray.getJSONObject(i);
                                SurveyItem item = new SurveyItem();
                                item.setId(jsonObj.getInt("id"));
                                item.setMahasiswaId(jsonObj.getInt("id_mahasiswa"));
                                item.setTitle(jsonObj.getString("title"));
                                item.setDescreption(jsonObj.getString("deskripsi"));
                                item.setStatus(jsonObj.getInt("penyebaran"));
                                item.setResponden(jsonObj.getInt("responden"));
                                item.setReward(jsonObj.getInt("hadiah"));
                                item.setCreatedAt(jsonObj.getString("createdAt"));
                                item.setUpdatedAt(jsonObj.getString("updatedAt"));
                                item.setExpiredAt(jsonObj.getString("expired"));
                                list.add(item);
                            }
                            adapterMySurvey.notifyDataSetChanged();
                            hideErrorLayout();
                        } else {
                            showErrorLayout("Empty List");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        showErrorLayout("Something wrong");
                    }
                }

                @Override
                public void onFailedRequest(String errorMsg, int errorCode) {
                    showErrorLayout(errorMsg);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            showErrorLayout("Empty List");
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

    private ActivityResultLauncher<Intent> myActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            if (list.size() > 0)
                list.clear();
            binding.swipeRefresh.setRefreshing(true);
            getData();
        }
    }));
}

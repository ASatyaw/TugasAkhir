package com.example.surveimy.ui.responden;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.surveimy.network.Constant.DETAIL_RESPONDEN_URL;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.surveimy.R;
import com.example.surveimy.adapters.AdapterAnswer;
import com.example.surveimy.databinding.ActivityDetailRespondenBinding;
import com.example.surveimy.models.AnswerItem;
import com.example.surveimy.models.RespondenItem;
import com.example.surveimy.network.ApiRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailRespondenActivity extends AppCompatActivity {
    private ActivityDetailRespondenBinding binding;
    private List<AnswerItem> answerItemList;
    private ApiRequest apiRequest;
    private RespondenItem respondenItem;
    private AdapterAnswer adapterAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailRespondenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        apiRequest = new ApiRequest(this);
        respondenItem = getIntent().getParcelableExtra("respondenObject");
        binding.imgBackHome.setOnClickListener((v) -> {
            onBackPressed();
        });
        answerItemList = new ArrayList<>();
        adapterAnswer = new AdapterAnswer(this, answerItemList);
        binding.list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.list.setAdapter(adapterAnswer);
        binding.btnRefresh.setOnClickListener((v) -> refreshData());
        binding.swipeRefresh.setOnRefreshListener(()->refreshData());
        binding.swipeRefresh.post(() -> {
            binding.swipeRefresh.setRefreshing(true);
            getData();
        });

    }

    private void refreshData(){
        if (answerItemList.size() > 0)
            answerItemList.clear();
        if (binding.layoutError.getVisibility() == VISIBLE)
            binding.layoutError.setVisibility(GONE);
        if (binding.list.getVisibility() == VISIBLE)
            binding.list.setVisibility(GONE);
        getData();
    }

    private void getData() {
        try {
            JSONObject jsonparams = new JSONObject();
            jsonparams.put("surveyId", respondenItem.getSurveyId());
            jsonparams.put("respondenId", respondenItem.getId());
            final String payload = jsonparams.toString();
            apiRequest.requestDataToServer(DETAIL_RESPONDEN_URL, payload, new ApiRequest.RequestCallback() {
                @Override
                public void onSuccessRequest(JSONObject response) {
                    Log.e("RespondenActivity", response.toString());
                    try {
                        JSONArray jsonArray = response.getJSONArray("data");
                        int numberQuestion = 1;
                        for(int i=0; i< jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            AnswerItem answerItem = new AnswerItem();
                            answerItem.setId(jsonObject.getInt("id"));
                            answerItem.setMahasiswaId(jsonObject.getInt("id_mahasiswa"));
                            answerItem.setSurveyId(jsonObject.getInt("id_kuesioner"));
                            answerItem.setRespondenId(jsonObject.getInt("id_penyebaran"));
                            answerItem.setQuestion(jsonObject.getString("pertanyaan"));
                            answerItem.setAnswerQuestion(jsonObject.getString("jawaban"));
                            answerItem.setNumberQuestion(numberQuestion);
                            answerItemList.add(answerItem);
                            numberQuestion++;
                        }
                        adapterAnswer.notifyDataSetChanged();
                        if(answerItemList.size()>0){
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
package com.example.surveimy.ui.survey;

import static com.example.surveimy.network.Constant.DETAIL_SURVEY_URL;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.surveimy.R;
import com.example.surveimy.databinding.ActivityDetailSurveyBinding;
import com.example.surveimy.models.SurveyItem;
import com.example.surveimy.network.ApiRequest;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;


public class DetailSurveyActivity extends AppCompatActivity {
    private ActivityDetailSurveyBinding binding;
    private ApiRequest apiRequest;
    private int penyebaranId;
    private SurveyItem surveyItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailSurveyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        penyebaranId = getIntent().getIntExtra("penyebaranId",0);
        binding.imgBackHome.setOnClickListener((v)-> onBackPressed());
        binding.btnSurvey.setEnabled(false);
        binding.btnSurvey.setOnClickListener((v)->{
            if(surveyItem !=null ){
                Intent intent = new Intent(DetailSurveyActivity.this, TakeSurveyActivity.class);
                intent.putExtra("surveyObject", surveyItem);
                startActivity(intent);
            }
        });
        apiRequest = new ApiRequest(this);
        getData();
    }
    //get detail survey
    private void getData() {
        apiRequest.setStatusProgress(true);
        try {
            JSONObject jsonparams = new JSONObject();
            jsonparams.put("penyebaranId", penyebaranId);
            final String payload = jsonparams.toString();
            apiRequest.requestDataToServer(DETAIL_SURVEY_URL, payload, new ApiRequest.RequestCallback() {
                @Override
                public void onSuccessRequest(JSONObject response) {
                    try {
                        JSONObject jsonObject = response.getJSONObject("data");
                        surveyItem = new SurveyItem();
                        surveyItem.setId(jsonObject.getInt("id"));
                        surveyItem.setSurveyId(jsonObject.getInt("id_kuesioner"));
                        surveyItem.setMahasiswaId(jsonObject.getInt("id_mahasiswa"));
                        surveyItem.setTitle(jsonObject.getString("title"));
                        surveyItem.setDescreption(jsonObject.getString("deskripsi"));
                        surveyItem.setReward(jsonObject.getInt("hadiah"));
                        surveyItem.setCreatedAt(jsonObject.getString("createdAt"));
                        surveyItem.setExpiredAt(jsonObject.getString("expired"));
                        surveyItem.setStatus(jsonObject.getInt("penyebaran"));

                        binding.tvTitle.setText(surveyItem.getTitle());
                        binding.tvDes.setText(surveyItem.getDescreption());
                        binding.btnSurvey.setEnabled(true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Snackbar.make(binding.getRoot(), getString(R.string.something_error), Snackbar.LENGTH_INDEFINITE)
                                .setAction("Try Again", (v) -> {
                                    getData();
                                }).show();
                    }

                }

                @Override
                public void onFailedRequest(String errorMsg, int errorCode) {
                    Snackbar.make(binding.getRoot(), getString(R.string.something_error), Snackbar.LENGTH_INDEFINITE)
                            .setAction("Try Again", (v) -> {
                                getData();
                            }).show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            Snackbar.make(binding.getRoot(), getString(R.string.something_error), Snackbar.LENGTH_INDEFINITE)
                    .setAction("Try Again", (v) -> {
                        getData();
                    }).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
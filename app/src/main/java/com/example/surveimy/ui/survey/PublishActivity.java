package com.example.surveimy.ui.survey;

import static com.example.surveimy.network.Constant.GET_PROFILE_URL;
import static com.example.surveimy.network.Constant.PUBLISH_SURVEY_URL;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.surveimy.App;
import com.example.surveimy.R;
import com.example.surveimy.databinding.ActivityPublishBinding;
import com.example.surveimy.network.ApiRequest;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class PublishActivity extends AppCompatActivity {
    private ActivityPublishBinding binding;
    private ApiRequest apiRequest;
    private String[] years;
    private String[] provinces;
    private String yearFrom = "", yearEnd = "";
    private String selectedGender = "";
    private String selectedProvince = "";
    private int selectedAge = 0;
    private int surveyId = 0;
    private int currentKoin= 0;
    private int rewardKoin = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPublishBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        apiRequest = new ApiRequest(this);
        binding.imgBackHome.setOnClickListener(v -> onBackPressed());

        //init spinner provinces
        provinces = getResources().getStringArray(R.array.provinces_name);
        ArrayAdapter<String> adapterProvince = new ArrayAdapter<>(this, R.layout.row_item_spinner_question, provinces);
        binding.spinnerProvince.setAdapter(adapterProvince);
        binding.spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    selectedProvince = adapterView.getItemAtPosition(i).toString();
                } else {
                    selectedProvince = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // init department spinner
        years = getResources().getStringArray(R.array.department_years);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, R.layout.row_item_spinner_question, years);
        binding.spinnerFrom.setAdapter(spinnerArrayAdapter);
        binding.spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    yearFrom = adapterView.getItemAtPosition(i).toString();
                } else {
                    yearFrom = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.spinnerEnd.setAdapter(spinnerArrayAdapter);
        binding.spinnerEnd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    yearEnd = adapterView.getItemAtPosition(i).toString();
                } else {
                    yearEnd = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // gender
        binding.radioGrup.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.radioMale) {
                selectedGender = "L";
            } else if (i == R.id.radioFemale) {
                selectedGender = "P";
            } else if (i == R.id.radioAll) {
                selectedGender = "A";
            }
        });

        //Age
        binding.radioGrupAge.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.radioAge1) {
                selectedAge = 1;
            } else if (i == R.id.radioAge1) {
                selectedAge = 2;
            } else {
                selectedAge = 0;
            }
        });
        binding.btnSubmitSurvey.setOnClickListener((v)->{
            String strReward = binding.edtNominal.getText().toString();
            if(strReward.trim().isEmpty()){
                binding.edtNominal.setError(getString(R.string.required));return;
            }
            rewardKoin = Integer.valueOf(strReward);
            publishSurvey();
        });
        surveyId = getIntent().getIntExtra("surveyId",0);
        getData();
    }
    private void publishSurvey(){
        try {
            JSONObject jsonparams = new JSONObject();
            jsonparams.put("kuesionerId", surveyId);
            jsonparams.put("mahasiswaId", App.getInstance().getMahasiswaId());
            jsonparams.put("gender", selectedGender);
            jsonparams.put("province", selectedProvince);
            jsonparams.put("yearFrom", yearFrom);
            jsonparams.put("yearEnd", yearEnd);
            jsonparams.put("age", selectedAge);
            jsonparams.put("reward", rewardKoin);
            final String payload = jsonparams.toString();
            apiRequest.setStatusProgress(true);
            apiRequest.requestDataToServer(PUBLISH_SURVEY_URL, payload, new ApiRequest.RequestCallback() {
                @Override
                public void onSuccessRequest(JSONObject response) {
                    try {
                        Toast.makeText(getApplicationContext(), response.getString("msg"), Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(() -> finish(), 1000);
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), getString(R.string.something_error), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailedRequest(String errorMsg, int errorCode) {
                    Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getData(){
        apiRequest.setStatusProgress(true);
        try {
            JSONObject jsonparams = new JSONObject();
            jsonparams.put("mahasiswaId", App.getInstance().getMahasiswaId());
            final String payload = jsonparams.toString();
            apiRequest.requestDataToServer(GET_PROFILE_URL, payload, new ApiRequest.RequestCallback() {
                @Override
                public void onSuccessRequest(JSONObject response) {
                    try {
                        JSONObject jsonObject = response.getJSONObject("user");
                        if(jsonObject!=null){
                            currentKoin = jsonObject.getInt("koin");
                            binding.tvKoin.setText(String.valueOf(currentKoin));
                        }


                    }catch (JSONException e){
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
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}

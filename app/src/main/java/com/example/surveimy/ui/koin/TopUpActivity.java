package com.example.surveimy.ui.koin;

import static com.example.surveimy.network.Constant.TOP_UP_KOIN;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.surveimy.App;
import com.example.surveimy.databinding.ActivityTopupBinding;
import com.example.surveimy.network.ApiRequest;
import com.google.android.material.chip.Chip;

import org.json.JSONException;
import org.json.JSONObject;

public class TopUpActivity extends AppCompatActivity {
    private ActivityTopupBinding binding;
    private ApiRequest apiRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTopupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        apiRequest = new ApiRequest(this);
        binding.imgBackHome.setOnClickListener(v -> onBackPressed());
        binding.chipGroupNominal.setOnCheckedStateChangeListener((group, checkedIds)-> {
            for( int chipId : checkedIds){
                final Chip chip = group.findViewById(chipId);
                if(chip!=null){
                    binding.edtNominal.setText(chip.getText().toString().replace(".",""));
                }
            }
        });
        binding.btnSubmitTopup.setOnClickListener((v)->{
            String strNominal = binding.edtNominal.getText().toString();
            if(strNominal.trim().isEmpty()){
                binding.edtNominal.setError("Please insert nominal");return;
            }
            final int nominal = Integer.valueOf(strNominal);
            if(nominal<10000){
                binding.edtNominal.setError("Minimum nominal 10.000");return;
            }

            if(nominal>200000){
                binding.edtNominal.setError("Maximal nominal 200.000");return;
            }
            //send top up to backend
            try {
                JSONObject jsonparams = new JSONObject();
                jsonparams.put("mahasiswaId", App.getInstance().getMahasiswaId());
                jsonparams.put("koinTotal", nominal);
                final String payload = jsonparams.toString();
                apiRequest.setStatusProgress(true);
                apiRequest.requestDataToServer(TOP_UP_KOIN, payload, new ApiRequest.RequestCallback() {
                    @Override
                    public void onSuccessRequest(JSONObject response) {
                        try {
                            Toast.makeText(getApplicationContext(), response.getString("msg"), Toast.LENGTH_SHORT).show();
                            new Handler().postDelayed(() -> onBackPressed(), 1000);
                        }catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Something error", Toast.LENGTH_SHORT).show();
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
        });
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

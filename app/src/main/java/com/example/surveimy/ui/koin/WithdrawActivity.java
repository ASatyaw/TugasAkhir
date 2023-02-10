package com.example.surveimy.ui.koin;

import static com.example.surveimy.network.Constant.GET_PROFILE_URL;
import static com.example.surveimy.network.Constant.WITHDRAW_KOIN;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.surveimy.App;
import com.example.surveimy.R;
import com.example.surveimy.databinding.ActivityWithdrawBinding;
import com.example.surveimy.network.ApiRequest;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class WithdrawActivity extends AppCompatActivity {
    private ActivityWithdrawBinding binding;
    private ApiRequest apiRequest;
    private int currentKoin = 0;
    private int nominal=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWithdrawBinding.inflate(getLayoutInflater());
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

        binding.btnSumbitWithdraw.setOnClickListener((v)->{
            String strNominal = binding.edtNominal.getText().toString();
            if(strNominal.trim().isEmpty()){
                binding.edtNominal.setError("Please insert nominal");return;
            }
            nominal = Integer.valueOf(strNominal);
            if(nominal > currentKoin){
                binding.edtNominal.setError("Your Koin not enough");return;
            }
            //withdraw to backend
            withDrawKoin();

        });
        
        getData();
    }
    // withdraw to server
    private void withDrawKoin(){
        try {
            JSONObject jsonparams = new JSONObject();
            jsonparams.put("mahasiswaId", App.getInstance().getMahasiswaId());
            jsonparams.put("koinTotal", nominal);
            final String payload = jsonparams.toString();
            apiRequest.setStatusProgress(true);
            apiRequest.requestDataToServer(WITHDRAW_KOIN, payload, new ApiRequest.RequestCallback() {
                @Override
                public void onSuccessRequest(JSONObject response) {
                    try {
                        Toast.makeText(getApplicationContext(), response.getString("msg"), Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(() -> onBackPressed(), 1000);
                    }catch (JSONException e){
                        e.printStackTrace();
                        Snackbar.make(binding.getRoot(), getString(R.string.something_error), Snackbar.LENGTH_INDEFINITE)
                                .setAction("Try Again", (v) -> {
                                    withDrawKoin();
                                }).show();
                    }
                }

                @Override
                public void onFailedRequest(String errorMsg, int errorCode) {
                    Snackbar.make(binding.getRoot(), errorMsg, Snackbar.LENGTH_INDEFINITE)
                            .setAction("Try Again", (v) -> {
                                withDrawKoin();
                            }).show();
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

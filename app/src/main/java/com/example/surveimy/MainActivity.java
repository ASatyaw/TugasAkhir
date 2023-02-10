package com.example.surveimy;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.surveimy.network.Constant.HOME_URL;
import static com.example.surveimy.network.Constant.STORE_FCM;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.surveimy.adapters.AdapterSurvey;
import com.example.surveimy.databinding.ActivityMainBinding;
import com.example.surveimy.models.SurveyItem;
import com.example.surveimy.network.ApiRequest;
import com.example.surveimy.ui.koin.HistoryActivity;
import com.example.surveimy.ui.login.LoginActivity;
import com.example.surveimy.ui.notifications.NotificationActivity;
import com.example.surveimy.ui.profile.ProfileActivity;
import com.example.surveimy.ui.survey.DetailSurveyActivity;
import com.example.surveimy.ui.survey.MySurveyActivity;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private AlertDialog alertDialog;
    private ApiRequest apiRequest;
    private List<SurveyItem> list;
    private AdapterSurvey adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        apiRequest = new ApiRequest(this);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener((task)->{
            if (!task.isSuccessful()) {
                return;
            }
            // Get new FCM registration token
            String tokenFcm = task.getResult();
            if(tokenFcm !=null){
                App.getInstance().setTokenFcm(tokenFcm);
                storeFcm(tokenFcm);
            }
        });
        binding.btnProfile.setOnClickListener((v)->{
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            myActivityResultLauncher.launch(intent);
        });
        list = new ArrayList<>();
        adapter = new AdapterSurvey(getApplicationContext(), list,(surveyItem)->{
            Intent intent = new Intent(this, DetailSurveyActivity.class);
            intent.putExtra("penyebaranId",surveyItem.getId());
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
        binding.btnRefresh.setOnClickListener((v)-> {
            if (list.size() > 0)
                list.clear();
            if (binding.layoutError.getVisibility() == VISIBLE)
                binding.layoutError.setVisibility(GONE);
            if (binding.list.getVisibility() == VISIBLE)
                binding.list.setVisibility(GONE);
            binding.swipeRefresh.setRefreshing(true);
            getData();
        });
        binding.swipeRefresh.setRefreshing(true);
        binding.btnLogout.setOnClickListener((v)->{
            showAlertLogout();
        });

        binding.btnNotifikasi.setOnClickListener((v)->{
            startActivity(new Intent(MainActivity.this, NotificationActivity.class));
        });
        binding.tvBrodcast.setOnClickListener((v)->{
            startActivity(new Intent(MainActivity.this, MySurveyActivity.class));
        });
        getData();
    }
    //get data home
    private void getData() {
        try {
            JSONObject jsonparams = new JSONObject();
            jsonparams.put("mahasiswaId", String.valueOf(App.getInstance().getMahasiswaId()));
            final String payload = jsonparams.toString();
            apiRequest.requestDataToServer(HOME_URL, payload, new ApiRequest.RequestCallback() {
                @Override
                public void onSuccessRequest(JSONObject response) {
                    try {
                        JSONObject jsonUser = response.getJSONObject("user");
                        JSONArray jsonArray = response.getJSONArray("data");
                        binding.tvName.setText(jsonUser.getString("name"));
                        binding.tvNim.setText(jsonUser.getString("nim"));
                        binding.tvStudy.setText(jsonUser.getString("studi"));
                        binding.tvViewNim.setText("Nim :");
                        binding.tvStudi.setText("Studi :");

                        if(jsonArray.length()>0){
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject jsonObj = jsonArray.getJSONObject(i);
                                SurveyItem item = new SurveyItem();
                                item.setId(jsonObj.getInt("id"));
                                item.setSurveyId(jsonObj.getInt("id_kuesioner"));
                                item.setMahasiswaId(jsonObj.getInt("id_mahasiswa"));
                                item.setTitle(jsonObj.getString("title"));
                                item.setDescreption(jsonObj.getString("deskripsi"));
                                item.setReward(jsonObj.getInt("hadiah"));
                                item.setCreatedAt(jsonObj.getString("createdAt"));
                                item.setExpiredAt(jsonObj.getString("expired"));
                                item.setStatus(jsonObj.getInt("penyebaran"));
                                list.add(item);
                            }
                            adapter.notifyDataSetChanged();
                            hideErrorLayout();
                        }else{
                            showErrorLayout("Empty List");
                        }

                    }catch (JSONException e){
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
        }

    }

    private void storeFcm(String tokenFcm){
        try {
            JSONObject jsonparams = new JSONObject();
            jsonparams.put("mahasiswaId", String.valueOf(App.getInstance().getMahasiswaId()));
            jsonparams.put("token", tokenFcm);
            final String payload = jsonparams.toString();
            apiRequest.requestDataToServer(STORE_FCM, payload, new ApiRequest.RequestCallback() {
                @Override
                public void onSuccessRequest(JSONObject response) {
                    Log.e("MainActivity",response.toString());
                }

                @Override
                public void onFailedRequest(String errorMsg, int errorCode) {
                    Log.e("MainActivity",errorMsg);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void showAlertLogout() {
        if (alertDialog == null) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Logout Account");
            alertDialogBuilder.setMessage(getString(R.string.are_you_sure));
            alertDialogBuilder.setPositiveButton(getString(R.string.yes), (DialogInterface arg0, int arg1) -> {
                App.getInstance().removeUser();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            });

            alertDialog = alertDialogBuilder.create();
        }
        alertDialog.show();
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
            if (binding.layoutError.getVisibility() == VISIBLE)
                binding.layoutError.setVisibility(GONE);
            if (binding.list.getVisibility() == VISIBLE)
                binding.list.setVisibility(GONE);
            getData();
        }
    }));
}
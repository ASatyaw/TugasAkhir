package com.example.surveimy.ui.login;

import static com.example.surveimy.network.Constant.LOGIN_URL;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.surveimy.App;
import com.example.surveimy.MainActivity;
import com.example.surveimy.R;
import com.example.surveimy.databinding.ActivityLoginBinding;
import com.example.surveimy.network.ApiRequest;
import com.example.surveimy.ui.register.RegisterActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private ApiRequest apiRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        binding.btnLogin.setOnClickListener((v) -> validateLogin());
        binding.btnRegister.setOnClickListener((v) -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
        apiRequest = new ApiRequest(this);
    }

    private void validateLogin() {
        final String email = binding.edtEmail.getText().toString();
        final String pass = binding.edtPass.getText().toString();

        if (email.trim().isEmpty()) {
            binding.edtEmail.setError(getResources().getText(R.string.required));
            return;
        }
        binding.edtEmail.setError(null);

        if (pass.trim().isEmpty()) {
            binding.edtPass.setError(getResources().getText(R.string.required));
            return;
        }
        binding.edtPass.setError(null);
        loginUser(email, pass);
    }
    private void loginUser(String email, String pass) {
        apiRequest.setStatusProgress(true);
        try {
            JSONObject jsonparams = new JSONObject();
            jsonparams.put("nim", email);
            jsonparams.put("password", pass);
            final String payload = jsonparams.toString();
            apiRequest.requestDataToServer(LOGIN_URL, payload, new ApiRequest.RequestCallback() {
                @Override
                public void onSuccessRequest(JSONObject response) {
                    try {
                        Toast.makeText(getApplicationContext(), response.getString("msg"), Toast.LENGTH_SHORT).show();
                        JSONObject jsonData = response.getJSONObject("data");
                        final int mahasiswaId = jsonData.getInt("id");
                        new Handler().postDelayed(()->{
                            App.getInstance().saveLogin(mahasiswaId);
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            finish();
                        },1000);

                    }catch (JSONException e){
                        e.printStackTrace();
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
}
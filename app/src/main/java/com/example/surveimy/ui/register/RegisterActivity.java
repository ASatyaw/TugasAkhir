package com.example.surveimy.ui.register;

import static com.example.surveimy.network.Constant.REGISTER_URL;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.surveimy.R;
import com.example.surveimy.databinding.ActivityRegisterBinding;
import com.example.surveimy.network.ApiRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private ApiRequest apiRequest;
    private String gender = "";
    private String fullname = "";
    private String nim = "";
    private String email = "";
    private String pass = "";
    private String study = "";
    private String department = "";
    private String province = "";
    private String bornDate = "";

    private String[] years;
    private String[] provinces;
    private String[] studies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
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
        apiRequest = new ApiRequest(this);

        //spinner department
        years = getResources().getStringArray(R.array.department_years);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, R.layout.row_item_spinner_question, years);

        binding.spinnerDepartment.setAdapter(spinnerArrayAdapter);
        binding.spinnerDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    department = adapterView.getItemAtPosition(i).toString();
                } else {
                    department = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //spinner province
        provinces = getResources().getStringArray(R.array.provinces_name);
        ArrayAdapter<String> adapterProvince = new ArrayAdapter<>(this, R.layout.row_item_spinner_question, provinces);
        binding.spinnerProvince.setAdapter(adapterProvince);
        binding.spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    province = adapterView.getItemAtPosition(i).toString();
                } else {
                    province = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //spinner study
        studies = getResources().getStringArray(R.array.study_programs);
        ArrayAdapter<String> adapterStudy = new ArrayAdapter<>(this, R.layout.row_item_spinner_question, studies);
        binding.spinnerStudy.setAdapter(adapterStudy);
        binding.spinnerStudy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    study = adapterView.getItemAtPosition(i).toString();
                } else {
                    study = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        binding.radioGrup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radioMale) {
                    gender = "P";
                } else if (i == R.id.radioFemale) {
                    gender = "W";
                }

            }
        });
        binding.btnRegister.setOnClickListener((v) -> {
            validateRegister();
        });
    }

    private void validateRegister() {
        fullname = binding.edtName.getText().toString();
        nim = binding.edtNim.getText().toString();
        email = binding.edtEmail.getText().toString();
        pass = binding.edtPass.getText().toString();
        bornDate = binding.edtBorn.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (fullname.trim().isEmpty()) {
            binding.edtName.setError(getResources().getText(R.string.required));
            return;
        }
        binding.edtName.setError(null);

        if (nim.trim().isEmpty()) {
            binding.edtNim.setError(getResources().getText(R.string.required));
            return;
        }
        binding.edtNim.setError(null);

        if (email.trim().isEmpty()) {
            binding.edtEmail.setError(getResources().getText(R.string.required));
            return;
        }
        if (!email.trim().matches(emailPattern)) {
            binding.edtEmail.setError(getResources().getText(R.string.invalid_email_addr));
            return;
        }
        binding.edtEmail.setError(null);

        if (pass.trim().isEmpty()) {
            binding.edtPass.setError(getResources().getText(R.string.required));
            return;
        }
        binding.edtPass.setError(null);
        if (gender.trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Tolong Isi Jenis Kelamin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (study.trim().isEmpty() || study.equals("Study")) {
            Toast.makeText(getApplicationContext(), "Tolong Isi Tahun Angkatan", Toast.LENGTH_SHORT).show();
            return;
        }
        if (department.trim().isEmpty() || department.equals("Department")) {
            Toast.makeText(getApplicationContext(), "Tolong Isi Program Studi", Toast.LENGTH_SHORT).show();
            return;
        }
        if (province.trim().isEmpty() || province.equals("Province")) {
            Toast.makeText(getApplicationContext(), "Tolong Isi Provinsi", Toast.LENGTH_SHORT).show();
            return;
        }
        if (bornDate.trim().isEmpty()) {
            binding.edtBorn.setError(getResources().getText(R.string.required));
            return;
        }
        binding.edtBorn.setError(null);

        try {
            apiRequest.setStatusProgress(true);
            JSONObject jsonparams = new JSONObject();
            jsonparams.put("username", fullname);
            jsonparams.put("nim", nim);
            jsonparams.put("email", email);
            jsonparams.put("password", pass);
            jsonparams.put("study", study);
            jsonparams.put("department", department);
            jsonparams.put("province", province);
            jsonparams.put("born", bornDate);
            jsonparams.put("gender", gender);
            final String payload = jsonparams.toString();
            Log.e("RegisterActivity", payload);
            apiRequest.requestDataToServer(REGISTER_URL, payload, new ApiRequest.RequestCallback() {
                @Override
                public void onSuccessRequest(JSONObject response) {
                    try {
                        Toast.makeText(getApplicationContext(), response.getString("msg"), Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(() -> finish(), 1000);
                    } catch (JSONException e) {
                        e.printStackTrace();
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
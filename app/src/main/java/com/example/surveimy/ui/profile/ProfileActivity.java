package com.example.surveimy.ui.profile;

import static com.example.surveimy.network.Constant.GET_PROFILE_URL;
import static com.example.surveimy.network.Constant.UPDATE_PROFILE_URL;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.surveimy.App;
import com.example.surveimy.R;
import com.example.surveimy.databinding.ActivityProfileBinding;
import com.example.surveimy.network.ApiRequest;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
    private ApiRequest apiRequest;
    private String gender = "";
    private String fullname = "";
    private String nim = "";
    private String email = "";
    private String study = "";
    private String department = "";
    private String province = "";
    private String bornDate = "";

    private String[] years;
    private String[] provinces;
    private String[] studies;
    private ArrayAdapter<String> adapterProvince,adapterYears,adapterStudy;
    private int mahasiswaId = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        apiRequest = new ApiRequest(this);
        mahasiswaId = App.getInstance().getMahasiswaId();
        apiRequest = new ApiRequest(this);
        binding.imgBackHome.setOnClickListener(v-> onBackPressed());
        //spinner department
        years = getResources().getStringArray(R.array.department_years);
        adapterYears = new ArrayAdapter<>(this, R.layout.row_item_spinner_question, years);
        binding.spinnerDepartment.setAdapter(adapterYears);
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
        adapterProvince = new ArrayAdapter<>(this, R.layout.row_item_spinner_question, provinces);
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
        adapterStudy = new ArrayAdapter<>(this, R.layout.row_item_spinner_question, studies);
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
        binding.btnSubmit.setOnClickListener((v) -> {
            updateProfile();
        });
        getProfile();
    }
    //get detail profile on database
    private void getProfile(){
        apiRequest.setStatusProgress(true);
        try {
            JSONObject jsonparams = new JSONObject();
            jsonparams.put("mahasiswaId", String.valueOf(App.getInstance().getMahasiswaId()));
            final String payload = jsonparams.toString();
            apiRequest.requestDataToServer(GET_PROFILE_URL, payload, new ApiRequest.RequestCallback() {
                @Override
                public void onSuccessRequest(JSONObject response) {
                    try {
                        JSONObject jsonObject = response.getJSONObject("user");
                        binding.edtName.setText(jsonObject.getString("name"));
                        binding.edtNim.setText(jsonObject.getString("nim"));
                        binding.edtEmail.setText(jsonObject.getString("email"));
                        binding.edtBorn.setText(jsonObject.getString("kelahiran"));
                        final String objgender = jsonObject.getString("gender");
                        if(objgender.equals("P")){
                            binding.radioMale.setChecked(true);
                        }else if(objgender.equals("W")){
                            binding.radioFemale.setChecked(true);
                        }

                        final String objstudi = jsonObject.getString("studi");
                        int spinnerPosition = adapterStudy.getPosition(objstudi);
                        binding.spinnerStudy.setSelection(spinnerPosition);

                        final String objdepartment = jsonObject.getString("angkatan");
                        int spinnerPosition2 = adapterYears.getPosition(objdepartment);
                        binding.spinnerDepartment.setSelection(spinnerPosition2);

                        final String objprovince = jsonObject.getString("provinsi");
                        int spinnerPosition3 = adapterProvince.getPosition(objprovince);
                        binding.spinnerProvince.setSelection(spinnerPosition3);

                    }catch (JSONException e){
                        e.printStackTrace();
                        showErrorSnackBar();
                    }
                }
                @Override
                public void onFailedRequest(String errorMsg, int errorCode) {
                    showErrorSnackBar();
                }
            });
        } catch (JSONException e) {
            showErrorSnackBar();
            e.printStackTrace();
        }
    }
    private void updateProfile() {
        fullname = binding.edtName.getText().toString();
        nim = binding.edtNim.getText().toString();
        email = binding.edtEmail.getText().toString();
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

        if (gender.trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Check gender", Toast.LENGTH_SHORT).show();
            return;
        }

        if (study.trim().isEmpty() || study.equals("Study")) {
            Toast.makeText(getApplicationContext(), "Please select Study", Toast.LENGTH_SHORT).show();
            return;
        }
        if (department.trim().isEmpty() || department.equals("Department")) {
            Toast.makeText(getApplicationContext(), "Please select department", Toast.LENGTH_SHORT).show();
            return;
        }
        if (province.trim().isEmpty() || province.equals("Province")) {
            Toast.makeText(getApplicationContext(), "Please select province", Toast.LENGTH_SHORT).show();
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
            jsonparams.put("mahasiswaId", mahasiswaId);
            jsonparams.put("username", fullname);
            jsonparams.put("nim", nim);
            jsonparams.put("email", email);
            jsonparams.put("study", study);
            jsonparams.put("department", department);
            jsonparams.put("province", province);
            jsonparams.put("born", bornDate);
            jsonparams.put("gender", gender);
            final String payload = jsonparams.toString();
            apiRequest.requestDataToServer(UPDATE_PROFILE_URL, payload, new ApiRequest.RequestCallback() {
                @Override
                public void onSuccessRequest(JSONObject response) {
                    try {
                        Toast.makeText(getApplicationContext(), response.getString("msg"), Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(()->{
                            Intent intent = new Intent();
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        },1000);
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

    private void showErrorSnackBar(){
        Snackbar.make(binding.getRoot(), getString(R.string.something_error), Snackbar.LENGTH_INDEFINITE)
                .setAction("Try Again", (v) -> {
                    getProfile();
                }).show();
    }

}
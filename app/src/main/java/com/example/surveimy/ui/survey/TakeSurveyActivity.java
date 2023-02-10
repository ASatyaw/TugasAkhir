package com.example.surveimy.ui.survey;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.surveimy.R.string.something_error;
import static com.example.surveimy.network.Constant.ANSWEAR_SURVEY_URL;
import static com.example.surveimy.network.Constant.LIST_QUESTION_URL;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.surveimy.App;
import com.example.surveimy.R;
import com.example.surveimy.databinding.ActivityTakeSurveyBinding;
import com.example.surveimy.models.AnswerItem;
import com.example.surveimy.models.OptionItem;
import com.example.surveimy.models.QuestionItem;
import com.example.surveimy.models.SurveyItem;
import com.example.surveimy.network.ApiRequest;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TakeSurveyActivity extends AppCompatActivity {
    private ActivityTakeSurveyBinding binding;
    private ApiRequest apiRequest;
    private int currentQuestion = 0;
    private SurveyItem surveyItem;
    private List<AnswerItem> answerItemList;
    private List<QuestionItem> questionItems;
    private List<QuestionItem> questionSectionItems;
    private List<QuestionItem> combineQuestionItems;
    private Snackbar snackbarError;
    private int mahasiswaId = 0;
    private boolean isSection = false;
    private int lastSection = 0,lastCurrentSection = 0;
    private QuestionItem lastQuestion;
    private LinearLayout.LayoutParams layoutParams;
    private int PaddingRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTakeSurveyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.imgBackHome.setOnClickListener((v) -> onBackPressed());
        mahasiswaId = App.getInstance().getMahasiswaId();

        apiRequest = new ApiRequest(this);
        answerItemList = new ArrayList<>();
        questionSectionItems = new ArrayList<>(); // all section question
        questionItems = new ArrayList<>(); // master question
        combineQuestionItems = new ArrayList<>(); // combine question
        surveyItem = getIntent().getParcelableExtra("surveyObject");
        if (surveyItem != null) binding.tvToolbarTitle.setText(surveyItem.getTitle());
        binding.btnPrev.setOnClickListener((v) -> prevQuestion());
        binding.btnNext.setOnClickListener((v) -> nextQuestion());
        binding.btnFinish.setOnClickListener(view -> sendAnswerToServer());

        PaddingRadioButton = dpToInt(8);
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, PaddingRadioButton, 0, PaddingRadioButton);

        snackbarError = Snackbar.make(binding.getRoot(), getString(something_error), Snackbar.LENGTH_INDEFINITE)
                .setAction("Try Again", (v) -> {
                    getDataQuestion();
                });
        getDataQuestion();
    }

    // store responden survey
    private void sendAnswerToServer() {
        try {
            final JSONObject jsonparams = new JSONObject();
            jsonparams.put("penyebaranId", surveyItem.getId());
            jsonparams.put("kuesionerId", surveyItem.getSurveyId());
            jsonparams.put("mahasiswaId", mahasiswaId);
            JSONArray arrayAnswer = new JSONArray();
            for (AnswerItem item : answerItemList) {
                JSONObject tempObj = new JSONObject();
                tempObj.put("id_kuesioner", surveyItem.getSurveyId());
                tempObj.put("id_pertanyaan", item.getQuestionId());
                tempObj.put("id_mahasiswa", mahasiswaId);
                tempObj.put("jawaban", item.getAnswerQuestion());
                tempObj.put("id_penyebaran", surveyItem.getId());
                arrayAnswer.put(tempObj);
            }
            jsonparams.put("answears", arrayAnswer.toString());
            final String payload = jsonparams.toString();

            apiRequest.requestDataToServer(ANSWEAR_SURVEY_URL, payload, new ApiRequest.RequestCallback() {
                @Override
                public void onSuccessRequest(JSONObject response) {
                    try {
                        Toast.makeText(getApplicationContext(), response.getString("msg"), Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(() -> finish(), 1000);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        snackbarError.show();
                    }
                }

                @Override
                public void onFailedRequest(String errorMsg, int errorCode) {
                    Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), getString(something_error), Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // prev button pressed
    private void prevQuestion() {
        if (currentQuestion > 0) {
            currentQuestion--;

            answerItemList.remove(currentQuestion);
            if (currentQuestion == 0) {
                binding.btnPrev.setVisibility(GONE);
                binding.btnNext.setVisibility(VISIBLE);
                binding.btnFinish.setVisibility(GONE);
            } else if (currentQuestion > 0) {
                binding.btnPrev.setVisibility(VISIBLE);
                binding.btnNext.setVisibility(VISIBLE);
                binding.btnFinish.setVisibility(GONE);
            }
            showQuestion(combineQuestionItems);
        }
    }

    // next button pressed
    private void nextQuestion() {
        final boolean isSingle = lastQuestion.getOptionItemList().isEmpty() ? true : false;
        String answearStr = getAnswerQuestion(isSingle);
        if (answearStr.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Jawaban belum terisi", Toast.LENGTH_SHORT).show();
            return;
        }
        //adding answear
        AnswerItem answerItem = new AnswerItem();
        answerItem.setQuestionId(lastQuestion.getId());
        answerItem.setAnswerQuestion(answearStr);
        answerItemList.add(answerItem);

        if (isSingle) {
            binding.edtAnswer.setText(null);
        } else { //
            lastSection = 0;
            isSection = false;
            for (OptionItem item : lastQuestion.getOptionItemList()) {
                if (item.getOptionTitle().equals(answearStr) && item.getSection() != 0) {
                    isSection = true;
                    lastSection = item.getSection();
                    break;
                }
            }


            if(isSection){
                if(combineQuestionItems.size() > questionItems.size()){
                    combineQuestionItems.clear();
                    combineQuestionItems.addAll(questionItems);
                }
                //combine section question
                for(QuestionItem item1 : questionSectionItems){
                    if(item1.getSection() == lastSection){
                        combineQuestionItems.add(item1);
                    }
                }
            }
        }

        int sisaPertanyaan = (combineQuestionItems.size() - 1) - currentQuestion;
        Log.e("TakeSurvey","sisa question : "+sisaPertanyaan);
        if (sisaPertanyaan >= 1) {
            binding.btnPrev.setVisibility(VISIBLE);
            binding.btnNext.setVisibility(VISIBLE);
            binding.btnFinish.setVisibility(GONE);
            currentQuestion++;
            showQuestion(combineQuestionItems);
        } else if (sisaPertanyaan == 0) { // selesai
            binding.btnPrev.setVisibility(VISIBLE);
            binding.btnNext.setVisibility(GONE);
            binding.btnFinish.setVisibility(VISIBLE);
            binding.tvNumber.setVisibility(GONE);
            binding.radioGrupAnswear.setVisibility(GONE);
            binding.edtAnswer.setVisibility(GONE);
            binding.tvQuestion.setText("Selesai, silakan submit untuk mengirimkan data");
            currentQuestion++;
        }
    }

    //show question
    private void showQuestion(List<QuestionItem> itemList) {

        if (binding.tvNumber.getVisibility() == GONE) {
            binding.tvNumber.setVisibility(VISIBLE);
        }
        lastQuestion = itemList.get(currentQuestion);
        int numberQuestion = currentQuestion+1;

        binding.tvNumber.setText(String.valueOf(numberQuestion));
        binding.tvQuestion.setText(lastQuestion.getQuestion());
        //init option question
        List<OptionItem> optionItemList = lastQuestion.getOptionItemList();
        if (optionItemList.size() > 0) {
            binding.radioGrupAnswear.removeAllViews();
            for (OptionItem item : lastQuestion.getOptionItemList()) {
                final RadioButton rb = new RadioButton(this);
                rb.setLayoutParams(layoutParams);
                rb.setText(item.getOptionTitle());
                rb.setTextColor(ContextCompat.getColor(this, R.color.primary));
                rb.setPadding(PaddingRadioButton, 0, 0, 0);
                rb.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.primary)));
                rb.setId(item.getId());
                rb.setChecked(false);
                binding.radioGrupAnswear.addView(rb);
            }
            binding.radioGrupAnswear.setVisibility(VISIBLE);
            binding.edtAnswer.clearFocus();
            binding.edtAnswer.setEnabled(false);
            binding.edtAnswer.setVisibility(GONE);
        } else {
            binding.radioGrupAnswear.setVisibility(GONE);
            binding.edtAnswer.setVisibility(VISIBLE);
            binding.edtAnswer.setEnabled(true);
        }


    }

    //getString text or radio value
    private String getAnswerQuestion(boolean isSingle) {
        String tempAnswer = "";
        if (isSingle) {
            tempAnswer = binding.edtAnswer.getText().toString();
        } else {
            int selectedId = binding.radioGrupAnswear.getCheckedRadioButtonId();
            RadioButton radioButton = (RadioButton) findViewById(selectedId);
            if (radioButton != null) {
                tempAnswer = radioButton.getText().toString();
            } else {
                tempAnswer = "";
            }
        }
        return tempAnswer;
    }

    private int dpToInt(int dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
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

    //get list question
    private void getDataQuestion() {
        if (!questionSectionItems.isEmpty()) {
            questionSectionItems.clear();
            questionItems.clear();
            answerItemList.clear();
        }

        apiRequest.setStatusProgress(true);
        try {
            JSONObject jsonparams = new JSONObject();
            jsonparams.put("kuesionerId", String.valueOf(surveyItem.getSurveyId()));
            final String payload = jsonparams.toString();
            apiRequest.requestDataToServer(LIST_QUESTION_URL, payload, new ApiRequest.RequestCallback() {
                @Override
                public void onSuccessRequest(JSONObject response) {
                    try {
                        JSONArray arrMaster = response.getJSONArray("master");
                        JSONArray arrSections = response.getJSONArray("data");
                        for (int i = 0; i < arrMaster.length(); i++) {
                            JSONObject jsonQuestion = arrMaster.getJSONObject(i);
                            QuestionItem questionItem = new QuestionItem();
                            questionItem.setId(jsonQuestion.getInt("id"));
                            questionItem.setQuestion(jsonQuestion.getString("pertanyaan"));
                            questionItem.setSection(jsonQuestion.getInt("section"));
                            JSONArray optionArr = jsonQuestion.getJSONArray("options");
                            final List<OptionItem> optionItemList = new ArrayList<>();
                            for (int j = 0; j < optionArr.length(); j++) {
                                JSONObject jsonOption = optionArr.getJSONObject(j);
                                OptionItem optionItem = new OptionItem();
                                optionItem.setId(jsonOption.getInt("id"));
                                optionItem.setQuestionId(jsonOption.getInt("id_pertanyaan"));
                                optionItem.setOptionTitle(jsonOption.getString("option"));
                                final int section = jsonOption.isNull("section") ? 0 : jsonOption.getInt("section");
                                optionItem.setSection(section);

                                optionItemList.add(optionItem);
                            }
                            questionItem.setOptionItemList(optionItemList);
                            questionItems.add(questionItem);
                        }


                        for (int i = 0; i < arrSections.length(); i++) {
                            JSONObject jsonQuestion = arrSections.getJSONObject(i);
                            QuestionItem questionItem = new QuestionItem();
                            questionItem.setId(jsonQuestion.getInt("id"));
                            questionItem.setQuestion(jsonQuestion.getString("pertanyaan"));
                            questionItem.setSection(jsonQuestion.getInt("section"));
                            JSONArray optionArr = jsonQuestion.getJSONArray("options");
                            final List<OptionItem> optionItemList = new ArrayList<>();
                            for (int j = 0; j < optionArr.length(); j++) {
                                JSONObject jsonOption = optionArr.getJSONObject(j);
                                OptionItem optionItem = new OptionItem();
                                optionItem.setId(jsonOption.getInt("id"));
                                optionItem.setQuestionId(jsonOption.getInt("id_pertanyaan"));
                                optionItem.setOptionTitle(jsonOption.getString("option"));
                                final int section = jsonOption.isNull("section") ? 0 : 1;
                                optionItem.setSection(section);
                                optionItemList.add(optionItem);
                            }
                            questionItem.setOptionItemList(optionItemList);
                            questionSectionItems.add(questionItem);
                        }
                        combineQuestionItems.addAll(questionItems);
                        showQuestion(combineQuestionItems);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        snackbarError.show();
                    }
                }

                @Override
                public void onFailedRequest(String errorMsg, int errorCode) {
                    snackbarError.show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
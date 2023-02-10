package com.example.surveimy.network;

public class Constant {
    public static String BASE_URL = "http://192.168.1.3:5000/app/";
    public static String REGISTER_URL = BASE_URL + "register";
    public static String LOGIN_URL = BASE_URL + "login";
    public static String STORE_FCM = BASE_URL + "fcm";
    public static String HOME_URL = BASE_URL + "home";
    public static String LIST_SURVEY_URL = BASE_URL + "kuesioner/list";

    public static String PUBLISH_SURVEY_URL =  BASE_URL + "kuesioner/publish";
    public static String DETAIL_SURVEY_URL = BASE_URL + "penyebaran/detail";

    public static String ANSWEAR_SURVEY_URL = BASE_URL + "penyebaran/answer";
    public static String LIST_QUESTION_URL = BASE_URL + "pertanyaan";
    public static String RESPONDEN_SURVEY_URL = BASE_URL +"responden/list";
    public static String DETAIL_RESPONDEN_URL = BASE_URL +"responden/detail";

    public static String GET_PROFILE_URL = BASE_URL +"mahasiswa";
    public static String UPDATE_PROFILE_URL = BASE_URL +"mahasiswa/update";

    public static String GET_HISTORY = BASE_URL +"koin/list";
    public static String TOP_UP_KOIN = BASE_URL +"koin/topup";
    public static String WITHDRAW_KOIN = BASE_URL +"koin/withdraw";

}

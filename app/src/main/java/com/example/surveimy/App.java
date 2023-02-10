package com.example.surveimy;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class App extends Application {
    private static App mInstance;
    private SharedPreferences sharedPref;
    private int mahasiswaId;
    private String tokenFcm;
    private boolean isLogin;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        sharedPref = this.getSharedPreferences("myprefsurvei", Context.MODE_PRIVATE);
        readMyPref();
    }

    public static synchronized App getInstance() {
        return mInstance;
    }

    private void readMyPref() {
        this.setIsLogin(sharedPref.getBoolean("islogin", false));
        this.setMahasiswaId(sharedPref.getInt("mahasiswaId", 0));
    }
    public void removeUser() {
        sharedPref.edit().putBoolean("islogin", false).apply();
        this.setIsLogin(false);
        sharedPref.edit().putInt("mahasiswaId", 0).apply();
        this.setMahasiswaId(0);
    }
    public void saveLogin(int id) {
        sharedPref.edit().putBoolean("islogin", true).apply();
        this.setIsLogin(true);
        sharedPref.edit().putInt("mahasiswaId", id).apply();
        this.setMahasiswaId(id);
    }
    public int getMahasiswaId() {
        return mahasiswaId;
    }

    public void setMahasiswaId(int mahasiswaId) {
        this.mahasiswaId = mahasiswaId;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean login) {
        isLogin = login;
    }

    public String getTokenFcm() {
        return tokenFcm;
    }

    public void setTokenFcm(String tokenFcm) {
        this.tokenFcm = tokenFcm;
    }
}

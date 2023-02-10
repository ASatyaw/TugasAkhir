package com.example.surveimy.network;

import static android.content.Context.CONNECTIVITY_SERVICE;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.example.surveimy.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ApiRequest {

    private static final String TAG = ApiRequest.class.getSimpleName();
    // DEFAULT 10 SECOND FOR TIMEOUT
    private final int INITIAL_TIME_OUT = 10000, MIDDLE_TIME_OUT = 30000, MAXIMUM_TIME_OUT = 60000;

    private int MODE_TIMEOUT = 0, MAX_NUM_RETRY = 0;
    private RequestQueue requestQueue;
    private final Context context;
    private final ProgressDialog dialog;
    private boolean statusProgress = false;

    public ApiRequest(Context context) {
        this.context = context;
        dialog = new ProgressDialog(context);
        dialog.setTitle(context.getString(R.string.please_wait));
        dialog.setMessage(context.getString(R.string.loading));
        dialog.setCancelable(false);
    }

    public void setMODE_TIMEOUT(int mode_timeout) {
        this.MODE_TIMEOUT = mode_timeout;
    }

    public void setMAX_NUM_RETRY(int MAX_NUM) {
        this.MAX_NUM_RETRY = MAX_NUM;
    }

    public void setStatusProgress(boolean status) {
        this.statusProgress = status;
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    //function menampilkan dialog
    protected void tampilDialog() {
        if (!dialog.isShowing()) {
            try {
                dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //function tutup dialog
    protected void tutupDialog() {
        if (dialog.isShowing()) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* Simple Request Queue */
    private <T> void addToRequestQueue(Request<T> request) {
        request.setTag(TAG);
        if (MODE_TIMEOUT == 1) {
            request.setRetryPolicy(new DefaultRetryPolicy(MIDDLE_TIME_OUT, MAX_NUM_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } else if (MODE_TIMEOUT == 2) {
            request.setRetryPolicy(new DefaultRetryPolicy(MAXIMUM_TIME_OUT, MAX_NUM_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } else {
            request.setRetryPolicy(new DefaultRetryPolicy(INITIAL_TIME_OUT, MAX_NUM_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }

        getRequestQueue().add(request);
    }

    /**
     * Request queue using tag
     *
     * @param request
     * @param tag
     * @param <T>
     */
    private <T> void addToRequestQueue(Request<T> request, String tag) {
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        if (MODE_TIMEOUT == 1) {
            request.setRetryPolicy(new DefaultRetryPolicy(MIDDLE_TIME_OUT, MAX_NUM_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } else if (MODE_TIMEOUT == 2) {
            request.setRetryPolicy(new DefaultRetryPolicy(MAXIMUM_TIME_OUT, MAX_NUM_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } else {
            request.setRetryPolicy(new DefaultRetryPolicy(INITIAL_TIME_OUT, MAX_NUM_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }
        getRequestQueue().add(request);
    }

    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll((req) -> {
                return true;
            });
            if (statusProgress) {
                tutupDialog();
            }
        }
    }

    public interface RequestCallback {
        void onSuccessRequest(JSONObject response);

        void onFailedRequest(String errorMsg, int errorCode);
    }

    public void clearCacheRequest() {
        RequestQueue requestQueue = getRequestQueue();
        requestQueue.getCache().clear();
    }


    //add request
    public void requestDataToServer(String url, final String requestBody, RequestCallback callback) {
        if (isNetworkAvailable()) {
            if (statusProgress) {
                tampilDialog();
            }
            CustomRequest customRequest = new CustomRequest(Request.Method.POST, url, null, (response) -> {
                if (statusProgress) {
                    tutupDialog();
                }
                callback.onSuccessRequest(response);
            }, (volleyerror) -> {
                NetworkResponse networkResponse = volleyerror.networkResponse;
                String msgError = context.getString(R.string.something_error);
                int errorCode = 500;
                if (networkResponse != null) {
                    String jsonError = new String(networkResponse.data);
                    try {
                        JSONObject obj = new JSONObject(jsonError);
                        msgError = obj.getString("error");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                tutupDialog();
                callback.onFailedRequest(msgError, errorCode);

            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json");
                    return params;
                }

                @Override
                public byte[] getBody() {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding", requestBody, "utf-8");
                        return null;
                    }
                }
            };
            addToRequestQueue(customRequest);
        } else {
            callback.onFailedRequest(context.getString(R.string.no_connection), 503);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }
}

/////////////////////////////////////////////////////////////////
// Legion_RestClient.java
//
// Created by Annapoorna
// Notifii Project
//
//Copyright (c) 2016 Notifii, LLC. All rights reserved
/////////////////////////////////////////////////////////////////
package network;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.SSLException;

import co.legion.client.R;
import co.legion.client.activities.CreateAccountActivity;
import co.legion.client.activities.LoginActivity;
import co.legion.client.activities.VerifyIdentityActivity;
import cz.msebera.android.httpclient.entity.StringEntity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import utils.LegionUtils;
import utils.Legion_Constants;

/**
 * This class Handles the HTTP requests..
 */
public class Legion_RestClient implements Legion_Constants {
    private static AsyncHttpClient asyncHttpClient;
    private final Context context;
    private Legion_NetworkCallback networkCallback;
    private Activity parentActivity;


    public Legion_RestClient(Context mContext, Legion_NetworkCallback networkCallback) {
        this.context = mContext;
        if (asyncHttpClient == null) {
            asyncHttpClient = new AsyncHttpClient();
        }
        this.networkCallback = networkCallback;
    }

    public static AsyncHttpClient getAsyncHttpClient(Context context) {
        asyncHttpClient.setMaxRetriesAndTimeout(20, TIME_OUT_FOR_SERVICE_REQUEST);
        return asyncHttpClient;
    }

    public void performHTTPGetRequest(final int requestCode, final String url, final RequestParams params, final String httpHeaders, final Legion_NetworkCallback networkCallback) throws Exception {
        if (httpHeaders != null) {
            getAsyncHttpClient(context).addHeader("sessionId", httpHeaders);
        }
        try {
            parentActivity = (Activity) networkCallback;
        } catch (Exception e1) {
            try {
                parentActivity = ((android.support.v4.app.Fragment) networkCallback).getActivity();
            } catch (Exception e2) {
                parentActivity = ((android.app.Fragment) networkCallback).getActivity();
            }
        }
        getAsyncHttpClient(context).get(context, url, params, new Legion_AsyncHttpResponseHandler(requestCode, networkCallback, url, params.toString(), true, parentActivity));
    }

    public void performPostRequest(final int requestCode, final String url, final Object reqObject, final String httpHeader, final Legion_NetworkCallback networkCallback) throws Exception {
        if (httpHeader != null) {
            getAsyncHttpClient(context).addHeader("sessionId", httpHeader);
        }
        try {
            parentActivity = (Activity) networkCallback;
        } catch (Exception e1) {
            try {
                parentActivity = ((android.support.v4.app.Fragment) networkCallback).getActivity();
            } catch (Exception e2) {
                parentActivity = ((android.app.Fragment) networkCallback).getActivity();
            }
        }
        getAsyncHttpClient(context).post(context, url, new StringEntity(reqObject.toString()), CONTENT_TYPE_APPLICATION_JSON, new Legion_AsyncHttpResponseHandler(requestCode, networkCallback, url, reqObject.toString(), false, parentActivity));
    }

    public void performPutRequest(final int requestCode, final String url, final Object reqObject, final String httpHeader, final Legion_NetworkCallback networkCallback) throws Exception {
        if (httpHeader != null) {
            getAsyncHttpClient(context).addHeader("sessionId", httpHeader);
        }
        try {
            parentActivity = (Activity) networkCallback;
        } catch (Exception e1) {
            try {
                parentActivity = ((android.support.v4.app.Fragment) networkCallback).getActivity();
            } catch (Exception e2) {
                parentActivity = ((android.app.Fragment) networkCallback).getActivity();
            }
        }
        getAsyncHttpClient(context).put(context, url, new StringEntity(reqObject.toString()), CONTENT_TYPE_APPLICATION_JSON, new Legion_AsyncHttpResponseHandler(requestCode, networkCallback, url, reqObject.toString(), false, parentActivity));
    }

    public void performDeleteRequest(final int requestCode, final String url, final Object reqObject, final String httpHeader, final Legion_NetworkCallback networkCallback) throws Exception {
        if (httpHeader != null) {
            getAsyncHttpClient(context).addHeader("sessionId", httpHeader);
        }
        try {
            parentActivity = (Activity) networkCallback;
        } catch (Exception e1) {
            try {
                parentActivity = ((android.support.v4.app.Fragment) networkCallback).getActivity();
            } catch (Exception e2) {
                parentActivity = ((android.app.Fragment) networkCallback).getActivity();
            }
        }
        getAsyncHttpClient(context).delete(context, url, new StringEntity(reqObject.toString()), CONTENT_TYPE_APPLICATION_JSON, new Legion_AsyncHttpResponseHandler(requestCode, networkCallback, url, reqObject.toString(), false, parentActivity));
    }

    // method that uses client:
    public static void performHttpPostRequest(final Activity parentActivity, final int requestCode, String URL, JSONObject jsonObject, String header, final Legion_NetworkCallback networkCallback) throws Exception {
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request.Builder requestBuilder = new Request.Builder().url(URL).post(body);
        if (header != null) {
            requestBuilder.addHeader("sessionId", header);
        }
        Request request = requestBuilder.build();

        Log.v("POST REQUEST " + requestCode, URL + jsonObject);
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException error) {
                error.printStackTrace();
                Log.v("POST RESPONSE " + requestCode, error.getMessage() + "");
                parentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String errorResponse = error.getMessage();
                        if (error instanceof SocketTimeoutException || error instanceof UnknownHostException) {
                            errorResponse = "Your request has been timed out. Please try again later.";
                        } else if (error instanceof SSLException || error instanceof ConnectException) {
                            errorResponse = null;
                            LegionUtils.hideProgressDialog();
                            LegionUtils.showOfflineDialog(parentActivity);
                        } else{
                            errorResponse = null;
                            LegionUtils.hideProgressDialog();
                            LegionUtils.showOfflineDialog(parentActivity);
                        }
                        Log.v("FAILURE RESPONSE " + requestCode, errorResponse + "");
                        networkCallback.onFailure(requestCode, errorResponse);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseDta = response.body().string();
                final String responseHeaders = response.headers().get("sessionId");
                Log.v("POST RESPONSE " + requestCode, responseDta);
                int httpResponseCode = response.code();
                if (parentActivity != null) {
                    if (httpResponseCode == 401 && !(parentActivity instanceof LoginActivity) && !(parentActivity instanceof CreateAccountActivity) && !(parentActivity instanceof VerifyIdentityActivity)) {
                        parentActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LegionUtils.hideProgressDialog();
                                LegionUtils.doLogout(parentActivity);
                                networkCallback.onFailure(requestCode, null);
                            }
                        });
                        return;
                    } else if ((httpResponseCode == 400 || (httpResponseCode >= 500 && httpResponseCode <= 599)) && !(parentActivity instanceof CreateAccountActivity)) {
                        parentActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LegionUtils.hideProgressDialog();
                                LegionUtils.showContactUsPopup(parentActivity);
                                networkCallback.onFailure(requestCode, null);
                            }
                        });
                        return;
                    }else if ((httpResponseCode >= 500 && httpResponseCode <= 599) && (parentActivity instanceof CreateAccountActivity)) {
                        parentActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LegionUtils.hideProgressDialog();
                                LegionUtils.showContactUsPopup(parentActivity);
                                networkCallback.onFailure(requestCode, null);
                            }
                        });
                        return;
                    }
                }

                parentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        networkCallback.onSuccess(requestCode, responseDta, responseHeaders);
                    }
                });
            }
        });
    }
}

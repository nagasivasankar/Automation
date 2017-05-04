
package network;

import android.app.Activity;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.SSLException;

import co.legion.client.R;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpStatus;
import utils.LegionUtils;

/**
 * This class deals with the Response Handling of All Requests.
 */
public class Legion_AsyncHttpResponseHandler extends AsyncHttpResponseHandler {

    private final String url;
    private final String data;
    private final boolean isGetRequest;
    private final Activity parentActivity;
    private int requestCode;
    private Legion_NetworkCallback networkCallback;

    public Legion_AsyncHttpResponseHandler(int requestCode, Legion_NetworkCallback networkCallback, String url, String data, boolean isGetRequest, Activity act) {
        this.requestCode = requestCode;
        this.networkCallback = networkCallback;
        this.url = url;
        this.data = data;
        this.isGetRequest = isGetRequest;
        this.parentActivity = act;
    }

    /**
     * Fired when a request returns successfully, override to handle in your own code
     *
     * @param statusCode   the status code of the response
     * @param headers      return headers, if any
     * @param responseBody the body of the HTTP response from the server
     */
    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        String response = new String(responseBody);
        Log.v("SUCCESS RESPONSE " + requestCode, response);
        if (parentActivity != null) {
            if (statusCode == 401) {
                LegionUtils.hideProgressDialog();
                LegionUtils.doLogout(parentActivity);
                parentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        networkCallback.onFailure(requestCode, null);
                    }
                });
                return;
            } else if (statusCode == 400 || (statusCode >= 500 && statusCode <= 599)) {
                LegionUtils.hideProgressDialog();
                LegionUtils.showContactUsPopup(parentActivity);
                parentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        networkCallback.onFailure(requestCode, null);
                    }
                });
                return;
            }
        }
        networkCallback.onSuccess(requestCode, response, headers);
        //}
    }


    /**
     * Fired when a request fails to complete, override to handle in your own code
     *
     * @param statusCode   return HTTP status code
     * @param headers      return headers, if any
     * @param responseBody the response body, if any
     * @param error        the underlying cause of the failure
     */
    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        error.printStackTrace();
        String errorResponse = error.getMessage();
        Log.v("FAILURE RESPONSE " + requestCode, errorResponse + "");
        Log.v("statusCode ", statusCode + "");
        if (parentActivity != null) {
            if (statusCode == 401) {
                parentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LegionUtils.hideProgressDialog();
                        LegionUtils.doLogout(parentActivity);
                        networkCallback.onFailure(requestCode, null);
                    }
                });
                return;
            } else if (statusCode == 400 || (statusCode >= 500 && statusCode <= 599)) {
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
        if (error instanceof TimeoutException || error instanceof SocketTimeoutException || error instanceof UnknownHostException) {
            errorResponse = "Your request has been timed out. Please try again later.";
        } else if (error instanceof SSLException || error instanceof ConnectException) {
            errorResponse = null;
            LegionUtils.hideProgressDialog();
            LegionUtils.showOfflineDialog(parentActivity);
        } else if (error instanceof IOException) {
            errorResponse = "Something went wrong. Please try again later.";
        } else if (error instanceof JSONException) {
            errorResponse = "Unexpected response.\nPlease try again later.";
        } else if (error instanceof Exception) {
            errorResponse = "Something went wrong.\nPlease try again later.";
        }else{
            errorResponse = null;
            LegionUtils.hideProgressDialog();
            LegionUtils.showOfflineDialog(parentActivity);
        }
        Log.v("FAILURE RESPONSE " + requestCode, errorResponse + "");
        networkCallback.onFailure(requestCode, errorResponse);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isGetRequest) {
            Log.v("REQUEST " + requestCode, url + "?" + data);
        } else {
            Log.v("REQUEST " + requestCode, url + data);
        }
        networkCallback.onStartRequest(requestCode);
    }

    @Override
    public void onRetry(int retryNo) {
        super.onRetry(retryNo);
    }

    @Override
    public void onProgress(long bytesWritten, long totalSize) {
        super.onProgress(bytesWritten, totalSize);
    }

    @Override
    public void onCancel() {
        super.onCancel();
    }

    @Override
    public void onFinish() {
        super.onFinish();
    }
}

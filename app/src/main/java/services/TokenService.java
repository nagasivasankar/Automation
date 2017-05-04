package services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class TokenService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String newRegId = FirebaseInstanceId.getInstance().getToken();
        sendRegistrationToServer(newRegId);
        Log.w("notification", newRegId);
    }

    private void sendRegistrationToServer(String token) {
    }
}
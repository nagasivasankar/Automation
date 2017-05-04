package co.legion.client.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import base.Legion_BaseActivity;
import co.legion.client.R;
import network.Legion_NetworkCallback;
import utils.Legion_Constants;

/**
 * Created by Administrator on 1/10/2017.
 */
public class CalloutShiftOfferDetailsActivity  extends Legion_BaseActivity implements View.OnClickListener, Legion_NetworkCallback, Legion_Constants {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_shift_offer_details);
    }

    @Override
    public void onStartRequest(int requestCode) {

    }

    @Override
    public void onSuccess(int requestCode, Object response, Object headers) {

    }

    @Override
    public void onFailure(int requestCode, String reasonPhrase) {

    }

    @Override
    public void onClick(View v) {

    }
}
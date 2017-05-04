package base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.appsee.Appsee;

import co.legion.client.R;
import helpers.Legion_PrefsManager;
import utils.LegionUtils;
import utils.Legion_Constants;

/**
 * Created by Administrator on 10/3/2016.
 */
public class Legion_BaseActivity extends AppCompatActivity implements Legion_Constants {

    public Legion_PrefsManager prefsManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefsManager = new Legion_PrefsManager(this);

        //initializing Appsee
        if (LegionUtils.isFeatureEnabled(this, "feature.appsee", "")) {
            String appSeeId = getString(R.string.appsee_id);
            Appsee.start(appSeeId);
        }
    }

    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Legion_BaseActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showToastInCenter(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast t = Toast.makeText(Legion_BaseActivity.this, msg, Toast.LENGTH_LONG);
                t.setGravity(Gravity.CENTER, 0, 0);
                t.show();
            }
        });
    }
}

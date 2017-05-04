package co.legion.client.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import base.Legion_BaseActivity;
import co.legion.client.R;
import utils.LegionUtils;

/**
 * Created by Administrator on 11/25/2016.
 */

public class PrivacyPolicyActivity extends Legion_BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textViewToolbar = (TextView)findViewById(R.id.titletext);
        textViewToolbar.setText("Legion Privacy Policy");
        ImageButton navigationClose = (ImageButton)findViewById(R.id.closeSetup);

        navigationClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LegionUtils.doApplyFont(getAssets(), (LinearLayout)findViewById(R.id.parentLayout));
    }
}

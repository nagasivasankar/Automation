package co.legion.client.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import base.Legion_BaseActivity;
import co.legion.client.R;
import fragments.ComingSoonDialogFragment;
import models.ScheduleWorkerShift;
import network.Legion_NetworkCallback;
import network.Legion_RestClient;
import utils.LegionUtils;

/**
 * Created by Administrator on 06-Jan-17.
 */
public class ShiftDropRequestSubmitActivity extends Legion_BaseActivity implements View.OnClickListener, Legion_NetworkCallback {

    private ScheduleWorkerShift scheduleDetails;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_drop_request);
        TextView tvToolbarTile = (TextView) findViewById(R.id.tv_title);
        TextView btBackTv = (TextView) findViewById(R.id.btBackTv);
        TextView btDoneTv = (TextView) findViewById(R.id.btNextTv);
        TextView cmtTextTv = (TextView) findViewById(R.id.cmtTextTv);
        ImageView backImage = (ImageView) findViewById(R.id.toolbarBack);
        backImage.setImageResource(R.drawable.dismiss);
        backImage.setOnClickListener(this);
        btDoneTv.setText("Submit");
        btBackTv.setOnClickListener(this);
        btDoneTv.setOnClickListener(this);
        tvToolbarTile.setText("Submit Drop Request");
        tvToolbarTile.setFocusable(true);
        tvToolbarTile.setFocusableInTouchMode(true);
        tvToolbarTile.requestFocus();
        try {
            scheduleDetails = (ScheduleWorkerShift) getIntent().getExtras().get(Extras_Keys.WORKSHIFTS_LIST);
            cmtTextTv.setText("I have a commitment " + LegionUtils.getMonthAndYearFromDate(scheduleDetails.getShiftStartDate()) + " that conflicts with my shift, and would really appreciate being able to drop it from my schedule.");
        } catch (Exception e) {
            Log.d("Exception", "May be it's fail");
        }
        LegionUtils.doApplyFont(getAssets(), (RelativeLayout) findViewById(R.id.parentLayout));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btNextTv) {//Done Button implementation
            try {
                if (!LegionUtils.isFeatureEnabled(this, "feature.dropShift", scheduleDetails.getBusinessKey().getName())) {
                    ComingSoonDialogFragment fragment = new ComingSoonDialogFragment();
                    String message = "Have a conflict? You can request to drop the shift. Until you receive confirmation that the change is approved, you are still responsible for covering your shift.\"";
                    Bundle b = new Bundle();
                    b.putString(Extras_Keys.MESSAGE, message);
                    fragment.setArguments(b);
                    fragment.show(getSupportFragmentManager(), "");
                } else {
                    doSubmitForDrop();
                }
            } catch (Exception e) {
                Log.d("Exception", "Invalid");
            }

        } else if (id == R.id.toolbarBack) {
            Intent i = new Intent(ShiftDropRequestSubmitActivity.this, ScheduleShiftDetailsActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.putExtra(Extras_Keys.WORKSHIFTS_LIST, scheduleDetails);
            startActivity(i);
        } else if (id == R.id.btBackTv) {
            finish();
        }
    }

    public void doSubmitForDrop() {
        LegionUtils.hideKeyboard(this);
        try {
            LegionUtils.showProgressDialog(this);
            Legion_RestClient restClient = new Legion_RestClient(this, this);
            JSONObject reqObject = new JSONObject();
            restClient.performPutRequest(WebServiceRequestCodes.SWAP_SUBMIT_DROP_REQ_CODE, ServiceUrls.SWAP_REQUEST_DROP_LIST_URL + scheduleDetails.getShiftId() + "/drop", reqObject, null, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStartRequest(int requestCode) {
        LegionUtils.showProgressDialog(this);
    }

    @Override
    public void onSuccess(int requestCode, Object response, Object headers) {
        if (requestCode == WebServiceRequestCodes.SWAP_SUBMIT_DROP_REQ_CODE) {
            LegionUtils.hideProgressDialog();
            try {
                JSONObject object = new JSONObject(response.toString());
                if (object.getString("responseStatus").equalsIgnoreCase("SUCCESS")) {
                    Intent i = new Intent(ShiftDropRequestSubmitActivity.this, ShiftDropSucessActivity.class);
                    i.putExtra(Extras_Keys.WORKSHIFTS_LIST, scheduleDetails);
                    startActivity(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onFailure(int requestCode, String reasonPhrase) {
        LegionUtils.hideProgressDialog();
    }
}

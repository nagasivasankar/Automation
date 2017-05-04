package co.legion.client.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapters.ShiftRequestSubmitAdapter;
import base.Legion_BaseActivity;
import co.legion.client.R;
import models.ScheduleWorkerShift;
import network.Legion_NetworkCallback;
import network.Legion_RestClient;
import ui.ActionEditText;
import utils.LegionUtils;
import utils.Legion_Constants;

/**
 * Created by Administrator on 05-Jan-17.
 */
public class ShiftRequestSubmitActivity extends Legion_BaseActivity implements Legion_Constants, View.OnClickListener, AdapterView.OnItemClickListener, Legion_NetworkCallback {
    private ScheduleWorkerShift scheduleDetails;
    private ListView listView;
    private ArrayList<ScheduleWorkerShift> selectedShiftsList;
    private ShiftRequestSubmitAdapter adapter;
    private int sizeOfSelectedListsCount;
    private TextView countTv;
    private ActionEditText cmtTextEt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_swap_request);

        listView = (ListView) findViewById(R.id.listView);
        TextView tvToolbarTile = (TextView) findViewById(R.id.tv_title);
        countTv = (TextView) findViewById(R.id.countTv);
        TextView btBackTv = (TextView) findViewById(R.id.btBackTv);
        TextView btDoneTv = (TextView) findViewById(R.id.btNextTv);
        ImageView backImage = (ImageView) findViewById(R.id.toolbarBack);
        backImage.setImageResource(R.drawable.dismiss);

        backImage.setOnClickListener(this);
        btDoneTv.setText("Submit");
        btBackTv.setOnClickListener(this);
        btDoneTv.setOnClickListener(this);
        tvToolbarTile.setText("Submit Swap Request");
        tvToolbarTile.setFocusable(true);
        tvToolbarTile.setFocusableInTouchMode(true);
        tvToolbarTile.requestFocus();
        scheduleDetails = (ScheduleWorkerShift) getIntent().getExtras().get(Extras_Keys.WORKSHIFTS_LIST);
        selectedShiftsList = (ArrayList<ScheduleWorkerShift>) getIntent().getExtras().get(Extras_Keys.SELECTED_WORKERSHIFT_LIST);
        adapter = new ShiftRequestSubmitAdapter(this, scheduleDetails, selectedShiftsList, countTv);
        sizeOfSelectedListsCount = selectedShiftsList.size();
        countTv.setText("" + sizeOfSelectedListsCount);
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.header_swap_request_submit,listView, false);
        cmtTextEt = (ActionEditText) header.findViewById(R.id.cmtTextEt);
        LegionUtils.doApplyFont(getAssets(), (LinearLayout) header.findViewById(R.id.headerParentLayout));
        listView.addHeaderView(header, null, false);
        cmtTextEt.setText("I have a commitment "+LegionUtils.getMonthAndYearFromDate(scheduleDetails.getShiftStartDate())+" that conflicts with my shift, and would really appreciate a swap.");
        listView.setAdapter(adapter);
        LegionUtils.doApplyFont(getAssets(), (RelativeLayout) findViewById(R.id.parentLayout));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btNextTv) {//Done Button implementation
            List<ScheduleWorkerShift> shiftsList = ((ShiftRequestSubmitAdapter) adapter).getShitsList();
            if (shiftsList.size() > 0)
                doSwapReqSubmit(shiftsList);
            else {
                showToast("No shifts found.");
            }
        } else if (id == R.id.toolbarBack) {
            Intent i = new Intent(ShiftRequestSubmitActivity.this, ScheduleShiftDetailsActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.putExtra(Extras_Keys.WORKSHIFTS_LIST, scheduleDetails);
            startActivity(i);
        } else if (id == R.id.btBackTv) {
            onBack();
        }
    }

    public void doSwapReqSubmit(List<ScheduleWorkerShift> shiftsList) {
        LegionUtils.hideKeyboard(this);
        try {
            if(LegionUtils.isOnline(this)) {
                LegionUtils.showProgressDialog(this);
                Legion_RestClient restClient = new Legion_RestClient(this, this);
                JSONObject reqObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < shiftsList.size(); i++) {
                    JSONObject childJsonObject = new JSONObject();
                    JSONObject offeredObj = new JSONObject();
                    offeredObj.put("shiftId", scheduleDetails.getShiftId());
                    JSONObject requestedObj = new JSONObject();
                    requestedObj.put("shiftId", shiftsList.get(i).getShiftId());
                    childJsonObject.put("shiftOffered", offeredObj);
                    childJsonObject.put("shiftRequested", requestedObj);
                    jsonArray.put(childJsonObject);
                }
                reqObject.put("shiftSwapOffers", jsonArray);
                restClient.performPostRequest(WebServiceRequestCodes.SWAP_SUBMIT_REQ_CODE, ServiceUrls.SWAP_REQUEST_SUBMIT_LIST_URL, reqObject, null, this);
            }else{
                LegionUtils.showOfflineDialog(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    public void onBack() {
        Intent intent = new Intent();
        List<Integer> removedShitsList = ((ShiftRequestSubmitAdapter) adapter).getRemovedShitsList();
        intent.putExtra(Extras_Keys.REMOVEDSHIFTS_LIST, "" + removedShitsList);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onStartRequest(int requestCode) {
        if (requestCode == WebServiceRequestCodes.SWAP_SUBMIT_REQ_CODE) {
            LegionUtils.showProgressDialog(this);
        }
    }

    @Override
    public void onSuccess(int requestCode, Object response, Object headers) {
        if (requestCode == WebServiceRequestCodes.SWAP_SUBMIT_REQ_CODE) {
            LegionUtils.hideProgressDialog();
            String error = "";
            try {
                JSONObject object = new JSONObject(response.toString());
                error =  object.optString("error");
                if (object.getString("responseStatus").equalsIgnoreCase("SUCCESS")) {
                    Intent i = new Intent(ShiftRequestSubmitActivity.this, ShiftSwapSucessActivity.class);
                    i.putExtra(Extras_Keys.WORKSHIFTS_LIST, scheduleDetails);
                    startActivity(i);
                }else{
                    LegionUtils.showMessageDialog(this,"Oops, we are not able to submit your request to swap shifts.",R.drawable.error_transient);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                LegionUtils.showMessageDialog(this,"Something went wrong! Please try again.",R.drawable.error_transient);
            }

        }
    }

    @Override
    public void onFailure(int requestCode, String reasonPhrase) {
        LegionUtils.hideProgressDialog();
        if (reasonPhrase == null) {
            reasonPhrase = "Something went wrong.";
        }
        if (reasonPhrase.contains("Something went wrong")) {
            LegionUtils.doLogout(this);
            return;
        }
        if (requestCode == WebServiceRequestCodes.SWAP_SUBMIT_REQ_CODE) {
            LegionUtils.showDialog(ShiftRequestSubmitActivity.this, reasonPhrase, true);
        }
    }
}

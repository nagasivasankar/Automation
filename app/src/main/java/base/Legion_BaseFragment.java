package base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import co.legion.client.R;
import helpers.Legion_PrefsManager;
import utils.LegionUtils;
import utils.Legion_Constants;

public class Legion_BaseFragment extends Fragment implements View.OnClickListener, Legion_Constants {
    public Legion_PrefsManager legionPreferences;
    public ImageView backImage;
    public TextView toolbarTitleTV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        legionPreferences = new Legion_PrefsManager(getActivity());
    }

    public void showToast(final String msg) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void showToastInCenter(final String msg) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast t = Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG);
                    t.setGravity(Gravity.CENTER, 0, 0);
                    t.show();
                }
            });
        }
    }

    /*Identifying the Views*/
    public void setupToolbar(View rootView, boolean isBackRequired, String toolbarTitle) {
        if(rootView == null){
            return;
        }
        backImage = (ImageView) rootView.findViewById(R.id.toolbarBack);
        toolbarTitleTV = (TextView) rootView.findViewById(R.id.tv_title);
        backImage.setOnClickListener(this);

        if (isBackRequired) {
            backImage.setVisibility(View.VISIBLE);
        } else {
            backImage.setVisibility(View.GONE);
        }
        toolbarTitleTV.setText(toolbarTitle);
    }

    /*Implementing the clicking functionality of Views*/
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbarBack:
                LegionUtils.hideKeyboard(getActivity());
                getActivity().onBackPressed();
                break;
        }
    }

}

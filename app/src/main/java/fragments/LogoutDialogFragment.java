package fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.legion.client.R;
import utils.LegionUtils;
import utils.Legion_Constants;

/**
 * Created by Administrator on 12/30/2016.
 */
public class LogoutDialogFragment extends DialogFragment implements Legion_Constants {

    private Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_coming_soon, container, false);
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        this.dialog = getDialog();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LegionUtils.doApplyFont(getActivity().getAssets(), (LinearLayout) rootView.findViewById(R.id.parentLayout));

        TextView messageTv = (TextView)rootView.findViewById(R.id.messageTV);
        messageTv.setText(Html.fromHtml(getArguments().getString(Extras_Keys.MESSAGE)));

        rootView.findViewById(R.id.closePopup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
    }

}

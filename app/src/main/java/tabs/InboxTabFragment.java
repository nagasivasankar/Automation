package tabs;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import base.Legion_BaseFragment;
import co.legion.client.R;
import helpers.CustomTypefaceSpan;
import utils.LegionUtils;

/**
 * Created by Administrator on 11/22/2016.
 */
public class InboxTabFragment extends Legion_BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_inbox, container, false);
    }

    public InboxTabFragment() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //setupToolbar(view, false, "Inbox");
        LegionUtils.doApplyFont(getActivity().getAssets(), (LinearLayout) view.findViewById(R.id.parentLayout));

        TextView tv = (TextView) view.findViewById(R.id.comingSoonTV);

        Typeface light = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Mallory Light Regular.ttf");
        Typeface bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Mallory Medium Regular.ttf");
        SpannableStringBuilder ss = new SpannableStringBuilder("Your Inbox stores messages and notifications you receive about your weekly schedules, specific shifts and store locations. \n\n\nStay informed of the latest work news without having to go in on your day off.");
        ss.setSpan(new CustomTypefaceSpan("", light), 0, 5, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        ss.setSpan(new CustomTypefaceSpan("", bold), 5, 10, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        ss.setSpan(new CustomTypefaceSpan("", light), 11, ss.toString().length() - 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        tv.setText(ss);
    }
}

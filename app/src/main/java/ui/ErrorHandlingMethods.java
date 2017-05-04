package ui;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import co.legion.client.R;

/**
 * Created by Administrator on 11/22/2016.
 */
public class ErrorHandlingMethods {

    public void phoneError(View view, TextView textView, Context context, ImageButton imageButton) {
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.errorRedColor));
        textView.setVisibility(View.VISIBLE);
        textView.setText(R.string.phoneError);
        textView.setTextColor(ContextCompat.getColor(context, R.color.errorRedColor));
        imageButton.setVisibility(View.VISIBLE);
    }

    public void phoneSuccess(View view, TextView textView, Context context, ImageButton imageButton) {
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.underLine));
        textView.setVisibility(View.INVISIBLE);
        imageButton.setVisibility(View.GONE);
    }

    public void emailError(View view, TextView textView, Context context, ImageButton imageButton) {
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.errorRedColor));
        textView.setVisibility(View.VISIBLE);
        textView.setText(R.string.emailError);
        textView.setTextColor(ContextCompat.getColor(context, R.color.errorRedColor));
        imageButton.setVisibility(View.VISIBLE);
    }

    public void emailSuccess(View view, TextView textView, Context context, ImageButton imageButton) {
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.underLine));
        textView.setVisibility(View.INVISIBLE);
        imageButton.setVisibility(View.GONE);
    }
}

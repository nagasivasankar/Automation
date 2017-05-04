package helpers;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.text.util.Linkify;

public class Legion_AlertDialog extends AlertDialog.Builder {

    private final Activity act;

    public Legion_AlertDialog(Activity act) {
        super(act);
        this.act = act;
    }

    public void showAlertDialog(String title, String message, String positiveButtonText,
                                DialogInterface.OnClickListener positiveListener, String negativeButtonText,
                                DialogInterface.OnClickListener negetiveListener, boolean isCancellable,
                                boolean cancelOnTouchOutside) {

        Typeface book = Typeface.createFromAsset(getContext().getAssets(), "fonts/Mallory-Book.ttf");
        Typeface medium = Typeface.createFromAsset(getContext().getAssets(), "fonts/Mallory-Medium.ttf");

        if (title != null) {
            setTitle(setTypeface(medium, title));
        }
        if (message != null) {
            // Linkify the message
            Linkify.addLinks(new SpannableString(message), Linkify.ALL);
            setMessage(setTypeface(book, Html.fromHtml(message)));
        }
        if (negativeButtonText != null) {
            setNegativeButton(negativeButtonText, negetiveListener);
        }
        if (positiveButtonText != null) {
            setPositiveButton(positiveButtonText, positiveListener);
        }
        setCancelable(isCancellable);
        final Dialog dialog = create();
        dialog.setCanceledOnTouchOutside(cancelOnTouchOutside);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (!act.isFinishing() && !act.isDestroyed()) {
                    show();
                }
            } else {
                show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static SpannableString setTypeface(Typeface typeface, CharSequence string) {
        SpannableString s = new SpannableString(string);
        s.setSpan(new TypefaceSpan(typeface), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return s;
    }

    private static class TypefaceSpan extends MetricAffectingSpan {

        private final Typeface typeface;

        public TypefaceSpan(Typeface typeface) {
            this.typeface = typeface;
        }

        @Override
        public void updateDrawState(TextPaint tp) {
            tp.setTypeface(typeface);
            tp.setFlags(tp.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }

        @Override
        public void updateMeasureState(TextPaint p) {
            p.setTypeface(typeface);
            p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
    }
}

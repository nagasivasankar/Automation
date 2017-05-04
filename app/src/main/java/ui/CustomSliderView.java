package ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import co.legion.client.R;

/**
 * Created by Administrator on 1/30/2017.
 */
public class CustomSliderView extends View implements GestureDetector.OnGestureListener {

    private GestureDetector gestureDetector;
    private float left = 0, top = 0, barWidth = 0;
    private Paint barPaint = new Paint();
    private RectF rectF = new RectF();
    private String text = "04 - 16";

    public CustomSliderView(Context context) {
        super(context);
    }

    public CustomSliderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomSliderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        left = e.getX();
        barWidth = 0;
        System.out.println(new Object() {}.getClass().getEnclosingMethod().getName());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        System.out.println(new Object() {}.getClass().getEnclosingMethod().getName());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        barPaint.setStrokeWidth(2);
        barPaint.setStrokeCap(Paint.Cap.ROUND);
        barPaint.setColor(ContextCompat.getColor(getContext(), R.color.green));
        rectF.set(left, top, left + barWidth, getHeight());
        canvas.drawRoundRect(rectF, 6, 6, barPaint);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(35);
        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        int textXPos = (int) Math.abs(barWidth + left) / 2 - (bounds.width() / 2);
        int textYPos = (int) ((canvas.getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2));
        canvas.drawText(text, textXPos, textYPos, textPaint);
        //drawText(canvas, textPaint, rectF, "04 - 16");
        System.out.println(new Object() {}.getClass().getEnclosingMethod().getName());
    }

    public static void drawText(Canvas canvas, Paint paint, RectF rectF, String text) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int x = (int) rectF.centerX() / 2 - (bounds.width() / 2);
        int y = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
        canvas.drawText(text, x, y, paint);
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        System.out.println(new Object() {}.getClass().getEnclosingMethod().getName());
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent startMotionEvent, MotionEvent endMotionEvent, float distanceMovedInX, float distanceMovedInY) {
        //Log.v("XXX-startMotionEvent", startMotionEvent.getX() + ", ");
        //Log.v("XXX-endMotionEvent", endMotionEvent.getX() + ", ");
        Log.v("XXX-distanceMoved", distanceMovedInX + "");
        Log.v("XXX-barWidth", barWidth + "");
        Log.v("XXX-left", left + "");

        barWidth += (-1 * distanceMovedInX);
        if (startMotionEvent.getX() <= endMotionEvent.getX() && endMotionEvent.getX() <= getWidth()) {
            //moving towards Right
        } else if (startMotionEvent.getX() >= endMotionEvent.getX() && endMotionEvent.getX() >= 0) {
            //moving towards Left
            //if(startMotionEvent.getX() + 100 < endMotionEvent.getX()) {
            //left += (-1 * distanceMovedInX);
            // }
        } else {
            Log.v("Unknown", "Invalid move");
        }

        System.out.println(new Object() {}.getClass().getEnclosingMethod().getName());
        invalidate();
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        System.out.println(new Object() {}.getClass().getEnclosingMethod().getName());
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public void doInitialSetup() {
        System.out.println(new Object() {}.getClass().getEnclosingMethod().getName());
        gestureDetector = new GestureDetector(this.getContext(), this);
        this.invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        onTouch(this, event);
        return true;
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                left = 0;
                barWidth = 0;
                this.invalidate();
                System.out.println(new Object() {}.getClass().getEnclosingMethod().getName());
                break;
            default:
                break;
        }
        return true;
    }

}

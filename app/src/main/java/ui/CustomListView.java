package ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

/**
 * Created by Administrator on 1/4/2017.
 */
public class CustomListView extends ListView {

    public CustomListView(Context context) {
        super(context);
    }

    public CustomListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setVisibility(final int visibility) {
        if (visibility != View.GONE || getCount() != 0)
            super.setVisibility(visibility);
    }
}

package adapters;

import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.legion.client.R;
import utils.LegionUtils;

public class StatesSpinnerAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    String[] states;
    Context context;

    public StatesSpinnerAdapter(Context con, String[] arrayList) {
        mInflater = LayoutInflater.from(con);
        states = arrayList;
        context = con;
    }

    @Override
    public int getCount() {
        int count =states.length;
        return count > 0 ? count - 1 : count;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ListContent holder;
        View v = convertView;
        if (v == null) {
            v = mInflater.inflate(R.layout.simple_dropdown_item_1line, null);
            holder = new ListContent();
            holder.name = (TextView) v.findViewById(R.id.text1);
            LegionUtils.doApplyFont(context.getAssets(), (LinearLayout) v.findViewById(R.id.itemParentLayout));
            v.setTag(holder);
        } else {
            holder = (ListContent) v.getTag();
        }
        holder.name.setText("" + states[position]);
        if(holder.name.getText().toString().equalsIgnoreCase("state")){
            holder.name.setTextColor(ActivityCompat.getColor(context,R.color.light_gray_2));
        }else{
            holder.name.setTextColor(ActivityCompat.getColor(context,R.color.slider_bubble_color));
        }
        return v;
    }

    static class ListContent {
        TextView name;
    }
}

   

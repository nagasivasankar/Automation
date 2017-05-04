package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import co.legion.client.R;
import de.hdodenhof.circleimageview.CircleImageView;
import models.AssociatedWorker;
import utils.LegionUtils;

/**
 * Created by Administrator on 12/8/2016.
 */
public class ScheduleShiftDetailsListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context context;
    private ArrayList<AssociatedWorker> associatedWorkerArrayList;

    public ScheduleShiftDetailsListAdapter(Context context, ArrayList<AssociatedWorker> associatedWorkers) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.associatedWorkerArrayList = associatedWorkers;
    }

    @Override
    public int getCount() {
        return associatedWorkerArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return associatedWorkerArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.list_item_horizontal_listview, null);
            holder = new ViewHolder();
            holder.imgThumbnail = (TextView) view.findViewById(R.id.iv_picSquareIV);
            holder.imgCircleThumbnail = (CircleImageView) view.findViewById(R.id.iv_picCircleIv);
            holder.leadIv = (ImageView) view.findViewById(R.id.leadIv);
            holder.frameLayout = (FrameLayout) view.findViewById(R.id.frameLayout);
            holder.frameLayoutCircle = (FrameLayout) view.findViewById(R.id.frameLayoutCircIv);
            holder.leadIvCircular = (ImageView) view.findViewById(R.id.leadIvCircular);
            holder.tvName = (TextView) view.findViewById(R.id.tv_name);
            LegionUtils.doApplyFont(context.getAssets(), (LinearLayout) view.findViewById(R.id.parentLayout));
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        AssociatedWorker associatedWorker = associatedWorkerArrayList.get(position);
        String nickName = associatedWorker.getNickName();
        if (nickName != null &&!nickName.equalsIgnoreCase("null") && nickName.length() > 0) {
            holder.tvName.setText(associatedWorker.getNickName().trim());
        } else {
            holder.tvName.setText(associatedWorker.getFirstName());
        }
        String picUrl = associatedWorker.getPictureUrl();
        if (associatedWorker.isShiftLead()) {

        }
        if (picUrl == null || picUrl.isEmpty() || (picUrl.equalsIgnoreCase("null")) || (picUrl.equalsIgnoreCase(""))) {
            String firstName = associatedWorker.getFirstName().trim();
            String lastName = associatedWorker.getLastName().trim();
            String name = "";
            if ((firstName != null) && (firstName.length() > 0)) {
                name = firstName.substring(0, 1);
            }
            if ((lastName != null) && (lastName.length() > 0)) {
                name = name + lastName.substring(0, 1);
            }
            if (associatedWorker.isShiftLead()) {
                holder.leadIv.setImageResource(R.drawable.manager_overlay);
                holder.leadIv.setVisibility(View.VISIBLE);
            } else {
                holder.leadIv.setVisibility(View.GONE);
            }
            holder.imgCircleThumbnail.setVisibility(View.GONE);
            holder.frameLayoutCircle.setVisibility(View.GONE);
            holder.frameLayout.setVisibility(View.VISIBLE);
            holder.imgThumbnail.setVisibility(View.VISIBLE);
            holder.imgThumbnail.setText(name.toUpperCase());
            holder.imgThumbnail.setBackgroundResource(R.drawable.bg_lite_white_circle_shifts);
        } else {
            holder.imgCircleThumbnail.setVisibility(View.VISIBLE);
            holder.imgThumbnail.setVisibility(View.GONE);
            holder.frameLayoutCircle.setVisibility(View.VISIBLE);
            holder.frameLayout.setVisibility(View.GONE);
            if (associatedWorker.isShiftLead()) {
                holder.leadIvCircular.setImageResource(R.drawable.manager_overlay);
                holder.leadIvCircular.setVisibility(View.VISIBLE);
            } else {
                holder.leadIvCircular.setVisibility(View.GONE);
            }
            Glide.with(context).load(picUrl).error(R.drawable.ic_place_holder_profile).into(holder.imgCircleThumbnail);
        }


        return view;
    }

    private class ViewHolder {
        public ImageView leadIvCircular, leadIv;
        public CircleImageView imgCircleThumbnail;
        public TextView tvName;
        public FrameLayout frameLayout, frameLayoutCircle;
        TextView imgThumbnail;
    }
}
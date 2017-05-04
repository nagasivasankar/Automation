package adapters;

import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import co.legion.client.R;
import de.hdodenhof.circleimageview.CircleImageView;
import helpers.Legion_PrefsManager;
import models.ShiftOffer;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import tabs.ShiftOffersTabFragment;
import utils.LegionUtils;
import utils.Legion_Constants;

/**
 * Created by Administrator on 1/4/2017.
 */
public class ShiftOffersAdapter extends BaseAdapter implements StickyListHeadersAdapter, Legion_Constants {

    private static final int OPEN_SHIFT_ROW_TYPE = 0;
    private static final int CALLOUT_SHIFT_ROW_TYPE = 1;
    private static final int SWAP_SHIFT_ROW_TYPE = 2;

    private final ArrayList<ShiftOffer> offersList;
    private final LayoutInflater inflater;
    private final ShiftOffersTabFragment hostFragment;
    private final FragmentActivity hostActivity;
    private final Legion_PrefsManager legionPreferences;

    public ShiftOffersAdapter(ShiftOffersTabFragment hostFragment, ArrayList<ShiftOffer> offersList) {
        this.offersList = offersList;
        this.inflater = hostFragment.getActivity().getLayoutInflater();
        this.hostFragment = hostFragment;
        this.hostActivity = hostFragment.getActivity();
        this.legionPreferences = new Legion_PrefsManager(hostActivity);
    }

    @Override
    public int getCount() {
        return offersList.size();
    }

    @Override
    public Object getItem(int position) {
        return offersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (offersList.get(position).getOfferType().equalsIgnoreCase("Open")) {
            return OPEN_SHIFT_ROW_TYPE;
        } else if (offersList.get(position).getOfferType().equalsIgnoreCase("Callout")) {
            return CALLOUT_SHIFT_ROW_TYPE;
        } else if (offersList.get(position).getOfferType().equalsIgnoreCase("ShiftSwap")) {
            return SWAP_SHIFT_ROW_TYPE;
        }
        return OPEN_SHIFT_ROW_TYPE;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        OpenShiftViewHolder openShiftViewHolder = null;
        SwapShiftViewHolder swapShiftViewHolder = null;
        int rowType = getItemViewType(position);

        if (rowType == OPEN_SHIFT_ROW_TYPE) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_shift_offers_open, null);
                openShiftViewHolder = new OpenShiftViewHolder(convertView);
                convertView.setTag(openShiftViewHolder);
                LegionUtils.doApplyFont(hostActivity.getAssets(), (LinearLayout) convertView.findViewById(R.id.parentLayout));
            } else {
                openShiftViewHolder = (OpenShiftViewHolder) convertView.getTag();
            }
        } else if (rowType == CALLOUT_SHIFT_ROW_TYPE) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_shift_offers_callout, null);
                openShiftViewHolder = new OpenShiftViewHolder(convertView);
                convertView.setTag(openShiftViewHolder);
                LegionUtils.doApplyFont(hostActivity.getAssets(), (LinearLayout) convertView.findViewById(R.id.parentLayout));
            } else {
                openShiftViewHolder = (OpenShiftViewHolder) convertView.getTag();
            }
        } else {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_shift_offers_swap, null);
                swapShiftViewHolder = new SwapShiftViewHolder(convertView);
                convertView.setTag(swapShiftViewHolder);
                LegionUtils.doApplyFont(hostActivity.getAssets(), (LinearLayout) convertView.findViewById(R.id.parentLayout));
            } else {
                swapShiftViewHolder = (SwapShiftViewHolder) convertView.getTag();
            }
        }

        final ShiftOffer offer = offersList.get(position);

        if (rowType == OPEN_SHIFT_ROW_TYPE || rowType == CALLOUT_SHIFT_ROW_TYPE) {
            if (rowType == OPEN_SHIFT_ROW_TYPE) {
                openShiftViewHolder.shiftTypeNameTV.setText("OPEN SHIFT");
            } else {
                openShiftViewHolder.shiftTypeNameTV.setText("CALLOUT");
            }
            openShiftViewHolder.statTimeTV.setText(LegionUtils.convertMinsToTimeinHrs(Long.valueOf(offer.getStaffingShift().getStartMin())));
            openShiftViewHolder.endTimeTV.setText(LegionUtils.convertMinsToTimeinHrs(Long.valueOf(offer.getStaffingShift().getEndMin())));
            openShiftViewHolder.durationTV.setText(LegionUtils.convertMinsToHrsReg(offer.getStaffingShift().getRegularMinutes()));
            openShiftViewHolder.roleTV.setText(offer.getStaffingShift().getRole());
            openShiftViewHolder.nameTV.setText(offer.getStaffingShift().getBusinessKey().getName());
            openShiftViewHolder.addressTV.setText(offer.getStaffingShift().getBusinessKey().getAddress());
            int estPay = 0;
            if ((offer.getEstimatedPay() != null) && (offer.getEstimatedPay().length() > 1)) {
                estPay = (int) (Double.parseDouble(offer.getEstimatedPay()));
            } else {
                estPay = 0;
            }
            openShiftViewHolder.amountTV.setText(" $ " + estPay);

            String availabilityMatchOrNot = offer.getStaffingShift().getAvailability();
            if (availabilityMatchOrNot != null) {
                if (availabilityMatchOrNot.equalsIgnoreCase("Yes")) {
                    openShiftViewHolder.thumbImage.setImageResource(R.drawable.availability_match);//green
                } else if (availabilityMatchOrNot.equalsIgnoreCase("No")) {
                    openShiftViewHolder.thumbImage.setImageResource(R.drawable.availability_mismatch); // red down
                } else {
                    openShiftViewHolder.thumbImage.setImageResource(R.drawable.availability_unknown);// grey up
                }
            }

            int startHour = LegionUtils.convertFromMinutesToHoursInt(offer.getStaffingShift().getStartMin());
            int periodImage = 0;
            String periodColor;
            if (startHour != 0) {
                if (startHour >= 5 && startHour < 9) {
                    periodImage = R.drawable.ic_sun_early_morning; //5Am to 9Am
                    periodColor = "#fab817";
                } else if (startHour >= 9 && startHour < 13) { //9Am to 1pm
                    periodImage = R.drawable.ic_sun_morning;
                    periodColor = "#ffa000";
                } else if (startHour >= 13 && startHour < 17) { //1pm to 5pm
                    periodImage = R.drawable.ic_sun_noon;
                    periodColor = "#f57c00";
                } else if (startHour >= 17 && startHour < 21) {  //5pm to 9pm
                    periodImage = R.drawable.ic_sun_evening;
                    periodColor = "#e75c15";
                } else {
                    periodImage = R.drawable.ic_moon; //9Pm12Am
                    periodColor = "#9a1010";
                }
                openShiftViewHolder.lineView.setBackgroundColor(Color.parseColor(periodColor));
                openShiftViewHolder.roleTV.setTextColor(Color.parseColor(periodColor));
                openShiftViewHolder.statusImageView.setImageResource(periodImage);
                openShiftViewHolder.dayNameTV.setText(getDay(offer.getStaffingShift().getShiftStartDate().replace("T", " ").replace("Z", " ")));
                openShiftViewHolder.dateTV.setText(getDate(offer.getStaffingShift().getShiftStartDate().replace("T", " ").replace("Z", " ")));

                if (offer.getOfferStatus().equalsIgnoreCase("Proposed") || !offer.isSeen()) {
                    openShiftViewHolder.shiftExpireTV.setText(getExpiryTime(offer.getExpiryTimestamp(), offer));
                } else {
                    String text = offer.getOfferStatus();
                    if (offer.getOfferStatus().equalsIgnoreCase("Proposed")) {
                        text = "Offer Proposed";
                    } else if (offer.getOfferStatus().equalsIgnoreCase("Claimed")) {
                        text = "Offer Accepted";
                    } else if (offer.getOfferStatus().equalsIgnoreCase("Accepted")) {
                        text = "Offer Approved";
                    } else if (offer.getOfferStatus().equalsIgnoreCase("Declined")) {
                        text = "Offer Rejected";
                    }
                    openShiftViewHolder.shiftExpireTV.setText(text);
                }
            }
        } else if (rowType == SWAP_SHIFT_ROW_TYPE) {
            swapShiftViewHolder.statTimeTV.setText(LegionUtils.getTimeFromDate(offer.getShiftOffered().getShiftStartDate()));
            swapShiftViewHolder.endTimeTV.setText(LegionUtils.getTimeFromDate(offer.getShiftOffered().getShiftEndDate()));
            swapShiftViewHolder.roleTV.setText(offer.getShiftOffered().getRole());
            swapShiftViewHolder.shiftersNameTv.setText(offer.getShiftOffered().getWorkerKey().getFirstName() + " wants to trade");
          //  Glide.with(hostActivity).load(offer.getShiftOffered().getWorkerKey().getPictureUrl()).into(swapShiftViewHolder.shiftersIv2);
            String picUrl = offer.getShiftOffered().getWorkerKey().getPictureUrl();
            if (picUrl == null || picUrl.isEmpty() || (picUrl.equalsIgnoreCase("null")) || (picUrl.equalsIgnoreCase(""))) {
                String firstName = offer.getShiftOffered().getWorkerKey().getFirstName().trim();
                String lastName = offer.getShiftOffered().getWorkerKey().getLastName().trim();
                String name = "";
                if ((firstName != null) && (firstName.length() > 0)) {
                    name = firstName.substring(0, 1);
                }
                if ((lastName != null) && (lastName.length() > 0)) {
                    name = name + lastName.substring(0, 1);
                }
                swapShiftViewHolder.shiftersIv2.setVisibility(View.GONE);
                swapShiftViewHolder.profileTv2.setVisibility(View.VISIBLE);
                swapShiftViewHolder.profileTv2.setText(name.toUpperCase());
            } else {
                swapShiftViewHolder.profileTv2.setVisibility(View.GONE);
                swapShiftViewHolder.shiftersIv2.setVisibility(View.VISIBLE);
                Glide.with(hostActivity).load(picUrl).into(swapShiftViewHolder.shiftersIv2);
            }

           // setImageOrText(offer.getShiftRequested(), swapShiftViewHolder.profileTv2, swapShiftViewHolder.shiftersIv2);
            String availabilityMatchOrNot = offer.getShiftOffered().getAvailability();
            String regularMins = LegionUtils.convertMinsToHrsReg(offer.getShiftOffered().getRegularMinutes());
            swapShiftViewHolder.durationTV.setText(regularMins);
            swapShiftViewHolder.addressTV.setText(LegionUtils.getUpdatedAddress(offer.getShiftOffered().getBusinessKey().getAddress()));
            if (offer.getOfferStatus().equalsIgnoreCase("Proposed") || !offer.isSeen()) {
                swapShiftViewHolder.expiryTV.setText(getExpiryTime(offer.getExpiryTimestamp(), offer));
            } else {
                String text = offer.getOfferStatus();
                if (offer.getOfferStatus().equalsIgnoreCase("Proposed")) {
                    text = "Offer Proposed";
                } else if (offer.getOfferStatus().equalsIgnoreCase("Claimed")) {
                    text = "Offer Accepted";
                } else if (offer.getOfferStatus().equalsIgnoreCase("Accepted")) {
                    text = "Offer Approved";
                } else if (offer.getOfferStatus().equalsIgnoreCase("Declined")) {
                    text = "Offer Rejected";
                }
                swapShiftViewHolder.expiryTV.setText(text);
            }

            if (availabilityMatchOrNot != null) {
                if (availabilityMatchOrNot.equalsIgnoreCase("Yes")) {
                    swapShiftViewHolder.addressTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.availability_match, 0);//green
                } else if (availabilityMatchOrNot.equalsIgnoreCase("No")) {
                    swapShiftViewHolder.addressTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.availability_mismatch, 0); // red down
                } else {
                    swapShiftViewHolder.addressTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.availability_unknown, 0);// grey up
                }
                swapShiftViewHolder.addressTV.setCompoundDrawablePadding(10);
            }
            String label;
            if ((regularMins.contains("1."))) {
                label = "Hr";
            } else {
                label = "Hrs";
            }
            swapShiftViewHolder.hrsTV.setText(label);
            int startHour = LegionUtils.convertFromMinutesToHoursInt(offer.getShiftOffered().getStartMin());
            int periodImage = 0;
            String periodColor;
            if (startHour != 0) {
                if (startHour >= 5 && startHour < 9) {
                    periodImage = R.drawable.ic_sun_early_morning; //5Am9Am
                    periodColor = "#fab817";
                } else if (startHour >= 9 && startHour < 13) { //9Am to 1pm
                    periodImage = R.drawable.ic_sun_morning;
                    periodColor = "#ffa000";
                } else if (startHour >= 13 && startHour < 17) { //1pm to 5pm
                    periodImage = R.drawable.ic_sun_noon;
                    periodColor = "#f57c00";
                } else if (startHour >= 17 && startHour < 21) {  //5pm to 9pm
                    periodImage = R.drawable.ic_sun_evening;
                    periodColor = "#e75c15";
                } else {
                    periodImage = R.drawable.ic_moon; //9Pm12Am
                    periodColor = "#9a1010";
                }
                swapShiftViewHolder.lineView.setBackgroundColor(Color.parseColor(periodColor));
                swapShiftViewHolder.statusImageView.setImageResource(periodImage);
                swapShiftViewHolder.roleTV.setTextColor(Color.parseColor(periodColor));
            }
            try {
                String finalDate = LegionUtils.getDatefromServerDate(offer.getShiftOffered().getShiftStartDate());
                String month = "";
                try {
                    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = fmt.parse(offer.getShiftRequested().getShiftStartDate().split("T")[0]);
                    SimpleDateFormat fmtOut = new SimpleDateFormat("MMM");
                    month = fmtOut.format(date).toUpperCase();
                } catch (Exception e) {
                }

        /*if(checkCurrentWeekOrNot){
            if(LegionUtils.getCurrentDate().equals(finalDate)){
                swapViewHolder.dayNameTV.setTextColor(ActivityCompat.getColor(hostActivity, R.color.white));
                swapViewHolder.dayViewLL.setBackgroundResource(R.drawable.bg_blue);
            }else{
                swapViewHolder.dayNameTV.setTextColor(ActivityCompat.getColor(hostActivity, R.color.light_black));
                swapViewHolder.dayViewLL.setBackgroundResource(0);
            }
        }else{*/
                swapShiftViewHolder.dayNameTV.setTextColor(ActivityCompat.getColor(hostActivity, R.color.light_black));
                swapShiftViewHolder.dayViewLL.setBackgroundResource(R.drawable.bg_white_corner);
                //}
                swapShiftViewHolder.dayNameTV.setText(Html.fromHtml(LegionUtils.getDayName(Integer.parseInt(offer.getShiftOffered().getDayOfTheWeek())) + "<br/>" + "<big>" + finalDate + "</big><br/>" + month));
            } catch (Exception e) {
                e.printStackTrace();
            }

            // current User Details
            swapShiftViewHolder.statTimeTV1.setText(LegionUtils.getTimeFromDate(offer.getShiftRequested().getShiftStartDate()));
            swapShiftViewHolder.endTimeTV1.setText(LegionUtils.getTimeFromDate(offer.getShiftRequested().getShiftEndDate()));
            swapShiftViewHolder.roleTV1.setText(offer.getShiftRequested().getRole());
           // Glide.with(hostActivity).load(offer.getShiftRequested().getWorkerKey().getPictureUrl()).into(swapShiftViewHolder.shiftersIv1);
            String picUrl2 = offer.getShiftRequested().getWorkerKey().getPictureUrl();
            if (picUrl2 == null || picUrl2.isEmpty() || (picUrl2.equalsIgnoreCase("null")) || (picUrl2.equalsIgnoreCase(""))) {
                String firstName = offer.getShiftRequested().getWorkerKey().getFirstName().trim();
                String lastName = offer.getShiftRequested().getWorkerKey().getLastName().trim();
                String name = "";
                if ((firstName != null) && (firstName.length() > 0)) {
                    name = firstName.substring(0, 1);
                }
                if ((lastName != null) && (lastName.length() > 0)) {
                    name = name + lastName.substring(0, 1);
                }
                swapShiftViewHolder.shiftersIv1.setVisibility(View.GONE);
                swapShiftViewHolder.profileTv1.setVisibility(View.VISIBLE);
                swapShiftViewHolder.profileTv1.setText(name.toUpperCase());
            } else {
                swapShiftViewHolder.profileTv1.setVisibility(View.GONE);
                swapShiftViewHolder.shiftersIv1.setVisibility(View.VISIBLE);
                Glide.with(hostActivity).load(picUrl2).into(swapShiftViewHolder.shiftersIv1);
            }


           // setImageOrText(offer.getShiftRequested(), swapShiftViewHolder.profileTv1, swapShiftViewHolder.shiftersIv1);
            String availabilityMatchOrNot1 = offer.getShiftRequested().getAvailability();
            String regularMins1 = LegionUtils.convertMinsToHrsReg(offer.getShiftRequested().getRegularMinutes());
            swapShiftViewHolder.durationTV1.setText(regularMins1);
            swapShiftViewHolder.addressTV1.setText(LegionUtils.getUpdatedAddress(offer.getShiftRequested().getBusinessKey().getAddress()));
            if (availabilityMatchOrNot1 != null) {
                if (availabilityMatchOrNot1.equalsIgnoreCase("Yes")) {
                    swapShiftViewHolder.addressTV1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.availability_match, 0);//green
                } else if (availabilityMatchOrNot1.equalsIgnoreCase("No")) {
                    swapShiftViewHolder.addressTV1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.availability_mismatch, 0); // red down
                } else {
                    swapShiftViewHolder.addressTV1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.availability_unknown, 0);// grey up
                }
                swapShiftViewHolder.addressTV1.setCompoundDrawablePadding(10);
            }
            String label1;
            if ((regularMins1.contains("1."))) {
                label1 = "Hr";
            } else {
                label1 = "Hrs";
            }
            swapShiftViewHolder.hrsTV1.setText(label1);
            int startHour1 = LegionUtils.convertFromMinutesToHoursInt(offer.getShiftRequested().getStartMin());
            int periodImage1 = 0;
            String periodColor1;
            if (startHour != 0) {
                if (startHour1 >= 5 && startHour1 < 9) {
                    periodImage1 = R.drawable.ic_sun_early_morning; //5Am9Am
                    periodColor1 = "#fab817";
                } else if (startHour1 >= 9 && startHour1 < 13) { //9Am to 1pm
                    periodImage1 = R.drawable.ic_sun_morning;
                    periodColor1 = "#ffa000";
                } else if (startHour1 >= 13 && startHour1 < 17) { //1pm to 5pm
                    periodImage1 = R.drawable.ic_sun_noon;
                    periodColor1 = "#f57c00";
                } else if (startHour1 >= 17 && startHour1 < 21) {  //5pm to 9pm
                    periodImage1 = R.drawable.ic_sun_evening;
                    periodColor1 = "#e75c15";
                } else {
                    periodImage1 = R.drawable.ic_moon; //9Pm12Am
                    periodColor1 = "#9a1010";
                }
                swapShiftViewHolder.lineView1.setBackgroundColor(Color.parseColor(periodColor1));
                swapShiftViewHolder.statusImageView1.setImageResource(periodImage1);
                swapShiftViewHolder.roleTV1.setTextColor(Color.parseColor(periodColor1));
            }
            try {
                String finalDate = LegionUtils.getDatefromServerDate(offer.getShiftRequested().getShiftStartDate());
                String month = "";
                try {
                    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = fmt.parse(offer.getShiftRequested().getShiftStartDate().split("T")[0]);
                    SimpleDateFormat fmtOut = new SimpleDateFormat("MMM");
                    month = fmtOut.format(date).toUpperCase();
                } catch (Exception e) {
                }
        /*if(checkCurrentWeekOrNot){
            if(LegionUtils.getCurrentDate().equals(finalDate)){
                swapViewHolder.dayNameTV1.setTextColor(ActivityCompat.getColor(hostActivity, R.color.white));
                swapViewHolder.dayViewLL1.setBackgroundResource(R.drawable.bg_blue);
            }else{
                swapViewHolder.dayNameTV1.setTextColor(ActivityCompat.getColor(hostActivity, R.color.light_black));
                swapViewHolder.dayViewLL1.setBackgroundResource(R.drawable.bg_white_corner);
            }
        }else{*/
                swapShiftViewHolder.dayNameTV1.setTextColor(ActivityCompat.getColor(hostActivity, R.color.light_black));
                swapShiftViewHolder.dayViewLL1.setBackgroundResource(0);
                //}
                swapShiftViewHolder.dayNameTV1.setText(Html.fromHtml(LegionUtils.getDayName(Integer.parseInt(offer.getShiftRequested().getDayOfTheWeek())) + "<br/>" + "<big>" + finalDate + "</big><br/>" + month));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null || !(convertView.getTag() instanceof HeaderViewHolder)) {
            convertView = inflater.inflate(R.layout.header_shift_offers_listview, parent, false);
            holder = new HeaderViewHolder(convertView);
            LegionUtils.doApplyFont(hostActivity.getAssets(), (LinearLayout) convertView.findViewById(R.id.parentLayout));
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        int count = 0;
        for (ShiftOffer offer : offersList) {
            if (offer.getOfferType().equalsIgnoreCase(offersList.get(position).getOfferType())) {
                ++count;
            }
        }
        holder.headerOffersCountTV.setText(count == 1 ? count + " OFFER" : count + " OFFERS");
        if (offersList.get(position).getOfferType().equalsIgnoreCase("ShiftSwap")) {
            holder.headerDateRangeTV.setText(getShiftWeekDateHeadernew(offersList.get(position).getShiftOffered().getShiftStartDate()));
        } else {
            holder.headerDateRangeTV.setText(getShiftWeekDateHeadernew(offersList.get(position).getStaffingShift().getShiftStartDate()));
            //  holder.headerDateRangeTV.setText(offersList.get(position).getStaffingShift().getShiftStartDate());
        }
        return convertView;
    }

    private String getShiftWeekDateHeader(String shiftStartDate) {
        try {
            SimpleDateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = inputFormatter.parse(shiftStartDate.split("T")[0]);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(date1.getTime());
            cal.add(Calendar.DATE, (cal.get(Calendar.DAY_OF_WEEK) - 2) * -1);

            SimpleDateFormat outputFormatter = new SimpleDateFormat("ddMMyy");
            String weekStartDate = outputFormatter.format(cal.getTime()).toUpperCase();
            int startMonthNumber = cal.get(Calendar.MONTH);
            cal.add(Calendar.DATE, 6);
            int endMonthNumber = cal.get(Calendar.MONTH);
            if (startMonthNumber == endMonthNumber) {
                return weekStartDate;
            } else {
                return weekStartDate;
            }
        } catch (Exception e) {
            return "";
        }
    }

    private String getShiftWeekDateHeadernew(String shiftStartDate) {
        try {
            SimpleDateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = inputFormatter.parse(shiftStartDate.split("T")[0]);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(date1.getTime());
            cal.add(Calendar.DATE, (cal.get(Calendar.DAY_OF_WEEK) - 2) * -1);

            SimpleDateFormat outputFormatter = new SimpleDateFormat("MMM d");
            String weekStartDate = outputFormatter.format(cal.getTime()).toUpperCase();
            int startMonthNumber = cal.get(Calendar.MONTH);
            cal.add(Calendar.DATE, 6);
            int endMonthNumber = cal.get(Calendar.MONTH);
            if (startMonthNumber == endMonthNumber) {
                return weekStartDate + " - " + cal.get(Calendar.DATE);
            } else {
                return weekStartDate + " - " + outputFormatter.format(cal.getTime()).toUpperCase();
            }
        } catch (Exception e) {
            return "";
        }
    }


    @Override
    public long getHeaderId(int position) {
       /* if (offersList.get(position).getOfferType().equalsIgnoreCase("Open")) {
            return 1;
        } else if (offersList.get(position).getOfferType().equalsIgnoreCase("ShiftSwap")) {
            return 2;
        } else {
            return 3;
        }*/
        if (getItemViewType(position) == SWAP_SHIFT_ROW_TYPE) {
            return Long.parseLong(getShiftWeekDateHeader(offersList.get(position).getShiftOffered().getShiftStartDate()));
        } else if (getItemViewType(position) == OPEN_SHIFT_ROW_TYPE) {
            return Long.parseLong(getShiftWeekDateHeader(offersList.get(position).getStaffingShift().getShiftStartDate()));
        } else {//callout
            return Long.parseLong(getShiftWeekDateHeader(offersList.get(position).getStaffingShift().getShiftStartDate()));
        }
    }

    private static class HeaderViewHolder {
        TextView headerDateRangeTV, headerOffersCountTV;

        public HeaderViewHolder(View convertView) {
            headerDateRangeTV = (TextView) convertView.findViewById(R.id.header_offers_range_date_tv);
            headerOffersCountTV = (TextView) convertView.findViewById(R.id.header_offers_count_tv);
        }
    }

    private static class OpenShiftViewHolder {
        View lineView;
        ImageView statusImageView, thumbImage, shiftTypeIV;
        TextView dayNameTV, amountTV, dateTV, statTimeTV, durationTV, endTimeTV, roleTV, nameTV, addressTV, shiftExpireTV, shiftTypeNameTV;

        public OpenShiftViewHolder(View convertView) {
            addressTV = (TextView) convertView.findViewById(R.id.addressTV);
            dayNameTV = (TextView) convertView.findViewById(R.id.dayNameTV);
            amountTV = (TextView) convertView.findViewById(R.id.amountTV);
            dateTV = (TextView) convertView.findViewById(R.id.dateTV);
            statTimeTV = (TextView) convertView.findViewById(R.id.statTimeTV);
            durationTV = (TextView) convertView.findViewById(R.id.durationTV);
            endTimeTV = (TextView) convertView.findViewById(R.id.endTimeTV);
            roleTV = (TextView) convertView.findViewById(R.id.roleTV);
            nameTV = (TextView) convertView.findViewById(R.id.nameTV);
            shiftExpireTV = (TextView) convertView.findViewById(R.id.shiftExpireTV);
            shiftTypeNameTV = (TextView) convertView.findViewById(R.id.shiftTypeNameTV);
            lineView = convertView.findViewById(R.id.lineView);

            statusImageView = (ImageView) convertView.findViewById(R.id.statusImageView);
            thumbImage = (ImageView) convertView.findViewById(R.id.thumbImage);
            shiftTypeIV = (ImageView) convertView.findViewById(R.id.shiftTypeIV);
        }
    }

    private String getExpiryTime(long expiryTimestamp, ShiftOffer offer) {
        Calendar now = Calendar.getInstance();

        Calendar expiryCal = Calendar.getInstance();
        expiryCal.setTimeInMillis(expiryTimestamp);

        long diffDays = TimeUnit.MILLISECONDS.toDays(expiryCal.getTimeInMillis() - now.getTimeInMillis());
        if (expiryCal.getTimeInMillis() < now.getTimeInMillis()) {
            if (offer.getOfferStatus().equalsIgnoreCase("Proposed")) {
                return "Offer Proposed";
            } else if (offer.getOfferStatus().equalsIgnoreCase("Claimed")) {
                return "Offer Accepted";
            } else if (offer.getOfferStatus().equalsIgnoreCase("Accepted")) {
                return "Offer Approved";
            } else if (offer.getOfferStatus().equalsIgnoreCase("Declined")) {
                return "Offer Rejected";
            }
            return offer.getOfferStatus();
        } else {
            if (diffDays < 1) {
                long diffHrs = TimeUnit.MILLISECONDS.toHours(expiryCal.getTimeInMillis() - now.getTimeInMillis());
                if (diffHrs < 1) {
                    long diffMins = TimeUnit.MILLISECONDS.toMinutes(expiryCal.getTimeInMillis() - now.getTimeInMillis());
                    if (diffMins < 1) {
                        return "Expires in " + TimeUnit.MILLISECONDS.toSeconds(expiryCal.getTimeInMillis() - now.getTimeInMillis()) + " seconds";
                    } else if (diffMins == 1) {
                        return "Expires in " + diffMins + " minute";
                    }
                    return "Expires in " + diffMins + " minutes";
                } else if (diffHrs == 1) {
                    return "Expires in " + diffHrs + " hr";
                }
                return "Expires in " + diffHrs + " hrs";
            } else if (diffDays >= 365) {
                int diffYears = (int) diffDays / 365;
                if (diffYears == 1) {
                    return "Expires in " + diffYears + " year";
                }
                return "Expires in " + diffYears + " years";
            } else if (diffDays >= 30) {
                int diffMonths = (int) diffDays / 30;
                if (diffMonths == 1) {
                    return "Expires in " + diffMonths + " month";
                }
                return "Expires in " + diffMonths + " months";
            } else {
                if (diffDays == 1) {
                    return "Expires in " + diffDays + " day";
                }
                return "Expires in " + diffDays + " days";
            }
        }
    }

    private String getDay(String dateFromServer) {
        String inputPattern = LegionUtils.DATE_FORMAT;
        String outputPattern = "EEE";

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(dateFromServer);
            date = getConvertedDate(date, "UTC");
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str.toUpperCase();
    }

    private String getDate(String dateFromServer) {
        String inputPattern = LegionUtils.DATE_FORMAT;
        String outputPattern = "d";

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(dateFromServer);
            date = getConvertedDate(date, "UTC");
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static Date getConvertedDate(Date date, String newTimeZone) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(LegionUtils.DATE_FORMAT);
            df.setTimeZone(TimeZone.getTimeZone(newTimeZone));

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            String newdt = df.format(calendar.getTime());
            try {
                date = df.parse(newdt);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }


    private class SwapShiftViewHolder {
        private TextView expiryTV;
        public ImageView statusImageView;
        public TextView dayNameTV;
        public TextView durationTV;
        public TextView dateTv;
        public TextView nameTV;
        public TextView addressTV;
        public TextView roleTV;
        public TextView statTimeTV;
        public TextView endTimeTV;
        public TextView hrsTV;
        public View lineView;
        public CircleImageView shiftersIv;
        public TextView shiftersNameTv;
        CircleImageView shiftersIv2;
        TextView profileTv2;

        //current user
        public ImageView statusImageView1;
        public TextView dayNameTV1;
        public TextView durationTV1;
        public TextView addressTV1;
        public TextView roleTV1;
        public TextView statTimeTV1;
        public TextView endTimeTV1;
        public TextView hrsTV1;
        public View lineView1;
        CircleImageView shiftersIv1;
         TextView profileTv1;
        public ImageView closeShiftIv;
        public LinearLayout dayViewLL1;
        public LinearLayout dayViewLL;

        public SwapShiftViewHolder(View convertView) {
            expiryTV = (TextView) convertView.findViewById(R.id.expiryTV);
            dayViewLL1 = (LinearLayout) convertView.findViewById(R.id.subDayLayout1);
            dayViewLL = (LinearLayout) convertView.findViewById(R.id.subDayLayout);
            dayNameTV = (TextView) convertView.findViewById(R.id.dayNameTV);
            roleTV = (TextView) convertView.findViewById(R.id.roleTv);
            addressTV = (TextView) convertView.findViewById(R.id.addressTV);
            statTimeTV = (TextView) convertView.findViewById(R.id.statTimeTV);
            endTimeTV = (TextView) convertView.findViewById(R.id.endTimeTV);
            durationTV = (TextView) convertView.findViewById(R.id.durationTV);
            statusImageView = (ImageView) convertView.findViewById(R.id.statusImageView);
            shiftersIv = (CircleImageView) convertView.findViewById(R.id.shiftersIv);
            lineView = (View) convertView.findViewById(R.id.lineView);
            hrsTV = (TextView) convertView.findViewById(R.id.hrsTV);
            shiftersNameTv = (TextView) convertView.findViewById(R.id.nameTv1);
            shiftersIv2 = (CircleImageView) convertView.findViewById(R.id.shiftersIv2);
            profileTv2 = (TextView) convertView.findViewById(R.id.profileTv2);
            dayNameTV1 = (TextView) convertView.findViewById(R.id.dayNameTV1);
            roleTV1 = (TextView) convertView.findViewById(R.id.roleTv1);
            addressTV1 = (TextView) convertView.findViewById(R.id.addressTV1);
            statTimeTV1 = (TextView) convertView.findViewById(R.id.statTimeTV1);
            endTimeTV1 = (TextView) convertView.findViewById(R.id.endTimeTV1);
            durationTV1 = (TextView) convertView.findViewById(R.id.durationTV1);
            statusImageView1 = (ImageView) convertView.findViewById(R.id.statusImageView1);
            shiftersIv1 = (CircleImageView) convertView.findViewById(R.id.shiftersIv1);
            profileTv1 = (TextView) convertView.findViewById(R.id.profileTv1);
            lineView1 = (View) convertView.findViewById(R.id.lineView1);
            hrsTV1 = (TextView) convertView.findViewById(R.id.hrsTV1);
            shiftersIv1 = (CircleImageView) convertView.findViewById(R.id.shiftersIv1);
            closeShiftIv = (ImageView) convertView.findViewById(R.id.closeShiftIv);
            /*dayNameTV1.setMinWidth((legionPreferences.getInt(Prefs_Keys.MIN_WIDTH) - 20));
            dayNameTV1.setTextSize(legionPreferences.getInt(Prefs_Keys.SIZE));
            dayNameTV.setMinWidth((legionPreferences.getInt(Prefs_Keys.MIN_WIDTH) - 20));
            dayNameTV.setTextSize(legionPreferences.getInt(Prefs_Keys.SIZE));*/
        }
    }
}

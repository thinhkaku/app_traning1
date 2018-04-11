package aero.pilotlog.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.entities.LimitRules;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.utilities.DateTimeUtils;
import aero.pilotlog.utilities.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by phuc.dd on 8/9/2017.
 */

public class FlightLimitAdapter extends MCCPilotLogBaseAdapter {
    private List<LimitRules> mList = new ArrayList<>();
    private final LayoutInflater mLayoutInflater;
    private Context mContext;
    private DatabaseManager mDatabaseManager;
    private String strLogTimeDecimal;

    public FlightLimitAdapter(Context pContext, List<LimitRules> pList) {
        mContext = pContext;
        this.mLayoutInflater = (LayoutInflater) pContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mList = pList;
        mDatabaseManager = DatabaseManager.getInstance(mContext);
        String logDecimal = mDatabaseManager.getSetting(MCCPilotLogConst.SETTING_CODE_IS_LOG_DECIMAL).getData();
        if (logDecimal.equals("2") || logDecimal.equals("1")) strLogTimeDecimal = "1";
        else strLogTimeDecimal = "0";
    }

    @Override
    protected int getCountList() {
        return getCount();
    }

    @Override
    protected String getStringItem(int pPosition) {
        return "";
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    /* @Override
     public boolean isEnabled(int position) {
         return false;
     }*/
    @Override
    public LimitRules getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mList.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.row_item_limit, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
            LimitRules limitRule = getItem(position);
            if (limitRule != null) {
                viewHolder.tvHours.setText(Utils.getHourTotals(strLogTimeDecimal, limitRule.getSumTotalTime()));
                if (limitRule.getSumTotalTime() < limitRule.getLMinutes()) {
                    int pointCaution = -9999;
                    switch (limitRule.getLZone()) {
                        case 0:
                            break;
                        case 1:
                            pointCaution = limitRule.getLMinutes() - 60;
                            break;
                        case 2:
                            pointCaution = limitRule.getLMinutes() - 120;
                            break;
                        case 3:
                            pointCaution = limitRule.getLMinutes() - 300;
                            break;
                        case 4:
                            pointCaution = limitRule.getLMinutes() - 600;
                            break;
                        case 5:
                            pointCaution = limitRule.getLMinutes() - 1500;
                            break;
                        case 6:
                            pointCaution = limitRule.getLMinutes() - limitRule.getLMinutes() / 100 * 5;
                            break;
                        case 7:
                            pointCaution = limitRule.getLMinutes() - limitRule.getLMinutes() / 10;
                            break;
                    }
                    if (pointCaution != -9999 && limitRule.getSumTotalTime() >= pointCaution) {
                        //caution
                        viewHolder.tvLimitMessage.setText("CAUTION");
                        viewHolder.tvLimitMessage.setBackgroundColor(mContext.getResources().getColor(R.color.mcc_amber));
                        viewHolder.tvHours.setTypeface(viewHolder.tvHours.getTypeface(), Typeface.BOLD);
                        viewHolder.tvHours.setTextColor(mContext.getResources().getColor(R.color.mcc_amber));
                    } else {
                        //valid
                        viewHolder.tvLimitMessage.setText("VALID");
                        viewHolder.tvLimitMessage.setBackgroundColor(mContext.getResources().getColor(R.color.mcc_cyan));
                    }
                } else {
                    //exceeded
                    viewHolder.tvLimitMessage.setText("EXCEEDED");
                    viewHolder.tvLimitMessage.setBackgroundColor(mContext.getResources().getColor(
                            limitRule.getLType() != 2 ? R.color.color_red : R.color.mcc_green_credit));
                    viewHolder.tvHours.setTypeface(viewHolder.tvHours.getTypeface(), Typeface.BOLD);
                    viewHolder.tvHours.setTextColor(mContext.getResources().getColor(
                            limitRule.getLType() != 2 ? R.color.color_red : R.color.mcc_green_credit));
                }
                String description = "";
                if (limitRule.getLPeriodCode() != 1008) {
                    if (limitRule.getlPeriodLong() != null) {
                        description = String.format("%1$s flight hours in %2$s",
                                Utils.getHourTotals(strLogTimeDecimal, limitRule.getLMinutes()), limitRule.getlPeriodLong().trim());

                    }
                } else {
                    if (limitRule.getlPeriodLong() != null) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Date dateFrom = new Date();
                        Date dateTo = new Date();
                        try {
                            dateFrom = format.parse(limitRule.getLFrom());
                            dateTo = format.parse(limitRule.getLTo());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        description = String.format("%1$s flight hours from %2$s until %3$s",
                                Utils.getHourTotals(strLogTimeDecimal, limitRule.getLMinutes()),
                                DateTimeUtils.formatDateToString(dateFrom), DateTimeUtils.formatDateToString(dateTo));
                    }
                }
                if (limitRule.getLType() != 2 && !TextUtils.isEmpty(description)) {
                    description = "Max " + description;
                }
                viewHolder.tvLimitDescription.setText(description);
            }
        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }


        return convertView;
    }


    public void refreshAdapter(List<LimitRules> pList) {
        this.mList = pList;
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        @Bind(R.id.tv_limit_message)
        TextView tvLimitMessage;
        @Bind(R.id.tv_hours)
        TextView tvHours;
        @Bind(R.id.tv_limit_description)
        TextView tvLimitDescription;

        public ViewHolder(View pView) {
            ButterKnife.bind(this, pView);
        }
    }
}
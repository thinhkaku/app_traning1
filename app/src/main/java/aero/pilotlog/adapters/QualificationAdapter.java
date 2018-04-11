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
import aero.pilotlog.databases.entities.Qualification;
import aero.pilotlog.databases.manager.DatabaseManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by phuc.dd on 8/15/2017.
 */

public class QualificationAdapter extends MCCPilotLogBaseAdapter {
    private List<Qualification> mList = new ArrayList<>();
    private final LayoutInflater mLayoutInflater;
    private Context mContext;
    private DatabaseManager mDatabaseManager;

    public enum QualificationType {
        CERTIFICATE,
        PROFICIENCY
    }

    public QualificationType qualificationType = QualificationType.CERTIFICATE;

    public QualificationAdapter(Context pContext, List<Qualification> pList) {
        mContext = pContext;
        this.mLayoutInflater = (LayoutInflater) pContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mList = pList;
        mDatabaseManager = DatabaseManager.getInstance(mContext);
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
    public Qualification getItem(int position) {
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
            convertView = mLayoutInflater.inflate(R.layout.row_item_qualification, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
            Qualification qualification = getItem(position);
            if (qualification != null) {
                if (qualificationType == QualificationType.CERTIFICATE) {
                    viewHolder.tvDescription3.setVisibility(View.VISIBLE);
                    viewHolder.tvDateValid.setText(qualification.getDateValid());
                    if (!TextUtils.isEmpty(qualification.getRefModel())) {
                        viewHolder.tvDescription1.setText(qualification.getZQualification().getQTypeLong() + " " + getTextRefModel(qualification.getRefModel()));
                    } else {
                        viewHolder.tvDescription1.setText(qualification.getZQualification().getQTypeLong());
                    }
                    viewHolder.tvDescription2.setText(getTextValidity(qualification.getValidity()));
                    viewHolder.tvDescription3.setText(qualification.getNotifyComment());
                    switch (qualification.getStatus()) {
                        case EXPIRED:
                            viewHolder.tvQualificationMessage.setText("EXPIRED");
                            viewHolder.tvQualificationMessage.setBackgroundColor(mContext.getResources().getColor(R.color.color_red));
                            viewHolder.tvDateValid.setTypeface(viewHolder.tvDateValid.getTypeface(), Typeface.BOLD);
                            viewHolder.tvDateValid.setTextColor(mContext.getResources().getColor(R.color.color_red));
                            break;
                        case UPCOMING:
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            Date dateValid = new Date();
                            Date today = new Date();
                            try {
                                dateValid = format.parse(qualification.getDateValid());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            viewHolder.tvQualificationMessage.setText("UPCOMING");
                            viewHolder.tvQualificationMessage.setBackgroundColor(mContext.getResources().getColor(R.color.mcc_amber));
                            viewHolder.tvDateValid.setTypeface(viewHolder.tvDateValid.getTypeface(), Typeface.BOLD);
                            viewHolder.tvDateValid.setTextColor(mContext.getResources().getColor(R.color.mcc_amber));

                            Calendar calendarValid = Calendar.getInstance();
                            calendarValid.setTime(dateValid);
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(today);
                            calendar.add(Calendar.DAY_OF_MONTH, qualification.getNotifyDays());
                            long diff = calendar.getTimeInMillis() - calendarValid.getTimeInMillis();
                            long days = diff / (24 * 60 * 60 * 1000);
                            viewHolder.tvDaysToDate.setText(days <= 1 ? String.valueOf(days) + " DAY" : String.valueOf(days) + " DAYS");
                            break;
                        case VALID:
                            viewHolder.tvQualificationMessage.setText("VALID");
                            viewHolder.tvQualificationMessage.setBackgroundColor(mContext.getResources().getColor(R.color.mcc_cyan));
                            break;
                        case HISTORICAL:
                            viewHolder.tvQualificationMessage.setText("HISTORICAL");
                            viewHolder.tvQualificationMessage.setBackgroundColor(mContext.getResources().getColor(R.color.gray_1));
                            viewHolder.tvDateValid.setTextColor(mContext.getResources().getColor(R.color.gray_1));
                            break;
                    }
                } else {
                    //viewHolder.tvDescription2.setVisibility(View.VISIBLE);
                    viewHolder.tvDateValid.setText(String.valueOf(qualification.getActualQty()));
                    String descriptionFormat = "%1$s%2$s%3$s%4$s%5$s%6$s";
                    String refModel = "";
                    if (!TextUtils.isEmpty(qualification.getRefModel())) {
                        refModel = " on " + getTextRefModel(qualification.getRefModel());
                    }
                    String refAirfield = "";
                    if (qualification.getRefAirfield() != null && !TextUtils.isEmpty(qualification.getStringRefAirfield())) {
                        refAirfield = " at " + qualification.getStringRefAirfield();
                    }
                    String minimumPeriod = qualification.getMinimumPeriod() > 1 ? " in " + qualification.getMinimumPeriod() + " days"
                            : " in " + qualification.getMinimumPeriod() + "day";
                    String description1 = "";
                    if (qualification.getQTypeCode() == 31) {
                        description1 = String.format(descriptionFormat, qualification.getMinimumQty()
                                , qualification.getMinimumQty() > 1 ?
                                        " " + qualification.getZQualification().getMinimumWord2() + " " :
                                        " " + qualification.getZQualification().getMinimumWord1() + " ",
                                qualification.getRefExtraCondition() != null ? qualification.getRefExtraCondition() : "",
                                refModel, refAirfield, minimumPeriod);
                    } else if (qualification.getQTypeCode() == 25) {
                        description1 = String.format(descriptionFormat, qualification.getMinimumQty()
                                , qualification.getMinimumQty() > 1 ?
                                        " " + qualification.getZQualification().getMinimumWord2() + " " :
                                        " " + qualification.getZQualification().getMinimumWord1() + " ",
                                mDatabaseManager.getSetting(MCCPilotLogConst.SETTING_CODE_USER_NUM).getData(),
                                refModel, refAirfield, minimumPeriod);
                    } else if (qualification.getQTypeCode() == 34) {
                        description1 = String.format(descriptionFormat, qualification.getMinimumQty()
                                , qualification.getMinimumQty() > 1 ?
                                        " " + qualification.getZQualification().getMinimumWord2() + " " :
                                        " " + qualification.getZQualification().getMinimumWord1() + " ",
                                mDatabaseManager.getSetting(MCCPilotLogConst.SETTING_CODE_USER_BOOL).getData(),
                                refModel, refAirfield, minimumPeriod);
                    } else {
                        description1 = String.format(descriptionFormat, qualification.getMinimumQty(),
                                qualification.getRefExtraCondition() != null ? " " + qualification.getRefExtraCondition() + " " : " "
                                , qualification.getMinimumQty() > 1 ?
                                        qualification.getZQualification().getMinimumWord2() :
                                        qualification.getZQualification().getMinimumWord1(),
                                refModel, refAirfield, minimumPeriod);
                    }

                    viewHolder.tvDescription1.setText(description1);
                    viewHolder.tvDescription2.setText(qualification.getNotifyComment());
                    viewHolder.tvDescription2.setTextColor(mContext.getResources().getColor(R.color.grey_description_text));
                    switch (qualification.getStatus()) {
                        case EXPIRED:
                            viewHolder.tvQualificationMessage.setText("EXPIRED");
                            viewHolder.tvQualificationMessage.setBackgroundColor(mContext.getResources().getColor(R.color.color_red));
                            viewHolder.tvDateValid.setTypeface(viewHolder.tvDateValid.getTypeface(), Typeface.BOLD);
                            viewHolder.tvDateValid.setTextColor(mContext.getResources().getColor(R.color.color_red));
                            break;
                        case UPCOMING:
                            viewHolder.tvQualificationMessage.setText("UPCOMING");
                            viewHolder.tvQualificationMessage.setBackgroundColor(mContext.getResources().getColor(R.color.mcc_amber));
                            viewHolder.tvDateValid.setTypeface(viewHolder.tvDateValid.getTypeface(), Typeface.BOLD);
                            viewHolder.tvDateValid.setTextColor(mContext.getResources().getColor(R.color.mcc_amber));
                            viewHolder.tvDaysToDate.setText(qualification.getProficiencyUpComingDay() <= 1 ?
                                    String.valueOf(qualification.getProficiencyUpComingDay()) + " DAY" :
                                    String.valueOf(qualification.getProficiencyUpComingDay()) + " DAYS");
                            break;
                        case VALID:
                            viewHolder.tvQualificationMessage.setText("VALID");
                            viewHolder.tvQualificationMessage.setBackgroundColor(mContext.getResources().getColor(R.color.mcc_cyan));
                            break;
                        case HISTORICAL:
                            viewHolder.tvQualificationMessage.setText("HISTORICAL");
                            viewHolder.tvQualificationMessage.setBackgroundColor(mContext.getResources().getColor(R.color.gray_1));
                            viewHolder.tvDateValid.setTextColor(mContext.getResources().getColor(R.color.gray_1));
                            break;
                    }
                }
            }
        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }


        return convertView;
    }


    public void refreshAdapter(List<Qualification> pList) {
        this.mList = pList;
        notifyDataSetChanged();
    }


    public QualificationType getQualificationType() {
        return qualificationType;
    }

    public void setQualificationType(QualificationType qualificationType) {
        this.qualificationType = qualificationType;
    }

    public String getTextValidity(int validity) {
        String preText = "Validity : ";
        switch (validity) {
            case 0:
                return preText + "N/A";
            case 1:
                return preText + "1 month";
            case 2:
                return preText + "2 months";
            case 3:
                return preText + "3 months";
            case 6:
                return preText + "6 months";
            case 12:
                return preText + "1 year";
            case 18:
                return preText + "1.5 years";
            case 24:
                return preText + "2 years";
            case 30:
                return preText + "2.5 years";
            case 36:
                return preText + "3 years";
            case 48:
                return preText + "4 years";
            case 60:
                return preText + "5 years";
            case 120:
                return preText + "10 years";
            case 999:
                return preText + "lifetime";
            default:
                return preText + "lifetime";
        }
    }

    public String getTextRefModel(String refModel) {
        switch (refModel) {
            case "1":
                return "Microlight";
            case "2":
                return "Glider";
            case "3":
                return "Lighter-than-Air";
            case "4":
                return "Rotorcraft";
            case "5":
                return "Aeroplane";
            case "6":
                return "Rotorcraft (SE)";
            case "7":
                return "Rotorcraft (ME)";
            case "8":
                return "Aeroplane (SE)";
            case "9":
                return "Aeroplane (ME)";
            case "10":
                return "Aircraft (Land)";
            case "11":
                return "Aircraft (Sea)";
            case "12":
                return "Aircraft (any - only)";
            case "13":
                return "Simulator (any - only)";
            case "14":
                return "Drone (any - only)";
            default:
                return refModel;
        }
    }

    public static class ViewHolder {
        @Bind(R.id.tv_qualification_message)
        TextView tvQualificationMessage;
        @Bind(R.id.tv_date_valid)
        TextView tvDateValid;
        @Bind(R.id.tv_days_to_date)
        TextView tvDaysToDate;
        @Bind(R.id.tv_description_1)
        TextView tvDescription1;
        @Bind(R.id.tv_description_2)
        TextView tvDescription2;
        @Bind(R.id.tv_description_3)
        TextView tvDescription3;
        /*@Bind(R.id.iv_qualification_icon)
        ImageView ivQualificationIcon;*/

        public ViewHolder(View pView) {
            ButterKnife.bind(this, pView);
        }
    }
}

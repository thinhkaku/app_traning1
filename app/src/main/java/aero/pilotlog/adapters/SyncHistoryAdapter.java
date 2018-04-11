package aero.pilotlog.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.models.SyncHistoryModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by tuan.pv on 12/28/2015.
 */
public class SyncHistoryAdapter extends BaseAdapter {
    private List<SyncHistoryModel> mList;
    private Context mContext;

    public SyncHistoryAdapter(List<SyncHistoryModel> pList, Context pContext){
        this.mList = pList;
        this.mContext = pContext;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public SyncHistoryModel getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.row_item_delay, null);
            holder = new ViewHolder();

            holder.tvTimeUpload = (TextView) convertView.findViewById(R.id.tvNumericCode);
            holder.tvFileType = (TextView) convertView.findViewById(R.id.tvDelayName);
            holder.tvFileName = (TextView) convertView.findViewById(R.id.tvDescription);
            holder.llRootItem = (LinearLayout) convertView.findViewById(R.id.llItemDelay);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        SyncHistoryModel syncHistoryModel = getItem(position);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        try {
            c.setTimeInMillis(Long.valueOf(syncHistoryModel.getTimestamp()));
         } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }

        holder.tvTimeUpload.setText(sdf.format(c.getTime()));
        switch (syncHistoryModel.getTypeHistoryFile()) {
            case MCCPilotLogConst.HISTORY_FLIGHT_RECORD:
                holder.tvFileType.setText(R.string.text_sync_history_flight);
                break;
            case MCCPilotLogConst.HISTORY_PILOT_PICTURE:
                holder.tvFileType.setText(R.string.text_sync_history_pilot_picture);
                break;
            case MCCPilotLogConst.HISTORY_SIGN_PICTURE:
                holder.tvFileType.setText(R.string.text_sync_history_sign);
                break;
            default:
                break;
        }
        holder.tvFileName.setText(syncHistoryModel.getFileNameDisplay());

        holder.llRootItem.setBackgroundColor(syncHistoryModel.isSelected() ? mContext.getResources().getColor(
                R.color.grey_divider)
                : Color.TRANSPARENT);

        return convertView;
    }

    public void removeSelectedItems() {
        for (int i = 0, count = mList.size(); i < count; i++) {
            mList.get(i).setSelected(false);
        }
    }

    public void setSelection(int pPosition) {
        if (pPosition >= 0) {
            getItem(pPosition).setSelected(true);
        }
        notifyDataSetChanged();
    }

    public void updateAdapter(List<SyncHistoryModel> pList) {
        this.mList = pList;
        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView tvTimeUpload;
        TextView tvFileType;
        TextView tvFileName;
        LinearLayout llRootItem;
    }
}

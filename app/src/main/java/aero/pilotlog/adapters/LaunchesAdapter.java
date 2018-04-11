package aero.pilotlog.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.databases.entities.ZLaunch;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tuan.na on 9/16/2015.
 * Launches adapter
 */
public class LaunchesAdapter extends MCCPilotLogBaseAdapter {

    private List<ZLaunch> mLaunches = new ArrayList<>();
    private final LayoutInflater mLayoutInflater;
    private Context mContext;

    public LaunchesAdapter(Context pContext, List<ZLaunch> pList) {
        mContext = pContext;
        this.mLayoutInflater = (LayoutInflater) pContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLaunches = pList;
    }

    @Override
    protected int getCountList() {
        return getCount();
    }

    @Override
    protected String getStringItem(int pPosition) {
        return null;
    }

    @Override
    public int getCount() {
        return mLaunches.size();
    }

    @Override
    public ZLaunch getItem(int position) {
        return mLaunches.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mLaunches.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.row_item_delay, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ZLaunch launch = getItem(position);
        if (launch != null) {
         /*   switch (launch.getLaunchCode()){
                case 0:
                    viewHolder.tvApp.setText(MCCPilotLogConst.STRING_EMPTY);
                    break;
                case 101:
                    viewHolder.tvApp.setText("AT");
                    break;
                case 102:
                    viewHolder.tvApp.setText("BG");
                    break;
                case 103:
                    viewHolder.tvApp.setText("CT");
                    break;
                case 104:
                    viewHolder.tvApp.setText("SL");
                    break;
                case 105:
                    viewHolder.tvApp.setText("WH");
                    break;
                default:
                    break;
            }*/
            viewHolder.tvApp.setText(launch.getLaunchLL());
            viewHolder.tvDetail.setText(launch.getLaunchShort());
            viewHolder.tvPrecision.setText(launch.getLaunchLong());
            //viewHolder.tvPrecision.setVisibility(View.GONE);

        }

        return convertView;
    }

    public void refreshAdapter(List<ZLaunch> pLaunch) {
        this.mLaunches = pLaunch;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @Bind(R.id.tvNumericCode)
        TextView tvApp;
        @Bind(R.id.tvDelayName)
        TextView tvDetail;
        @Bind(R.id.tvDescription)
        TextView tvPrecision;

        public ViewHolder(View pView) {
            ButterKnife.bind(this, pView);
        }
    }
}

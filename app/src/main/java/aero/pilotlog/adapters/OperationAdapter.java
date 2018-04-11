package aero.pilotlog.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View;
import aero.pilotlog.databases.entities.ZOperation;
import aero.crewlounge.pilotlog.R;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by phuc.dd on 2/15/2017.
 */
public class OperationAdapter extends MCCPilotLogBaseAdapter{
    private List<ZOperation> mOperationLists = new ArrayList<>();
    private final LayoutInflater mLayoutInflater;
    private Context mContext;

    public OperationAdapter(Context pContext, List<ZOperation> pList) {
        mContext = pContext;
        this.mLayoutInflater = (LayoutInflater) pContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mOperationLists = pList;
    }

    @Override
    protected int getCountList() {
        return getCount();
    }

    @Override
    protected String getStringItem(int pPosition) {
        return mOperationLists.get(pPosition).getOpsShort();
    }

    @Override
    public int getCount() {
        return mOperationLists.size();
    }

    @Override
    public ZOperation getItem(int position) {
        return mOperationLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mOperationLists.get(position).hashCode();
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

        ZOperation zOperation = getItem(position);
        if (zOperation != null) {
            viewHolder.tvApp.setText(zOperation.getOpsShort());
            viewHolder.tvDetail.setText(zOperation.getOpsLong());
        }
        viewHolder.tvPrecision.setVisibility(View.GONE);

        return convertView;
    }

    public void refreshAdapter(List<ZOperation> operations) {
        this.mOperationLists = operations;
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

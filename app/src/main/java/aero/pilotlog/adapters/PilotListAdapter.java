package aero.pilotlog.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;
//import net.mcc3si.databases.entities.FlightPilot;
import aero.pilotlog.databases.entities.Pilot;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.tasks.ImageProcessTask;
import aero.pilotlog.utilities.StorageUtils;
import aero.pilotlog.utilities.Utils;
import aero.pilotlog.widgets.IndexableListView;
import aero.pilotlog.widgets.swipelayout.SimpleSwipeListener;
import aero.pilotlog.widgets.swipelayout.SwipeLayout;
import aero.pilotlog.widgets.swipelayout.adapters.BaseSwipeAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ngoc.dh on 7/21/2015.
 * Pilot List Adapter
 */
public abstract class PilotListAdapter extends BaseSwipeAdapter {

    private Context mContext;
    private List<Pilot> mPilotList = new ArrayList<>();
    //private List<FlightPilot> mFlightPilots;
    private DatabaseManager mDatabaseManager;
    private boolean mIsSelectMode = false;
    private boolean isEnableItemClick = true;
    private static final long DELAY_ENABLE_CLICK = 100;
    private SwipeLayout mSwipeLayout;
    private DisplayImageOptions options;
    private IndexableListView mListView;

    public boolean isOpenSwipe() {
        return mSwipeLayout.getOpenStatus() == SwipeLayout.Status.Open;
    }

    public void setSoftType(int softType) {
        this.softType = softType;
    }

    private int softType = 0;

    public void setSelectMode(boolean pFlag) {
        mIsSelectMode = pFlag;
    }

    public void setListView(IndexableListView lv) {
        mListView = lv;
    }

    public PilotListAdapter(Context pContext, List<Pilot> pPilotList, int softType) {
        this.mContext = pContext;
        this.mPilotList = pPilotList;
        this.softType = softType;
        mDatabaseManager = DatabaseManager.getInstance(pContext);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.pilot_profile)
                .showImageForEmptyUri(R.drawable.pilot_profile)
                .showImageOnFail(R.drawable.pilot_profile)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    public int getCount() {
        return mPilotList.size();
    }

    @Override
    public Pilot getItem(int position) {
        return mPilotList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public boolean getSwipeLayoutShown() {
        return mSwipeLayout != null && mSwipeLayout.isShown();
    }

    public void closeSwipe(boolean pSmooth) {
        if (mSwipeLayout != null) {
            mSwipeLayout.close(pSmooth);
            mSwipeLayout = null;
        }
    }

    public boolean isEnableClick() {
        return isEnableItemClick;
    }

    @Override
    public void fillValues(final int position, View convertView) {
        final ViewHolder holder = new ViewHolder(convertView, position);
        TextView t = (TextView) convertView.findViewById(R.id.position);
        t.setText((position + 1) + ".");

        Pilot mPilot = getItem(position);
        File f = new File(StorageUtils.getStorageRootFolder(mContext), String.format(MCCPilotLogConst.PILOT_IMAGE_FILE_NAME_V4, Utils.getGUIDFromByteArray(mPilot.getPilotCode())));
        holder.mTvName.setText(TextUtils.isEmpty(mPilot.getPilotName()) ? MCCPilotLogConst.STRING_EMPTY : mPilot.getPilotName().trim());
        holder.mTvEmpId.setText(TextUtils.isEmpty(mPilot.getPilotRef()) ? MCCPilotLogConst.STRING_EMPTY : mPilot.getPilotRef().toUpperCase().trim());
        holder.mTvCompany.setText(TextUtils.isEmpty(mPilot.getCompany()) ? MCCPilotLogConst.STRING_EMPTY : mPilot.getCompany().trim());
        if (f.exists()) {
            //PL-201
            ImageProcessTask mTask = new ImageProcessTask((Activity) mContext) {
                @Override
                public void handleBitmap(Bitmap bitmap) {
                    if (bitmap != null) {
                        holder.mIvPilot.setImageBitmap(bitmap);
                    } else {
                        holder.mIvPilot.setImageResource(R.drawable.camera_cyan);
                    }
                }
            };
            mTask.execute(f);
            //ImageLoader.getInstance().displayImage("file://" + f.getAbsolutePath(), holder.mIvPilot, options);
            //End PL-201
        } else {
            holder.mIvPilot.setImageResource(R.drawable.camera_cyan);
        }
        holder.mSwipe.setSwipeEnabled(!mIsSelectMode);//In select mode then disable swipe layout
        holder.mSwipe.setClickToClose(true);
        holder.mSwipe.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                mSwipeLayout = layout;
            }

            @Override
            public void onHandRelease(final SwipeLayout layout, float xvel, float yvel) {
                isEnableItemClick = false;
                layout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isEnableItemClick = true;
                    }
                }, DELAY_ENABLE_CLICK);
                super.onHandRelease(layout, xvel, yvel);
            }
        });

        holder.rlFront.setBackgroundColor(mPilot.isSelected() ? mContext.getResources().getColor(
                R.color.mcc_cyan_derived)
                : Color.TRANSPARENT);
//Task PL-56 28/3/16
//         if (mPilot.ismClick()){
//            holder.mIvPilot.setImageResource(R.drawable.pilot_profile_2);
//        }else{
//            holder.mIvPilot.setImageResource(R.drawable.pilot_profile);
//        }
        //End Task PL-56
    }

    @Override
    protected String getStringItem(int pPosition) {
        return getItem(pPosition).getPilotName();
    }

    @Override
    protected int getCountList() {
        return getCount();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View view;
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (softType == 1) {
            view = inflater.inflate(R.layout.row_item_pilot, parent, false);
        } else {
            view = inflater.inflate(R.layout.row_item_pilot_name, parent, false);
        }
        return view;
    }


    public void removeAllItemSelected() {
        for (int i = 0, count = mPilotList.size(); i < count; i++) {
            mPilotList.get(i).setSelected(false);
        }
    }

    public void setSelectItem(int position) {
        mPilotList.get(position).setSelected(true);
        notifyDataSetChanged();
    }

    public abstract void deleteAction(int position);

    public abstract void deActiveAction(int position);

    @Override
    public void closeAllItems(boolean smooth) {

    }

    public void refreshAdapter(List<Pilot> pPilots) {
        this.mPilotList = pPilots;
        notifyDataSetChanged();
    }

    public class ViewHolder {
        @Bind(R.id.tvPilotName)
        TextView mTvName;
        @Bind(R.id.tvEmployeeID)
        TextView mTvEmpId;
        @Bind(R.id.tvCompany)
        TextView mTvCompany;
        @Bind(R.id.swipe)
        SwipeLayout mSwipe;
        @Bind(R.id.rlFront)
        RelativeLayout rlFront;
        @Bind(R.id.ivPilotPic)
        ImageView mIvPilot;

        private int mIndex;

        @OnClick({R.id.btnDelete, R.id.btnDeActive})
        public void onClickDelete(View pView) {
            switch (pView.getId()) {
                case R.id.btnDelete:
                    deleteAction(mIndex);
                    break;
                case R.id.btnDeActive:
                    deActiveAction(mIndex);
                    break;
            }

        }

        public ViewHolder(View view, int pPosition) {
            ButterKnife.bind(this, view);

            this.mIndex = pPosition;
        }
    }
}
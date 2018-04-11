package aero.pilotlog.tasks;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.text.TextUtils;
import android.widget.TextView;

import aero.pilotlog.activities.BaseActivity;
import aero.pilotlog.utilities.LogUtils;
import aero.pilotlog.utilities.NetworkUtils;

/**
 * Created by tuan.pv on 2015/12/30.
 */
public class CheckInternetTask extends AsyncTask<String, Integer, Void> {

    private BaseActivity mActivity;
    private String mNetworkType;
    private TextView mTvNetwork;

    public CheckInternetTask(BaseActivity activity, TextView tvNetwork) {
        mActivity = activity;
        mTvNetwork = tvNetwork;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... arg0) {
        try {
            while (TextUtils.isEmpty(mNetworkType)) {
                mNetworkType = NetworkUtils.getNetworkType(mActivity);
                SystemClock.sleep(2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        LogUtils.e("check internet again!");
        mTvNetwork.setText(mNetworkType);
        mTvNetwork.setTextColor(mActivity.getResources().getColor(android.R.color.holo_red_light));
        mNetworkType = "";
//        ViewUtils.setEnableMenuItem(mMiDownloadAll, true);
//        ViewUtils.setEnableMenuItem(mMiDownload7Day, true);
    }
}

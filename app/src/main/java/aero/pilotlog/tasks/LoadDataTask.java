package aero.pilotlog.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.interfaces.IAsyncTaskCallback;

/**
 * Created by tuan.pv on 2015/08/28.
 */
public class LoadDataTask extends AsyncTask<Void, Void, Void> {

    private IAsyncTaskCallback mIAsyncTaskCallback;
    private ProgressDialog mDialog;
    private Context mContext;
    private LinearLayout mLinearLayout;

    public LoadDataTask(Context pContext, IAsyncTaskCallback pTaskCallback) {
        this.mIAsyncTaskCallback = pTaskCallback;
        this.mContext = pContext;
    }

    public LoadDataTask(Context pContext, IAsyncTaskCallback pTaskCallback, LinearLayout pLinearLayout) {
        this.mIAsyncTaskCallback = pTaskCallback;
        this.mContext = pContext;
        this.mLinearLayout = pLinearLayout;
    }

    @Override
    protected void onPreExecute() {
//        checkShowDiaLogLoading();
        super.onPreExecute();
        if (mLinearLayout != null) {
            mLinearLayout.setVisibility(View.VISIBLE);
        }
        if (mIAsyncTaskCallback != null) {
            mIAsyncTaskCallback.start();
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
//        checkDismissDiaLogLoading();
        super.onPostExecute(aVoid);
        if (mLinearLayout != null) {
            mLinearLayout.setVisibility(View.GONE);
        }
        if (mIAsyncTaskCallback != null) {
            mIAsyncTaskCallback.end();
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        if (mIAsyncTaskCallback != null) {
            mIAsyncTaskCallback.doWork();
            publishProgress();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        if (mIAsyncTaskCallback != null) {
            mIAsyncTaskCallback.updateUI();

        }
    }

    private void checkDismissDiaLogLoading() {
        if (mDialog != null && mDialog.isShowing()) {
            try {
                mDialog.dismiss();
            } catch (Throwable ignored) {
            }
            mDialog = null;
        }
    }

    private void checkShowDiaLogLoading() {
        if (mDialog == null) {
            mDialog = new ProgressDialog(mContext);
            mDialog.setCancelable(false);
            mDialog.setMessage(mContext.getString(R.string.loading));
        }
        try {
            mDialog.show();
        } catch (Throwable ignored) {
        }
    }
}

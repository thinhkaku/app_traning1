package aero.pilotlog.utilities;

import android.app.Activity;
import android.app.ProgressDialog;


/**
 * Created by tuan.na on 2/5/2015.
 * Progress Dialog
 */
public class ProgressDialogUtils  {

    private static ProgressDialog progressDialog;
    private Activity mActivity;

    public ProgressDialogUtils(Activity mActivity) {
        this.mActivity = mActivity;
    }

    /**Show progress dialog with given string resource */
    public ProgressDialog showProgressDialog (int stringMessageResource){
        progressDialog = new ProgressDialog(mActivity);
        progressDialog.setMessage(mActivity.getString(stringMessageResource));
        progressDialog.setCancelable(false);
        progressDialog.show();
        return progressDialog;
    }

    public ProgressDialog showProgressDialog (String stringMessage){
        progressDialog = new ProgressDialog(mActivity);
        progressDialog.setMessage(stringMessage);
        progressDialog.setCancelable(false);
        progressDialog.show();
        return progressDialog;
    }

    /**Hide progress dialog if it is showing */
    public boolean hideProgressDialog() {
        if (progressDialog != null){
            progressDialog.dismiss();
            return true;
        }
        return false;
    }

    public boolean isShowing(){
        return progressDialog != null && progressDialog.isShowing();
    }

    public void updateMessage(final String message){
        if (isShowing()){
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.setMessage(message);
                }
            });
        }
    }

    public void updateMessage(final int message){
        if (isShowing()){
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.setMessage(mActivity.getString(message));
                }
            });
        }
    }
}

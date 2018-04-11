package aero.pilotlog.widgets;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import aero.crewlounge.pilotlog.R;


public class AlertToast extends Toast {

    private Context mContext;
    private boolean mIsLengthLong = true;

    public AlertToast(Context context) {
        super(context);
        mContext = context;
    }

    public AlertToast(Context context, boolean isLengthLong) {
        super(context);
        mContext = context;
        mIsLengthLong = isLengthLong;
    }

    public void showToast() {
        if (mIsLengthLong) {
            makeText(mContext, R.string.dialog_loading, Toast.LENGTH_LONG).show();
        } else {
            makeText(mContext, R.string.dialog_loading, Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelToast() {
        try{
            if(getView().isShown())     // true if visible
            cancel();
        } catch (Exception ex) {
            Log.e(this.getClass().getName(), "Exception when cancel toast !", ex);
        }
    }

}

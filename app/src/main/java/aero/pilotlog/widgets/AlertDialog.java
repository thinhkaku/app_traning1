package aero.pilotlog.widgets;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import aero.crewlounge.pilotlog.R;


public class AlertDialog extends android.app.AlertDialog {

    private Context mContext;

    public AlertDialog(Context context) {
        super(context);
        mContext = context;
    }

    public static void createOTPDialog(AppCompatActivity mActivity, OnClickListener onClickListener, View layout) {
        android.app.AlertDialog.Builder mBuilder = new Builder(mActivity);
        mBuilder.setTitle("OTP:");
        mBuilder.setPositiveButton(android.R.string.ok, onClickListener);
        mBuilder.setNegativeButton(android.R.string.cancel, null);
        mBuilder.setView(layout);
        mBuilder.show();
    }

    /**
     * Create dialog with title and content as string.
     *
     * @param title   String
     * @param content String
     * @return Alert Dialog
     */
    public android.app.AlertDialog.Builder createDialog(String title, String content) {
        return new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyleTranlucent)
                .setTitle(title)
                .setMessage(content)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    /**
     * Create dialog with only content string.
     *
     * @param content String
     * @return Builder
     */
    public Builder createDialog(String content) {
        return new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyleTranlucent)
                .setMessage(content)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    /**
     * Create dialog with only content ID
     *
     * @param contentId Content ID
     * @return Builder
     */
    public Builder createDialog(int contentId) {
        return new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyleTranlucent)
                .setMessage(mContext.getResources().getString(contentId))
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    /**
     * Create dialog with title and content are resource string.
     *
     * @param titleResourse   Title resource
     * @param contentResourse Content resource
     * @return Builder
     */
    public Builder createDialog(int titleResourse, int contentResourse) {
        return new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyleTranlucent)
                .setTitle(mContext.getResources().getString(titleResourse))
                .setMessage(mContext.getResources().getString(contentResourse))
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    /**
     * create dialog with title and content
     *
     * @param titleResourse Title resource
     * @param content       String
     * @return Builder
     */
    public Builder createDialog(int titleResourse, String content) {
        return new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyleTranlucent)
                .setTitle(mContext.getResources().getString(titleResourse))
                .setMessage(content)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    public Builder createDialog(int titleResourse, String content, OnClickListener onClickListener) {
        return new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyleTranlucent)
                .setTitle(mContext.getResources().getString(titleResourse))
                .setMessage(content)
                .setPositiveButton(R.string.ok, onClickListener);
    }

    public Builder createDialog(int titleResourse, int contentResource, OnClickListener onClickListener) {
        return new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyleTranlucent)
                .setTitle(titleResourse)
                .setMessage(contentResource)
                .setPositiveButton(R.string.ok, onClickListener);
    }

    public Builder createConfirmDialog(int titleResourse, int contentResourse, OnClickListener clickListener) {
        return new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyleTranlucent)
                .setTitle(titleResourse)
                .setMessage(contentResourse)
                .setPositiveButton(R.string.yes, clickListener)
                .setNegativeButton(R.string.no, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
    }

    /**
     * Create dialog confirm with dymamic negative/positive label
     *
     * @param titleResourse    Title resource
     * @param contentResourse  Content resource
     * @param positiveResource Positive Resource
     * @param negativeResource Negative Resource
     * @param clickListener    Click Listener
     * @return Builder
     */
    public Builder createConfirmDialog(int titleResourse, int contentResourse, int positiveResource,
                                       int negativeResource, OnClickListener clickListener) {
        return new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyleTranlucent)
                .setTitle(titleResourse)
                .setMessage(contentResourse)
                .setPositiveButton(positiveResource, clickListener)
                .setNegativeButton(negativeResource, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
    }

    /**
     * Create confirm dialog with both listener for positive button and negative button
     *
     * @param titleResourse    Title resource
     * @param contentResourse  Content resource
     * @param positiveResource Positive Resource
     * @param negativeResource Negative Resource
     * @param clickListener    Click Listener
     * @param negativeListener Negative Listener
     * @return Builder
     */
    public Builder createConfirmDialog(int titleResourse, int contentResourse, int positiveResource,
                                       int negativeResource, OnClickListener clickListener, OnClickListener negativeListener) {
        return new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyleTranlucent)
                .setTitle(titleResourse)
                .setMessage(contentResourse)
                .setPositiveButton(positiveResource, clickListener)
                .setNegativeButton(negativeResource, negativeListener);
    }

    public Builder createConfirmDialog(int titleResourse, String message, int positiveResource,
                                       int negativeResource, OnClickListener clickListener, OnClickListener negativeListener) {
        return new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyleTranlucent)
                .setTitle(titleResourse)
                .setMessage(message)
                .setPositiveButton(positiveResource, clickListener)
                .setNegativeButton(negativeResource, negativeListener);
    }


    public Builder createConfirmDialog(int titleResourse, String contentResourse, OnClickListener clickListener) {
        return new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyleTranlucent)
                .setTitle(titleResourse)
                .setMessage(contentResourse)
                .setPositiveButton(R.string.yes, clickListener)
                .setNegativeButton(R.string.no, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
    }

    public Builder createConfirmDialog(int titleResourse, int positiveText, int negativeText, String contentResourse, OnClickListener clickListener) {
        return new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyleTranlucent)
                .setTitle(titleResourse)
                .setMessage(contentResourse)
                .setPositiveButton(positiveText, clickListener)
                .setNegativeButton(negativeText, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
    }

    public Builder createConfirmDialog(int contentResourse, int positiveText, int negativeText, OnClickListener postList, OnClickListener negativeList) {
        return new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyleTranlucent)
                .setMessage(contentResourse)
                .setPositiveButton(mContext.getResources().getString(positiveText), postList)
                .setNegativeButton(mContext.getResources().getString(negativeText), negativeList);
    }

    public Builder createConfirmDialog(int contentResourse, int positiveText, int negativeText, OnClickListener postList) {
        return new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyleTranlucent)
                .setMessage(contentResourse)
                .setPositiveButton(mContext.getResources().getString(positiveText), postList)
                .setNegativeButton(mContext.getResources().getString(negativeText), new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
    }

    public Builder createConfirmDialog(int titleResource, String contentResourse, int positiveText, int negativeText, OnClickListener postList) {
        return new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyleTranlucent)
                .setTitle(titleResource)
                .setMessage(contentResourse)
                .setPositiveButton(mContext.getResources().getString(positiveText), postList)
                .setNegativeButton(mContext.getResources().getString(negativeText), new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
    }

}

package aero.pilotlog.widgets;

import android.support.v7.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.ApiConstant;
import aero.pilotlog.interfaces.ISequenceAction;
import aero.pilotlog.interfaces.ISequenceActionWithObject;
import aero.pilotlog.interfaces.ISequenceMultiChoiceAction;

/**
 * Created by tuan.pv on 2015/07/10.
 */
public class MccDialog {
    public static final int FLAG_RESOURCE_NULL = -1;
    public static final String FLAG_RESOURCE_EMPTY = "";
    private static ProgressDialog mProgressDialog;


    public interface MCCDialogCallBack {
        /**
         * Handle event click button on dialog
         *
         * @param pAlertDialog
         * @param pDialogType
         */
        void onClickDialog(DialogInterface pAlertDialog, int pDialogType);
    }

    public interface MCCDialogCallBackBoolean {
        /**
         * Handle event click button on dialog
         *
         * @param pAlertDialog
         * @param pDialogType
         */
        boolean onClickDialog(DialogInterface pAlertDialog, int pDialogType);
    }

    public MccDialog() {
    }

    /**
     * Get AlertDialog
     *
     * @param context
     * @param titleResId          Set -1 if do not want to show
     * @param messageResId        Set -1 if do not want to show
     * @param positiveButtonResId Set -1 if do not want to show
     * @param negativeButtonResId Set -1 if do not want to show
     * @param callBack
     * @return
     */
    public static android.support.v7.app.AlertDialog getAlertDialog(Context context, int titleResId, int messageResId, int positiveButtonResId, int negativeButtonResId, int neutralButtonResId, final MCCDialogCallBack callBack) {
        String strTitle = "";
        if (titleResId != FLAG_RESOURCE_NULL) {
            strTitle = context.getString(titleResId);
        }

        return getAlertDialog(context, strTitle, context.getString(messageResId), positiveButtonResId, negativeButtonResId, neutralButtonResId, callBack);
    }

    public static android.support.v7.app.AlertDialog getAlertDialog(Context context, int titleResId, String messageResId, int positiveButtonResId, int negativeButtonResId, int neutralButtonResId, final MCCDialogCallBack callBack) {
        String strTitle = "";
        if (titleResId != FLAG_RESOURCE_NULL) {
            strTitle = context.getString(titleResId);
        }

        return getAlertDialog(context, strTitle, messageResId, positiveButtonResId, negativeButtonResId, neutralButtonResId, callBack);
    }


    public static android.support.v7.app.AlertDialog getInformationAlertDialog(final Context context, String titleRes, String message) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inputTextDialog = inflater.inflate(R.layout.layout_dialog_with_text, null);
        LinearLayout linearLayout = (LinearLayout) inputTextDialog.findViewById(R.id.custom_dialog_title);
        TextView tvTitle = (TextView) inputTextDialog.findViewById(R.id.message);
        tvTitle.setText(titleRes);
        TextView tvContent = (TextView) inputTextDialog.findViewById(R.id.content);
        tvContent.setGravity(Gravity.LEFT);
        tvContent.setText(message);
        AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context, R.style.MessageDialogTheme);
        linearLayout.setBackgroundColor(context.getResources().getColor(R.color.mcc_cyan));
        alertDialogBuilder.setView(inputTextDialog);
        alertDialogBuilder.setPositiveButton(R.string.alert_ok_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

            }
        });
        final android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                //Only after .show() was called
                int textViewId = alertDialog.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
                TextView tv = (TextView) alertDialog.findViewById(textViewId);
                if (tv != null) {
                    tv.setTextColor(context.getResources().getColor(R.color.mcc_cyan));
                }

                int dividerId = alertDialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
                View divider = alertDialog.findViewById(dividerId);
                if (divider != null) {
                    divider.setBackgroundColor(context.getResources().getColor(R.color.mcc_cyan));
                }
            }
        });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        return alertDialog;
    }

    public static android.support.v7.app.AlertDialog getAlertDialog(final Context context, String titleRes, String message, int positiveButtonResId, int negativeButtonResId, int neutralButtonResId, final MCCDialogCallBack callBack) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inputTextDialog = inflater.inflate(R.layout.layout_dialog_with_text, null);
        LinearLayout linearLayout = (LinearLayout) inputTextDialog.findViewById(R.id.custom_dialog_title);
        TextView tvTitle = (TextView) inputTextDialog.findViewById(R.id.message);
        tvTitle.setText(titleRes);
        TextView tvContent = (TextView) inputTextDialog.findViewById(R.id.content);
        tvContent.setText(message);
        AlertDialog.Builder alertDialogBuilder;
        int countButton = 0;
        if (negativeButtonResId != FLAG_RESOURCE_NULL) countButton += 1;
        if (neutralButtonResId != FLAG_RESOURCE_NULL) countButton += 1;
        if (positiveButtonResId != FLAG_RESOURCE_NULL) countButton += 1;

        if (countButton == 1) {
            alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context, R.style.AlertDialogTheme);
            linearLayout.setBackgroundColor(context.getResources().getColor(R.color.mcc_magenta));
        } else {
            alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context, R.style.MessageDialogTheme);
            linearLayout.setBackgroundColor(context.getResources().getColor(R.color.mcc_cyan));
        }

        alertDialogBuilder.setView(inputTextDialog);
        if (positiveButtonResId != FLAG_RESOURCE_NULL) {
            alertDialogBuilder.setPositiveButton(positiveButtonResId, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    if (callBack != null) {
                        callBack.onClickDialog(dialogInterface, DialogInterface.BUTTON_POSITIVE);
                    }
                }
            });
        }
        if (neutralButtonResId != FLAG_RESOURCE_NULL) {
            alertDialogBuilder.setNeutralButton(neutralButtonResId, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (callBack != null) {
                        callBack.onClickDialog(dialogInterface, DialogInterface.BUTTON_NEUTRAL);
                    }
                }
            });
        }
        if (negativeButtonResId != FLAG_RESOURCE_NULL) {
            alertDialogBuilder.setNegativeButton(negativeButtonResId, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int which) {
                    if (callBack != null) {
                        callBack.onClickDialog(dialogInterface, DialogInterface.BUTTON_NEGATIVE);
                    }
                }
            });
        }

        final android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                //Only after .show() was called
                int textViewId = alertDialog.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
                TextView tv = (TextView) alertDialog.findViewById(textViewId);
                if (tv != null) {
                    tv.setTextColor(context.getResources().getColor(R.color.mcc_cyan));
                }

                int dividerId = alertDialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
                View divider = alertDialog.findViewById(dividerId);
                if (divider != null) {
                    divider.setBackgroundColor(context.getResources().getColor(R.color.mcc_cyan));
                }
            }
        });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        return alertDialog;
    }

    public static android.support.v7.app.AlertDialog getAlertDialogMagenta(final Context context, String titleRes, String message, int positiveButtonResId, int negativeButtonResId, int neutralButtonResId, final MCCDialogCallBack callBack) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inputTextDialog = inflater.inflate(R.layout.layout_dialog_with_text, null);
        LinearLayout linearLayout = (LinearLayout) inputTextDialog.findViewById(R.id.custom_dialog_title);
        TextView tvTitle = (TextView) inputTextDialog.findViewById(R.id.message);
        tvTitle.setText(titleRes);
        TextView tvContent = (TextView) inputTextDialog.findViewById(R.id.content);
        tvContent.setText(message);
        AlertDialog.Builder alertDialogBuilder;
        int countButton = 0;
        if (negativeButtonResId != FLAG_RESOURCE_NULL) countButton += 1;
        if (neutralButtonResId != FLAG_RESOURCE_NULL) countButton += 1;
        if (positiveButtonResId != FLAG_RESOURCE_NULL) countButton += 1;

        alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context, R.style.AlertDialogTheme);
        linearLayout.setBackgroundColor(context.getResources().getColor(R.color.mcc_magenta));

        alertDialogBuilder.setView(inputTextDialog);
        if (positiveButtonResId != FLAG_RESOURCE_NULL) {
            alertDialogBuilder.setPositiveButton(positiveButtonResId, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    if (callBack != null) {
                        callBack.onClickDialog(dialogInterface, DialogInterface.BUTTON_POSITIVE);
                    }
                }
            });
        }
        if (neutralButtonResId != FLAG_RESOURCE_NULL) {
            alertDialogBuilder.setNeutralButton(neutralButtonResId, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (callBack != null) {
                        callBack.onClickDialog(dialogInterface, DialogInterface.BUTTON_NEUTRAL);
                    }
                }
            });
        }
        if (negativeButtonResId != FLAG_RESOURCE_NULL) {
            alertDialogBuilder.setNegativeButton(negativeButtonResId, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int which) {
                    if (callBack != null) {
                        callBack.onClickDialog(dialogInterface, DialogInterface.BUTTON_NEGATIVE);
                    }
                }
            });
        }

        final android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                //Only after .show() was called
                int textViewId = alertDialog.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
                TextView tv = (TextView) alertDialog.findViewById(textViewId);
                if (tv != null) {
                    tv.setTextColor(context.getResources().getColor(R.color.mcc_cyan));
                }

                int dividerId = alertDialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
                View divider = alertDialog.findViewById(dividerId);
                if (divider != null) {
                    divider.setBackgroundColor(context.getResources().getColor(R.color.mcc_cyan));
                }
            }
        });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        return alertDialog;
    }

    /**
     * Get AlertDialog with 2 buttons button no title
     */
    public static android.support.v7.app.AlertDialog getAlertDialog(Context context, int titleId, int messageResId, int negativeButtonResId, final MCCDialogCallBack callBack) {
        return getAlertDialog(context, titleId, messageResId, R.string.alert_ok_button, negativeButtonResId, FLAG_RESOURCE_NULL, callBack);
    }

    /**
     * Get AlertDialog with one negative button
     *
     * @param context
     * @param pTitleResId
     * @param negativeButtonResId
     * @param callBack
     * @return
     */
    public static android.support.v7.app.AlertDialog getAlertDialog(Context context, int pTitleResId, String pMsgContent, int negativeButtonResId, final MCCDialogCallBack callBack) {
        return getAlertDialog(context, context.getString(pTitleResId), pMsgContent, FLAG_RESOURCE_NULL, negativeButtonResId, FLAG_RESOURCE_NULL, callBack);
    }


    /**
     * Show alert dialog with ok and cancel button
     *
     * @param pContext
     * @param pMsgContentResId
     * @param pDialogCallBack
     * @return
     */
    public static android.support.v7.app.AlertDialog getCancelContinueAlertDialog(Context pContext, int pTitleId, int pMsgContentResId, final MCCDialogCallBack pDialogCallBack) {
        return getAlertDialog(pContext, pTitleId, pMsgContentResId, R.string.alert_continue_button, R.string.alert_cancel_button, FLAG_RESOURCE_NULL, pDialogCallBack);
    }

    public static android.support.v7.app.AlertDialog getOkCancelAlertDialog(Context pContext, int pMsgContentResId, final MCCDialogCallBack pDialogCallBack) {
        return getAlertDialog(pContext, FLAG_RESOURCE_NULL, pMsgContentResId, R.string.alert_ok_button, R.string.alert_cancel_button, FLAG_RESOURCE_NULL, pDialogCallBack);
    }

    public static android.support.v7.app.AlertDialog getDeActiveAlertDialogMagenta(Context pContext, int pTitleId, String pMsgContentResId, final MCCDialogCallBack pDialogCallBack) {
        return getAlertDialogMagenta(pContext, pContext.getString(pTitleId), pMsgContentResId, R.string.alert_de_active_button, R.string.alert_cancel_button, FLAG_RESOURCE_NULL, pDialogCallBack);
    }

    public static android.support.v7.app.AlertDialog getDeActiveAlertDialog(Context pContext, int pTitleId, String pMsgContentResId, final MCCDialogCallBack pDialogCallBack) {
        return getAlertDialog(pContext, pContext.getString(pTitleId), pMsgContentResId, R.string.alert_de_active_button, R.string.alert_cancel_button, FLAG_RESOURCE_NULL, pDialogCallBack);
    }

    public static android.support.v7.app.AlertDialog getOkCancelAlertDialog(Context pContext, int pTitleId, int pMsgContentResId, final MCCDialogCallBack pDialogCallBack) {
        return getAlertDialog(pContext, pTitleId, pMsgContentResId, R.string.alert_ok_button, R.string.alert_cancel_button, FLAG_RESOURCE_NULL, pDialogCallBack);
    }

    public static android.support.v7.app.AlertDialog getOkDeleteAllAlertDialog(Context pContext, int pTitleId, int pMsgContentResId, final MCCDialogCallBack pDialogCallBack) {
        return getAlertDialog(pContext, pTitleId, pMsgContentResId, R.string.alert_deleteAll_button, R.string.alert_cancel_button, FLAG_RESOURCE_NULL, pDialogCallBack);
    }

    public static android.support.v7.app.AlertDialog getOkDeleteAllAlertDialog(Context pContext, int pTitleId, String pMsgContentResId, final MCCDialogCallBack pDialogCallBack) {
        return getAlertDialog(pContext, pTitleId, pMsgContentResId, R.string.alert_deleteAll_button, R.string.alert_cancel_button, FLAG_RESOURCE_NULL, pDialogCallBack);
    }

    public static android.support.v7.app.AlertDialog getOkDeleteAlertDialog(Context pContext, int pTitleId, int pMsgContentResId, final MCCDialogCallBack pDialogCallBack) {
        return getAlertDialog(pContext, pTitleId, pMsgContentResId, R.string.alert_delete_button, R.string.alert_cancel_button, FLAG_RESOURCE_NULL, pDialogCallBack);
    }

    public static android.support.v7.app.AlertDialog getOkDeleteAlertDialog(Context pContext, int pTitleId, String pMsgContentResId, final MCCDialogCallBack pDialogCallBack) {
        return getAlertDialog(pContext, pTitleId, pMsgContentResId, R.string.alert_delete_button, R.string.alert_cancel_button, FLAG_RESOURCE_NULL, pDialogCallBack);
    }

    public static android.support.v7.app.AlertDialog getOkCancelAlertDialog(Context pContext, int pTitleId, String pMsgContentResId, final MCCDialogCallBack pDialogCallBack) {
        return getAlertDialog(pContext, pContext.getString(pTitleId), pMsgContentResId, R.string.alert_ok_button, R.string.alert_cancel_button, FLAG_RESOURCE_NULL, pDialogCallBack);
    }

    public static android.support.v7.app.AlertDialog getOkCancelAlertDialog(Context pContext, String pMsgContentResContent, final MCCDialogCallBack pDialogCallBack) {
        return getAlertDialog(pContext, FLAG_RESOURCE_EMPTY, pMsgContentResContent, R.string.alert_ok_button, R.string.alert_cancel_button, FLAG_RESOURCE_NULL, pDialogCallBack);
    }

    public static android.support.v7.app.AlertDialog getYesNoAlertDialog(Context pContext, String pMsgContentResContent, final MCCDialogCallBack pDialogCallBack) {
        return getAlertDialog(pContext, FLAG_RESOURCE_EMPTY, pMsgContentResContent, R.string.alert_yes_button, R.string.alert_no_button, FLAG_RESOURCE_NULL, pDialogCallBack);
    }

    public static android.support.v7.app.AlertDialog getYesNoAlertDialog(Context pContext, String pTitle, String pMsgContentResContent, final MCCDialogCallBack pDialogCallBack) {
        return getAlertDialog(pContext, pTitle, pMsgContentResContent, R.string.alert_yes_button, R.string.alert_no_button, FLAG_RESOURCE_NULL, pDialogCallBack);
    }

    public static android.support.v7.app.AlertDialog getYesNoAlertDialog(Context pContext, int pTitleId, int pMsgContentResId, final MCCDialogCallBack pDialogCallBack) {
        return getAlertDialog(pContext, pTitleId, pMsgContentResId, R.string.alert_yes_button, R.string.alert_no_button, FLAG_RESOURCE_NULL, pDialogCallBack);
    }

    public static android.support.v7.app.AlertDialog getSaveCancelAlertDialog(Context pContext, String pTitle, String pMsgContentResContent, final MCCDialogCallBack pDialogCallBack) {
        return getAlertDialog(pContext, pTitle, pMsgContentResContent, R.string.alert_save_button, R.string.alert_cancel_button, FLAG_RESOURCE_NULL, pDialogCallBack);
    }

    public static android.support.v7.app.AlertDialog getSaveCancelAlertDialog(Context pContext, int pTitleId, int pMsgContentResId, final MCCDialogCallBack pDialogCallBack) {
        return getAlertDialog(pContext, pTitleId, pMsgContentResId, R.string.alert_save_button, R.string.alert_cancel_button, FLAG_RESOURCE_NULL, pDialogCallBack);
    }


    public static android.support.v7.app.AlertDialog getOkCancelAlertDialog(Context pContext, int pMsgContentResId) {
        return getAlertDialog(pContext, FLAG_RESOURCE_NULL, pMsgContentResId, R.string.alert_ok_button, R.string.alert_cancel_button, FLAG_RESOURCE_NULL, new SimpleDialogDismissListener());
    }

    /**
     * Show alert dialog with ok button
     *
     * @param pContext
     * @param pMsgContentResId
     * @param pDialogCallBack
     * @return
     */
    public static AlertDialog getOkAlertDialog(Context pContext, int pMsgContentResId, final MCCDialogCallBack pDialogCallBack) {
        return getAlertDialog(pContext, FLAG_RESOURCE_NULL, pMsgContentResId, R.string.alert_ok_button, FLAG_RESOURCE_NULL, FLAG_RESOURCE_NULL, pDialogCallBack);
    }

    public static AlertDialog getOkAlertDialog(Context pContext, String pMsgContent, final MCCDialogCallBack pDialogCallBack) {
        return getAlertDialog(pContext, FLAG_RESOURCE_EMPTY, pMsgContent, R.string.alert_ok_button, FLAG_RESOURCE_NULL, FLAG_RESOURCE_NULL, pDialogCallBack);
    }

    public static AlertDialog getOkAlertDialog(Context pContext, int pTitleId, int pMsgContentResId) {
//        return getAlertDialog(pContext, pTitleId,pMsgContentRes, R.string.alert_ok_button, new SimpleDialogDismissListener());
        return getAlertDialog(pContext, pTitleId, pMsgContentResId, R.string.alert_ok_button, FLAG_RESOURCE_NULL, FLAG_RESOURCE_NULL, new SimpleDialogDismissListener());
    }

    public static AlertDialog getOkAlertDialog(Context pContext, int pTitleId, int pMsgContentResId, final MCCDialogCallBack pDialogCallBack) {
        return getAlertDialog(pContext, pTitleId, pMsgContentResId, R.string.alert_ok_button, FLAG_RESOURCE_NULL, FLAG_RESOURCE_NULL, pDialogCallBack);
    }

    public static AlertDialog getOkAlertDialog(Context pContext, int pTitleId, String pMsgContentResId, final MCCDialogCallBack pDialogCallBack) {
        return getAlertDialog(pContext, pTitleId, pMsgContentResId, R.string.alert_ok_button, FLAG_RESOURCE_NULL, FLAG_RESOURCE_NULL, pDialogCallBack);
    }

    public static AlertDialog getOkAlertDialog(Context pContext, String pTitle, String pMsgContent) {
        return getAlertDialog(pContext, pTitle, pMsgContent, R.string.alert_ok_button, FLAG_RESOURCE_NULL, FLAG_RESOURCE_NULL, new SimpleDialogDismissListener());
    }

    public static AlertDialog getOkAlertDialog(Context pContext, int pTitleId, String pMsgContent) {
        return getAlertDialog(pContext, pTitleId, pMsgContent, R.string.alert_ok_button, new SimpleDialogDismissListener());
    }

    public static AlertDialog getOkAlertDialog(Context pContext, int pMsgContentResId) {
        return getAlertDialog(pContext, FLAG_RESOURCE_NULL, pMsgContentResId, R.string.alert_ok_button, FLAG_RESOURCE_NULL, FLAG_RESOURCE_NULL, new SimpleDialogDismissListener());
    }

    public static AlertDialog getOkAlertDialog(Context pContext, String pMsgContentRes) {
        return getAlertDialog(pContext, FLAG_RESOURCE_EMPTY, pMsgContentRes, R.string.alert_ok_button, FLAG_RESOURCE_NULL, FLAG_RESOURCE_NULL, new SimpleDialogDismissListener());
    }

    public AlertDialog getSingleSelectLisAlertDialog(final Context pContext, int pTitleResId, final CharSequence[] pListItems, int pDefaultValueIndex, final ISequenceAction action) {
        AlertDialog.Builder builder = new AlertDialog.Builder(pContext);
        builder.setSingleChoiceItems(pListItems, pDefaultValueIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String selectedItem = pListItems[i].toString();
                action.sequenceAction(selectedItem, i);
                dialogInterface.dismiss();
            }
        });
        builder.setTitle(pTitleResId);
        return builder.create();
    }

    public AlertDialog getSingleSelectLisAlertDialog(final Context pContext, String pTitle, final CharSequence[] pListItems, int pDefaultValueIndex, final ISequenceAction action) {
        LayoutInflater inflater = (LayoutInflater) pContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inputTextDialog = inflater.inflate(R.layout.layout_title_dialog, null);
        TextView tvTitle = (TextView) inputTextDialog.findViewById(R.id.message);
        tvTitle.setText(pTitle);
        AlertDialog.Builder builder = new AlertDialog.Builder(pContext, R.style.MessageDialogTheme);
        builder.setSingleChoiceItems(pListItems, pDefaultValueIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String selectedItem = pListItems[i].toString();
                action.sequenceAction(selectedItem, i);
                dialogInterface.dismiss();
            }
        });
        //builder.setTitle(pTitle);
        builder.setCustomTitle(inputTextDialog);
        return builder.create();
    }

    public AlertDialog getMultiSelectListAlertDialog(final Context pContext, String pTitle,
                                                     final CharSequence[] pListItems, final boolean[] checkedItems,
                                                     final ISequenceMultiChoiceAction action) {
        LayoutInflater inflater = (LayoutInflater) pContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inputTextDialog = inflater.inflate(R.layout.layout_title_dialog, null);
        TextView tvTitle = (TextView) inputTextDialog.findViewById(R.id.message);
        tvTitle.setText(pTitle);
        AlertDialog.Builder builder = new AlertDialog.Builder(pContext, R.style.MessageDialogTheme);

        builder.setMultiChoiceItems(pListItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                checkedItems[i] = b;
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                action.sequenceAction(checkedItems);
                dialog.dismiss();
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        //builder.setTitle(pTitle);
        builder.setCustomTitle(inputTextDialog);
        return builder.create();
    }

    public AlertDialog getSingleSelectListAlertDialog(final Context context, String pTitle, final ListAdapter adapter, final ISequenceActionWithObject action) {
        return new AlertDialog.Builder(context)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int index) {
                        Object selectedItem = adapter.getItem(index);
                        action.sequenceAction(selectedItem, index);
                        dialog.dismiss();
                    }
                })
                .setTitle(pTitle)
                .setCancelable(false)
                .show();
    }

    public static class SimpleDialogDismissListener implements MCCDialogCallBack {

        /**
         * Handle event click button on dialog
         *
         * @param pAlertDialog
         * @param pDialogType
         */
        @Override
        public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
            pAlertDialog.dismiss();
        }
    }

    public static void showProgressDialog(Context context) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.setMessage(context.getString(R.string.loading));
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    public static void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }


    public static AlertDialog.Builder createDialog(Context context, int titleResourse, int contentResourse) {
        return new AlertDialog.Builder(context, R.style.AlertDialogTheme)
                .setTitle(context.getResources().getString(titleResourse))
                .setMessage(context.getResources().getString(contentResourse))
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    public AlertDialog.Builder createDialog(Context context, int titleResourse, String content) {
        return new AlertDialog.Builder(context, R.style.AlertDialogTheme)
                .setTitle(context.getResources().getString(titleResourse))
                .setMessage(content)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    public static AlertDialog.Builder createDialog(Context context, int contentId) {
        return new AlertDialog.Builder(context, R.style.AlertDialogTheme)
                .setMessage(context.getResources().getString(contentId))
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    public static AlertDialog.Builder createDialog(Context context, String title, String content) {
        return new AlertDialog.Builder(context, R.style.AlertDialogTheme)
                .setTitle(title)
                .setMessage(content)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    public static void showGeneralError(Context context) {
        createDialog(context, context.getString(R.string.error), context.getString(R.string.general_api_error)).show();
    }

    public static void showErrorByErrorCode(Context context, int status) {
        switch (status) {
            case ApiConstant.ERROR_LOGIN_DENIED:
                createDialog(context, R.string.error, R.string.setting_user_password_incorrect).show();
                break;
            case ApiConstant.ERROR_LICENSE_REQUIRED:
                createDialog(context, R.string.error, R.string.general_api_error).show();
                break;
            case ApiConstant.ERROR_NOT_AUTHORIZED:
                createDialog(context, R.string.error, R.string.general_api_error).show();
                break;
            case ApiConstant.ERROR_NOT_FOUND:
                createDialog(context, R.string.error, R.string.general_api_error).show();
                break;
            case ApiConstant.ERROR_ROUTE_UNKNOWN:
                createDialog(context, R.string.error, R.string.general_api_error).show();
                break;
            case ApiConstant.ERROR_CONFLICT:
                createDialog(context, R.string.error, R.string.account_exists).show();
                break;
            case ApiConstant.ERROR_MISSING_DATA:
                createDialog(context, R.string.error, R.string.general_api_error).show();
                break;
            case ApiConstant.ERROR_TOKEN_INVALID:
                createDialog(context, R.string.error, R.string.general_api_error).show();
                break;
            case ApiConstant.ERROR_TOKEN_EXPIRED:
                createDialog(context, R.string.error, R.string.general_api_error).show();
                break;
            case ApiConstant.ERROR_INVALID_SIGNATURE:
                createDialog(context, R.string.error, R.string.general_api_error).show();
                break;


           /* case ApiConstant.ERROR_USER_DOESNOT_EXIST:
                createDialog(context, R.string.wrong_user_account_msg).show();
                break;
            case ApiConstant.ERROR_WRONG_PASSWORD:

                break;
            case ApiConstant.ERROR_UNKNOWN:
                createDialog(context, R.string.general_api_error).show();
                break;*/
            default:
                createDialog(context, R.string.general_api_error).show();
        }
    }
}

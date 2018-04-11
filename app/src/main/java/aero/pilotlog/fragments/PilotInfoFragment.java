package aero.pilotlog.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.common.MccEnum;
import aero.pilotlog.databases.entities.Pilot;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.interfaces.IPilotCallback;
import aero.pilotlog.models.FlightModel;
import aero.pilotlog.tasks.ImageProcessTask;
import aero.pilotlog.utilities.DateTimeUtils;
import aero.pilotlog.utilities.KeyboardUtils;
import aero.pilotlog.utilities.PhotoUtils;
import aero.pilotlog.utilities.ScreenUtils;
import aero.pilotlog.utilities.StorageUtils;
import aero.pilotlog.utilities.Utils;
import aero.pilotlog.widgets.CircleImageView;
import aero.pilotlog.widgets.FloatingActionMenu;
import aero.pilotlog.widgets.ItemInputTextWithIcon;
import aero.pilotlog.widgets.MccDialog;
import aero.pilotlog.widgets.SubActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tuan.na on 7/22/2015.
 * Pilot info screen
 */
public class PilotInfoFragment extends BaseMCCFragment {
    @Bind(R.id.sv_pilot_info)
    ScrollView svScrollView;
    @Bind(R.id.iv_pilot_image)
    CircleImageView mIvPilotImage;
    @Bind(R.id.tvEmployeeID)
    TextView mTVPilotId;
    @Bind(R.id.tv_pilot_name)
    TextView mTVPilotName;
    @Bind(R.id.tv_pilot_company)
    TextView mTVPilotCompany;
    @Bind(R.id.tv_pilot_email)
    TextView mTvPilotEmail;
    @Bind(R.id.tv_pilot_phone_number)
    TextView mTvPilotPhoneNumber;
    @Bind(R.id.ibLeft)
    ImageButton mIbLeft;
    @Bind(R.id.tvTitle)
    TextView mTvTitle;
    @Bind(R.id.tvNumber)
    TextView mTvNumber;
    @Bind(R.id.rl_fullscreen)
    RelativeLayout mRlFullScreen;
    @Bind(R.id.iv_pilot_image_fullscreen)
    ImageView mIvImageFullScreen;
    @Bind(R.id.rlBackIcon)
    RelativeLayout mHeaderRlBack;
    @Bind(R.id.ibMenu)
    ImageButton mHeaderIbMenu;
    @Bind(R.id.lnEdit)
    LinearLayout lnEdit;
    @Bind(R.id.iv_image_menu)
    CircleImageView mImageView;
    @Bind(R.id.lnHeader)
    LinearLayout lnHeader;
    @Bind(R.id.edt_item_pilot_history)
    ItemInputTextWithIcon edtPilotHistory;
    @Bind(R.id.ln_history)
    LinearLayout lnHistory;
    @Bind(R.id.notes)
    TextView tvNotes;
    @Bind(R.id.lnCertificate)
    LinearLayout lnCertificate;
    @Bind(R.id.tvCertificate)
    TextView tvCertificate;
    @Bind(R.id.tv_pilot_facebook)
    TextView tvFacebook;
    @Bind(R.id.tv_pilot_linked_in)
    TextView tvLinkedIn;

    private Pilot mPilot;
    private byte[] mPilotCode;
    private boolean mHasImage;
    private IPilotCallback mIPilotCallback;
    private Bundle mBundle;
    private FloatingActionMenu mImageMenu;
    private DatabaseManager mDatabaseManager;


    public void setIPilotCallback(IPilotCallback pIPilotCallback) {
        this.mIPilotCallback = pIPilotCallback;
    }

    @Override
    protected void onCreateFragment(View pContentView) {
        ButterKnife.bind(this, mViewContainer);
        mDatabaseManager = DatabaseManager.getInstance(mActivity);
        mBundle = getArguments();
        initInfoPilot();

    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_pilot_info;
    }

    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getFooterResId() {
        return 0;
    }

    private void setImageMenu(SubActionButton buttonCamera, SubActionButton buttonGallery, SubActionButton buttonDelete, int radius) {
        mImageMenu = new FloatingActionMenu.Builder(mActivity)
                .addSubActionView(buttonCamera)
                .addSubActionView(buttonGallery)
                .addSubActionView(buttonDelete)
                .setStartAngle(360)
                .setEndAngle(450)
                .setRadius(radius)
                .attachTo(mImageView)
                .build();
    }


    public void initInfoPilot() {

        mPilotCode = mBundle.getByteArray(MCCPilotLogConst.PILOT_CODE_KEY);
        //mPilotName = pBundle.getString(MCCPilotLogConst.PILOT_NAME_KEY);

        //set header views
        mHeaderIbMenu.setVisibility(View.GONE);
        if (!isTablet()) {
            mTvTitle.setText("Back");
            mIbLeft.setVisibility(View.VISIBLE);
        }else {
            mTvTitle.setText("");
            //Disable header back icon for tablet
            mHeaderRlBack.setClickable(false);
        }
        mTvNumber.setVisibility(View.GONE);

        //PL-487
        String mPilotInfoMode = mBundle.getString(MCCPilotLogConst.PILOT_INFO_MODE, MCCPilotLogConst.STRING_EMPTY);
        if (!TextUtils.isEmpty(mPilotInfoMode)) {
            mImageView.setVisibility(View.GONE);
            mIbLeft.setVisibility(View.VISIBLE);
            mHeaderRlBack.setClickable(true);
        }
        //End PL-487

        DatabaseManager mDbManager = DatabaseManager.getInstance(mActivity);
        if (mPilotCode != null) {
            mPilot = mDbManager.getPilotByCode(mPilotCode);
        }
        if (mPilot != null) {
            //PL-201
            //set circular button
            SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(mActivity);
            ImageView iconTakePhoto = new ImageView(mActivity);
            ImageView iconPickPhoto = new ImageView(mActivity);
            ImageView iconDeletePhoto = new ImageView(mActivity);
            // set icon
            iconTakePhoto.setImageDrawable(getResources().getDrawable(R.drawable.camera));
            iconPickPhoto.setImageDrawable(getResources().getDrawable(R.drawable.gallery));
            iconDeletePhoto.setImageDrawable(getResources().getDrawable(R.drawable.clear));

            SubActionButton buttonCamera = rLSubBuilder.setContentView(iconTakePhoto).build();
            buttonCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mImageMenu.close(true);
                        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                        , 9);
                            }
                            return;
                        }
                        new PhotoUtils(PilotInfoFragment.this).takePhotoFromCameraForFragment(mActivity, imageFileName());
                    } catch (ActivityNotFoundException anfe) {
                        anfe.printStackTrace();
                        Utils.showToast(mActivity, R.string.toast_camera_not_found);
                    }
                }
            });
            SubActionButton buttonGallery = rLSubBuilder.setContentView(iconPickPhoto).build();
            buttonGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mImageMenu.close(true);
                    if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                    , 8);
                        }
                        return;
                    }
                    new PhotoUtils(PilotInfoFragment.this).pickPhotoFromGalleryForFragment();
                }
            });
            SubActionButton buttonDelete = rLSubBuilder.setContentView(iconDeletePhoto).build();
            iconDeletePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mImageMenu.close(true);
                    MccDialog.getAlertDialog(mActivity, R.string.delete_picture, R.string.confirm_message_delete_pilot_picture, R.string.alert_cancel_button, new MccDialog.MCCDialogCallBack() {
                        @Override
                        public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                            if (pDialogType == DialogInterface.BUTTON_POSITIVE) {
                                new File(StorageUtils.getStorageRootFolder(mActivity), imageFileName()).delete();
                                mIvPilotImage.setImageResource(R.drawable.ic_camera_transparent);
                                deletePilotImage();
                                mHasImage = false;
                            }
                        }
                    }).show();
                }
            });

            ScreenUtils.getNavigationBarHeight(mActivity);
            ScreenUtils.getScreenDimension(mActivity);

            //1280 800
            if ((ScreenUtils.SCREEN_WIDTH == 1920 && ScreenUtils.SCREEN_HEIGHT == 1200) || (ScreenUtils.SCREEN_WIDTH == 1080 && ScreenUtils.SCREEN_HEIGHT == 1920)
                    || (ScreenUtils.SCREEN_WIDTH == 1200 && ScreenUtils.SCREEN_HEIGHT == 1920) || (ScreenUtils.SCREEN_WIDTH == 1600 && ScreenUtils.SCREEN_HEIGHT == 2560)
                    ) {
                setImageMenu(buttonCamera, buttonGallery, buttonDelete, 180);
            } else if ((ScreenUtils.SCREEN_WIDTH == 720 || ScreenUtils.SCREEN_WIDTH == 768) && ScreenUtils.SCREEN_HEIGHT == 1280) {
                setImageMenu(buttonCamera, buttonGallery, buttonDelete, 130);
            } else if (ScreenUtils.SCREEN_WIDTH == 2560 && ScreenUtils.SCREEN_HEIGHT == 1600) {
                setImageMenu(buttonCamera, buttonGallery, buttonDelete, 160);
            } else if (ScreenUtils.SCREEN_WIDTH == 800 && ScreenUtils.SCREEN_HEIGHT == 1280) {
                setImageMenu(buttonCamera, buttonGallery, buttonDelete, 120);
            } else if (ScreenUtils.SCREEN_HEIGHT > 2000) { //PL-1067
                setImageMenu(buttonCamera, buttonGallery, buttonDelete, 200);
            } else {
                setImageMenu(buttonCamera, buttonGallery, buttonDelete, 100);
            }
            svScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    mImageMenu.close(true);
                }
            });

            //mTvTitle.setText("Back");
            if (!TextUtils.isEmpty(mPilot.getPilotEMail())) {
                mTvPilotEmail.setText(mPilot.getPilotEMail());
            }
            if (!TextUtils.isEmpty(mPilot.getPilotPhone())) {
                mTvPilotPhoneNumber.setText(mPilot.getPilotPhone());
            }
            if (!TextUtils.isEmpty(mPilot.getNotes()))
                tvNotes.setText(mPilot.getNotes());

            if (!TextUtils.isEmpty(mPilot.getPilotRef())) {
                mTVPilotId.setText(mPilot.getPilotRef());
            }
            if (!TextUtils.isEmpty(mPilot.getPilotName())) {
                mTVPilotName.setText(mPilot.getPilotName().toUpperCase());
            }
            if (!TextUtils.isEmpty(mPilot.getCompany())) {
                mTVPilotCompany.setText(mPilot.getCompany());
            }
            if (!TextUtils.isEmpty(mPilot.getFacebook())) {
                tvFacebook.setText(mPilot.getFacebook());
            }
            if (!TextUtils.isEmpty(mPilot.getLinkedIn())) {
                tvLinkedIn.setText(mPilot.getLinkedIn());
            }
            if (!TextUtils.isEmpty(mPilot.getCertificate())) {
                lnCertificate.setVisibility(View.VISIBLE);
                tvCertificate.setText(mPilot.getCertificate());
            }
            lnEdit.setVisibility(View.VISIBLE);
            edtPilotHistory.setDescription("None");

        }
        showPilotImage(mIvPilotImage);
        lnHeader.post(new Runnable() {
            public void run() {
                if(isVisible()){
                    int headerHeight = lnHeader.getHeight();
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            (int) getResources().getDimension(R.dimen.profile_image_size),
                            (int) getResources().getDimension(R.dimen.profile_image_size)
                    );
                    int imageHeight = mIvPilotImage.getHeight();
                    params.setMargins(0, headerHeight - imageHeight / 2 - (imageHeight % 2) / 2,
                            (int) getResources().getDimension(R.dimen.margin_right_profile_image), 0);
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    mIvPilotImage.setLayoutParams(params);
                    params = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(0, headerHeight, 0, 0);
                    svScrollView.setLayoutParams(params);
                    mIvPilotImage.setVisibility(View.VISIBLE);
                }
            }
        });
        initHistory();

    }


    @Nullable
    @OnClick({/*R.id.iv_gallery,*/ R.id.iv_camera,/* R.id.iv_delete_pilot_image,*/ R.id.rlBackIcon,/* R.id.ibMenu,*/ R.id.iv_pilot_image, R.id.iv_pilot_image_fullscreen,
            R.id.iv_call, R.id.iv_sms, R.id.iv_mail, R.id.iv_export_contact, R.id.tv_action_bar_right, R.id.tv_pilot_phone_number, R.id.tv_pilot_email,
            R.id.notes, R.id.ln_notes, R.id.ivFacebook,R.id.ln_history})
    public void onClick(View view) {
        if (mImageMenu != null) {
            mImageMenu.close(true);
        }
        switch (view.getId()) {
         /*   case R.id.iv_gallery:
                new PhotoUtils(this).pickPhotoFromGalleryForFragment();
                break;
            case R.id.iv_camera:
                try {
                    new PhotoUtils(PilotInfoFragment.this).takePhotoFromCameraForFragment(mActivity, imageFileName());
                } catch (ActivityNotFoundException anfe) {
                    anfe.printStackTrace();
                    Utils.showToast(mActivity, R.string.toast_camera_not_found);
                }
                break;
            case R.id.iv_delete_pilot_image:
                MccDialog.getAlertDialog(mActivity, R.string.delete_picture, R.string.confirm_message_delete_pilot_picture, R.string.alert_cancel_button, new MccDialog.MCCDialogCallBack() {
                    @Override
                    public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                        if (pDialogType == DialogInterface.BUTTON_POSITIVE) {
                            new File(StorageUtils.getStorageRootFolder(mActivity), imageFileName()).delete();
                            mIvPilotImage.setImageResource(R.drawable.ic_camera_transparent);
                            deletePilotImage();
                            mHasImage = false;
                        }
                    }
                }).show();
                break;*/
            case R.id.rlBackIcon:
                if (mIPilotCallback != null && mPilot != null) {
                    mIPilotCallback.selectPilot(mPilot);
                }

                finishFragment();
                break;
            case R.id.iv_pilot_image:
                mRlFullScreen.setVisibility(View.VISIBLE);
                mViewHeaderContainer.setVisibility(View.GONE);
                mFooterContainer.setVisibility(View.GONE);
                if (mHasImage) {
                    showPilotImage(mIvImageFullScreen);
                } else {
                    mIvImageFullScreen.setImageResource(R.drawable.ic_camera_transparent);
                }
                break;
            case R.id.iv_pilot_image_fullscreen:
                mViewHeaderContainer.setVisibility(View.VISIBLE);
                mFooterContainer.setVisibility(View.VISIBLE);
                mRlFullScreen.setVisibility(View.GONE);
                break;
            //case R.id.btn_bottom1: //edit
            case R.id.tv_action_bar_right:
                Bundle bundleEdit = new Bundle();
                bundleEdit.putByteArray(MCCPilotLogConst.PILOT_CODE_KEY, mPilotCode);
                bundleEdit.putInt(MCCPilotLogConst.PILOT_ADD_EDIT_VIEW_TYPE, MCCPilotLogConst.PILOT_EDIT_VIEW);
                PilotAddEditFragment pilotAddEditFragment = new PilotAddEditFragment();
                    replaceFragmentTablet(isTablet()? R.id.rightContainerFragment:R.id.fragmentMain, pilotAddEditFragment, bundleEdit, FLAG_ADD_STACK);
                break;
            //case R.id.btn_bottom2:
            case R.id.iv_sms://sms
                if (mPilot != null && !TextUtils.isEmpty(mPilot.getPilotPhone())) {
                    sendSMS(mPilot.getPilotPhone());
                }
                break;
            //case R.id.btn_bottom3: //email
            case R.id.iv_mail:
                if (mPilot != null && !TextUtils.isEmpty(mPilot.getPilotEMail())) {
                    sendEmail(mPilot.getPilotEMail());
                }
                break;
            //case R.id.btn_bottom4: //call
            case R.id.iv_call:
                if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}
                                , 10);
                    }
                    return;
                }
                if (mPilot != null && !TextUtils.isEmpty(mPilot.getPilotPhone())) {
                    call(mPilot.getPilotPhone());
                }
                break;
            case R.id.ivFacebook:
                Intent facebookIntent = getOpenFacebookIntent(tvFacebook.getText().toString());
                startActivity(facebookIntent);
                break;
            //case R.id.btn_bottom5: //export to contact
            case R.id.iv_export_contact:
                MccDialog.getAlertDialog(mActivity, R.string.export_contact_dialog_title, R.string.export_contact_dialog_message,
                        R.string.alert_yes_button, R.string.alert_no_button, MccDialog.FLAG_RESOURCE_NULL, new MccDialog.MCCDialogCallBack() {
                            @Override
                            public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                                if (pDialogType == DialogInterface.BUTTON_POSITIVE) {
                                    new AsyncTask<Void, Void, Boolean>() {

                                        @Override
                                        protected void onPreExecute() {
                                            super.onPreExecute();
                                            Utils.showToast(mActivity, "Processing ...");
                                        }

                                        @Override
                                        protected Boolean doInBackground(Void... voids) {
                                            return exportToContact();
                                        }

                                        @Override
                                        protected void onPostExecute(Boolean aBoolean) {
                                            super.onPostExecute(aBoolean);
                                            Utils.showToast(mActivity, aBoolean ? getString(R.string.pilot_export_success) : getString(R.string.pilot_export_fail));
                                        }
                                    }.execute();
                                }
                            }
                        }).show();
                break;
            case R.id.tv_pilot_phone_number:
                //ivCopyPhone.setVisibility(View.VISIBLE);
                showCopyToClipboard(mTvPilotPhoneNumber);
                break;
            case R.id.tv_pilot_email:
                showCopyToClipboard(mTvPilotEmail);
                break;
            case R.id.ln_notes:
            case R.id.notes:
                displayTextBox(tvNotes);
                break;
            case R.id.ln_history:
                Bundle bundle = new Bundle();
                bundle.putByteArray(MCCPilotLogConst.PILOT_CODE_KEY, mPilotCode);
                replaceFragment(isTablet()? R.id.rightContainerFragment:R.id.fragmentMain, LogbooksListFragment.class, bundle, FLAG_ADD_STACK);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    if (mPilot != null && !TextUtils.isEmpty(mPilot.getPilotPhone())) {
                        call(mPilot.getPilotPhone());
                    }
                } else {
                }

                break;
            case 9:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        new PhotoUtils(PilotInfoFragment.this).takePhotoFromCameraForFragment(mActivity, imageFileName());
                    } catch (ActivityNotFoundException anfe) {
                        anfe.printStackTrace();
                        Utils.showToast(mActivity, R.string.toast_camera_not_found);
                    }
                } else {
                }

                break;
            case 8:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mImageMenu.close(true);
                    new PhotoUtils(PilotInfoFragment.this).pickPhotoFromGalleryForFragment();

                } else {
                }

                break;
            default:
                break;
        }
    }

    private void displayTextBox(final TextView currentTextView) {
        final InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);

        String titleDialog;
        titleDialog = "EDIT NOTES";
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inputTextDialog = inflater.inflate(R.layout.dialog_input_text, null);
        final EditText inputText = (EditText) inputTextDialog.findViewById(R.id.input);
        inputText.setGravity(Gravity.TOP);
        inputText.setHeight(300);
        inputText.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(254);
        inputText.setFilters(fArray);
        if (!TextUtils.isEmpty(currentTextView.getText().toString())) {
            inputText.setText(currentTextView.getText().toString());
            inputText.setSelection(currentTextView.getText().length() - 1);
        }
        inputText.selectAll();
        //KeyboardUtils.showKeyboard(inputText);
        TextView tvTitle = (TextView) inputTextDialog.findViewById(R.id.message);
        tvTitle.setText(titleDialog);
        new android.support.v7.app.AlertDialog.Builder(mActivity, R.style.MessageDialogTheme)
                //.setTitle(titleDialog)
                .setView(inputTextDialog)
                .setNegativeButton(R.string.sign_text_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        inputText.clearFocus();
                        if(KeyboardUtils.isKeyboardShow(mActivity)){
                            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        }
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.alert_ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        inputText.clearFocus();
                        if(KeyboardUtils.isKeyboardShow(mActivity)){
                            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        }
                        currentTextView.setText(inputText.getText().toString());
                        if (mPilot != null) {
                            mPilot.setNotes(inputText.getText().toString());
                            mDatabaseManager.insertPilot(mPilot);
                        }
                        dialog.dismiss();
                    }
                }).show();
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void showCopyToClipboard(final TextView textView) {
        textView.setBackgroundColor(getResources().getColor(R.color.gray_2));
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.layout_alert_copy_clipboard, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setView(layout);
        final AlertDialog dialog = builder.create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                textView.setBackgroundColor(getResources().getColor(R.color.background_white));
            }
        });
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.TOP | Gravity.LEFT;
        int x = textView.getRootView().getWidth() / 2 - 160;
        int y = getRelativeTop(textView) - 4 * textView.getHeight();
        wmlp.x = x;   //x position
        wmlp.y = y;   //y position
        dialog.show();
        dialog.getWindow().setLayout(320, 200);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        RelativeLayout lnCopy = (RelativeLayout) layout.findViewById(R.id.ln_copy_clipboard);
        TextView tvCopy = (TextView) layout.findViewById(R.id.tv_copy);
        ImageView ivCopy = (ImageView) layout.findViewById(R.id.iv_copy);
        /*RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,0,ivCopy.getHeight()/2 - tvCopy.getHeight()/2 + ivCopy.getHeight()/4);
        Log.d("iv Height", String.valueOf(ivCopy.getHeight()));
        Log.d("TV Height",String.valueOf(tvCopy.getHeight()));
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        tvCopy.setLayoutParams(params);*/
        if (lnCopy != null) {
            lnCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    ClipboardManager clipboard = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(textView.getText(), textView.getText());
                    clipboard.setPrimaryClip(clip);
                }
            });
        }

    }

    private int getRelativeTop(View myView) {
        if (myView.getParent() == myView.getRootView())
            return myView.getTop();
        else
            return myView.getTop() + getRelativeTop((View) myView.getParent());
    }

    private boolean deletePilotImage() {
        //mPilot.setSyncPict(SyncConst.SYNC_PICT_NO);
       /* mPilot.setPC(SyncConst.PC);
        DatabaseManager.getInstance(mActivity).updatePilot(mPilot);*/

        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PhotoUtils.PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            final Uri mImageUri = data.getData();
            ImageProcessTask mTask = new ImageProcessTask(mActivity, imageFileName(), true) {
                @Override
                public void handleBitmap(Bitmap bitmap) {
                    changeToPortraitLayout(new File(PhotoUtils.getRealPathFromURI(mActivity, mImageUri)));
                    mIvPilotImage.setImageBitmap(bitmap);
                    mHasImage = true;
                }
            };
            mTask.execute(new File(PhotoUtils.getRealPathFromURI(mActivity, mImageUri)));

            //mPilot.setSyncPict(SyncConst.SYNC_PICT_YES);
            /*mPilot.setPC(SyncConst.PHONE);
            DatabaseManager.getInstance(mActivity).updatePilot(mPilot);*/
        } else if (requestCode == PhotoUtils.TAKE_IMAGE && resultCode == Activity.RESULT_OK) {
            final File f = new File(StorageUtils.getStorageRootFolder(mActivity), imageFileName());
            if (!f.exists()) {
                return;
            }
            ImageProcessTask mTask = new ImageProcessTask(mActivity, imageFileName(), false) {
                @Override
                public void handleBitmap(Bitmap bitmap) {
                    changeToPortraitLayout(f);
                    mIvPilotImage.setImageBitmap(bitmap);
                    mHasImage = true;
                }
            };
            mTask.execute(f);
            PhotoUtils.saveTakenImageToGallery(mActivity, Uri.fromFile(f));

            //mPilot.setSyncPict(SyncConst.SYNC_PICT_YES);
            /*mPilot.setPC(SyncConst.PHONE);
            DatabaseManager.getInstance(mActivity).updatePilot(mPilot);*/
        }
        System.gc();
    }

    /**
     * Return image file name depends on Sync ID
     *
     * @return filename
     */
    private String imageFileName() {
//        return String.format(isSyncV4 ? MCCPilotLogConst.PILOT_IMAGE_FILE_NAME_V4 : MCCPilotLogConst.PILOT_IMAGE_FILE_NAME_V3, mPilotCode);
        //Log.d("Pilot image",String.format(MCCPilotLogConst.PILOT_IMAGE_FILE_NAME_V4, Utils.getGUIDFromByteArray(mPilotCode)));
        return String.format(MCCPilotLogConst.PILOT_IMAGE_FILE_NAME_V4, Utils.getGUIDFromByteArray(mPilotCode));
    }

    /**
     * Show pilot image when launch page
     *
     * @param viewToShow image view
     */
    private void showPilotImage(final ImageView viewToShow) {
//        final File f = new File(StorageUtils.getStorageRootFolder(mActivity), String.format(isSyncV4 ? MCCPilotLogConst.PILOT_IMAGE_FILE_NAME_V4
//                : MCCPilotLogConst.PILOT_IMAGE_FILE_NAME_V3, mPilotCode));
        final File f = new File(StorageUtils.getStorageRootFolder(mActivity), String.format(MCCPilotLogConst.PILOT_IMAGE_FILE_NAME_V4, Utils.getGUIDFromByteArray(mPilotCode)));
        if (f.exists()) {
            mHasImage = true;
            //mPilot.setSyncPict(SyncConst.SYNC_PICT_YES);//2015-12-30 TuanPV comment
            //DatabaseManager.getInstance(mActivity).updatePilot(mPilot);
            //mPbLoadingImage.setVisibility(View.VISIBLE);
            ImageProcessTask mTask = new ImageProcessTask(mActivity) {
                @Override
                public void handleBitmap(Bitmap bitmap) {
                    //mPbLoadingImage.setVisibility(View.GONE);
                    if (bitmap != null) {
                        viewToShow.setImageBitmap(bitmap);
                    } else {
                        viewToShow.setImageResource(R.drawable.ic_camera_transparent);
                    }
                }
            };
            mTask.execute(f);
        } else {
            viewToShow.setImageResource(R.drawable.ic_camera_transparent);
        }
    }

    /**
     * Send sms
     *
     * @param pPhoneNumber phone number
     */
    private void sendSMS(String pPhoneNumber) {
        //Change due to PL-436
//        pPhoneNumber = pPhoneNumber.replaceAll("[^\\d]", "");
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", pPhoneNumber, null)));
    }

    /**
     * Call Phone number
     *
     * @param pPhoneNumber phone number
     */
    private void call(String pPhoneNumber) {
        //Change due to PL-436
//        pPhoneNumber = pPhoneNumber.replaceAll("[^\\d]", "");
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + pPhoneNumber));
        startActivity(callIntent);
    }

    /**
     * Send email
     *
     * @param pEmailAddress email address
     */
    private void sendEmail(String pEmailAddress) {
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{pEmailAddress});
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    public void changeToPortraitLayout(File pFile) {
        if (PhotoUtils.getPhotoOrientation(pFile.getAbsolutePath())) {
            replaceContentView(R.layout.fragment_pilot_info);
            ButterKnife.bind(this, mViewContainer);
            initInfoPilot();
        } else {
            replaceContentView(R.layout.fragment_pilot_info);
            ButterKnife.bind(this, mViewContainer);
            initInfoPilot();
        }
    }

    /**
     * Export data to OS contact
     *
     * @return true if success, otherwise return false
     */
    private boolean exportToContact() {
        if (mPilot == null) {
            return false;
        }
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        //------------------------------------------------------ Names
        if (!TextUtils.isEmpty(mPilot.getPilotName())) {
            ops.add(ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            mPilot.getPilotName()).build());
        }

        //------------------------------------------------------ Work Numbers
        if (!TextUtils.isEmpty(mPilot.getPilotPhone())) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mPilot.getPilotPhone())
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                    .build());
        }

        //------------------------------------------------------ Email
        if (!TextUtils.isEmpty(mPilot.getPilotEMail())) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, mPilot.getPilotEMail())
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                    .build());
        }

        //------------------------------------------------------ Organization
        if (!TextUtils.isEmpty(mPilot.getCompany())) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, mPilot.getCompany())
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, "")
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .build());
        }

        // ------------------------------------------------------------------- Image
        if (mHasImage) {
            File mImageFile = new File(StorageUtils.getStorageRootFolder(mActivity), imageFileName());
            if (mImageFile.exists()) {
                Bitmap bmp;
                try {
                    bmp = PhotoUtils.decodeBitmapFromUri(mActivity, Uri.fromFile(mImageFile));
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                            .withValue(ContactsContract.Data.MIMETYPE,
                                    ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                            .withValue(ContactsContract.CommonDataKinds.Photo.DATA15, byteArray)
                            .build());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Asking the Contact provider to create a new contact
        try {
            mActivity.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkValidBackPress() {
        return mRlFullScreen.getVisibility() == View.GONE || mRlFullScreen.getVisibility() == View.INVISIBLE;
    }

    public void outImgFullScreen() {
        mViewHeaderContainer.setVisibility(View.VISIBLE);
        mFooterContainer.setVisibility(View.VISIBLE);
        mRlFullScreen.setVisibility(View.GONE);
    }

    public Intent getOpenFacebookIntent(String input){ //Context context) {
        if(TextUtils.isEmpty(input)){
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse(String.format("https://www.facebook.com/%s", mPilot.getPilotName())));
        }else {
            if(input.contains("http://") || input.contains("https://")){
                return new Intent(Intent.ACTION_VIEW,
                        Uri.parse(input));
            }else {
                return new Intent(Intent.ACTION_VIEW,
                        Uri.parse(String.format("https://www.facebook.com/%s",input)));
            }
        }
       /* return new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.facebook.com/arkverse"));*/
      /*  try {
            context.getPackageManager()
                    .getPackageInfo("com.facebook.katana", 0); //Checks if FB is even installed.
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("fb://profile/timlaibautroi123")); //Trys to make intent with FB's URI
                    //Uri.parse("https://www.facebook.com/timlaibautroi123"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/arkverse")); //catches and opens a url to the desired page
        }*/
    }


    private void initHistory(){
        List<FlightModel> flightModels = mDatabaseManager.getLogbookListByAircraftPilotAirfield(MccEnum.dateFilter.LAST_90_DAYS, null, null, mPilotCode);
        edtPilotHistory.setVisibilityLineBottom(View.GONE);
        if (flightModels != null && flightModels.size() > 0) {
            String strCurrentDate = flightModels.get(0).getFlightDateUTC();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date newDate = new Date();
            try {
                newDate = format.parse(strCurrentDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String flightDate = DateTimeUtils.formatDateToString(newDate);
            edtPilotHistory.setDescription(String.format("%s : %s", flightDate, flightModels.get(0).getFlightAirfield()));
            if (!TextUtils.isEmpty(flightModels.get(0).getAircraftName()))
            {
                String onAircraft = String.format("on %s",
                       flightModels.get(0).getAircraftName());
                edtPilotHistory.setFootNote(onAircraft);
            }

        } else {
            lnHistory.setVisibility(View.GONE);
            edtPilotHistory.setDescription("none");
        }
    }

    @Override
    public void onResume() {
       initInfoPilot();
        super.onResume();
    }

}

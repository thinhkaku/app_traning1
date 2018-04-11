package aero.pilotlog.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.tasks.ImageProcessTask;
import aero.pilotlog.utilities.PhotoUtils;
import aero.pilotlog.utilities.ScreenUtils;
import aero.pilotlog.utilities.StorageUtils;
import aero.pilotlog.utilities.Utils;
import aero.pilotlog.widgets.CircleImageView;
import aero.pilotlog.widgets.FloatingActionMenu;
import aero.pilotlog.widgets.ItemSettingList;
import aero.pilotlog.widgets.MccDialog;
import aero.pilotlog.widgets.SubActionButton;

import java.io.File;
import java.text.ParseException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingProfileFragment extends BaseMCCFragment {
    @Bind(R.id.tvTitle)
    TextView mTvTitle;
    @Bind(R.id.ibLeft)
    ImageButton mIbLeft;
    @Bind(R.id.btnRight)
    Button mBtnRight;
    @Bind(R.id.ibMenu)
    ImageButton mIbMenu;
    @Bind(R.id.item_setting_gender)
    ItemSettingList mItemSettingGender;
    @Bind(R.id.item_setting_lastname)
    ItemSettingList mItemSettingLastName;
    @Bind(R.id.item_setting_firstname)
    ItemSettingList mItemSettingFirstName;
    @Bind(R.id.item_setting_function)
    ItemSettingList mItemSettingFunction;
    @Bind(R.id.item_setting_birthday)
    ItemSettingList mItemSettingBirthday;
    @Bind(R.id.item_setting_address)
    ItemSettingList mItemSettingAddress;
    @Bind(R.id.item_setting_countryOfResidence)
    ItemSettingList mItemSettingCountryOfResidence;
    @Bind(R.id.item_setting_cityOfResidence)
    ItemSettingList mItemSettingCityOfResidence;
    @Bind(R.id.item_setting_contactEmail)
    ItemSettingList mItemSettingContactEmail;
    @Bind(R.id.item_setting_cellPhone)
    ItemSettingList mItemSettingCellPhone;
    @Bind(R.id.item_setting_facebook)
    ItemSettingList mItemSettingFacebook;
    @Bind(R.id.item_setting_linkedIn)
    ItemSettingList mItemSettingLinkedIn;
    @Bind(R.id.iv_image_menu)
    CircleImageView mImageView;
    @Bind(R.id.scrollView)
    ScrollView svScrollView;
    /*@Bind(R.id.item_setting_moreAboutMe)
    ItemSettingList mItemSettingMoreAboutMe;
    @Bind(R.id.item_setting_myInterests)
    ItemSettingList mItemSettingMyInterests;*/
    private int mViewType = MCCPilotLogConst.SELECT_MODE;

    public void setViewType(int pViewType) {
        this.mViewType = pViewType;
    }

    public SettingProfileFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_setting_profile;
    }

    @Override
    protected int getFooterResId() {
        return 0;
    }

    @Override
    protected void onCreateFragment(View pContentView) throws ParseException {
        ButterKnife.bind(this, mViewContainer);
        initView();
    }

    private void initView() {
        mTvTitle.setText(getString(R.string.setting_title) + " - " + getString(R.string.setting_my_profile));
        mIbLeft.setVisibility(mViewType == MCCPilotLogConst.LIST_MODE || isTablet() ? View.GONE : View.VISIBLE);
        mIbMenu.setVisibility(mViewType == MCCPilotLogConst.LIST_MODE ? View.VISIBLE : View.GONE);
        if (isTablet()) {
            mBtnRight.setVisibility(View.GONE);
        }
        mItemSettingGender.setVisibleIconRight(View.GONE);
        mItemSettingGender.canShare = false;
        mItemSettingLastName.setVisibleIconRight(View.GONE);
        mItemSettingLastName.canShare = false;
        mItemSettingFirstName.setVisibleIconRight(View.GONE);
        mItemSettingFirstName.canShare = false;
        mItemSettingFunction.setVisibleIconRight(View.GONE);
        mItemSettingFunction.canShare = false;
        mItemSettingBirthday.setVisibleIconRight(View.GONE);
        mItemSettingAddress.setVisibleIconRight(View.GONE);
        mItemSettingCountryOfResidence.setVisibleIconRight(View.GONE);
        mItemSettingCityOfResidence.setVisibleIconRight(View.GONE);
        //mItemSettingCountryOfResidence.setIconRight(R.drawable.icon_setting_navigation);
       // mItemSettingCityOfResidence.setIconRight(R.drawable.icon_setting_navigation);
        //mItemSettingMoreAboutMe.setIconRight(R.drawable.icon_setting_navigation);
        //mItemSettingMyInterests.setIconRight(R.drawable.icon_setting_navigation);
        mItemSettingContactEmail.setIconRight(R.drawable.ic_item_email);
        mItemSettingCellPhone.setIconRight(R.drawable.ic_item_call);
        mItemSettingFacebook.setIconRight(R.drawable.ic_item_facebook);
        mItemSettingLinkedIn.setIconRight(R.drawable.ic_item_linkin);

        mItemSettingLinkedIn.setVisibleLineEnd(View.GONE);
        mItemSettingCityOfResidence.setVisibleLineEnd(View.GONE);
        mItemSettingGender.setVisibleLineEnd(View.GONE);
        initImageProfile();
        initData();
    }

    private void setShareIcon(int shareMode, ItemSettingList itemSettingList){
        switch (shareMode){
            case ItemSettingList.SHARE_WITH_ALL_USER:
                break;
            case ItemSettingList.SHARE_ONLY_WITH_SAME_COMPANY:
                break;
            case ItemSettingList.SHARE_WITH_SAME_COMPANY_AND_FUNCTION:
                break;
            case ItemSettingList.SHARE_WITH_NO_BODY:
                break;
            default:
                break;
        }
        itemSettingList.setIconLeft(shareMode);
    }

    @Nullable
    @OnClick({R.id.rlBackIcon})
    public void onClick(View pView) {
        if (mImageMenu != null) {
            mImageMenu.close(true);
        }
        switch (pView.getId()) {
            case R.id.rlBackIcon:
                Log.d("rlBackIcon", "touch");
                finishFragment();
                break;
            default:
                break;
        }
    }

    //Test data
    private void initData(){
        mItemSettingGender.setDescription("Male");
        mItemSettingLastName.setDescription("Boone");
        mItemSettingFirstName.setDescription("Pat");
        mItemSettingFunction.setDescription("Flight Crew Member");
        mItemSettingBirthday.setDescription("1967-12-12");
        mItemSettingBirthday.setIconLeft(ItemSettingList.SHARE_ONLY_WITH_SAME_COMPANY);
        mItemSettingAddress.setDescription("Belgium");
        mItemSettingAddress.setIconLeft(ItemSettingList.SHARE_ONLY_WITH_SAME_COMPANY);
        mItemSettingCountryOfResidence.setDescription("Belgium");
        mItemSettingCountryOfResidence.setIconLeft(ItemSettingList.SHARE_WITH_ALL_USER);
        mItemSettingCityOfResidence.setDescription("Herk-de-Stad");
        mItemSettingCityOfResidence.setIconLeft(ItemSettingList.SHARE_ONLY_WITH_SAME_COMPANY);
        mItemSettingContactEmail.setDescription("pat.boone@b787mrg.net");
        mItemSettingContactEmail.setIconLeft(ItemSettingList.SHARE_WITH_SAME_COMPANY_AND_FUNCTION);
        mItemSettingCellPhone.setDescription("+32478842206");
        mItemSettingCellPhone.setIconLeft(ItemSettingList.SHARE_WITH_SAME_COMPANY_AND_FUNCTION);
        mItemSettingFacebook.setDescription("Not as long as live");
        mItemSettingFacebook.setIconLeft(ItemSettingList.SHARE_WITH_NO_BODY);
        mItemSettingLinkedIn.setDescription("b737mrg");
        mItemSettingLinkedIn.setIconLeft(ItemSettingList.SHARE_WITH_NO_BODY);
        //mItemSettingMoreAboutMe.setDescription("Developer crewCONNECT");
        //mItemSettingMoreAboutMe.setIconLeft(ItemSettingList.SHARE_WITH_SAME_COMPANY_AND_FUNCTION);
        //mItemSettingMyInterests.setDescription("Computers, Music listening, Sailing, Tennis, Writing");
        //mItemSettingMyInterests.setIconLeft(ItemSettingList.SHARE_WITH_SAME_COMPANY_AND_FUNCTION);
    }
    private FloatingActionMenu mImageMenu;
    private boolean mHasImage;

    private void setImageMenu(SubActionButton buttonCamera, SubActionButton buttonGallery, SubActionButton buttonDelete, int radius) {
        mImageMenu = new FloatingActionMenu.Builder(mActivity)
                .addSubActionView(buttonCamera)
                .addSubActionView(buttonGallery)
                .addSubActionView(buttonDelete)
                .setStartAngle(180)
                .setEndAngle(90)
                .setRadius(radius)
                .attachTo(mImageView)
                .build();
    }

    private void initImageProfile(){
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
                    new PhotoUtils(SettingProfileFragment.this).takePhotoFromCameraForFragment(mActivity, imageFileName());
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
                new PhotoUtils(SettingProfileFragment.this).pickPhotoFromGalleryForFragment();
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
                            mImageView.setImageResource(R.drawable.ic_camera_transparent);
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
            setImageMenu(buttonCamera, buttonGallery, buttonDelete, 230);
        } else if ((ScreenUtils.SCREEN_WIDTH == 720 || ScreenUtils.SCREEN_WIDTH == 768) && ScreenUtils.SCREEN_HEIGHT == 1280) {
            setImageMenu(buttonCamera, buttonGallery, buttonDelete, 180);
        } else if (ScreenUtils.SCREEN_WIDTH == 2560 && ScreenUtils.SCREEN_HEIGHT == 1600) {
            setImageMenu(buttonCamera, buttonGallery, buttonDelete, 210);
        } else if (ScreenUtils.SCREEN_WIDTH == 800 && ScreenUtils.SCREEN_HEIGHT == 1280) {
            setImageMenu(buttonCamera, buttonGallery, buttonDelete, 170);
        } else if (ScreenUtils.SCREEN_HEIGHT > 2000) { //PL-1067
            setImageMenu(buttonCamera, buttonGallery, buttonDelete, 250);
        } else {
            setImageMenu(buttonCamera, buttonGallery, buttonDelete, 150);
        }
        svScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                mImageMenu.close(true);
            }
        });
    }
    private String imageFileName() {
//        return String.format(isSyncV4 ? MCCPilotLogConst.PILOT_IMAGE_FILE_NAME_V4 : MCCPilotLogConst.PILOT_IMAGE_FILE_NAME_V3, mPilotCode);
        return String.format(MCCPilotLogConst.PILOT_IMAGE_FILE_NAME_V4, mItemSettingFirstName.getDescription());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PhotoUtils.PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            final Uri mImageUri = data.getData();
            ImageProcessTask mTask = new ImageProcessTask(mActivity, imageFileName(), true) {
                @Override
                public void handleBitmap(Bitmap bitmap) {
                    //changeToPortraitLayout(new File(PhotoUtils.getRealPathFromURI(mActivity, mImageUri)));
                    mImageView.setImageBitmap(bitmap);
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
                    //changeToPortraitLayout(f);
                    mImageView.setImageBitmap(bitmap);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 9:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        new PhotoUtils(SettingProfileFragment.this).takePhotoFromCameraForFragment(mActivity, imageFileName());
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
                    new PhotoUtils(SettingProfileFragment.this).pickPhotoFromGalleryForFragment();

                } else {
                }

                break;
            default:
                break;
        }
    }
}

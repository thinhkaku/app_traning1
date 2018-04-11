package aero.pilotlog.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.entities.ImagePic;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.fragments.FlightAddsFragment;
import aero.pilotlog.utilities.DateTimeUtils;
import aero.pilotlog.utilities.MCCApplication;
import aero.pilotlog.utilities.OrientationUtils;
import aero.pilotlog.utilities.PaintUtils;
import aero.pilotlog.utilities.PhotoUtils;
import aero.pilotlog.utilities.StorageUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import aero.pilotlog.utilities.Utils;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ngoc.dh on 8/13/2015.
 * Signature
 */
public class SignatureActivity extends Activity {
    @Bind(R.id.mPaint)
    View mSignView;
    @Bind(R.id.tv_sign_set_date)
    TextView mHeaderSetDate;
    @Bind(R.id.tv_sign_save)
    TextView mHeaderSave;
    @Bind(R.id.ivSign)
    ImageView mIvSign;
    @Bind(R.id.rl_signature)
    RelativeLayout rlSignature;

    private File mSignDir;
    private int mScreenMultiply;
    private Calendar mCalendar = Calendar.getInstance();
    private PaintUtils mPaintUtils;
    private boolean mIsFirstTime = false;
    private byte[] flightCode;
    private DatabaseManager databaseManager;
    private ImagePic mImagePic;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd_HHmmss", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        databaseManager = DatabaseManager.getInstance(this);
        //PL-219 add code
        if (!MCCPilotLogConst.sIsTablet) {
            SignatureActivity.this.setTheme(R.style.AppTheme);
            OrientationUtils.lockOrientationLandscape(this);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //End PL-219



        super.onCreate(savedInstanceState);

        mIsFirstTime = true;

        setFinishOnTouchOutside(false);
        setContentView(R.layout.fragment_signature);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * Initialize view
     */
    private void initView() {

        /*Get view type*/
        mHeaderSetDate.setVisibility(View.VISIBLE);
        mHeaderSave.setVisibility(View.VISIBLE);
        int mScreenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        int mScreenWidth = this.getWindowManager().getDefaultDisplay().getWidth();

        //PL-219
        if (MCCPilotLogConst.sIsTablet) {
            if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ||
                    getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
                rlSignature.getLayoutParams().height = (int) (mScreenWidth / 2.2);
                rlSignature.getLayoutParams().width = rlSignature.getLayoutParams().height * 2;
            } else {
                rlSignature.getLayoutParams().height = (int) (mScreenHeight / 2.2);
                rlSignature.getLayoutParams().width = rlSignature.getLayoutParams().height * 2;
            }
        }
        //End PL-219
        mPaintUtils = new PaintUtils(this);
        /*Adjust watermark's size*/
        if (!MCCApplication.isTablet(this)) {
            if (mScreenWidth > 1024 || mScreenHeight > 1024) {
                mScreenMultiply = 2;
            }
        } else {
            mScreenMultiply = 1;
        }

       /* if (mViewType == MCCPilotLogConst.SIGNATURE_FROM_MENU) {
            showDatePickerDialog();
        } else {*/
        mHeaderSetDate.setVisibility(View.GONE);
        Bundle bundle = getIntent().getBundleExtra(MCCPilotLogConst.SIGNATURE_BUNDLE);
        flightCode = bundle.getByteArray(MCCPilotLogConst.FLIGHT_CODE);

        openOrCreateNewSignature();
   /*     Bundle b = getIntent().getBundleExtra(MCCPilotLogConst.SIGNATURE_BUNDLE);
        if (b != null) {
            String date = b.getString(MCCPilotLogConst.FLIGHT_DATE);
            try {
                mCalendar.setTime(FlightAddsFragment.DB_DATE_FORMAT.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // setCalendarDate();
            openOrCreateNewSignature();
        } else {
            finish();
        }*/
        // }
    }

    private void openOrCreateNewSignature() {

        if (flightCode != null) {
            mImagePic = databaseManager.getSignatureByFlightCode(flightCode);
            if (mImagePic != null) {
                openSignature(mImagePic.getFileName());
                //open signature
            } else {
                //create new signature
                clearSignature(true);
            }
        } else {
            clearSignature(true);
            //create new signature
        }

    }

    /**
     * Handle button Back, Calendar, Save, and Clear
     *
     * @param pView view
     */
    @OnClick({R.id.tv_sign_cancel, R.id.tv_sign_set_date, R.id.tv_sign_save, R.id.ivClear/*, R.id.ibMenu*/})
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.tv_sign_cancel: //Handle button back
                if (MCCApplication.isTablet) {
                    OrientationUtils.unlockOrientation(SignatureActivity.this);
                }
                finish();
                break;
            case R.id.tv_sign_set_date: //Handle button choose date
                /*if (mViewType == MCCPilotLogConst.SIGNATURE_FROM_MENU) {
                    showDatePickerDialog();
                }*/
                break;
            case R.id.tv_sign_save: //Handle button save
                if (MCCApplication.isTablet(this)) {
                    OrientationUtils.unlockOrientation(SignatureActivity.this);
                }
                /*Save the new signature*/
                if (!mIvSign.isShown()) {
                    saveSignature();
                }
                finish();
                break;
            case R.id.ivClear: //Handle button clear
                invalidateView();
                clearSignature(true);
                break;
            default:
                break;
        }
    }

    /**
     * Clear signature
     */
    public void clearSignature(boolean isPaintWatermark) {
        mIvSign.setVisibility(View.INVISIBLE);
        mPaintUtils.clearCanvas(mCalendar, isPaintWatermark);
        mIvSign.invalidate();
        mIvSign.refreshDrawableState();
    }

    /**
     * Save signature
     */
    public void saveSignature() {
        /*Create bitmap from sign view*/
        mSignView.setDrawingCacheEnabled(true);
        mSignView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                , View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        mSignView.buildDrawingCache(true);
        Bitmap bm = Bitmap.createBitmap(mSignView.getDrawingCache());
        mSignView.setDrawingCacheEnabled(false);
        mSignView.destroyDrawingCache();

        /*Save rescaled bitmap to file*/
        if (bm != null) {
            try {
//                bm = PhotoUtils.createScaledBitmap(bm, bm.getWidth(), bm.getWidth()/2);
                String path = createSignFolder();
//                SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.sign_date_format), Locale.US);
               /* String fName = getString(R.string.sign_file_format_sign) + simpleDateFormat.format(mCalendar.getTime());
                File file = new File(path, fName);// + getString(R.string.sign_file_format_jpg));
                PhotoUtils.makeImageFile(bm, file);*/
                if (mImagePic == null) {
                    String fName = getString(R.string.sign_file_format_sign) + simpleDateFormat.format(mCalendar.getTime());
                    File file = new File(path, fName);// + getString(R.string.sign_file_format_jpg));
                    PhotoUtils.makeImageFile(bm, file);
                    mImagePic = new ImagePic();
                    mImagePic.setLinkCode(flightCode);
                    mImagePic.setFileName(fName);
                    byte[] imageCode = Utils.getByteArrayFromGUID(Utils.generateStringGUID());
                    mImagePic.setImgCode(imageCode);
                    mImagePic.setRecord_Upload(true);
                    mImagePic.setRecord_Modified(DateTimeUtils.getCurrentUTCUnixTimeStamp());
                }else {
                    String fName = mImagePic.getFileName();
                    File file = new File(path, fName);// + getString(R.string.sign_file_format_jpg));
                    PhotoUtils.makeImageFile(bm, file);
                }
                databaseManager.insertImagePic(mImagePic);
                setResult(Activity.RESULT_OK);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Create folder containing signatures
     *
     * @return path to folder Sign
     */
    private String createSignFolder() {
        String pathToFiles = StorageUtils.getStorageRootFolder(this);
        File folderFiles = new File(pathToFiles);
        String path = folderFiles + File.separator + getString(R.string.sign_folder);
        mSignDir = new File(path);
        if (!mSignDir.exists()) {
            mSignDir.mkdir();
        }
        return path;
    }

    /**
     * Open signature
     *
     * @param pName: file name
     */
    private void openSignature(String pName) {
        mSignView.setVisibility(View.VISIBLE);

        /*Get directory of Sign folder*/
        //final String localImageName = String.format(MCCPilotLogConst.SIGN_NAME_FORMAT, pName);
        createSignFolder();
        String fileNamePic = mSignDir.getPath() + File.separator + pName;
        File signImgFile = new File(fileNamePic);

        /*Show bitmap from file*/
        if (signImgFile.exists()) {
            Bitmap bm = null;
            try {
                bm = PhotoUtils.decodeBitmapFromUri(this, Uri.fromFile(signImgFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mIvSign.setImageBitmap(bm);
            mIvSign.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Check if the image file exists
     *
     * @param pName: file name
     * @return: if file exists
     */
  /*  public boolean checkIfFileExists(String pName) {
        final String localImageName = String.format(MCCPilotLogConst.SIGN_NAME_FORMAT, pName);
        String path = createSignFolder();
        String fileNamePic = mSignDir.getPath() + File.separator + localImageName;
        File signImgFile = new File(fileNamePic);
        return (signImgFile.exists());
    }*/

    /**
     * Set date for calendar
     */
 /*   public void setCalendarDate() {
//        final SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.sign_date_format), Locale.getDefault());
        //final SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd", Locale.getDefault());
        String mSignName =  simpleDateFormat.format(mCalendar.getTime());
        if (checkIfFileExists(mSignName)) {
            openSignature(mSignName);
        } else {
            clearSignature(true);
        }
    }*/

    /**
     * Show date picker dialog
     */
   /* public void showDatePickerDialog() {
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);

        *//*Set date for watermark*//*
        DatePickerDialog datePicker = new DatePickerDialog(this, R.style.DatePickerTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                mHeaderSave.setVisibility(View.VISIBLE);
                invalidateView();
                mCalendar = new GregorianCalendar(Locale.US);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setCalendarDate();
                mIsFirstTime = false;
            }
        }, year, month, day);

        *//*If cancel, choose current date*//*
        datePicker.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.alert_cancel_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mIsFirstTime) {
                    mHeaderSave.setVisibility(View.GONE);
                    invalidateView();
                    clearSignature(false);
                }
                mIsFirstTime = false;
            }
        });
        datePicker.setCancelable(false);
        datePicker.show();
    }*/

    /**
     * Invalidate view
     */
    private void invalidateView() {
        mSignView.invalidate();
        mIvSign.invalidate();
    }
}
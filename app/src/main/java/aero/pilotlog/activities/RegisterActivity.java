package aero.pilotlog.activities;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.SyncStateContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonElement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.api.APIManager;
import aero.pilotlog.api.MccNetWorkingService;
import aero.pilotlog.common.ApiConstant;
import aero.pilotlog.common.CloudApiConstants;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.common.SettingPreferenceKeyConst;
import aero.pilotlog.interfaces.MccCallback;
import aero.pilotlog.models.SuccessRespond;
import aero.pilotlog.models.UserProfile;
import aero.pilotlog.models.UserProfileRespond;
import aero.pilotlog.utilities.ProfileUtils;
import aero.pilotlog.utilities.StorageUtils;
import aero.pilotlog.widgets.AlertDialog;
import aero.pilotlog.widgets.CircleImageView;
import aero.pilotlog.widgets.MccDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RegisterActivity extends FragmentActivity implements View.OnClickListener {
    private AlertDialog alertDialog;

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    Button btnContinue, btnNoThanks, btnWatchVideo;
    CircleImageView circleImageView1, circleImageView2, circleImageView3;
    ImageView imgBack;
    LinearLayout llRegisterBottom;
    private ProgressDialog mProgressDialog;
    private EditText edtEmail, edtPassword, edtFirstName, edtLastName;
    private TextView tvEmailSent, tvAcknowledge;
    private ProfileUtils profileUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        profileUtils = ProfileUtils.newInstance();
        alertDialog = new AlertDialog(this);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        btnContinue = (Button) findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(this);

        circleImageView1 = (CircleImageView) findViewById(R.id.circleImage1);
        circleImageView2 = (CircleImageView) findViewById(R.id.circleImage2);
        circleImageView3 = (CircleImageView) findViewById(R.id.circleImage3);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(this);
        llRegisterBottom = (LinearLayout) findViewById(R.id.ll_register_bottom);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {

                    case 1:
                        edtEmail = (EditText) mViewPager.findViewById(R.id.edtEmail);
                        break;
                    case 2:
                        edtPassword = (EditText) mViewPager.findViewById(R.id.edtPassword);
                        edtPassword.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                final int DRAWABLE_LEFT = 0;
                                final int DRAWABLE_TOP = 1;
                                final int DRAWABLE_RIGHT = 2;
                                final int DRAWABLE_BOTTOM = 3;

                                switch (event.getAction()) {
                                    case MotionEvent.ACTION_DOWN:
                                        if (event.getRawX() >= (edtPassword.getRight() - edtPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                                            // your action here

                                            edtPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                                            return false;
                                        }

                                        break;
                                    case MotionEvent.ACTION_UP:
                                        if (event.getRawX() >= (edtPassword.getRight() - edtPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                                            // your action here

                                            edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                            return false;
                                        }

                                        break;
                                }
                                return false;
                            }
                        });
                        break;
                    case 3:
                        btnNoThanks = (Button) findViewById(R.id.btnNoThanks);
                        btnNoThanks.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent;
                                if (MCCPilotLogConst.sIsTablet) {
                                    intent = new Intent(RegisterActivity.this, MainTabletActivity.class);
                                } else {
                                    intent = new Intent(RegisterActivity.this, MainActivity.class);
                                }
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                RegisterActivity.this.finish();
                            }
                        });
                        btnWatchVideo = (Button) findViewById(R.id.btnWatchVideo);
                        btnWatchVideo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(MCCPilotLogConst.WATCH_VIDEO_LINK));
                                startActivity(browserIntent);
                            }
                        });
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setStatusBar();
    }

    private void setStatusBar() {
        Window window = this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.mcc_color_dark));
        }
    }

    public final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.btnContinue:
                    int currentItem = mViewPager.getCurrentItem();
                    if (currentItem < mViewPager.getAdapter().getCount()) {
                        switch (currentItem) {
                            case 0:
                                try {
                                    edtFirstName = (EditText) mViewPager.findViewById(R.id.edtFirstName);
                                    edtLastName = (EditText) mViewPager.findViewById(R.id.edtLastName);
                                    if (!TextUtils.isEmpty(edtFirstName.getText().toString()) && !TextUtils.isEmpty(edtLastName.getText().toString())) {
                                        circleImageView1.setImageResource(R.color.mcc_color_login_screen);
                                        mViewPager.setCurrentItem(currentItem + 1);
                                    } else {
                                        alertDialog.createDialog( "Confirm", "Fill out the First Name and Last Name field").show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
                                try {

                                    if (!TextUtils.isEmpty(edtEmail.getText().toString())) {
                                        if (!validate(edtEmail.getText().toString())) {
                                            alertDialog.createDialog(R.string.confirm, R.string.invalid_email).show();
                                        } else if (!edtEmail.getText().toString().contains(".") || !edtEmail.getText().toString().contains("@") || edtEmail.getText().toString().contains("yaho.com") || edtEmail.getText().toString().contains("gail.com ") || edtEmail.getText().toString().contains("gmai.com")) {
                                            alertDialog.createDialog(R.string.confirm, R.string.invalid_email).show();
                                        } else {
                                            tvEmailSent = (TextView) mViewPager.findViewById(R.id.tvEmailSent);
                                            tvAcknowledge = (TextView) mViewPager.findViewById(R.id.tvAcknowledge);
                                            tvEmailSent.setText("An email was sent to\n" + edtEmail.getText().toString());
                                            circleImageView2.setImageResource(R.color.mcc_color_login_screen);
                                            mViewPager.setCurrentItem(currentItem + 1);
                                        }
                                    } else {
                                        alertDialog.createDialog( "Invalid Email", "Fill out the Email field").show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                break;
                            case 2:
                                if (TextUtils.isEmpty(edtPassword.getText().toString())) {
                                    alertDialog.createDialog(R.string.confirm, R.string.password_empty).show();
                                } else if (edtPassword.getText().toString().length() < 6) {
                                    alertDialog.createDialog(R.string.confirm, R.string.invalid_password_length).show();
                                } else {
                                    if (mProgressDialog == null) {
                                        mProgressDialog = new ProgressDialog(this);
                                    }
                                    mProgressDialog.setMessage(getString(R.string.loading));
                                    mProgressDialog.show();

                                    register();


                                }
                                break;
                            case 3:

                                break;

                        }

                    }
                    break;
                case R.id.imgBack:
                    onBackPressed();
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void register() {

        MccNetWorkingService mccNetWorkingService = APIManager.getNetworkingServiceInstance();
        mccNetWorkingService.register(edtEmail.getText().toString(), edtPassword.getText().toString(),
                edtFirstName.getText().toString(), edtLastName.getText().toString(),new MccCallback<>(
                        new MccCallback.RequestListener<SuccessRespond>() {
                            @Override
                            public void onSuccess(SuccessRespond response) {
                                registerSuccess();
                            }

                            @Override
                            public void onRetrofitFailure(RetrofitError error) {
                                registerFail(error.getResponse().getStatus());
                            }

                            @Override
                            public void onFailure(int errorCode) {
                                registerFail(errorCode);
                            }
                        }));
    }
    private void checkLogin(String email, String password) {

        MccNetWorkingService mccNetWorkingService = APIManager.getNetworkingServiceInstance();
        mccNetWorkingService.checkUserLogin(email, password, CloudApiConstants.PLATFORM, CloudApiConstants.MODULE, "0",
                new MccCallback<>(
                        new MccCallback.RequestListener<UserProfileRespond>() {
                            @Override
                            public void onSuccess(UserProfileRespond response) {
                                profileUtils.setUserProfileRespond(response);
                                StorageUtils.writeStringToSharedPref(RegisterActivity.this, SettingPreferenceKeyConst.PREF_EMAIL_SAVE, edtEmail.getText().toString());
                                StorageUtils.writeStringToSharedPref(RegisterActivity.this, SettingPreferenceKeyConst.PREF_PASSWORD_REGIS, edtPassword.getText().toString());
                                llRegisterBottom.setVisibility(View.GONE);
                                mViewPager.setCurrentItem(3);
                                circleImageView3.setImageResource(R.color.mcc_color_login_screen);
                            }

                            @Override
                            public void onRetrofitFailure(RetrofitError error) {
                               // loginFail(error.getResponse().getStatus());
                                MccDialog.showErrorByErrorCode(RegisterActivity.this, error.getResponse().getStatus());
                            }

                            @Override
                            public void onFailure(int errorCode) {
                                //loginFail(errorCode);
                                MccDialog.showErrorByErrorCode(RegisterActivity.this, errorCode);
                            }
                        }));
    }
    public void registerSuccess() {
        tvEmailSent.setText("Registration was successful.");
        tvAcknowledge.setText("Just a moment while we setup your new User Profile...");
        mProgressDialog.dismiss();
        checkLogin(edtEmail.getText().toString(),edtPassword.getText().toString());
    }

    public void registerFail(int errorCode) {
        mProgressDialog.dismiss();
        if(errorCode==ApiConstant.ERROR_CONFLICT){
            new android.support.v7.app.AlertDialog.Builder(this, R.style.AlertDialogTheme)
                    .setTitle(this.getResources().getString(R.string.error))
                    .setMessage(this.getResources().getString(R.string.account_exists))
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.putExtra("email", edtEmail.getText().toString());
                            startActivity(intent);
                        }
                    }).show();
        }else {
            MccDialog.showErrorByErrorCode(this,errorCode);
        }
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() > 0 && mViewPager.getCurrentItem() != 3) {
            int currentItem = mViewPager.getCurrentItem();
            switch (currentItem) {
                case 1:
                    mViewPager.setCurrentItem(currentItem - 1);
                    llRegisterBottom.setVisibility(View.VISIBLE);
                    circleImageView1.setImageResource(R.color.transparent);
                    break;
                case 2:
                    mViewPager.setCurrentItem(currentItem - 1);
                    llRegisterBottom.setVisibility(View.VISIBLE);
                    circleImageView2.setImageResource(R.color.transparent);
                    break;
                case 3:
                    break;
            }

        } else {
            super.onBackPressed();
        }
    }


    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView;
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    rootView = inflater.inflate(R.layout.register_placeholder, container, false);
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.register_placeholder2, container, false);
                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.register_placeholder3, container, false);
                    break;
                case 4:
                    rootView = inflater.inflate(R.layout.register_placeholder4, container, false);
                    break;
                default:
                    rootView = inflater.inflate(R.layout.register_placeholder, container, false);
                    break;
            }
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}

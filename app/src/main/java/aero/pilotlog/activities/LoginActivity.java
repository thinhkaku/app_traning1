package aero.pilotlog.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aero.crewlounge.pilotlog.BuildConfig;
import aero.crewlounge.pilotlog.R;
import aero.pilotlog.api.APIManager;
import aero.pilotlog.api.MccNetWorkingService;
import aero.pilotlog.common.CloudApiConstants;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.common.SettingPreferenceKeyConst;
import aero.pilotlog.interfaces.MccCallback;
import aero.pilotlog.interfaces.OnloadRequest;
import aero.pilotlog.models.SuccessRespond;
import aero.pilotlog.models.UserProfileRespond;
import aero.pilotlog.tasks.RequestDataAsync;
import aero.pilotlog.utilities.ProfileUtils;
import aero.pilotlog.utilities.StorageUtils;
import aero.pilotlog.widgets.AlertDialog;
import aero.pilotlog.widgets.MccDialog;
import retrofit.ApiUtil;
import retrofit.Callback;
import retrofit.Hinh;
import retrofit.RetrofitError;
import retrofit.SanPham;
import retrofit.SoaService;
import retrofit.client.Response;
import retrofit2.Call;

public class LoginActivity extends Activity implements View.OnClickListener ,OnloadRequest{
    private AlertDialog alertDialog;
    private ProgressDialog mProgressDialog;
    EditText edtEmail, edtPassword;
    Button btnLogin, btnForgot;
    ImageView imgBack;
    private ProfileUtils profileUtils;
    private SoaService soaService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        profileUtils = ProfileUtils.newInstance();
        alertDialog = new AlertDialog(this);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnForgot = (Button) findViewById(R.id.btnForgot);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(this);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin.setOnClickListener(this);
        btnForgot.setOnClickListener(this);
        setStatusBar();
        Bundle bundle = getIntent().getExtras();

        String emailFromRegister = bundle != null ? bundle.get("email").toString() : "";
        if (!TextUtils.isEmpty(emailFromRegister)) {
            edtEmail.setText(emailFromRegister);
        } else {
            loginWithCache();
        }

    }

    private void loginWithCache() {
        String email = StorageUtils.getStringFromSharedPref(this, SettingPreferenceKeyConst.PREF_EMAIL_SAVE, edtEmail.getText().toString());
        String password = StorageUtils.getStringFromSharedPref(this, SettingPreferenceKeyConst.PREF_PASSWORD_REGIS, edtPassword.getText().toString());
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(this);
            }
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.show();
            checkLogin(email, password);
        }
    }

    private void setStatusBar() {
        Window window = this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.mcc_color_dark));
        }
    }

    private void checkLogin(String email, String password) {

        MccNetWorkingService mccNetWorkingService = APIManager.getNetworkingServiceInstance();
        mccNetWorkingService.checkUserLogin(email, password, CloudApiConstants.PLATFORM, CloudApiConstants.MODULE, "0",
                new MccCallback<>(
                        new MccCallback.RequestListener<UserProfileRespond>() {
                            @Override
                            public void onSuccess(UserProfileRespond response) {

                                profileUtils.setUserProfileRespond(response);
                                loginSuccess();
                            }

                            @Override
                            public void onRetrofitFailure(RetrofitError error) {
                                loginFail(error.getResponse().getStatus());
                            }

                            @Override
                            public void onFailure(int errorCode) {
                                loginFail(errorCode);
                            }
                        }));
    }

    private void forgotPassword(String email) {
        MccNetWorkingService mccNetWorkingService = APIManager.getNetworkingServiceInstance();
        mccNetWorkingService.forgotPass(email,
                new MccCallback<>(
                        new MccCallback.RequestListener<SuccessRespond>() {
                            @Override
                            public void onSuccess(SuccessRespond response) {
                                resetPassSuccess();
                            }

                            @Override
                            public void onRetrofitFailure(RetrofitError error) {
                                resetPassFail(error.getResponse().getStatus());
                            }

                            @Override
                            public void onFailure(int errorCode) {
                                resetPassFail(errorCode);
                            }
                        }));
    }

    private void loginSuccess() {
        if (checkDayRequest()==true){
            requestToSever();
        }
        mProgressDialog.dismiss();
        if (!TextUtils.isEmpty(edtEmail.getText().toString()) && !TextUtils.isEmpty(edtPassword.getText().toString())) {
            StorageUtils.writeStringToSharedPref(this, SettingPreferenceKeyConst.PREF_EMAIL_SAVE, edtEmail.getText().toString());
            StorageUtils.writeStringToSharedPref(this, SettingPreferenceKeyConst.PREF_PASSWORD_REGIS, edtPassword.getText().toString());
        }

        Intent intent;
        if (MCCPilotLogConst.sIsTablet) {
            intent = new Intent(this, MainTabletActivity.class);
        } else {
            intent = new Intent(this, MainActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        this.finish();
    }


    //Kiem tra so ngay request
    private boolean checkDayRequest(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String currentYearMonth = sdf.format(new Date());
        String yearMonth = StorageUtils.getStringFromSharedPref(this, SettingPreferenceKeyConst.SAVE_DAY_REQUEST, "0");
        if (Integer.parseInt(yearMonth)<Integer.parseInt(currentYearMonth)){
            StorageUtils.writeStringToSharedPref(this, SettingPreferenceKeyConst.SAVE_DAY_REQUEST, currentYearMonth);
            return true;
        }
        return false;
    }


    private void requestToSever(){
        String BD = String.valueOf(BuildConfig.VERSION_CODE);
        String CT=profileUtils.getUserProfileRespond().getUserProfile().getThumbnail();
        String ID=profileUtils.getUserProfileRespond().getUserProfile().getUserId();
        Random r = new Random();
        String alphabet = "123-abcdefghxyz";
        String test="";
        for (int i = 0; i < 28; i++) {
            test+=alphabet.charAt(r.nextInt(alphabet.length()));
        }
        ID=ID+test;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String DT = sdf.format(new Date());
        String CS=String.valueOf(Integer.parseInt(DT.substring(11,15))+911) ;
        MccNetWorkingService mccNetWorkingService1 = APIManager.getNetworkingServiceInstance1();
        mccNetWorkingService1.sendData("STD","WIN",BD,ID,"ChinaRep","0",DT,CS,
                new MccCallback<String>(new MccCallback.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                Log.d("onSucess",response);
            }

            @Override
            public void onRetrofitFailure(RetrofitError error) {
                Log.d("onRetrofitFailure",error.toString());
            }

            @Override
            public void onFailure(int errorCode) {
                Log.d("onFailure",errorCode+"");
            }
        }));
    }

    private void loginFail(int errorCode) {
        MccDialog.showErrorByErrorCode(this, errorCode);
        mProgressDialog.dismiss();
    }


    private void resetPassSuccess() {
        mProgressDialog.dismiss();
        edtPassword.setText("");
        alertDialog.createDialog(R.string.forgot_password_dialog_title, String.format(getString(R.string.reset_password_success), edtEmail.getText().toString())).show();
    }

    private void resetPassFail(int errorCode) {
        mProgressDialog.dismiss();
        edtPassword.setText("");
        //alertDialog.createDialog(R.string.confirm, R.string.invalid_email).show();
        MccDialog.showErrorByErrorCode(this, errorCode);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                    alertDialog.createDialog(R.string.confirm, R.string.email_empty).show();
                } else if (TextUtils.isEmpty(edtPassword.getText().toString())) {
                    alertDialog.createDialog(R.string.confirm, R.string.password_empty).show();
                } else if (!validate(edtEmail.getText().toString())) {
                    alertDialog.createDialog(R.string.confirm, R.string.invalid_email).show();
                } else {
                    if (mProgressDialog == null) {
                        mProgressDialog = new ProgressDialog(this);
                    }
                    mProgressDialog.setMessage(getString(R.string.loading));
                    mProgressDialog.show();
                    checkLogin(edtEmail.getText().toString(), edtPassword.getText().toString());
                }
                break;
            case R.id.btnForgot:
                if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                    alertDialog.createDialog(R.string.confirm, R.string.email_empty).show();

                } else if (!validate(edtEmail.getText().toString())) {
                    alertDialog.createDialog(R.string.confirm, R.string.invalid_email).show();
                } else {
                    if (mProgressDialog == null) {
                        mProgressDialog = new ProgressDialog(this);
                    }
                    mProgressDialog.setMessage(getString(R.string.processing_dialog_reseting_password));
                    mProgressDialog.show();
                    forgotPassword(edtEmail.getText().toString());
                }
                break;
            case R.id.imgBack:
                finish();
                break;
        }


    }

    public final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    @Override
    public void onSucess(String result) {
        //Log.e("onSuccess",result);
        if (result.equals("DONE")){
            Toast.makeText(LoginActivity.this,"Request thanh cong"+result,Toast.LENGTH_LONG).show();
        }
        Toast.makeText(LoginActivity.this,""+result,Toast.LENGTH_LONG).show();

    }

    @Override
    public void onErorr() {
        Toast.makeText(LoginActivity.this,"Request Looix",Toast.LENGTH_LONG).show();
    }
}

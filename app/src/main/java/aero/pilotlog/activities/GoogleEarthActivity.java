package aero.pilotlog.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.entities.Airfield;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.utilities.LocationUtils;
import aero.pilotlog.utilities.LogUtils;
import aero.pilotlog.utilities.MCCApplication;
import aero.pilotlog.utilities.OrientationUtils;
import aero.pilotlog.utilities.ProgressDialogUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class GoogleEarthActivity extends Activity {

    @Bind(R.id.ibMenu)
    ImageButton mIbMenu;
    @Bind(R.id.ibLeft)
    ImageButton mIbLeft;
    @Bind(R.id.tvTitle)
    TextView mTvTitle;
    @Bind(R.id.tvNumber)
    TextView mTvNumber;
    @Bind(R.id.webview)
    WebView mWebview;

    private static final String GOOGLE_URL = "http://maps.google.com/maps?q=loc:%s,%s&z=17&t=k";
    private static final String DIV = " - ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_airfield_google_earth);
        ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        mIbMenu.setVisibility(View.GONE);
        mIbLeft.setVisibility(View.VISIBLE);
        mTvNumber.setVisibility(View.GONE);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(MCCPilotLogConst.GOOGLE_EARTH_BUNDLE);
        if (bundle != null) {
            byte[] mAirfieldCode = bundle.getByteArray(MCCPilotLogConst.AIRFIELD_CODE_KEY);
            if (mAirfieldCode != null) {
                Airfield mAirfieldPilot = new DatabaseManager(this).getAirfieldByCode(mAirfieldCode);
                if (mAirfieldPilot != null) {
                    if (TextUtils.isEmpty(mAirfieldPilot.getAFICAO())) {
                        mTvTitle.setText(mAirfieldPilot.getAFIATA());
                    } else if (TextUtils.isEmpty(mAirfieldPilot.getAFIATA())) {
                        mTvTitle.setText(mAirfieldPilot.getAFICAO());
                    } else {
                        mTvTitle.setText(mAirfieldPilot.getAFICAO() + DIV + mAirfieldPilot.getAFIATA());
                    }

                    if (mAirfieldPilot.getLatitude()!=null && mAirfieldPilot.getLongitude()!=null) {
                        final ProgressDialogUtils progress = new ProgressDialogUtils(this);
                        String url = String.format(GOOGLE_URL, LocationUtils.getLatitudeString(mAirfieldPilot.getLatitude()),
                                LocationUtils.getLongitudeString(mAirfieldPilot.getLongitude()));
                        LogUtils.e("URL goole map: " + url);
                        mWebview.setWebViewClient(new WebViewClient() {

                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                view.loadUrl(url);
                                return true;
                            }

                            @Override
                            public void onPageFinished(WebView view, String url) {
                                super.onPageFinished(view, url);
                                LogUtils.e("finish");
                                progress.hideProgressDialog();

                            }
                        });
                        mWebview.getSettings().setJavaScriptEnabled(true);
                        mWebview.loadUrl(url);
                        mWebview.requestFocus();
                        progress.showProgressDialog(R.string.loading);
                    }
                }
            }
        }
        System.gc();
    }


    @OnClick({R.id.ibLeft})
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.ibLeft: //Handle button back
                if (MCCApplication.isTablet) {
                    OrientationUtils.unlockOrientation(GoogleEarthActivity.this);
                }
                finish();
                break;
            default:
                break;
        }
    }
}

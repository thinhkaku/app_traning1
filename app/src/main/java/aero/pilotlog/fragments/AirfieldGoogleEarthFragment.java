package aero.pilotlog.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.entities.Airfield;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.utilities.LocationUtils;
import aero.pilotlog.utilities.LogUtils;
import aero.pilotlog.utilities.ProgressDialogUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tuan.na on 7/30/2015.
 * Airfield google earth
 */
public class AirfieldGoogleEarthFragment extends BaseMCCFragment {

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
    @Bind(R.id.llTitleMap)
    LinearLayout mLlTitleMap;

    private static final String GOOGLE_URL = "http://maps.google.com/maps?q=loc:%s,%s&z=17&t=k";

    @Override
    protected void onCreateFragment(View pContentView) {
        ButterKnife.bind(this, mViewContainer);
        initView();
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_airfield_google_earth;
    }

    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getFooterResId() {
        return FLAG_NO_RESOURCE;
    }

    /**
     * Initialize view
     */
    private void initView() {
        mIbMenu.setVisibility(View.GONE);
        mIbLeft.setVisibility(View.VISIBLE);
        mTvNumber.setVisibility(View.GONE);
        mLlTitleMap.setVisibility(View.GONE);
        Bundle bundle = getArguments();
        if (bundle != null) {
            byte[] mAirfieldCode = bundle.getByteArray(MCCPilotLogConst.AIRFIELD_CODE_KEY);
            if (mAirfieldCode != null) {
                //AirfieldPilot mAirfieldPilot = new DatabaseManager(mActivity).getAirfieldPilotFromCode(mAirfieldCode);
                Airfield airfield = DatabaseManager.getInstance(mActivity).getAirfieldByCode(mAirfieldCode);
                if (airfield != null) {
                    if (TextUtils.isEmpty(airfield.getAFICAO())) {
                        mTvTitle.setText(airfield.getAFIATA());
                    } else if (TextUtils.isEmpty(airfield.getAFIATA())) {
                        mTvTitle.setText(airfield.getAFICAO());
                    } else {
                        mTvTitle.setText(airfield.getAFICAO() + " - " + airfield.getAFIATA());
                    }

                        final ProgressDialogUtils progress = new ProgressDialogUtils(mActivity);
                        //http://maps.google.com/maps?q=loc:40.01696,32.33076&z=17
                        String url = String.format(GOOGLE_URL, LocationUtils.getLatitudeString(String.valueOf(airfield.getLatitude())),
                                LocationUtils.getLongitudeString(String.valueOf(airfield.getLongitude())));
                        LogUtils.e("URL goole map: " + url);
                        mWebview.getSettings().setJavaScriptEnabled(true);
                        mWebview.setWebViewClient(new WebViewClient() {
                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                view.loadUrl(url);
                                return true;
                            }

                            @Override
                            public void onPageFinished(WebView view, String url) {
                                view.loadUrl("javascript:(function() { " +
                                        "document.getElementById('ml-searchbox').style.display='none'; " +
                                        "document.getElementsByClassName('ml-if-pane-collapsed ml-panes-entity-collapsed-header-container')[0].style.display='none'; " +
                                        "document.getElementsByClassName('ml-panes-entity-container ml-scrollable')[0].style.display='none'; " +
                                        "})()");
                                if(flagLoadUrlFinished==1){
                                    mWebview.setVisibility(View.VISIBLE);
                                    progress.hideProgressDialog();
                                }
                                flagLoadUrlFinished++;
                                super.onPageFinished(view, url);

                            }
                        });
                    flagLoadUrlFinished = 0;
                        //mWebview.getSettings().setJavaScriptEnabled(true);
                        mWebview.loadUrl(url);
                        mWebview.requestFocus();
                        progress.showProgressDialog(R.string.loading);
                }
            }
        }
        System.gc();
    }
    int flagLoadUrlFinished = 0;

    @OnClick(R.id.rlBackIcon)
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.rlBackIcon:
                finishFragment();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebview != null) {
            mWebview.removeAllViews();
            mWebview.destroy();
        }
        mWebview = null;
    }
}
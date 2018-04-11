package aero.pilotlog.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.ApiConstant;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.entities.Airfield;
import aero.pilotlog.databases.entities.ZCountry;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.utilities.NetworkUtils;
import aero.pilotlog.widgets.AlertToast;
import aero.pilotlog.widgets.MccDialog;

import java.text.ParseException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class AirfieldTripAdvisorFragment extends BaseMCCFragment {
    @Bind(R.id.ibLeft)
    ImageButton mIbLeft;
    @Bind(R.id.tvTitle)
    TextView mTvTitle;
    @Bind(R.id.rlBackIcon)
    RelativeLayout mHeaderRlBack;
    @Bind(R.id.ibMenu)
    ImageButton mHeaderIbMenu;

    @Bind(R.id.wv_content)
    WebView mWvContent;
    @Bind(R.id.pb_loading)
    ProgressBar mProgressBar;

    private AlertToast mToastLoading;
    private Airfield mAirfield = null;// = new Airfield();
    DatabaseManager databaseManager;

    public AirfieldTripAdvisorFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_airfield_wiki;
    }

    @Override
    protected int getFooterResId() {
        return 0;
    }

    @Override
    protected void onCreateFragment(View pContentView) throws ParseException {
        ButterKnife.bind(this, mViewContainer);
        Bundle bundle = getArguments();
        databaseManager = DatabaseManager.getInstance(mActivity);
        initView(bundle);
    }
    private void initView(Bundle bundle) {
        //set header views
        mHeaderIbMenu.setVisibility(View.GONE);
        //if (!isTablet()) {
            mIbLeft.setVisibility(View.VISIBLE);
        //}
        //Disable header back icon for tablet
       /* if (isTablet()) {
            mHeaderRlBack.setClickable(false);
        }*/
        mTvTitle.setText("TripAdvisor");
        mToastLoading = new AlertToast(mActivity);
        mToastLoading.showToast();

        if (!NetworkUtils.isHavingNetwork(mActivity)) {
            mProgressBar.setVisibility(View.INVISIBLE);
            MccDialog alertDialog = new MccDialog();
            alertDialog. createDialog(mActivity, R.string.title_activity_airfield_trip_advisor, R.string.dialog_no_internet_content).show();
            mToastLoading.cancelToast();
            return;
        }

        mWvContent.setWebViewClient(new WebViewClient());
        mWvContent.getSettings().setJavaScriptEnabled(true);
        byte[] airfieldCode = bundle.getByteArray(MCCPilotLogConst.AIRFIELD_CODE_KEY);
        mWvContent.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
            }
        });
        if (airfieldCode != null) {
            mAirfield = databaseManager.getAirfieldByCode(airfieldCode);
            String searchString = mAirfield.getCity().trim().replace(' ', '+').replace(',', '+');
            if (TextUtils.isEmpty(searchString)) {
                ZCountry country = databaseManager.getCountryByCode(mAirfield.getAFCountry());
                searchString = (country.getCountryName() + " " + mAirfield.getAFName()).trim().replace(' ', '+').replace(',', '+');
            }
            mWvContent.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int progress) {
                    if (progress < 100 && mProgressBar.getVisibility() == View.INVISIBLE) {
                        mProgressBar.setVisibility(ProgressBar.VISIBLE);
                    }
                    mProgressBar.setProgress(progress);
                    if (progress == 100) {
                        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                    }
                }
            });
            mWvContent.loadUrl(ApiConstant.TRIP_ADVISOR_URL + searchString);
            mWvContent.setVisibility(View.VISIBLE);
            mToastLoading.cancelToast();
        }
    }

    @Nullable
    @OnClick({R.id.rlBackIcon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlBackIcon:
                finishFragment();
                break;
        }
    }
}

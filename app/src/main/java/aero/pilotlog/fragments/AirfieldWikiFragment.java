package aero.pilotlog.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.entities.Airfield;
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
public class AirfieldWikiFragment extends BaseMCCFragment {
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
    public AirfieldWikiFragment() {
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

    private void initView(Bundle bundle){
        //set header views
        mHeaderIbMenu.setVisibility(View.GONE);
       // if (!isTablet()) {
            mIbLeft.setVisibility(View.VISIBLE);
        //}
        //Disable header back icon for tablet
        /*if (isTablet()) {
            mHeaderRlBack.setClickable(false);
        }*/
        mTvTitle.setText(R.string.title_activity_airfield_wikipedia);
        mToastLoading = new AlertToast(mActivity);
        mToastLoading.showToast();
        // Find views by IDs
        if (!NetworkUtils.isHavingNetwork(mActivity)) {
            mProgressBar.setVisibility(View.INVISIBLE);
            MccDialog alertDialog = new MccDialog();
            alertDialog. createDialog(mActivity, R.string.title_activity_airfield_wikipedia, R.string.dialog_no_internet_content).show();
            mToastLoading.cancelToast();
            return;
        }
        byte[] airfieldCode = bundle.getByteArray(MCCPilotLogConst.AIRFIELD_CODE_KEY);
        mWvContent.setWebViewClient(new WebViewClient());
        mWvContent.getSettings().setJavaScriptEnabled(true);

        if (airfieldCode != null) {
            mAirfield = databaseManager.getAirfieldByCode(airfieldCode);
            mWvContent.addJavascriptInterface(new JavaScriptInterface(mActivity), "MY_JS");
            mWvContent.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    view.loadUrl("javascript:window.MY_JS.isAirportSite(document.body.innerHTML);");
                }
            });
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
            mWvContent.loadUrl(String.format("http://en.wikipedia.org/wiki/%s", mAirfield.getAFICAO()));
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

    public class JavaScriptInterface {
        Context mContext;

        /** Instantiate the interface and set the context */
        JavaScriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void isAirportSite(String body) {
            if (!body.contains("Airport type")) {
                if (mAirfield.getAFICAO().length() > 1) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mWvContent.setWebViewClient(new WebViewClient() {
                                @Override
                                public void onPageFinished(WebView view, String url) {
                                    view.loadUrl("javascript:window.MY_JS.isAirportExistInList(document.body.innerHTML);");
                                }
                            });
                            mWvContent.loadUrl(String.format("http://en.wikipedia.org/wiki/List_of_airports_by_ICAO_code:_%s", mAirfield.getAFICAO().substring(0, 1)));
                        }
                    });
                }
            } else {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mWvContent.setVisibility(View.VISIBLE);
                        mToastLoading.cancelToast();
                    }
                });
            }
        }

        @JavascriptInterface
        public void isAirportExistInList(final String body) {
            int pos = body.indexOf(String.format(">%s<", mAirfield.getAFICAO()));
            if (pos == -1) {
                MccDialog alertDialog = new MccDialog();
                alertDialog.createDialog(mActivity, R.string.title_activity_airfield_wikipedia,
                        String.format("%s is not listed on Wikipedia", mAirfield.getAFName())).show();
            } else {
                pos = body.indexOf("href=", pos);
                if (!"wiki".equals(body.substring(pos + 7, pos + 11))) {
                    /*MccDialog alertDialog = new MccDialog();
                    alertDialog.createDialog(mActivity, R.string.title_activity_airfield_wikipedia,
                            String.format("There is no Wikipedia page for %", mAirfield.getAFName())).show();*/
                    MccDialog.getOkAlertDialog(mActivity, R.string.title_activity_airfield_wikipedia, String.format("There is no Wikipedia page for %", mAirfield.getAFName())).show();
                } else {
                    final int finalPos = pos;
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mWvContent.setWebViewClient(new WebViewClient() {
                                @Override
                                public void onPageFinished(WebView view, String url) {
                                    mWvContent.setVisibility(View.VISIBLE);
                                    mToastLoading.cancelToast();
                                }
                            });
                            mWvContent.loadUrl(String.format("http://en.wikipedia.org/%s", body.substring(finalPos + 7, body.indexOf("\" title", finalPos))));
                        }
                    });
                }
            }
        }

        /** Show a dialog from the web page */
        @JavascriptInterface
        public void showDialog(String msg) {
            new AlertDialog.Builder(mContext)
                    .setTitle(R.string.title_activity_airfield_wikipedia)
                    .setMessage(msg)
                    .show();
        }

        /** Show a toast from the web page */
        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }
    }
}

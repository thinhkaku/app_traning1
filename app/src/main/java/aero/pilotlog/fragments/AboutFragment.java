package aero.pilotlog.fragments;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.Html;
import android.view.View;
import android.widget.TextView;


import aero.crewlounge.pilotlog.BuildConfig;
import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.utilities.DatabaseUtils;
import aero.pilotlog.utilities.StorageUtils;
import aero.pilotlog.widgets.MccDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tuan.pv on 2015/07/09.
 * About screen
 */
public class AboutFragment extends BaseMCCFragment {

    @Bind(R.id.tvTitle)
    TextView mTvTitle;
    @Bind(R.id.tvNumber)
    TextView mTvNumber;
    @Bind(R.id.tvContactHelpDesk)
    TextView mTvContactHelpDesk;
    @Bind(R.id.tvRateApp)
    TextView mTvRateApp;
    @Bind(R.id.tvVersion)
    TextView mTvVersion;
    @Bind(R.id.tvVisit)
    TextView mTvMccPilotLog;
    @Bind(R.id.tvLinkToTerms)
    TextView mTvLinkToTerms;

    private String mAppVersion;
    private String mAppNumberCode;
    private String mSyncId;

    @Override
    protected int getContentResId() {
        return R.layout.fragment_about;
    }

    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getFooterResId() {
        return FLAG_NO_RESOURCE;
    }

    @Override
    protected void onCreateFragment(View pContentView) {
        ButterKnife.bind(this, mViewContainer);

        initView();
    }

    /**
     * init view
     */
    public void initView() {
        mTvTitle.setText(getString(R.string.text_about));
        mTvTitle.setPadding(0, 0, 0, 0);
        mTvNumber.setVisibility(View.GONE);
        mAppVersion = BuildConfig.VERSION_NAME;
        mAppNumberCode = String.valueOf(BuildConfig.VERSION_CODE);
        mTvContactHelpDesk.setText(Html.fromHtml(getString(R.string.text_contact_helpdesk)));
        mTvRateApp.setText(Html.fromHtml(getString(R.string.text_rate_app)));
        mTvMccPilotLog.setText(Html.fromHtml(getString(R.string.text_mccpilotlog)));
        mTvLinkToTerms.setText(Html.fromHtml(getString(R.string.text_terms_and_cond)));
        mTvVersion.setText(getString(R.string.text_version) + MCCPilotLogConst.SPACE + mAppVersion + MCCPilotLogConst.NEW_LINE_CHARACTER
                + getString(R.string.text_build) + MCCPilotLogConst.SPACE + BuildConfig.VERSION_CODE + MCCPilotLogConst.NEW_LINE_CHARACTER + getString(R.string.text_mcc));
       // mSyncId = DatabaseManager.getInstance(mActivity).getSetting(SettingsConst.SYNC_ID).getData();
    }

    @Nullable
    @OnClick({R.id.ibMenu, R.id.tvContactHelpDesk, R.id.tvRateApp, R.id.tvVisit, R.id.tvLinkToTerms, R.id.ibVisitWeb, R.id.ibRateApp, R.id.ibContactHelpDesk, R.id.ibTermAndCond})
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.ibMenu:
                toggleMenu();
                break;
            case R.id.tvContactHelpDesk:
            case R.id.ibContactHelpDesk:
                // Email is sent to help desk when device id is not empty
//                if (!TextUtils.isEmpty(mSyncId)) {
                   // contactHelpDesk();
//                }
                replaceFragmentForMenu(isTablet() ? R.id.llRootMainTablet : R.id.fragmentMain, HelpFragment.class, null, FLAG_NOT_ADD_STACK);
                break;
            case R.id.tvRateApp:
                rateApp();
                break;
            case R.id.ibRateApp:
                rateApp();
                break;
            case R.id.tvVisit:
                gotoMCCSite();
                break;
            case R.id.ibVisitWeb:
                gotoMCCSite();
                break;
            case R.id.tvLinkToTerms:
                readTermsAndCond();
                break;
            case R.id.ibTermAndCond:
                readTermsAndCond();
                break;
            default:
                break;
        }
    }

    /**
     * Compose mail with several information to Helpdesk
     */
    public void contactHelpDesk() {
        Intent gmailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        //gmailIntent.setClassName(MCCPilotLogConst.PACKAGE_GMAIL, MCCPilotLogConst.ACTIVITY_GMAIL);
        gmailIntent = initIntentSendMail(gmailIntent);

        try {
            startActivity(gmailIntent);
        } catch (ActivityNotFoundException ex) {
            // handle error
            Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            intent.setType(MCCPilotLogConst.INTENT_TYPE);
            final PackageManager pm = mActivity.getPackageManager();
            final List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
            ResolveInfo best = null;

            for (final ResolveInfo info : matches) {
                if (info.activityInfo.packageName.endsWith(MCCPilotLogConst.DOT_GM) || info.activityInfo.packageName.endsWith(MCCPilotLogConst.INBOX) || info.activityInfo.name.toLowerCase()
                        .contains(MCCPilotLogConst.GMAIL)) {
                    best = info;
                    break;
                }
            }
            if (best != null) {
                intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                intent = initIntentSendMail(intent);

                startActivity(intent);
            } else {
                MccDialog.getOkAlertDialog(mActivity, R.string.msg_not_found_email_app).show();
            }
        }
    }

    /**
     * init intent send mail
     *
     * @param pIntent Intent
     * @return Intent
     */
    private Intent initIntentSendMail(Intent pIntent) {
        String subject, lineSep, header;

        subject = MCCPilotLogConst.PREFIX_SUBJECT + mSyncId + MCCPilotLogConst.SUFFIX_SUBJECT;
        lineSep = System.getProperty(MCCPilotLogConst.TEXT_LINE_SEPARATOR);
        header = MCCPilotLogConst.MCC_APP_NAME + lineSep +
                MCCPilotLogConst.TEXT_APP_VERSION + mAppVersion + "  (" + mAppNumberCode + ")" + lineSep +
                MCCPilotLogConst.TEXT_DEVICE + MCCPilotLogConst.TEXT_OS + android.os.Build.VERSION.RELEASE + "  (" + android.os.Build.MANUFACTURER + "  " + android.os.Build.MODEL + ")" + lineSep +
                MCCPilotLogConst.TEXT_SYNC_ID + mSyncId + lineSep + MCCPilotLogConst.LINE_EMAIL_HEADER + lineSep + MCCPilotLogConst.WRITE_MESSAGE_HERE + lineSep + lineSep + lineSep;
        pIntent.setType(MCCPilotLogConst.INTENT_TYPE);
        pIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        pIntent.putExtra(Intent.EXTRA_TEXT, header);
        pIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{MCCPilotLogConst.EMAIL_HELPDESK});

        String packageName = getApplication().getPackageName();
        ArrayList<Uri> uris = new ArrayList<>();
        Uri fileDbUri;
        File dbFolder = new File(StorageUtils.getStorageRootFolder(mActivity) + DatabaseUtils.DATABASES, DatabaseManager.DATABASE_VER_5);
        File fileDb = null;
        if (dbFolder.exists()) {
            fileDb = StorageUtils.compressFile(mActivity, dbFolder);
        }
        if (fileDb != null && fileDb.length() > 0) {
            fileDbUri = FileProvider.getUriForFile(mActivity, packageName, fileDb);
            uris.add(fileDbUri);
        }

        pIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

        return pIntent;
    }

    /**
     * Redirect to app mccPILOTLOG on google play
     */
    public void rateApp() {
        MccDialog.getOkCancelAlertDialog(mActivity, R.string.msg_confirm_rate_app, new MccDialog.MCCDialogCallBack() {
            @Override
            public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                if (pDialogType == DialogInterface.BUTTON_POSITIVE) {//Tap on "YES"
                    final String appPackageName = mActivity.getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MCCPilotLogConst.GOOGLE_MARKET1 + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MCCPilotLogConst.GOOGLE_MARKET2 + appPackageName)));
                    }
                }
            }
        }).show();
    }

    /**
     * Redirect to MCC site
     */
    public void gotoMCCSite() {
        try {
            Uri uri = Uri.parse(MCCPilotLogConst.MCC_SITE);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } catch (ActivityNotFoundException anfe) {
            MccDialog.getOkAlertDialog(mActivity, R.string.msg_not_found_browser).show();
        }
    }

    public void readTermsAndCond() {
        try {
            Uri uri = Uri.parse(MCCPilotLogConst.TERM_AND_COND_SITE);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } catch (ActivityNotFoundException anfe) {
            MccDialog.getOkAlertDialog(mActivity, R.string.msg_not_found_browser).show();
        }
    }

    @Override
    protected void onKeyBackPress() {
        finishFragment();
    }
}

package aero.pilotlog.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import aero.crewlounge.pilotlog.BuildConfig;
import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.tasks.DownloadAsync;
import aero.pilotlog.utilities.DatabaseUtils;
import aero.pilotlog.utilities.NetworkUtils;
import aero.pilotlog.utilities.ProgressDialogUtils;
import aero.pilotlog.utilities.StorageUtils;
import aero.pilotlog.utilities.Utils;
import aero.pilotlog.widgets.MccDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ngoc.dh on 7/13/2015.
 * Help fragment
 */

public class HelpFragment extends BaseMCCFragment {

    @Bind(R.id.tvTitle)
    TextView mTvTitle;
    @Bind(R.id.tvNumber)
    TextView mTvNumber;
    @Bind(R.id.llgetStart)
    LinearLayout mLlStart;
    @Bind(R.id.divStart)
    View mDiv;
    @Bind(R.id.tv_title)
    TextView mHeaderTvTitle;

    private static final int PROGRESS_BAR_MAX_LENGTH = 100;
    private static final int INITIAL_PROGRESS_BAR = 10;
    private static final int STRING_START = 16;
    private static final int STRING_END = 18;
    private ProgressDialog mProgressDialog;
    private Context mContext;

    private String mAppVersion;
    private String mAppNumberCode;
    private String mSyncId = STRING_EMPTY;

    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_help;
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

    private void initView() {
        mTvTitle.setText(getString(R.string.help_title_action_bar));
        mTvTitle.setPadding(0, 0, 0, 0);
        mTvNumber.setVisibility(View.GONE);
        mContext = mActivity;
        mAppVersion = BuildConfig.VERSION_NAME;
        mAppNumberCode = String.valueOf(BuildConfig.VERSION_CODE);
       /* Setting setting = new DatabaseManager(mActivity).getSetting(SettingsConst.SYNC_ID);
        if (setting != null) {
            mSyncId = setting.getData();
        }*/
        if (TextUtils.isEmpty(mSyncId) || MCCPilotLogConst.NULL.equalsIgnoreCase(mSyncId)) {
            mLlStart.setVisibility(View.VISIBLE);
            mDiv.setVisibility(View.VISIBLE);
        } else {
            mLlStart.setVisibility(View.GONE);
            mDiv.setVisibility(View.GONE);
        }
        mHeaderTvTitle.setText(R.string.help_title);
    }

    @Nullable
    @OnClick({R.id.ibMenu, R.id.llUserGuide, R.id.llSupport, R.id.llHelpdesk, R.id.llgetStart})
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.ibMenu:
                toggleMenu();
                break;
            case R.id.llUserGuide:
                createProgressDialog(mContext);
                viewUserGuide();
                break;
            case R.id.llSupport:
                Intent browserSupportIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(MCCPilotLogConst.URL_SUPPORT));
                startActivity(browserSupportIntent);
                break;
            case R.id.llgetStart:
                Intent browserStartIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(MCCPilotLogConst.URL_START));
                startActivity(browserStartIntent);
                break;
            case R.id.llHelpdesk:
//                if (!TextUtils.isEmpty(mSyncId)) {
                contactHelpDesk();
//                }
                break;
            default:
                break;
        }
    }

    /**
     * Create the progress dialog
     *
     * @param mContext Context
     */

    private void createProgressDialog(Context mContext) {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("Downloading ...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
    }

    /**
     * View the mobile user guide
     */
    public void viewUserGuide() {
        final int oldVersionNum = StorageUtils.getIntFromSharedPref(mContext, MCCPilotLogConst.USER_GUIDE_VERSION, MCCPilotLogConst.DEFAULT_GUIDE_VERSION);
        if (NetworkUtils.isHavingNetwork(mContext)) {
            new AsyncTaskDownloadVersion(mContext) {
                @Override
                public void onPostDownload(String newVersionString) {
                    if (!TextUtils.isEmpty(newVersionString)) {
                        int newVersionNum = Integer.parseInt(newVersionString);
                        if (newVersionNum == oldVersionNum) {
                            if (new File(MCCPilotLogConst.PDF_FILE_PATH).exists()) {
                                viewPdf(Uri.fromFile(new File(MCCPilotLogConst.PDF_FILE_PATH)));
                            } else {
                                new AsyncTaskDownloadHelp(mContext).execute(MCCPilotLogConst.PDF_USER_GUIDE);
                            }
                        } else {
                            //Show dialog when network is available and the file exists in the device
                            if (new File(MCCPilotLogConst.PDF_FILE_PATH).exists()) {
                                final File oldPdf = new File(MCCPilotLogConst.PDF_FILE_PATH);
                                oldPdf.delete();
                            }
                            final AsyncTaskDownloadHelp asyncTaskDownloadHelp = new AsyncTaskDownloadHelp(mContext);
                            asyncTaskDownloadHelp.execute(MCCPilotLogConst.PDF_USER_GUIDE);
                            StorageUtils.writeIntToSharedPref(mContext, MCCPilotLogConst.USER_GUIDE_VERSION, newVersionNum);
                        }
                    } else { //Download if
                        final AsyncTaskDownloadHelp asyncTaskDownloadHelp = new AsyncTaskDownloadHelp(mContext);
                        asyncTaskDownloadHelp.execute(MCCPilotLogConst.PDF_USER_GUIDE);
                    }
                }
            }.execute("http://www.crewlounge.aero/docpdf/mccpilotlog_userguide.txt");

        } else { // if network is unavailable, open the existing file if there is one
            if (new File(MCCPilotLogConst.PDF_FILE_PATH).exists()) {
                viewPdf(Uri.fromFile(new File(MCCPilotLogConst.PDF_FILE_PATH)));
            } else {
                Utils.showToast(mContext, R.string.unavailable_network);
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
        /*header = MCCPilotLogConst.TEXT_SYNC_ID + mSyncId + lineSep + MCCPilotLogConst.TEXT_APP_VERSION + mAppVersion + lineSep +
                MCCPilotLogConst.TEXT_DEVICE + android.os.Build.MANUFACTURER + "  " + android.os.Build.MODEL + lineSep +
                MCCPilotLogConst.TEXT_OS + android.os.Build.VERSION.RELEASE;*/
        header = MCCPilotLogConst.MCC_APP_NAME + lineSep +
                MCCPilotLogConst.TEXT_APP_VERSION + mAppVersion + "  (" + mAppNumberCode + ")" + lineSep +
                MCCPilotLogConst.TEXT_DEVICE + MCCPilotLogConst.TEXT_OS + android.os.Build.VERSION.RELEASE + "  (" + android.os.Build.MANUFACTURER + "  " + android.os.Build.MODEL + ")" + lineSep +
                MCCPilotLogConst.TEXT_SYNC_ID + mSyncId + lineSep + MCCPilotLogConst.LINE_EMAIL_HEADER + lineSep + MCCPilotLogConst.WRITE_MESSAGE_HERE + lineSep + lineSep + lineSep;
        pIntent.setType(MCCPilotLogConst.INTENT_TYPE);
        pIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        pIntent.putExtra(Intent.EXTRA_TEXT, header);
        pIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{MCCPilotLogConst.EMAIL_HELPDESK});

        ArrayList<Uri> uris = new ArrayList<>();
        String packageName = getApplication().getPackageName();
        Uri fileDbUri;
        File dbFolder = new File(StorageUtils.getStorageRootFolder(mActivity) + DatabaseUtils.DATABASES, DatabaseManager.DATABASE_VER_5);
        File fileDb = null;
        if (dbFolder.exists()) {
            fileDb = StorageUtils.compressFile(mActivity, dbFolder);
        }
        if (fileDb != null && fileDb.length() > 0) {
            try{
                fileDbUri = FileProvider.getUriForFile(mActivity, packageName, fileDb);
                uris.add(fileDbUri);
            }catch (Exception ex){
                ex.printStackTrace();
            }

        }

        Uri fileUri;
        File xmlPCFolder = new File(StorageUtils.getStorageRootFolder(mActivity), MCCPilotLogConst.XMLPC_FOLDER);
        File filePC = null;
        if (xmlPCFolder.exists()) {
            filePC = StorageUtils.compressFolder(mActivity, xmlPCFolder);
        }
        if (filePC != null) {// && filePC.length() > 0) {
            fileUri = FileProvider.getUriForFile(mActivity, packageName, filePC);
            uris.add(fileUri);
        }

        pIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

        return pIntent;
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
     * Check if Google Play is installed
     */

    public static boolean isGooglePlayInstalled(Context context) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed;
        try {
            PackageInfo info = pm.getPackageInfo(MCCPilotLogConst.ANDROID_PACKAGE_LINK, PackageManager.GET_ACTIVITIES);
            String label = (String) info.applicationInfo.loadLabel(pm);
            app_installed = (label != null && !label.equals(R.string.market_label));
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    /**
     * Open the existing PDF file
     */
    private void viewPdf(Uri file) {
        Intent intent;
        intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(file, MCCPilotLogConst.INTENT_TYPE_PDF);
        try {
            mContext.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // No application to view, ask to download one
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(R.string.no_application_found);
            builder.setMessage(R.string.download_from_market);
            builder.setPositiveButton(R.string.accept_download,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Download application if Google play is available
                            if (isGooglePlayInstalled(mContext)) {
                                Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                                marketIntent
                                        .setData(Uri
                                                .parse(MCCPilotLogConst.GOOGLE_MARKET_PDF));
                                mContext.startActivity(marketIntent);
                            } else { // Return to menu screen if Google play is unavailable
                                Toast.makeText(mContext, R.string.unavailable_market, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
            builder.setNegativeButton(R.string.refuse_download, null);
            builder.create().show();
        }
    }

    /**
     * AsyncTask to download user guide version
     */
    public abstract class AsyncTaskDownloadVersion extends AsyncTask<String, Void, String> {
        private Context context;
        private String versionContent;
        private String userGuideVersion = null;
        private ProgressDialogUtils progressDialog;

        public AsyncTaskDownloadVersion(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialogUtils((Activity) context);
            progressDialog.showProgressDialog(context.getString(R.string.help_download_version));
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                versionContent = DownloadAsync.downloadString(params[0]);
                if (!TextUtils.isEmpty(versionContent)) {
                    if (versionContent.length() > STRING_START) {
                        userGuideVersion = versionContent.substring(STRING_START, STRING_END);
                    }
                }
                return userGuideVersion;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.hideProgressDialog();
            onPostDownload(s);
        }

        public abstract void onPostDownload(String s);
    }

    /**
     * Use AsyncTask to download user guide pdf file
     */
    public class AsyncTaskDownloadHelp extends AsyncTask<String, Integer, String> {
        private Context context;
        private PowerManager.WakeLock mWakeLock;

        /**
         * Constructor
         *
         * @param context context
         */
        public AsyncTaskDownloadHelp(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            publishProgress(INITIAL_PROGRESS_BAR);

            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return R.string.return_http + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream(MCCPilotLogConst.PDF_FILE_PATH);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }

            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(PROGRESS_BAR_MAX_LENGTH);
            mProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mWakeLock.release();
            mProgressDialog.dismiss();
            viewPdf(Uri.fromFile(new File(MCCPilotLogConst.PDF_FILE_PATH)));
            if (result != null) {
                Toast.makeText(context, R.string.user_guide_download_error + result, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, R.string.user_guide_downloaded, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onKeyBackPress() {
        finishFragment();
    }
}


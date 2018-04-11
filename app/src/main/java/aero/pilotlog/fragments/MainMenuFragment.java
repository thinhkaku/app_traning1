package aero.pilotlog.fragments;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import aero.crewlounge.pilotlog.BuildConfig;
import aero.crewlounge.pilotlog.R;
import aero.pilotlog.activities.SignatureActivity;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.entities.Qualification;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.utilities.OrientationUtils;
import aero.pilotlog.utilities.ProfileUtils;
import aero.pilotlog.widgets.MccDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tuan.pv on 2015/07/06.
 * Main menu
 */
public class MainMenuFragment extends BaseMCCFragment {

    /*@Bind(R.id.tvTitleLogo)
    TextView mTvTitleLogo;*/
    @Bind(R.id.tv_about_description)
    TextView mTvAboutDescription;
    @Bind(R.id.ivQualificationStatus)
    ImageView ivQualificationStatus;
    @Bind(R.id.tvUsername)
    TextView tvUsername;
    @Bind(R.id.tvAirline)
    TextView tvAirline;
    private ProfileUtils profileUtils;

    private View mView;
    private aero.pilotlog.databases.manager.DatabaseManager mDatabaseManager;

    @Override
    protected int getContentResId() {
        return R.layout.fragment_main_menu;
    }

    @Override
    protected int getHeaderResId() {
        return FLAG_NO_RESOURCE;
    }

    @Override
    protected int getFooterResId() {
        return FLAG_NO_RESOURCE;
    }

    @Override
    protected void onCreateFragment(View pContentView) {
        ButterKnife.bind(this, mViewContainer);
        mDatabaseManager = aero.pilotlog.databases.manager.DatabaseManager.getInstance(mActivity);
        profileUtils = ProfileUtils.newInstance();
        tvUsername.setText(profileUtils.getUserProfileRespond().getUserProfile().getFirstName() + " " +
                profileUtils.getUserProfileRespond().getUserProfile().getLastName());
        String company = mDatabaseManager.getSetting("AirlineOperator").getData();
       tvAirline.setText(!TextUtils.isEmpty(company) ?
                company : "");

        //updateSyncId();
        if (!isTablet()) {
            OrientationUtils.lockOrientationPortrait(mActivity);
        }
        mTvAboutDescription.setText("Version " + BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")");

        List<Qualification> mListCertificate = mDatabaseManager.getCertificateRules();
        boolean isQualificationExpired = false;
        boolean isQualificationUpComing = false;
        for (Qualification qualification : mListCertificate) {
            if (qualification.getStatus() == Qualification.QualificationStatus.EXPIRED) {
                isQualificationExpired = true;
                break;
            }
            if (!isQualificationUpComing && qualification.getStatus() == Qualification.QualificationStatus.UPCOMING) {
                isQualificationUpComing = true;
            }
        }
        List<Qualification> mListProficiency = mDatabaseManager.getProficiencyRules();
        if (!isQualificationExpired) {
            for (Qualification qualification : mListProficiency) {
                if (qualification.getStatus() == Qualification.QualificationStatus.EXPIRED) {
                    isQualificationExpired = true;
                    break;
                }
                if (!isQualificationUpComing && qualification.getStatus() == Qualification.QualificationStatus.UPCOMING) {
                    isQualificationUpComing = true;
                }
            }
        }
        if (isQualificationExpired) {
            ivQualificationStatus.setVisibility(View.VISIBLE);
            ivQualificationStatus.setImageResource(android.R.color.transparent);
            ivQualificationStatus.setBackgroundResource(R.drawable.ic_expired);

            String settingLocalLastQualificationCheck = mDatabaseManager.getSettingLocal(MCCPilotLogConst.SETTING_CODE_LAST_QUALIFICATION_CHECK).getData();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date lastUpdate = new Date();
            try {
                lastUpdate = format.parse(settingLocalLastQualificationCheck);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Calendar calendarLastUpdate = Calendar.getInstance();
            calendarLastUpdate.setTime(lastUpdate);
            long diff = Calendar.getInstance().getTimeInMillis() - calendarLastUpdate.getTimeInMillis();
            long days = diff / (24 * 60 * 60 * 1000);
            if (days >= 1) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MccDialog.getAlertDialog(mActivity, R.string.qualification_dialog_warning_title, R.string.qualification_dialog_warning_content,
                                R.string.alert_view_button, R.string.alert_remindLater_button, MccDialog.FLAG_RESOURCE_NULL, new MccDialog.MCCDialogCallBack() {
                                    @Override
                                    public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                        Date today = new Date();
                                        mDatabaseManager.updateSettingLocal(MCCPilotLogConst.SETTING_CODE_LAST_QUALIFICATION_CHECK, format.format(today));
                                        if (pDialogType != DialogInterface.BUTTON_NEGATIVE) {
                                            Bundle bundle = new Bundle();
                                            replaceFragment(isTablet() ? R.id.rightContainerFragment : R.id.fragmentMain, QualificationFragment.class, bundle, FLAG_NOT_ADD_STACK);

                                        }
                                    }
                                }).show();
                    }
                });
            }
        } else if (isQualificationUpComing) {
            ivQualificationStatus.setVisibility(View.VISIBLE);
            ivQualificationStatus.setImageResource(android.R.color.transparent);
            ivQualificationStatus.setBackgroundResource(R.drawable.ic_upcoming);
        }
    }


  /*  public void updateSyncId() {
        String mSyncID = STRING_EMPTY;
        Setting setting = new DatabaseManager(mActivity).getSetting(SettingsConst.SYNC_ID);
        if (setting != null) {
            mSyncID = setting.getData();
        }
        if (TextUtils.isEmpty(mSyncID) || MCCPilotLogConst.NULL.equalsIgnoreCase(mSyncID)) {
            mSyncID = getString(R.string.app_name);
        }
        mTvTitleLogo.setText(mSyncID);
    }*/

    protected void onSlidingMenuClosed() {
        super.onSlidingMenuClosed();

        Bundle bundle = new Bundle();
        if (mView != null) {
            FragmentManager fm = mActivity.getSupportFragmentManager();
            //PL-613
            /*if (mView.getId() != R.id.llSign) {
                try {
                    List<android.support.v4.app.Fragment> fragmentList = fm.getFragments();
                    if (fragmentList != null && fragmentList.size() > 0) {
                        for (android.support.v4.app.Fragment fragment :
                                fragmentList) {
                            if (fragment != null && fragment.getTag() != null && !fragment.getTag().equalsIgnoreCase(MainMenuFragment.class.getName())
                                    && !fragment.getTag().equalsIgnoreCase(ContainerFragment.class.getName())) {
                                fm.beginTransaction().remove(fragment).commit();
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/
            //End PL-613
            //PL-410
            int backStackCount = fm.getBackStackEntryCount();
            if (backStackCount > 0) {
                fm.popBackStack();
            }
            //End PL-410
            switch (mView.getId()) {
                case R.id.llAirfields:
                    bundle.putInt(MCCPilotLogConst.SCREEN_VIEW_AS_TYPE, MCCPilotLogConst.LIST_MODE);
                    if (isTablet()) {
                        bundle.putInt(MCCPilotLogConst.SCREEN_TYPE, MCCPilotLogConst.BUTTON_AIRFIELDS);
                        replaceFragmentTablet(R.id.llRootMainTablet, ContainerFragment.class, bundle, FLAG_NOT_ADD_STACK);
                    } else {
                        //bundle.putInt(MCCPilotLogConst.SCREEN_VIEW_AS_TYPE, MCCPilotLogConst.SELECT_MODE);
                        bundle.putString(MCCPilotLogConst.SELECT_LIST_TYPE, MCCPilotLogConst.SELECT_LIST_TYPE_AIRFIELD_INFO);
                        replaceFragmentForMenu(R.id.fragmentMain, AirfieldsListFragment.class, bundle, FLAG_NOT_ADD_STACK);
                    }
                    break;
                case R.id.llAircraft:
                    bundle.putInt(MCCPilotLogConst.SCREEN_VIEW_AS_TYPE, MCCPilotLogConst.LIST_MODE);
                    if (isTablet()) {
                        bundle.putInt(MCCPilotLogConst.SCREEN_TYPE, MCCPilotLogConst.BUTTON_AIRCRAFT);
                        replaceFragmentTablet(R.id.llRootMainTablet, ContainerFragment.class, bundle, FLAG_NOT_ADD_STACK);
                    } else {
                        replaceFragmentForMenu(R.id.fragmentMain, AircraftListFragment.class, null, FLAG_NOT_ADD_STACK);
                    }
                    break;
                case R.id.llFlights:
                    bundle.putInt(MCCPilotLogConst.SCREEN_TYPE, MCCPilotLogConst.BUTTON_FLIGHT);
                    if (isTablet()) {
                        replaceFragmentTablet(R.id.llRootMainTablet, ContainerFragment.class, bundle, FLAG_NOT_ADD_STACK);
                    } else {
                        replaceFragmentForMenu(R.id.fragmentMain, FlightListFragment.class, bundle, FLAG_NOT_ADD_STACK);
                    }
                    break;
                case R.id.llWeather:
                    if (isTablet()) {
                        bundle.putInt(MCCPilotLogConst.SCREEN_TYPE, MCCPilotLogConst.BUTTON_WEATHER);
                        replaceFragmentTablet(R.id.llRootMainTablet, ContainerFragment.class, bundle, FLAG_NOT_ADD_STACK);
                    } else {
                        replaceFragmentForMenu(R.id.fragmentMain, WeatherListFragment.class, null, FLAG_NOT_ADD_STACK);
                    }
                    break;
                case R.id.llExpenses:
                    if (isTablet()) {
                        bundle.putInt(MCCPilotLogConst.SCREEN_TYPE, MCCPilotLogConst.BUTTON_EXPENSES);
                        replaceFragmentTablet(R.id.llRootMainTablet, ContainerFragment.class, bundle, FLAG_NOT_ADD_STACK);
                    } else {
                        replaceFragmentForMenu(R.id.fragmentMain, ExpenseListFragment.class, bundle, FLAG_NOT_ADD_STACK);
                    }
                    break;
              /*  case R.id.llSign:
//                    OrientationUtils.lockOrientationLandscape(mActivity);

                    bundle.putInt(MCCPilotLogConst.SIGNATURE_VIEW_TYPE, MCCPilotLogConst.SIGNATURE_FROM_MENU);
                    Intent intentSign = new Intent(mActivity, SignatureActivity.class);
                    intentSign.putExtra(MCCPilotLogConst.SIGNATURE_BUNDLE, bundle);
                    startActivity(intentSign);
//
                    break;*/
                case R.id.llPilots:
                    bundle.putInt(MCCPilotLogConst.SCREEN_VIEW_AS_TYPE, MCCPilotLogConst.LIST_MODE);
                    if (isTablet()) {
                        bundle.putInt(MCCPilotLogConst.SCREEN_TYPE, MCCPilotLogConst.BUTTON_PILOTS);
                        replaceFragmentTablet(R.id.llRootMainTablet, ContainerFragment.class, bundle, FLAG_NOT_ADD_STACK);
                    } else {
                        replaceFragmentForMenu(R.id.fragmentMain, PilotListFragment.class, null, FLAG_NOT_ADD_STACK);
                    }
                    break;
                case R.id.llDuty:
                    if (isTablet()) {
                        bundle.putInt(MCCPilotLogConst.SCREEN_TYPE, MCCPilotLogConst.BUTTON_DUTY);
                        replaceFragmentTablet(R.id.llRootMainTablet, ContainerFragment.class, bundle, FLAG_NOT_ADD_STACK);
                    } else {
                        replaceFragmentForMenu(R.id.fragmentMain, DutyFragment.class, bundle, FLAG_NOT_ADD_STACK);
                    }

                    break;
                case R.id.llLimit:
                    replaceFragmentForMenu(isTablet() ? R.id.llRootMainTablet : R.id.fragmentMain, LimitsFragment.class, bundle, FLAG_NOT_ADD_STACK);
                    break;
                case R.id.llTotals:
                    if (isTablet()) {
                        replaceFragmentTablet(R.id.llRootMainTablet, TotalsFragment.class, bundle, FLAG_NOT_ADD_STACK);
                    } else {
                        replaceFragmentForMenu(R.id.fragmentMain, TotalsFragment.class, bundle, FLAG_NOT_ADD_STACK);
                    }
                    break;
                case R.id.llLogbook:
                    bundle.putInt(MCCPilotLogConst.SCREEN_TYPE, MCCPilotLogConst.BUTTON_LOGBOOK);
                    if (isTablet()) {
                        replaceFragmentTablet(R.id.llRootMainTablet, ContainerFragment.class, bundle, FLAG_NOT_ADD_STACK);
                    } else {
                        replaceFragmentForMenu(R.id.fragmentMain, LogbooksListFragment.class, bundle, FLAG_NOT_ADD_STACK);
                    }
                    break;
                case R.id.llQualifications:
                    replaceFragmentForMenu(isTablet() ? R.id.llRootMainTablet : R.id.fragmentMain, QualificationFragment.class, bundle, FLAG_NOT_ADD_STACK);
                    break;
                case R.id.llDelay:
                    if (isTablet()) {
                        bundle.putInt(MCCPilotLogConst.SCREEN_TYPE, MCCPilotLogConst.BUTTON_DELAY);
                        replaceFragmentTablet(R.id.llRootMainTablet, ContainerFragment.class, bundle, FLAG_NOT_ADD_STACK);
                    } else {
                        replaceFragmentForMenu(R.id.fragmentMain, DelayListFragment.class, bundle, FLAG_NOT_ADD_STACK);
                    }
                  /*  if (isTablet()) {
                        bundle.putInt(MCCPilotLogConst.SCREEN_VIEW_AS_TYPE, MCCPilotLogConst.LIST_MODE);
                        bundle.putInt(MCCPilotLogConst.DELAY_OR_TAILS, MCCPilotLogConst.DELAY_ADAPTER);
                        bundle.putInt(MCCPilotLogConst.SCREEN_TYPE, MCCPilotLogConst.BUTTON_DELAY);
                        replaceFragmentTablet(R.id.llRootMainTablet, ContainerFragment.class, bundle, FLAG_NOT_ADD_STACK);
                    } else {
                        bundle.putInt(MCCPilotLogConst.SCREEN_VIEW_AS_TYPE, MCCPilotLogConst.LIST_MODE);
                        bundle.putInt(MCCPilotLogConst.DELAY_OR_TAILS, MCCPilotLogConst.DELAY_ADAPTER);
                        replaceFragmentForMenu(R.id.fragmentMain, DelayTailsListFragment.class, bundle, FLAG_NOT_ADD_STACK);
                    }*/
                    //bundle.putInt(MCCPilotLogConst.SCREEN_VIEW_AS_TYPE, MCCPilotLogConst.LIST_MODE);
                    //replaceFragmentForMenu(isTablet() ? R.id.llRootMainTablet : R.id.fragmentMain, DelayListFragment.class, bundle, FLAG_NOT_ADD_STACK);
                    break;
                case R.id.llSync:
                    if (isTablet()) {
                        bundle.putInt(MCCPilotLogConst.SCREEN_TYPE, MCCPilotLogConst.BUTTON_SYNC);
                        replaceFragmentTablet(R.id.llRootMainTablet, ContainerFragment.class, bundle, FLAG_NOT_ADD_STACK);
                    } else {
                        replaceFragmentForMenu(R.id.fragmentMain, SyncV5Fragment.class, bundle, FLAG_NOT_ADD_STACK);
                    }
                    /*Sync v3 apply to Sync ID with 1Digit, v4 - 2 digits */
                  /*  String syncId = DatabaseManager.getInstance(mActivity).getSetting(SettingsConst.SYNC_ID).getData();
                    if (syncId != null) {
                        if (isTablet()) {
//                            bundle.putInt(MCCPilotLogConst.SCREEN_TYPE, MCCPilotLogConst.BUTTON_SYNC);
//                            bundle.putString(MCCPilotLogConst.TEXT_SYNC_ID, syncId);
//                            replaceFragment(R.id.llRootMainTablet, ContainerFragment.class, bundle, FLAG_NOT_ADD_STACK);
                            replaceFragmentTablet(R.id.llRootMainTablet, syncId.length() == 16 ? SyncV4Fragment.class : SyncV3Fragment.class, bundle, FLAG_NOT_ADD_STACK);
                        } else {
                            replaceFragment(R.id.fragmentMain, syncId.length() == 16 ? SyncV4Fragment.class : SyncV3Fragment.class, bundle, FLAG_NOT_ADD_STACK);
                        }
                    }*/
                    /*if (isTablet()) {
                        //bundle.putInt(MCCPilotLogConst.SCREEN_TYPE, MCCPilotLogConst.BUTTON_SYNC);
                        //bundle.putString(MCCPilotLogConst.TEXT_SYNC_ID, syncId);
                        //replaceFragment(R.id.llRootMainTablet, ContainerFragment.class, bundle, FLAG_NOT_ADD_STACK);
                        replaceFragmentTablet(R.id.llRootMainTablet, SyncV3Fragment.class, bundle, FLAG_NOT_ADD_STACK);
                    } else {
                        replaceFragment(R.id.fragmentMain, SyncV3Fragment.class, bundle, FLAG_NOT_ADD_STACK);
                    }*/
                    break;
                case R.id.llHelp:
                    replaceFragmentForMenu(isTablet() ? R.id.llRootMainTablet : R.id.fragmentMain, HelpFragment.class, bundle, FLAG_NOT_ADD_STACK);
                    break;
                case R.id.llTails:
                    if (isTablet()) {
                        bundle.putInt(MCCPilotLogConst.SCREEN_TYPE, MCCPilotLogConst.BUTTON_TAILS);
                        replaceFragmentTablet(R.id.llRootMainTablet, ContainerFragment.class, bundle, FLAG_NOT_ADD_STACK);
                    } else {
                        replaceFragmentForMenu(R.id.fragmentMain, TailListFragment.class, bundle, FLAG_NOT_ADD_STACK);
                    }
                    //replaceFragmentForMenu(isTablet() ? R.id.llRootMainTablet : R.id.fragmentMain, TailListFragment.class, bundle, FLAG_NOT_ADD_STACK);
                    break;
                case R.id.llSettings:
                   /* if (isTablet()) {
                        replaceFragmentTablet(R.id.llRootMainTablet, SettingsFragment.class, bundle, FLAG_NOT_ADD_STACK);
                    } else {
                        replaceFragment(R.id.fragmentMain, SettingsFragment.class, FLAG_NOT_ADD_STACK);
                    }*/
                    replaceFragmentForMenu(isTablet() ? R.id.llRootMainTablet : R.id.fragmentMain, SettingAndToolFragment.class, null, FLAG_NOT_ADD_STACK);
                    break;
                case R.id.llAbout:
                    replaceFragmentForMenu(isTablet() ? R.id.llRootMainTablet : R.id.fragmentMain, AboutFragment.class, bundle, FLAG_NOT_ADD_STACK);
                    break;
                case R.id.ivLogo:
                    gotoMCCSite();
                    break;

                default:
                    break;
            }

            mView = null;
        }
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

    @Override
    protected void onSlidingMenuOpened() {
        super.onSlidingMenuOpened();
    }

    /**
     * Handle onclick button
     *
     * @param pView
     */
    @OnClick({R.id.llAirfields, R.id.llAircraft, R.id.llFlights, R.id.llWeather, /*R.id.llSign,*/ R.id.llPilots, R.id.llDuty, R.id.llExpenses
            , R.id.llTotals, R.id.llLogbook, R.id.llQualifications, R.id.llDelay, R.id.llSync, R.id.llHelp, R.id.llTails, R.id.llSettings, R.id.llAbout, R.id.ivLogo,
            R.id.llLimit})
    public void onClick(View pView) {
        toggleMenu();
        mView = pView;
    }

    @Override
    protected void onKeyBackPress() {
//        finishActivity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isTablet()) {
            OrientationUtils.lockOrientationPortrait(mActivity);
        }
    }
}

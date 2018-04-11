package aero.pilotlog.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.fragments.ContainerFragment;
import aero.pilotlog.fragments.PilotListFragment;
import aero.pilotlog.fragments.TotalsFragment;
import aero.pilotlog.utilities.KeyboardUtils;

import butterknife.ButterKnife;

/**
 * Created by tuan.pv on 2015/08/12.
 * Tablet version
 */
public class MainTabletActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tablet);
        ButterKnife.bind(this);
//        OrientationUtils.lockOrientationLandscape(this);
        setContentAboveNavigationBar();

        initDefaultView();

//        SimpleDateFormat sdf1 = new SimpleDateFormat("yyMMddHHmmss", Locale.US);
//        final String dateTime = sdf1.format(Calendar.getInstance().getTime());
//        LogUtils.e("now dateTime millisecond: " + DateTimeUtils.getDateLastSync(151206222123l));
    }

    /**
     * Init view
     */
    public void initDefaultView() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        ContainerFragment containerFragment = new ContainerFragment();
        //bundle.putInt(MCCPilotLogConst.SCREEN_TYPE, MCCPilotLogConst.BUTTON_FLIGHT);
        //JIRA PL-741
        DatabaseManager mDbManager = new DatabaseManager(this);
        String mLandingScreen = "";
        try {
            mLandingScreen = mDbManager.getSettingLocal("LandingPage").getData();
        } catch (Exception e) {
        }
        switch (mLandingScreen) {
            case "Sync":
               /* String syncId = DatabaseManager.getInstance(this).getSetting(SettingsConst.SYNC_ID).getData();
                if (syncId != null) {
                    if(syncId.length() == 16){
                        SyncV4Fragment syncV4Fragment = new SyncV4Fragment();
                        syncV4Fragment.setArguments(bundle);
                        ft.replace(R.id.llRootMainTablet, syncV4Fragment, SyncV4Fragment.class.getName()).commit();
                    }else {*/
                       /* SyncV3Fragment syncV3Fragment = new SyncV3Fragment();
                        syncV3Fragment.setArguments(bundle);
                        ft.replace(R.id.llRootMainTablet, syncV3Fragment, SyncV3Fragment.class.getName()).commit();*/
               /*     }
                }*/
                break;
            case "Flights":
                bundle.putInt(MCCPilotLogConst.SCREEN_TYPE, MCCPilotLogConst.BUTTON_FLIGHT);
                break;
            case "Pilots":
                bundle.putInt(MCCPilotLogConst.SCREEN_VIEW_AS_TYPE, MCCPilotLogConst.LIST_MODE);
                bundle.putInt(MCCPilotLogConst.SCREEN_TYPE, MCCPilotLogConst.BUTTON_PILOTS);
                break;
            case "Airfields":
                bundle.putInt(MCCPilotLogConst.SCREEN_VIEW_AS_TYPE, MCCPilotLogConst.LIST_MODE);
                bundle.putInt(MCCPilotLogConst.SCREEN_TYPE, MCCPilotLogConst.BUTTON_AIRFIELDS);
                break;
            case "Totals":
                TotalsFragment totalsFragment = new TotalsFragment();
                ft.replace(R.id.llRootMainTablet,totalsFragment, TotalsFragment.class.getName()).commit();
                break;
            case "Logbook":
                bundle.putInt(MCCPilotLogConst.SCREEN_TYPE, MCCPilotLogConst.BUTTON_LOGBOOK);
                break;
            case "Duty":
                bundle.putInt(MCCPilotLogConst.SCREEN_TYPE, MCCPilotLogConst.BUTTON_DUTY);
                break;
            case "Qualifications":
                bundle.putInt(MCCPilotLogConst.SCREEN_TYPE, MCCPilotLogConst.BUTTON_QUALIFICATION);
                break;
            case "Weather":
                bundle.putInt(MCCPilotLogConst.SCREEN_TYPE, MCCPilotLogConst.BUTTON_WEATHER);
                break;
            case "Delay":
                bundle.putInt(MCCPilotLogConst.SCREEN_VIEW_AS_TYPE, MCCPilotLogConst.LIST_MODE);
                bundle.putInt(MCCPilotLogConst.DELAY_OR_TAILS, MCCPilotLogConst.DELAY_ADAPTER);
                bundle.putInt(MCCPilotLogConst.SCREEN_TYPE, MCCPilotLogConst.BUTTON_DELAY);
                break;
            case "Tails":
                bundle.putInt(MCCPilotLogConst.DELAY_OR_TAILS, MCCPilotLogConst.TAILS_ADAPTER);
                bundle.putInt(MCCPilotLogConst.SCREEN_TYPE, MCCPilotLogConst.BUTTON_TAILS);
                break;
            default:
                bundle.putInt(MCCPilotLogConst.SCREEN_TYPE, MCCPilotLogConst.BUTTON_FLIGHT);
                break;
        }
        //End JIRA PL-741
        if (!mLandingScreen.equalsIgnoreCase("totals") && !mLandingScreen.equalsIgnoreCase("sync"))
        {
            containerFragment.setArguments(bundle);
            ft.replace(R.id.llRootMainTablet, containerFragment).commit();
        }

        callStartPopup();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.llRootMainTablet).getChildFragmentManager().findFragmentById(R.id.leftContainerFragment);
        if (f != null && f instanceof PilotListFragment) {
            ((PilotListFragment) f).setContactInfo(resultCode, data);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        KeyboardUtils.hideKeyboard(this);
    }

}
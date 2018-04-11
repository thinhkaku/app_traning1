package aero.pilotlog.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.fragments.AirfieldsListFragment;
import aero.pilotlog.fragments.DelayTailsListFragment;
import aero.pilotlog.fragments.DutyFragment;
import aero.pilotlog.fragments.FlightListFragment;
import aero.pilotlog.fragments.LogbooksListFragment;
import aero.pilotlog.fragments.PilotListFragment;
import aero.pilotlog.fragments.TailListFragment;
import aero.pilotlog.fragments.TailsFragment;
import aero.pilotlog.fragments.TotalsFragment;
import aero.pilotlog.fragments.WeatherListFragment;
import aero.pilotlog.utilities.KeyboardUtils;
import aero.pilotlog.utilities.OrientationUtils;


import butterknife.ButterKnife;

/**
 * Created by tuan.pv on 2015/07/06.
 * Phone version
 */
public class MainActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OrientationUtils.lockOrientationPortrait(this);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setContentAboveNavigationBar();

        initDefaultView();
    }

    public void initDefaultView() {
        Bundle bundle = new Bundle();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        /*LogbookListFragment logbookListFragment = new LogbookListFragment();
        logbookListFragment.setArguments(bundle);
        ft.replace(R.id.fragmentMain, logbookListFragment, LogbookListFragment.class.getName()).commit();*/
        //JIRA PL-741
        DatabaseManager mDbManager = new DatabaseManager(this);


        String mLandingScreen = "";
       /* try {
            mLandingScreen = mDbManager.getSetting(SettingsConst.LANDING_SCREEN).getData();
        }catch (Exception e)
        {
        }*/
        //mLandingScreen = StorageUtils.getStringFromSharedPref(this, MCCPilotLogConst.LANDING_SCREEN, "Flights");
        try {
            mLandingScreen = mDbManager.getSettingLocal("LandingPage").getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (mLandingScreen) {
            case "Sync":
                /*String syncId = DatabaseManager.getInstance(this).getSetting(SettingsConst.SYNC_ID).getData();
                if (syncId != null) {
                    if(syncId.length() == 16){
                        SyncV4Fragment syncV4Fragment = new SyncV4Fragment();
                        syncV4Fragment.setArguments(bundle);
                        ft.replace(R.id.fragmentMain, syncV4Fragment, SyncV4Fragment.class.getName()).commit();
                    }else {*/
             /*   SyncV3Fragment syncV3Fragment = new SyncV3Fragment();
                syncV3Fragment.setArguments(bundle);
                ft.replace(R.id.fragmentMain, syncV3Fragment, SyncV3Fragment.class.getName()).commit();*/
                /*    }
                }*/
                break;
            case "Flights":
                //bundle.putInt(MCCPilotLogConst.SCREEN_TYPE, MCCPilotLogConst.BUTTON_FLIGHT);
                FlightListFragment flightListFragment = new FlightListFragment();
                //flightListFragment.setArguments(bundle);
                ft.replace(R.id.fragmentMain, flightListFragment, FlightListFragment.class.getName()).commit();
                //replaceFragment(R.id.fragmentMain, FlightListFragment.class, bundle, FLAG_NOT_ADD_STACK);
                break;
            case "Pilots":
                bundle.putInt(MCCPilotLogConst.SCREEN_VIEW_AS_TYPE, MCCPilotLogConst.LIST_MODE);
                PilotListFragment pilotListFragment = new PilotListFragment();
                pilotListFragment.setArguments(bundle);
                ft.replace(R.id.fragmentMain, pilotListFragment, PilotListFragment.class.getName()).commit();
                break;
            case "Airfields":
                bundle.putInt(MCCPilotLogConst.SCREEN_VIEW_AS_TYPE, MCCPilotLogConst.LIST_MODE);
                AirfieldsListFragment airfieldListFragment = new AirfieldsListFragment();
                airfieldListFragment.setArguments(bundle);
                ft.replace(R.id.fragmentMain, airfieldListFragment, AirfieldsListFragment.class.getName()).commit();
                break;
            case "Totals":
                TotalsFragment totalsFragment = new TotalsFragment();
                totalsFragment.setArguments(bundle);
                ft.replace(R.id.fragmentMain, totalsFragment, TotalsFragment.class.getName()).commit();
                break;
            case "Logbook":
                //bundle.putInt(MCCPilotLogConst.SCREEN_TYPE, MCCPilotLogConst.BUTTON_LOGBOOK);
                LogbooksListFragment logbookListFragment = new LogbooksListFragment();
                //logbookListFragment.setArguments(bundle);
                ft.replace(R.id.fragmentMain, logbookListFragment, LogbooksListFragment.class.getName()).commit();
                break;
            case "Duty":
                DutyFragment dutyFragment = new DutyFragment();
                dutyFragment.setArguments(bundle);
                ft.replace(R.id.fragmentMain, dutyFragment, DutyFragment.class.getName()).commit();
                break;
          /*  case "Currency":
                CurrenciesFragment currenciesFragment = new CurrenciesFragment();
                currenciesFragment.setArguments(bundle);
                ft.replace(R.id.fragmentMain, currenciesFragment, CurrenciesFragment.class.getName()).commit();
                break;*/
            case "Weather":
                WeatherListFragment weatherListFragment = new WeatherListFragment();
                weatherListFragment.setArguments(bundle);
                ft.replace(R.id.fragmentMain, weatherListFragment, WeatherListFragment.class.getName()).commit();
                break;
            case "Delay":
                bundle.putInt(MCCPilotLogConst.SCREEN_VIEW_AS_TYPE, MCCPilotLogConst.LIST_MODE);
                bundle.putInt(MCCPilotLogConst.DELAY_OR_TAILS, MCCPilotLogConst.DELAY_ADAPTER);
                DelayTailsListFragment delayListFragment = new DelayTailsListFragment();
                delayListFragment.setArguments(bundle);
                ft.replace(R.id.fragmentMain, delayListFragment, DelayTailsListFragment.class.getName()).commit();
                break;
            case "Tails":
                bundle.putInt(MCCPilotLogConst.SCREEN_TYPE, MCCPilotLogConst.BUTTON_TAILS);
                TailListFragment tailsListFragment = new TailListFragment();
                tailsListFragment.setArguments(bundle);
                ft.replace(R.id.fragmentMain, tailsListFragment, TailListFragment.class.getName()).commit();
                break;
            default:
                bundle.putInt(MCCPilotLogConst.SCREEN_TYPE, MCCPilotLogConst.BUTTON_FLIGHT);
                FlightListFragment flightFragment = new FlightListFragment();
                flightFragment.setArguments(bundle);
                ft.replace(R.id.fragmentMain, flightFragment, FlightListFragment.class.getName()).commit();
                break;
        }
        //End JIRA PL-741
        callStartPopup();

    }

    @Override
    protected void onStop() {
        super.onStop();
        KeyboardUtils.hideKeyboard(this);
    }


}
package aero.pilotlog.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tuan.pv on 2015/07/31.
 * Container fragment
 */
public class ContainerFragment extends BaseMCCFragment {

    @Bind(R.id.leftContainerFragment)
    FrameLayout mLeftContainerFragment;
    @Bind(R.id.rightContainerFragment)
    FrameLayout mRightContainerFragment;

    private int mScreenType;

    @Override
    protected int getContentResId() {
        return R.layout.fragment_container_tablet;
    }

    @Override
    protected boolean ignoreBaseContainerLayout() {
        return true;
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
    protected void onCreateFragment(final View pContentView) {
        ButterKnife.bind(this, mViewContainer);
        final Bundle bundle = getArguments();
        if (bundle != null) {
            updateWidthLayoutTablet();
            mScreenType = bundle.getInt(MCCPilotLogConst.SCREEN_TYPE);

            initContentLeft(bundle);
            initContentRight(bundle);

        }
    }

    /**
     * init and replace fragment left
     *
     * @param arguments
     */
    public void initContentLeft(Bundle arguments) {
        final FragmentManager fm = mActivity.getSupportFragmentManager();
        final Bundle bundle = arguments != null ? arguments : new Bundle();
        Class<? extends BaseFragment> fragment = null;
        switch (mScreenType) {
            case MCCPilotLogConst.BUTTON_DELAY:
                fragment = DelayListFragment.class;
                break;
            case MCCPilotLogConst.BUTTON_TAILS:
                fragment = TailListFragment.class;
                break;
            case MCCPilotLogConst.BUTTON_FLIGHT:
                //fragment = LogbooksListFragment.class;
                fragment = FlightListFragment.class;
                break;
            case MCCPilotLogConst.BUTTON_AIRFIELDS:
                fragment = AirfieldsListFragment.class;
                break;
            case MCCPilotLogConst.BUTTON_PILOTS:
                fragment = PilotListFragment.class;
                break;
            case MCCPilotLogConst.BUTTON_WEATHER:
                fragment = WeatherListFragment.class;
                break;
            case MCCPilotLogConst.BUTTON_AIRCRAFT:
                fragment = AircraftListFragment.class;
                break;
            case MCCPilotLogConst.BUTTON_LOGBOOK:
                fragment = LogbooksListFragment.class;
                break;
            case MCCPilotLogConst.BUTTON_QUALIFICATION:
                //fragment = CurrenciesFragment.class;
                fragment = QualificationFragment.class;
                break;
            case MCCPilotLogConst.BUTTON_DUTY:
                //fragment = DutyLeftTabletFragment.class;
                fragment = DutyFragment.class;
                break;
            case MCCPilotLogConst.BUTTON_LIMITS:
                //fragment = DutyLeftTabletFragment.class;
                fragment = LimitsFragment.class;
                break;
            case MCCPilotLogConst.BUTTON_EXPENSES:
                //fragment = DutyLeftTabletFragment.class;
                fragment = ExpenseListFragment.class;
                break;
            case MCCPilotLogConst.BUTTON_SYNC:
                //fragment = DutyLeftTabletFragment.class;
                fragment = SyncV5Fragment.class;
                break;
            default:
                break;
        }

        replaceFragmentTablet(R.id.leftContainerFragment, fragment, bundle, fm, FLAG_NOT_ADD_STACK);
    }

    /**
     * init and replace fragment right
     *
     * @param arguments arguments
     */
    public void initContentRight(Bundle arguments) {
        final FragmentManager fm = mActivity.getSupportFragmentManager();
        final Bundle bundle = arguments != null ? arguments : new Bundle();
        Class<? extends BaseFragment> fragment = null;
        switch (mScreenType) {
            case MCCPilotLogConst.BUTTON_DELAY:
            case MCCPilotLogConst.BUTTON_TAILS:
                fragment = DelayTailsSearchFragment.class;
                break;
            case MCCPilotLogConst.BUTTON_FLIGHT:
                bundle.putInt(MCCPilotLogConst.FLIGHT_SCREEN_MODE, FlightAddsFragment.MODE_ADD);
//                fragment = FlightAddFragment.class;
                fragment = FlightAddEmptyFragment.class;
                break;
            case MCCPilotLogConst.BUTTON_AIRFIELDS:
                fragment = AirfieldEmptyFragment.class;
//                fragment = AirfieldInfoFragment.class;
                break;
            case MCCPilotLogConst.BUTTON_PILOTS:
//                bundle.putString(MCCPilotLogConst.PILOT_CODE_KEY, "1");
//                fragment = PilotInfoFragment.class;
                fragment = PilotInfoEmptyFragment.class;
                break;
            case MCCPilotLogConst.BUTTON_WEATHER:
//                bundle.putInt(MCCPilotLogConst.WEATHER_VIEW_TYPE, AirfieldWeatherFragment.VIEW_EXISTED_REPORT_EMPTY);
//                fragment = AirfieldWeatherFragment.class;
                fragment = AirfieldWeatherEmptyFragment.class;
                break;
            case MCCPilotLogConst.BUTTON_AIRCRAFT:
                fragment = AircraftInfoEmptyFragment.class;
                break;
            case MCCPilotLogConst.BUTTON_LOGBOOK:
                fragment = LogbookFlightViewEmptyFragment.class;
                break;
            case MCCPilotLogConst.BUTTON_QUALIFICATION:
                fragment = CurrenciesRemarkFragment.class;
                break;
            case MCCPilotLogConst.BUTTON_DUTY:
                //fragment = DutyFragment.class;
                fragment = FlightAddEmptyFragment.class;
                break;
            case MCCPilotLogConst.BUTTON_LIMITS:
                //fragment = DutyLeftTabletFragment.class;
                fragment = FlightAddEmptyFragment.class;
                break;
            case MCCPilotLogConst.BUTTON_EXPENSES:
                //fragment = DutyLeftTabletFragment.class;
                fragment = FlightAddEmptyFragment.class;
            case MCCPilotLogConst.BUTTON_SYNC:
                //fragment = DutyLeftTabletFragment.class;
                fragment = FlightAddEmptyFragment.class;
                break;
            default:
                break;
        }

        replaceFragmentTablet(R.id.rightContainerFragment, fragment, bundle, fm, FLAG_NOT_ADD_STACK);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateWidthLayoutTablet();
    }

    /**
     * update again width layout tablet when rotate screen.
     */
    private void updateWidthLayoutTablet() {
        LinearLayout.LayoutParams lpLeft, lpRight;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            lpLeft = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 3);
            lpRight = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 2);
        } else {
            lpLeft = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 4);
            lpRight = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 3);
        }
        mLeftContainerFragment.setLayoutParams(lpLeft);
        mRightContainerFragment.setLayoutParams(lpRight);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onKeyBackPress() {
        // Not Handle key back press in this fragment
        // super.onKeyBackPress();
    }
}

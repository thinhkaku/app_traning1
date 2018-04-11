package aero.pilotlog.fragments;

import android.view.View;

import aero.crewlounge.pilotlog.R;

/**
 * Created on 2/25/2016.
 * Airfield Weather Empty
 */
public class AirfieldWeatherEmptyFragment extends BaseMCCFragment {
    @Override
    protected int getHeaderResId() {
        return FLAG_NO_RESOURCE;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_airfield_weather_empty;
    }

    @Override
    protected int getFooterResId() {
        return FLAG_NO_RESOURCE;
    }

    @Override
    protected void onCreateFragment(View pContentView) {

    }
}
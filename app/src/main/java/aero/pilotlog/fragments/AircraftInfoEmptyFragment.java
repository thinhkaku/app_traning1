package aero.pilotlog.fragments;

import android.view.View;

import aero.crewlounge.pilotlog.R;

/**
 * Created by tuan.pv on 2015/08/28.
 * Aircraft empty detail
 */
public class AircraftInfoEmptyFragment extends BaseMCCFragment {
    @Override
    protected int getHeaderResId() {
        return FLAG_NO_RESOURCE;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_aircraft_info_empty;
    }

    @Override
    protected int getFooterResId() {
        return FLAG_NO_RESOURCE;
    }

    @Override
    protected void onCreateFragment(View pContentView) {

    }
}

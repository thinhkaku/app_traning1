package aero.pilotlog.fragments;


import android.support.v4.app.Fragment;
import android.view.View;

import aero.crewlounge.pilotlog.R;

import java.text.ParseException;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoogleMapFragment extends BaseMCCFragment {


    public GoogleMapFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getHeaderResId() {
        return 0;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_google_map;
    }

    @Override
    protected int getFooterResId() {
        return 0;
    }

    @Override
    protected void onCreateFragment(View pContentView) throws ParseException {
        ButterKnife.bind(this, mViewContainer);
    }
}

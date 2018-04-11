package aero.pilotlog.interfaces;

import android.view.View;

import aero.pilotlog.models.FlightModel;

/**
 * Created by phuc.dd on 6/23/2017.
 */

public interface ISwipeLayoutFlightCallback {
    void onDeleteRecord(View pView, FlightModel pFlight, int pPosition);
}



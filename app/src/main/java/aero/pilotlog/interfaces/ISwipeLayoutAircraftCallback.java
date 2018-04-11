package aero.pilotlog.interfaces;

import android.view.View;

import aero.pilotlog.databases.entities.Aircraft;

/**
 * Created by phuc.dd on 8/24/2017.
 */

public interface ISwipeLayoutAircraftCallback {
    void onDelete(View pView, Aircraft pModel, int PIndex);
    void onDeActivate(View pView, Aircraft pModel, int PIndex);
}

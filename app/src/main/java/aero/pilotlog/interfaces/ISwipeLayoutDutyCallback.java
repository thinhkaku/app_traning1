package aero.pilotlog.interfaces;

import android.view.View;

import aero.pilotlog.models.DutyModels;
import aero.pilotlog.models.FlightModel;

/**
 * Created by phuc.dd on 7/20/2017.
 */

public interface ISwipeLayoutDutyCallback {
    void onDeleteRecord(View pView, DutyModels duty, int pPosition);

    void onDeleteChildRecord(View pView, DutyModels duty, FlightModel flightModel, int pPosition);
}

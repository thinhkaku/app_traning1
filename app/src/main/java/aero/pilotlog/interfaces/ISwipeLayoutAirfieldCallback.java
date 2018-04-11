package aero.pilotlog.interfaces;
import android.view.View;
import aero.pilotlog.databases.entities.Airfield;

/**
 * Created by phuc.dd on 3/13/2017.
 */
public interface ISwipeLayoutAirfieldCallback {
    void onDelete(View pView, Airfield pModel, int PIndex);
}

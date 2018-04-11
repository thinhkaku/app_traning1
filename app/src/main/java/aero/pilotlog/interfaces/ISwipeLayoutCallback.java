package aero.pilotlog.interfaces;

import android.view.View;

import aero.pilotlog.databases.entities.WeatherLocal;

/**
 * Created by tuan.pv on 2015/07/22.
 */
public interface ISwipeLayoutCallback {
    void onDelete(View pView, WeatherLocal pModel, int PIndex);
}

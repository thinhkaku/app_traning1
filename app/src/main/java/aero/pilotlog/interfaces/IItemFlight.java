package aero.pilotlog.interfaces;

import android.view.View;

import aero.pilotlog.widgets.ItemFlightView;

/**
 * Created by tuan.pv on 2015/08/20.
 */
public interface IItemFlight {

    /**
     * Interface method for left icon click
     * @param pView view which contains icon
     */
    void onClickFlightIcon1(View pView);

    /**
     * Interface method for right icon click
     * @param pView view which contains icon
     */
    void onClickFlightIcon2(ItemFlightView pView, boolean pCheck);
}

package aero.pilotlog.interfaces;

import aero.pilotlog.widgets.ItemsFlightView;
import android.view.View;

/**
 * Created by phuc.dd on 3/27/2017.
 */
public interface IItemsFlight {

    void onClickIconTop(ItemsFlightView itemsFlightView);

    void onClickIconBottom(ItemsFlightView itemsFlightView);

    void onFinishInput(ItemsFlightView itemsFlightView, boolean isChangeMinuteTotal);

    void onShowInfoPage(int flightItemId);

    void onFocusChange(View view, ItemsFlightView itemsFlightView);
}

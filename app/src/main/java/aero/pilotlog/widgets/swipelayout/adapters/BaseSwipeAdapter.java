package aero.pilotlog.widgets.swipelayout.adapters;

import android.view.View;
import android.view.ViewGroup;


import aero.pilotlog.adapters.MCCPilotLogBaseAdapter;
import aero.pilotlog.widgets.swipelayout.SwipeLayout;
import aero.pilotlog.widgets.swipelayout.implments.SwipeItemMangerImpl;
import aero.pilotlog.widgets.swipelayout.interfaces.SwipeAdapterInterface;
import aero.pilotlog.widgets.swipelayout.interfaces.SwipeItemMangerInterface;
import aero.pilotlog.widgets.swipelayout.util.Attributes;

import java.util.List;

public abstract class BaseSwipeAdapter extends MCCPilotLogBaseAdapter implements SwipeItemMangerInterface, SwipeAdapterInterface {

    protected SwipeItemMangerImpl mItemManger = new SwipeItemMangerImpl(this);

    /**
     * return the {@link aero.pilotlog.widgets.swipelayout.SwipeLayout} resource id, int the view item.
     * @param position
     * @return
     */
    public abstract int getSwipeLayoutResourceId(int position);

    /**
     * generate a new view item.
     * Never bind SwipeListener or fill values here, every item has a chance to fill value or bind
     * listeners in fillValues.
     * to fill it in {@code fillValues} method.
     * @param position
     * @param parent
     * @return
     */
    public abstract View generateView(int position, ViewGroup parent);

    /**
     * fill values or bind listeners to the view.
     * @param position
     * @param convertView
     */
    public abstract void fillValues(int position, View convertView);

    @Override
    public void notifyDatasetChanged() {
        super.notifyDataSetChanged();
    }

//    public abstract View getView(int position, View convertView, ViewGroup parent);


    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        //View v = convertView;
        //if(v == null){
        View v = generateView(position, parent);
        //}
        mItemManger.bind(v, position);
        fillValues(position, v);
        return v;
    }

    @Override
    public void openItem(int position) {
        mItemManger.openItem(position);
    }

    @Override
    public void closeItem(int position) {
        mItemManger.closeItem(position);
    }

    @Override
    public void closeAllExcept(SwipeLayout layout) {
        mItemManger.closeAllExcept(layout);
    }

    @Override
    public void closeAllItems() {
        mItemManger.closeAllItems();
    }

    @Override
    public List<Integer> getOpenItems() {
        return mItemManger.getOpenItems();
    }

    @Override
    public List<SwipeLayout> getOpenLayouts() {
        return mItemManger.getOpenLayouts();
    }

    @Override
    public void removeShownLayouts(SwipeLayout layout) {
        mItemManger.removeShownLayouts(layout);
    }

    @Override
    public boolean isOpen(int position) {
        return mItemManger.isOpen(position);
    }

    @Override
    public Attributes.Mode getMode() {
        return mItemManger.getMode();
    }

    @Override
    public void setMode(Attributes.Mode mode) {
        mItemManger.setMode(mode);
    }


}

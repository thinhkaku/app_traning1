package aero.pilotlog.interfaces;

import android.view.View;

import aero.pilotlog.databases.entities.Expense;

/**
 * Created by phuc.dd on 8/3/2017.
 */

public interface ISwipeLayoutExpenseCallback {
    void onDeleteRecord(View pView, Expense expense, int pPosition);
}

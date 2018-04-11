package aero.pilotlog.interfaces;

import aero.pilotlog.databases.entities.Pilot;

/**
 * Created by tuan.pv on 2015/07/22.
 */
public interface IPilotCallback {
    void selectPilot(Pilot pPilot);
}

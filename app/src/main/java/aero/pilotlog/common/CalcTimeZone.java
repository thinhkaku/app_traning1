package aero.pilotlog.common;

import aero.pilotlog.databases.entities.ZTimeZone;
import aero.pilotlog.databases.entities.ZTimeZoneDST;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.utilities.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phuc.dd on 1/20/2017.
 */
public class CalcTimeZone {
    private static CalcTimeZone instance;
    private List<Integer> timeZoneCodeException;

    public static CalcTimeZone getInstance() {
        if (instance == null) {
            instance = new CalcTimeZone();
        }
        return instance;
    }

    public CalcTimeZone() {
        if (timeZoneCodeException == null) {
            timeZoneCodeException = new ArrayList<>();
            timeZoneCodeException.add(25);
        }
    }

    public double getTimeZone(DatabaseManager databaseManager, int timeZoneCode) {
        ZTimeZone timeZone = databaseManager.getTimeZoneByCode(timeZoneCode);
        boolean isTimeZoneException = false;
        for (int i = 0; i < timeZoneCodeException.size(); i++) {
            if (timeZoneCode == timeZoneCodeException.get(i)) {
                isTimeZoneException = true;
                break;
            }
        }
        if (isTimeZoneException) {
            return getTimeZoneException(timeZoneCode);
        }
        return TimeUtils.getTimeOffset(timeZone.getTimeZone());
    }

    public double getTimeZoneException(int timeZoneCode) {
        switch (timeZoneCode) {
            case 1:
                break;
            case 2:
                break;
        }
        return 0;
    }

    public String getDSTRule(DatabaseManager databaseManager, int timeZoneCode) {
        ZTimeZone timeZone = databaseManager.getTimeZoneByCode(timeZoneCode);
        ZTimeZoneDST timeZoneDST = databaseManager.getTimeZoneDSTByCode(timeZone.getDSTCode());
        return timeZoneDST.getRule();
    }

}

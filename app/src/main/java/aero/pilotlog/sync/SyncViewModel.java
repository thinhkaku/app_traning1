/*
package net.mcc3si.sync;

import android.content.Context;

import net.mcc3si.R;
import net.mcc3si.common.MCCPilotLogConst;
import net.mcc3si.common.SettingsConst;
import net.mcc3si.databases.entities.Aircraft;
import net.mcc3si.databases.entities.Airfield;
import net.mcc3si.databases.entities.AirfieldPilot;
import net.mcc3si.databases.entities.Currencies;
import net.mcc3si.databases.entities.Flight;
import net.mcc3si.databases.entities.FlightPilot;
import net.mcc3si.databases.entities.Pilot;
import net.mcc3si.databases.entities.Totals;
import net.mcc3si.databases.manager.DatabaseManager;
import net.mcc3si.utilities.DateTimeUtils;
import net.mcc3si.utilities.StorageUtils;
import net.mcc3si.utilities.ValidationUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

*/
/**
 * Created by tuan.na on 8/19/2015.
 * Sync View
 *//*

public class SyncViewModel {

    */
/**
     * Add new airfield to 40000 db. Called when A4 tag appears.
     *
     * @param pContext  context
     * @param pAirfield airfield to be added
     * @return boolean determines succession of process
     *//*

    public static boolean addNewAirfieldToBigDb(Context pContext, Airfield pAirfield) {
        return DatabaseManager.getInstance(pContext).addNewAirfieldToBigDb(pAirfield);
    }

    */
/**
     * Add new airfield to user db.
     *
     * @param pContext  context
     * @param pAirfield airfield
     * @return boolean determines succession of process
     *//*

    public static boolean addNewAirfield(Context pContext, AirfieldPilot pAirfield) {
        return DatabaseManager.getInstance(pContext).addNewAirfield(pAirfield);
    }

    */
/**
     * Add new aircraft to db
     *
     * @param pContext  context
     * @param pAirfield aircraft
     * @return boolean determines succession of process
     *//*

    public static boolean addNewAircraft(Context pContext, Aircraft pAirfield) {
        return DatabaseManager.getInstance(pContext).addNewAircraft(pAirfield);
    }

    */
/**
     * Add of update pilot from sync
     *
     * @param pContext context
     * @param pPilot   pilot
     * @return boolean determines succession of process
     *//*

    public static boolean syncPilot(Context pContext, Pilot pPilot) {
        return DatabaseManager.getInstance(pContext).insertOrUpdatePilot(pPilot);
    }

    */
/**
     * Accept flight. Only accept flight which is equals or greater than today
     *
     * @param pDate date
     * @return is accepted
     *//*

    public static boolean acceptSaveFlight(String pDate) {
//        Calendar c = Calendar.getInstance();
        if (pDate.length() == 8) {
            //TuanPV edit
            Date currentDate, flightDate;
            currentDate = DateTimeUtils.getCurrentDate();
            flightDate = DateTimeUtils.getDateFromCalendar(pDate);
            if (currentDate != null && flightDate != null && flightDate.compareTo(currentDate) >= 0) {
                return true;
            }
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.US);
//            try {
//                c.setTime(sdf.parse(pDate));
//                if (c.compareTo(Calendar.getInstance()) > 0) {
//                    return true;//future flight
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
        }

        return false;
    }

    public static boolean addFlightFromPc(Context pContext, FlightPilot pFlight) {
        //TODO: Add flight logic
        //TuanPV add new
        FlightPilot flightPilotTemp = DatabaseManager.getInstance(pContext).getFlightByFlightCode(pFlight.getFlightCode());
        if (flightPilotTemp != null) {
            Date currentDate, flightDate;
            currentDate = DateTimeUtils.getCurrentDate();
            flightDate = DateTimeUtils.getDateFromCalendar(pFlight.getFlightDate());
            if (currentDate != null && flightDate != null && flightDate.compareTo(currentDate) != 0) {
                DatabaseManager.getInstance(pContext).updateFlight(pFlight);
            }
        } else {
            DatabaseManager.getInstance(pContext).insertFlight(pFlight, false);
        }

        return true;
    }

    */
/**
     * Update totals. There is only 1 totals record in db
     *
     * @param pContext context
     * @param pTotals  totals
     * @return boolean determines succession of process
     *//*

    public static boolean updateTotalsFromPc(Context pContext, Totals pTotals) {
        return DatabaseManager.getInstance(pContext).updateTotals(pTotals);
    }

    */
/**
     * Add logbook to db.
     *
     * @param pContext        context
     * @param pLogbookFlights list of logbook
     * @param pVersion        version of xml
     * @return boolean determines succession of process
     *//*

    public static boolean addLogbookFromPc(Context pContext, List<String> pLogbookFlights, int pVersion) {
        boolean result = false;
        try {
            int i = 0;
            for (String fl : pLogbookFlights) {
                i++;
                if (i == 1430) {
                    result = false;
                }
                boolean exist;
                Flight logFlight = getFlightFromMap(fl, pVersion);
                exist = DatabaseManager.getInstance(pContext).checkExistLogbookFlight(logFlight.getFlightCode());
                if (exist) {
                    DatabaseManager.getInstance(pContext).updateLogbookFlight(logFlight);
                } else {
                    DatabaseManager.getInstance(pContext).insertNewLogbookFlight(logFlight);
                }
            }
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    */
/**
     * Get logbook flight from sync
     *
     * @param flightData string logbook flight
     * @param version    version of xml
     * @return logbook flight
     *//*

    public static Flight getFlightFromMap(String flightData, int version) {

        Flight flight = null;
        try {
            flightData = flightData.replace(MCCPilotLogConst.NEW_LINE_CHARACTER, MCCPilotLogConst.STRING_EMPTY);
            flightData = flightData.replace("|", "##");
            String[] data = flightData.split("##");

            flight = new Flight();
            flight.setFlightCode(Long.valueOf(data[0]));
            flight.setFlightDate(data[1]);
            flight.setDeviceCode(data[2]);
            flight.setReference(data[3]);
            flight.setModel(data[4]);
            flight.setSubModel(data[5]);
            flight.setDepCode(Long.valueOf(data[6]));
            flight.setArrCode(Long.valueOf(data[7]));
            flight.setFlightNumber(data[8]);
            flight.setSignBox(data[9]);
            int pf = 0;
            if (data[10].equalsIgnoreCase("-1")) {
                pf = -1;
            }
            flight.setPF("" + pf);
            flight.setTODay(data[11]);
            flight.setTONight(data[12]);
            flight.setLdgDay(data[13]);
            flight.setLdgNight(data[14]);
            flight.setHoldling(data[15]);
            flight.setAutoland(data[16]);

            flight.setTypeOfApp1(data[17]);
            flight.setTypeOfApp2(data[18]);
            flight.setTypeOfApp3(data[19]);

            flight.setTypeOfLaunch(data[20]);
            flight.setLiftSW(data[21]);
            flight.setDepTime(Long.valueOf(data[22]));
            flight.setArrTime(Long.valueOf(data[23]));
            flight.setLogUTC(data[24]);
            flight.setMinTotal(data[25]);
            flight.setMinPIC(data[26]);
            flight.setMinCoP(data[27]);
            flight.setMinDual(data[28]);
            flight.setMinPICus(data[29]);
            flight.setMinInstr(data[30]);
            flight.setMinExam(data[31]);
            flight.setMinIFR(data[32]);
            flight.setMinIMT(data[33]);
            flight.setMinSFR(data[34]);
            flight.setMinNight(data[35]);
            flight.setMinXC(data[36]);
            flight.setMinREL(data[37]);

            flight.setMinU1(data[38]);
            flight.setMinU2(data[39]);
            flight.setMinU3(data[40]);
            flight.setMinU4(data[41]);
            flight.setUserN1(data[42]);
            flight.setUserN2(data[43]);
            flight.setUserN3(data[44]);

            flight.setDeIce(Long.valueOf(data[45]));
            flight.setDelay(data[46]);
            flight.setPax(data[47]);
            flight.setFuel(data[48]);
            flight.setFuelUsed(data[49]);


            flight.setCostRent(Long.valueOf(data[50]));
            flight.setCostPilot(Long.valueOf(data[51]));
            flight.setCostPerDiem(Long.valueOf(data[52]));

            if (version >= 7) {
                flight.setTimeTO(data[53]);
                flight.setTimeLDG(data[54]);
                flight.setP1(data[55]);
                flight.setP2(data.length > 56 ? data[56] : "");
                flight.setP3(data.length > 57 ? data[57] : "");
                flight.setP4(data.length > 58 ? data[58] : "");
                flight.setRemarks(data.length > 59 ? data[59] : "");
                flight.setTypeOfInstr(data.length > 60 ? data[60] : "");
                flight.setFlightReport(data.length > 61 ? data[61] : "");
            } else {
                flight.setTimeTO("");
                flight.setTimeLDG("");

                flight.setP1(data[53]);
                flight.setP2(data.length > 54 ? data[54] : "");
                flight.setP3(data.length > 55 ? data[55] : "");
                flight.setP4(data.length > 56 ? data[56] : "");
                flight.setRemarks(data.length > 57 ? data[57] : "");
                flight.setTypeOfInstr(data.length > 58 ? data[58] : "");
                flight.setFlightReport(data.length > 59 ? data[59] : "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flight;
    }

    */
/**
     * Delete record from table
     *
     * @param pContext   context
     * @param pTableName table name
     * @param pRecordId  record id to be deleted
     * @return boolean determines succession of process
     *//*

    public static boolean deleteFromTable(Context pContext, String pTableName, final String pRecordId) {
        try {
            String code = "";
            boolean isLogbook = false;
            if (pTableName.equalsIgnoreCase("airfield")) {
                code = "AFCode";
                pTableName = "airfield";
            } else if (pTableName.equalsIgnoreCase("aircraft")) {
                code = "AircraftCode";
                pTableName = "aircraft";
            } else if (pTableName.equalsIgnoreCase("flight")) {
                code = "FlightCode";
                pTableName = "flight";
            } else if (pTableName.equalsIgnoreCase("pilot")) {
                code = "PilotCode";
                pTableName = "pilot";
                File[] pictureFiles = new File(StorageUtils.getStorageRootFolder(pContext)).listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        return (filename.contains("jpg") && filename.contains(pRecordId));
                    }
                });
                for (File f : pictureFiles) {
                    f.delete();
                }
            } else if (pTableName.equalsIgnoreCase("logbook")) {
                isLogbook = true;
                code = "FlightCode";
                pTableName = "lbflight";
            }
            String query = String.format("DELETE FROM %s WHERE %s = %s", pTableName, code, pRecordId);
            if (isLogbook) {
                DatabaseManager.getInstance(pContext).executeRawQueryLogbook(query);
            } else {
                DatabaseManager.getInstance(pContext).executeRawQueryPilot(query);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    */
/**
     * Update table
     *
     * @param pContext   context
     * @param pTableName table name
     * @param pRecordId  record to be updated
     * @param pReplaceBy update value
     * @return boolean determines succession of process
     *//*

    public static boolean updateTable(Context pContext, String pTableName, String pRecordId, String pReplaceBy) {
        File mImageFile; */
/*= new File(StorageUtils.getStorageRootFolder(mActivity), imageFileName()); *//*

        //ver5
        try {

           */
/* String query;

            if (pTableName.equalsIgnoreCase("pilot")) {
                String oldFileName = (DatabaseManager.getInstance(pContext).getSetting(SettingsConst.SYNC_ID).getData().length() == 16)
                        ? String.format("img.%s.jpg", pRecordId) : String.format("img.%s.jpg", pRecordId);
                String newFileName = (DatabaseManager.getInstance(pContext).getSetting(SettingsConst.SYNC_ID).getData().length() == 16)
                        ? String.format("img.%s.jpg", pReplaceBy) : String.format("img.%s.jpg", pReplaceBy);

                boolean bNumeric = false;
                if (ValidationUtils.isNumeric(pRecordId)) {
                    bNumeric = true;
                }

                boolean isNewPilotInDB = DatabaseManager.getInstance(pContext).getPilotByPilotCode(pReplaceBy) != null;
                if (bNumeric) {
                    DatabaseManager.getInstance(pContext).deletePilot(pRecordId);
                    try {
                        File oldFile = new File(StorageUtils.getStorageRootFolder(pContext), oldFileName);
                        File newFile = new File(StorageUtils.getStorageRootFolder(pContext), newFileName);
                        if (isNewPilotInDB) {
                            if (newFile.exists()) {
                                oldFile.delete();
                            } else {
                                StorageUtils.copyFile(oldFile, new File(StorageUtils.getStorageRootFolder(pContext), newFileName));
                                oldFile.delete();
                            }
                        } else {
                            oldFile.delete();
                        }

                    } catch (Exception ignored) {
                    }
                } else {
                    query = String.format("UPDATE [pilot] SET [PilotCode] = '%s' WHERE [PilotCode] = '%s'", pReplaceBy, pRecordId);
                    DatabaseManager.getInstance(pContext).executeRawQueryPilot(query);
                }

                query = String.format("UPDATE flight SET P1Code = '%s' WHERE P1Code = '%s'", pReplaceBy, pRecordId);
                DatabaseManager.getInstance(pContext).executeRawQueryPilot(query);
                query = String.format("UPDATE flight SET P2Code = '%s' WHERE P2Code = '%s'", pReplaceBy, pRecordId);
                DatabaseManager.getInstance(pContext).executeRawQueryPilot(query);
                query = String.format("UPDATE flight SET P3Code = '%s' WHERE P3Code = '%s'", pReplaceBy, pRecordId);
                DatabaseManager.getInstance(pContext).executeRawQueryPilot(query);
                query = String.format("UPDATE flight SET P4Code = '%s' WHERE P4Code = '%s'", pReplaceBy, pRecordId);
                DatabaseManager.getInstance(pContext).executeRawQueryPilot(query);
            }*//*


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    */
/**
     * Get all signature to be synced
     *
     * @param pContext context
     * @return list of signature name
     *//*

    public static List<String> getAllSignatures(Context pContext) {
        List<String> signatures = new ArrayList<>();
        File signFolder = new File(StorageUtils.getStorageRootFolder(pContext), pContext.getString(R.string.sign_folder));
        if (signFolder.exists() && signFolder.isDirectory()) {
            signatures.addAll(Arrays.asList(signFolder.list()));
        }

        return signatures;
    }

    public static List<FlightPilot> getAllFlightToSync(Context pContext, boolean pIncomplete) {
        return DatabaseManager.getInstance(pContext).getAllFlightToSync(pIncomplete);
    }

    public static List<Pilot> getAllPilotToSync(Context pContext, boolean isSyncPict) {
        return DatabaseManager.getInstance(pContext).getAllPilotToSync(isSyncPict);
    }

    public static List<AirfieldPilot> getAllAirfieldToSync(Context pContext) {
        return DatabaseManager.getInstance(pContext).getAllAirfieldToSync();
    }

    public static boolean deleteSyncedFlights(Context pContext, boolean pIncompleteFlight) {
        List<FlightPilot> mFlightList = getAllFlightToSync(pContext, pIncompleteFlight);
        if (mFlightList != null) {
            for (FlightPilot flight : mFlightList) {
                DatabaseManager.getInstance(pContext).deleteFlightPilot(flight);
            }
        }

        return true;
    }

    public static boolean updateDbAfterSync(Context pContext, boolean pIncompleteFlight) {
        try {
            String query = "update pilot set PC='PC', syncPict='0'";
            DatabaseManager.getInstance(pContext).executeRawQueryPilot(query);
            query = "update airfield set PC='PC'";
            DatabaseManager.getInstance(pContext).executeRawQueryPilot(query);

            List<FlightPilot> flights = getAllFlightToSync(pContext, pIncompleteFlight);
            if (flights != null && !flights.isEmpty()) {
                Totals syncedTotals = DatabaseManager.getInstance(pContext).getTotalsFromSyncedFlight(flights);
                Totals curTotals = DatabaseManager.getInstance(pContext).getTotalsFromTotals();
                Totals updatedTotals = new Totals();

                int curSumU1 = 0, curSumU2 = 0, curSumU3 = 0, curSumU4 = 0, curSumACFT = 0, curSumSIM = 0, curSumPIC = 0, curSumPICus = 0, curSumCoP = 0,
                        curSumInstr = 0, curSumDual = 0, curSumExam = 0, curSumIFR = 0, curSumNight = 0, curSumIMT = 0, curSumXC = 0, curSumSFR = 0,
                        curSumREL = 0, curSumClass1 = 0, curSumClass2 = 0, curSumClass3 = 0, curSumClass4 = 0, curSumClass5 = 0, curSumPower0 = 0, curSumPower1 = 0,
                        curSumPower2 = 0, curSumPower3 = 0, curSumPower4 = 0, curSumPower5 = 0, curSumPower6 = 0, curSumTODay = 0, curSumLdgDay = 0,
                        curSumTONight = 0, curSumLdgNight = 0, curSumHolding = 0, curSumAutoland = 0,
                        syncedSumU1 = 0, syncedSumU2 = 0, syncedSumU3 = 0, syncedSumU4 = 0, syncedSumACFT = 0, syncedSumSIM = 0, syncedSumPIC = 0, syncedSumPICus = 0, syncedSumCoP = 0,
                        syncedSumInstr = 0, syncedSumDual = 0, syncedSumExam = 0, syncedSumIFR = 0, syncedSumNight = 0, syncedSumIMT = 0, syncedSumXC = 0, syncedSumSFR = 0,
                        syncedSumREL = 0, syncedSumClass1 = 0, syncedSumClass2 = 0, syncedSumClass3 = 0, syncedSumClass4 = 0, syncedSumClass5 = 0, syncedSumPower0 = 0, syncedSumPower1 = 0,
                        syncedSumPower2 = 0, syncedSumPower3 = 0, syncedSumPower4 = 0, syncedSumPower5 = 0, syncedSumPower6 = 0, syncedSumTODay = 0, syncedSumLdgDay = 0,
                        syncedSumTONight = 0, syncedSumLdgNight = 0, syncedSumHolding = 0, syncedSumAutoland = 0;

                if (curTotals.getSumU1() != null) {
                    curSumU1 = curTotals.getSumU1();
                }
                if (curTotals.getSumU2() != null) {
                    curSumU2 = curTotals.getSumU2();
                }
                if (curTotals.getSumU3() != null) {
                    curSumU3 = curTotals.getSumU3();
                }
                if (curTotals.getSumU4() != null) {
                    curSumU4 = curTotals.getSumU4();
                }
                if (curTotals.getSumACFT() != null) {
                    curSumACFT = curTotals.getSumACFT();
                }
                if (curTotals.getSumSIM() != null) {
                    curSumSIM = curTotals.getSumSIM();
                }
                if (curTotals.getSumPIC() != null) {
                    curSumPIC = curTotals.getSumPIC();
                }
                if (curTotals.getSumPICus() != null) {
                    curSumPICus = curTotals.getSumPICus();
                }
                if (curTotals.getSumCoP() != null) {
                    curSumCoP = curTotals.getSumCoP();
                }
                if (curTotals.getSumInstr() != null) {
                    curSumInstr = curTotals.getSumInstr();
                }
                if (curTotals.getSumDual() != null) {
                    curSumDual = curTotals.getSumDual();
                }
                if (curTotals.getSumExam() != null) {
                    curSumExam = curTotals.getSumExam();
                }
                if (curTotals.getSumIFR() != null) {
                    curSumIFR = curTotals.getSumIFR();
                }
                if (curTotals.getSumNight() != null) {
                    curSumNight = curTotals.getSumNight();
                }
                if (curTotals.getSumIMT() != null) {
                    curSumIMT = curTotals.getSumIMT();
                }
                if (curTotals.getSumXC() != null) {
                    curSumXC = curTotals.getSumXC();
                }
                if (curTotals.getSumSFR() != null) {
                    curSumSFR = curTotals.getSumSFR();
                }
                if (curTotals.getSumREL() != null) {
                    curSumREL = curTotals.getSumREL();
                }
                if (curTotals.getSumClass1() != null) {
                    curSumClass1 = curTotals.getSumClass1();
                }
                if (curTotals.getSumClass2() != null) {
                    curSumClass2 = curTotals.getSumClass2();
                }
                if (curTotals.getSumClass3() != null) {
                    curSumClass3 = curTotals.getSumClass3();
                }
                if (curTotals.getSumClass4() != null) {
                    curSumClass4 = curTotals.getSumClass4();
                }
                if (curTotals.getSumClass5() != null) {
                    curSumClass5 = curTotals.getSumClass5();
                }
                if (curTotals.getSumPower0() != null) {
                    curSumPower0 = curTotals.getSumPower0();
                }
                if (curTotals.getSumPower1() != null) {
                    curSumPower1 = curTotals.getSumPower1();
                }
                if (curTotals.getSumPower2() != null) {
                    curSumPower2 = curTotals.getSumPower2();
                }
                if (curTotals.getSumPower3() != null) {
                    curSumPower3 = curTotals.getSumPower3();
                }
                if (curTotals.getSumPower4() != null) {
                    curSumPower4 = curTotals.getSumPower4();
                }
                if (curTotals.getSumPower5() != null) {
                    curSumPower5 = curTotals.getSumPower5();
                }
                if (curTotals.getSumPower6() != null) {
                    curSumPower6 = curTotals.getSumPower6();
                }
                if (curTotals.getSumTODay() != null) {
                    curSumTODay = curTotals.getSumTODay();
                }
                if (curTotals.getSumLdgDay() != null) {
                    curSumLdgDay = curTotals.getSumLdgDay();
                }
                if (curTotals.getSumTONight() != null) {
                    curSumTONight = curTotals.getSumTONight();
                }
                if (curTotals.getSumLdgNight() != null) {
                    curSumLdgNight = curTotals.getSumLdgNight();
                }
                if (curTotals.getSumHolding() != null) {
                    curSumHolding = curTotals.getSumHolding();
                }
                if (curTotals.getSumAutoland() != null) {
                    curSumAutoland = curTotals.getSumAutoland();
                }

                if (syncedTotals.getSumU1() != null) {
                    syncedSumU1 = syncedTotals.getSumU1();
                }
                if (syncedTotals.getSumU2() != null) {
                    syncedSumU2 = syncedTotals.getSumU2();
                }
                if (syncedTotals.getSumU3() != null) {
                    syncedSumU3 = syncedTotals.getSumU3();
                }
                if (syncedTotals.getSumU4() != null) {
                    syncedSumU4 = syncedTotals.getSumU4();
                }
                if (syncedTotals.getSumACFT() != null) {
                    syncedSumACFT = syncedTotals.getSumACFT();
                }
                if (syncedTotals.getSumSIM() != null) {
                    syncedSumSIM = syncedTotals.getSumSIM();
                }
                if (syncedTotals.getSumPIC() != null) {
                    syncedSumPIC = syncedTotals.getSumPIC();
                }
                if (syncedTotals.getSumPICus() != null) {
                    syncedSumPICus = syncedTotals.getSumPICus();
                }
                if (syncedTotals.getSumCoP() != null) {
                    syncedSumCoP = syncedTotals.getSumCoP();
                }
                if (syncedTotals.getSumInstr() != null) {
                    syncedSumInstr = syncedTotals.getSumInstr();
                }
                if (syncedTotals.getSumDual() != null) {
                    syncedSumDual = syncedTotals.getSumDual();
                }
                if (syncedTotals.getSumExam() != null) {
                    syncedSumExam = syncedTotals.getSumExam();
                }
                if (syncedTotals.getSumIFR() != null) {
                    syncedSumIFR = syncedTotals.getSumIFR();
                }
                if (syncedTotals.getSumNight() != null) {
                    syncedSumNight = syncedTotals.getSumNight();
                }
                if (syncedTotals.getSumIMT() != null) {
                    syncedSumIMT = syncedTotals.getSumIMT();
                }
                if (syncedTotals.getSumXC() != null) {
                    syncedSumXC = syncedTotals.getSumXC();
                }
                if (syncedTotals.getSumSFR() != null) {
                    syncedSumSFR = syncedTotals.getSumSFR();
                }
                if (syncedTotals.getSumREL() != null) {
                    syncedSumREL = syncedTotals.getSumREL();
                }
                if (syncedTotals.getSumClass1() != null) {
                    syncedSumClass1 = syncedTotals.getSumClass1();
                }
                if (syncedTotals.getSumClass2() != null) {
                    syncedSumClass2 = syncedTotals.getSumClass2();
                }
                if (syncedTotals.getSumClass3() != null) {
                    syncedSumClass3 = syncedTotals.getSumClass3();
                }
                if (syncedTotals.getSumClass4() != null) {
                    syncedSumClass4 = syncedTotals.getSumClass4();
                }
                if (syncedTotals.getSumClass5() != null) {
                    syncedSumClass5 = syncedTotals.getSumClass5();
                }
                if (syncedTotals.getSumPower0() != null) {
                    syncedSumPower0 = syncedTotals.getSumPower0();
                }
                if (syncedTotals.getSumPower1() != null) {
                    syncedSumPower1 = syncedTotals.getSumPower1();
                }
                if (syncedTotals.getSumPower2() != null) {
                    syncedSumPower2 = syncedTotals.getSumPower2();
                }
                if (syncedTotals.getSumPower3() != null) {
                    syncedSumPower3 = syncedTotals.getSumPower3();
                }
                if (syncedTotals.getSumPower4() != null) {
                    syncedSumPower4 = syncedTotals.getSumPower4();
                }
                if (syncedTotals.getSumPower5() != null) {
                    syncedSumPower5 = syncedTotals.getSumPower5();
                }
                if (syncedTotals.getSumPower6() != null) {
                    syncedSumPower6 = syncedTotals.getSumPower6();
                }
                if (syncedTotals.getSumTODay() != null) {
                    syncedSumTODay = syncedTotals.getSumTODay();
                }
                if (syncedTotals.getSumLdgDay() != null) {
                    syncedSumLdgDay = syncedTotals.getSumLdgDay();
                }
                if (syncedTotals.getSumTONight() != null) {
                    syncedSumTONight = syncedTotals.getSumTONight();
                }
                if (syncedTotals.getSumLdgNight() != null) {
                    syncedSumLdgNight = syncedTotals.getSumLdgNight();
                }
                if (syncedTotals.getSumHolding() != null) {
                    syncedSumHolding = syncedTotals.getSumHolding();
                }
                if (syncedTotals.getSumAutoland() != null) {
                    syncedSumAutoland = syncedTotals.getSumAutoland();
                }

                updatedTotals.setSumU1(curSumU1 + syncedSumU1);
                updatedTotals.setSumU2(curSumU2 + syncedSumU2);
                updatedTotals.setSumU3(curSumU3 + syncedSumU3);
                updatedTotals.setSumU4(curSumU4 + syncedSumU4);
                updatedTotals.setSumACFT(curSumACFT + syncedSumACFT);
                updatedTotals.setSumSIM(curSumSIM + syncedSumSIM);
                updatedTotals.setSumPIC(curSumPIC + syncedSumPIC);
                updatedTotals.setSumPICus(curSumPICus + syncedSumPICus);
                updatedTotals.setSumCoP(curSumCoP + syncedSumCoP);
                updatedTotals.setSumInstr(curSumInstr + syncedSumInstr);
                updatedTotals.setSumDual(curSumDual + syncedSumDual);
                updatedTotals.setSumExam(curSumExam + syncedSumExam);
                updatedTotals.setSumIFR(curSumIFR + syncedSumIFR);
                updatedTotals.setSumNight(curSumNight + syncedSumNight);
                updatedTotals.setSumIMT(curSumIMT + syncedSumIMT);
                updatedTotals.setSumXC(curSumXC + syncedSumXC);
                updatedTotals.setSumSFR(curSumSFR + syncedSumSFR);
                updatedTotals.setSumREL(curSumREL + syncedSumREL);
                updatedTotals.setSumClass1(curSumClass1 + syncedSumClass1);
                updatedTotals.setSumClass2(curSumClass2 + syncedSumClass2);
                updatedTotals.setSumClass3(curSumClass3 + syncedSumClass3);
                updatedTotals.setSumClass4(curSumClass4 + syncedSumClass4);
                updatedTotals.setSumClass5(curSumClass5 + syncedSumClass5);
                updatedTotals.setSumPower0(curSumPower0 + syncedSumPower0);
                updatedTotals.setSumPower1(curSumPower1 + syncedSumPower1);
                updatedTotals.setSumPower2(curSumPower2 + syncedSumPower2);
                updatedTotals.setSumPower3(curSumPower3 + syncedSumPower3);
                updatedTotals.setSumPower4(curSumPower4 + syncedSumPower4);
                updatedTotals.setSumPower5(curSumPower5 + syncedSumPower5);
                updatedTotals.setSumPower6(curSumPower6 + syncedSumPower6);
                updatedTotals.setSumTODay(curSumTODay + syncedSumTODay);
                updatedTotals.setSumLdgDay(curSumLdgDay + syncedSumLdgDay);
                updatedTotals.setSumTONight(curSumTONight + syncedSumTONight);
                updatedTotals.setSumLdgNight(curSumLdgNight + syncedSumLdgNight);
                updatedTotals.setSumHolding(curSumHolding + syncedSumHolding);
                updatedTotals.setSumAutoland(curSumAutoland + syncedSumAutoland);

                DatabaseManager.getInstance(pContext).updateTotals(updatedTotals);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean addCurrencies(Context pContext, Currencies pCurrencies) {
        return DatabaseManager.getInstance(pContext).insertNewCurrencies(pCurrencies);
    }

    public static HashMap<String, Integer> changedPilotCode = new HashMap<>();

    public static int updateLatestPilotCode(Context pContext, int latestPilotCode) {
        List<Pilot> listPilot = getAllPilotToSync(pContext, false);
        List<FlightPilot> listFlightP1;
        List<FlightPilot> listFlightP2;
        List<FlightPilot> listFlightP3;
        List<FlightPilot> listFlightP4;
        for (Pilot p : listPilot) {
            if (p.getPilotCode().contains("p")) {
                latestPilotCode++;
                String queryUpdatePilot = String.format("UPDATE pilot SET PilotCode = '%s' WHERE PilotCode = '%s'", latestPilotCode, p.getPilotCode());
                changedPilotCode.put(p.getPilotCode(), latestPilotCode);
                DatabaseManager.getInstance(pContext).executeRawQueryPilot(queryUpdatePilot);
                listFlightP1 = DatabaseManager.getInstance(pContext).getAllFlightWithPilot1(p.getPilotCode());
                listFlightP2 = DatabaseManager.getInstance(pContext).getAllFlightWithPilot2(p.getPilotCode());
                listFlightP3 = DatabaseManager.getInstance(pContext).getAllFlightWithPilot3(p.getPilotCode());
                listFlightP4 = DatabaseManager.getInstance(pContext).getAllFlightWithPilot4(p.getPilotCode());
                for (FlightPilot f : listFlightP1) {
//                    String queryUpdateFlight = String.format("UPDATE flight SET P1Code = '%s' WHERE FlightCode = '%s'", latestPilotCode, p.getPilotCode());
                    String queryUpdateFlight = String.format("UPDATE flight SET P1Code = '%s' WHERE FlightCode = '%s'", latestPilotCode, f.getFlightCode());
                    DatabaseManager.getInstance(pContext).executeRawQueryPilot(queryUpdateFlight);
                }
                for (FlightPilot f : listFlightP2) {
//                    String queryUpdateFlight = String.format("UPDATE flight SET P1Code = '%s' WHERE FlightCode = '%s'", latestPilotCode, p.getPilotCode());
                    String queryUpdateFlight = String.format("UPDATE flight SET P2Code = '%s' WHERE FlightCode = '%s'", latestPilotCode, f.getFlightCode());
                    DatabaseManager.getInstance(pContext).executeRawQueryPilot(queryUpdateFlight);
                }
                for (FlightPilot f : listFlightP3) {
//                    String queryUpdateFlight = String.format("UPDATE flight SET P1Code = '%s' WHERE FlightCode = '%s'", latestPilotCode, p.getPilotCode());
                    String queryUpdateFlight = String.format("UPDATE flight SET P3Code = '%s' WHERE FlightCode = '%s'", latestPilotCode, f.getFlightCode());
                    DatabaseManager.getInstance(pContext).executeRawQueryPilot(queryUpdateFlight);
                }
                for (FlightPilot f : listFlightP4) {
//                    String queryUpdateFlight = String.format("UPDATE flight SET P1Code = '%s' WHERE FlightCode = '%s'", latestPilotCode, p.getPilotCode());
                    String queryUpdateFlight = String.format("UPDATE flight SET P4Code = '%s' WHERE FlightCode = '%s'", latestPilotCode, f.getFlightCode());
                    DatabaseManager.getInstance(pContext).executeRawQueryPilot(queryUpdateFlight);
                }
            }
        }
        return latestPilotCode;
    }

    public static int updateLatestFlightCode(Context pContext, int latestFlightCode, boolean pIncompleteFlight) {
        List<FlightPilot> listFlight = getAllFlightToSync(pContext, pIncompleteFlight);
        for (FlightPilot f : listFlight) {
            if (f.getFlightCode().contains("f")) {
                latestFlightCode++;
                String queryUpdateFlight = String.format("UPDATE flight SET FlightCode = '%s' WHERE FlightCode = '%s'", latestFlightCode, f.getFlightCode());
                DatabaseManager.getInstance(pContext).executeRawQueryPilot(queryUpdateFlight);
            }
        }
        return latestFlightCode;
    }

    public static List<String> getPictureList(Context pContext) {
        List<String> pictureList = new ArrayList<>();
        List<Pilot> pilotsWithPicturesToSync = getAllPilotToSync(pContext, true); //pictures
        List<String> signaturePicturesToSync = getAllSignatures(pContext); //pictures
        try {
            try {
                File rootDir = new File(StorageUtils.getStorageRootFolder(pContext));
                File pictureDir = new File(StorageUtils.getStorageRootFolder(pContext), MCCPilotLogConst.PICTURE_FOLDER);
                File syncedSignDir = new File(StorageUtils.getStorageRootFolder(pContext), MCCPilotLogConst.SYNCED_SIGN_FOLDER);
                if (!pictureDir.exists()) {
                    pictureDir.mkdir();
                }
                if (!syncedSignDir.exists()) {
                    syncedSignDir.mkdir();
                }

                for (Pilot p : pilotsWithPicturesToSync) {
                    String localImageName = String.format(MCCPilotLogConst.PILOT_IMAGE_FILE_NAME_V4, p.getPilotCode());
                    File pilotPicture = new File(rootDir, localImageName);
                    if (pilotPicture.exists()) {
                        File tempPictureFile = new File(pictureDir, localImageName);
                        StorageUtils.copyFile(pilotPicture, tempPictureFile);
                        //String imgName = String.format("pict.%s.%s.zip", DatabaseManager.getInstance(pContext).getSetting(SettingsConst.SYNC_ID).getData().split("-")[0], p.getPilotCode());
                        String imgName = String.format("img.%s.zip", p.getPilotCode());
                        StorageUtils.zipFileAtPath(tempPictureFile.getAbsolutePath(), new File(pictureDir, imgName).getAbsolutePath());
                        pictureList.add(imgName);
                        tempPictureFile.delete();
                    }
                }

                for (String sign : signaturePicturesToSync) {
                    File signFile = new File(rootDir + File.separator + MCCPilotLogConst.SIGN_FOLDER, sign);
                    if (signFile.exists()) {
                        String tempSign = sign.replace(".jpg", MCCPilotLogConst.STRING_EMPTY).replace("img.", MCCPilotLogConst.STRING_EMPTY);
                        //String imgName = String.format("img.%s.%s.zip", DatabaseManager.getInstance(pContext).getSetting(SettingsConst.SYNC_ID).getData().split("-")[0], tempSign);
                        String imgName = String.format("img.%s.zip", tempSign);
                        StorageUtils.zipFileAtPath(signFile.getAbsolutePath(), new File(rootDir + File.separator + MCCPilotLogConst.SIGN_FOLDER, imgName).getAbsolutePath());
                        pictureList.add(imgName);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pictureList;
    }
}
*/

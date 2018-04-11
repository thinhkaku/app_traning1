package aero.pilotlog.databases.manager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;
import android.util.Log;

import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.common.MccEnum;
import aero.pilotlog.databases.dao.AircraftDao;
import aero.pilotlog.databases.dao.AirfieldDao;
import aero.pilotlog.databases.dao.AllowanceRulesDao;
import aero.pilotlog.databases.dao.BackupDBDao;
import aero.pilotlog.databases.dao.DaoMaster;
import aero.pilotlog.databases.dao.DaoSession;
import aero.pilotlog.databases.dao.DutyDao;
import aero.pilotlog.databases.dao.ExpenseDao;
import aero.pilotlog.databases.dao.FlightDao;
import aero.pilotlog.databases.dao.ImagePicDao;
import aero.pilotlog.databases.dao.LimitRulesDao;
import aero.pilotlog.databases.dao.MyQueryBuildDao;
import aero.pilotlog.databases.dao.MyQueryDao;
import aero.pilotlog.databases.dao.PilotDao;
import aero.pilotlog.databases.dao.RecordDeleteDao;
import aero.pilotlog.databases.dao.SettingConfigDao;
import aero.pilotlog.databases.dao.SettingLocalDao;
import aero.pilotlog.databases.dao.ValidationRulesDao;
import aero.pilotlog.databases.dao.WeatherAFDao;
import aero.pilotlog.databases.dao.WeatherDao;
import aero.pilotlog.databases.dao.WeatherLocalDao;
import aero.pilotlog.databases.dao.ZApproachCatDao;
import aero.pilotlog.databases.dao.ZApproachDao;
import aero.pilotlog.databases.dao.ZCountryDao;
import aero.pilotlog.databases.dao.ZCurrencyDao;
import aero.pilotlog.databases.dao.ZDelayDao;
import aero.pilotlog.databases.dao.ZDelayGroupDao;
import aero.pilotlog.databases.dao.ZExpenseDao;
import aero.pilotlog.databases.dao.ZExpenseGroupDao;
import aero.pilotlog.databases.dao.ZFNPTDao;
import aero.pilotlog.databases.dao.ZLaunchDao;
import aero.pilotlog.databases.dao.ZLimitDao;
import aero.pilotlog.databases.dao.ZOperationDao;
import aero.pilotlog.databases.dao.ZTimeZoneDSTDao;
import aero.pilotlog.databases.dao.ZTimeZoneDao;
import aero.pilotlog.databases.entities.Aircraft;
import aero.pilotlog.databases.entities.Airfield;
import aero.pilotlog.databases.entities.AllowanceRules;
import aero.pilotlog.databases.entities.BackupDB;
import aero.pilotlog.databases.entities.Duty;
import aero.pilotlog.databases.entities.Expense;
import aero.pilotlog.databases.entities.Flight;
import aero.pilotlog.databases.entities.ImagePic;
import aero.pilotlog.databases.entities.LimitRules;
import aero.pilotlog.databases.entities.MyQuery;
import aero.pilotlog.databases.entities.MyQueryBuild;
import aero.pilotlog.databases.entities.Pilot;
import aero.pilotlog.databases.entities.Qualification;
import aero.pilotlog.databases.entities.RecordDelete;
import aero.pilotlog.databases.entities.SettingConfig;
import aero.pilotlog.databases.entities.SettingLocal;
import aero.pilotlog.databases.entities.Totals;
import aero.pilotlog.databases.entities.ValidationRules;
import aero.pilotlog.databases.entities.Weather;
import aero.pilotlog.databases.entities.WeatherAF;
import aero.pilotlog.databases.entities.WeatherLocal;
import aero.pilotlog.databases.entities.ZApproach;
import aero.pilotlog.databases.entities.ZApproachCat;
import aero.pilotlog.databases.entities.ZCountry;
import aero.pilotlog.databases.entities.ZCurrency;
import aero.pilotlog.databases.entities.ZDelay;
import aero.pilotlog.databases.entities.ZDelayGroup;
import aero.pilotlog.databases.entities.ZExpense;
import aero.pilotlog.databases.entities.ZExpenseGroup;
import aero.pilotlog.databases.entities.ZFNPT;
import aero.pilotlog.databases.entities.ZLaunch;
import aero.pilotlog.databases.entities.ZLimit;
import aero.pilotlog.databases.entities.ZOperation;
import aero.pilotlog.databases.entities.ZQualification;
import aero.pilotlog.databases.entities.ZTimeZone;
import aero.pilotlog.databases.entities.ZTimeZoneDST;
import aero.pilotlog.models.DutyModels;
import aero.pilotlog.models.FlightModel;
import aero.pilotlog.utilities.FlightUtils;
import aero.pilotlog.utilities.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;

/**
 * Created by phuc.dd on 12/2/2016.
 */
public class DatabaseManager {
    private static DatabaseManager mInstance;

    /**
     * Name of three databases
     */
    public static final String DATABASE_VER_5 = "db35_PILOTLOG.sqlite";

    /**
     * The Android Activity reference for access to DatabaseManager.
     */
    private final Context mContext;
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase mDatabase;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    /**
     * Constructs a new DatabaseManager with the specified arguments.
     *
     * @param pContext The Android {@link Context}.
     */
    public DatabaseManager(final Context pContext) {
        this.mContext = pContext;
        mHelper = new DaoMaster.DevOpenHelper(this.mContext, DATABASE_VER_5, null);
    }

    /**
     * @param pContext The Android {@link Context}.
     * @return this.mInstance
     */
    public static DatabaseManager getInstance(Context pContext) {

        if (mInstance == null) {
            mInstance = new DatabaseManager(pContext);
        }
        return mInstance;
    }

    /**
     * Query for readable DB
     */
    private void openReadableDb() throws SQLiteException {
        if (mHelper == null) {
            mHelper = new DaoMaster.DevOpenHelper(this.mContext, DATABASE_VER_5, null);
        }
        mDatabase = mHelper.getReadableDatabase();
        mDaoMaster = new DaoMaster(mDatabase);
        mDaoSession = mDaoMaster.newSession();
    }

    /**
     * Query for writable DB
     */
    private void openWritableDb() throws SQLiteException {
        if (mHelper == null) {
            mHelper = new DaoMaster.DevOpenHelper(this.mContext, DATABASE_VER_5, null);
        }
        mDatabase = mHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(mDatabase);
        mDaoSession = mDaoMaster.newSession();
    }

    public void closeDbConnections() {
        if (mDaoSession != null) {
            mDaoSession.clear();
            mDaoSession = null;
        }

        if (mDatabase != null && mDatabase.isOpen()) {
            mDatabase.close();
        }

        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }

        if (mInstance != null) {
            mInstance = null;
        }
    }

 /*   public List<FlightPilot> getAllFlightPilots() {
        List<FlightPilot> flightPilots = null;
        try {
            openReadableDb(DATA_TYPE_PILOT);
            flightPilots = mDaoSessionPilot.getFlightDao().loadAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return flightPilots;
    }*/

    public List<Aircraft> getAircraftByRefSearch(String pRefSearch) {
        List<Aircraft> mAircraft = new ArrayList<>();
        try {
            openReadableDb();
            mAircraft = mDaoSession.getAircraftDao().queryBuilder().where(AircraftDao.Properties.RefSearch.like("%" + pRefSearch.toUpperCase() + "%")).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return mAircraft;
    }

    public Aircraft getAircraftByReference(String reference) {
        Aircraft aircraft = null;
        try {
            openReadableDb();
            aircraft = mDaoSession.getAircraftDao().queryBuilder().where(AircraftDao.Properties.Reference.eq(reference)).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return aircraft;
    }


  /*  public void insertAircraft(Aircraft aircraft) {
        try {
            openWritableDb();
            AircraftDao aircraftDao = mDaoSession.getAircraftDao();
            aircraftDao.insert(aircraft);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
    }*/

    public List<Aircraft> getAllAircraft() {
        List<Aircraft> aircraft = null;
        try {
            openReadableDb();
            if (!mDaoMaster.getDatabase().isOpen()) {
                openReadableDb();
            }
            AircraftDao aircraftDao = mDaoSession.getAircraftDao();
            aircraft = aircraftDao.queryBuilder().where(AircraftDao.Properties.Active.eq(true)).list();
        } catch (Exception e) {
            e.printStackTrace();
        }

        closeDbConnections();

        return aircraft;
    }


    public boolean isAircraftExist(String reference) {
        long count = 0;
        openReadableDb();
        try {
            AircraftDao aircraftDao = mDaoSession.getAircraftDao();
            QueryBuilder queryBuilder = aircraftDao.queryBuilder();
            queryBuilder = queryBuilder.where(AircraftDao.Properties.RefSearch.eq(reference));
            count = queryBuilder.count();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return count > 0;
    }

    public Aircraft getAircraftByCode(byte[] aircraftCode) {
        if (aircraftCode == null) return null;
        Aircraft response = null;
        try {
            openReadableDb();
            String conditionString = AircraftDao.Properties.AircraftCode.columnName + '=' + Utils.escapeBlobArgument(aircraftCode);
            WhereCondition condition = new WhereCondition.StringCondition(conditionString);
            response = mDaoSession.getAircraftDao().queryBuilder().where(condition).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return response;
    }

    public boolean isAircraftUsedInFlight(byte[] aircraftCode) {
        long count = 0;

        openReadableDb();
        try {
            FlightDao flightDao = mDaoSession.getFlightDao();
            String conditionString = FlightDao.Properties.AircraftCode.columnName + '=' + Utils.escapeBlobArgument(aircraftCode);
            WhereCondition condition = new WhereCondition.StringCondition(conditionString);
            count = flightDao.queryBuilder().where(condition).count();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return count > 0;
    }

    public boolean deleteAllAircraft() {
        boolean result = true;
        try {
            openWritableDb();
            mDatabase.beginTransaction();
            AircraftDao aircraftDao = mDaoSession.getAircraftDao();
            List<Aircraft> aircraftList = aircraftDao.loadAll();
            aircraftDao.queryBuilder()
                    .buildDelete().executeDeleteWithoutDetachingEntities();
            List<RecordDelete> recordDeleteList = new ArrayList<>();
            for (int i = 0; i < aircraftList.size(); i++) {
                RecordDelete recordDelete = new RecordDelete(AircraftDao.TABLENAME, aircraftList.get(i).getAircraftCode());
                recordDeleteList.add(recordDelete);
            }
            insertDeleteRecords(recordDeleteList);
            mDatabase.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            mDatabase.endTransaction();
            closeDbConnections();
        }
        return result;
    }


    public boolean deleteAircraft(byte[] aircraftCode) {
        boolean result = true;
        try {
            openWritableDb();
            mDatabase.beginTransaction();
            String conditionString = AircraftDao.Properties.AircraftCode.columnName + '=' + Utils.escapeBlobArgument(aircraftCode);
            WhereCondition condition = new WhereCondition.StringCondition(conditionString);
            mDaoSession.getAircraftDao().queryBuilder().where(condition).buildDelete().executeDeleteWithoutDetachingEntities();
            RecordDelete recordDelete = new RecordDelete(AircraftDao.TABLENAME, aircraftCode);
            insertDeleteRecord(recordDelete);
            mDatabase.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            mDatabase.endTransaction();
            closeDbConnections();
        }
        return result;
    }

    public List<Aircraft> searchAircraft(String pText) {
        List<Aircraft> aircraft = null;
        try {
            openReadableDb();
            AircraftDao aircraftDao = mDaoSession.getAircraftDao();
            if (!TextUtils.isEmpty(pText)) {

                aircraft = aircraftDao.queryBuilder().whereOr(
                        AircraftDao.Properties.Model.like("%" + pText + "%")
                        , AircraftDao.Properties.Reference.like("%" + pText + "%")
                        , AircraftDao.Properties.Company.like("%" + pText + "%")
                        , AircraftDao.Properties.SubModel.like("%" + pText + "%")).list();
            } else {
                aircraft = aircraftDao.loadAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return aircraft;
    }

    public List<Flight> getLastFlightByAircraft(byte[] aircraftCode) {
        List<Flight> response = null;
        try {
            openReadableDb();
            String conditionString = FlightDao.Properties.AircraftCode.columnName + '=' + Utils.escapeBlobArgument(aircraftCode);
            WhereCondition condition = new WhereCondition.StringCondition(conditionString);
            response = mDaoSession.getFlightDao().queryBuilder().where(condition).orderAsc(FlightDao.Properties.DateUTC).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return response;
    }

    public Flight getPreviousFlight() {
        Flight response = null;
        try {
            openReadableDb();
            response = mDaoSession.getFlightDao().queryBuilder().limit(1).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return response;
    }

    public List<Pilot> getAllPilot() {
        List<Pilot> response = null;
        try {
            openReadableDb();
            if (!mDaoMaster.getDatabase().isOpen()) {
                openReadableDb();
            }
            PilotDao pilotDao = mDaoSession.getPilotDao();
            response = pilotDao.queryBuilder().where(PilotDao.Properties.Active.eq(true)).list();
            //response = pilotDao.loadAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return response;
    }


    public List<Pilot> getAllPilots(String search) {
        List<Pilot> pilots = null;
        try {
            openReadableDb();
            PilotDao pilotDao = mDaoSession.getPilotDao();
            QueryBuilder<Pilot> queryBuilder = pilotDao.queryBuilder().where(PilotDao.Properties.PilotCode.gt(0));
            if (!TextUtils.isEmpty(search)) {
                pilots = queryBuilder.whereOr(PilotDao.Properties.PilotRef.like("%" + search + "%")
                        , PilotDao.Properties.PilotName.like("%" + search + "%")
                        , PilotDao.Properties.Certificate.like("%" + search + "%")
                        , PilotDao.Properties.RosterAlias.like("%" + search + "%")
                        , PilotDao.Properties.Company.like("%" + search + "%")).list();
            } else {
                pilots = queryBuilder.list();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return pilots;
    }

    public Pilot getPilotByCode(byte[] code) {
        if (code == null) return null;
        Pilot response = null;
        try {
            openReadableDb();
            String conditionString = PilotDao.Properties.PilotCode.columnName + '=' + Utils.escapeBlobArgument(code);
            WhereCondition condition = new WhereCondition.StringCondition(conditionString);
            response = mDaoSession.getPilotDao().queryBuilder().where(condition).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return response;
    }

    public Pilot getPilotByName(String name) {
        Pilot response = null;
        try {
            openReadableDb();
            response = mDaoSession.getPilotDao().queryBuilder().where(PilotDao.Properties.PilotName.eq(name), PilotDao.Properties.Active.eq(true)).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return response;
    }

    public Pilot getPilotByNameOrRef(String value) {
        Pilot response = null;
        try {
            openReadableDb();
            response = mDaoSession.getPilotDao().queryBuilder().whereOr(PilotDao.Properties.PilotName.like("%" + value + "%"),
                    PilotDao.Properties.PilotRef.like("%" + value + "%")).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return response;
    }

 /*   public boolean updatePilotCode(Pilot oldPilot){
        Pilot response = null;
        try {
            openReadableDb();
            *//*String conditionString = PilotDao.Properties.PilotCode.columnName + '=' + Utils.escapeBlobArgument(name);
            WhereCondition condition = new WhereCondition.StringCondition(conditionString);*//*
            //response = mDaoSession.getPilotDao().queryBuilder().where(PilotDao.Properties.PilotName.eq(name)).unique();

            mDaoSession.getPilotDao().update(oldPilot);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        //return response;
        return true;
    }*/

    public boolean isPilotExist(String reference) {
        long count = 0;
        openReadableDb();
        try {
            PilotDao pilotDao = mDaoSession.getPilotDao();
            QueryBuilder queryBuilder = pilotDao.queryBuilder();
            queryBuilder = queryBuilder.where(PilotDao.Properties.PilotRef.eq(reference));
            count = queryBuilder.count();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return count > 0;
    }

    public boolean insertPilot(Pilot pilot) {
        try {
            openWritableDb();
            mDaoSession.getPilotDao().insertOrReplace(pilot);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeDbConnections();
        }
    }

    public boolean deletePilot(byte[] code) {
        boolean result = true;
        try {
            openWritableDb();
            mDatabase.beginTransaction();
            String conditionString = PilotDao.Properties.PilotCode.columnName + '=' + Utils.escapeBlobArgument(code);
            WhereCondition condition = new WhereCondition.StringCondition(conditionString);
            mDaoSession.getPilotDao().queryBuilder().where(condition).buildDelete().executeDeleteWithoutDetachingEntities();
            RecordDelete recordDelete = new RecordDelete(PilotDao.TABLENAME, code);
            insertDeleteRecord(recordDelete);
            mDatabase.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            mDatabase.endTransaction();
            closeDbConnections();
        }
        return result;
    }

   /* public boolean activeOrDeActivePilot(byte[] code, boolean isActive) {
        boolean result = true;
        try {
            openWritableDb();
            String conditionString = PilotDao.Properties.PilotCode.columnName + '=' + Utils.escapeBlobArgument(code);
            WhereCondition condition = new WhereCondition.StringCondition(conditionString);
            mDaoSession.getPilotDao().update();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            closeDbConnections();
        }
        return result;
    }*/

    public boolean deleteAllPilots() {
        boolean result = true;
        try {
            openWritableDb();
            mDatabase.beginTransaction();
            PilotDao pilotDao = mDaoSession.getPilotDao();
            List<Pilot> pilotList = pilotDao.queryBuilder()
                    .where(AirfieldDao.Properties.ShowList.eq(true)).list();
            pilotDao.queryBuilder()
                    .where(PilotDao.Properties.PilotRef.isNotNull())
                    .buildDelete().executeDeleteWithoutDetachingEntities();
            List<RecordDelete> recordDeleteList = new ArrayList<>();
            for (int i = 0; i < pilotList.size(); i++) {
                RecordDelete recordDelete = new RecordDelete(PilotDao.TABLENAME, pilotList.get(i).getPilotCode());
                recordDeleteList.add(recordDelete);
            }
            insertDeleteRecords(recordDeleteList);
            mDatabase.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            mDatabase.endTransaction();
            closeDbConnections();
        }
        return result;
    }

    public boolean isPilotUsedInFlight(byte[] code) {
        long count = 0;
        try {
            openWritableDb();
            FlightDao flightDao = mDaoSession.getFlightDao();
            QueryBuilder queryBuilder = flightDao.queryBuilder();
            String conditionString1 = FlightDao.Properties.P1Code.columnName + '=' + Utils.escapeBlobArgument(code);
            WhereCondition condition2 = new WhereCondition.StringCondition(conditionString1);
            String conditionString2 = FlightDao.Properties.P2Code.columnName + '=' + Utils.escapeBlobArgument(code);
            WhereCondition condition3 = new WhereCondition.StringCondition(conditionString2);
            String conditionString3 = FlightDao.Properties.P3Code.columnName + '=' + Utils.escapeBlobArgument(code);
            WhereCondition condition4 = new WhereCondition.StringCondition(conditionString3);
            String conditionString4 = FlightDao.Properties.P4Code.columnName + '=' + Utils.escapeBlobArgument(code);
            WhereCondition condition1 = new WhereCondition.StringCondition(conditionString4);
            queryBuilder = queryBuilder.whereOr(condition1, condition2, condition3, condition4);
            count = queryBuilder.count();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return count > 0;
    }

    public List<Airfield> getAllAirfieldFavorite() {
        List<Airfield> response = null;
        try {
            openReadableDb();
            if (!mDaoMaster.getDatabase().isOpen()) {
                openReadableDb();
            }
            AirfieldDao airfieldDao = mDaoSession.getAirfieldDao();
            response = airfieldDao.queryBuilder().where(AirfieldDao.Properties.ShowList.eq(true)).list();
        } catch (Exception e) {
            e.printStackTrace();
        }

        closeDbConnections();

        return response;
    }


    public ZCountry getCountryByCode(int code) {
        ZCountry response = null;
        try {
            openReadableDb();
            if (!mDaoMaster.getDatabase().isOpen()) {
                openReadableDb();
            }
            ZCountryDao countryDao = mDaoSession.getZCountryDao();
            response = countryDao.queryBuilder().where(ZCountryDao.Properties.CountryCode.eq(code)).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }

        closeDbConnections();

        return response;
    }

    public ZCountry getCountryByIso3166(String code) {
        ZCountry response = null;
        try {
            openReadableDb();
            if (!mDaoMaster.getDatabase().isOpen()) {
                openReadableDb();
            }
            ZCountryDao countryDao = mDaoSession.getZCountryDao();
            response = countryDao.queryBuilder().where(ZCountryDao.Properties.ISO_3166.eq(code)).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }

        closeDbConnections();

        return response;
    }

    public ZCountry getCountryByCode(String code) {
        ZCountry response = null;
        try {
            openReadableDb();
            if (!mDaoMaster.getDatabase().isOpen()) {
                openReadableDb();
            }
            ZCountryDao countryDao = mDaoSession.getZCountryDao();
            response = countryDao.queryBuilder().where(ZCountryDao.Properties.CountryCode.eq(code)).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }

        closeDbConnections();

        return response;
    }

    public ZCountry getCountryByIsoCode(String code) {
        ZCountry response = null;
        try {
            openReadableDb();
            if (!mDaoMaster.getDatabase().isOpen()) {
                openReadableDb();
            }
            ZCountryDao countryDao = mDaoSession.getZCountryDao();
            response = countryDao.queryBuilder().where(ZCountryDao.Properties.ISO_3166.eq(code)).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }

        closeDbConnections();

        return response;
    }

    public Airfield getAirfieldByCode(byte[] code) {
        if (code == null) return null;
        Airfield response = null;
        try {
            openReadableDb();
            String conditionString = AirfieldDao.Properties.AFCode.columnName + '=' + Utils.escapeBlobArgument(code);
            WhereCondition condition = new WhereCondition.StringCondition(conditionString);
            response = mDaoSession.getAirfieldDao().queryBuilder().where(condition).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return response;
    }


    public Airfield getAirfieldByICAOIATA(String pIcaoIata) {
        Airfield airfield = null;
        try {
            openReadableDb();
            airfield = mDaoSession.getAirfieldDao().queryBuilder()
                    .whereOr(AirfieldDao.Properties.AFIATA.eq(pIcaoIata), AirfieldDao.Properties.AFICAO.eq(pIcaoIata)).unique();

        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return airfield;
    }

    public Airfield getAirfieldByICAOIATAWhereNotEdit(String pICaoIAta, byte[] code) {
        Airfield airfield = null;
        if (code != null) {
            String conditionString = AirfieldDao.Properties.AFCode.columnName + "!=" + Utils.escapeBlobArgument(code);
            WhereCondition condition = new WhereCondition.StringCondition(conditionString);
            try {
                openReadableDb();
                airfield = mDaoSession.getAirfieldDao().queryBuilder()
                        .where(condition)
                        .whereOr(AirfieldDao.Properties.AFIATA.eq(pICaoIAta), AirfieldDao.Properties.AFICAO.eq(pICaoIAta)).unique();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                openReadableDb();
                airfield = mDaoSession.getAirfieldDao().queryBuilder()
                        .whereOr(AirfieldDao.Properties.AFIATA.eq(pICaoIAta), AirfieldDao.Properties.AFICAO.eq(pICaoIAta)).unique();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        closeDbConnections();

        return airfield;
    }

    public List<Airfield> getAirfieldsByICAOIATAOrAFName(String keySearch, boolean favorite) {
        List<Airfield> airfields = null;
        try {
            openReadableDb();
            if (keySearch.length() == 3) {
                airfields = mDaoSession.getAirfieldDao().queryBuilder()
                        .where(AirfieldDao.Properties.AFIATA.like("%" + keySearch + "%")).where(AirfieldDao.Properties.ShowList.eq(favorite)).limit(200).list();
            } else if (keySearch.length() == 4) {
                airfields = mDaoSession.getAirfieldDao().queryBuilder()
                        .where(AirfieldDao.Properties.AFICAO.like("%" + keySearch + "%")).where(AirfieldDao.Properties.ShowList.eq(favorite)).limit(200).list();
            } else {
                airfields = mDaoSession.getAirfieldDao().queryBuilder()
                        .where(AirfieldDao.Properties.AFName.like("%" + keySearch + "%")).where(AirfieldDao.Properties.ShowList.eq(favorite)).limit(200).list();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return airfields;
    }

    public List<Airfield> getAirfieldsByICAOIATA(String keySearch, boolean favorite) {
        List<Airfield> airfields = null;
        try {
            openReadableDb();
            airfields = mDaoSession.getAirfieldDao().queryBuilder()
                    .whereOr(AirfieldDao.Properties.AFIATA.like("%" + keySearch + "%"),
                            AirfieldDao.Properties.AFICAO.like("%" + keySearch + "%")).where(AirfieldDao.Properties.ShowList.eq(favorite)).limit(200).list();

        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return airfields;
    }

    public Airfield getAirfieldsByName(String keySearch, boolean favorite) {
        Airfield airfield = null;
        try {
            openReadableDb();
            airfield = mDaoSession.getAirfieldDao().queryBuilder()
                    .where(AirfieldDao.Properties.AFName.eq(keySearch), AirfieldDao.Properties.ShowList.eq(favorite)).unique();

        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return airfield;
    }

/*    public boolean updateAirfield(Airfield airfield) {
        try {
            openWritableDb();
            mDaoSession.getAirfieldDao().insertOrReplace(airfield);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeDbConnections();
        }
    }*/

    public Flight getFlightByCode(byte[] code) {
        Flight response = null;
        try {
            openReadableDb();
            String conditionString = FlightDao.Properties.FlightCode.columnName + '=' + Utils.escapeBlobArgument(code);
            WhereCondition condition = new WhereCondition.StringCondition(conditionString);
            response = mDaoSession.getFlightDao().queryBuilder().where(condition).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return response;
    }

    public long getSizeFlight() {
        long count = 0;

        openReadableDb();
        try {
            count = mDaoSession.getFlightDao().count();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return count;
    }


    public List<Flight> getAllFlight() {
        List<Flight> response = null;
        try {
            openReadableDb();
            if (!mDaoMaster.getDatabase().isOpen()) {
                openReadableDb();
            }
            FlightDao flightDao = mDaoSession.getFlightDao();
            response = flightDao.loadAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        closeDbConnections();

        return response;
    }

    public ZTimeZone getTimeZoneByCode(int timeZoneCode) {
        ZTimeZone zTimeZone = null;
        try {
            openReadableDb();
            zTimeZone = mDaoSession.getZTimeZoneDao().queryBuilder().where(ZTimeZoneDao.Properties.TZCode.eq(timeZoneCode)).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return zTimeZone;
    }

    public ZTimeZoneDST getTimeZoneDSTByCode(String dSTCode) {
        ZTimeZoneDST zTimeZoneDST = null;
        try {
            openReadableDb();
            zTimeZoneDST = mDaoSession.getZTimeZoneDSTDao().queryBuilder().where(ZTimeZoneDSTDao.Properties.DSTCode.eq(dSTCode)).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return zTimeZoneDST;
    }

    public List<ZLaunch> getAllGliderLaunch() {
        List<ZLaunch> listZLaunch = null;
        try {
            openReadableDb();
            listZLaunch = mDaoSession.getZLaunchDao().loadAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return listZLaunch;
    }

    public ZLaunch getGliderLaunch(String pZLaunchCode) {
        ZLaunch zLaunch = null;
        try {
            openReadableDb();
            zLaunch = mDaoSession.getZLaunchDao().queryBuilder().where(ZLaunchDao.Properties.LaunchCode.eq(pZLaunchCode)).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return zLaunch;
    }

    public ZLaunch getGliderLaunch(int pZLaunchCode) {
        ZLaunch zLaunch = null;
        try {
            openReadableDb();
            zLaunch = mDaoSession.getZLaunchDao().queryBuilder().where(ZLaunchDao.Properties.LaunchCode.eq(pZLaunchCode)).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return zLaunch;
    }

    public ZLaunch getGliderLaunchByLaunchShort(String pZlaunchShort) {
        ZLaunch zLaunch = null;
        try {
            openReadableDb();
            zLaunch = mDaoSession.getZLaunchDao().queryBuilder().where(ZLaunchDao.Properties.LaunchShort.eq(pZlaunchShort)).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return zLaunch;
    }

    public List<ZLaunch> searchLaunches(String pText) {
        List<ZLaunch> launches = null;
        try {
            openReadableDb();
            launches = mDaoSession.getZLaunchDao().queryBuilder()
                    .whereOr(ZLaunchDao.Properties.LaunchCode.like("%" + pText + "%"),
                            ZLaunchDao.Properties.LaunchShort.like("%" + pText + "%")).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return launches;
    }

    public List<ZApproach> getAllZApproach() {
        List<ZApproach> list = null;
        try {
            openReadableDb();
            list = mDaoSession.getZApproachDao().loadAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return list;
    }

    public ZApproach getZApproach(String pZApproach) {
        ZApproach response = null;
        try {
            openReadableDb();
            response = mDaoSession.getZApproachDao().queryBuilder().where(ZApproachDao.Properties.APCode.eq(pZApproach)).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return response;
    }

    public ZApproach getZApproach(int pZApproach) {
        ZApproach response = null;
        try {
            openReadableDb();
            response = mDaoSession.getZApproachDao().queryBuilder().where(ZApproachDao.Properties.APCode.eq(pZApproach)).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return response;
    }

    public ZApproach getZApproachByAPShort(String pAPShort) {
        ZApproach response = null;
        try {
            openReadableDb();
            response = mDaoSession.getZApproachDao().queryBuilder().where(ZApproachDao.Properties.APShort.eq(pAPShort)).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return response;
    }

    public List<ZApproach> searchZApproach(String pText) {
        List<ZApproach> response = null;
        try {
            openReadableDb();
            response = mDaoSession.getZApproachDao().queryBuilder()
                    .whereOr(ZApproachDao.Properties.APCode.like("%" + pText + "%"),
                            ZApproachDao.Properties.APShort.like("%" + pText + "%"),
                            ZApproachDao.Properties.APLong.like("%" + pText + "%")).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return response;
    }

    public ZApproachCat getZApproachCat(String pAPCode) {
        ZApproachCat response = null;
        try {
            openReadableDb();
            response = mDaoSession.getZApproachCatDao().queryBuilder().where(ZApproachCatDao.Properties.APCat.eq(pAPCode)).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return response;
    }

    //delay
    public List<ZDelay> getAllZDelay(String search) {
        List<ZDelay> list = new ArrayList<>();
        try {
            String sqlSelect = "SELECT DelayCode,DelayDD,DelayShort,DelayLong,DelayGroupName from ZDelay INNER JOIN ZDelayGroup ON [ZDelay].[DelayGroupCode] = [ZDelayGroup].[DelayGroupCode] ";
          /*  if(!TextUtils.isEmpty(search)){
                String where = " WHERE DelayCode Like %"+search+"% OR DelayDD Like %"+search+"% OR DelayShort Like %"+search+"% OR DelayLong Like %"+search+"% OR DelayGroupName Like %"+search+"%";
                sqlSelect += where;
            }*/
            if (!mDaoMaster.getDatabase().isOpen()) {
                openReadableDb();
            }
            Cursor cursor = mDaoMaster.getDatabase().rawQuery(sqlSelect, null);
            if (cursor.moveToFirst()) {
                do {
                    Integer delayCode = cursor.getInt(0);
                    String delayDD = cursor.getString(1);
                    String delayShort = cursor.getString(2);
                    String delayLong = cursor.getString(3);
                    String delayGroupName = cursor.getString(4);
                    ZDelay zDelay = new ZDelay();
                    zDelay.setDelayCode(delayCode);
                    zDelay.setDelayDD(delayDD);
                    zDelay.setDelayShort(delayShort);
                    zDelay.setDelayLong(delayLong);
                    zDelay.setDelayGroup(delayGroupName);

                    list.add(zDelay);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return list;
    }

    public ZDelay getZDelay(String pCode) {
        ZDelay response = null;
        try {
            openReadableDb();
            response = mDaoSession.getZDelayDao().queryBuilder().where(ZDelayDao.Properties.DelayCode.eq(pCode)).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return response;
    }

    public ZDelay getZDelayByDelayShort(String pShort) {
        ZDelay response = null;
        try {
            openReadableDb();
            response = mDaoSession.getZDelayDao().queryBuilder().where(ZDelayDao.Properties.DelayShort.eq(pShort)).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return response;
    }

    public List<ZDelay> searchZDelay(String pText) {
        List<ZDelay> response = null;
        try {
            openReadableDb();
            response = mDaoSession.getZDelayDao().queryBuilder()
                    .whereOr(ZDelayDao.Properties.DelayCode.like("%" + pText + "%"),
                            ZDelayDao.Properties.DelayShort.like("%" + pText + "%"),
                            ZDelayDao.Properties.DelayLong.like("%" + pText + "%"),
                            ZDelayDao.Properties.DelayDD.like("%" + pText + "%")).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return response;
    }

    public ZDelayGroup getZDelayGroup(String pCode) {
        ZDelayGroup response = null;
        try {
            openReadableDb();
            response = mDaoSession.getZDelayGroupDao().queryBuilder().where(ZDelayGroupDao.Properties.DelayGroupCode.eq(pCode)).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return response;
    }

    public List<ZDelayGroup> getAllZDelayGroup() {
        List<ZDelayGroup> response = null;
        try {
            openReadableDb();
            response = mDaoSession.getZDelayGroupDao().queryBuilder().orderDesc(ZDelayGroupDao.Properties.DelayGroupName).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return response;
    }

    public List<ZDelay> getZDelayByGroupCode(String pCode) {
        List<ZDelay> response = null;
        try {
            openReadableDb();
            response = mDaoSession.getZDelayDao().queryBuilder().where(ZDelayDao.Properties.DelayGroupCode.eq(pCode)).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return response;
    }
    //end delay

    public List<ZOperation> getAllZOperation() {
        List<ZOperation> list = null;
        try {
            openReadableDb();
            list = mDaoSession.getZOperationDao().loadAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return list;
    }

    public ZOperation getZOperation(String pZOperation) {
        ZOperation response = null;
        try {
            openReadableDb();
            response = mDaoSession.getZOperationDao().queryBuilder().where(ZOperationDao.Properties.OpsCode.eq(pZOperation)).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return response;
    }

    public ZOperation getZOperation(int code) {
        ZOperation response = null;
        try {
            openReadableDb();
            response = mDaoSession.getZOperationDao().queryBuilder().where(ZOperationDao.Properties.OpsCode.eq(code)).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return response;
    }

    public ZOperation getZOperationByOpsShort(String pOpsShort) {
        ZOperation response = null;
        try {
            openReadableDb();
            response = mDaoSession.getZOperationDao().queryBuilder().where(ZOperationDao.Properties.OpsShort.eq(pOpsShort)).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return response;
    }

    public List<ZOperation> searchZOperation(String pText) {
        List<ZOperation> response = null;
        try {
            openReadableDb();
            response = mDaoSession.getZOperationDao().queryBuilder()
                    .whereOr(ZOperationDao.Properties.OpsCode.like("%" + pText + "%"),
                            ZOperationDao.Properties.OpsShort.like("%" + pText + "%"),
                            ZOperationDao.Properties.OpsLong.like("%" + pText + "%")).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return response;
    }

    public List<ZCountry> getAllCountry() {
        List<ZCountry> list = null;
        try {
            openReadableDb();
            list = mDaoSession.getZCountryDao().queryBuilder().orderAsc(ZCountryDao.Properties.RegAC)
                    .orderAsc(ZCountryDao.Properties.CountryName).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return list;
    }

    public List<ZCountry> searchCountry(String pText) {
        List<ZCountry> response = null;
        try {
            openReadableDb();
            response = mDaoSession.getZCountryDao().queryBuilder()
                    .whereOr(ZCountryDao.Properties.RegAC.like("%" + pText + "%"),
                            ZCountryDao.Properties.CountryName.like("%" + pText + "%")).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return response;
    }


    public SettingConfig getSetting(String name) {
        SettingConfig setting = null;
        try {
            openReadableDb();
            SettingConfigDao settingDao = mDaoSession.getSettingConfigDao();
            setting = settingDao.queryBuilder().where(SettingConfigDao.Properties.Name.eq(name)).build().unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return setting;
    }

    public SettingConfig getSetting(int code) {
        SettingConfig setting = null;
        try {
            openReadableDb();
            SettingConfigDao settingDao = mDaoSession.getSettingConfigDao();
            setting = settingDao.queryBuilder().where(SettingConfigDao.Properties.ConfigCode.eq(code)).unique();
        } catch (IllegalStateException ie) {
            ie.printStackTrace();
            closeDbConnections();
            return getSetting(code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //closeDbConnections();

        return setting;
    }

    public SettingLocal getSettingLocal(String name) {
        SettingLocal setting = null;
        try {
            openReadableDb();
            SettingLocalDao settingDao = mDaoSession.getSettingLocalDao();
            setting = settingDao.queryBuilder().where(SettingLocalDao.Properties.Name.eq(name)).build().unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return setting;
    }

    public SettingLocal getSettingLocal(int code) {
        SettingLocal setting = null;
        try {
            openReadableDb();
            SettingLocalDao settingDao = mDaoSession.getSettingLocalDao();
            setting = settingDao.queryBuilder().where(SettingLocalDao.Properties.ConfigCode.eq(code)).build().unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return setting;
    }

    public List<SettingConfig> getSettingGroup(String group) {
        List<SettingConfig> setting = null;
        try {
            openReadableDb();
            SettingConfigDao settingDao = mDaoSession.getSettingConfigDao();
            setting = settingDao.queryBuilder().where(SettingConfigDao.Properties.Group.eq(group)).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return setting;
    }

    public boolean updateSetting(String name, String value) {
        boolean result = true;
        SettingConfig setting = null;
        SettingConfigDao settingDao;
        try {
            openReadableDb();
            settingDao = mDaoSession.getSettingConfigDao();
            setting = settingDao.queryBuilder().where(SettingConfigDao.Properties.Name.eq(name)).build().unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (setting != null) {
            try {
                openWritableDb();
                String updateQuery = SqlUtils.createSqlUpdate(SettingConfigDao.TABLENAME, new String[]{SettingConfigDao.Properties.Data.name},
                        new String[]{SettingConfigDao.Properties.Name.name});
                mDaoMaster.getDatabase().execSQL(updateQuery, new Object[]{value, name});
            } catch (Exception e) {
                e.printStackTrace();
                result = false;
            }
        }
        closeDbConnections();

        return result;
    }

    public boolean updateSetting(int code, String value) {
        boolean result = true;
        SettingConfig setting = null;
        SettingConfigDao settingDao;
        try {
            openReadableDb();
            settingDao = mDaoSession.getSettingConfigDao();
            setting = settingDao.queryBuilder().where(SettingConfigDao.Properties.ConfigCode.eq(code)).build().unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (setting != null) {
            try {
                openWritableDb();
                String updateQuery = SqlUtils.createSqlUpdate(SettingConfigDao.TABLENAME, new String[]{SettingConfigDao.Properties.Data.name},
                        new String[]{SettingConfigDao.Properties.ConfigCode.name});
                mDaoMaster.getDatabase().execSQL(updateQuery, new Object[]{value, code});
            } catch (Exception e) {
                e.printStackTrace();
                result = false;
            }
        }
        closeDbConnections();

        return result;
    }

    public boolean updateSettingLocal(String name, String value) {
        boolean result = true;
        SettingLocal setting = null;
        SettingLocalDao settingDao;
        try {
            openReadableDb();
            settingDao = mDaoSession.getSettingLocalDao();
            setting = settingDao.queryBuilder().where(SettingLocalDao.Properties.Name.eq(name)).build().unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (setting != null) {
            try {
                openWritableDb();
                String updateQuery = SqlUtils.createSqlUpdate(SettingLocalDao.TABLENAME, new String[]{SettingLocalDao.Properties.Data.name},
                        new String[]{SettingLocalDao.Properties.Name.name});
                mDaoMaster.getDatabase().execSQL(updateQuery, new Object[]{value, name});
            } catch (Exception e) {
                e.printStackTrace();
                result = false;
            }
        }
        closeDbConnections();

        return result;
    }

    public boolean updateSettingLocal(int code, String value) {
        boolean result = true;
        SettingLocal setting = null;
        SettingLocalDao settingDao;
        try {
            openReadableDb();
            settingDao = mDaoSession.getSettingLocalDao();
            setting = settingDao.queryBuilder().where(SettingLocalDao.Properties.ConfigCode.eq(code)).build().unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (setting != null) {
            try {
                openWritableDb();
                String updateQuery = SqlUtils.createSqlUpdate(SettingLocalDao.TABLENAME, new String[]{SettingLocalDao.Properties.Data.name},
                        new String[]{SettingLocalDao.Properties.ConfigCode.name});
                mDaoMaster.getDatabase().execSQL(updateQuery, new Object[]{value, code});
            } catch (Exception e) {
                e.printStackTrace();
                result = false;
            }
        }
        closeDbConnections();

        return result;
    }

    public List<ZCurrency> getAllZCurrency() {
        List<ZCurrency> listZCurrency = null;
        try {
            openReadableDb();
            listZCurrency = mDaoSession.getZCurrencyDao().queryBuilder().orderAsc(ZCurrencyDao.Properties.CurrShort).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return listZCurrency;
    }

    public List<ZCurrency> searchZCurrency(String pText) {
        List<ZCurrency> response = null;
        try {
            openReadableDb();
            response = mDaoSession.getZCurrencyDao().queryBuilder()
                    .whereOr(ZCurrencyDao.Properties.CurrShort.like("%" + pText + "%"),
                            ZCurrencyDao.Properties.CurrLong.like("%" + pText + "%")).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return response;
    }

    public ZCurrency getZCurrencyByCode(String code) {
        ZCurrency zCurrency = null;
        try {
            openReadableDb();
            ZCurrencyDao currencyDao = mDaoSession.getZCurrencyDao();
            zCurrency = currencyDao.queryBuilder().where(ZCurrencyDao.Properties.CurrCode.eq(code)).build().unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return zCurrency;
    }

    public ZCurrency getZCurrencyByCode(int code) {
        ZCurrency zCurrency = null;
        try {
            openReadableDb();
            ZCurrencyDao currencyDao = mDaoSession.getZCurrencyDao();
            zCurrency = currencyDao.queryBuilder().where(ZCurrencyDao.Properties.CurrCode.eq(code)).build().unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return zCurrency;
    }

    public List<ZFNPT> getAllZFNPT() {
        List<ZFNPT> response = null;
        try {
            openReadableDb();
            response = mDaoSession.getZFNPTDao().loadAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return response;
    }

    public List<ZFNPT> getAllZFNPTByDrone(boolean isDrone) {
        List<ZFNPT> response = null;
        try {
            openReadableDb();
            response = mDaoSession.getZFNPTDao().queryBuilder().where(ZFNPTDao.Properties.Drone.eq(isDrone)).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return response;
    }

    public ZFNPT getFNPTByCode(int code, boolean isDrone) {
        ZFNPT response = null;
        try {
            openReadableDb();
            ZFNPTDao zfnptDao = mDaoSession.getZFNPTDao();
            response = zfnptDao.queryBuilder().where(ZFNPTDao.Properties.FnptCode.eq(code), ZFNPTDao.Properties.Drone.eq(isDrone)).build().unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return response;
    }

    public ZFNPT getFNPTByCode(int code) {
        ZFNPT response = null;
        try {
            openReadableDb();
            ZFNPTDao zfnptDao = mDaoSession.getZFNPTDao();
            response = zfnptDao.queryBuilder().where(ZFNPTDao.Properties.FnptCode.eq(code)).build().unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return response;
    }

    public List<ZFNPT> searchZFNPT(String pText) {
        List<ZFNPT> response = null;
        try {
            openReadableDb();
            response = mDaoSession.getZFNPTDao().queryBuilder()
                    .whereOr(ZFNPTDao.Properties.FnptCode.like("%" + pText + "%"),
                            ZFNPTDao.Properties.FnptShort.like("%" + pText + "%"),
                            ZFNPTDao.Properties.FnptLong.like("%" + pText + "%")).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return response;
    }

    public boolean insertAircraft(Aircraft aircraft) {
        try {
            openWritableDb();
            mDaoSession.getAircraftDao().insertOrReplace(aircraft);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeDbConnections();
        }
    }

    public boolean updateAircraft(Aircraft aircraft) {
        try {
            openWritableDb();
            mDaoSession.getAircraftDao().update(aircraft);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeDbConnections();
        }
    }

    public boolean insertFlight(Flight flight) {
        try {
            openWritableDb();
            mDaoSession.getFlightDao().insertOrReplace(flight);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeDbConnections();
        }
    }

    public boolean deleteFlight(byte[] flightCode) {
        boolean result = true;
        try {
            openWritableDb();
            mDatabase.beginTransaction();
            String conditionString = FlightDao.Properties.FlightCode.columnName + '=' + Utils.escapeBlobArgument(flightCode);
            WhereCondition condition = new WhereCondition.StringCondition(conditionString);
            mDaoSession.getFlightDao().queryBuilder().where(condition).buildDelete().executeDeleteWithoutDetachingEntities();
            RecordDelete recordDelete = new RecordDelete(FlightDao.TABLENAME, flightCode);
            insertDeleteRecord(recordDelete);
            mDatabase.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            mDatabase.endTransaction();
            closeDbConnections();
        }
        return result;
    }

    public boolean deleteFlight(int pType, FlightModel pFlight, FlightUtils.TimeMode timeMode) {
        boolean result = true;
        try {
            openWritableDb();
            mDatabase.beginTransaction();
            switch (pType) {
                case MCCPilotLogConst.LOGBOOK_DELETE_ONE:
                    String conditionString = FlightDao.Properties.FlightCode.columnName + '=' + Utils.escapeBlobArgument(pFlight.getFlightCode());
                    WhereCondition condition = new WhereCondition.StringCondition(conditionString);
                    mDaoSession.getFlightDao().queryBuilder().where(condition).buildDelete().executeDeleteWithoutDetachingEntities();
                    RecordDelete recordDelete = new RecordDelete(AircraftDao.TABLENAME, pFlight.getFlightCode());
                    insertDeleteRecord(recordDelete);
                    break;
                case MCCPilotLogConst.LOGBOOK_DELETE_ALL:
                    List<Flight> flightList = mDaoSession.getFlightDao().loadAll();
                    mDaoSession.getFlightDao().deleteAll();
                    List<RecordDelete> recordDeleteList = new ArrayList<>();
                    for (int i = 0; i < flightList.size(); i++) {
                        RecordDelete recordDelete2 = new RecordDelete(FlightDao.TABLENAME, flightList.get(i).getFlightCode());
                        recordDeleteList.add(recordDelete2);
                    }
                    insertDeleteRecords(recordDeleteList);
                    break;
                case MCCPilotLogConst.LOGBOOK_DELETE_PRIOR:
                    String dateColumnName = "";
                    switch (timeMode) {
                        case BASE:
                            dateColumnName = FlightDao.Properties.DateBASE.columnName;
                            break;
                        case LOCAL:
                            dateColumnName = FlightDao.Properties.DateLOCAL.columnName;
                            break;
                        case UTC:
                            dateColumnName = FlightDao.Properties.DateUTC.columnName;
                            break;
                    }
                    List<Flight> flightList1 = mDaoSession.getFlightDao().queryBuilder().where(new WhereCondition.StringCondition(dateColumnName + " <= DATE('now','-1 day')")).list();
                    mDaoSession.getDatabase().delete(FlightDao.TABLENAME, dateColumnName + " <= DATE('now','-1 day')", null);
                    List<RecordDelete> recordDeleteList1 = new ArrayList<>();
                    for (int i = 0; i < flightList1.size(); i++) {
                        RecordDelete recordDelete3 = new RecordDelete(FlightDao.TABLENAME, flightList1.get(i).getFlightCode());
                        recordDeleteList1.add(recordDelete3);
                    }
                    insertDeleteRecords(recordDeleteList1);
                    break;
                case MCCPilotLogConst.LOGBOOK_DELETE_IN:
                    //mDaoSession.getDatabase().delete(FlightDao.TABLENAME, FlightDao.Properties.DateUTC.columnName + " = '" + todayDate + "'", null);
                    break;
            }
            mDatabase.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            mDatabase.endTransaction();
            closeDbConnections();
        }
        return result;
    }

    public int getNumberOfFlights() {
        openReadableDb();
        int count = (int) mDaoSession.getFlightDao().count();
        closeDbConnections();
        return count;
    }

    public List<FlightModel> getDutyFlightList(int pTypeSort, String eventDateUTC, int eventStartUTC, int eventEndUTC) {
        List<FlightModel> flightModels = new ArrayList<>();
        FlightModel flightModel;
        String strOrderBy;
        switch (pTypeSort) {
            case MCCPilotLogConst.SORT_BY_1_GREAT_THAN_31:
                strOrderBy = " ORDER BY [flight].[DateUTC] ASC, FlightTime ASC, [flight].[FlightCode] ASC";
                break;
            case MCCPilotLogConst.SORT_BY_31_GREAT_THAN_1:
                strOrderBy = " ORDER BY [flight].[DateUTC] DESC, FlightTime DESC, [flight].[FlightCode] DESC";
                break;
            case MCCPilotLogConst.SORT_BY_31_LESS_THAN_1:
                strOrderBy = " ORDER BY [flight].[DateUTC] DESC, FlightTime ASC, [flight].[FlightCode] ASC";
                break;
            default:
                strOrderBy = " ORDER BY [flight].[DateUTC] ASC, FlightTime ASC, [flight].[FlightCode] ASC";
                break;
        }
        String where = "";
        if (eventStartUTC <= eventEndUTC) {
            where = "WHERE [flight].[DateUTC] == '" + eventDateUTC + "' AND [flight].[DepTimeUTC] > " + eventStartUTC + " AND [flight].[DepTimeUTC] < " + eventEndUTC;
        } else {
            where = "WHERE ([flight].[DateUTC] == '" + eventDateUTC + "' AND [flight].[DepTimeUTC] > " + eventEndUTC + ") OR ([flight].[DateUTC] == DATE('" + eventDateUTC + "','+1 day') AND [flight].[DepTimeUTC] < " + eventStartUTC + ")";
        }
        try {
            String sqlSelect = "SELECT FlightCode, DateUTC, DepCode, ArrCode, Reference, P1Code, P2Code, P3code, P4code, " +
                    "        CASE (SELECT [SettingConfig].[Data] FROM [SettingConfig] WHERE [SettingConfig].[ConfigCode] = 8)" +
                    " WHEN '0'" +
                    " THEN (CASE WHEN LENGTH([airfield1].[AFIcao]) >0 THEN [airfield1].[AFIcao] ELSE [airfield1].[AFIata] END)" +
                    " || '-' ||" +
                    " (CASE WHEN LENGTH([airfield2].[AFIcao]) >0 THEN [airfield2].[AFIcao] ELSE [airfield2].[AFIata] END)" +
                    " WHEN '1'" +
                    " THEN (CASE WHEN LENGTH([airfield1].[AFIata]) >0 THEN [airfield1].[AFIata] ELSE [airfield1].[AFIcao] END)" +
                    " || '-' ||" +
                    " (CASE WHEN LENGTH([airfield2].[AFIata]) >0 THEN [airfield2].[AFIata] ELSE [airfield2].[AFIcao] END)" +
                    "        END" +
                    " AS FlightAirfield," +
                    "(CASE [aircraft].[DeviceCode]" +
                    "    WHEN '2' THEN 'Simulator'" +
                    "    WHEN '3' THEN 'Drone'" +
                    "    ELSE [flight].[FlightNumber]" +
                    "    END) AS FlightNumber," +
                    " (CASE (SELECT [SettingConfig].[Data] FROM [SettingConfig] WHERE [SettingConfig].[ConfigCode] = 308)" +
                    " WHEN '1' THEN " +
                    "                          COALESCE(substr('00' || ([flight].[DepTimeUTC] / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ([flight].[DepTimeUTC] % 60), -2, 2) || ' - ' ||" +
                    "                          substr('00' || ([flight].[ArrTimeUTC] / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ([flight].[ArrTimeUTC] % 60), -2, 2), " +
                    "                          substr('00' || ([flight].[DepTimeSCHED] / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ([flight].[DepTimeSCHED] % 60), -2, 2) || ' - ' ||" +
                    "                          substr('00' || ([flight].[ArrTimeSCHED] / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ([flight].[ArrTimeSCHED] % 60), -2, 2), " +
                    "                          CASE WHEN [flight].[minTotal] <= 0 THEN NULL" +
                    "                          ELSE CASE (SELECT [SettingConfig].[Data] FROM [SettingConfig] WHERE [SettingConfig].[ConfigCode] = 50)" +
                    "                          WHEN '1' THEN round([flight].[minTotal] * 1.0 / 60, 1) || ''" +
                    "                          WHEN '0' THEN ([flight].[minTotal] / 60) || ':' || substr('00' || ([flight].[minTotal] % 60), -2, 2)" +
                    "                          END" +
                    "                          END) " +
                    " WHEN '0' THEN " +
                    "                          COALESCE(substr('00' || ((([flight].[DepTimeUTC] + [flight].[depOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([flight].[DepTimeUTC] + [flight].[depOffset] + 1440)%1440) % 60), -2, 2) || ' - ' ||" +
                    "                          substr('00' || ((([flight].[ArrTimeUTC]+ [flight].[arrOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([flight].[ArrTimeUTC]+ [flight].[arrOffset] + 1440)%1440) % 60), -2, 2), " +
                    "                          substr('00' || ((([flight].[DepTimeSCHED]+ [flight].[depOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([flight].[DepTimeSCHED]+ [flight].[depOffset] + 1440)%1440) % 60), -2, 2) || ' - ' ||" +
                    "                          substr('00' || ((([flight].[ArrTimeSCHED]+ [flight].[arrOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([flight].[ArrTimeSCHED]+ [flight].[arrOffset] + 1440)%1440) % 60), -2, 2), " +
                    "                          CASE WHEN [flight].[minTotal] <= 0 THEN NULL" +
                    "                          ELSE CASE (SELECT [SettingConfig].[Data] FROM [SettingConfig] WHERE [SettingConfig].[ConfigCode] = 50)" +
                    "                          WHEN '1' THEN round([flight].[minTotal] * 1.0 / 60, 1) || ''" +
                    "                          WHEN '0' THEN ([flight].[minTotal] / 60) || ':' || substr('00' || ([flight].[minTotal] % 60), -2, 2)" +
                    "                          END" +
                    "                          END) " +
                    " WHEN '-1' THEN " +
                    "                           COALESCE(substr('00' || ((([flight].[DepTimeUTC] + [flight].[baseOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([flight].[DepTimeUTC] + [flight].[baseOffset] + 1440)%1440) % 60), -2, 2) || ' - ' ||" +
                    "                          substr('00' || ((([flight].[ArrTimeUTC]+ [flight].[baseOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([flight].[ArrTimeUTC]+ [flight].[baseOffset] + 1440)%1440) % 60), -2, 2), " +
                    "                          substr('00' || ((([flight].[DepTimeSCHED]+ [flight].[baseOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([flight].[DepTimeSCHED]+ [flight].[baseOffset] + 1440)%1440) % 60), -2, 2) || ' - ' ||" +
                    "                          substr('00' || ((([flight].[ArrTimeSCHED]+ [flight].[baseOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([flight].[ArrTimeSCHED]+ [flight].[baseOffset] + 1440)%1440) % 60), -2, 2), " +
                    "                          CASE WHEN [flight].[minTotal] <= 0 THEN NULL" +
                    "                          ELSE CASE (SELECT [SettingConfig].[Data] FROM [SettingConfig] WHERE [SettingConfig].[ConfigCode] = 50)" +
                    "                          WHEN '1' THEN round([flight].[minTotal] * 1.0 / 60, 1) || ''" +
                    "                          WHEN '0' THEN ([flight].[minTotal] / 60) || ':' || substr('00' || ([flight].[minTotal] % 60), -2, 2)" +
                    "                          END" +
                    "                          END) " +
                    "END) AS FlightTime," +
                    "[flight].[ToEdit]," +
                    "[flight].[NextPage]," +
                    "[aircraft].[DeviceCode]," +
                    " (CASE (SELECT [SettingConfig].[Data] FROM [SettingConfig] WHERE [SettingConfig].[ConfigCode] = 309)" +
                    "    WHEN '0' THEN [flight].[DateLOCAL]" +
                    "    WHEN '-1' THEN [flight].[DateBASE]" +
                    "    ELSE [flight].[DateUTC]" +
                    "    END) AS FlightDate, [aircraft].[Reference]" +

                    " FROM [flight] LEFT JOIN [airfield] AS [airfield1] ON [flight].[DepCode] = [airfield1].[AFCode]" +
                    "               LEFT JOIN [airfield] AS [airfield2] ON [flight].[ArrCode] = [airfield2].[AFCode]" +
                    "               LEFT JOIN [aircraft] ON [flight].[AircraftCode] = [aircraft].[AircraftCode]" +
                    " " + where +
                   /* " WHERE (CASE" +
                    " WHEN [flight].[ToEdit] = 0" +
                    " THEN [aircraft].[DeviceCode] > 0 AND FlightDate >= date('now')" +
                    " ELSE [aircraft].[DeviceCode] > 0 AND FlightDate >= date('now','-30 day') END)" +*/
                    strOrderBy + ";";
            //End PL-61
            openReadableDb();
            if (!mDaoMaster.getDatabase().isOpen()) {
                openReadableDb();
            }
            Cursor cursor = mDaoMaster.getDatabase().rawQuery(sqlSelect, null);
            if (cursor.moveToFirst()) {
                do {
                    byte[] flightCode = cursor.getBlob(0);
                    String flightDate = cursor.getString(15);
                    byte[] flightDepCode = cursor.getBlob(2);
                    byte[] flightArrCode = cursor.getBlob(3);
                    String flightReference = cursor.getString(4);
                    byte[] flightP1 = cursor.getBlob(5);
                    byte[] flightP2 = cursor.getBlob(6);
                    byte[] flightP3 = cursor.getBlob(7);
                    byte[] flightP4 = cursor.getBlob(8);
                    String flightAirfield = cursor.getString(9);
                    String flightNumber = cursor.getString(10);
                    String flightTime = cursor.getString(11);
                    Long toEdit = cursor.getLong(12);
                    Long nextPage = cursor.getLong(13);
                    Long aircraftDeviceCode = cursor.getLong(14);
                    String aircraftName = cursor.getString(16);
                    flightModel = new FlightModel(flightCode, flightDate, flightArrCode, flightDepCode,
                            flightAirfield, flightNumber, flightTime, flightReference,
                            flightP1, flightP2, flightP3, flightP4, toEdit == 1 ? true : false,
                            nextPage == 1 ? true : false, aircraftDeviceCode, aircraftName);
                    flightModels.add(flightModel);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return flightModels;
    }


    public List<FlightModel> getFlightList(int pTypeSort) {
        List<FlightModel> flightModels = new ArrayList<>();
        FlightModel flightModel;
        String strOrderBy;
        switch (pTypeSort) {
            case MCCPilotLogConst.SORT_BY_1_GREAT_THAN_31:
                strOrderBy = " ORDER BY [flight].[DateUTC] ASC, FlightTime ASC, [flight].[FlightCode] ASC";
                break;
            case MCCPilotLogConst.SORT_BY_31_GREAT_THAN_1:
                strOrderBy = " ORDER BY [flight].[DateUTC] DESC, FlightTime DESC, [flight].[FlightCode] DESC";
                break;
            case MCCPilotLogConst.SORT_BY_31_LESS_THAN_1:
                strOrderBy = " ORDER BY [flight].[DateUTC] DESC, FlightTime ASC, [flight].[FlightCode] ASC";
                break;
            default:
                strOrderBy = " ORDER BY [flight].[DateUTC] ASC, FlightTime ASC, [flight].[FlightCode] ASC";
                break;
        }
        try {
            String sqlSelect = "SELECT FlightCode, DateUTC, DepCode, ArrCode, Reference, P1Code, P2Code, P3code, P4code, " +
                    "        CASE (SELECT [SettingConfig].[Data] FROM [SettingConfig] WHERE [SettingConfig].[ConfigCode] = 8)" +
                    " WHEN '0'" +
                    " THEN (CASE WHEN (LENGTH([airfield1].[AFIcao]) >0 AND [airfield1].[AFIcao]!='ZZZZ') THEN [airfield1].[AFIcao] ELSE [airfield1].[AFIata] END)" +
                    " || '-' ||" +
                    " (CASE WHEN LENGTH([airfield2].[AFIcao]) >0 THEN [airfield2].[AFIcao] ELSE [airfield2].[AFIata] END)" +
                    " WHEN '1'" +
                    " THEN (CASE WHEN LENGTH([airfield1].[AFIata]) >0 THEN [airfield1].[AFIata] ELSE [airfield1].[AFIcao] END)" +
                    " || '-' ||" +
                    " (CASE WHEN LENGTH([airfield2].[AFIata]) >0 THEN [airfield2].[AFIata] ELSE [airfield2].[AFIcao] END)" +
                    "        END" +
                    " AS FlightAirfield," +
                    "(CASE [aircraft].[DeviceCode]" +
                    "    WHEN '2' THEN 'Simulator'" +
                    "    WHEN '3' THEN 'Drone'" +
                    "    ELSE [flight].[FlightNumber]" +
                    "    END) AS FlightNumber," +
                    " (CASE (SELECT [SettingConfig].[Data] FROM [SettingConfig] WHERE [SettingConfig].[ConfigCode] = 308)" +
                    " WHEN '1' THEN " +
                    "                          COALESCE(substr('00' || ([flight].[DepTimeUTC] / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ([flight].[DepTimeUTC] % 60), -2, 2) || ' - ' ||" +
                    "                          substr('00' || ([flight].[ArrTimeUTC] / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ([flight].[ArrTimeUTC] % 60), -2, 2), " +
                    "                          substr('00' || ([flight].[DepTimeSCHED] / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ([flight].[DepTimeSCHED] % 60), -2, 2) || ' - ' ||" +
                    "                          substr('00' || ([flight].[ArrTimeSCHED] / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ([flight].[ArrTimeSCHED] % 60), -2, 2), " +
                    "                          CASE WHEN [flight].[minTotal] <= 0 THEN NULL" +
                    "                          ELSE CASE (SELECT [SettingConfig].[Data] FROM [SettingConfig] WHERE [SettingConfig].[ConfigCode] = 50)" +
                    "                          WHEN '1' THEN round([flight].[minTotal] * 1.0 / 60, 1) || ''" +
                    "                          WHEN '0' THEN ([flight].[minTotal] / 60) || ':' || substr('00' || ([flight].[minTotal] % 60), -2, 2)" +
                    "                          END" +
                    "                          END) " +
                    " WHEN '0' THEN " +
                    "                          COALESCE(substr('00' || ((([flight].[DepTimeUTC] + [flight].[depOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([flight].[DepTimeUTC] + [flight].[depOffset] + 1440)%1440) % 60), -2, 2) || ' - ' ||" +
                    "                          substr('00' || ((([flight].[ArrTimeUTC]+ [flight].[arrOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([flight].[ArrTimeUTC]+ [flight].[arrOffset] + 1440)%1440) % 60), -2, 2), " +
                    "                          substr('00' || ((([flight].[DepTimeSCHED]+ [flight].[depOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([flight].[DepTimeSCHED]+ [flight].[depOffset] + 1440)%1440) % 60), -2, 2) || ' - ' ||" +
                    "                          substr('00' || ((([flight].[ArrTimeSCHED]+ [flight].[arrOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([flight].[ArrTimeSCHED]+ [flight].[arrOffset] + 1440)%1440) % 60), -2, 2), " +
                    "                          CASE WHEN [flight].[minTotal] <= 0 THEN NULL" +
                    "                          ELSE CASE (SELECT [SettingConfig].[Data] FROM [SettingConfig] WHERE [SettingConfig].[ConfigCode] = 50)" +
                    "                          WHEN '1' THEN round([flight].[minTotal] * 1.0 / 60, 1) || ''" +
                    "                          WHEN '0' THEN ([flight].[minTotal] / 60) || ':' || substr('00' || ([flight].[minTotal] % 60), -2, 2)" +
                    "                          END" +
                    "                          END) " +
                    " WHEN '-1' THEN " +
                    "                           COALESCE(substr('00' || ((([flight].[DepTimeUTC] + [flight].[baseOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([flight].[DepTimeUTC] + [flight].[baseOffset] + 1440)%1440) % 60), -2, 2) || ' - ' ||" +
                    "                          substr('00' || ((([flight].[ArrTimeUTC]+ [flight].[baseOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([flight].[ArrTimeUTC]+ [flight].[baseOffset] + 1440)%1440) % 60), -2, 2), " +
                    "                          substr('00' || ((([flight].[DepTimeSCHED]+ [flight].[baseOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([flight].[DepTimeSCHED]+ [flight].[baseOffset] + 1440)%1440) % 60), -2, 2) || ' - ' ||" +
                    "                          substr('00' || ((([flight].[ArrTimeSCHED]+ [flight].[baseOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([flight].[ArrTimeSCHED]+ [flight].[baseOffset] + 1440)%1440) % 60), -2, 2), " +
                    "                          CASE WHEN [flight].[minTotal] <= 0 THEN NULL" +
                    "                          ELSE CASE (SELECT [SettingConfig].[Data] FROM [SettingConfig] WHERE [SettingConfig].[ConfigCode] = 50)" +
                    "                          WHEN '1' THEN round([flight].[minTotal] * 1.0 / 60, 1) || ''" +
                    "                          WHEN '0' THEN ([flight].[minTotal] / 60) || ':' || substr('00' || ([flight].[minTotal] % 60), -2, 2)" +
                    "                          END" +
                    "                          END) " +
                    "END) AS FlightTime," +
                    "[flight].[ToEdit]," +
                    "[flight].[NextPage]," +
                    "[aircraft].[DeviceCode]," +
                    " (CASE (SELECT [SettingConfig].[Data] FROM [SettingConfig] WHERE [SettingConfig].[ConfigCode] = 309)" +
                    "    WHEN '0' THEN [flight].[DateLOCAL]" +
                    "    WHEN '-1' THEN [flight].[DateBASE]" +
                    "    ELSE [flight].[DateUTC]" +
                    "    END) AS FlightDate, [aircraft].[Reference]" +

                    " FROM [flight] LEFT JOIN [airfield] AS [airfield1] ON [flight].[DepCode] = [airfield1].[AFCode]" +
                    "               LEFT JOIN [airfield] AS [airfield2] ON [flight].[ArrCode] = [airfield2].[AFCode]" +
                    "               LEFT JOIN [aircraft] ON [flight].[AircraftCode] = [aircraft].[AircraftCode]" +
                    " WHERE (CASE" +
                    " WHEN [flight].[ToEdit] = 0" +
                    " THEN [aircraft].[DeviceCode] > 0 AND FlightDate >= date('now')" +
                    " ELSE [aircraft].[DeviceCode] > 0 AND FlightDate >= date('now','-30 day') END)" +
                    strOrderBy + ";";
            //End PL-61
            openReadableDb();
            if (!mDaoMaster.getDatabase().isOpen()) {
                openReadableDb();
            }
            Cursor cursor = mDaoMaster.getDatabase().rawQuery(sqlSelect, null);
            if (cursor.moveToFirst()) {
                do {
                    byte[] flightCode = cursor.getBlob(0);
                    String flightDate = cursor.getString(15);
                    byte[] flightDepCode = cursor.getBlob(2);
                    byte[] flightArrCode = cursor.getBlob(3);
                    String flightReference = cursor.getString(4);
                    byte[] flightP1 = cursor.getBlob(5);
                    byte[] flightP2 = cursor.getBlob(6);
                    byte[] flightP3 = cursor.getBlob(7);
                    byte[] flightP4 = cursor.getBlob(8);
                    String flightAirfield = cursor.getString(9);
                    String flightNumber = cursor.getString(10);
                    String flightTime = cursor.getString(11);
                    Long toEdit = cursor.getLong(12);
                    Long nextPage = cursor.getLong(13);
                    Long aircraftDeviceCode = cursor.getLong(14);
                    String aircraftName = cursor.getString(16);
                    flightModel = new FlightModel(flightCode, flightDate, flightArrCode, flightDepCode,
                            flightAirfield, flightNumber, flightTime, flightReference,
                            flightP1, flightP2, flightP3, flightP4, toEdit == 1 ? true : false,
                            nextPage == 1 ? true : false, aircraftDeviceCode, aircraftName);
                    flightModels.add(flightModel);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return flightModels;
    }

    public List<FlightModel> getLogbookList(int pTypeSort, MccEnum.dateFilter dateFilter, byte[] aircraftCode,
                                            byte[] airfieldCode, byte[] pilotCode) {
        List<FlightModel> flightModels = new ArrayList<>();
        FlightModel flightModel;
        String strOrderBy;
        switch (pTypeSort) {
            case MCCPilotLogConst.SORT_BY_1_GREAT_THAN_31:
                strOrderBy = " ORDER BY [flight].[DateUTC] ASC, FlightTime ASC, [flight].[FlightCode] ASC";
                break;
            case MCCPilotLogConst.SORT_BY_31_GREAT_THAN_1:
                strOrderBy = " ORDER BY [flight].[DateUTC] DESC, FlightTime DESC, [flight].[FlightCode] DESC";
                break;
            case MCCPilotLogConst.SORT_BY_31_LESS_THAN_1:
                strOrderBy = " ORDER BY [flight].[DateUTC] DESC, FlightTime ASC, [flight].[FlightCode] ASC";
                break;
            default:
                strOrderBy = " ORDER BY [flight].[DateUTC] ASC, FlightTime ASC, [flight].[FlightCode] ASC";
                break;
        }
        String where = " ";
        String filterAircraft = "", filterPilot = "", filterAirfield = "";
        if (aircraftCode != null)
            filterAircraft = " AND [flight].[AircraftCode] = " + Utils.escapeBlobArgument(aircraftCode);
        if (pilotCode != null)
            filterPilot = " AND ([flight].[p1Code] = " + Utils.escapeBlobArgument(pilotCode) +
                    " OR [flight].[p2Code] = " + Utils.escapeBlobArgument(pilotCode) +
                    " OR [flight].[p3Code] = " + Utils.escapeBlobArgument(pilotCode) +
                    " OR [flight].[p4Code] = " + Utils.escapeBlobArgument(pilotCode) + ")";
        if (airfieldCode != null)
            filterAirfield = " AND ([flight].[depCode] = " + Utils.escapeBlobArgument(airfieldCode) + " OR [flight].[arrCode] = " + Utils.escapeBlobArgument(airfieldCode) + ")";
        switch (dateFilter) {
            case LAST_90_DAYS:
                where = " WHERE FlightDate >= DATE('now','-90 day')";
                break;
            case LAST_6_MONTHS:
                where = " WHERE FlightDate >= DATE('now','-180 day')";
                break;
            case YEAR_2017:
                where = " WHERE  strftime('%Y', date(FlightDate)) ==  strftime('%Y', date('now'))";
                break;
            case YEAR_2016:
                where = " WHERE  strftime('%Y', date(FlightDate, '+1 years')) ==  strftime('%Y', date('now'))";
                break;
            case ALL_REPORTS:
                break;
        }
        where = where + filterAircraft + filterAirfield + filterPilot;
        try {
            String sqlSelect = "SELECT FlightCode, DateUTC, DepCode, ArrCode, Reference, P1Code, P2Code, P3code, P4code, " +
                    "        CASE (SELECT [SettingConfig].[Data] FROM [SettingConfig] WHERE [SettingConfig].[ConfigCode] = 8)" +
                    " WHEN '0'" +
                    " THEN (CASE WHEN (LENGTH([airfield1].[AFIcao]) >0 AND [airfield1].[AFIcao]!='ZZZZ') THEN [airfield1].[AFIcao] ELSE [airfield1].[AFIata] END)" +
                    " || '-' ||" +
                    " (CASE WHEN LENGTH([airfield2].[AFIcao]) >0 THEN [airfield2].[AFIcao] ELSE [airfield2].[AFIata] END)" +
                    " WHEN '1'" +
                    " THEN (CASE WHEN LENGTH([airfield1].[AFIata]) >0 THEN [airfield1].[AFIata] ELSE [airfield1].[AFIcao] END)" +
                    " || '-' ||" +
                    " (CASE WHEN LENGTH([airfield2].[AFIata]) >0 THEN [airfield2].[AFIata] ELSE [airfield2].[AFIcao] END)" +
                    "        END" +
                    " AS FlightAirfield," +
                    "(CASE [aircraft].[DeviceCode]" +
                    "    WHEN '2' THEN 'Simulator'" +
                    "    WHEN '3' THEN 'Drone'" +
                    "    ELSE [flight].[FlightNumber]" +
                    "    END) AS FlightNumber," +
                    " (CASE (SELECT [SettingConfig].[Data] FROM [SettingConfig] WHERE [SettingConfig].[ConfigCode] = 308)" +
                    " WHEN '1' THEN " +
                    "                          COALESCE(substr('00' || ([flight].[DepTimeUTC] / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ([flight].[DepTimeUTC] % 60), -2, 2) || ' - ' ||" +
                    "                          substr('00' || ([flight].[ArrTimeUTC] / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ([flight].[ArrTimeUTC] % 60), -2, 2), " +
                    "                          substr('00' || ([flight].[DepTimeSCHED] / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ([flight].[DepTimeSCHED] % 60), -2, 2) || ' - ' ||" +
                    "                          substr('00' || ([flight].[ArrTimeSCHED] / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ([flight].[ArrTimeSCHED] % 60), -2, 2), " +
                    "                          CASE WHEN [flight].[minTotal] <= 0 THEN NULL" +
                    "                          ELSE CASE (SELECT [SettingConfig].[Data] FROM [SettingConfig] WHERE [SettingConfig].[ConfigCode] = 50)" +
                    "                          WHEN '1' THEN round([flight].[minTotal] * 1.0 / 60, 1) || ''" +
                    "                          WHEN '0' THEN ([flight].[minTotal] / 60) || ':' || substr('00' || ([flight].[minTotal] % 60), -2, 2)" +
                    "                          END" +
                    "                          END) " +
                    " WHEN '0' THEN " +
                    "                          COALESCE(substr('00' || ((([flight].[DepTimeUTC] + [flight].[depOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([flight].[DepTimeUTC] + [flight].[depOffset] + 1440)%1440) % 60), -2, 2) || ' - ' ||" +
                    "                          substr('00' || ((([flight].[ArrTimeUTC]+ [flight].[arrOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([flight].[ArrTimeUTC]+ [flight].[arrOffset] + 1440)%1440) % 60), -2, 2), " +
                    "                          substr('00' || ((([flight].[DepTimeSCHED]+ [flight].[depOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([flight].[DepTimeSCHED]+ [flight].[depOffset] + 1440)%1440) % 60), -2, 2) || ' - ' ||" +
                    "                          substr('00' || ((([flight].[ArrTimeSCHED]+ [flight].[arrOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([flight].[ArrTimeSCHED]+ [flight].[arrOffset] + 1440)%1440) % 60), -2, 2), " +
                    "                          CASE WHEN [flight].[minTotal] <= 0 THEN NULL" +
                    "                          ELSE CASE (SELECT [SettingConfig].[Data] FROM [SettingConfig] WHERE [SettingConfig].[ConfigCode] = 50)" +
                    "                          WHEN '1' THEN round([flight].[minTotal] * 1.0 / 60, 1) || ''" +
                    "                          WHEN '0' THEN ([flight].[minTotal] / 60) || ':' || substr('00' || ([flight].[minTotal] % 60), -2, 2)" +
                    "                          END" +
                    "                          END) " +
                    " WHEN '-1' THEN " +
                    "                           COALESCE(substr('00' || ((([flight].[DepTimeUTC] + [flight].[baseOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([flight].[DepTimeUTC] + [flight].[baseOffset] + 1440)%1440) % 60), -2, 2) || ' - ' ||" +
                    "                          substr('00' || ((([flight].[ArrTimeUTC]+ [flight].[baseOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([flight].[ArrTimeUTC]+ [flight].[baseOffset] + 1440)%1440) % 60), -2, 2), " +
                    "                          substr('00' || ((([flight].[DepTimeSCHED]+ [flight].[baseOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([flight].[DepTimeSCHED]+ [flight].[baseOffset] + 1440)%1440) % 60), -2, 2) || ' - ' ||" +
                    "                          substr('00' || ((([flight].[ArrTimeSCHED]+ [flight].[baseOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([flight].[ArrTimeSCHED]+ [flight].[baseOffset] + 1440)%1440) % 60), -2, 2), " +
                    "                          CASE WHEN [flight].[minTotal] <= 0 THEN NULL" +
                    "                          ELSE CASE (SELECT [SettingConfig].[Data] FROM [SettingConfig] WHERE [SettingConfig].[ConfigCode] = 50)" +
                    "                          WHEN '1' THEN round([flight].[minTotal] * 1.0 / 60, 1) || ''" +
                    "                          WHEN '0' THEN ([flight].[minTotal] / 60) || ':' || substr('00' || ([flight].[minTotal] % 60), -2, 2)" +
                    "                          END" +
                    "                          END) " +
                    "END) AS FlightTime," +
                    "[flight].[ToEdit]," +
                    "[flight].[NextPage]," +
                    "[aircraft].[DeviceCode]," +
                    " (CASE (SELECT [SettingConfig].[Data] FROM [SettingConfig] WHERE [SettingConfig].[ConfigCode] = 309)" +
                    "    WHEN '0' THEN [flight].[DateLOCAL]" +
                    "    WHEN '-1' THEN [flight].[DateBASE]" +
                    "    ELSE [flight].[DateUTC]" +
                    "    END) AS FlightDate, [aircraft].[Reference]" +
                    " FROM [flight] LEFT JOIN [airfield] AS [airfield1] ON [flight].[DepCode] = [airfield1].[AFCode]" +
                    "               LEFT JOIN [airfield] AS [airfield2] ON [flight].[ArrCode] = [airfield2].[AFCode]" +
                    "               LEFT JOIN [aircraft] ON [flight].[AircraftCode] = [aircraft].[AircraftCode]" + where +
                   /* " WHERE (CASE" +
                    " WHEN [flight].[ToEdit] = 0" +
                    " THEN [flight].[AircraftCode] > 0 AND FlightDate >= date('now')" +
                    " ELSE [flight].[AircraftCode] > 0 AND FlightDate >= date('now','-30 day') END)" +*/
                    /*"               LEFT JOIN [pilot] ON [flight].[P1Code] = [pilot].[PilotCode]"+*/
                    strOrderBy + ";";

            //End PL-61
            openReadableDb();
            if (!mDaoMaster.getDatabase().isOpen()) {
                openReadableDb();
            }
            Cursor cursor = mDaoMaster.getDatabase().rawQuery(sqlSelect, null);
            if (cursor.moveToFirst()) {
                do {
                    byte[] flightCode = cursor.getBlob(0);
                    String flightDate = cursor.getString(15);
                    byte[] flightDepCode = cursor.getBlob(2);
                    byte[] flightArrCode = cursor.getBlob(3);
                    String flightReference = cursor.getString(4);
                    byte[] flightP1 = cursor.getBlob(5);
                    byte[] flightP2 = cursor.getBlob(6);
                    byte[] flightP3 = cursor.getBlob(7);
                    byte[] flightP4 = cursor.getBlob(8);
                    String flightAirfield = cursor.getString(9);
                    String flightNumber = cursor.getString(10);
                    String flightTime = cursor.getString(11);
                    Long toEdit = cursor.getLong(12);
                    Long nextPage = cursor.getLong(13);
                    Long aircraftDeviceCode = cursor.getLong(14);
                    String aircraftName = cursor.getString(16);
                    flightModel = new FlightModel(flightCode, flightDate, flightArrCode, flightDepCode,
                            flightAirfield, flightNumber, flightTime, flightReference,
                            flightP1, flightP2, flightP3, flightP4, toEdit == 1 ? true : false,
                            nextPage == 1 ? true : false, aircraftDeviceCode, aircraftName);
                    flightModels.add(flightModel);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return flightModels;
    }

    public List<FlightModel> getLogbookListByAircraftPilotAirfield(MccEnum.dateFilter dateFilter, byte[] aircraftCode,
                                                                   byte[] airfieldCode, byte[] pilotCode) {
        List<FlightModel> flightModels = new ArrayList<>();
        FlightModel flightModel;
        String strOrderBy = " ORDER BY [flight].[DateUTC] DESC, [flight].[DepTimeUTC] DESC";
        String where = " ";
        String filterAircraft = "", filterPilot = "", filterAirfield = "";
        if (aircraftCode != null)
            filterAircraft = " AND [flight].[AircraftCode] = " + Utils.escapeBlobArgument(aircraftCode);
        if (pilotCode != null)
            filterPilot = " AND ([flight].[p1Code] = " + Utils.escapeBlobArgument(pilotCode) +
                    " OR [flight].[p2Code] = " + Utils.escapeBlobArgument(pilotCode) +
                    " OR [flight].[p3Code] = " + Utils.escapeBlobArgument(pilotCode) +
                    " OR [flight].[p4Code] = " + Utils.escapeBlobArgument(pilotCode) + ")";
        if (airfieldCode != null)
            filterAirfield = " AND ([flight].[DepCode] = " + Utils.escapeBlobArgument(airfieldCode) + " OR [flight].[ArrCode] = " + Utils.escapeBlobArgument(airfieldCode) + ")";
        switch (dateFilter) {
            case LAST_90_DAYS:
                where = " WHERE FlightDate >= DATE('now','-90 day')";
                break;
            case LAST_6_MONTHS:
                where = " WHERE FlightDate >= DATE('now','-180 day')";
                break;
            case YEAR_2017:
                where = " WHERE  strftime('%Y', date(FlightDate)) ==  strftime('%Y', date('now'))";
                break;
            case YEAR_2016:
                where = " WHERE  strftime('%Y', date(FlightDate, '+1 years')) ==  strftime('%Y', date('now'))";
                break;
            case ALL_REPORTS:
                break;
        }
        where = where + filterAircraft + filterAirfield + filterPilot;
        try {
            String sqlSelect = "SELECT FlightCode, DateUTC, DepCode, ArrCode, Reference, P1Code, P2Code, P3code, P4code, " +
                    "        CASE (SELECT [SettingConfig].[Data] FROM [SettingConfig] WHERE [SettingConfig].[ConfigCode] = 8)" +
                    " WHEN '0'" +
                    " THEN (CASE WHEN (LENGTH([airfield1].[AFIcao]) >0 AND [airfield1].[AFIcao]!='ZZZZ') THEN [airfield1].[AFIcao] ELSE [airfield1].[AFIata] END)" +
                    " || '-' ||" +
                    " (CASE WHEN LENGTH([airfield2].[AFIcao]) >0 THEN [airfield2].[AFIcao] ELSE [airfield2].[AFIata] END)" +
                    " WHEN '1'" +
                    " THEN (CASE WHEN LENGTH([airfield1].[AFIata]) >0 THEN [airfield1].[AFIata] ELSE [airfield1].[AFIcao] END)" +
                    " || '-' ||" +
                    " (CASE WHEN LENGTH([airfield2].[AFIata]) >0 THEN [airfield2].[AFIata] ELSE [airfield2].[AFIcao] END)" +
                    "        END" +
                    " AS FlightAirfield," +
                    "(CASE [aircraft].[DeviceCode]" +
                    "    WHEN '2' THEN 'Simulator'" +
                    "    WHEN '3' THEN 'Drone'" +
                    "    ELSE [flight].[FlightNumber]" +
                    "    END) AS FlightNumber," +
                    " (CASE (SELECT [SettingConfig].[Data] FROM [SettingConfig] WHERE [SettingConfig].[ConfigCode] = 308)" +
                    " WHEN '1' THEN " +
                    "                          COALESCE(substr('00' || ([flight].[DepTimeUTC] / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ([flight].[DepTimeUTC] % 60), -2, 2) || ' - ' ||" +
                    "                          substr('00' || ([flight].[ArrTimeUTC] / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ([flight].[ArrTimeUTC] % 60), -2, 2), " +
                    "                          substr('00' || ([flight].[DepTimeSCHED] / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ([flight].[DepTimeSCHED] % 60), -2, 2) || ' - ' ||" +
                    "                          substr('00' || ([flight].[ArrTimeSCHED] / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ([flight].[ArrTimeSCHED] % 60), -2, 2), " +
                    "                          CASE WHEN [flight].[minTotal] <= 0 THEN NULL" +
                    "                          ELSE CASE (SELECT [SettingConfig].[Data] FROM [SettingConfig] WHERE [SettingConfig].[ConfigCode] = 50)" +
                    "                          WHEN '1' THEN round([flight].[minTotal] * 1.0 / 60, 1) || ''" +
                    "                          WHEN '0' THEN ([flight].[minTotal] / 60) || ':' || substr('00' || ([flight].[minTotal] % 60), -2, 2)" +
                    "                          END" +
                    "                          END) " +
                    " WHEN '0' THEN " +
                    "                          COALESCE(substr('00' || ((([flight].[DepTimeUTC] + [flight].[depOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([flight].[DepTimeUTC] + [flight].[depOffset] + 1440)%1440) % 60), -2, 2) || ' - ' ||" +
                    "                          substr('00' || ((([flight].[ArrTimeUTC]+ [flight].[arrOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([flight].[ArrTimeUTC]+ [flight].[arrOffset] + 1440)%1440) % 60), -2, 2), " +
                    "                          substr('00' || ((([flight].[DepTimeSCHED]+ [flight].[depOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([flight].[DepTimeSCHED]+ [flight].[depOffset] + 1440)%1440) % 60), -2, 2) || ' - ' ||" +
                    "                          substr('00' || ((([flight].[ArrTimeSCHED]+ [flight].[arrOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([flight].[ArrTimeSCHED]+ [flight].[arrOffset] + 1440)%1440) % 60), -2, 2), " +
                    "                          CASE WHEN [flight].[minTotal] <= 0 THEN NULL" +
                    "                          ELSE CASE (SELECT [SettingConfig].[Data] FROM [SettingConfig] WHERE [SettingConfig].[ConfigCode] = 50)" +
                    "                          WHEN '1' THEN round([flight].[minTotal] * 1.0 / 60, 1) || ''" +
                    "                          WHEN '0' THEN ([flight].[minTotal] / 60) || ':' || substr('00' || ([flight].[minTotal] % 60), -2, 2)" +
                    "                          END" +
                    "                          END) " +
                    " WHEN '-1' THEN " +
                    "                           COALESCE(substr('00' || ((([flight].[DepTimeUTC] + [flight].[baseOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([flight].[DepTimeUTC] + [flight].[baseOffset] + 1440)%1440) % 60), -2, 2) || ' - ' ||" +
                    "                          substr('00' || ((([flight].[ArrTimeUTC]+ [flight].[baseOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([flight].[ArrTimeUTC]+ [flight].[baseOffset] + 1440)%1440) % 60), -2, 2), " +
                    "                          substr('00' || ((([flight].[DepTimeSCHED]+ [flight].[baseOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([flight].[DepTimeSCHED]+ [flight].[baseOffset] + 1440)%1440) % 60), -2, 2) || ' - ' ||" +
                    "                          substr('00' || ((([flight].[ArrTimeSCHED]+ [flight].[baseOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([flight].[ArrTimeSCHED]+ [flight].[baseOffset] + 1440)%1440) % 60), -2, 2), " +
                    "                          CASE WHEN [flight].[minTotal] <= 0 THEN NULL" +
                    "                          ELSE CASE (SELECT [SettingConfig].[Data] FROM [SettingConfig] WHERE [SettingConfig].[ConfigCode] = 50)" +
                    "                          WHEN '1' THEN round([flight].[minTotal] * 1.0 / 60, 1) || ''" +
                    "                          WHEN '0' THEN ([flight].[minTotal] / 60) || ':' || substr('00' || ([flight].[minTotal] % 60), -2, 2)" +
                    "                          END" +
                    "                          END) " +
                    "END) AS FlightTime," +
                    "[flight].[ToEdit]," +
                    "[flight].[NextPage]," +
                    "[aircraft].[DeviceCode]," +
                    " (CASE (SELECT [SettingConfig].[Data] FROM [SettingConfig] WHERE [SettingConfig].[ConfigCode] = 309)" +
                    "    WHEN '0' THEN [flight].[DateLOCAL]" +
                    "    WHEN '-1' THEN [flight].[DateBASE]" +
                    "    ELSE [flight].[DateUTC]" +
                    "    END) AS FlightDate, [aircraft].[Reference]" +
                    " FROM [flight] LEFT JOIN [airfield] AS [airfield1] ON [flight].[DepCode] = [airfield1].[AFCode]" +
                    "               LEFT JOIN [airfield] AS [airfield2] ON [flight].[ArrCode] = [airfield2].[AFCode]" +
                    "               LEFT JOIN [aircraft] ON [flight].[AircraftCode] = [aircraft].[AircraftCode]" + where +
                   /* " WHERE (CASE" +
                    " WHEN [flight].[ToEdit] = 0" +
                    " THEN [flight].[AircraftCode] > 0 AND FlightDate >= date('now')" +
                    " ELSE [flight].[AircraftCode] > 0 AND FlightDate >= date('now','-30 day') END)" +*/
                    /*"               LEFT JOIN [pilot] ON [flight].[P1Code] = [pilot].[PilotCode]"+*/
                    strOrderBy + ";";

            //End PL-61
            openReadableDb();
            if (!mDaoMaster.getDatabase().isOpen()) {
                openReadableDb();
            }
            Cursor cursor = mDaoMaster.getDatabase().rawQuery(sqlSelect, null);
            if (cursor.moveToFirst()) {
                do {
                    byte[] flightCode = cursor.getBlob(0);
                    String flightDate = cursor.getString(15);
                    byte[] flightDepCode = cursor.getBlob(2);
                    byte[] flightArrCode = cursor.getBlob(3);
                    String flightReference = cursor.getString(4);
                    byte[] flightP1 = cursor.getBlob(5);
                    byte[] flightP2 = cursor.getBlob(6);
                    byte[] flightP3 = cursor.getBlob(7);
                    byte[] flightP4 = cursor.getBlob(8);
                    String flightAirfield = cursor.getString(9);
                    String flightNumber = cursor.getString(10);
                    String flightTime = cursor.getString(11);
                    Long toEdit = cursor.getLong(12);
                    Long nextPage = cursor.getLong(13);
                    Long aircraftDeviceCode = cursor.getLong(14);
                    String aircraftName = cursor.getString(16);
                    flightModel = new FlightModel(flightCode, flightDate, flightArrCode, flightDepCode,
                            flightAirfield, flightNumber, flightTime, flightReference,
                            flightP1, flightP2, flightP3, flightP4, toEdit == 1 ? true : false,
                            nextPage == 1 ? true : false, aircraftDeviceCode, aircraftName);
                    flightModels.add(flightModel);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return flightModels;
    }

    public Duty getDutyByCode(byte[] code) {
        if (code == null) return null;
        Duty response = null;
        try {
            openReadableDb();
            String conditionString = DutyDao.Properties.DutyCode.columnName + '=' + Utils.escapeBlobArgument(code);
            WhereCondition condition = new WhereCondition.StringCondition(conditionString);
            response = mDaoSession.getDutyDao().queryBuilder().where(condition).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return response;
    }

    public List<Duty> getAllDuty() {
        List<Duty> response = null;
        try {
            openReadableDb();
            if (!mDaoMaster.getDatabase().isOpen()) {
                openReadableDb();
            }
            DutyDao dutyDao = mDaoSession.getDutyDao();
            response = dutyDao.loadAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        closeDbConnections();

        return response;
    }

    public boolean deleteDuty(byte[] code) {
        boolean result = true;
        try {
            openWritableDb();
            mDatabase.beginTransaction();
            String conditionString = DutyDao.Properties.DutyCode.columnName + '=' + Utils.escapeBlobArgument(code);
            WhereCondition condition = new WhereCondition.StringCondition(conditionString);
            mDaoSession.getDutyDao().queryBuilder().where(condition).buildDelete().executeDeleteWithoutDetachingEntities();
            RecordDelete recordDelete = new RecordDelete(DutyDao.TABLENAME, code);
            insertDeleteRecord(recordDelete);
            mDatabase.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            mDatabase.endTransaction();
            closeDbConnections();
        }
        return result;

    }

    public boolean deleteAllDuties() {
        boolean result = true;
        try {
            openWritableDb();
            mDatabase.beginTransaction();
            DutyDao dutyDao = mDaoSession.getDutyDao();
            List<Duty> dutyList = dutyDao.loadAll();
            dutyDao.queryBuilder()
                    .buildDelete().executeDeleteWithoutDetachingEntities();
            List<RecordDelete> recordDeleteList = new ArrayList<>();
            for (int i = 0; i < dutyList.size(); i++) {
                RecordDelete recordDelete = new RecordDelete(DutyDao.TABLENAME, dutyList.get(i).getDutyCode());
                recordDeleteList.add(recordDelete);
            }
            insertDeleteRecords(recordDeleteList);
            mDatabase.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            mDatabase.endTransaction();
            closeDbConnections();
        }
        return result;
    }

    public List<DutyModels> getDutyList(int pTypeSort) {
        List<DutyModels> dutyModels = new ArrayList<>();
        DutyModels dutyModel;
        String strOrderBy;
        switch (pTypeSort) {
            case MCCPilotLogConst.SORT_BY_1_GREAT_THAN_31:
                strOrderBy = " ORDER BY DutyDate ASC, DutyTime ASC, [duty].[DutyCode] ASC";
                break;
            case MCCPilotLogConst.SORT_BY_31_GREAT_THAN_1:
                strOrderBy = " ORDER BY DutyDate DESC, DutyTime DESC, [duty].[DutyCode] DESC";
                break;
            case MCCPilotLogConst.SORT_BY_31_LESS_THAN_1:
                strOrderBy = " ORDER BY DutyDate DESC, DutyTime ASC, [duty].[DutyCode] ASC";
                break;
            default:
                strOrderBy = " ORDER BY DutyDate ASC, DutyTime ASC, [duty].[DutyCode] ASC";
                break;
        }
        try {
            String sqlSelect = "SELECT DutyCode, " +
                    " (CASE (SELECT [SettingConfig].[Data] FROM [SettingConfig] WHERE [SettingConfig].[ConfigCode] = 308)" +
                    " WHEN '1' THEN " +
                    "                          substr('00' || (NULLIF([duty].[EventStartUTC], [duty].[EventEndUTC]) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || (NULLIF([duty].[EventStartUTC], [duty].[EventEndUTC]) % 60), -2, 2) || ' - ' ||" +
                    "                          substr('00' || (NULLIF([duty].[EventEndUTC], [duty].[EventStartUTC]) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || (NULLIF([duty].[EventEndUTC], [duty].[EventStartUTC]) % 60), -2, 2) " +
                    "                           " +
                    " WHEN '0' THEN " +
                    "                          substr('00' || ((([duty].[EventStartUTC] + [duty].[startOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([duty].[EventStartUTC] + [duty].[startOffset] + 1440)%1440) % 60), -2, 2) || ' - ' ||" +
                    "                          substr('00' || ((([duty].[EventEndUTC]+ [duty].[endOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([duty].[EventEndUTC]+ [duty].[endOffset] + 1440)%1440) % 60), -2, 2) " +
                    "                           " +
                    " WHEN '-1' THEN " +
                    "                           substr('00' || ((([duty].[EventStartUTC] + [duty].[baseOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([duty].[EventStartUTC] + [duty].[baseOffset] + 1440)%1440) % 60), -2, 2) || ' - ' ||" +
                    "                          substr('00' || ((([duty].[EventEndUTC]+ [duty].[baseOffset] + 1440)%1440) / 60), -2, 2) || ':' ||" +
                    "                          substr('00' || ((([duty].[EventEndUTC]+ [duty].[baseOffset] + 1440)%1440) % 60), -2, 2) " +
                    "                         " +
                    "END) AS DutyTime," +
                    " (CASE (SELECT [SettingConfig].[Data] FROM [SettingConfig] WHERE [SettingConfig].[ConfigCode] = 309)" +
                    "    WHEN '0' THEN [duty].[EventDateLOCAL]" +
                    "    WHEN '-1' THEN [duty].[EventDateBASE]" +
                    "    ELSE [duty].[EventDateUTC]" +
                    "    END) AS DutyDate, " +
                    "  (CASE (SELECT [SettingConfig].[Data] FROM [SettingConfig] WHERE [SettingConfig].[ConfigCode] = 50)" +
                    "                          WHEN '1' THEN round([duty].[duration] * 1.0 / 60, 1) || ''" +
                    "                          WHEN '0' THEN ([duty].[duration] / 60) || ':' || substr('00' || ([duty].[duration] % 60), -2, 2)" +
                    "                          END) AS Duration, " +
                    " EventDescription, EventDateUTC, EventStartUTC, EventEndUTC " +
                    " FROM [duty] " +
                    strOrderBy + ";";
            //End PL-61
            openReadableDb();
            if (!mDaoMaster.getDatabase().isOpen()) {
                openReadableDb();
            }
            Cursor cursor = mDaoMaster.getDatabase().rawQuery(sqlSelect, null);
            if (cursor.moveToFirst()) {
                do {
                    byte[] dutyCode = cursor.getBlob(0);
                    String dutyTime = cursor.getString(1);
                    String dutyDate = cursor.getString(2);
                    String dutyDuration = cursor.getString(3);
                    String dutyDescription = cursor.getString(4);
                    String eventDateUTC = cursor.getString(5);
                    int eventStartUTC = cursor.getInt(6);
                    int eventEndUTC = cursor.getInt(7);

                    dutyModel = new DutyModels(dutyCode, dutyDescription, dutyDate, dutyTime, dutyDuration, eventDateUTC, eventStartUTC, eventEndUTC);
                    dutyModels.add(dutyModel);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return dutyModels;
    }

    public boolean insertDuty(Duty duty) {
        try {
            openWritableDb();
            mDaoSession.getDutyDao().insertOrReplace(duty);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeDbConnections();
        }
    }


    public List<ZExpenseGroup> getAllExpenseGroup() {
        List<ZExpenseGroup> response = null;
        try {
            openReadableDb();
            if (!mDaoMaster.getDatabase().isOpen()) {
                openReadableDb();
            }
            ZExpenseGroupDao zExpenseGroupDao = mDaoSession.getZExpenseGroupDao();
            //response = zExpenseGroupDao.loadAll();
            response = zExpenseGroupDao.queryBuilder().orderAsc(ZExpenseGroupDao.Properties.ExpGroupShort).list();
        } catch (Exception e) {
            e.printStackTrace();
        }

        closeDbConnections();

        return response;
    }

    public List<ZExpense> getAllZExpense() {
        List<ZExpense> response = null;
        try {
            openReadableDb();
            if (!mDaoMaster.getDatabase().isOpen()) {
                openReadableDb();
            }
            ZExpenseDao zExpenseDao = mDaoSession.getZExpenseDao();
            response = zExpenseDao.loadAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        closeDbConnections();

        return response;
    }

    public boolean deleteExpense(byte[] code) {
        boolean result = true;
        try {
            openWritableDb();
            mDatabase.beginTransaction();
            String conditionString = ExpenseDao.Properties.ExpCode.columnName + '=' + Utils.escapeBlobArgument(code);
            WhereCondition condition = new WhereCondition.StringCondition(conditionString);
            mDaoSession.getExpenseDao().queryBuilder().where(condition).buildDelete().executeDeleteWithoutDetachingEntities();
            RecordDelete recordDelete = new RecordDelete(ExpenseDao.TABLENAME, code);
            insertDeleteRecord(recordDelete);
            mDatabase.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            mDatabase.endTransaction();
            closeDbConnections();
        }
        return result;
    }

    public boolean deleteAllExpenses() {
        boolean result = true;
        try {
            openWritableDb();
            mDatabase.beginTransaction();
            ExpenseDao expenseDao = mDaoSession.getExpenseDao();
            List<Expense> expenseList = expenseDao.loadAll();
            expenseDao.queryBuilder()
                    .buildDelete().executeDeleteWithoutDetachingEntities();
            List<RecordDelete> recordDeleteList = new ArrayList<>();
            for (int i = 0; i < expenseList.size(); i++) {
                RecordDelete recordDelete = new RecordDelete(ExpenseDao.TABLENAME, expenseList.get(i).getExpCode());
                recordDeleteList.add(recordDelete);
            }
            insertDeleteRecords(recordDeleteList);
            mDatabase.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            mDatabase.endTransaction();
            closeDbConnections();
        }
        return result;
    }

    public Expense getExpenseByCode(byte[] code) {
        if (code == null) return null;
        Expense response = null;
        try {
            openReadableDb();
            String conditionString = ExpenseDao.Properties.ExpCode.columnName + '=' + Utils.escapeBlobArgument(code);
            WhereCondition condition = new WhereCondition.StringCondition(conditionString);
            response = mDaoSession.getExpenseDao().queryBuilder().where(condition).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return response;
    }

    public List<Expense> getAllExpense(int pTypeSort) {
        List<Expense> response = new ArrayList<>();
        String strOrderBy;
        switch (pTypeSort) {
            case MCCPilotLogConst.SORT_BY_1_GREAT_THAN_31:
                strOrderBy = " ORDER BY [expense].[ExpDate] ASC, [expense].[ExpCode] ASC";
                break;
            case MCCPilotLogConst.SORT_BY_31_GREAT_THAN_1:
                strOrderBy = " ORDER BY [expense].[ExpDate] DESC, [expense].[ExpCode] DESC";
                break;
            case MCCPilotLogConst.SORT_BY_31_LESS_THAN_1:
                strOrderBy = " ORDER BY [expense].[ExpDate] DESC, [expense].[ExpCode] ASC";
                break;
            default:
                strOrderBy = " ORDER BY [expense].[ExpDate] ASC, [expense].[ExpCode] ASC";
                break;
        }

        try {
            String sqlSelect = "SELECT * FROM EXPENSE " +
                    strOrderBy + ";";
            openReadableDb();
            if (!mDaoMaster.getDatabase().isOpen()) {
                openReadableDb();
            }
            Cursor cursor = mDaoMaster.getDatabase().rawQuery(sqlSelect, null);
            if (cursor.moveToFirst()) {
                do {
                    byte[] expCode = cursor.getBlob(0);
                    String date = cursor.getString(1);
                    int etCode = cursor.getInt(2);
                    String description = cursor.getString(3);
                    Long amount = cursor.getLong(4);
                    int currCode = cursor.getInt(5);
                    Long amountForeign = cursor.getLong(6);
                    int currCodeForeign = cursor.getInt(7);
                    int linkTable = cursor.getInt(8);
                    byte[] linkCode = cursor.getBlob(9);
                    Long recordModify = cursor.getLong(10);
                    int recordUpload = cursor.getInt(11);
                    Expense expense = new Expense(expCode, date, etCode, description, amount, currCode, amountForeign, currCodeForeign, linkTable, linkCode, recordModify, recordUpload == 1 ? true : false);
                    response.add(expense);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    /*    closeDbConnections();
        try {
            openReadableDb();
            if (!mDaoMaster.getDatabase().isOpen()) {
                openReadableDb();
            }
            ExpenseDao expenseDao = mDaoSession.getExpenseDao();
            response = expenseDao.loadAll();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        closeDbConnections();

        return response;
    }


    public List<ZExpense> getAllZExpenseByGroup(int groupCode) {
        List<ZExpense> response = null;
        try {
            openReadableDb();
            if (!mDaoMaster.getDatabase().isOpen()) {
                openReadableDb();
            }
            ZExpenseDao zExpenseDao = mDaoSession.getZExpenseDao();
            response = zExpenseDao.queryBuilder().where(ZExpenseDao.Properties.ExpGroupCode.eq(groupCode)).orderAsc(ZExpenseDao.Properties.ExpTypeShort).list();
        } catch (Exception e) {
            e.printStackTrace();
        }

        closeDbConnections();

        return response;
    }

    public ZExpenseGroup getZExpenseGroupByCode(int groupCode) {
        ZExpenseGroup response = null;
        try {
            openReadableDb();
            if (!mDaoMaster.getDatabase().isOpen()) {
                openReadableDb();
            }
            ZExpenseGroupDao zExpenseGroupDao = mDaoSession.getZExpenseGroupDao();
            response = zExpenseGroupDao.queryBuilder().where(ZExpenseGroupDao.Properties.ExpGroupCode.eq(groupCode)).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }

        closeDbConnections();

        return response;
    }

    public ZExpense getZExpenseByCode(int code) {
        ZExpense response = null;
        try {
            openReadableDb();
            if (!mDaoMaster.getDatabase().isOpen()) {
                openReadableDb();
            }
            ZExpenseDao zExpenseDao = mDaoSession.getZExpenseDao();
            response = zExpenseDao.queryBuilder().where(ZExpenseDao.Properties.ETCode.eq(code)).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }

        closeDbConnections();

        return response;
    }

    public List<ZExpense> searchZExpenseByGroupCode(int groupCode, String pText) {
        List<ZExpense> response = null;
        try {
            openReadableDb();
            response = mDaoSession.getZExpenseDao().queryBuilder()
                    .where(ZExpenseDao.Properties.ExpGroupCode.eq(groupCode))
                    .whereOr(ZExpenseDao.Properties.ExpTypeShort.like("%" + pText + "%"),
                            ZExpenseDao.Properties.ExpTypeLong.like("%" + pText + "%")).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return response;
    }

    public boolean insertExpense(Expense expense) {
        try {
            openWritableDb();
            mDaoSession.getExpenseDao().insertOrReplace(expense);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeDbConnections();
        }
    }

    public Totals getTotalsFromFlight(boolean isSelectAir, boolean isSelectSimulator, boolean isSelectDrone) {
        Totals totals = null;
        try {
            openReadableDb();
            String where = "WHERE";
            if (!isSelectAir && !isSelectSimulator && !isSelectDrone) {
                where += "  DeviceCode = 9999";//no data
            } else {
                if (isSelectAir) where += " DeviceCode = 1 OR";
                if (isSelectSimulator) where += " DeviceCode = 2 OR";
                if (isSelectDrone) where += " DeviceCode = 3";
                if (where.endsWith("OR")) where = where.substring(0, where.length() - 3);
                where += " AND DateUTC <= DATE('now','-0 day')";
            }
            //re-use from version on windows phone
            String sqlSelect = "" +
                    //"SELECT Sum(minTotal) AS sumAIRCRAFT," +
                    "SELECT " +
                    "Sum(minPIC) AS sumPIC," +//0
                    "Sum(minCoP) AS sumCoP," +//1
                    "Sum(minDual) AS sumDual," +//2
                    "Sum(minPICus) AS sumPICus," +//3
                    "Sum(minInstr) AS sumInstr," +//4
                    "Sum(MinExam) AS sumExam," +//5
                    "Sum(MinIFR) AS sumIFR," +//6
                    "Sum(MinSFR) AS sumSFR," +//7
                    "Sum(MinIMT) AS sumIMT," +//8
                    "Sum(MinREL) AS sumREL," +//9
                    "Sum(MinNight) AS sumNight," +//10
                    "Sum(MinXC) AS sumXC," +//11
                    "Sum(minU1) AS sumU1," +//12
                    "Sum(minU2) AS sumU2," +//13
                    "Sum(minU3) AS sumU3," +//14
                    "Sum(minU4) AS sumU4," +//15
                    "Sum(CASE Class WHEN 6 THEN minTotal ELSE 0 END) AS sumClass6," +//16
                    "Sum(CASE Class WHEN 1 THEN minTotal ELSE 0 END) AS sumClass1," +//17
                    "Sum(CASE Class WHEN 2 THEN minTotal ELSE 0 END) AS sumClass2," +//18
                    "Sum(CASE Class WHEN 3 THEN minTotal ELSE 0 END) AS sumClass3," +//19
                    "Sum(CASE Class WHEN 4 THEN minTotal ELSE 0 END) AS sumClass4," +//20
                    "Sum(CASE Class WHEN 5 THEN minTotal ELSE 0 END) AS sumClass5," +//21
                    "Sum(CASE WHEN Power IS NULL THEN minTotal ELSE 0 END) AS sumPowerM1," +//22
                    "Sum(CASE Power WHEN 0 THEN minTotal ELSE 0 END) AS sumPower0," +//23
                    "Sum(CASE Power WHEN 1 THEN minTotal ELSE 0 END) AS sumPower1," +//24
                    "Sum(CASE Power WHEN 2 THEN minTotal ELSE 0 END) AS sumPower2," +//25
                    "Sum(CASE Power WHEN 3 THEN minTotal ELSE 0 END) AS sumPower3," +//26
                    "Sum(CASE Power WHEN 4 THEN minTotal ELSE 0 END) AS sumPower4," +//27
                    "Sum(CASE Power WHEN 5 THEN minTotal ELSE 0 END) AS sumPower5," +//28
                    "Sum(CASE Power WHEN 6 THEN minTotal ELSE 0 END) AS sumPower6," +//29
                    "Sum(TODay) AS sumTODay," +//30
                    "Sum(TONight) AS sumTONight," +//31
                    "Sum(LdgDay) AS sumLdgDay," +//32
                    "Sum(LdgNight) AS sumLdgNight," +//33
                    "Sum(Holding) AS sumHolding," +//34
                    "Sum(LiftSW) " +//35
                    "FROM flight LEFT JOIN aircraft ON flight.AircraftCode = aircraft.AircraftCode " +
                    where;

            Cursor cursor = mDaoMaster.getDatabase().rawQuery(sqlSelect, null);
            if (cursor.moveToFirst()) {
                int sumPIC = cursor.getInt(0);
                int sumCoP = cursor.getInt(1);
                int sumDual = cursor.getInt(2);
                int sumPICus = cursor.getInt(3);
                int sumInstr = cursor.getInt(4);
                int sumExam = cursor.getInt(5);
                int sumIFR = cursor.getInt(6);
                int sumSFR = cursor.getInt(7);
                int sumIMT = cursor.getInt(8);
                int sumREL = cursor.getInt(9);
                int sumNight = cursor.getInt(10);
                int sumXC = cursor.getInt(11);
                int sumU1 = cursor.getInt(12);
                int sumU2 = cursor.getInt(13);
                int sumU3 = cursor.getInt(14);
                int sumU4 = cursor.getInt(15);
                int sumClass6 = cursor.getInt(16);
                int sumClass1 = cursor.getInt(17);
                int sumClass2 = cursor.getInt(18);
                int sumClass3 = cursor.getInt(19);
                int sumClass4 = cursor.getInt(20);
                int sumClass5 = cursor.getInt(21);
                int sumPowerM1 = cursor.getInt(22);
                int sumPower0 = cursor.getInt(23);
                int sumPower1 = cursor.getInt(24);
                int sumPower2 = cursor.getInt(25);
                int sumPower3 = cursor.getInt(26);
                int sumPower4 = cursor.getInt(27);
                int sumPower5 = cursor.getInt(28);
                int sumPower6 = cursor.getInt(29);
                int sumTODay = cursor.getInt(30);
                int sumTONight = cursor.getInt(31);
                int sumLdgDay = cursor.getInt(32);
                int sumLdgNight = cursor.getInt(33);
                int sumHolding = cursor.getInt(34);
                int sumLiftSw = cursor.getInt(35);
                totals = new Totals(1, sumPIC, sumCoP, sumDual, sumPICus, sumInstr, sumExam, sumIFR, sumSFR, sumIMT, sumREL, sumNight
                        , sumXC, sumU1, sumU2, sumU3, sumU4, sumClass1, sumClass2, sumClass3, sumClass4, sumClass5, sumClass6, sumPower0, sumPower1
                        , sumPower2, sumPower3, sumPower4, sumPower5, sumPower6, sumTODay, sumTONight, sumLdgDay, sumLdgNight, sumHolding,
                        sumLiftSw, 0, 0, 0);
                totals.setSumPowerM1(sumPowerM1);
            }
            cursor.close();

            boolean isDeductReliefTime = getSetting(MCCPilotLogConst.SETTING_CODE_DEDUCT_RELIEF).getData().equals("1");
            String sumTotalValue = "";
            if (isDeductReliefTime) {
                sumTotalValue = "minTotal - minREL";
            } else {
                sumTotalValue = "minTotal";
            }

            String sqlSelectAir = String.format("SELECT Sum(" + sumTotalValue + ") AS sumAIR " +
                    "FROM flight INNER JOIN aircraft ON flight.AircraftCode = aircraft.AircraftCode " +
                    "WHERE DeviceCode = 1 AND DateUTC <= DATE('now','-0 day')");
            cursor = mDaoMaster.getDatabase().rawQuery(sqlSelectAir, null);
            if (totals != null) {
                if (cursor.moveToFirst()) {
                    totals.setSumACFT(cursor.getInt(0));
                }
            }
            cursor.close();

            String sqlSelectSim = String.format("SELECT Sum(" + sumTotalValue + ") AS sumSIM " +
                    "FROM flight INNER JOIN aircraft ON flight.AircraftCode = aircraft.AircraftCode " +
                    "WHERE DeviceCode = 2 AND DateUTC <= DATE('now','-0 day')");
            cursor = mDaoMaster.getDatabase().rawQuery(sqlSelectSim, null);
            if (totals != null) {
                if (cursor.moveToFirst()) {
                    totals.setSumSIM(cursor.getInt(0));
                }
            }
            cursor.close();

            String sqlSelectDrone = String.format("SELECT Sum(" + sumTotalValue + ") AS sumDRONE " +
                    "FROM flight INNER JOIN aircraft ON flight.AircraftCode = aircraft.AircraftCode " +
                    "WHERE DeviceCode = 3 AND DateUTC <= DATE('now','-0 day')");
            cursor = mDaoMaster.getDatabase().rawQuery(sqlSelectDrone, null);
            if (totals != null) {
                if (cursor.moveToFirst()) {
                    totals.setSumDRONE(cursor.getInt(0));
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return totals;
    }

    public ZLimit getZLimitByCode(int code) {
        ZLimit response = null;
        try {
            openReadableDb();
            if (!mDaoMaster.getDatabase().isOpen()) {
                openReadableDb();
            }
            ZLimitDao zLimitDao = mDaoSession.getZLimitDao();
            response = zLimitDao.queryBuilder().where(ZLimitDao.Properties.LPeriodCode.eq(code)).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }

        closeDbConnections();

        return response;
    }


    public boolean insertLimitRulesSample(int lType) {
        try {
            openWritableDb();
            LimitRules limitRule = new LimitRules();
            limitRule.setLimitCode(Utils.getByteArrayFromGUID(Utils.generateStringGUID()));
            limitRule.setLMinutes(2500);
            limitRule.setLType(lType);
            limitRule.setLPeriodCode(3);
            limitRule.setLZone(1);
            mDaoSession.getLimitRulesDao().insertOrReplace(limitRule);
            limitRule = new LimitRules();
            limitRule.setLimitCode(Utils.getByteArrayFromGUID(Utils.generateStringGUID()));
            limitRule.setLMinutes(14000);
            limitRule.setLType(lType);
            limitRule.setLPeriodCode(14);
            limitRule.setLZone(2);
            mDaoSession.getLimitRulesDao().insertOrReplace(limitRule);
            limitRule = new LimitRules();
            limitRule.setLimitCode(Utils.getByteArrayFromGUID(Utils.generateStringGUID()));
            limitRule.setLMinutes(800);
            limitRule.setLType(lType);
            limitRule.setLPeriodCode(1001);
            limitRule.setLZone(3);
            mDaoSession.getLimitRulesDao().insertOrReplace(limitRule);
            limitRule = new LimitRules();
            limitRule.setLimitCode(Utils.getByteArrayFromGUID(Utils.generateStringGUID()));
            limitRule.setLMinutes(920);
            limitRule.setLType(lType);
            limitRule.setLPeriodCode(1002);
            limitRule.setLZone(4);
            mDaoSession.getLimitRulesDao().insertOrReplace(limitRule);
            limitRule = new LimitRules();
            limitRule.setLimitCode(Utils.getByteArrayFromGUID(Utils.generateStringGUID()));
            limitRule.setLMinutes(25000);
            limitRule.setLType(lType);
            limitRule.setLPeriodCode(1003);
            limitRule.setLZone(5);
            mDaoSession.getLimitRulesDao().insertOrReplace(limitRule);
            limitRule = new LimitRules();
            limitRule.setLimitCode(Utils.getByteArrayFromGUID(Utils.generateStringGUID()));
            limitRule.setLMinutes(80000);
            limitRule.setLType(lType);
            limitRule.setLPeriodCode(1004);
            limitRule.setLZone(6);
            mDaoSession.getLimitRulesDao().insertOrReplace(limitRule);
            limitRule = new LimitRules();
            limitRule.setLimitCode(Utils.getByteArrayFromGUID(Utils.generateStringGUID()));
            limitRule.setLMinutes(20000);
            limitRule.setLType(lType);
            limitRule.setLPeriodCode(1005);
            limitRule.setLZone(6);
            mDaoSession.getLimitRulesDao().insertOrReplace(limitRule);
            limitRule = new LimitRules();
            limitRule.setLimitCode(Utils.getByteArrayFromGUID(Utils.generateStringGUID()));
            limitRule.setLMinutes(40000);
            limitRule.setLType(lType);
            limitRule.setLPeriodCode(1006);
            limitRule.setLZone(6);
            mDaoSession.getLimitRulesDao().insertOrReplace(limitRule);
            limitRule = new LimitRules();
            limitRule.setLimitCode(Utils.getByteArrayFromGUID(Utils.generateStringGUID()));
            limitRule.setLMinutes(80000);
            limitRule.setLType(lType);
            limitRule.setLPeriodCode(1007);
            limitRule.setLZone(7);
            mDaoSession.getLimitRulesDao().insertOrReplace(limitRule);
            limitRule = new LimitRules();
            limitRule.setLimitCode(Utils.getByteArrayFromGUID(Utils.generateStringGUID()));
            limitRule.setLMinutes(80000);
            limitRule.setLType(lType);
            limitRule.setLPeriodCode(1008);
            limitRule.setLZone(7);
            limitRule.setLFrom("2017-08-04");
            limitRule.setLTo("2017-08-13");
            mDaoSession.getLimitRulesDao().insertOrReplace(limitRule);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeDbConnections();
        }
    }


    public List<LimitRules> getAllLimitRules(int homeBaseOffset) {
        openReadableDb();
        List<LimitRules> limitRuleList = null;
        try {
            //openReadableDb();
            if (!mDaoMaster.getDatabase().isOpen()) {
                openReadableDb();
            }
            LimitRulesDao limitRulesDao = mDaoSession.getLimitRulesDao();
            limitRuleList = limitRulesDao.loadAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //List<LimitRules> limitRuleList = getAllLimitRules();
        if (limitRuleList == null) return limitRuleList;
        for (LimitRules limitRule : limitRuleList) {
            int lPeriodCode = limitRule.getLPeriodCode();
            ZLimit zLimit = getZLimitByCode(lPeriodCode);
            String startDate = "";
            String endDate = "DATE('now')";

            if (zLimit != null) {
                limitRule.setlPeriodLong(zLimit.getLPeriodLong());
                switch (zLimit.getLPeriodCode()) {
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                    case 26:
                    case 27:
                    case 28:
                    case 30:
                    case 35:
                    case 42:
                    case 49:
                    case 56:
                    case 60:
                    case 90:
                    case 100:
                    case 180:
                    case 365:
                        startDate = String.format("DATE('now','-%s day')", zLimit.getLPeriodCode());
                        break;
                    case 1001:
                        startDate = "DATE('now','-24 hour')";
                        break;
                    case 1002:
                        startDate = "DATE('now','-1 day')";
                        break;
                    case 1003:
                        startDate = "DATE('now','-1 month')";
                        break;
                    case 1004:
                        startDate = "DATE('now','-12 month')";
                        break;
                    case 1005:
                        startDate = "DATE('now','-3 month')";
                        break;
                    case 1006:
                        startDate = "DATE('now','-6 month')";
                        break;
                    case 1007:
                        startDate = "DATE('now','-1 year')";
                        break;
                    case 1008:
                        startDate = "DATE('" + limitRule.getLFrom() + "')";
                        endDate = "DATE('" + limitRule.getLTo() + "')";
                        //startDate = "DATE('now','-20 hours')";
                        break;

                }
                String sql = "";
                switch (limitRule.getLType()) {
                    case 1:
                    case 2:
                        boolean isDeductReliefTime = getSetting(MCCPilotLogConst.SETTING_CODE_DEDUCT_RELIEF).getData().equals("1");
                        String sumTotalValue = "";
                        if (isDeductReliefTime) {
                            sumTotalValue = "minTotal - minREL";
                        } else {
                            sumTotalValue = "minTotal";
                        }
                        sql = "SELECT SUM(CASE WHEN" +
                                " DateUTC = " + startDate + " THEN (CASE WHEN (DepTimeUTC + " + homeBaseOffset + " + minTotal > 1440) THEN (DepTimeUTC + " + homeBaseOffset + " + " + sumTotalValue + " - 1440)" +
                                " ELSE 0 END)" +
                                " ELSE (CASE WHEN (DateUTC = " + endDate + ") THEN (CASE WHEN (DepTimeUTC + " + homeBaseOffset + " + minTotal > 1440) THEN (1440-DepTimeUTC-" + homeBaseOffset + ") ELSE " + sumTotalValue + " END) ELSE " + sumTotalValue + " END)" +
                                " END)" +
                                " FROM Flight WHERE DateUTC >= " + startDate + " AND DateUTC <= " + endDate;
                        break;
                    case 3:
                        sql = "SELECT SUM (CASE WHEN" +
                                " EventDateUTC = " + startDate + " THEN (CASE WHEN (EventStartUTC + " + homeBaseOffset + " + Duration > 1440) THEN (EventStartUTC + " + homeBaseOffset + " + Duration - 1440)" +
                                " ELSE 0 END)" +
                                " ELSE (CASE WHEN (EventDateUTC = " + endDate + ") THEN (CASE WHEN (EventStartUTC + " + homeBaseOffset + " + Duration > 1440) THEN (1440 - EventStartUTC - " + homeBaseOffset + ") ELSE Duration END) ELSE Duration END)" +
                                " END)" +
                                " FROM Duty WHERE EventDateUTC >=" + startDate + " AND  EventDateUTC <= " + endDate;
                        break;
                }

                if (!mDaoMaster.getDatabase().isOpen()) {
                    openReadableDb();
                }
                Cursor cursor = mDaoMaster.getDatabase().rawQuery(sql, null);
                if (cursor.moveToFirst()) {
                    int sumTotalTime = cursor.getInt(0);
                    limitRule.setSumTotalTime(sumTotalTime);
                    //return sumTotalTime;
                }
                cursor.close();
            }
        }
        closeDbConnections();
        return limitRuleList;
    }

    public boolean insertQualificationRulesSample(boolean isCertificate) {
        try {
            openWritableDb();
            Qualification qualification;
            qualification = new Qualification();
            qualification.setQCode(Utils.getByteArrayFromGUID(Utils.generateStringGUID()));
            qualification.setQTypeCode(5);
            qualification.setValidity(6);
            qualification.setDateIssued("2017-02-15");
            qualification.setDateValid("2017-08-15");
            qualification.setMinimumQty(1);
            qualification.setMinimumPeriod(1);
            qualification.setRefModel("12");
            qualification.setRefExtra(12);
            qualification.setNotifyDays(12);
            qualification.setNotifyComment("canh chim co don");
            mDaoSession.getQualificationDao().insert(qualification);

            qualification = new Qualification();
            qualification.setQCode(Utils.getByteArrayFromGUID(Utils.generateStringGUID()));
            qualification.setQTypeCode(5);
            qualification.setValidity(6);
            qualification.setDateIssued("2017-02-20");
            qualification.setDateValid("2017-08-20");
            qualification.setMinimumQty(1);
            qualification.setMinimumPeriod(1);
            qualification.setRefModel("12");
            qualification.setRefExtra(12);
            qualification.setNotifyDays(12);
            qualification.setNotifyComment("canh chim co don");
            mDaoSession.getQualificationDao().insert(qualification);

            qualification = new Qualification();
            qualification.setQCode(Utils.getByteArrayFromGUID(Utils.generateStringGUID()));
            qualification.setQTypeCode(5);
            qualification.setValidity(999);
            qualification.setDateIssued("2017-03-20");
            qualification.setDateValid("2017-09-20");
            qualification.setMinimumQty(1);
            qualification.setMinimumPeriod(1);
            qualification.setRefModel("12");
            qualification.setRefExtra(12);
            qualification.setNotifyDays(0);
            qualification.setNotifyComment("canh chim co don");
            mDaoSession.getQualificationDao().insert(qualification);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeDbConnections();
        }

    }

    public List<Qualification> getCertificateRules() {
        openReadableDb();
        List<Qualification> certificateRules = new ArrayList<>();
        try {
            //openReadableDb();
            if (!mDaoMaster.getDatabase().isOpen()) {
                openReadableDb();
            }
            String sql = "SELECT QUALIFICATION.*, ZQUALIFICATION.QTYPELONG FROM QUALIFICATION INNER JOIN ZQUALIFICATION ON [QUALIFICATION].QTypeCode = [ZQUALIFICATION].QTypeCode WHERE IsCertificate = 1 ";
            if (!mDaoMaster.getDatabase().isOpen()) {
                openReadableDb();
            }
            Cursor cursor = mDaoMaster.getDatabase().rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    byte[] qCode = cursor.getBlob(0);
                    int qCodeType = cursor.getInt(1);
                    int validity = cursor.getInt(2);
                    String dateIssued = cursor.getString(3);
                    String dValid = cursor.getString(4);
                    int minimumQTy = cursor.getInt(5);
                    int minimumPeriod = cursor.getInt(6);
                    String refModel = cursor.getString(7);
                    byte[] refAirfield = cursor.getBlob(8);
                    int refExtra = cursor.getInt(9);
                    int notifyDay = cursor.getInt(10);
                    String notifyComment = cursor.getString(11);
                    Long recordModify = cursor.getLong(12);
                    int recordUpload = cursor.getInt(13);
                    String qTypeLong = cursor.getString(14);

                    Qualification qualification = new Qualification(qCode, qCodeType, validity, dateIssued, dValid,
                            minimumQTy, minimumPeriod, refModel, refAirfield, refExtra, notifyDay, notifyComment,
                            recordModify, recordUpload == 1 ? true : false);
                    ZQualification zQualification = new ZQualification();
                    zQualification.setQTypeLong(qTypeLong);
                    qualification.setZQualification(zQualification);
                    certificateRules.add(qualification);
                    if (qualification.getNotifyDays() != 0) {
                        if (qualification.getValidity() != 999) {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            Date dateValid = new Date();
                            Date today = new Date();
                            try {
                                dateValid = format.parse(qualification.getDateValid());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (today.after(dateValid)) {
                                //expired
                                qualification.setStatus(Qualification.QualificationStatus.EXPIRED);
                            } else {
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(today);
                                calendar.add(Calendar.DAY_OF_MONTH, qualification.getNotifyDays());
                                if (calendar.getTime().after(dateValid)) {
                                    //upcoming
                                    qualification.setStatus(Qualification.QualificationStatus.UPCOMING);
                                } else {
                                    //valid
                                    qualification.setStatus(Qualification.QualificationStatus.VALID);
                                }
                            }
                        } else {
                            //valid
                            qualification.setStatus(Qualification.QualificationStatus.VALID);
                        }

                    } else {
                        //historical
                        qualification.setStatus(Qualification.QualificationStatus.HISTORICAL);
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return certificateRules;
    }

    public boolean insertProficiencyRulesSample() {
        try {
            openWritableDb();
            Qualification qualification;

            qualification = new Qualification();
            qualification.setQCode(Utils.getByteArrayFromGUID(Utils.generateStringGUID()));
            qualification.setQTypeCode(34);
            qualification.setMinimumQty(5);
            qualification.setMinimumPeriod(80);
            qualification.setRefModel("14");
            qualification.setNotifyDays(0);
            qualification.setNotifyComment("canh chim co don");
            mDaoSession.getQualificationDao().insert(qualification);

            qualification = new Qualification();
            qualification.setQCode(Utils.getByteArrayFromGUID(Utils.generateStringGUID()));
            qualification.setQTypeCode(3);
            qualification.setMinimumQty(3);
            qualification.setMinimumPeriod(90);
            qualification.setRefModel("1");
            qualification.setNotifyDays(12);
            qualification.setNotifyComment("canh chim co don");
            mDaoSession.getQualificationDao().insert(qualification);

            qualification = new Qualification();
            qualification.setQCode(Utils.getByteArrayFromGUID(Utils.generateStringGUID()));
            qualification.setQTypeCode(4);
            qualification.setMinimumQty(3);
            qualification.setMinimumPeriod(90);
            qualification.setRefModel("2");
            qualification.setNotifyDays(12);
            qualification.setNotifyComment("canh chim co don");
            mDaoSession.getQualificationDao().insert(qualification);

            qualification = new Qualification();
            qualification.setQCode(Utils.getByteArrayFromGUID(Utils.generateStringGUID()));
            qualification.setQTypeCode(7);
            qualification.setMinimumQty(3);
            qualification.setMinimumPeriod(90);
            qualification.setRefModel("3");
            qualification.setNotifyDays(12);
            qualification.setNotifyComment("canh chim co don");
            mDaoSession.getQualificationDao().insert(qualification);

            qualification = new Qualification();
            qualification.setQCode(Utils.getByteArrayFromGUID(Utils.generateStringGUID()));
            qualification.setQTypeCode(10);
            qualification.setMinimumQty(3);
            qualification.setMinimumPeriod(90);
            qualification.setRefModel("4");
            qualification.setNotifyDays(12);
            qualification.setNotifyComment("canh chim co don");
            mDaoSession.getQualificationDao().insert(qualification);

            qualification = new Qualification();
            qualification.setQCode(Utils.getByteArrayFromGUID(Utils.generateStringGUID()));
            qualification.setQTypeCode(11);
            qualification.setMinimumQty(3);
            qualification.setMinimumPeriod(90);
            qualification.setRefModel("5");
            qualification.setNotifyDays(12);
            qualification.setNotifyComment("canh chim co don");
            mDaoSession.getQualificationDao().insert(qualification);

            qualification = new Qualification();
            qualification.setQCode(Utils.getByteArrayFromGUID(Utils.generateStringGUID()));
            qualification.setQTypeCode(16);
            qualification.setMinimumQty(3);
            qualification.setMinimumPeriod(90);
            qualification.setRefModel("6");
            qualification.setNotifyDays(12);
            qualification.setRefAirfield(Utils.getByteArrayFromGUID("00000000-0000-0000-0000-000000027899"));//VVTS
            qualification.setNotifyComment("canh chim co don");
            mDaoSession.getQualificationDao().insert(qualification);

            qualification = new Qualification();
            qualification.setQCode(Utils.getByteArrayFromGUID(Utils.generateStringGUID()));
            qualification.setQTypeCode(17);
            qualification.setMinimumQty(3);
            qualification.setMinimumPeriod(90);
            qualification.setRefModel("7");
            qualification.setNotifyDays(12);
            qualification.setNotifyComment("canh chim co don");
            mDaoSession.getQualificationDao().insert(qualification);

            qualification = new Qualification();
            qualification.setQCode(Utils.getByteArrayFromGUID(Utils.generateStringGUID()));
            qualification.setQTypeCode(18);
            qualification.setMinimumQty(3);
            qualification.setMinimumPeriod(90);
            qualification.setRefModel("8");
            qualification.setNotifyDays(12);
            qualification.setNotifyComment("canh chim co don");
            mDaoSession.getQualificationDao().insert(qualification);

            qualification = new Qualification();
            qualification.setQCode(Utils.getByteArrayFromGUID(Utils.generateStringGUID()));
            qualification.setQTypeCode(19);
            qualification.setMinimumQty(3);
            qualification.setMinimumPeriod(180);
            qualification.setRefModel("A320-100");
            qualification.setRefExtra(105);
            qualification.setNotifyDays(12);
            qualification.setNotifyComment("canh chim co don");
            mDaoSession.getQualificationDao().insert(qualification);

            qualification = new Qualification();
            qualification.setQCode(Utils.getByteArrayFromGUID(Utils.generateStringGUID()));
            qualification.setQTypeCode(20);
            qualification.setMinimumQty(4);
            qualification.setMinimumPeriod(90);
            qualification.setRefModel("A320");
            qualification.setRefExtra(101);
            qualification.setNotifyDays(12);
            qualification.setNotifyComment("canh chim co don");
            mDaoSession.getQualificationDao().insert(qualification);

            qualification = new Qualification();
            qualification.setQCode(Utils.getByteArrayFromGUID(Utils.generateStringGUID()));
            qualification.setQTypeCode(25);
            qualification.setMinimumQty(5);
            qualification.setMinimumPeriod(80);
            qualification.setRefModel("9");
            qualification.setNotifyDays(12);
            qualification.setNotifyComment("canh chim co don");
            mDaoSession.getQualificationDao().insert(qualification);

            qualification = new Qualification();
            qualification.setQCode(Utils.getByteArrayFromGUID(Utils.generateStringGUID()));
            qualification.setQTypeCode(26);
            qualification.setMinimumQty(5);
            qualification.setMinimumPeriod(80);
            qualification.setRefModel("10");
            qualification.setNotifyDays(12);
            qualification.setNotifyComment("canh chim co don");
            mDaoSession.getQualificationDao().insert(qualification);

            qualification = new Qualification();
            qualification.setQCode(Utils.getByteArrayFromGUID(Utils.generateStringGUID()));
            qualification.setQTypeCode(27);
            qualification.setMinimumQty(5);
            qualification.setMinimumPeriod(80);
            qualification.setNotifyDays(12);
            qualification.setNotifyComment("canh chim co don");
            mDaoSession.getQualificationDao().insert(qualification);

            qualification = new Qualification();
            qualification.setQCode(Utils.getByteArrayFromGUID(Utils.generateStringGUID()));
            qualification.setQTypeCode(30);
            qualification.setMinimumQty(5);
            qualification.setMinimumPeriod(80);
            qualification.setRefModel("11");
            qualification.setNotifyDays(12);
            qualification.setNotifyComment("canh chim co don");
            mDaoSession.getQualificationDao().insert(qualification);

            qualification = new Qualification();
            qualification.setQCode(Utils.getByteArrayFromGUID(Utils.generateStringGUID()));
            qualification.setQTypeCode(31);
            qualification.setMinimumQty(5);
            qualification.setMinimumPeriod(80);
            qualification.setRefExtra(111);
            qualification.setRefModel("12");
            qualification.setNotifyDays(12);
            qualification.setNotifyComment("canh chim co don");
            mDaoSession.getQualificationDao().insert(qualification);

            qualification = new Qualification();
            qualification.setQCode(Utils.getByteArrayFromGUID(Utils.generateStringGUID()));
            qualification.setQTypeCode(33);
            qualification.setMinimumQty(5);
            qualification.setMinimumPeriod(80);
            qualification.setRefModel("13");
            qualification.setNotifyDays(12);
            qualification.setNotifyComment("canh chim co don");
            mDaoSession.getQualificationDao().insert(qualification);

            qualification = new Qualification();
            qualification.setQCode(Utils.getByteArrayFromGUID(Utils.generateStringGUID()));
            qualification.setQTypeCode(34);
            qualification.setMinimumQty(5);
            qualification.setMinimumPeriod(80);
            qualification.setRefModel("14");
            qualification.setNotifyDays(12);
            qualification.setNotifyComment("canh chim co don");
            mDaoSession.getQualificationDao().insert(qualification);


            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeDbConnections();
        }

    }

    public List<Qualification> getProficiencyRules() {
        openReadableDb();
        List<Qualification> certificateRules = new ArrayList<>();
        try {
            //openReadableDb();
            if (!mDaoMaster.getDatabase().isOpen()) {
                openReadableDb();
            }
            String sql = "SELECT QUALIFICATION.*, ZQUALIFICATION.QTYPELONG, ZQUALIFICATION.MinimumWord1, ZQUALIFICATION.MinimumWord2," +
                    " ZAPPROACH.ApShort, ZLAUNCH.LaunchShort, ZOPERATION.OpsShort, " +
                    " (CASE (SELECT [SettingConfig].[Data] FROM [SettingConfig] WHERE [SettingConfig].[ConfigCode] = 8)" +
                    " WHEN '0'" +
                    " THEN (CASE WHEN (LENGTH([airfield].[AFIcao]) >0 AND [airfield].[AFIcao]!='ZZZZ') THEN [airfield].[AFIcao] ELSE [airfield].[AFIata] END) " +
                    " ELSE (CASE WHEN LENGTH([airfield].[AFIata]) >0 THEN [airfield].[AFIata] ELSE [airfield].[AFIcao] END) END)" +
                    " FROM QUALIFICATION " +
                    " INNER JOIN ZQUALIFICATION ON [QUALIFICATION].QTypeCode = [ZQUALIFICATION].QTypeCode " +
                    " LEFT JOIN ZAPPROACH ON [QUALIFICATION].refExtra = [ZAPPROACH].ApCode " +
                    " LEFT JOIN ZLAUNCH ON [QUALIFICATION].refExtra = [ZLAUNCH].LaunchCode " +
                    " LEFT JOIN ZOPERATION ON [QUALIFICATION].refExtra = [ZOPERATION].OpsCode " +
                    " LEFT JOIN AIRFIELD ON [QUALIFICATION].refAirfield = [AIRFIELD].AFCode " +
                    " WHERE IsCertificate = 0 ";
            if (!mDaoMaster.getDatabase().isOpen()) {
                openReadableDb();
            }
            Cursor cursor = mDaoMaster.getDatabase().rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    byte[] qCode = cursor.getBlob(0);
                    int qCodeType = cursor.getInt(1);
                    int validity = cursor.getInt(2);
                    String dateIssued = cursor.getString(3);
                    String dateValid = cursor.getString(4);
                    int minimumQTy = cursor.getInt(5);
                    int minimumPeriod = cursor.getInt(6);
                    String refModel = cursor.getString(7);
                    byte[] refAirfield = cursor.getBlob(8);
                    int refExtra = cursor.getInt(9);
                    int notifyDay = cursor.getInt(10);
                    String notifyComment = cursor.getString(11);
                    Long recordModify = cursor.getLong(12);
                    int recordUpload = cursor.getInt(13);
                    String qTypeLong = cursor.getString(14);
                    String minimumWord1 = cursor.getString(15);
                    String minimumWord2 = cursor.getString(16);
                    String stringRefAirfield = cursor.getString(20);
                    String refExtraCondition = "";

                    Qualification qualification = new Qualification(qCode, qCodeType, validity, dateIssued, dateValid,
                            minimumQTy, minimumPeriod, refModel, refAirfield, refExtra, notifyDay, notifyComment,
                            recordModify, recordUpload == 1 ? true : false);
                    ZQualification zQualification = new ZQualification();
                    zQualification.setQTypeLong(qTypeLong);
                    zQualification.setMinimumWord1(minimumWord1);
                    zQualification.setMinimumWord2(minimumWord2);
                    qualification.setZQualification(zQualification);
                    qualification.setStringRefAirfield(stringRefAirfield);
                    //qualification.setQTypeLong(qTypeLong);

                    String sqlActualNumberSumOnFlightTable = "SELECT ";
                    String sumQuery = "";
                    switch (qCodeType) {
                        case 3:
                            sumQuery = " SUM(LdgDay + LdgNight) ";
                            break;
                        case 4:
                            //sumQuery = " SUM(CASE WHEN tagApproach LIKE '%421%' THEN 1 ELSE 0 END)";
                            sumQuery = " SUM(CASE WHEN LENGTH('421') <  (LENGTH(tagApproach) - LENGTH(REPLACE(tagApproach,'421',''))) THEN 2 WHEN LENGTH('421') = (LENGTH(tagApproach) - LENGTH(REPLACE(tagApproach,'421',''))) THEN 1 ELSE 0 END)";
                            refExtraCondition = cursor.getString(17);
                            qualification.setRefExtraCondition(refExtraCondition);
                            break;
                        case 7:
                            sumQuery = " SUM(CASE WHEN (minTotal > 0 OR minPic >0 OR minAir>0 OR minPicUs>0" +
                                    " OR minCop>0 OR minDual>0 OR minINSTR>0 OR minEXAM>0 OR minREL>0" +
                                    " OR minNIGHT>0 OR minXC>0 OR minIFR>0 OR minIMT>0 OR minSFR>0 OR minU1>0" +
                                    " OR minU2>0 OR minU3>0 OR minU4>0) THEN 1 ELSE 0 END) ";
                            break;
                        case 10:
                            sumQuery = " SUM(ToDay + ToNight) ";
                            break;
                        case 11:
                            sumQuery = " SUM(LdgNight) ";
                            break;
                        case 16:
                            sumQuery = " SUM(LdgDay + LdgNight) ";
                            break;
                        case 17:
                            sumQuery = " SUM(Holding) ";
                            break;
                        case 18:
                            sumQuery = " SUM(LiftSW) ";
                            break;
                        case 19:
                            //sumQuery = " SUM(CASE WHEN tagApproach LIKE '%" + refExtra + "%' THEN 1 ELSE 0 END)";
                            sumQuery = " SUM(CASE WHEN LENGTH('" + refExtra + "') <  (LENGTH(tagApproach) - LENGTH(REPLACE(tagApproach,'" + refExtra + "','')))" +
                                    " THEN 2 WHEN LENGTH('" + refExtra + "') = (LENGTH(tagApproach) - LENGTH(REPLACE(tagApproach,'" + refExtra + "','')))" +
                                    " THEN 1 ELSE 0 END)";
                            refExtraCondition = cursor.getString(17);
                            qualification.setRefExtraCondition(refExtraCondition);
                            break;
                        case 20:
                            //sumQuery = " SUM(CASE WHEN tagLaunch LIKE '%" + refExtra + "%' THEN 1 ELSE 0 END)";
                            sumQuery = " SUM(CASE WHEN LENGTH('104') < (LENGTH(tagLaunch) - LENGTH(REPLACE(tagLaunch,'104','')))" +
                                    " THEN (CASE WHEN LENGTH('" + refExtra + "') <  (LENGTH(tagLaunch) - LENGTH(REPLACE(tagLaunch,'" + refExtra + "',''))) " +
                                    " THEN 2 WHEN LENGTH('" + refExtra + "') = (LENGTH(tagLaunch) -LENGTH(REPLACE(tagLaunch,'" + refExtra + "','')))" +
                                    " THEN 1 ELSE 0 END) ELSE (CASE WHEN tagLaunch LIKE '%" + refExtra + "%' THEN 1 ELSE 0 END)  END) ";
                            refExtraCondition = cursor.getString(18);
                            qualification.setRefExtraCondition(refExtraCondition);
                            break;
                        case 25:
                            sumQuery = " SUM(userNum) ";
                            break;
                        case 26:
                            sumQuery = " SUM(ToNight) ";
                            break;
                        case 27:
                            sumQuery = " SUM(1) ";
                            break;
                        case 30:
                            sumQuery = " SUM(1) ";
                            break;
                        case 31:
                            //sumQuery = " SUM(CASE WHEN tagOps LIKE '%" + refExtra + "%' THEN 1 ELSE 0 END)";
                            sumQuery = " SUM(CASE WHEN 0 < LENGTH(tagOps)- (LENGTH(tagOps) - LENGTH(REPLACE(REPLACE(tagOps,'" + refExtra + "',''),'|:',''))) " +
                                    " THEN (CASE WHEN LENGTH('" + refExtra + "') <  (LENGTH(tagOps) - LENGTH(REPLACE(tagOps,'" + refExtra + "',''))) " +
                                    " THEN 2 WHEN LENGTH('" + refExtra + "') = (LENGTH(tagOps) -LENGTH(REPLACE(tagOps,'" + refExtra + "','')))" +
                                    " THEN 1 ELSE 0 END) ELSE (CASE WHEN tagOps LIKE '%" + refExtra + "%' THEN 1 ELSE 0 END)  END) ";
                            refExtraCondition = cursor.getString(19);
                            qualification.setRefExtraCondition(refExtraCondition);
                            break;
                        case 33:
                            sumQuery = " SUM(CASE WHEN [aircraft].TailWheel = 1 THEN (LdgDay + LdgNight) ELSE 0 END) ";
                            break;
                        case 34:
                            sumQuery = " SUM(CASE WHEN UserBool = 1 THEN 1 ELSE 0 END) ";
                            break;
                    }
                    sqlActualNumberSumOnFlightTable += sumQuery + " FROM FLIGHT INNER JOIN AIRCRAFT ON FLIGHT.AircraftCode = AIRCRAFT.AircraftCode";
                    String sqlWhereRefModel = " WHERE";
                    if (!TextUtils.isEmpty(refModel)) {
                        switch (refModel) {
                            case "1":
                                sqlWhereRefModel += " Aircraft.Class = 1 ";
                                break;
                            case "2":
                                sqlWhereRefModel += " Aircraft.Class = 2 ";
                                break;
                            case "3":
                                sqlWhereRefModel += " Aircraft.Class = 3 ";
                                break;
                            case "4":
                                sqlWhereRefModel += " Aircraft.Class = 4 ";
                                break;
                            case "5":
                                sqlWhereRefModel += " Aircraft.Class = 5 ";
                                break;
                            case "6":
                                sqlWhereRefModel += " (Aircraft.Class = 4 AND Aircraft.Power > 0 AND Aircraft.Power < 4) ";
                                break;
                            case "7":
                                sqlWhereRefModel += " (Aircraft.Class = 4 AND Aircraft.Power > 3) ";
                                break;
                            case "8":
                                sqlWhereRefModel += " (Aircraft.Class = 5 AND Aircraft.Power > 0 AND Aircraft.Power < 4) ";
                                break;
                            case "9":
                                sqlWhereRefModel += " (Aircraft.Class = 5 AND Aircraft.Power > 3) ";
                                break;
                            case "10":
                                sqlWhereRefModel += " (Aircraft.Class = 5 AND Aircraft.Sea = 0) ";
                                break;
                            case "11":
                                sqlWhereRefModel += " (Aircraft.Class = 5 AND Aircraft.Sea = 1) ";
                                break;
                            case "12":
                                sqlWhereRefModel += " Aircraft.DeviceCode = 1 ";
                                break;
                            case "13":
                                sqlWhereRefModel += " Aircraft.DeviceCode = 2 ";
                                break;
                            case "14":
                                sqlWhereRefModel += " Aircraft.DeviceCode = 3 ";
                                break;
                            default:
                                sqlWhereRefModel += " (UPPER(Aircraft.Model) = UPPER('" + refModel + "') OR UPPER(Aircraft.Model || '-' || Aircraft.SubModel) = UPPER('" + refModel + "')) ";
                                break;
                        }
                    } else {
                        sqlWhereRefModel = "";
                    }
                    String sqlWhereRefAirfield = "";
                    if (!TextUtils.isEmpty(sqlWhereRefModel)) {
                        sqlWhereRefAirfield = " AND ";
                    } else {
                        sqlWhereRefAirfield = " WHERE ";
                    }
                    if (refAirfield != null) {
                        if (qCodeType == 16) {
                            sqlWhereRefAirfield += " flight.ArrCode = " + Utils.escapeBlobArgument(refAirfield);
                        } else {
                            sqlWhereRefAirfield += " (flight.DepCode = " + Utils.escapeBlobArgument(refAirfield) +
                                    "OR flight.ArrCode = " + Utils.escapeBlobArgument(refAirfield) + ")";
                        }

                    } else {
                        sqlWhereRefAirfield = "";
                    }
                    sqlActualNumberSumOnFlightTable += sqlWhereRefModel;
                    sqlActualNumberSumOnFlightTable += sqlWhereRefAirfield;
                    String sqlWhereFlightDate = "";
                    if (TextUtils.isEmpty(sqlWhereRefModel) && TextUtils.isEmpty(sqlWhereRefAirfield)) {
                        sqlWhereFlightDate = " WHERE ";
                    } else {
                        sqlWhereFlightDate = " AND ";
                    }
                    String endDate = "DATE('now')";
                    String startDate = "DATE('now','-" + qualification.getMinimumPeriod() + " day')";
                    sqlWhereFlightDate += " Flight.DateUTC >= " + startDate + " AND Flight.DateUTC <= " + endDate;
                    String sqlFinish = sqlActualNumberSumOnFlightTable + sqlWhereFlightDate;

                    Cursor cursorActualQty = mDaoMaster.getDatabase().rawQuery(sqlFinish, null);
                    if (cursorActualQty.moveToFirst()) {
                        int actualQty = cursorActualQty.getInt(0);
                        qualification.setActualQty(actualQty);
                        //check upcoming
                        if (qualification.getNotifyDays() != 0) {
                            if (actualQty >= minimumQTy) {
                                while (actualQty >= minimumQTy) {
                                    minimumPeriod--;
                                    if (TextUtils.isEmpty(sqlWhereRefModel) && TextUtils.isEmpty(sqlWhereRefAirfield)) {
                                        sqlWhereFlightDate = " WHERE ";
                                    } else {
                                        sqlWhereFlightDate = " AND ";
                                    }
                                    startDate = "DATE('now','-" + minimumPeriod + " day')";
                                    sqlWhereFlightDate += " Flight.DateUTC >= " + startDate + " AND Flight.DateUTC <= " + endDate;
                                    sqlFinish = sqlActualNumberSumOnFlightTable + sqlWhereFlightDate;
                                    Cursor cursorCheckUpComing = mDaoMaster.getDatabase().rawQuery(sqlFinish, null);
                                    if (cursorCheckUpComing.moveToFirst()) {
                                        actualQty = cursorCheckUpComing.getInt(0);
                                    }
                                    cursorCheckUpComing.close();
                                }
                                Calendar calendarUpComing = Calendar.getInstance();
                                calendarUpComing.add(Calendar.DAY_OF_MONTH, -(minimumPeriod + 1));
                                calendarUpComing.add(Calendar.DAY_OF_MONTH, qualification.getMinimumPeriod());
                                Calendar calendarNotifyDay = Calendar.getInstance();
                                calendarNotifyDay.add(Calendar.DAY_OF_MONTH, qualification.getNotifyDays());
                                if (calendarUpComing.after(calendarNotifyDay)) {
                                    //valid
                                    qualification.setStatus(Qualification.QualificationStatus.VALID);
                                } else {
                                    //upcoming
                                    Calendar calendar = Calendar.getInstance();
                                    long diff = calendarUpComing.getTimeInMillis() - calendar.getTimeInMillis();
                                    long days = diff / (24 * 60 * 60 * 1000);
                                    qualification.setProficiencyUpComingDay((int) days);
                                    qualification.setStatus(Qualification.QualificationStatus.UPCOMING);
                                }
                            } else {
                                //expired
                                qualification.setStatus(Qualification.QualificationStatus.EXPIRED);
                            }
                        } else {
                            qualification.setStatus(Qualification.QualificationStatus.HISTORICAL);
                        }
                    }

                    cursorActualQty.close();
                    certificateRules.add(qualification);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return certificateRules;
    }


    ///////////////////////////////////////////////////////////////////
/*    public boolean updateAFCode() {
        try {
            openWritableDb();
            String preAFCode = "00000000-0000-0000-0000-0000000";

            for (int i = 0; i <= 40000; i++) {
                String valueOfi = String.valueOf(i);
                while (valueOfi.length() < 5) {
                    valueOfi = "0" + valueOfi;
                }
                String afCode = preAFCode + valueOfi;
                //Log.d("afCode",afCode);
                String afCodeNew = afCode.replace("-", "");
                mDatabase.execSQL("update airfield set afcode =X'" + afCodeNew + "' where afcode ='" + afCode + "'");
                Log.d("update afCode", afCodeNew);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeDbConnections();
        }
    }*/

    public boolean updateAFCode() {
        try {
            openWritableDb();
            List<Airfield> airfieldList = new ArrayList<>();
            airfieldList = mDaoSession.getAirfieldDao().queryBuilder().list();
          /*  for (Airfield airfield :
                    airfieldList) {
                //String strAFCode = Utils.getGUIDFromByteArray(airfield.getAFCode());
                byte[] afCode = Utils.getByteArrayFromGUID(Utils.escapeBlobArgument(airfield.getAFCode()));
                airfield.setAFCode(afCode);
                insertOrUpdateAirfield(airfield);
                Log.d("update afCode", Utils.getGUIDFromByteArray(airfield.getAFCode()));
            }*/
            String preAFCode = "00000000-0000-0000-0000-0000000";

            for (int i = 0; i <= 40000; i++) {
                String valueOfi = String.valueOf(i);
                while (valueOfi.length() < 5) {
                    valueOfi = "0" + valueOfi;
                }
                String afCode = preAFCode + valueOfi;
                //Log.d("afCode",afCode);
                String afCodeNew = afCode.replace("-", "");
                mDatabase.execSQL("update airfield set afcode =X'" + afCodeNew + "' where afcode ='" + afCode + "'");
                Log.d("update afCode", afCodeNew);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeDbConnections();
        }
    }

    public boolean updatePilotCode() {
        try {
            openWritableDb();
            String prePilotCode = "00000000-0000-0000-0000-0000000";

            for (int i = 0; i <= 2; i++) {
                String valueOfi = String.valueOf(i);
                while (valueOfi.length() < 5) {
                    valueOfi = "0" + valueOfi;
                }
                String pilotCode = prePilotCode + valueOfi;
                //Log.d("afCode",afCode);
                String pilotCodeNew = pilotCode.replace("-", "");
                mDatabase.execSQL("update pilot set pilotCode =X'" + pilotCodeNew + "' where pilotcode ='" + pilotCode + "'");
                Log.d("update afCode", pilotCodeNew);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeDbConnections();
        }
    }

    //
    public boolean insertOrUpdateAirfield(Airfield airfield) {
        try {
            openWritableDb();
            mDaoSession.getAirfieldDao().insertOrReplace(airfield);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeDbConnections();
        }
    }

    public boolean insertOrUpdateAirfieldForSync(Airfield airfield) {
        try {
            openReadableDb();
            Airfield localAirfield = null;
            try {
                String conditionString = AirfieldDao.Properties.AFCode.columnName + '=' + Utils.escapeBlobArgument(airfield.getAFCode());
                WhereCondition condition = new WhereCondition.StringCondition(conditionString);
                localAirfield = mDaoSession.getAirfieldDao().queryBuilder().where(condition).unique();
            } catch (Exception e) {
                e.printStackTrace();
            }
            openWritableDb();
            if (localAirfield != null) {
                if (localAirfield.getRecord_Modified() < airfield.getRecord_Modified()) {
                    airfield.setRecord_Upload(false);
                    mDaoSession.getAirfieldDao().insertOrReplace(airfield);
                    return true;
                }
                return true;
            } else {
                airfield.setRecord_Upload(false);
                mDaoSession.getAirfieldDao().insertOrReplace(airfield);
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeDbConnections();
        }
    }

    public boolean insertOrUpdateDataForSync(List<Airfield> airfields, List<Aircraft> aircrafts,
                                             List<Pilot> pilots, List<Expense> expenses, List<AllowanceRules> allowanceRules,
                                             List<ValidationRules> validationRules, List<LimitRules> limitRules,
                                             List<MyQueryBuild> myQueryBuilds, List<MyQuery> myQueries, List<WeatherAF> weatherAFS,
                                             List<Weather> weathers, List<BackupDB> backupDBS, List<Flight> flights, List<Duty> duties,
                                             List<ImagePic> imagePics) {
        try {
            openReadableDb();
            if(airfields!=null && airfields.size()>0)
                for (int i=0; i< airfields.size(); i++) {
                Airfield localAirfield;
                try {
                    airfields.get(i).setRecord_Upload(false);
                    String conditionString = AirfieldDao.Properties.AFCode.columnName + '=' + Utils.escapeBlobArgument(airfields.get(i).getAFCode());
                    WhereCondition condition = new WhereCondition.StringCondition(conditionString);
                    localAirfield = mDaoSession.getAirfieldDao().queryBuilder().where(condition).unique();
                    if (localAirfield != null && localAirfield.getRecord_Modified() > airfields.get(i).getRecord_Modified()) {
                        airfields.remove(airfields.get(i));//don't update airfield that local record modified more than API record modified
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
            if(aircrafts!=null && aircrafts.size()>0)
                for (int i=0; i< aircrafts.size(); i++) {
                Aircraft local;
                try {
                    aircrafts.get(i).setRecord_Upload(false);
                    String conditionString = AircraftDao.Properties.AircraftCode.columnName + '=' + Utils.escapeBlobArgument(aircrafts.get(i).getAircraftCode());
                    WhereCondition condition = new WhereCondition.StringCondition(conditionString);
                    local = mDaoSession.getAircraftDao().queryBuilder().where(condition).unique();
                    if (local != null && local.getRecord_Modified() > aircrafts.get(i).getRecord_Modified()) {
                        aircrafts.remove(aircrafts.get(i));//don't update airfield that local record modified more than API record modified
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
            if(pilots!=null && pilots.size()>0)
                for (int i=0; i< pilots.size(); i++) {
                Pilot local;
                try {
                    pilots.get(i).setRecord_Upload(false);
                    String conditionString = PilotDao.Properties.PilotCode.columnName + '=' + Utils.escapeBlobArgument(pilots.get(i).getPilotCode());
                    WhereCondition condition = new WhereCondition.StringCondition(conditionString);
                    local = mDaoSession.getPilotDao().queryBuilder().where(condition).unique();
                    if (local != null && local.getRecord_Modified() > pilots.get(i).getRecord_Modified()) {
                        pilots.remove(pilots.get(i));//don't update airfield that local record modified more than API record modified
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
            if(expenses!=null && expenses.size()>0)
                for (int i=0; i< expenses.size(); i++) {
                Expense local;
                try {
                    expenses.get(i).setRecord_Upload(false);
                    String conditionString = ExpenseDao.Properties.ExpCode.columnName + '=' + Utils.escapeBlobArgument(expenses.get(i).getExpCode());
                    WhereCondition condition = new WhereCondition.StringCondition(conditionString);
                    local = mDaoSession.getExpenseDao().queryBuilder().where(condition).unique();
                    if (local != null && local.getRecord_Modified() > expenses.get(i).getRecord_Modified()) {
                        expenses.remove(expenses.get(i));//don't update airfield that local record modified more than API record modified
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
            if(allowanceRules!=null && allowanceRules.size()>0)
                for (int i=0; i< allowanceRules.size(); i++) {
                AllowanceRules local;
                try {
                    allowanceRules.get(i).setRecord_Upload(false);
                    String conditionString = AllowanceRulesDao.Properties.ARCode.columnName + '=' + Utils.escapeBlobArgument(allowanceRules.get(i).getARCode());
                    WhereCondition condition = new WhereCondition.StringCondition(conditionString);
                    local = mDaoSession.getAllowanceRulesDao().queryBuilder().where(condition).unique();
                    if (local != null && local.getRecord_Modified() > allowanceRules.get(i).getRecord_Modified()) {
                        allowanceRules.remove(allowanceRules.get(i));//don't update airfield that local record modified more than API record modified
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
            if(validationRules!=null && validationRules.size()>0)
                for (int i=0; i< validationRules.size(); i++) {
                ValidationRules local;
                try {
                    validationRules.get(i).setRecord_Upload(false);
                    String conditionString = ValidationRulesDao.Properties.VCode.columnName + '=' + Utils.escapeBlobArgument(validationRules.get(i).getVCode());
                    WhereCondition condition = new WhereCondition.StringCondition(conditionString);
                    local = mDaoSession.getValidationRulesDao().queryBuilder().where(condition).unique();
                    if (local != null && local.getRecord_Modified() > validationRules.get(i).getRecord_Modified()) {
                        validationRules.remove(validationRules.get(i));//don't update airfield that local record modified more than API record modified
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
            if(limitRules!=null && limitRules.size()>0)
                for (int i=0; i< limitRules.size(); i++) {
                LimitRules local;
                try {
                    limitRules.get(i).setRecord_Upload(false);
                    String conditionString = LimitRulesDao.Properties.LimitCode.columnName + '=' + Utils.escapeBlobArgument(limitRules.get(i).getLimitCode());
                    WhereCondition condition = new WhereCondition.StringCondition(conditionString);
                    local = mDaoSession.getLimitRulesDao().queryBuilder().where(condition).unique();
                    if (local != null && local.getRecord_Modified() > limitRules.get(i).getRecord_Modified()) {
                        limitRules.remove(limitRules.get(i));//don't update airfield that local record modified more than API record modified
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
            if(myQueryBuilds!=null && myQueryBuilds.size()>0)
                for (int i=0; i< myQueryBuilds.size(); i++) {
                MyQueryBuild local;
                try {
                    myQueryBuilds.get(i).setRecord_Upload(false);
                    String conditionString = MyQueryBuildDao.Properties.MQBCode.columnName + '=' + Utils.escapeBlobArgument(myQueryBuilds.get(i).getMQBCode());
                    WhereCondition condition = new WhereCondition.StringCondition(conditionString);
                    local = mDaoSession.getMyQueryBuildDao().queryBuilder().where(condition).unique();
                    if (local != null && local.getRecord_Modified() > myQueryBuilds.get(i).getRecord_Modified()) {
                        myQueryBuilds.remove(myQueryBuilds.get(i));//don't update airfield that local record modified more than API record modified
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
            if(myQueries!=null && myQueries.size()>0)
                for (int i=0; i< myQueries.size(); i++) {
                MyQuery local;
                try {
                    myQueries.get(i).setRecord_Upload(false);
                    String conditionString = MyQueryDao.Properties.MQCode.columnName + '=' + Utils.escapeBlobArgument(myQueries.get(i).getMQCode());
                    WhereCondition condition = new WhereCondition.StringCondition(conditionString);
                    local = mDaoSession.getMyQueryDao().queryBuilder().where(condition).unique();
                    if (local != null && local.getRecord_Modified() > myQueries.get(i).getRecord_Modified()) {
                        myQueries.remove(myQueries.get(i));//don't update airfield that local record modified more than API record modified
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
            if(weatherAFS!=null && weatherAFS.size()>0)
                for (int i=0; i< weatherAFS.size(); i++) {
                WeatherAF local;
                try {
                    weatherAFS.get(i).setRecord_Upload(false);
                    String conditionString = WeatherAFDao.Properties.WxAFCode.columnName + '=' + Utils.escapeBlobArgument(weatherAFS.get(i).getWxAFCode());
                    WhereCondition condition = new WhereCondition.StringCondition(conditionString);
                    local = mDaoSession.getWeatherAFDao().queryBuilder().where(condition).unique();
                    if (local != null && local.getRecord_Modified() > weatherAFS.get(i).getRecord_Modified()) {
                        weatherAFS.remove(weatherAFS.get(i));//don't update airfield that local record modified more than API record modified
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
            if(weathers!=null && weathers.size()>0)
                for (int i=0; i< weathers.size(); i++) {
                Weather local;
                try {
                    weathers.get(i).setRecord_Upload(false);
                    String conditionString = WeatherDao.Properties.WxCode.columnName + '=' + Utils.escapeBlobArgument(weathers.get(i).getWxCode());
                    WhereCondition condition = new WhereCondition.StringCondition(conditionString);
                    local = mDaoSession.getWeatherDao().queryBuilder().where(condition).unique();
                    if (local != null && local.getRecord_Modified() > weathers.get(i).getRecord_Modified()) {
                        weathers.remove(weathers.get(i));//don't update airfield that local record modified more than API record modified
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
            if(backupDBS!=null && backupDBS.size()>0)
                for (int i=0; i< backupDBS.size(); i++) {
                BackupDB local;
                try {
                    backupDBS.get(i).setRecord_Upload(false);
                    String conditionString = BackupDBDao.Properties.BackupCode.columnName + '=' + Utils.escapeBlobArgument(backupDBS.get(i).getBackupCode());
                    WhereCondition condition = new WhereCondition.StringCondition(conditionString);
                    local = mDaoSession.getBackupDBDao().queryBuilder().where(condition).unique();
                    if (local != null && local.getRecord_Modified() > backupDBS.get(i).getRecord_Modified()) {
                        backupDBS.remove(backupDBS.get(i));//don't update airfield that local record modified more than API record modified
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
            if(flights!=null && flights.size()>0)
            for (int i=0; i< flights.size(); i++) {
                Flight local;
                try {
                    flights.get(i).setRecord_Upload(false);
                    String conditionString = FlightDao.Properties.FlightCode.columnName + '=' + Utils.escapeBlobArgument(flights.get(i).getFlightCode());
                    WhereCondition condition = new WhereCondition.StringCondition(conditionString);
                    local = mDaoSession.getFlightDao().queryBuilder().where(condition).unique();
                    if (local != null && local.getRecord_Modified() > flights.get(i).getRecord_Modified()) {
                        flights.remove(flights.get(i));//don't update airfield that local record modified more than API record modified
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
            if(duties!=null && duties.size()>0)
                for (int i=0; i< duties.size(); i++) {
                Duty local;
                try {
                    duties.get(i).setRecord_Upload(false);
                    String conditionString = DutyDao.Properties.DutyCode.columnName + '=' + Utils.escapeBlobArgument(duties.get(i).getDutyCode());
                    WhereCondition condition = new WhereCondition.StringCondition(conditionString);
                    local = mDaoSession.getDutyDao().queryBuilder().where(condition).unique();
                    if (local != null && local.getRecord_Modified() > duties.get(i).getRecord_Modified()) {
                        duties.remove(duties.get(i));//don't update airfield that local record modified more than API record modified
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
            if(imagePics!=null && imagePics.size()>0)
                for (int i=0; i< imagePics.size(); i++) {
                ImagePic local;
                try {
                    imagePics.get(i).setRecord_Upload(false);
                    String conditionString = ImagePicDao.Properties.ImgCode.columnName + '=' + Utils.escapeBlobArgument(imagePics.get(i).getImgCode());
                    WhereCondition condition = new WhereCondition.StringCondition(conditionString);
                    local = mDaoSession.getImagePicDao().queryBuilder().where(condition).unique();
                    if (local != null && local.getRecord_Modified() > imagePics.get(i).getRecord_Modified()) {
                        imagePics.remove(imagePics.get(i));//don't update airfield that local record modified more than API record modified
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

            openWritableDb();
            mDaoSession.getAirfieldDao().insertOrReplaceInTx(airfields);
            mDaoSession.getAircraftDao().insertOrReplaceInTx(aircrafts);
            mDaoSession.getPilotDao().insertOrReplaceInTx(pilots);
            mDaoSession.getExpenseDao().insertOrReplaceInTx(expenses);
            mDaoSession.getAllowanceRulesDao().insertOrReplaceInTx(allowanceRules);
            mDaoSession.getLimitRulesDao().insertOrReplaceInTx(limitRules);
            //mDaoSession.getValidationRulesDao().insertOrReplaceInTx(validationRules);
            mDaoSession.getMyQueryDao().insertOrReplaceInTx(myQueries);
            mDaoSession.getMyQueryBuildDao().insertOrReplaceInTx(myQueryBuilds);
            mDaoSession.getWeatherAFDao().insertOrReplaceInTx(weatherAFS);
            mDaoSession.getWeatherDao().insertOrReplaceInTx(weathers);
            mDaoSession.getBackupDBDao().insertOrReplaceInTx(backupDBS);
            mDaoSession.getFlightDao().insertOrReplaceInTx(flights);
            //mDaoSession.getFlightDao().insertOrReplace(flights.get(0));
            mDaoSession.getDutyDao().insertOrReplaceInTx(duties);
            mDaoSession.getImagePicDao().insertOrReplaceInTx(imagePics);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeDbConnections();
        }
        return true;
    }



    public boolean deleteAllAirfields() {
        boolean result = true;
        try {
            openWritableDb();
            mDatabase.beginTransaction();
            AirfieldDao airfieldDao = mDaoSession.getAirfieldDao();
            List<Airfield> airfieldList = airfieldDao.queryBuilder()
                    .where(AirfieldDao.Properties.ShowList.eq(true)).list();
            airfieldDao.queryBuilder().where(AirfieldDao.Properties.ShowList.eq(true)).buildDelete().executeDeleteWithoutDetachingEntities();
            List<RecordDelete> recordDeleteList = new ArrayList<>();
            for (int i = 0; i < airfieldList.size(); i++) {
                RecordDelete recordDelete = new RecordDelete(AirfieldDao.TABLENAME, airfieldList.get(i).getAFCode());
                recordDeleteList.add(recordDelete);
            }
            insertDeleteRecords(recordDeleteList);
            mDatabase.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            mDatabase.endTransaction();
            closeDbConnections();
        }
        return result;
    }

    public boolean isAirfieldUsedInFlight(byte[] pAirfieldCode) {
        long count = 0;

        openReadableDb();
        try {
            FlightDao flightDao = mDaoSession.getFlightDao();
            String conditionString = FlightDao.Properties.ArrCode.columnName + '=' + Utils.escapeBlobArgument(pAirfieldCode);
            WhereCondition condition1 = new WhereCondition.StringCondition(conditionString);
            String conditionString2 = FlightDao.Properties.DepCode.columnName + '=' + Utils.escapeBlobArgument(pAirfieldCode);
            WhereCondition condition2 = new WhereCondition.StringCondition(conditionString2);
            count = flightDao.queryBuilder().whereOr(condition1, condition2).count();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return count > 0;
    }

    public boolean deleteAirfield(byte[] airfieldCode) {
        boolean result = true;
        try {
            openWritableDb();
            mDatabase.beginTransaction();
            String conditionString = AirfieldDao.Properties.AFCode.columnName + '=' + Utils.escapeBlobArgument(airfieldCode);
            WhereCondition condition = new WhereCondition.StringCondition(conditionString);
            mDaoSession.getAirfieldDao().queryBuilder().where(condition).buildDelete().executeDeleteWithoutDetachingEntities();
            RecordDelete recordDelete = new RecordDelete(AirfieldDao.TABLENAME, airfieldCode);
            insertDeleteRecord(recordDelete);
            mDatabase.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            mDatabase.endTransaction();
            closeDbConnections();
        }
        return result;
    }


    // Timezone
    public ZTimeZone getZTimezone(Integer timezoneCode) {
        ZTimeZone response = null;
        try {
            openReadableDb();
            response = mDaoSession.getZTimeZoneDao().queryBuilder().where(ZTimeZoneDao.Properties.TZCode.eq(timezoneCode)).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return response;
    }

    public List<ZTimeZone> getAllZTimezone() {
        List<ZTimeZone> list = null;
        try {
            openReadableDb();
            list = mDaoSession.getZTimeZoneDao().queryBuilder().orderAsc(ZTimeZoneDao.Properties.ZoneShort).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return list;
    }

    public List<ZTimeZone> searchZTimezone(String pText) {
        List<ZTimeZone> response = null;
        try {
            openReadableDb();
            response = mDaoSession.getZTimeZoneDao().queryBuilder()
                    .whereOr(ZTimeZoneDao.Properties.TimeZone.like("%" + pText + "%"),
                            ZTimeZoneDao.Properties.ZoneShort.like("%" + pText + "%"),
                            ZTimeZoneDao.Properties.ZoneLong.like("%" + pText + "%")).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return response;
    }

    //Country
    public ZCountry getZCountry(Integer countryCode) {
        ZCountry response = null;
        try {
            openReadableDb();
            ZCountryDao countryDao = mDaoSession.getZCountryDao();
            response = countryDao.queryBuilder().where(ZCountryDao.Properties.CountryCode.eq(countryCode)).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return response;
    }

    public List<ZCountry> getAllZCountry() {
        List<ZCountry> list = null;
        try {
            openReadableDb();
            list = mDaoSession.getZCountryDao().queryBuilder().orderAsc(ZCountryDao.Properties.CountryName).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return list;
    }

    /*weather local*/
    public boolean saveWeather(WeatherLocal pNewWeather) {
        WeatherLocal mWeather;
        try {
            openWritableDb();
            WeatherLocalDao mWeatherDao = mDaoSession.getWeatherLocalDao();
            mWeather = mWeatherDao.queryBuilder().whereOr(WeatherLocalDao.Properties.AFIata.eq(pNewWeather.getAFIata()),
                    WeatherLocalDao.Properties.AFIcao.eq(pNewWeather.getAFIcao())).unique();
            if (mWeather != null) {
                mWeatherDao.update(pNewWeather);
                mWeatherDao.refresh(pNewWeather);
            } else {
                mWeatherDao.insert(pNewWeather);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        closeDbConnections();

        return true;
    }

    public List<WeatherLocal> getAllWeather() {
        List<WeatherLocal> mWeatherList = null;
        try {
            openReadableDb();
            mWeatherList = mDaoSession.getWeatherLocalDao().loadAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return mWeatherList;
    }

    public void deleteWeatherByObject(WeatherLocal pWeather) {
        try {
            openWritableDb();
            mDaoSession.getWeatherLocalDao().delete(pWeather);
        } catch (Exception e) {
            e.printStackTrace();
        }

        closeDbConnections();
    }

    public void deleteAllWeather() {
        try {
            openWritableDb();
            mDaoSession.getWeatherLocalDao().deleteAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        closeDbConnections();
    }

    public HashMap<String, String> getAllTodayFlightAirfield() {
        HashMap<String, String> mAirfieldList = new HashMap<>();
        try {
            openReadableDb();
            Cursor c = mDaoMaster.getDatabase().rawQuery("SELECT airfield.AFICao, airfield.AFIata FROM airfield INNER JOIN flight on (airfield.AFCode = flight.DepCode " +
                    "or airfield.AFCode = flight.ArrCode)" + "WHERE flight.DateUTC = date('now')", null);
            while (c.moveToNext()) {
                mAirfieldList.put(c.getString(0), c.getString(1));
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return mAirfieldList;
    }

    public WeatherLocal getWeather(String icao) {
        WeatherLocal mWeather = null;
        try {
            if (!mDatabase.isOpen())
                openReadableDb();
            mWeather = mDaoSession.getWeatherLocalDao().queryBuilder().where(WeatherLocalDao.Properties.AFIcao.eq(icao)).unique();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDbConnections();
        }
        return mWeather;
    }

    public boolean insertDeleteRecords(List<RecordDelete> recordDeletes) {
        try {
            if (!mDatabase.isOpen())
                openWritableDb();
            mDaoSession.getRecordDeleteDao().insertInTx(recordDeletes);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertDeleteRecord(RecordDelete recordDeletes) {
        try {
            mDaoSession.getRecordDeleteDao().insert(recordDeletes);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void removeDeleteRecord(byte[] recordCode){
        try {
            openWritableDb();
            String conditionString = RecordDeleteDao.Properties.RecordCode.columnName + '=' + Utils.escapeBlobArgument(recordCode);
            WhereCondition condition = new WhereCondition.StringCondition(conditionString);
            mDaoSession.getRecordDeleteDao().queryBuilder().where(condition).buildDelete().executeDeleteWithoutDetachingEntities();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeDbConnections();
        }
    }

    public List<RecordDelete> getRecordDelete(){
        List<RecordDelete> response = null;
        try {
            openReadableDb();
            if (!mDaoMaster.getDatabase().isOpen()) {
                openReadableDb();
            }
            RecordDeleteDao dao = mDaoSession.getRecordDeleteDao();
            response = dao.loadAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        closeDbConnections();

        return response;
    }

    public ImagePic getSignatureByFlightCode(byte[] flightCode) {
        if (flightCode == null) return null;
        ImagePic response = null;
        try {
            openReadableDb();
            String conditionString = ImagePicDao.Properties.LinkCode.columnName + '=' + Utils.escapeBlobArgument(flightCode);
            WhereCondition condition = new WhereCondition.StringCondition(conditionString);
            response = mDaoSession.getImagePicDao().queryBuilder().where(condition).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();
        return response;
    }

    public boolean insertImagePic(ImagePic imagePic) {
        try {
            openWritableDb();
            mDaoSession.getImagePicDao().insertOrReplace(imagePic);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeDbConnections();
        }
    }

    public List<Airfield> getAllAirfieldToSync() {
       List<Airfield> airfields = new ArrayList<>();
        try {
            openReadableDb();
            airfields = mDaoSession.getAirfieldDao().queryBuilder()
                    .where(AirfieldDao.Properties.Record_Upload.eq(true)).list();

        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDbConnections();

        return airfields;
    }
}

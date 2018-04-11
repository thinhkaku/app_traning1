package aero.pilotlog.databases.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import aero.pilotlog.databases.entities.ZCurrency;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ZCURRENCY.
*/
public class ZCurrencyDao extends AbstractDao<ZCurrency, Integer> {

    public static final String TABLENAME = "ZCURRENCY";

    /**
     * Properties of entity ZCurrency.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property CurrCode = new Property(0, Integer.class, "CurrCode", true, "CURRCODE");
        public final static Property CurrShort = new Property(1, String.class, "CurrShort", false, "CURRSHORT");
        public final static Property CurrLong = new Property(2, String.class, "CurrLong", false, "CURRLONG");
        public final static Property ConversionRate = new Property(3, Long.class, "ConversionRate", false, "CONVERSIONRATE");
        public final static Property Record_Modified = new Property(4, Long.class, "Record_Modified", false, "RECORD_MODIFIED");
    };


    public ZCurrencyDao(DaoConfig config) {
        super(config);
    }
    
    public ZCurrencyDao(DaoConfig config, aero.pilotlog.databases.dao.DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ZCURRENCY' (" + //
                "'CURRCODE' INTEGER PRIMARY KEY ," + // 0: CurrCode
                "'CURRSHORT' TEXT," + // 1: CurrShort
                "'CURRLONG' TEXT," + // 2: CurrLong
                "'CONVERSIONRATE' INTEGER," + // 3: ConversionRate
                "'RECORD_MODIFIED' INTEGER);"); // 4: Record_Modified
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ZCURRENCY'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ZCurrency entity) {
        stmt.clearBindings();
 
        Integer CurrCode = entity.getCurrCode();
        if (CurrCode != null) {
            stmt.bindLong(1, CurrCode);
        }
 
        String CurrShort = entity.getCurrShort();
        if (CurrShort != null) {
            stmt.bindString(2, CurrShort);
        }
 
        String CurrLong = entity.getCurrLong();
        if (CurrLong != null) {
            stmt.bindString(3, CurrLong);
        }
 
        Long ConversionRate = entity.getConversionRate();
        if (ConversionRate != null) {
            stmt.bindLong(4, ConversionRate);
        }
 
        Long Record_Modified = entity.getRecord_Modified();
        if (Record_Modified != null) {
            stmt.bindLong(5, Record_Modified);
        }
    }

    /** @inheritdoc */
    @Override
    public Integer readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ZCurrency readEntity(Cursor cursor, int offset) {
        ZCurrency entity = new ZCurrency( //
            cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0), // CurrCode
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // CurrShort
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // CurrLong
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3), // ConversionRate
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4) // Record_Modified
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ZCurrency entity, int offset) {
        entity.setCurrCode(cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0));
        entity.setCurrShort(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCurrLong(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setConversionRate(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
        entity.setRecord_Modified(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Integer updateKeyAfterInsert(ZCurrency entity, long rowId) {
        return entity.getCurrCode();
    }
    
    /** @inheritdoc */
    @Override
    public Integer getKey(ZCurrency entity) {
        if(entity != null) {
            return entity.getCurrCode();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
package aero.pilotlog.databases.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import aero.pilotlog.databases.entities.ZTimeZone;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ZTIME_ZONE.
*/
public class ZTimeZoneDao extends AbstractDao<ZTimeZone, Integer> {

    public static final String TABLENAME = "ZTIMEZONE";

    /**
     * Properties of entity ZTimeZone.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property TZCode = new Property(0, Integer.class, "TZCode", true, "TZCODE");
        public final static Property TimeZone = new Property(1, String.class, "TimeZone", false, "TIMEZONE");
        public final static Property ZoneShort = new Property(2, String.class, "ZoneShort", false, "ZONESHORT");
        public final static Property ZoneLong = new Property(3, String.class, "ZoneLong", false, "ZONELONG");
        public final static Property DSTCode = new Property(4, String.class, "DSTCode", false, "DSTCODE");
        public final static Property Record_Modified = new Property(5, Long.class, "Record_Modified", false, "RECORD_MODIFIED");
    };


    public ZTimeZoneDao(DaoConfig config) {
        super(config);
    }
    
    public ZTimeZoneDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ZTIMEZONE' (" + //
                "'TZCODE' INTEGER PRIMARY KEY ," + // 0: TZCode
                "'TIMEZONE' TEXT," + // 1: TimeZone
                "'ZONESHORT' TEXT," + // 2: ZoneShort
                "'ZONELONG' TEXT," + // 3: ZoneLong
                "'DSTCODE' TEXT," + // 4: DSTCode
                "'RECORD_MODIFIED' INTEGER);"); // 5: Record_Modified
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ZTIMEZONE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ZTimeZone entity) {
        stmt.clearBindings();
 
        Integer TZCode = entity.getTZCode();
        if (TZCode != null) {
            stmt.bindLong(1, TZCode);
        }
 
        String TimeZone = entity.getTimeZone();
        if (TimeZone != null) {
            stmt.bindString(2, TimeZone);
        }
 
        String ZoneShort = entity.getZoneShort();
        if (ZoneShort != null) {
            stmt.bindString(3, ZoneShort);
        }
 
        String ZoneLong = entity.getZoneLong();
        if (ZoneLong != null) {
            stmt.bindString(4, ZoneLong);
        }
 
        String DSTCode = entity.getDSTCode();
        if (DSTCode != null) {
            stmt.bindString(5, DSTCode);
        }
 
        Long Record_Modified = entity.getRecord_Modified();
        if (Record_Modified != null) {
            stmt.bindLong(6, Record_Modified);
        }
    }

    /** @inheritdoc */
    @Override
    public Integer readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ZTimeZone readEntity(Cursor cursor, int offset) {
        ZTimeZone entity = new ZTimeZone( //
            cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0), // TZCode
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // TimeZone
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // ZoneShort
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // ZoneLong
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // DSTCode
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5) // Record_Modified
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ZTimeZone entity, int offset) {
        entity.setTZCode(cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0));
        entity.setTimeZone(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setZoneShort(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setZoneLong(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setDSTCode(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setRecord_Modified(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Integer updateKeyAfterInsert(ZTimeZone entity, long rowId) {
        return entity.getTZCode();
    }
    
    /** @inheritdoc */
    @Override
    public Integer getKey(ZTimeZone entity) {
        if(entity != null) {
            return entity.getTZCode();
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
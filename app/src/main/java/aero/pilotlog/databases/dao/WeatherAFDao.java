package aero.pilotlog.databases.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import aero.pilotlog.databases.entities.WeatherAF;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table WEATHER_AF.
*/
public class WeatherAFDao extends AbstractDao<WeatherAF, byte[]> {

    public static final String TABLENAME = "WEATHERAF";

    /**
     * Properties of entity WeatherAF.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property WxAFCode = new Property(0, byte[].class, "WxAFCode", true, "WXAFCODE");
        public final static Property WxCode = new Property(1, byte[].class, "WxCode", false, "WXCODE");
        public final static Property AFCode = new Property(2, byte[].class, "AFCode", false, "AFCODE");
        public final static Property Record_Modified = new Property(3, Long.class, "Record_Modified", false, "RECORD_MODIFIED");
        public final static Property Record_Upload = new Property(4, Boolean.class, "Record_Upload", false, "RECORD_UPLOAD");
    };


    public WeatherAFDao(DaoConfig config) {
        super(config);
    }
    
    public WeatherAFDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'WEATHERAF' (" + //
                "'WXAFCODE' BLOB PRIMARY KEY ," + // 0: WxAFCode
                "'WXCODE' BLOB," + // 1: WxCode
                "'AFCODE' BLOB," + // 2: AFCode
                "'RECORD_MODIFIED' INTEGER," + // 3: Record_Modified
                "'RECORD_UPLOAD' INTEGER);"); // 4: Record_Upload
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'WEATHERAF'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, WeatherAF entity) {
        stmt.clearBindings();
 
        byte[] WxAFCode = entity.getWxAFCode();
        if (WxAFCode != null) {
            stmt.bindBlob(1, WxAFCode);
        }
 
        byte[] WxCode = entity.getWxCode();
        if (WxCode != null) {
            stmt.bindBlob(2, WxCode);
        }
 
        byte[] AFCode = entity.getAFCode();
        if (AFCode != null) {
            stmt.bindBlob(3, AFCode);
        }
 
        Long Record_Modified = entity.getRecord_Modified();
        if (Record_Modified != null) {
            stmt.bindLong(4, Record_Modified);
        }
 
        Boolean Record_Upload = entity.getRecord_Upload();
        if (Record_Upload != null) {
            stmt.bindLong(5, Record_Upload ? 1l: 0l);
        }
    }

    /** @inheritdoc */
    @Override
    public byte[] readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getBlob(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public WeatherAF readEntity(Cursor cursor, int offset) {
        WeatherAF entity = new WeatherAF( //
            cursor.isNull(offset + 0) ? null : cursor.getBlob(offset + 0), // WxAFCode
            cursor.isNull(offset + 1) ? null : cursor.getBlob(offset + 1), // WxCode
            cursor.isNull(offset + 2) ? null : cursor.getBlob(offset + 2), // AFCode
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3), // Record_Modified
            cursor.isNull(offset + 4) ? null : cursor.getShort(offset + 4) != 0 // Record_Upload
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, WeatherAF entity, int offset) {
        entity.setWxAFCode(cursor.isNull(offset + 0) ? null : cursor.getBlob(offset + 0));
        entity.setWxCode(cursor.isNull(offset + 1) ? null : cursor.getBlob(offset + 1));
        entity.setAFCode(cursor.isNull(offset + 2) ? null : cursor.getBlob(offset + 2));
        entity.setRecord_Modified(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
        entity.setRecord_Upload(cursor.isNull(offset + 4) ? null : cursor.getShort(offset + 4) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected byte[] updateKeyAfterInsert(WeatherAF entity, long rowId) {
        return entity.getWxAFCode();
    }
    
    /** @inheritdoc */
    @Override
    public byte[] getKey(WeatherAF entity) {
        if(entity != null) {
            return entity.getWxAFCode();
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
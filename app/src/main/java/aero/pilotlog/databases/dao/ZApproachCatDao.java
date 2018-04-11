package aero.pilotlog.databases.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import aero.pilotlog.databases.entities.ZApproachCat;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ZAPPROACH_CAT.
*/
public class ZApproachCatDao extends AbstractDao<ZApproachCat, Integer> {

    public static final String TABLENAME = "ZAPPROACHCAT";

    /**
     * Properties of entity ZApproachCat.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property APCat = new Property(0, Integer.class, "APCat", true, "APCAT");
        public final static Property APCatShort = new Property(1, String.class, "APCatShort", false, "APCATSHORT");
        public final static Property APCatLong = new Property(2, String.class, "APCatLong", false, "APCATLONG");
        public final static Property Record_Modified = new Property(3, Long.class, "Record_Modified", false, "RECORD_MODIFIED");
    };


    public ZApproachCatDao(DaoConfig config) {
        super(config);
    }
    
    public ZApproachCatDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ZAPPROACHCAT' (" + //
                "'APCAT' INTEGER PRIMARY KEY ," + // 0: APCat
                "'APCATSHORT' TEXT," + // 1: APCatShort
                "'APCATLONG' TEXT," + // 2: APCatLong
                "'RECORD_MODIFIED' INTEGER);"); // 3: Record_Modified
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ZAPPROACHCAT'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ZApproachCat entity) {
        stmt.clearBindings();
 
        Integer APCat = entity.getAPCat();
        if (APCat != null) {
            stmt.bindLong(1, APCat);
        }
 
        String APCatShort = entity.getAPCatShort();
        if (APCatShort != null) {
            stmt.bindString(2, APCatShort);
        }
 
        String APCatLong = entity.getAPCatLong();
        if (APCatLong != null) {
            stmt.bindString(3, APCatLong);
        }
 
        Long Record_Modified = entity.getRecord_Modified();
        if (Record_Modified != null) {
            stmt.bindLong(4, Record_Modified);
        }
    }

    /** @inheritdoc */
    @Override
    public Integer readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ZApproachCat readEntity(Cursor cursor, int offset) {
        ZApproachCat entity = new ZApproachCat( //
            cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0), // APCat
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // APCatShort
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // APCatLong
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3) // Record_Modified
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ZApproachCat entity, int offset) {
        entity.setAPCat(cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0));
        entity.setAPCatShort(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setAPCatLong(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setRecord_Modified(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Integer updateKeyAfterInsert(ZApproachCat entity, long rowId) {
        return entity.getAPCat();
    }
    
    /** @inheritdoc */
    @Override
    public Integer getKey(ZApproachCat entity) {
        if(entity != null) {
            return entity.getAPCat();
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
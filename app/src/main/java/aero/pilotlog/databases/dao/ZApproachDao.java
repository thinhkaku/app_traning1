package aero.pilotlog.databases.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import aero.pilotlog.databases.entities.ZApproach;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ZAPPROACH.
*/
public class ZApproachDao extends AbstractDao<ZApproach, Integer> {

    public static final String TABLENAME = "ZAPPROACH";

    /**
     * Properties of entity ZApproach.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property APCode = new Property(0, Integer.class, "APCode", true, "APCODE");
        public final static Property APCat = new Property(1, Integer.class, "APCat", false, "APCAT");
        public final static Property APShort = new Property(2, String.class, "APShort", false, "APSHORT");
        public final static Property APLong = new Property(3, String.class, "APLong", false, "APLONG");
        public final static Property MostUsed = new Property(4, Integer.class, "MostUsed", false, "MOSTUSED");
        public final static Property Record_Modified = new Property(5, Long.class, "Record_Modified", false, "RECORD_MODIFIED");
    };


    public ZApproachDao(DaoConfig config) {
        super(config);
    }
    
    public ZApproachDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ZAPPROACH' (" + //
                "'APCODE' INTEGER PRIMARY KEY ," + // 0: APCode
                "'APCAT' INTEGER," + // 1: APCat
                "'APSHORT' TEXT," + // 2: APShort
                "'APLONG' TEXT," + // 3: APLong
                "'MOSTUSED' INTEGER," + // 4: MostUsed
                "'RECORD_MODIFIED' INTEGER);"); // 5: Record_Modified
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ZAPPROACH'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ZApproach entity) {
        stmt.clearBindings();
 
        Integer APCode = entity.getAPCode();
        if (APCode != null) {
            stmt.bindLong(1, APCode);
        }
 
        Integer APCat = entity.getAPCat();
        if (APCat != null) {
            stmt.bindLong(2, APCat);
        }
 
        String APShort = entity.getAPShort();
        if (APShort != null) {
            stmt.bindString(3, APShort);
        }
 
        String APLong = entity.getAPLong();
        if (APLong != null) {
            stmt.bindString(4, APLong);
        }
 
        Integer MostUsed = entity.getMostUsed();
        if (MostUsed != null) {
            stmt.bindLong(5, MostUsed);
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
    public ZApproach readEntity(Cursor cursor, int offset) {
        ZApproach entity = new ZApproach( //
            cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0), // APCode
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // APCat
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // APShort
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // APLong
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // MostUsed
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5) // Record_Modified
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ZApproach entity, int offset) {
        entity.setAPCode(cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0));
        entity.setAPCat(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setAPShort(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setAPLong(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setMostUsed(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setRecord_Modified(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Integer updateKeyAfterInsert(ZApproach entity, long rowId) {
        return entity.getAPCode();
    }
    
    /** @inheritdoc */
    @Override
    public Integer getKey(ZApproach entity) {
        if(entity != null) {
            return entity.getAPCode();
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

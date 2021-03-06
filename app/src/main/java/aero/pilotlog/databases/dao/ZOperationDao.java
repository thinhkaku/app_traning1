package aero.pilotlog.databases.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import aero.pilotlog.databases.entities.ZOperation;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ZOPERATION.
*/
public class ZOperationDao extends AbstractDao<ZOperation, Integer> {

    public static final String TABLENAME = "ZOPERATION";

    /**
     * Properties of entity ZOperation.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property OpsCode = new Property(0, Integer.class, "OpsCode", true, "OPSCODE");
        public final static Property OpsShort = new Property(1, String.class, "OpsShort", false, "OPSSHORT");
        public final static Property OpsLong = new Property(2, String.class, "OpsLong", false, "OPSLONG");
        public final static Property MostUsed = new Property(3, Integer.class, "MostUsed", false, "MOSTUSED");
        public final static Property Record_Modified = new Property(4, Long.class, "Record_Modified", false, "RECORD_MODIFIED");
    };


    public ZOperationDao(DaoConfig config) {
        super(config);
    }
    
    public ZOperationDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ZOPERATION' (" + //
                "'OPSCODE' INTEGER PRIMARY KEY ," + // 0: OpsCode
                "'OPSSHORT' TEXT," + // 1: OpsShort
                "'OPSLONG' TEXT," + // 2: OpsLong
                "'MOST_USED' INTEGER," + // 3: MostUsed
                "'RECORD_MODIFIED' INTEGER);"); // 4: Record_Modified
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ZOPERATION'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ZOperation entity) {
        stmt.clearBindings();
 
        Integer OpsCode = entity.getOpsCode();
        if (OpsCode != null) {
            stmt.bindLong(1, OpsCode);
        }
 
        String OpsShort = entity.getOpsShort();
        if (OpsShort != null) {
            stmt.bindString(2, OpsShort);
        }
 
        String OpsLong = entity.getOpsLong();
        if (OpsLong != null) {
            stmt.bindString(3, OpsLong);
        }
 
        Integer MostUsed = entity.getMostUsed();
        if (MostUsed != null) {
            stmt.bindLong(4, MostUsed);
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
    public ZOperation readEntity(Cursor cursor, int offset) {
        ZOperation entity = new ZOperation( //
            cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0), // OpsCode
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // OpsShort
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // OpsLong
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // MostUsed
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4) // Record_Modified
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ZOperation entity, int offset) {
        entity.setOpsCode(cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0));
        entity.setOpsShort(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setOpsLong(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setMostUsed(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setRecord_Modified(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Integer updateKeyAfterInsert(ZOperation entity, long rowId) {
        return entity.getOpsCode();
    }
    
    /** @inheritdoc */
    @Override
    public Integer getKey(ZOperation entity) {
        if(entity != null) {
            return entity.getOpsCode();
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

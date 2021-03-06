package aero.pilotlog.databases.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import aero.pilotlog.databases.entities.ZPackage;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ZPACKAGE.
*/
public class ZPackageDao extends AbstractDao<ZPackage, Integer> {

    public static final String TABLENAME = "ZPACKAGE";

    /**
     * Properties of entity ZPackage.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property PackageCode = new Property(0, Integer.class, "PackageCode", true, "PACKAGECODE");
        public final static Property PackID = new Property(1, String.class, "PackID", false, "PACKID");
        public final static Property Table = new Property(2, String.class, "Table", false, "TABLE");
        public final static Property Details = new Property(3, String.class, "Details", false, "DETAILS");
        public final static Property Record_Modified = new Property(4, Long.class, "Record_Modified", false, "RECORD_MODIFIED");
    };


    public ZPackageDao(DaoConfig config) {
        super(config);
    }
    
    public ZPackageDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ZPACKAGE' (" + //
                "'PACKAGECODE' INTEGER PRIMARY KEY ," + // 0: PackageCode
                "'PACKID' TEXT," + // 1: PackID
                "'TABLE' TEXT," + // 2: Table
                "'DETAILS' TEXT," + // 3: Details
                "'RECORD_MODIFIED' INTEGER);"); // 4: Record_Modified
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ZPACKAGE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ZPackage entity) {
        stmt.clearBindings();
 
        Integer PackageCode = entity.getPackageCode();
        if (PackageCode != null) {
            stmt.bindLong(1, PackageCode);
        }
 
        String PackID = entity.getPackID();
        if (PackID != null) {
            stmt.bindString(2, PackID);
        }
 
        String Table = entity.getTable();
        if (Table != null) {
            stmt.bindString(3, Table);
        }
 
        String Details = entity.getDetails();
        if (Details != null) {
            stmt.bindString(4, Details);
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
    public ZPackage readEntity(Cursor cursor, int offset) {
        ZPackage entity = new ZPackage( //
            cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0), // PackageCode
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // PackID
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // Table
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // Details
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4) // Record_Modified
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ZPackage entity, int offset) {
        entity.setPackageCode(cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0));
        entity.setPackID(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setTable(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setDetails(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setRecord_Modified(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Integer updateKeyAfterInsert(ZPackage entity, long rowId) {
        return entity.getPackageCode();
    }
    
    /** @inheritdoc */
    @Override
    public Integer getKey(ZPackage entity) {
        if(entity != null) {
            return entity.getPackageCode();
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

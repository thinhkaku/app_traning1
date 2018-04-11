package aero.pilotlog.databases.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import aero.pilotlog.databases.entities.ZTimeZoneDST;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ZTIME_ZONE_DST.
*/
public class ZTimeZoneDSTDao extends AbstractDao<ZTimeZoneDST, String> {

    public static final String TABLENAME = "ZTIMEZONEDST";

    /**
     * Properties of entity ZTimeZoneDST.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property DSTCode = new Property(0, String.class, "DSTCode", true, "DSTCODE");
        public final static Property Rule = new Property(1, String.class, "Rule", false, "RULE");
        public final static Property Record_Modified = new Property(2, Long.class, "Record_Modified", false, "RECORD_MODIFIED");
    };


    public ZTimeZoneDSTDao(DaoConfig config) {
        super(config);
    }
    
    public ZTimeZoneDSTDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ZTIMEZONEDST' (" + //
                "'DSTCODE' TEXT PRIMARY KEY NOT NULL ," + // 0: DSTCode
                "'RULE' TEXT," + // 1: Rule
                "'RECORD_MODIFIED' INTEGER);"); // 2: Record_Modified
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ZTIMEZONEDST'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ZTimeZoneDST entity) {
        stmt.clearBindings();
 
        String DSTCode = entity.getDSTCode();
        if (DSTCode != null) {
            stmt.bindString(1, DSTCode);
        }
 
        String Rule = entity.getRule();
        if (Rule != null) {
            stmt.bindString(2, Rule);
        }
 
        Long Record_Modified = entity.getRecord_Modified();
        if (Record_Modified != null) {
            stmt.bindLong(3, Record_Modified);
        }
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ZTimeZoneDST readEntity(Cursor cursor, int offset) {
        ZTimeZoneDST entity = new ZTimeZoneDST( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // DSTCode
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // Rule
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2) // Record_Modified
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ZTimeZoneDST entity, int offset) {
        entity.setDSTCode(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setRule(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setRecord_Modified(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(ZTimeZoneDST entity, long rowId) {
        return entity.getDSTCode();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(ZTimeZoneDST entity) {
        if(entity != null) {
            return entity.getDSTCode();
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

package aero.pilotlog.databases.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import aero.pilotlog.databases.entities.LimitRules;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table LIMIT_RULES.
*/
public class LimitRulesDao extends AbstractDao<LimitRules, byte[]> {

    public static final String TABLENAME = "LIMITRULES";

    /**
     * Properties of entity LimitRules.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property LimitCode = new Property(0, byte[].class, "LimitCode", true, "LIMITCODE");
        public final static Property LType = new Property(1, Integer.class, "LType", false, "LTYPE");
        public final static Property LMinutes = new Property(2, Integer.class, "LMinutes", false, "LMINUTES");
        public final static Property LPeriod = new Property(3, Integer.class, "LPeriodCode", false, "LPERIODCODE");
        public final static Property LFrom = new Property(4, String.class, "LFrom", false, "LFROM");
        public final static Property LTo = new Property(5, String.class, "LTo", false, "LTO");
        public final static Property LZone = new Property(6, Integer.class, "LZone", false, "LZONE");
        public final static Property Record_Modified = new Property(7, Long.class, "Record_Modified", false, "RECORD_MODIFIED");
        public final static Property Record_Upload = new Property(8, Boolean.class, "Record_Upload", false, "RECORD_UPLOAD");
    }


    public LimitRulesDao(DaoConfig config) {
        super(config);
    }
    
    public LimitRulesDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'LIMITRULES' (" + //
                "'LIMITCODE' BLOB PRIMARY KEY ," + // 0: LimitCode
                "'LTYPE' INTEGER," + // 1: LType
                "'LMINUTES' INTEGER," + // 2: LMinutes
                "'LPERIODCODE' INTEGER," + // 3: LPeriod
                "'LFROM' TEXT," + // 4: LFrom
                "'LTO' TEXT," + // 5: LTo
                "'LZONE' INTEGER," + // 6: LZone
                "'RECORD_MODIFIED' INTEGER," + // 7: Record_Modified
                "'RECORD_UPLOAD' INTEGER);"); // 8: Record_Upload
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'LIMITRULES'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, LimitRules entity) {
        stmt.clearBindings();
 
        byte[] LimitCode = entity.getLimitCode();
        if (LimitCode != null) {
            stmt.bindBlob(1, LimitCode);
        }
 
        Integer LType = entity.getLType();
        if (LType != null) {
            stmt.bindLong(2, LType);
        }
 
        Integer LMinutes = entity.getLMinutes();
        if (LMinutes != null) {
            stmt.bindLong(3, LMinutes);
        }
 
        Integer LPeriod = entity.getLPeriodCode();
        if (LPeriod != null) {
            stmt.bindLong(4, LPeriod);
        }
 
        String LFrom = entity.getLFrom();
        if (LFrom != null) {
            stmt.bindString(5, LFrom);
        }
 
        String LTo = entity.getLTo();
        if (LTo != null) {
            stmt.bindString(6, LTo);
        }
 
        Integer LZone = entity.getLZone();
        if (LZone != null) {
            stmt.bindLong(7, LZone);
        }
 
        Long Record_Modified = entity.getRecord_Modified();
        if (Record_Modified != null) {
            stmt.bindLong(8, Record_Modified);
        }
 
        Boolean Record_Upload = entity.getRecord_Upload();
        if (Record_Upload != null) {
            stmt.bindLong(9, Record_Upload ? 1l: 0l);
        }
    }

    /** @inheritdoc */
    @Override
    public byte[] readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getBlob(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public LimitRules readEntity(Cursor cursor, int offset) {
        LimitRules entity = new LimitRules( //
            cursor.isNull(offset + 0) ? null : cursor.getBlob(offset + 0), // LimitCode
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // LType
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // LMinutes
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // LPeriod
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // LFrom
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // LTo
            cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // LZone
            cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7), // Record_Modified
            cursor.isNull(offset + 8) ? null : cursor.getShort(offset + 8) != 0 // Record_Upload
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, LimitRules entity, int offset) {
        entity.setLimitCode(cursor.isNull(offset + 0) ? null : cursor.getBlob(offset + 0));
        entity.setLType(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setLMinutes(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setLPeriodCode(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setLFrom(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setLTo(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setLZone(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
        entity.setRecord_Modified(cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7));
        entity.setRecord_Upload(cursor.isNull(offset + 8) ? null : cursor.getShort(offset + 8) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected byte[] updateKeyAfterInsert(LimitRules entity, long rowId) {
        return entity.getLimitCode();
    }
    
    /** @inheritdoc */
    @Override
    public byte[] getKey(LimitRules entity) {
        if(entity != null) {
            return entity.getLimitCode();
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

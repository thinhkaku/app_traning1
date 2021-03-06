package aero.pilotlog.databases.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import aero.pilotlog.databases.entities.AllowanceRules;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ALLOWANCE_RULES.
*/
public class AllowanceRulesDao extends AbstractDao<AllowanceRules, byte[]> {

    public static final String TABLENAME = "ALLOWANCERULES";

    /**
     * Properties of entity AllowanceRules.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property ARCode = new Property(0, byte[].class, "ARCode", true, "ARCODE");
        public final static Property ARType = new Property(1, Integer.class, "ARType", false, "ARTYPE");
        public final static Property ARClassEngine = new Property(2, Integer.class, "ARClassEngine", false, "ARCLASSENGINE");
        public final static Property ARModel = new Property(3, String.class, "ARModel", false, "ARMODEL");
        public final static Property ARSubModel = new Property(4, String.class, "ARSubModel", false, "ARSUBMODEL");
        public final static Property ARCurrency = new Property(5, Integer.class, "ARCurrency", false, "ARCURRENCY");
        public final static Property ARCostFix = new Property(6, Integer.class, "ARCostFix", false, "ARCOSTFIX");
        public final static Property ARCostVar = new Property(7, Integer.class, "ARCostVar", false, "ARCOSTVAR");
        public final static Property ARCostMinHour = new Property(8, Integer.class, "ARCostMinHour", false, "ARCOSTMINHOUR");
        public final static Property ARCostBase = new Property(9, Integer.class, "ARCostBase", false, "ARCOSTBASE");
        public final static Property ARWhen = new Property(10, Integer.class, "ARWhen", false, "ARWHEN");
        public final static Property ARWhen1 = new Property(11, String.class, "ARWhen1", false, "ARWHEN1");
        public final static Property ARWhen2 = new Property(12, String.class, "ARWhen2", false, "ARWHEN2");
        public final static Property ARReadable = new Property(13, String.class, "ARReadable", false, "ARREADABLE");
        public final static Property Record_Modified = new Property(14, Long.class, "Record_Modified", false, "RECORD_MODIFIED");
        public final static Property Record_Upload = new Property(15, Boolean.class, "Record_Upload", false, "RECORD_UPLOAD");
    };


    public AllowanceRulesDao(DaoConfig config) {
        super(config);
    }
    
    public AllowanceRulesDao(DaoConfig config, aero.pilotlog.databases.dao.DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ALLOWANCERULES' (" + //
                "'ARCODE' BLOB PRIMARY KEY ," + // 0: ARCode
                "'ARTYPE' INTEGER," + // 1: ARType
                "'ARCLASSENGINE' INTEGER," + // 2: ARClassEngine
                "'ARMODEL' TEXT," + // 3: ARModel
                "'ARSUBMODEL' TEXT," + // 4: ARSubModel
                "'ARCURRENCY' INTEGER," + // 5: ARCurrency
                "'ARCOSTFIX' INTEGER," + // 6: ARCostFix
                "'ARCOSTVAR' INTEGER," + // 7: ARCostVar
                "'ARCOSTMINHOUR' INTEGER," + // 8: ARCostMinHour
                "'ARCOSTBASE' INTEGER," + // 9: ARCostBase
                "'ARWHEN' INTEGER," + // 10: ARWhen
                "'ARWHEN1' TEXT," + // 11: ARWhen1
                "'ARWHEN2' TEXT," + // 12: ARWhen2
                "'ARREADABLE' TEXT," + // 13: ARReadable
                "'RECORD_MODIFIED' INTEGER," + // 14: Record_Modified
                "'RECORD_UPLOAD' INTEGER);"); // 15: Record_Upload
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ALLOWANCERULES'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, AllowanceRules entity) {
        stmt.clearBindings();
 
        byte[] ARCode = entity.getARCode();
        if (ARCode != null) {
            stmt.bindBlob(1, ARCode);
        }
 
        Integer ARType = entity.getARType();
        if (ARType != null) {
            stmt.bindLong(2, ARType);
        }
 
        Integer ARClassEngine = entity.getARClassEngine();
        if (ARClassEngine != null) {
            stmt.bindLong(3, ARClassEngine);
        }
 
        String ARModel = entity.getARModel();
        if (ARModel != null) {
            stmt.bindString(4, ARModel);
        }
 
        String ARSubModel = entity.getARSubModel();
        if (ARSubModel != null) {
            stmt.bindString(5, ARSubModel);
        }
 
        Integer ARCurrency = entity.getARCurrency();
        if (ARCurrency != null) {
            stmt.bindLong(6, ARCurrency);
        }
 
        Integer ARCostFix = entity.getARCostFix();
        if (ARCostFix != null) {
            stmt.bindLong(7, ARCostFix);
        }
 
        Integer ARCostVar = entity.getARCostVar();
        if (ARCostVar != null) {
            stmt.bindLong(8, ARCostVar);
        }
 
        Integer ARCostMinHour = entity.getARCostMinHour();
        if (ARCostMinHour != null) {
            stmt.bindLong(9, ARCostMinHour);
        }
 
        Integer ARCostBase = entity.getARCostBase();
        if (ARCostBase != null) {
            stmt.bindLong(10, ARCostBase);
        }
 
        Integer ARWhen = entity.getARWhen();
        if (ARWhen != null) {
            stmt.bindLong(11, ARWhen);
        }
 
        String ARWhen1 = entity.getARWhen1();
        if (ARWhen1 != null) {
            stmt.bindString(12, ARWhen1);
        }
 
        String ARWhen2 = entity.getARWhen2();
        if (ARWhen2 != null) {
            stmt.bindString(13, ARWhen2);
        }
 
        String ARReadable = entity.getARReadable();
        if (ARReadable != null) {
            stmt.bindString(14, ARReadable);
        }
 
        Long Record_Modified = entity.getRecord_Modified();
        if (Record_Modified != null) {
            stmt.bindLong(15, Record_Modified);
        }
 
        Boolean Record_Upload = entity.getRecord_Upload();
        if (Record_Upload != null) {
            stmt.bindLong(16, Record_Upload ? 1l: 0l);
        }
    }

    /** @inheritdoc */
    @Override
    public byte[] readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getBlob(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public AllowanceRules readEntity(Cursor cursor, int offset) {
        AllowanceRules entity = new AllowanceRules( //
            cursor.isNull(offset + 0) ? null : cursor.getBlob(offset + 0), // ARCode
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // ARType
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // ARClassEngine
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // ARModel
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // ARSubModel
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // ARCurrency
            cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // ARCostFix
            cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7), // ARCostVar
            cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8), // ARCostMinHour
            cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9), // ARCostBase
            cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10), // ARWhen
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // ARWhen1
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // ARWhen2
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // ARReadable
            cursor.isNull(offset + 14) ? null : cursor.getLong(offset + 14), // Record_Modified
            cursor.isNull(offset + 15) ? null : cursor.getShort(offset + 15) != 0 // Record_Upload
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, AllowanceRules entity, int offset) {
        entity.setARCode(cursor.isNull(offset + 0) ? null : cursor.getBlob(offset + 0));
        entity.setARType(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setARClassEngine(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setARModel(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setARSubModel(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setARCurrency(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setARCostFix(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
        entity.setARCostVar(cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7));
        entity.setARCostMinHour(cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8));
        entity.setARCostBase(cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9));
        entity.setARWhen(cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10));
        entity.setARWhen1(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setARWhen2(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setARReadable(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setRecord_Modified(cursor.isNull(offset + 14) ? null : cursor.getLong(offset + 14));
        entity.setRecord_Upload(cursor.isNull(offset + 15) ? null : cursor.getShort(offset + 15) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected byte[] updateKeyAfterInsert(AllowanceRules entity, long rowId) {
        return entity.getARCode();
    }
    
    /** @inheritdoc */
    @Override
    public byte[] getKey(AllowanceRules entity) {
        if(entity != null) {
            return entity.getARCode();
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

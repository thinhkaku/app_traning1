package aero.pilotlog.databases.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import aero.pilotlog.databases.entities.ZExpense;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table ZEXPENSE.
*/
public class ZExpenseDao extends AbstractDao<ZExpense, Integer> {

    public static final String TABLENAME = "ZEXPENSE";

    /**
     * Properties of entity ZExpense.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property ETCode = new Property(0, Integer.class, "ETCode", true, "ETCODE");
        public final static Property Debit = new Property(1, Boolean.class, "Debit", false, "DEBIT");
        public final static Property ExpTypeShort = new Property(2, String.class, "ExpTypeShort", false, "EXPTYPESHORT");
        public final static Property ExpTypeLong = new Property(3, String.class, "ExpTypeLong", false, "EXPTYPELONG");
        public final static Property ExpGroupCode = new Property(4, Integer.class, "ExpGroupCode", false, "EXPGROUPCODE");
        public final static Property MostUsed = new Property(5, Integer.class, "MostUsed", false, "MOSTUSED");
        public final static Property Record_Modified = new Property(6, Long.class, "Record_Modified", false, "RECORD_MODIFIED");
    };


    public ZExpenseDao(DaoConfig config) {
        super(config);
    }
    
    public ZExpenseDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ZEXPENSE' (" + //
                "'ETCODE' INTEGER PRIMARY KEY ," + // 0: ETCode
                "'DEBIT' INTEGER," + // 1: Debit
                "'EXPTYPESHORT' TEXT," + // 2: ExpTypeShort
                "'EXPTYPELONG' TEXT," + // 3: ExpTypeLong
                "'EXPGROUPCODE' INTEGER," + // 4: ExpGroupCode
                "'MOSTUSED' INTEGER," + // 5: MostUsed
                "'RECORD_MODIFIED' INTEGER);"); // 6: Record_Modified
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ZEXPENSE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ZExpense entity) {
        stmt.clearBindings();
 
        Integer ETCode = entity.getETCode();
        if (ETCode != null) {
            stmt.bindLong(1, ETCode);
        }
 
        Boolean Debit = entity.getDebit();
        if (Debit != null) {
            stmt.bindLong(2, Debit ? 1l: 0l);
        }
 
        String ExpTypeShort = entity.getExpTypeShort();
        if (ExpTypeShort != null) {
            stmt.bindString(3, ExpTypeShort);
        }
 
        String ExpTypeLong = entity.getExpTypeLong();
        if (ExpTypeLong != null) {
            stmt.bindString(4, ExpTypeLong);
        }
 
        Integer ExpGroupCode = entity.getExpGroupCode();
        if (ExpGroupCode != null) {
            stmt.bindLong(5, ExpGroupCode);
        }
 
        Integer MostUsed = entity.getMostUsed();
        if (MostUsed != null) {
            stmt.bindLong(6, MostUsed);
        }
 
        Long Record_Modified = entity.getRecord_Modified();
        if (Record_Modified != null) {
            stmt.bindLong(7, Record_Modified);
        }
    }

    /** @inheritdoc */
    @Override
    public Integer readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ZExpense readEntity(Cursor cursor, int offset) {
        ZExpense entity = new ZExpense( //
            cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0), // ETCode
            cursor.isNull(offset + 1) ? null : cursor.getShort(offset + 1) != 0, // Debit
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // ExpTypeShort
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // ExpTypeLong
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // ExpGroupCode
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // MostUsed
            cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6) // Record_Modified
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ZExpense entity, int offset) {
        entity.setETCode(cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0));
        entity.setDebit(cursor.isNull(offset + 1) ? null : cursor.getShort(offset + 1) != 0);
        entity.setExpTypeShort(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setExpTypeLong(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setExpGroupCode(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setMostUsed(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setRecord_Modified(cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6));
     }
    
    /** @inheritdoc */
    @Override
    protected Integer updateKeyAfterInsert(ZExpense entity, long rowId) {
        return entity.getETCode();
    }
    
    /** @inheritdoc */
    @Override
    public Integer getKey(ZExpense entity) {
        if(entity != null) {
            return entity.getETCode();
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

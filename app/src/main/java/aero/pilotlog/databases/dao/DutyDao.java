package aero.pilotlog.databases.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import aero.pilotlog.databases.entities.Duty;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table DUTY.
*/
public class DutyDao extends AbstractDao<Duty, byte[]> {

    public static final String TABLENAME = "DUTY";

    /**
     * Properties of entity Duty.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property DutyCode = new Property(0, byte[].class, "DutyCode", true, "DUTYCODE");
        public final static Property DutyType = new Property(1, Integer.class, "DutyType", false, "DUTYTYPE");
        public final static Property Deadhead = new Property(2, Boolean.class, "Deadhead", false, "DEADHEAD");
        public final static Property EventDescription = new Property(3, String.class, "EventDescription", false, "EVENTDESCRIPTION");
        public final static Property EventDateUTC = new Property(4, String.class, "EventDateUTC", false, "EVENTDATEUTC");
        public final static Property EventDateLOCAL = new Property(5, String.class, "EventDateLOCAL", false, "EVENTDATELOCAL");
        public final static Property EventDateBASE = new Property(6, String.class, "EventDateBASE", false, "EVENTDATEBASE");
        public final static Property EventStartUTC = new Property(7, Integer.class, "EventStartUTC", false, "EVENTSTARTUTC");
        public final static Property EventEndUTC = new Property(8, Integer.class, "EventEndUTC", false, "EVENTENDUTC");
        public final static Property StartOffset = new Property(9, Integer.class, "StartOffset", false, "STARTOFFSET");
        public final static Property EndOffset = new Property(10, Integer.class, "EndOffset", false, "ENDOFFSET");
        public final static Property BaseOffset = new Property(11, Integer.class, "BaseOffset", false, "BASEOFFSET");
        public final static Property StartTZCode = new Property(12, Integer.class, "StartTZCode", false, "STARTTZCODE");
        public final static Property EndTZCode = new Property(13, Integer.class, "EndTZCode", false, "ENDTZCODE");
        public final static Property Duration = new Property(14, Integer.class, "Duration", false, "DURATION");
        public final static Property DutyNotes = new Property(15, String.class, "DutyNotes", false, "DUTYNOTES");
        public final static Property Record_Modified = new Property(16, Long.class, "Record_Modified", false, "RECORD_MODIFIED");
        public final static Property Record_Upload = new Property(17, Boolean.class, "Record_Upload", false, "RECORD_UPLOAD");
    };


    public DutyDao(DaoConfig config) {
        super(config);
    }
    
    public DutyDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'DUTY' (" + //
                "'DUTYCODE' BLOB PRIMARY KEY ," + // 0: DutyCode
                "'DUTYTYPE' INTEGER," + // 1: DutyType
                "'DEADHEAD' INTEGER," + // 2: Deadhead
                "'EVENTDESCRIPTION' TEXT," + // 3: EventDescription
                "'EVENTDATEUTC' TEXT," + // 4: EventDateUTC
                "'EVENTDATELOCAL' TEXT," + // 5: EventDateLOCAL
                "'EVENTDATEBASE' TEXT," + // 6: EventDateBASE
                "'EVENTSTARTUTC' INTEGER," + // 7: EventStartUTC
                "'EVENTENDUTC' INTEGER," + // 8: EventEndUTC
                "'STARTOFFSET' INTEGER," + // 9: StartOffset
                "'ENDOFFSET' INTEGER," + // 10: EndOffset
                "'BASEOFFSET' INTEGER," + // 11: BaseOffset
                "'STARTTZCODE' INTEGER," + // 12: StartTZCode
                "'ENDTZCODE' INTEGER," + // 13: EndTZCode
                "'DURATION' INTEGER," + // 14: Duration
                "'DUTYNOTES' TEXT," + // 15: DutyNotes
                "'RECORD_MODIFIED' INTEGER," + // 16: Record_Modified
                "'RECORD_UPLOAD' INTEGER);"); // 17: Record_Upload
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'DUTY'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Duty entity) {
        stmt.clearBindings();
 
        byte[] DutyCode = entity.getDutyCode();
        if (DutyCode != null) {
            stmt.bindBlob(1, DutyCode);
        }
 
        Integer DutyType = entity.getDutyType();
        if (DutyType != null) {
            stmt.bindLong(2, DutyType);
        }
 
        Boolean Deadhead = entity.getDeadhead();
        if (Deadhead != null) {
            stmt.bindLong(3, Deadhead ? 1l: 0l);
        }
 
        String EventDescription = entity.getEventDescription();
        if (EventDescription != null) {
            stmt.bindString(4, EventDescription);
        }
 
        String EventDateUTC = entity.getEventDateUTC();
        if (EventDateUTC != null) {
            stmt.bindString(5, EventDateUTC);
        }
 
        String EventDateLOCAL = entity.getEventDateLOCAL();
        if (EventDateLOCAL != null) {
            stmt.bindString(6, EventDateLOCAL);
        }
 
        String EventDateBASE = entity.getEventDateBASE();
        if (EventDateBASE != null) {
            stmt.bindString(7, EventDateBASE);
        }
 
        Integer EventStartUTC = entity.getEventStartUTC();
        if (EventStartUTC != null) {
            stmt.bindLong(8, EventStartUTC);
        }
 
        Integer EventEndUTC = entity.getEventEndUTC();
        if (EventEndUTC != null) {
            stmt.bindLong(9, EventEndUTC);
        }
 
        Integer StartOffset = entity.getStartOffset();
        if (StartOffset != null) {
            stmt.bindLong(10, StartOffset);
        }
 
        Integer EndOffset = entity.getEndOffset();
        if (EndOffset != null) {
            stmt.bindLong(11, EndOffset);
        }
 
        Integer BaseOffset = entity.getBaseOffset();
        if (BaseOffset != null) {
            stmt.bindLong(12, BaseOffset);
        }
 
        Integer StartTZCode = entity.getStartTZCode();
        if (StartTZCode != null) {
            stmt.bindLong(13, StartTZCode);
        }
 
        Integer EndTZCode = entity.getEndTZCode();
        if (EndTZCode != null) {
            stmt.bindLong(14, EndTZCode);
        }
 
        Integer Duration = entity.getDuration();
        if (Duration != null) {
            stmt.bindLong(15, Duration);
        }
 
        String DutyNotes = entity.getDutyNotes();
        if (DutyNotes != null) {
            stmt.bindString(16, DutyNotes);
        }
 
        Long Record_Modified = entity.getRecord_Modified();
        if (Record_Modified != null) {
            stmt.bindLong(17, Record_Modified);
        }
 
        Boolean Record_Upload = entity.getRecord_Upload();
        if (Record_Upload != null) {
            stmt.bindLong(18, Record_Upload ? 1l: 0l);
        }
    }

    /** @inheritdoc */
    @Override
    public byte[] readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getBlob(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Duty readEntity(Cursor cursor, int offset) {
        Duty entity = new Duty( //
            cursor.isNull(offset + 0) ? null : cursor.getBlob(offset + 0), // DutyCode
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // DutyType
            cursor.isNull(offset + 2) ? null : cursor.getShort(offset + 2) != 0, // Deadhead
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // EventDescription
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // EventDateUTC
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // EventDateLOCAL
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // EventDateBASE
            cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7), // EventStartUTC
            cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8), // EventEndUTC
            cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9), // StartOffset
            cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10), // EndOffset
            cursor.isNull(offset + 11) ? null : cursor.getInt(offset + 11), // BaseOffset
            cursor.isNull(offset + 12) ? null : cursor.getInt(offset + 12), // StartTZCode
            cursor.isNull(offset + 13) ? null : cursor.getInt(offset + 13), // EndTZCode
            cursor.isNull(offset + 14) ? null : cursor.getInt(offset + 14), // Duration
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // DutyNotes
            cursor.isNull(offset + 16) ? null : cursor.getLong(offset + 16), // Record_Modified
            cursor.isNull(offset + 17) ? null : cursor.getShort(offset + 17) != 0 // Record_Upload
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Duty entity, int offset) {
        entity.setDutyCode(cursor.isNull(offset + 0) ? null : cursor.getBlob(offset + 0));
        entity.setDutyType(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setDeadhead(cursor.isNull(offset + 2) ? null : cursor.getShort(offset + 2) != 0);
        entity.setEventDescription(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setEventDateUTC(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setEventDateLOCAL(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setEventDateBASE(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setEventStartUTC(cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7));
        entity.setEventEndUTC(cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8));
        entity.setStartOffset(cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9));
        entity.setEndOffset(cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10));
        entity.setBaseOffset(cursor.isNull(offset + 11) ? null : cursor.getInt(offset + 11));
        entity.setStartTZCode(cursor.isNull(offset + 12) ? null : cursor.getInt(offset + 12));
        entity.setEndTZCode(cursor.isNull(offset + 13) ? null : cursor.getInt(offset + 13));
        entity.setDuration(cursor.isNull(offset + 14) ? null : cursor.getInt(offset + 14));
        entity.setDutyNotes(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setRecord_Modified(cursor.isNull(offset + 16) ? null : cursor.getLong(offset + 16));
        entity.setRecord_Upload(cursor.isNull(offset + 17) ? null : cursor.getShort(offset + 17) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected byte[] updateKeyAfterInsert(Duty entity, long rowId) {
        return entity.getDutyCode();
    }
    
    /** @inheritdoc */
    @Override
    public byte[] getKey(Duty entity) {
        if(entity != null) {
            return entity.getDutyCode();
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
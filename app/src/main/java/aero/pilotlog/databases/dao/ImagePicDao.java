package aero.pilotlog.databases.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import aero.pilotlog.databases.entities.ImagePic;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table IMAGE_PIC.
*/
public class ImagePicDao extends AbstractDao<ImagePic, byte[]> {

    public static final String TABLENAME = "IMAGEPIC";

    /**
     * Properties of entity ImagePic.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property ImgCode = new Property(0, byte[].class, "ImgCode", true, "IMGCODE");
        public final static Property LinkCode = new Property(1, byte[].class, "LinkCode", false, "LINKCODE");
        public final static Property FileName = new Property(2, String.class, "FileName", false, "FILENAME");
        public final static Property ImgURL = new Property(3, String.class, "ImgURL", false, "IMGURL");
        public final static Property Record_Modified = new Property(4, Long.class, "Record_Modified", false, "RECORD_MODIFIED");
        public final static Property Record_Upload = new Property(5, Boolean.class, "Record_Upload", false, "RECORD_UPLOAD");
    };


    public ImagePicDao(DaoConfig config) {
        super(config);
    }
    
    public ImagePicDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'IMAGEPIC' (" + //
                "'IMGCODE' BLOB PRIMARY KEY ," + // 0: ImgCode
                "'LINKCODE' BLOB," + // 1: LinkCode
                "'FILENAME' TEXT," + // 2: FileName
                "'IMGURL' TEXT," + // 3: ImgURL
                "'RECORD_MODIFIED' INTEGER," + // 4: Record_Modified
                "'RECORD_UPLOAD' INTEGER);"); // 5: Record_Upload
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'IMAGEPIC'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ImagePic entity) {
        stmt.clearBindings();
 
        byte[] ImgCode = entity.getImgCode();
        if (ImgCode != null) {
            stmt.bindBlob(1, ImgCode);
        }
 
        byte[] LinkCode = entity.getLinkCode();
        if (LinkCode != null) {
            stmt.bindBlob(2, LinkCode);
        }
 
        String FileName = entity.getFileName();
        if (FileName != null) {
            stmt.bindString(3, FileName);
        }
 
        String ImgURL = entity.getImgURL();
        if (ImgURL != null) {
            stmt.bindString(4, ImgURL);
        }
 
        Long Record_Modified = entity.getRecord_Modified();
        if (Record_Modified != null) {
            stmt.bindLong(5, Record_Modified);
        }
 
        Boolean Record_Upload = entity.getRecord_Upload();
        if (Record_Upload != null) {
            stmt.bindLong(6, Record_Upload ? 1l: 0l);
        }
    }

    /** @inheritdoc */
    @Override
    public byte[] readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getBlob(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ImagePic readEntity(Cursor cursor, int offset) {
        ImagePic entity = new ImagePic( //
            cursor.isNull(offset + 0) ? null : cursor.getBlob(offset + 0), // ImgCode
            cursor.isNull(offset + 1) ? null : cursor.getBlob(offset + 1), // LinkCode
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // FileName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // ImgURL
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4), // Record_Modified
            cursor.isNull(offset + 5) ? null : cursor.getShort(offset + 5) != 0 // Record_Upload
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ImagePic entity, int offset) {
        entity.setImgCode(cursor.isNull(offset + 0) ? null : cursor.getBlob(offset + 0));
        entity.setLinkCode(cursor.isNull(offset + 1) ? null : cursor.getBlob(offset + 1));
        entity.setFileName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setImgURL(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setRecord_Modified(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
        entity.setRecord_Upload(cursor.isNull(offset + 5) ? null : cursor.getShort(offset + 5) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected byte[] updateKeyAfterInsert(ImagePic entity, long rowId) {
        return entity.getImgCode();
    }
    
    /** @inheritdoc */
    @Override
    public byte[] getKey(ImagePic entity) {
        if(entity != null) {
            return entity.getImgCode();
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

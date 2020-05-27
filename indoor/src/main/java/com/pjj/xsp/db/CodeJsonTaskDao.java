package com.pjj.xsp.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CODE_JSON_TASK".
*/
public class CodeJsonTaskDao extends AbstractDao<CodeJsonTask, Long> {

    public static final String TABLENAME = "CODE_JSON_TASK";

    /**
     * Properties of entity CodeJsonTask.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property CommandId = new Property(1, String.class, "commandId", false, "COMMAND_ID");
        public final static Property Json = new Property(2, String.class, "json", false, "JSON");
        public final static Property FailFeedback = new Property(3, String.class, "failFeedback", false, "FAIL_FEEDBACK");
        public final static Property Download = new Property(4, String.class, "download", false, "DOWNLOAD");
        public final static Property DeleteTag = new Property(5, String.class, "deleteTag", false, "DELETE_TAG");
    }


    public CodeJsonTaskDao(DaoConfig config) {
        super(config);
    }
    
    public CodeJsonTaskDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CODE_JSON_TASK\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"COMMAND_ID\" TEXT NOT NULL ," + // 1: commandId
                "\"JSON\" TEXT NOT NULL ," + // 2: json
                "\"FAIL_FEEDBACK\" TEXT NOT NULL ," + // 3: failFeedback
                "\"DOWNLOAD\" TEXT NOT NULL ," + // 4: download
                "\"DELETE_TAG\" TEXT NOT NULL );"); // 5: deleteTag
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CODE_JSON_TASK\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, CodeJsonTask entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getCommandId());
        stmt.bindString(3, entity.getJson());
        stmt.bindString(4, entity.getFailFeedback());
        stmt.bindString(5, entity.getDownload());
        stmt.bindString(6, entity.getDeleteTag());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, CodeJsonTask entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getCommandId());
        stmt.bindString(3, entity.getJson());
        stmt.bindString(4, entity.getFailFeedback());
        stmt.bindString(5, entity.getDownload());
        stmt.bindString(6, entity.getDeleteTag());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public CodeJsonTask readEntity(Cursor cursor, int offset) {
        CodeJsonTask entity = new CodeJsonTask( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // commandId
            cursor.getString(offset + 2), // json
            cursor.getString(offset + 3), // failFeedback
            cursor.getString(offset + 4), // download
            cursor.getString(offset + 5) // deleteTag
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, CodeJsonTask entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCommandId(cursor.getString(offset + 1));
        entity.setJson(cursor.getString(offset + 2));
        entity.setFailFeedback(cursor.getString(offset + 3));
        entity.setDownload(cursor.getString(offset + 4));
        entity.setDeleteTag(cursor.getString(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(CodeJsonTask entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(CodeJsonTask entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(CodeJsonTask entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
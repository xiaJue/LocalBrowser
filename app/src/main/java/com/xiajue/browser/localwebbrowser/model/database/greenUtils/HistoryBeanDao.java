package com.xiajue.browser.localwebbrowser.model.database.greenUtils;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.xiajue.browser.localwebbrowser.model.bean.HistoryBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "HISTORY_BEAN".
*/
public class HistoryBeanDao extends AbstractDao<HistoryBean, String> {

    public static final String TABLENAME = "HISTORY_BEAN";

    /**
     * Properties of entity HistoryBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property LastLoad = new Property(0, String.class, "lastLoad", true, "LAST_LOAD");
        public final static Property Title = new Property(1, String.class, "title", false, "TITLE");
        public final static Property Url = new Property(2, String.class, "url", false, "URL");
    }


    public HistoryBeanDao(DaoConfig config) {
        super(config);
    }
    
    public HistoryBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"HISTORY_BEAN\" (" + //
                "\"LAST_LOAD\" TEXT PRIMARY KEY NOT NULL ," + // 0: lastLoad
                "\"TITLE\" TEXT," + // 1: title
                "\"URL\" TEXT);"); // 2: url
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"HISTORY_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, HistoryBean entity) {
        stmt.clearBindings();
 
        String lastLoad = entity.getLastLoad();
        if (lastLoad != null) {
            stmt.bindString(1, lastLoad);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(2, title);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(3, url);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, HistoryBean entity) {
        stmt.clearBindings();
 
        String lastLoad = entity.getLastLoad();
        if (lastLoad != null) {
            stmt.bindString(1, lastLoad);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(2, title);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(3, url);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public HistoryBean readEntity(Cursor cursor, int offset) {
        HistoryBean entity = new HistoryBean( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // lastLoad
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // title
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // url
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, HistoryBean entity, int offset) {
        entity.setLastLoad(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setTitle(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setUrl(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    @Override
    protected final String updateKeyAfterInsert(HistoryBean entity, long rowId) {
        return entity.getLastLoad();
    }
    
    @Override
    public String getKey(HistoryBean entity) {
        if(entity != null) {
            return entity.getLastLoad();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(HistoryBean entity) {
        return entity.getLastLoad() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}

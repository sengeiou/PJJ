package com.pjj.xsp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.pjj.xsp.utils.Log;

import com.pjj.xsp.utils.SharedUtils;

import org.greenrobot.greendao.database.Database;

public class PjjDevOpenHelper extends DaoMaster.DevOpenHelper {
    public PjjDevOpenHelper(Context context, String name) {
        super(context, name);
    }

    public PjjDevOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
        //super.onUpgrade(db, oldVersion, newVersion);
        if (oldVersion < newVersion) {
            if (newVersion == 3) {
                String key = SharedUtils.getXmlForKey("cooperationMode");
                if (!"4".equals(key))
                    MigrationHelper.getInstance().migrate(db, DateTaskDao.class, TaskTextDao.class);
            } else if (newVersion == 4) {
                String key = SharedUtils.getXmlForKey("cooperationMode");
                if ("4".equals(key)) {
                    //MigrationHelper.getInstance().migrate(db, MinePlayTaskDao.class);
                } else {
                    MigrationHelper.getInstance().migrate(db, DateTaskDao.class, TaskTextDao.class);
                }
            } else if (newVersion == 5) {
                super.onUpgrade(db, oldVersion, newVersion);
//                String searchTable;
//                String searchIndex;
//                String
            }
        }
    }
}

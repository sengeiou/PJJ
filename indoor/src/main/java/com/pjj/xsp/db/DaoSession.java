package com.pjj.xsp.db;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.pjj.xsp.db.CodeJsonTask;
import com.pjj.xsp.db.DateTask;
import com.pjj.xsp.db.MinePlayTask;
import com.pjj.xsp.db.TaskText;

import com.pjj.xsp.db.CodeJsonTaskDao;
import com.pjj.xsp.db.DateTaskDao;
import com.pjj.xsp.db.MinePlayTaskDao;
import com.pjj.xsp.db.TaskTextDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig codeJsonTaskDaoConfig;
    private final DaoConfig dateTaskDaoConfig;
    private final DaoConfig minePlayTaskDaoConfig;
    private final DaoConfig taskTextDaoConfig;

    private final CodeJsonTaskDao codeJsonTaskDao;
    private final DateTaskDao dateTaskDao;
    private final MinePlayTaskDao minePlayTaskDao;
    private final TaskTextDao taskTextDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        codeJsonTaskDaoConfig = daoConfigMap.get(CodeJsonTaskDao.class).clone();
        codeJsonTaskDaoConfig.initIdentityScope(type);

        dateTaskDaoConfig = daoConfigMap.get(DateTaskDao.class).clone();
        dateTaskDaoConfig.initIdentityScope(type);

        minePlayTaskDaoConfig = daoConfigMap.get(MinePlayTaskDao.class).clone();
        minePlayTaskDaoConfig.initIdentityScope(type);

        taskTextDaoConfig = daoConfigMap.get(TaskTextDao.class).clone();
        taskTextDaoConfig.initIdentityScope(type);

        codeJsonTaskDao = new CodeJsonTaskDao(codeJsonTaskDaoConfig, this);
        dateTaskDao = new DateTaskDao(dateTaskDaoConfig, this);
        minePlayTaskDao = new MinePlayTaskDao(minePlayTaskDaoConfig, this);
        taskTextDao = new TaskTextDao(taskTextDaoConfig, this);

        registerDao(CodeJsonTask.class, codeJsonTaskDao);
        registerDao(DateTask.class, dateTaskDao);
        registerDao(MinePlayTask.class, minePlayTaskDao);
        registerDao(TaskText.class, taskTextDao);
    }
    
    public void clear() {
        codeJsonTaskDaoConfig.clearIdentityScope();
        dateTaskDaoConfig.clearIdentityScope();
        minePlayTaskDaoConfig.clearIdentityScope();
        taskTextDaoConfig.clearIdentityScope();
    }

    public CodeJsonTaskDao getCodeJsonTaskDao() {
        return codeJsonTaskDao;
    }

    public DateTaskDao getDateTaskDao() {
        return dateTaskDao;
    }

    public MinePlayTaskDao getMinePlayTaskDao() {
        return minePlayTaskDao;
    }

    public TaskTextDao getTaskTextDao() {
        return taskTextDao;
    }

}

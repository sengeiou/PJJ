package com.pjj.xsp.db;

import android.util.Log;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

@Entity
public class CodeJsonTask {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String commandId;
    @NotNull
    private String json;
    @NotNull
    private String failFeedback = "0";//1 true 上传反馈失败，已执行完
    @NotNull
    private String download = "0";//1 true 下载成功，上传反馈失败
    @NotNull
    private String deleteTag = "0";


    @Generated(hash = 477187826)
    public CodeJsonTask(Long id, @NotNull String commandId, @NotNull String json, @NotNull String failFeedback,
                        @NotNull String download, @NotNull String deleteTag) {
        this.id = id;
        this.commandId = commandId;
        this.json = json;
        this.failFeedback = failFeedback;
        this.download = download;
        this.deleteTag = deleteTag;
    }

    @Generated(hash = 783201499)
    public CodeJsonTask() {
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommandId() {
        return this.commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    public String getJson() {
        return this.json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getFailFeedback() {
        return this.failFeedback;
    }

    public void setFailFeedback(String failFeedback) {

        this.failFeedback = failFeedback;
    }

    public String getDownload() {
        return this.download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public static boolean checkIsHas(String commandId) {
        return HandleDaoDb.checkIsHas(CodeJsonTask.class, commandId, CodeJsonTaskDao.Properties.CommandId);
    }

    public boolean saveDb() {
        Long aLong = HandleDaoDb.insertBean(this, DaoManager.getInstance().getDaoSession().getCodeJsonTaskDao());
        return aLong > -1;
    }

    public void update() {
        HandleDaoDb.updateBean(this);
    }

    public static List<CodeJsonTask> filterFeedbackFail() {
        QueryBuilder<CodeJsonTask> queryBuilder = DaoManager.getInstance().getDaoSession().queryBuilder(CodeJsonTask.class);
        queryBuilder.where(CodeJsonTaskDao.Properties.FailFeedback.eq("1"));
        queryBuilder.where(CodeJsonTaskDao.Properties.DeleteTag.notEq("1"));
        List<CodeJsonTask> list = queryBuilder.list();
        return list;
    }

    public static void deleteCommandId() {
        Log.e("TAG", "deleteCommandId: " );
        QueryBuilder<CodeJsonTask> queryBuilder = DaoManager.getInstance().getDaoSession().queryBuilder(CodeJsonTask.class);
        queryBuilder.where(CodeJsonTaskDao.Properties.DeleteTag.eq("1"));
        DeleteQuery<CodeJsonTask> codeJsonTaskDeleteQuery = queryBuilder.buildDelete();
        codeJsonTaskDeleteQuery.executeDeleteWithoutDetachingEntities();
    }

    public String getDeleteTag() {
        return this.deleteTag;
    }

    public void setDeleteTag(String deleteTag) {
        this.deleteTag = deleteTag;
    }
}

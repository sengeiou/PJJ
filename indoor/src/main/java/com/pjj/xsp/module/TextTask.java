package com.pjj.xsp.module;

/**
 * Created by XinHeng on 2019/03/30.
 * describe：
 */
public class TextTask implements PlayTaskParent {
    private String filePath;
    private String fileType;
    private String templetType;
    private boolean deleteTag;

    public void setDeleteTag(boolean deleteTag) {
        this.deleteTag = deleteTag;
    }

    public TextTask(String filePath, String fileType, String templetType) {
        this.filePath = filePath;
        this.fileType = fileType;
        this.templetType = templetType;
    }

    @Override
    public String getTempletType() {
        return templetType;
    }

    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public String getVideoPath() {
        return null;
    }

    @Override
    public String getFileType() {
        return fileType;
    }

    @Override
    public int getPlayTime() {
        return 5;
    }

    @Override
    public String getTaskTag() {
        return "";
    }

    @Override
    public boolean deleteTag() {
        return deleteTag;
    }

}

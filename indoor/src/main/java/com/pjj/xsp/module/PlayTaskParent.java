package com.pjj.xsp.module;

/**
 * Created by XinHeng on 2019/03/26.
 * describe：
 */
public interface PlayTaskParent {
    /**
     * 获取文件路径
     * 默认为图片
     *
     * @return
     */
    String getFilePath();

    /**
     * 视频
     *
     * @return
     */
    String getVideoPath();

    /**
     * 获取文件类型
     *
     * @return
     */
    String getFileType();

    /**
     * 模板类型
     *
     * @return
     */
    String getTempletType();

    int getPlayTime();

    String getTaskTag();

    boolean deleteTag();
}

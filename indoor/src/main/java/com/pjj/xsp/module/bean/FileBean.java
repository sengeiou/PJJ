package com.pjj.xsp.module.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by XinHeng on 2019/03/28.
 * describe：
 */
public class FileBean {

    /**
     * fileName : dde712e5f14442538b491eafdb539f36.mp4
     * file_id : eca4b248f9db42e78ce7da49fd67920f
     * type : 2
     * filePlace : 1
     */

    private String fileName;
    private String file_id;
    @SerializedName(value = "type", alternate = {"fileType"})
    private String type;
    private String filePlace;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFile_id() {
        return file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFilePlace() {
        return filePlace;
    }

    public void setFilePlace(String filePlace) {
        this.filePlace = filePlace;
    }
}

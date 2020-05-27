package com.pjj.xsp.module.parameter;

import com.pjj.xsp.module.bean.BoardBean;

/**
 * Create by xinheng on 2018/10/31。
 * describe：
 */
public class SocketXspInf {
    private String operateType;
    private BoardBean boardBean;

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public BoardBean getBoardBean() {
        return boardBean;
    }

    public void setBoardBean(BoardBean boardBean) {
        this.boardBean = boardBean;
    }
}

package com.visitor.tengli.facepadlygc.fs;

import android.appwidget.AppWidgetManager;

/**
 * created by yangshaojie  on 2018/11/1
 * email: ysjr-2002@163.com
 */
public class SocketMessageBean {

    private int IDType;

    /// <summary>
    /// 验证状态 0:成功 1:失败
    /// </summary>
    private int Status;

    private String Avatar;

    private String Name;

    private String Message;

    public void setIDType(int IDType) {
        this.IDType = IDType;
    }

    public int getIDType() {
        return IDType;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getStatus() {
        return Status;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getMessage() {
        return Message;
    }
}

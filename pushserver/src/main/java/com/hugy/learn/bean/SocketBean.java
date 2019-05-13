package com.hugy.learn.bean;

import java.net.Socket;

/**
 * @author hugy
 */
public class SocketBean {
    private String uid;
    private String account;
    /**
     * 此 socket 进行分组, all 表示任何消息皆可以接收
     */
    private String group;
    private String phone;
    private Socket socket;
    private String state;
    /**
     * 此 socket 最近更新的时间， 便于将一定时间不操作的 socket 进行清理
     */
    private long timeUpdate;

    public SocketBean() {
    }

    public SocketBean(String uid, String account, String group, String phone, Socket socket, long timeUpdate) {
        this.uid = uid;
        this.account = account;
        this.group = group;
        this.phone = phone;
        this.socket = socket;
        this.timeUpdate = timeUpdate;
        this.state = "on";
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getUid() {
        return uid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getTimeUpdate() {
        return timeUpdate;
    }

    public void setTimeUpdate(long timeUpdate) {
        this.timeUpdate = timeUpdate;
    }
}

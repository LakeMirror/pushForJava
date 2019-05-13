package com.hugy.learn.databasebean;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author hugy
 * 记录离线而未推送成功的数据，此数据会定时清空（暂定每天晚上12:00）， 清空的数据应包括未发送但是创建时间已经超过24小时的数据
 * 和已经发送过的数据
 */
@Component
public class UnSendRecord {
    /**
     * 唯一标记
     */
    String number;
    /**
     * 手机端为每个手机用户分配的唯一编码
     */
    String uid;
    /**
     * 要发送的数据的格式 暂定 通知方式☆通知类型☆内容
     */
    String message;
    /**
     * 创建此条记录的时间
     */
    Date timeCreate;
    /**
     * 状态 0 未发送 1 已发送
     */
    String state;

    public UnSendRecord() {
    }

    public UnSendRecord(String number, String uid, String message, Date timeCreate, String state) {
        this.number = number;
        this.uid = uid;
        this.message = message;
        this.timeCreate = timeCreate;
        this.state = state;
    }

    public UnSendRecord(String number, String uid) {
        this.number = number;
        this.uid = uid;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimeCreate() {
        return timeCreate;
    }

    public void setTimeCreate(Date timeCreate) {
        this.timeCreate = timeCreate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

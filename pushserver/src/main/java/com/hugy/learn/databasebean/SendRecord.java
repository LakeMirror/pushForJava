package com.hugy.learn.databasebean;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SendRecord {

    private String uid;
    private String account;
    private String message;

    public SendRecord(String uid, String account, String message) {
        this.uid = uid;
        this.account = account;
        this.message = message;
    }

    public SendRecord() {
    }

    public String getUid() {
        return uid;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

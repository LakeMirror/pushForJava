package com.hugy.learn.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author hugy
 */
@Component
public class PushModel implements Serializable {
    /**
     * 推送的方式
     * noti 通知栏显示
     * app 利用 app 的弹窗
     */
    private String notifyMethod;
    /**
     * 通知的重要程度
     * error
     * warn
     * normal
     * ...
     */
    private String level;
    /**
     * 通知给哪些组的用户
     */
    private String group;
    /**
     * 消息
     */
    private String message;

    public PushModel() {
    }

    public PushModel(String notifyMethod, String level, String group, String message) {
        this.notifyMethod = notifyMethod;
        this.level = level;
        this.group = group;
        this.message = message;
    }

    public String getNotifyMethod() {
        return notifyMethod;
    }

    public void setNotifyMethod(String notifyMethod) {
        this.notifyMethod = notifyMethod;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

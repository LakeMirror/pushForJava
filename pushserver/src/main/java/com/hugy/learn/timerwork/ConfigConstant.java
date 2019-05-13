package com.hugy.learn.timerwork;

/**
 * @author hugy
 */
public interface ConfigConstant {
    /**
     * 重新发起消息推送的间隔频率
     */
    int TIME_RESEND_FREQUENT = 10 * 1000;
    /**
     * 清理闲置 SOCKET 的时间间隔
     */
    int TIME_CLEAR_SOCKET_DEAD = 60 * 10000;
    /**
     * 判断多长时间没动静的 Socket 算是死亡
     */
    int TIME_JUDGE_SOCKET_DEAD = 10 * 1000;
}

package com.hugy.learn.timerwork;

import com.hugy.learn.App;
import com.hugy.learn.databasebean.UnSendRecord;
import org.apache.ibatis.session.SqlSession;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component注解用于对那些比较中立的类进行注释；
//相对与在持久层、业务层和控制层分别采用 @Repository、@Service 和 @Controller 对分层中的类进行注释
@Component
@EnableScheduling   // 1.开启定时任务
@EnableAsync        // 2.开启多线程
public class RecordTimer {


    @Async
    @Scheduled(fixedDelay = ConfigConstant.TIME_RESEND_FREQUENT)
    public void reSendMessage() {
        SqlSession sqlSession = App.sqlSession();
        String statement = "com.hugy.learn.unsend.selectUnSendRecord";
        List<UnSendRecord> unSendRecords = sqlSession.selectList(statement);
        App.sendMessage(unSendRecords);
    }

    @Async
    @Scheduled(fixedDelay = ConfigConstant.TIME_CLEAR_SOCKET_DEAD)
    public void cleanSocket() {
        App.clearSocket();
    }

}

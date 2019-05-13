package com.hugy.learn.service;

import com.hugy.learn.App;
import com.hugy.learn.databasebean.UnSendRecord;
import com.hugy.learn.utils.MapUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SocketService {

    public void restoreOffLineUid(List<UnSendRecord> uids, String message) {
        if (uids.size() <= 0) return;
        String statement = "com.hugy.learn.unsend.insertUnSendRecord";
        Map param = MapUtils.asMap("records", uids, "message", message);
        SqlSession sqlSession = App.sqlSession();
        sqlSession.insert(statement, param);
        sqlSession.commit();
        sqlSession.close();
    }

}

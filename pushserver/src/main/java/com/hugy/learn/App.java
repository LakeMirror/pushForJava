package com.hugy.learn;

import com.hugy.learn.bean.PushModel;
import com.hugy.learn.databasebean.SendRecord;
import com.hugy.learn.bean.SocketBean;
import com.hugy.learn.constant.SocketConstant;
import com.hugy.learn.databasebean.UnSendRecord;
import com.hugy.learn.service.SocketService;
import com.hugy.learn.timerwork.ConfigConstant;
import com.hugy.learn.utils.MapUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.n3r.idworker.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.*;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.*;

@SpringBootApplication
@RestController
public class App {
    @Autowired
    SocketService mService;

    private static List<SocketBean> list = new ArrayList();

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        startSocketServer();
    }

    public static void startSocketServer() {
        try {
            ServerSocket mSocket = new ServerSocket(9998);
            while (true) {
                Socket socket = mSocket.accept();
                InputStream is = socket.getInputStream();
                byte[] bts = new byte[1024];
                is.read(bts);
                System.out.println(new String(bts));
                String ids = new String(bts);
                saveSocket(ids, socket);
                ReceiveThread receiveThread = new ReceiveThread(socket);
                receiveThread.start();
//                changeState(new String(bts), "on", socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存此 socket
     *
     * @param ids 格式 唯一标识☆分组☆用户名☆电话
     *            如果 uid 相同或者 账户名 手机号相同 可以确认是同一个人， 只需要更新原先 bean 即可
     */
    private static void saveSocket(String ids, Socket socket) {
        String[] arr = ids.split(SocketConstant.SEPARATOR);
        if (arr.length >= 3) {
            boolean hasRecord = false;
            SocketBean bean = new SocketBean(arr[0], arr[2], arr[1], arr[3], socket, System.currentTimeMillis());
            for (int position = 0; position < list.size(); position ++) {
                SocketBean item = list.get(position);
                if ((item.getUid().equals(arr[0]))
                        || (item.getAccount().equals(arr[2]) && item.getPhone().equals(arr[3]))) {
                    list.set(position, bean);
                    hasRecord = true;
                    return;
                }
            }
            if (!hasRecord) {
                list.add(bean);
            }
        }
    }

    public static void sendMessage(List<UnSendRecord> unSendRecords) {
        List<String> deleteUid = new ArrayList<>();
        List<SendRecord> sendRecords = new ArrayList<>();
        try {
            for (UnSendRecord record : unSendRecords) {
                for (SocketBean entry : list) {
//                    String key = entry.getUid().toString();
                    // 设备在线
                    if ("on".equals(entry.getState()) && entry.getUid().contains(record.getUid())) {
                        Socket socket = entry.getSocket();
                        OutputStream outputStream = socket.getOutputStream();
                        outputStream.write(record.getMessage().getBytes());
                        outputStream.flush();
                        deleteUid.add(record.getNumber());
                        sendRecords.add(new SendRecord(entry.getUid(), entry.getAccount(), record.getMessage()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            removeSendRecords(deleteUid);
            doPushRecord(sendRecords);
        }
    }

    public static void clearSocket() {
        Iterator<SocketBean> iterator = list.iterator();
        while (iterator.hasNext()) {
            SocketBean bean = iterator.next();
            if ("off".equals(bean.getState()) || bean.getTimeUpdate() < System.currentTimeMillis() - ConfigConstant.TIME_JUDGE_SOCKET_DEAD) {
                iterator.remove();
            }
        }
    }

    @RequestMapping(value = "/push", method = RequestMethod.POST)
    public void push(@RequestBody PushModel param) {
        formatParam(param);
        List<UnSendRecord> offUid = new ArrayList<>();
        String message = (param.getNotifyMethod() + SocketConstant.SEPARATOR + param.getLevel()
                + SocketConstant.SEPARATOR + param.getMessage());
        List<SendRecord> sendRecords = new ArrayList<>();
        try {
            for (SocketBean entry : list) {
                // 如果分组信息不是 all 或者 包含要发的分组， 则不给这个人发送信息
                if (!("all".equals(entry.getGroup()) || entry.getGroup().contains(param.getGroup()))) {
                    continue;
                }
                if ("off".equals(entry.getState())) {
                    offUid.add(new UnSendRecord(Id.next() + "", entry.getUid()));
                    continue;
                }
                Socket socket = entry.getSocket();
                if (socket.isConnected()) {
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(message.getBytes());
                    outputStream.flush();
                    sendRecords.add(new SendRecord(entry.getUid(), entry.getAccount(), message));
                } else {
                    changeState(entry.getUid(), "off");
                    offUid.add(new UnSendRecord(Id.next() + "", entry.getUid()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mService.restoreOffLineUid(offUid, message);
            doPushRecord(sendRecords);
        }
    }
    public static void doPushRecord(List<SendRecord> records) {
        if (records.size() <= 0) return;
        String statement = "com.hugy.learn.record.pushRecord";
        Map params = MapUtils.asMap("records", records);
        SqlSession sqlSession = App.sqlSession();
        sqlSession.insert(statement, params);
        sqlSession.commit();
        sqlSession.close();
    }
    private void formatParam(PushModel param) {
        if ("".equals(param.getNotifyMethod()) || param.getNotifyMethod() == null) {
            param.setNotifyMethod(SocketConstant.METHOD_NOTIFICATION);
        }
        if ("".equals(param.getLevel()) || param.getLevel() == null) {
            param.setLevel(SocketConstant.LEVEL_NORMAL);
        }
        if (param.getMessage() == null) {
            param.setMessage("");
        }
    }

    @RequestMapping(value = "/offline/{uid}", method = RequestMethod.GET)
    public void offLine(@PathVariable String uid) {
        changeState(uid, "off");
    }

    public static class ReceiveThread extends Thread {
        public Socket mSocket;

        public ReceiveThread(Socket mSocket) {
            this.mSocket = mSocket;
        }

        @Override
        public void run() {
            try {
                if (mSocket != null) {
                    InputStream inputStream = mSocket.getInputStream();
                    byte[] src = new byte[1024];
                    inputStream.read(src);
                    System.out.println(src.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void changeState(String uid, String state) {
        try {
            Iterator<SocketBean> keys = list.iterator();
            while (keys.hasNext()) {
                SocketBean bean = keys.next();
                String[] arr = uid.split(SocketConstant.SEPARATOR);
                if (bean.getUid().trim().equals(arr[0].trim())) {
                    bean.setState(state);
                    bean.setTimeUpdate(System.currentTimeMillis());
                    if ("off".equals(state)) {
                        bean.setSocket(null);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SqlSession sqlSession() {
        InputStream inputStream = App.class.
                getClassLoader().getResourceAsStream("mybatis-configuration.xml");

        SqlSessionFactory sqlSessionFactory =
                new SqlSessionFactoryBuilder().build(inputStream);

        return sqlSessionFactory.openSession();
    }

    public static void removeSendRecords(List<String> deleteUid) {
        if (deleteUid.size() == 0) {
            return;
        }
        String statement = "com.hugy.learn.unsend.deleteRecords";
        Map param = MapUtils.asMap("records", deleteUid);
        SqlSession sqlSession = App.sqlSession();
        sqlSession.delete(statement, param);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void test() {
        try {
            URL url = new URL("http://localhost:8888/test/push");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");
            OutputStream os = connection.getOutputStream();
            os.write("{\"notifyMethod\": \"noti\", \"level\": \"warn\", \"group\":\"all\",\"message\":\"test\"}".getBytes());
            os.flush();
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (200 == responseCode) {
                System.out.println("OK");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

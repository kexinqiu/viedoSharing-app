package com.pilipili.app.service.websocket;

import com.alibaba.fastjson.JSONObject;

import com.pilipili.app.domain.Danmu;
import com.pilipili.app.domain.constant.UserMomentsConstant;
import com.pilipili.app.service.DanmuService;
import com.pilipili.app.service.util.RocketMQUtil;
import com.pilipili.app.service.util.TokenUtil;
import io.netty.util.internal.StringUtil;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


@Component
@ServerEndpoint("/imserver/{token}")
public class WebSocketService {

    //日志
    private final Logger logger =  LoggerFactory.getLogger(this.getClass());

    //线程有可能不安全，原子性实体类为了保证线程安全
    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);

    //为了保证线程安全，用于存储数据
    //多例模式，每个客户端唯一标识key对应一个websocket服务
    public static final ConcurrentHashMap<String, WebSocketService> WEBSOCKET_MAP = new ConcurrentHashMap<>();

    //服务端与客户端的对话
    private Session session;

    private String sessionId;

    private Long userId;

    private static ApplicationContext APPLICATION_CONTEXT;

    //websocket是多例模式，springboot是单例模式
    //因此用autowired注入时，springboot只会注入一次
    //用于解决多例模式注入
    public static void setApplicationContext(ApplicationContext applicationContext){
        WebSocketService.APPLICATION_CONTEXT = applicationContext;
    }

    @OnOpen
    public void openConnection(Session session, @PathParam("token") String token){
        System.out.println("onopen");
        try{
            this.userId = TokenUtil.verifyToken(token);
        }catch (Exception ignored){}
        this.sessionId = session.getId();
        this.session = session;
        if(WEBSOCKET_MAP.containsKey(sessionId)){
            WEBSOCKET_MAP.remove(sessionId);
            WEBSOCKET_MAP.put(sessionId, this);
        }else{
            WEBSOCKET_MAP.put(sessionId, this);
            ONLINE_COUNT.getAndIncrement();
        }
        logger.info("用户连接成功：" + sessionId + "，当前在线人数为：" + ONLINE_COUNT.get());
        try{
            this.sendMessage("0");
        }catch (Exception e){
            logger.error("连接异常");
        }
    }

    @OnClose
    public void closeConnection(){
        if(WEBSOCKET_MAP.containsKey(sessionId)){
            WEBSOCKET_MAP.remove(sessionId);
            ONLINE_COUNT.getAndDecrement();
        }
        logger.info("用户退出：" + sessionId + "当前在线人数为：" + ONLINE_COUNT.get());
    }

    @OnMessage
    public void onMessage(String message){
        logger.info("用户信息：" + sessionId + "，报文：" + message);
        if(!StringUtil.isNullOrEmpty(message)){
            try{
                //群发消息
                for(Map.Entry<String, WebSocketService> entry : WEBSOCKET_MAP.entrySet()){
                    //获得了每一个和相连的客户端对应的service
                    WebSocketService webSocketService = entry.getValue();
                    DefaultMQProducer danmusProducer = (DefaultMQProducer)APPLICATION_CONTEXT.getBean("danmusProducer");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("message", message);
                    jsonObject.put("sessionId", webSocketService.getSessionId());
                    Message msg = new Message(UserMomentsConstant.TOPIC_DANMUS, jsonObject.toJSONString().getBytes(StandardCharsets.UTF_8));
                    RocketMQUtil.asyncSendMsg(danmusProducer, msg);
                }
                if(this.userId != null){
                    //保存弹幕到数据库
                    Danmu danmu = JSONObject.parseObject(message, Danmu.class);
                    danmu.setUserId(userId);
                    danmu.setCreateTime(new Date());
                    //多例模式调用
                    DanmuService danmuService = (DanmuService)APPLICATION_CONTEXT.getBean("danmuService");
                    //异步请求数据库
                    danmuService.asyncAddDanmu(danmu);

                    //保存弹幕到redis
                    danmuService.addDanmusToRedis(danmu);
                }
            }catch (Exception e){
                logger.error("弹幕接收出现问题");
                e.printStackTrace();
            }
        }
    }

    @OnError
    public void onError(Throwable error){
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    //或直接指定时间间隔，例如：5秒
    @Scheduled(fixedRate=5000)
    private void noticeOnlineCount() throws IOException {
        for(Map.Entry<String, WebSocketService> entry : WebSocketService.WEBSOCKET_MAP.entrySet()){
            WebSocketService webSocketService = entry.getValue();
            if(webSocketService.session.isOpen()){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("onlineCount", ONLINE_COUNT.get());
                jsonObject.put("msg", "当前在线人数为" + ONLINE_COUNT.get());
                webSocketService.sendMessage(jsonObject.toJSONString());
            }
        }
    }

    public Session getSession() {
        return session;
    }

    public String getSessionId() {
        return sessionId;
    }
}

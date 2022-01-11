package com.dev.utils;

import com.dev.Persist;
import com.dev.objects.Sale;
import com.dev.objects.Store;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class MessagesHandler extends TextWebSocketHandler {

    private static List<WebSocketSession> sessionList = new CopyOnWriteArrayList<>();
    private static Map<String ,WebSocketSession> sessionMap = new HashMap<>();
    private static Persist persist;


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        Map<String, String> map = Utils.splitQuery(session.getUri().getQuery());
        sessionMap.put(map.get("token"),session);
        sessionList.add(session);
        System.out.println(sessionMap.get("token")+session.toString());
        System.out.println("afterConnectionEstablished");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        System.out.println("handleTextMessage");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        sessionList.remove(session);
        System.out.println("afterConnectionClosed");

    }

//    public void sendSaleToUsers(){
//        WebSocketSession session = sessionMap.get(token);
//        if (session != null) {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("saleText", saleText);
//            jsonObject.put("sOe",sOe);
//            try {
//                session.sendMessage(new TextMessage(jsonObject.toString()));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        } else {
//            System.out.println("no token " + token);
//        }
//    }


@PostConstruct
    public void init () {
        new Thread(() -> {
            while (true) {
                try {
                    sendNewNotification();
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
   }
        public void sendNewNotification () {
        for (WebSocketSession session : sessionList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("test","haha");
            try {
                session.sendMessage(new TextMessage(jsonObject.toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
package com.dev.utils;

import com.dev.Persist;
import com.dev.objects.Sale;
import com.dev.objects.Store;
import com.dev.objects.UserObject;
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
    private static List<UserObject> userObjectList;
    private static List<Sale> startSales;
    private static List<Sale> endSales;


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

    public void sendStartSaleToUsers(){
        WebSocketSession session = sessionMap.get("CE8724150E6FDCAD631F5432D5C9DC3D");
       List<UserObject> userObjectList = persist.getUsersToSendStartSales();
        for (UserObject userObject : userObjectList)
            session= sessionMap.get(userObject.getToken());
        JSONObject jsonObject = new JSONObject();
        List <Sale>startSales=persist.getStartSales();
        if (session != null) {
            for(Sale sale:startSales) {
                jsonObject.put("saleText", sale.getSaleText());
                jsonObject.put("sOe", "START");
            }
            try {
                session.sendMessage(new TextMessage(jsonObject.toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("no token");
        }
    }

//
@PostConstruct
    public void init () {
        new Thread(() -> {
            while (true) {
                try {
                    sendStartSaleToUsers();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
   }
//        public void sendNewNotification () {
//        for (WebSocketSession session : sessionList) {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("test","haha");
//            try {
//                session.sendMessage(new TextMessage(jsonObject.toString()));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
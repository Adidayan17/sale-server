package com.dev.utils;

import com.dev.Persist;
import com.dev.objects.Organizations;
import com.dev.objects.Sale;
import com.dev.objects.Store;
import com.dev.objects.UserObject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class MessagesHandler extends TextWebSocketHandler {

    private static List<WebSocketSession> sessionList = new CopyOnWriteArrayList<>();
    private static Map<String ,WebSocketSession> sessionMap = new HashMap<>();

    @Autowired
    private  Persist persist;
    private  List<UserObject> userObjectList;
    private  List<Sale> startSales;
    private  List<Sale> endSales;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        Map<String, String> map = Utils.splitQuery(session.getUri().getQuery());
        sessionMap.put(map.get("token"),session);
        sessionList.add(session);
        System.out.println(sessionMap.get("token") + session.toString());
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


    public void sendSale (){
//        List<JSONObject> jsonObjectList = new ArrayList<>();
        List<Sale> startSales = persist.getStartSales();
        List<UserObject> userObjects=null ;
        List<Organizations> organizations=persist.getAllOrganizations();
        if (startSales!=null) {
            for (Sale start : startSales) {
                if (start.getAvailableForAll() != 1) {
                    for (Organizations organization : organizations) {
                        if (persist.doseStoreBelongToOrganization(start.getStore().getId(), organization.getId())) {
                            userObjects = persist.getUserByOrganizationId(organization.getId());
                            if (userObjects != null) {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("saleText", start.getSaleText());
                                jsonObject.put("sOe", "START");// to fix
                                // jsonObjectList.add(jsonObject);
                                for (UserObject userObject : userObjects)
                                    sessionList.add(sessionMap.get(userObject.getToken()));
                                for(WebSocketSession session:sessionList)
                                    try {
                                        if(session!=null)
                                        session.sendMessage(new TextMessage(jsonObject.toString()));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                            }

                        }

                    }

                }
            }
        }   else {
            System.out.println("no start sale now");
        }
        }


    public void sendStartSaleToUsers() {
        List<JSONObject> jsonObjectList = new ArrayList<>();
        List<UserObject> userObjectList = new ArrayList<>();
        List<Sale> startSales1 = new ArrayList<>();
//        if (persist != null) {
//            startSales1 = persist.getStartSales();
//            userObjectList = persist.getUsersToSendStartSales();
//        }
        if (userObjectList != null) {
            {
                for (UserObject userObject : userObjectList)
                    sessionList.add(sessionMap.get(userObject.getToken()));
            }
            for (WebSocketSession session : sessionList)
                if (session != null) {
                    if (startSales1 != null) {
                        for (Sale sale : startSales1) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("saleText", sale.getSaleText());
                            jsonObject.put("sOe", "START");
                            jsonObjectList.add(jsonObject);
                        }
                    }
                    try {
                        if (jsonObjectList.size() > 0) {
                            for (JSONObject jsonObject : jsonObjectList) {
                                session.sendMessage(new TextMessage(jsonObject.toString()));
                            }
                        } else {
                            System.out.println("no start sale now");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
        } else {
            System.out.println("no token");
        }
    }

    public void sendEndSaleToUsers() {
        List<JSONObject> jsonObjectList = new ArrayList<>();
        List<UserObject> userObjectList = new ArrayList<>();
        List<Sale> endSales1 = new ArrayList<>();
        if (persist != null) {
            endSales1 = persist.getEndSales();
            userObjectList = persist.getUsersToSendEndSales();
        }
        if (userObjectList != null) {
            {
                for (UserObject userObject : userObjectList)
                    sessionList.add(sessionMap.get(userObject.getToken()));
            }
            for (WebSocketSession session : sessionList)
                if (session != null) {
                    if (endSales1 != null) {
                        for (Sale sale : endSales1) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("saleText", sale.getSaleText());
                            jsonObject.put("sOe", "Expired");
                            jsonObjectList.add(jsonObject);
                        }
                    }
                    try {
                        if (jsonObjectList.size() > 0) {
                            for (JSONObject jsonObject : jsonObjectList) {
                                session.sendMessage(new TextMessage(jsonObject.toString()));
                            }
                        } else {
                            System.out.println("no end sale now");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
        } else {
            System.out.println("no token");
        }
    }

@PostConstruct
    public void init () {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(10000);
//                    sendStartSaleToUsers();
//                    sendEndSaleToUsers();
                    sendSale();
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
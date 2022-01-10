package com.dev.controllers;

import com.dev.Persist;
import com.dev.objects.Organizations;
import com.dev.objects.Sale;
import com.dev.objects.Store;
import com.dev.objects.UserObject;
import com.dev.utils.MessagesHandler;
import com.dev.utils.Utils;
import org.hibernate.engine.jdbc.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@RestController
public class TestController {
    @Autowired
    private Persist persist;
    private MessagesHandler messagesHandler;
    @PostConstruct
    private void init () {



    }
    @RequestMapping(value ="add-user" , method = RequestMethod.POST)
    public boolean addUser (String username ,String password){
       return persist.addUser(username,password);
    }
    @RequestMapping(value = "log-in")
    public String logIn (String username , String password){
        return persist.logIn(username,password);
    }
    @RequestMapping(value = "if-first-log-in")
    public boolean doseFirstLogIn (String token){
        return persist.firstLogIn(token);
    }
    @RequestMapping(value = "get-organizations")
    public List<Organizations> getOrganizations (){
        return persist.gatOrganizations();
    }
   @RequestMapping (value = "get-sales-by-user")
    public List<Sale> getSalesForUser (String token){
        return persist.getSaleForUser(token);
   }
   @RequestMapping (value = "change-setting")
    public void changeSetting (String token , int organizationId){
        persist.changeSettingForUserAndOrganization( token , organizationId);
    }
    @RequestMapping(value = "get-sales-by-store-id")
    public List<Sale> getSaleForStore (int storeId){
        return persist.getSalesForStore(storeId);
    }
   @RequestMapping(value = "get-store-name-by-store-id")
    public String getStoreNameById (int storeId){
        return persist.getStoreById(storeId).getStoreName();
   }
   @RequestMapping(value = "if-sale-belong-to-user")
    public boolean doseSaleBelongToUser (String token , int saleId){
        return persist.doseSaleBelongToUser(token,saleId);
   }
   @RequestMapping(value = "get-all-stores")
    public List<Store> getStores (){
        return persist.getStores();
   }
   @RequestMapping(value = "if-user-belong-to-organization")
    public boolean doseUserBelongToOrganization (String token , int organizationId){
      return persist.doseUserBelongToOrganization(token,organizationId);
   }
}

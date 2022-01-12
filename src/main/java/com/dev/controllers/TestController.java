package com.dev.controllers;

import com.dev.Persist;
import com.dev.objects.Organizations;
import com.dev.objects.Sale;
import com.dev.objects.Store;
import com.dev.objects.UserObject;
import com.dev.utils.MessagesHandler;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;


import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
public class TestController {
    @Autowired
    private Persist persist;
    private MessagesHandler messagesHandler;


    public TestController(MessagesHandler messagesHandler) {
        this.messagesHandler = messagesHandler;
    }

    @PostConstruct
    private void init () {



    }
    @RequestMapping(value ="add-user" , method = RequestMethod.POST)
    public boolean addUser (@RequestParam String username ,String password){
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
    @RequestMapping(value = "inc_first_log_in",method = RequestMethod.POST )
    public void incFirstLogIn (@RequestParam String token){
        persist.incFirstLogIn(token);

    }
    @RequestMapping(value = "get-organizations")
    public List<Organizations> getOrganizations (){
        return persist.gatOrganizations();
    }
   @RequestMapping (value = "get-sales-by-user")
    public List<Sale> getSalesForUser (String token){
        return persist.getSaleForUser(token);
   }

    @RequestMapping (value = "get-all-sales")
    public List<Sale> getAllSalesByToken (String token){
        return persist.getAllSalesByToken(token);
    }
   @RequestMapping (value = "change-setting" )
    public boolean changeSetting ( String token , int organizationId){
      return   persist.changeSettingForUserAndOrganization( token , organizationId);
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

   @RequestMapping(value = "get-organization-for-user")
    public List<Organizations> gatOrganizationsForUser (String token){
        return persist.gatOrganizationsForUser(token);
   }


   @RequestMapping(value = "get-start-sales")
    public List<Sale> getStartSales (){
        return persist.getStartSales();
    }

    @RequestMapping(value = "get-users-to-send-start-sales")
    public List<UserObject> getUsersToSendStartSales (){
        return persist.getUsersToSendStartSales();
    }
    @RequestMapping(value = "get-users-to-send-end-sales")
    public List<UserObject> getUsersToSendEndSales (){
        return persist.getUsersToSendEndSales();
    }


   //    public void startSale () {
//        String sOe;
//        List<Sale> sales=persist.getAllSales();
//        for(Sale sale:sales){
//            if(Objects.equals(sale.getStartDate(), "11-1-2022")){
//                sOe="start";
//                messagesHandler.sendSaleToUser(sale.getSaleText(),sOe);}
//            if(Objects.equals(sale.getEndDate(), "11-1-2022")){
//                sOe="end";
//                messagesHandler.sendSaleToUser(sale.getSaleText(),sOe);}
//        }
//
//    }


}

package com.dev;

import com.dev.objects.*;
import com.dev.utils.Utils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class Persist {
    private final SessionFactory sessionFactory;
    private Connection connection;


    @Autowired
    public Persist(SessionFactory sf) {
        this.sessionFactory = sf;
    }

    @PostConstruct
    public void createConnectionToDatabase() {

        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sales?useSSL=false", "root", "1234");


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // sign up

    public boolean didUsernameAvailable(String username) {
        boolean availableName = true;
        Session session = sessionFactory.openSession();
        UserObject userObject = (UserObject) session.createQuery("FROM UserObject u WHERE u.username =:username")
                .setParameter("username", username)
                .uniqueResult();
        session.close();

        if (userObject != null) {
            availableName = false;
        }
        return availableName;
    }


    public boolean addUser(String username, String password) {
        boolean success = false;
        if (didUsernameAvailable(username)) {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            UserObject userObject = new UserObject(username, password, Utils.createHash(username, password));
            session.save(userObject);
            transaction.commit();
            session.close();
            if (userObject.getId() != 0) {
                success = true;

            }
        }
        return success;
    }

    // log in

    public String logIn(String username, String password) {
        Session session = sessionFactory.openSession();
        UserObject userObject = (UserObject) session.createQuery("FROM UserObject u WHERE u.username =:username AND u.password =:password")
                .setParameter("username", username)
                .setParameter("password", password)
                .uniqueResult();
        session.close();
        if (userObject != null)
            return userObject.getToken();
        else {
            return null;
        }
    }
    public boolean firstLogIn(String token) {
        UserObject userObject = getUserByToken(token);
        if (userObject.getFirstLogIn() == 0) {;
            return true;
        } else {
       return false;
        }
    }

    public void incFirstLogIn (String token){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        UserObject userObject = getUserByToken(token);
        int firstLogin = userObject.getFirstLogIn()+1;
        userObject.setFirstLogIn(firstLogin);
        session.saveOrUpdate(userObject);
        transaction.commit();
        session.close();
    }

    // get user

    public UserObject getUserByToken (String token ){
        Session session = sessionFactory.openSession();
        UserObject userObject = (UserObject) session.createQuery("FROM UserObject u WHERE u.token = :token")
                .setParameter("token",token)
                .uniqueResult();
        session.close();
      return userObject;
    }

    //get organization

    public Organizations getOrganizationById (int id){
        Session session = sessionFactory.openSession();
        Organizations organizations = (Organizations) session.createQuery("FROM Organizations o WHERE o.id =:id")
                .setParameter("id",id)
                .uniqueResult();
        session.close();
        return organizations;
    }

    //get store by id
    public Store getStoreById (int id){
        Session session = sessionFactory.openSession();
        Store store = (Store) session.createQuery("FROM Store s WHERE s.id =:id")
                .setParameter("id",id)
                .uniqueResult();
        session.close();
        return store;
    }

    // get all organizations
    public List<Organizations> gatOrganizations() {

        return sessionFactory.openSession().createQuery("FROM Organizations").list();

    }

    // get all stores
    public List<Store> getStores (){
        return sessionFactory.openSession().createQuery("FROM Store ").list();

    }

    // get organizations for user
    public List<Organizations> gatOrganizationsForUser (String  token){
         Session session =sessionFactory.openSession();
        List <Organizations> organizations = session.createQuery("SELECT organizations FROM OrganizationUser o WHERE o.userObject.id=:id ")
                .setParameter("id",getUserByToken(token).getId())
                .list();
        session.close();
        return organizations;
    }

    //get stores for organizations
    public List<Store> getStoresForOrganization (int organizationId){
        Session session = sessionFactory.openSession();
        List <Store> stores = session.createQuery("SELECT store FROM OrganizationStore o WHERE o.organizations.id=:id")
                .setParameter("id",organizationId)
                .list();
        session.close();
        return stores;
    }

    // get sales for store
    public List<Sale> getSalesForStore (int storeId){
        return sessionFactory.openSession().createQuery("FROM Sale s where s.store.id=:id")
                .setParameter("id",storeId)
                .list();

    }
    //get all sales for a user

    public List<Sale> getSaleForUser  (String token)
    {
        Session session = sessionFactory.openSession();
        List<Sale> sales = new ArrayList<>();
        List<Organizations> organizations = gatOrganizationsForUser(token);
        for (Organizations organization : organizations ){
            List<Store> stores = getStoresForOrganization(organization.getId());
            for (Store store : stores) {
                sales.addAll(getSalesForStore(store.getId()));
            }

        }
        session.close();
        return sales;
    }
//All sales for search page
    public List<Sale> getAllSalesByToken  ()
    {
        return sessionFactory.openSession().createQuery("FROM Sale s ")
                .list();
    }
    public List<Sale> getAllSales ()
    {
        return sessionFactory.openSession().createQuery("FROM Sale s ")
                .list();
    }
    public String getSaleText(){
        List<Sale> sales=getAllSales();
        String someText = "";
        for(Sale sale: sales){
            someText=sale.getSaleText();
        }return someText;
    }

    //remove user to organization
    public void removeUserFromOrganization (String token , int organizationId){

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        OrganizationUser  organizationUserToDelete = (OrganizationUser) session.createQuery(" FROM OrganizationUser o where o.userObject.id =:userId AND o.organizations.id =:organizationId")
                .setParameter("userId",getUserByToken(token).getId())
                .setParameter("organizationId",organizationId)
                .uniqueResult();
        OrganizationUser organizationUser = (OrganizationUser) session.load(OrganizationUser.class,organizationUserToDelete.getId());
        session.delete(organizationUser);
        transaction.commit();
        session.close();

    }
    // add user to organization
    public void addUserToOrganization (String token , int organizationId){

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        OrganizationUser organizationUser = new OrganizationUser(getOrganizationById(organizationId),getUserByToken(token));
        session.save(organizationUser);
        transaction.commit();
        session.close();



    }


    // change user from organization
    public boolean changeSettingForUserAndOrganization(String token , int organizationId){
        if (getUserByToken(token) != null) {

            if (doseUserBelongToOrganization(token, organizationId)) {

                removeUserFromOrganization(token, organizationId);
                return false;

            } else {
                addUserToOrganization(token, organizationId);
                return true;
            }
        }

        return false;


    }

    //dose store belong to organization
    public boolean doseStoreBelongToOrganization (int storeId , int organizationId){
       Session session = sessionFactory.openSession();
        Organizations organizations=(Organizations)session
                .createQuery("SELECT organizations FROM OrganizationStore o WHERE o.store.id =:storeId AND o.organizations.id =:organizationId")
                .setParameter("storeId",storeId)
                .setParameter("organizationId",organizationId)
                .uniqueResult();
        session.close();
        if (organizations!=null)
        {
           return true;
        }else {
            return false;
        }

    }
     // dose user belong to organization

    public boolean doseUserBelongToOrganization (String token , int organizationId){
        Session session = sessionFactory.openSession();
        Organizations organizations=(Organizations)session
                .createQuery("SELECT organizations FROM OrganizationUser o WHERE o.userObject.id =:userId AND o.organizations.id =:organizationId")
                .setParameter("userId",getUserByToken(token).getId())
                .setParameter("organizationId",organizationId)
                .uniqueResult();
        session.close();
        if (organizations!=null)
        {
            return true;
        }else {
            return false;
        }

    }

    //dose store belong to user
    public boolean doseStoreBelongToUser (String token ,int storeId ){
        Session session = sessionFactory.openSession();
       List <Organizations> organizations =session.createQuery("SELECT organizations FROM OrganizationStore o WHERE o.store.id=:id ")
                .setParameter("id",storeId)
                .list();
        session.close();
       for(Organizations organizations1:organizations){
        return doseUserBelongToOrganization(token,organizations1.getId());
    }return doseUserBelongToOrganization(token,0);} //doesnt work perfect!


    // if sale belong to user
    public boolean doseSaleBelongToUser (String token , int saleId) {
        Session session= sessionFactory.openSession();
        Store store = (Store) session.createQuery("SELECT store FROM Sale s WHERE s.id=:id")
                .setParameter("id",saleId)
                .uniqueResult();
        session.close();
        return doseStoreBelongToUser(token, store.getId());
    }


   // list of users

    public List<UserObject> getUsersToSendStartSales() {
     List<UserObject> userObjectList=new ArrayList<>();
     List<Organizations> organizations=getAllOrganizations();
     List<Sale> startSales=getStartSales();
     for(Sale start:startSales){
         if(start.getAvailableForAll()==1){userObjectList=getAllUsers(); return userObjectList;}
         for (Organizations organizations1:organizations){
             if(doseStoreBelongToOrganization(start.getStore().getId(),organizations1.getId())){
                 /// send message
                 userObjectList=getUserByOrganizationId(organizations1.getId());
             }
         }
     }  return userObjectList;
    }
    public List<UserObject> getUsersToSendEndSales() {
        List<UserObject> userObjectList=new ArrayList<>();
        List<Organizations> organizations=getAllOrganizations();
        List<Sale> endSales=getEndSales();
        for(Sale end:endSales){
            if(end.getAvailableForAll()==1){userObjectList=getAllUsers(); return userObjectList;}
            for (Organizations organizations1:organizations){
                if(doseStoreBelongToOrganization(end.getStore().getId(),organizations1.getId())){
                    userObjectList=getUserByOrganizationId(organizations1.getId());
                }
            }
        }  return userObjectList;
    }
    public List<UserObject> getAllUsers() {
        {
            return sessionFactory.openSession().createQuery("FROM UserObject u ")
                    .list();
        }}

    public List<UserObject> getUserByOrganizationId(int organizationId){
        {   Session session = sessionFactory.openSession();
            List<UserObject> userObjectList =sessionFactory.openSession().createQuery("SELECT userObject FROM OrganizationUser u WHERE u.organizations.id=:id").setParameter("id",organizationId)
                    .list();
            session.close();
            return userObjectList;
        }
    }
    public List<Organizations> getAllOrganizations ()
    {
        return sessionFactory.openSession().createQuery("FROM Organizations o ")
                .list();
    }
    public List<Sale> getStartSales(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH");
        Date date = new Date();
        String currentDate = formatter.format(date);
        Session session = sessionFactory.openSession();
        List <Sale> sales =session.createQuery("FROM Sale s WHERE s.startDate=:currentDate")
                .setParameter("currentDate",currentDate)
                .list();
        session.close();
        return sales;
    }
    public List<Sale> getEndSales(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH");
        Date date = new Date();
        String currentDate = formatter.format(date);
        Session session = sessionFactory.openSession();
        List <Sale> sales =session.createQuery("FROM Sale s WHERE s.endDate=:currentDate ")
                .setParameter("currentDate",currentDate)
                .list();
        session.close();
        return sales;
    }


}



















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
import java.util.ArrayList;
import java.util.List;

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
                    "jdbc:mysql://localhost:3306/sales?SSL=false", "root", "1234");


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // sign up

    public boolean didUsernameAvailable (String username ){
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


    public boolean addUser (String username , String password ){
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

    public String logIn (String username , String password){

        Session session = sessionFactory.openSession();
        UserObject userObject = (UserObject) session.createQuery("FROM UserObject u WHERE u.username =:username AND u.password =:password")
                .setParameter("username",username)
                .setParameter("password",password)
                .uniqueResult();
        session.close();
        if (userObject!= null) {
          return userObject.getToken();
        }else {
          return null;
        }

    }

    public boolean firstLogIn (String token){
        if (getUserByToken(token).getFirstLogIn()!=0 ){
            return false ;
        }else {
            UserObject userObject = getUserByToken(token);
            userObject.setFirstLogIn(1);
            return true;

        }
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
    public List<Organizations> gatOrganizationsForUser (int userId){
         Session session =sessionFactory.openSession();
        List <Organizations> organizations = session.createQuery("SELECT organizations FROM OrganizationUser o WHERE o.userObject.id=:id ")
                .setParameter("id",userId)
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
        List<Organizations> organizations = gatOrganizationsForUser(getUserByToken(token).getId());
        for (Organizations organization : organizations ){
            List<Store> stores = getStoresForOrganization(organization.getId());
            for (Store store : stores) {
                sales.addAll(getSalesForStore(store.getId()));
            }

        }
        session.close();
        return sales;
    }

    //remove user to organization
    public void removeUserFromOrganization (String token , int organizationId){

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        OrganizationUser  organizationUserToDelete = (OrganizationUser) session.createQuery("SELECT organizations FROM OrganizationUser O  where O.userObject.id =:userId AND o.organizations.id=:organizationId")
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
    public void changeSettingForUserAndOrganization(String token , int organizationId){

         if (doseUserBelongToOrganization(token,organizationId)){

            removeUserFromOrganization(token,organizationId);

         }else {
             addUserToOrganization(token, organizationId);
         }


    }



    //dose store belong to organization
    public boolean doseStoreBelongToOrganization (int storeId , int organizationId){
       Session session = sessionFactory.openSession();
        Organizations organizations=(Organizations)session
                .createQuery("FROM OrganizationStore o WHERE o.store.id =:storeId AND o.organizations.id =:organizationId")
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
        Organizations organizations = (Organizations) session.createQuery("FROM OrganizationStore o WHERE o.store.id=:id ")
                .setParameter("id",storeId)
                .uniqueResult();
        session.close();
        if (doseUserBelongToOrganization(token,organizations.getId()))
        {
            return true;
        }else {
            return false;
        }
    }

    // if sale belong to user
    public boolean doseSaleBelongToUser (String token , int saleId) {
        Session session= sessionFactory.openSession();
        Store store = (Store) session.createQuery("FROM Sale s WHERE s.id=:id")
                .setParameter("id",saleId)
                .uniqueResult();
        session.close();
        if (doseStoreBelongToUser(token,store.getId())){
            return true;
        }else {
            return false;

        }


    }

    }



















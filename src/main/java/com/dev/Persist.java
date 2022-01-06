package com.dev;

import com.dev.objects.UserObject;
import com.dev.utils.Utils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class Persist {
    private final SessionFactory sessionFactory;

    @Autowired
    public Persist(SessionFactory sf) {
        this.sessionFactory = sf;
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















}
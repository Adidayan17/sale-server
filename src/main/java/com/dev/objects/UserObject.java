package com.dev.objects;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class UserObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name="id")
    public int id;

    @Column (name="username")
    private String username;

    @Column (name="password")
    private String password;

    @Column (name="token")
    private String token;

    @Column (name ="first_log_in")
    private int firstLogIn ;



    public UserObject (String username , String password ,String token){
        this.username= username;
        this.password= password;
        this.token = token;
        this.firstLogIn = 0 ;
    }

    public UserObject (UserObject userObject){
        this.id = userObject.getId();
        this.username = userObject.getUsername();
        this.password = userObject.getPassword();
        this.token = userObject.getToken();
        this.firstLogIn = userObject.getFirstLogIn();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void addPost (String post) {
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFirstLogIn() {
        return firstLogIn;
    }

    public void setFirstLogIn(int firstLogIn) {
        this.firstLogIn = firstLogIn;
    }
}

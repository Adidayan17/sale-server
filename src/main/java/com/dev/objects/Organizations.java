package com.dev.objects;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "shop")
public class Organizations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name="id")
    public int id;

    @Column (name = "organizationName")
    private String organizationName;

    @ManyToOne
    @JoinColumn(name ="UserObject")
    private UserObject userObject;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public UserObject getUserObject() {
        return userObject;
    }

    public void setUserObject(UserObject userObject) {
        this.userObject = userObject;
    }
}

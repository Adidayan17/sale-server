package com.dev.objects;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "organization_user")


public class OrganizationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name="id")
    public int id;

    @ManyToOne
    @JoinColumn(name = "organization")
    private Organizations organizations ;

    @ManyToOne
    @JoinColumn(name = "user")
    private UserObject userObject ;

    public OrganizationUser(){

    }

    public OrganizationUser (Organizations organization , UserObject userObject){
        this.organizations = organization;
        this.userObject= userObject;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Organizations getOrganizations() {
        return organizations;
    }

    public void setOrganizations(Organizations organizations) {
        this.organizations = organizations;
    }

    public UserObject getUserObject() {
        return userObject;
    }

    public void setUserObject(UserObject userObject) {
        this.userObject = userObject;
    }
}

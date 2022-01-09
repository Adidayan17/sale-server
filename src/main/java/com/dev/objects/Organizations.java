package com.dev.objects;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "organizations")
public class Organizations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name="id")
    public int id;

    @Column (name = "organizationName")
    private String organizationName;




    public Organizations (){

    }
    public Organizations (Organizations organizations){
        this.id = organizations.getId();
        this.organizationName = organizations.getOrganizationName();
    }

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

}

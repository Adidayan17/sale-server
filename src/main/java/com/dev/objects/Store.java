package com.dev.objects;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name ="store")

public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name="id")
    public int id;

    @Column (name = "storeName")
    private String storeName;

    @ManyToOne
    @JoinColumn(name ="organizations")
    private Organizations organizations;

    public Store(int id, String storeName, Organizations organizations) {
        this.id = id;
        this.storeName = storeName;
        this.organizations = organizations;
    }

    public Store() {
//cons
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Organizations getOrganizations() {
        return organizations;
    }

    public void setOrganizations(Organizations organizations) {
        this.organizations = organizations;
    }
}

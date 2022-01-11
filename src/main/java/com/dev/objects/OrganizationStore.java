package com.dev.objects;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "organization_store")

public class OrganizationStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name="id")
    public int id;

    @ManyToOne
    @JoinColumn(name = "organization")
    private Organizations organizations ;

    @ManyToOne
    @JoinColumn(name = "store")
    private Store store;


    public OrganizationStore(int id, Organizations organizations, Store store) {
        this.id = id;
        this.organizations = organizations;
        this.store = store;
    }

    public OrganizationStore() {

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

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }
}

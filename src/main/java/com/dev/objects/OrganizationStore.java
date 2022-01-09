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




}

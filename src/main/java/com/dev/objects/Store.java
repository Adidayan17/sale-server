package com.dev.objects;

import javax.persistence.*;
import java.util.ArrayList;
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

    @Transient
    private List<Sale> sales;

    public Store(int id, String storeName, Organizations organizations) {
        this.id = id;
        this.storeName = storeName;

    }

    public Store() {
        this.sales = new ArrayList<>();
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

    public List<Sale> getSales() {
        return sales;
    }

    public void setSales(List<Sale> sales) {
        this.sales = sales;
    }
}

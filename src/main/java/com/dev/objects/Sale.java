package com.dev.objects;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "sale")

public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name="id")
    private int id ;

    @Column (name="sale_text")
    private String saleText ;

    @Column (name="start_date")
    private String startDate ;

    @Column (name="end_date")
    private String endDate ;

    @Column (name="available_for_all")
    private int availableForAll =0 ;

    @ManyToOne
    @JoinColumn(name="store")
    private Store store;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public String getSaleText() {
        return saleText;
    }

    public void setSaleText(String saleText) {
        this.saleText = saleText;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getAvailableForAll() {
        return availableForAll;
    }

    public void setAvailableForAll(int availableForAll) {
        this.availableForAll = availableForAll;
    }
}

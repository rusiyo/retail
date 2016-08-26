package com.tenx.ms.retail.store.domain;

import com.tenx.ms.retail.product.domain.ProductEntity;
import com.tenx.ms.retail.product.rest.dto.Product;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "store")
public class StoreEntity {

    public StoreEntity() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "manager_name")
    private String managerName;

    @Column(name = "phone")
    private String phone;

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getPhone() { return phone; }

    public void setPhone(String phone) {
        this.phone = phone;
    }


}

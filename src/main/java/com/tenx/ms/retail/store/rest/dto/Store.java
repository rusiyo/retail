package com.tenx.ms.retail.store.rest.dto;

import com.tenx.ms.commons.validation.constraints.PhoneNumber;
import io.swagger.annotations.ApiModelProperty;
import org.apache.avro.reflect.Nullable;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.validation.constraints.NotNull;

/**
 * Created by javier on 8/25/16.
 */
public class Store {
    @ApiModelProperty(value = "The store ID", readOnly = true)
    private Long storeId;

    @ApiModelProperty(value = "The Store Name")
    @NotNull
    private String name;

    @ApiModelProperty("The location of the store")
    @Nullable
    private String location;

    @ApiModelProperty("The name person in charge of the store")
    @Nullable
    private String managerName;

    @ApiModelProperty("The phone to contact the store")
    @Nullable
    @PhoneNumber
    private String phone;

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String toString() {
        return "Store #" + storeId + " " + name;
    }
}

package com.tenx.ms.retail.order.rest.dto;

import com.tenx.ms.commons.validation.constraints.Email;
import com.tenx.ms.commons.validation.constraints.PhoneNumber;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;
import java.util.List;


public class Order {

    @ApiModelProperty(value = "The order ID", readOnly = true)
    private Long orderId;
    @ApiModelProperty(value = "The store ID", readOnly = true)
    @NotNull
    private Long storeId;
    @ApiModelProperty(value = "The creation date of the order")
    @DateTimeFormat
    @NotNull
    private Timestamp orderDate;
    @ApiModelProperty("Status of the order (i.e. ORDERED, PACKING, SHIPPED)")
    @NotNull
    private OrderStatus status;
    @ApiModelProperty("The purchaser's first name (alpha only)")
    @Pattern(regexp = "[a-zA-Z]*$")
    @NotNull
    private String firstName;
    @ApiModelProperty("The purchaser's last name (alpha only)")
    @NotNull
    private String lastName;
    @ApiModelProperty("Email address of the purchaser")
    @Email
    @NotNull
    private String email;
    @ApiModelProperty("Purchaser's Phone number")
    @PhoneNumber
    @NotNull
    private String phone;
    @ApiModelProperty("Products included in this order")
    @NotNull
    private List<OrderProduct> products;

    public Order() {
    }

    public Order(Long orderId, Long storeId) {
        this.orderId = orderId;
        this.storeId = storeId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public List<OrderProduct> getProducts() {
        return products;
    }

    public void setProducts(List<OrderProduct> products) {
        this.products = products;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public String toString() {
        return "Order #" + orderId + " from Store: " + storeId + " For: " + firstName + " " + lastName + "(" + phone + ")";
    }
}

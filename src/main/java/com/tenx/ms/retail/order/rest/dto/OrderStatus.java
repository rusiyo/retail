package com.tenx.ms.retail.order.rest.dto;

import org.apache.commons.lang3.StringUtils;

public enum OrderStatus {
    ORDERED(0), PACKING(1), SHIPPED(2);

    private final int id;

    OrderStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static OrderStatus getOrderStatusByName(String name) {
        if (StringUtils.isNotBlank(name)) {
            for (OrderStatus status : OrderStatus.values()) {
                if (name.equalsIgnoreCase(status.toString())) {
                    return status;
                }
            }
        }
        return null;
    }

    public static OrderStatus getOrderStatusById(Integer id) {
        for (OrderStatus status : OrderStatus.values()) {
            if (id.equals(status.getId())) {
                return status;
            }
        }
        return null;
    }

    public static boolean isValidStatusId(Integer id) {
        boolean isValid = false;
        if (getOrderStatusById(id) != null) {
            isValid = true;
        }
        return isValid;
    }
}

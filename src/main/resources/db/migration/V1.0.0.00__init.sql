CREATE TABLE store (
  store_id BIGINT NOT NULL AUTO_INCREMENT,
  name varchar(45) NOT NULL,
  location varchar(200) DEFAULT NULL,
  manager_name varchar(45) DEFAULT NULL,
  phone varchar(45) DEFAULT NULL,
  PRIMARY KEY (store_id)
) ENGINE=InnoDB AUTO_INCREMENT=1;

CREATE TABLE product (
    product_id BIGINT NOT NULL AUTO_INCREMENT,
    store_id BIGINT NOT NULL,
    name varchar(45) NOT NULL,
    description varchar(200) NOT NULL,
    sku varchar(10) NOT NULL,
    price decimal(15,2) NOT NULL,
    PRIMARY KEY (product_id)
) ENGINE=InnoDB AUTO_INCREMENT=1;

CREATE TABLE stock (
    product_id BIGINT NOT NULL,
    store_id BIGINT NOT NULL,
    count INT NOT NULL,
    PRIMARY KEY (product_id, store_id)
) ENGINE=InnoDB AUTO_INCREMENT=1;

CREATE TABLE order_table (
    order_id BIGINT NOT NULL AUTO_INCREMENT,
    store_id BIGINT NOT NULL,
    order_date TIMESTAMP NOT NULL,
    status INT NOT NULL,
    first_name varchar(200) NOT NULL,
    last_name varchar(200) NOT NULL,
    email varchar(200) NOT NULL,
    phone varchar(45) NOT NULL,
    PRIMARY KEY (order_id)
) ENGINE=InnoDB AUTO_INCREMENT=1;

CREATE TABLE order_product (
    order_product_id BIGINT NOT NULL AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    order_id BIGINT NOT NULL,
    count INT NOT NULL,
    PRIMARY KEY (order_product_id)
) ENGINE=InnoDB AUTO_INCREMENT=1;

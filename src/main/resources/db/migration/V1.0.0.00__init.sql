CREATE TABLE store (
  store_id BIGINT NOT NULL AUTO_INCREMENT,
  name varchar(45) DEFAULT NULL,
  location varchar(200) DEFAULT NULL,
  manager_name varchar(45) DEFAULT NULL,
  phone varchar(45) DEFAULT NULL,
  PRIMARY KEY (store_id)
) ENGINE=InnoDB AUTO_INCREMENT=1;

CREATE TABLE product (
    product_id BIGINT NOT NULL AUTO_INCREMENT,
    store_id BIGINT NOT NULL,
    name varchar(45) DEFAULT NULL,
    description varchar(200) DEFAULT NULL,
    sku varchar(10) DEFAULT NULL,
    price decimal(15,2) DEFAULT NULL,
    PRIMARY KEY (product_id)
) ENGINE=InnoDB AUTO_INCREMENT=1;

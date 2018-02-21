package com.bustanil.jurnal.product;

import lombok.Data;

import java.math.BigDecimal;

@Data(staticConstructor = "create")
public class Product {

    private final String id;
    private final String code;
    private final String name;
    private final Integer quantity;
    private final BigDecimal price;

}

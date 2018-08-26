package com.bustanil.jurnal.product;

import com.bustanil.jurnal.AuditedEntity;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor(staticName = "create")
@Data
public class Product extends AuditedEntity {

    private final String id;
    private final String code;
    private final String name;
    private final Integer quantity;
    private final BigDecimal price;

}


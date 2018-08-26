package com.bustanil.jurnal.sales.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SaleItem {

    @EqualsAndHashCode.Include
    private String id;
    private String productCode;
    private String productId;
    private String productName;
    private Integer quantity = 0;
    private BigDecimal price = BigDecimal.ZERO;

    public SaleItem(){
        this.id = UUID.randomUUID().toString();
    }

    public BigDecimal getSubTotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    public void addQuantity(Integer quantity) {
        setQuantity(getQuantity() + quantity);
    }
}

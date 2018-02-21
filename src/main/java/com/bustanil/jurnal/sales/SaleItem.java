package com.bustanil.jurnal.sales;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaleItem {

    private String productCode;
    private String productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;

    public BigDecimal getSubTotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

}

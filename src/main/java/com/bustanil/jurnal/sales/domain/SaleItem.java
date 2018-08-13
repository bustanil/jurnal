package com.bustanil.jurnal.sales.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@NoArgsConstructor(force = true)
@Data
@Entity
public class SaleItem {

    @Id
    private String id;
    private String productCode;
    private String productId;
    private String productName;
    private Integer quantity = 0;
    private BigDecimal price = BigDecimal.ZERO;

    public BigDecimal getSubTotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

}

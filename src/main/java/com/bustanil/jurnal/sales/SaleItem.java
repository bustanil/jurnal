package com.bustanil.jurnal.sales;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@NoArgsConstructor(force = true)
@RequiredArgsConstructor(staticName = "create")
@Data
@Entity
public class SaleItem {

    @Id
    private final String id;
    private final String productCode;
    private final String productId;
    private final String productName;
    private Integer quantity = 0;
    private BigDecimal price = BigDecimal.ZERO;

    public BigDecimal getSubTotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

}

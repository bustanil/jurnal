package com.bustanil.jurnal.sales.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class SaleItem {

    @Id
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

}

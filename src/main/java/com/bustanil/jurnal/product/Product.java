package com.bustanil.jurnal.product;

import com.bustanil.jurnal.AuditedEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@NoArgsConstructor(force = true)
@RequiredArgsConstructor(staticName = "create")
@Data
@Entity
public class Product extends AuditedEntity {

    @Id private final String id;
    @Basic(optional = false) private final String code;
    @Basic(optional = false) private final String name;
    @Basic(optional = false) private final Integer quantity;
    @Basic(optional = false) private final BigDecimal price;

}


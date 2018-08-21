package com.bustanil.jurnal.sales.domain;

import com.bustanil.jurnal.AuditedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

@NoArgsConstructor(force = true)
@RequiredArgsConstructor(staticName = "create")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Sale extends AuditedEntity {

    @Id
    private final String id;
    @OneToMany
    private Collection<SaleItem> items = new ArrayList<>();

    public BigDecimal getTotal() {
        return items.stream()
                .reduce(BigDecimal.ZERO, (total, saleItem) -> total.add(saleItem.getSubTotal()), BigDecimal::add);
    }

    public void addItems(SaleItem... items) {
        for (SaleItem item : items) {
            addItem(item);
        }
    }

    private void addItem(SaleItem item) {
        if(!combineToExistingIfPossible(item)){
            items.add(item);
        }

    }

    public void reset() {
        getItems().clear();
    }

    public void remove(SaleItem saleItem) {
        getItems().remove(saleItem);
    }

    public boolean combineToExistingIfPossible(SaleItem saleItem) {
        boolean found = false;
        for (SaleItem item : items) {
            if (saleItem.getProductCode() != null && item.getProductCode().equals(saleItem.getProductCode())) {
                item.addQuantity(saleItem.getQuantity());
                found = true;
                break;
            }
        }
        return found;
    }
}

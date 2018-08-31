package com.bustanil.jurnal.sales.domain;

import com.bustanil.jurnal.AuditedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class Sale extends AuditedEntity {

    private String id;
    private Collection<SaleItem> items = new HashSet<>();

    public Sale(){
        this.id = autogenerateId();
    }

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

    private String autogenerateId(){
        return UUID.randomUUID().toString();
    }

    public void reset() {
        this.id = autogenerateId();
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

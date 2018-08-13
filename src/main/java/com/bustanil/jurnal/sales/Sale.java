package com.bustanil.jurnal.sales;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

@NoArgsConstructor(force = true)
@RequiredArgsConstructor(staticName = "create")
@Entity
@Data
public class Sale {

    @Id
    private final String id;
    private Collection<SaleItem> items;

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
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
    }
}

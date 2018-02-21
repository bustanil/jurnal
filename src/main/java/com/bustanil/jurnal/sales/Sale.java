package com.bustanil.jurnal.sales;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class Sale {

    private LocalDateTime createdDate;
    private List<SaleItem> items;
    private String createdBy;

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

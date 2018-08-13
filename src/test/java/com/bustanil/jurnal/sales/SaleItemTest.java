package com.bustanil.jurnal.sales;

import com.bustanil.jurnal.sales.domain.SaleItem;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class SaleItemTest {

    @Test
    public void getSubTotal() {

        SaleItem item = new SaleItem();
        item.setPrice(BigDecimal.valueOf(100_000));
        item.setQuantity(20);
        item.setProductCode("TEST");
        item.setProductName("Test Product");

        assertThat(item.getSubTotal(), equalTo(BigDecimal.valueOf(2_000_000)));
    }
}
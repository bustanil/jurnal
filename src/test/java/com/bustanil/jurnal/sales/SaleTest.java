package com.bustanil.jurnal.sales;

import com.bustanil.jurnal.sales.domain.Sale;
import com.bustanil.jurnal.sales.domain.SaleItem;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class SaleTest {

    @Test
    public void getTotal() {
        SaleItem item1 = new SaleItem();
        item1.setPrice(BigDecimal.valueOf(100_000));
        item1.setQuantity(20);
        item1.setProductCode("TEST1");
        item1.setProductName("Test Product 1");

        SaleItem item2 = new SaleItem();
        item2.setPrice(BigDecimal.valueOf(50_000));
        item2.setQuantity(1);
        item2.setProductCode("TEST2");
        item2.setProductName("Test Product 2");

        Sale sale = new Sale();
        sale.addItems(item1, item2);

        assertThat(sale.getTotal(), equalTo(BigDecimal.valueOf(2_050_000)));

    }
}
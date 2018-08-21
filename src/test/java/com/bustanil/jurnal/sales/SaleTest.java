package com.bustanil.jurnal.sales;

import com.bustanil.jurnal.sales.domain.Sale;
import com.bustanil.jurnal.sales.domain.SaleItem;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
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

    @Test
    public void addingExistingProductCodeShouldCombine(){
        Sale sale = new Sale();
        SaleItem saleItem = new SaleItem();
        saleItem.setProductCode("AA");
        saleItem.setQuantity(10);
        saleItem.setPrice(BigDecimal.valueOf(1_000));
        sale.addItems(saleItem);

        SaleItem saleItem2 = new SaleItem();
        saleItem2.setProductCode("AA");
        saleItem2.setQuantity(5);
        saleItem2.setPrice(BigDecimal.valueOf(1_000));
        sale.addItems(saleItem2);

        assertThat(sale.getItems().size(), is(1));
        assertThat(sale.getTotal(), is(BigDecimal.valueOf(15_000)));


    }
}
package com.bustanil.jurnal.sales;

import com.bustanil.jurnal.sales.domain.Sale;
import com.bustanil.jurnal.sales.domain.SaleItem;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import static com.bustanil.jurnal.jooq.Tables.SALE_ITEM;
import static com.bustanil.jurnal.jooq.tables.Sale.SALE;

@Service
public class SaleService {

    @Autowired
    DSLContext create;

    @Transactional
    public void makeSale(Sale sale){
        create.insertInto(SALE, SALE.ID, SALE.CREATE_DATE, SALE.TOTAL)
                .values(sale.getId(), new Timestamp(new Date().getTime()), sale.getTotal())
                .execute();
        for (SaleItem item : sale.getItems()) {
            String generatedId = UUID.randomUUID().toString();
            create.insertInto(SALE_ITEM,
                    SALE_ITEM.ID, SALE_ITEM.PRODUCT_CODE, SALE_ITEM.PRODUCT_NAME, SALE_ITEM.PRICE,
                    SALE_ITEM.QUANTITY, SALE_ITEM.SALE_ID, SALE_ITEM.SUB_TOTAL)
                    .values(generatedId, item.getProductCode(), item.getProductName(), item.getPrice(),
                            item.getQuantity(), sale.getId(), item.getSubTotal())
                    .execute();
        }
    }

}

package com.bustanil.jurnal.product;

import com.bustanil.jurnal.jooq.tables.records.ProductRecord;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bustanil.jurnal.jooq.tables.Product.PRODUCT;

@Repository
public class ProductRepository {

    @Autowired
    DSLContext create;

    public List<Product> getAll() {
        List<Product> result = new ArrayList<>();
        Result<ProductRecord> productRecords = create.selectFrom(PRODUCT).fetch();
        for (ProductRecord productRecord : productRecords) {
            Product product = Product.create(productRecord.getId(), productRecord.getCode(), productRecord.getName(),
                    productRecord.getQuantity(), productRecord.getPrice());
            result.add(product);
        }
        return result;
    }

    public Optional<Product> findByCode(String code) {
        ProductRecord productRecord = create.selectFrom(PRODUCT).where(PRODUCT.CODE.eq(code)).fetchOne();
        if (productRecord != null) {
            return Optional.of(Product.create(productRecord.getId(), productRecord.getCode(), productRecord.getName(),
                    productRecord.getQuantity(), productRecord.getPrice()));
        }
        return Optional.empty();
    }
}

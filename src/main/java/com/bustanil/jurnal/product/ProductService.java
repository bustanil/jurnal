package com.bustanil.jurnal.product;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private List<Product> products;

    public ProductService() {
        this.products = new ArrayList<>();
        this.products.add(Product.create("1", "MNYANGIN", "Minyak Angin", 1, BigDecimal.valueOf(30_000)));
        this.products.add(Product.create("2", "BTREABC", "Baterai ABC", 2, BigDecimal.valueOf(10_000)));
        this.products.add(Product.create("2", "BTREABC1", "Baterai ABC 1", 2, BigDecimal.valueOf(10_000)));
        this.products.add(Product.create("2", "BTREABC2", "Baterai ABC 2", 2, BigDecimal.valueOf(10_000)));
        this.products.add(Product.create("2", "BTREABC3", "Baterai ABC 3", 2, BigDecimal.valueOf(10_000)));
    }

    public List<Product> getAll() {
        return products;
    }

    public Optional<Product> findByCode(String code) {
        for (Product product : products) {
            if (product.getCode().equals(code)) {
                return Optional.of(product);
            }
        }
        return Optional.empty();
    }
}

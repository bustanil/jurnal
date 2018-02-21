package com.bustanil.jurnal.product;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private List<Product> products;

    public ProductService() {
        this.products = new ArrayList<>();
        this.products.add(Product.create("1", "MNYANGIN", "Minyak Angin", 1, BigDecimal.valueOf(30_000)));
        this.products.add(Product.create("2", "BTREABC", "Baterai ABC", 2, BigDecimal.valueOf(10_000)));
    }

    public List<Product> getAll() {
        return products;
    }
}

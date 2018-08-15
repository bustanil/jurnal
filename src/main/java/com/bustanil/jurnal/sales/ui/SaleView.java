package com.bustanil.jurnal.sales.ui;

import com.bustanil.jurnal.product.Product;
import com.bustanil.jurnal.product.ProductService;
import com.bustanil.jurnal.sales.domain.SaleItem;
import com.vaadin.data.Binder;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.Editor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringView(name = SaleView.VIEW_NAME)
public class SaleView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "sale";

    private List<SaleItem> saleItems = new ArrayList<>();
    private Grid<SaleItem> saleItemGrid;

    @Autowired
    ProductService productService;

    public SaleView(){
        createSaleItemGrid();
        Button addItemButton = new Button("Add Item");
        addItemButton.addClickListener(event -> {
            saleItems.add(new SaleItem());
            saleItemGrid.getDataProvider().refreshAll();
        });
        addComponent(addItemButton);
        createBottomSection();
    }

    private void createSaleItemGrid() {
        SaleItem firstRow = new SaleItem();
        saleItems.add(firstRow);

        saleItemGrid = new Grid<>(SaleItem.class);
        saleItemGrid.setItems(saleItems);
        saleItemGrid.setSizeFull();
        saleItemGrid.setColumnOrder("productCode", "productName", "quantity", "price", "subTotal");
        saleItemGrid.getColumn("id").setHidden(true);
        saleItemGrid.getColumn("productId").setHidden(true);

        TextField productCodeField = new TextField();
        Editor<SaleItem> editor = saleItemGrid.getEditor();
        Binder<SaleItem> binder = editor.getBinder();
        Binder.Binding<SaleItem, String> productCode1 = binder.bind(productCodeField, "productCode");
        editor.setEnabled(true);
        editor.setBuffered(false);
        Grid.Column<SaleItem, String> productCode = (Grid.Column<SaleItem, String>) saleItemGrid.getColumn("productCode");
        productCode.setEditorBinding(productCode1);

        binder.addValueChangeListener(event -> {
            Optional<Product> maybeProduct = productService.findByCode((String) event.getValue());
            maybeProduct.ifPresent(product -> {
                SaleItem saleItem = binder.getBean();
                saleItem.setProductName(product.getName());
                saleItemGrid.getDataProvider().refreshItem(saleItem);
            });
        });
        addComponent(saleItemGrid);
    }

    private void createBottomSection() {
        HorizontalLayout bottomPart = new HorizontalLayout();
        bottomPart.addComponent(new Button("New Sale"));
        VerticalLayout paymentPart = new VerticalLayout();
        HorizontalLayout totalField = new HorizontalLayout();
        totalField.addComponent(new Label("Total"));
        totalField.addComponent(new TextField());
        paymentPart.addComponent(totalField);
        HorizontalLayout paymentField = new HorizontalLayout();
        paymentField.addComponent(new Label("Payment"));
        paymentField.addComponent(new TextField());
        paymentPart.addComponent(paymentField);
        paymentPart.addComponent(new Button("Complete Transaction"));
        bottomPart.addComponent(paymentPart);
        bottomPart.setComponentAlignment(paymentPart, Alignment.MIDDLE_RIGHT);
        bottomPart.setSizeFull();
        addComponent(bottomPart);
    }

}

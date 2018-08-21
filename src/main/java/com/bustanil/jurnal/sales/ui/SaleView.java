package com.bustanil.jurnal.sales.ui;

import com.bustanil.jurnal.product.Product;
import com.bustanil.jurnal.product.ProductService;
import com.bustanil.jurnal.sales.domain.Sale;
import com.bustanil.jurnal.sales.domain.SaleItem;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToBigDecimalConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.server.UserError;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.Editor;
import com.vaadin.ui.renderers.ButtonRenderer;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Optional;

@SpringView(name = SaleView.VIEW_NAME)
public class SaleView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "sale";

    private Sale sale;
    private Grid<SaleItem> saleItemGrid;
    private Editor<SaleItem> gridEditor;

    @Autowired
    ProductService productService;
    private Binder<Sale> saleBinder;

    public SaleView(){
        sale = new Sale();
        TextField quickAddProduct = new TextField();
        quickAddProduct.addShortcutListener(new ShortcutListener("Quick Add", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object o, Object o1) {
                quickAddProduct.setComponentError(null);
                String productCode = quickAddProduct.getValue();
                Optional<Product> maybeProduct = productService.findByCode(productCode);
                if (maybeProduct.isPresent()) {
                    SaleItem saleItem = new SaleItem();
                    Product product = maybeProduct.get();
                    saleItem.setProductCode(productCode);
                    saleItem.setProductName(product.getName());
                    saleItem.setPrice(product.getPrice());
                    saleItem.setQuantity(1);
                    sale.addItems(saleItem);
                    saleItemGrid.getDataProvider().refreshAll();
                    quickAddProduct.clear();
                    saleBinder.readBean(sale);
                } else {
                    quickAddProduct.setComponentError(new UserError("Invalid product code"));
                }
            }
        });
        addComponent(quickAddProduct);
        createSaleItemGrid();
        createBottomSection();
    }

    private void newSale(){
        sale.reset();
        saleItemGrid.getDataProvider().refreshAll();
        saleBinder.readBean(sale);
    }

    private void createSaleItemGrid() {
        saleItemGrid = new Grid<>(SaleItem.class);
        saleItemGrid.setItems(sale.getItems());
        saleItemGrid.setSizeFull();
        saleItemGrid.addColumn(saleItem -> "Delete", new ButtonRenderer<>(clickEvent -> {
            SaleItem item = clickEvent.getItem();
            sale.remove(item);
            saleItemGrid.getDataProvider().refreshAll();
            saleBinder.readBean(sale);
        }));
        saleItemGrid.setColumnOrder("productCode", "productName", "quantity", "price", "subTotal");
        saleItemGrid.getColumn("id").setHidden(true);
        saleItemGrid.getColumn("productId").setHidden(true);

        setupGridEditor();
        makeProductCodeEditable();
        makeQuantityEditable();
        addComponent(saleItemGrid);

        Button addItemButton = new Button("Add Item");
        addItemButton.addClickListener(event -> {
            sale.addItems(new SaleItem());
            saleItemGrid.getDataProvider().refreshAll();
        });
        addComponent(addItemButton);
    }

    private void makeProductCodeEditable() {
        TextField productCodeField = new TextField();
        Binder<SaleItem> binder = gridEditor.getBinder();
        Binder.Binding<SaleItem, String> productCodeBinding = binder.bind(productCodeField, "productCode");
        Grid.Column<SaleItem, String> productCodeColumn = (Grid.Column<SaleItem, String>) saleItemGrid.getColumn("productCode");
        productCodeColumn.setEditorBinding(productCodeBinding);

        gridEditor.addSaveListener(event -> {
            SaleItem saleItem = event.getBean();
            Optional<Product> maybeProduct = productService.findByCode(saleItem.getProductCode());
            if (maybeProduct.isPresent()) {
                Product product = maybeProduct.get();
                saleItem.setProductName(product.getName());
                if (saleItem.getQuantity() == 0) {
                    saleItem.setQuantity(1);
                }
                saleItem.setPrice(product.getPrice());
                if (sale.combineToExistingIfPossible(saleItem)) {
                    sale.remove(saleItem);
                    saleItemGrid.getDataProvider().refreshAll();
                } else {
                    saleItemGrid.getDataProvider().refreshItem(saleItem);
                }
                saleBinder.readBean(sale);
            } else {
                saleItem.setQuantity(0);
                saleItem.setProductName(null);
                saleItem.setPrice(BigDecimal.ZERO);
                saleItemGrid.getDataProvider().refreshItem(saleItem);
            }
        });
    }

    private void makeQuantityEditable() {
        TextField quantityField = new TextField();
        quantityField.addFocusListener(focusEvent -> quantityField.selectAll());
        Binder<SaleItem> binder = gridEditor.getBinder();
        Binder.Binding<SaleItem, Integer> quantityBinding =
                binder.forField(quantityField)
                        .asRequired("Quantity must be set")
                        .withConverter(new StringToIntegerConverter("Must input integer!"))
                        .bind("quantity");
        Grid.Column<SaleItem, String> quantityColumn = (Grid.Column<SaleItem, String>) saleItemGrid.getColumn("quantity");
        quantityColumn.setEditorBinding(quantityBinding);
    }

    private void setupGridEditor() {
        gridEditor = saleItemGrid.getEditor();
        gridEditor.setEnabled(true);
        gridEditor.setBuffered(true);
    }

    private void createBottomSection() {
        HorizontalLayout bottomPart = new HorizontalLayout();
        Button newSaleButton = new Button("New Sale");
        bottomPart.addComponent(newSaleButton);
        newSaleButton.addClickListener(clickEvent -> newSale());


        VerticalLayout paymentPart = new VerticalLayout();
        HorizontalLayout totalField = new HorizontalLayout();
        totalField.addComponent(new Label("Total"));
        TextField totalTextField = new TextField();
        totalTextField.setReadOnly(true);
        totalField.addComponent(totalTextField);
        saleBinder = new Binder<>(Sale.class);
        saleBinder.forField(totalTextField).withConverter(new StringToBigDecimalConverter(null)).bind(Sale::getTotal, null);
        saleBinder.readBean(sale);

        paymentPart.addComponent(totalField);
        HorizontalLayout paymentField = new HorizontalLayout();
        paymentField.addComponent(new Label("Payment"));
        TextField paymentTextField = new TextField();
        paymentField.addComponent(paymentTextField);
        paymentPart.addComponent(paymentField);
        paymentPart.addComponent(new Button("Complete Transaction"));
        bottomPart.addComponent(paymentPart);
        bottomPart.setComponentAlignment(paymentPart, Alignment.MIDDLE_RIGHT);
        bottomPart.setSizeFull();
        addComponent(bottomPart);
    }

}

package com.bustanil.jurnal.sales.ui;

import com.bustanil.jurnal.product.Product;
import com.bustanil.jurnal.product.ProductService;
import com.bustanil.jurnal.sales.domain.SaleItem;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.Editor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringView(name = SaleView.VIEW_NAME)
public class SaleView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "sale";

    private List<SaleItem> saleItems = new ArrayList<>();
    private Grid<SaleItem> saleItemGrid;
    private Editor<SaleItem> gridEditor;

    @Autowired
    ProductService productService;
    private TextField totalTextField;

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

        setupGridEditor();
        makeProductCodeEditable();
        makeQuantityEditable();
        addComponent(saleItemGrid);
    }

    private void makeProductCodeEditable() {
        TextField productCodeField = new TextField();
        Binder<SaleItem> binder = gridEditor.getBinder();
        Binder.Binding<SaleItem, String> productCodeBinding = binder.bind(productCodeField, "productCode");
        Grid.Column<SaleItem, String> productCodeColumn = (Grid.Column<SaleItem, String>) saleItemGrid.getColumn("productCode");
        productCodeColumn.setEditorBinding(productCodeBinding);

        binder.addValueChangeListener(event -> {
            Optional<Product> maybeProduct = productService.findByCode((String) event.getValue());
            maybeProduct.ifPresent(product -> {
                SaleItem saleItem = binder.getBean();
                saleItem.setProductName(product.getName());
                saleItem.setQuantity(1);
                saleItem.setPrice(product.getPrice());
                saleItemGrid.getDataProvider().refreshItem(saleItem);
            });
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
        gridEditor.setBuffered(false);
    }

    private void createBottomSection() {
        HorizontalLayout bottomPart = new HorizontalLayout();
        bottomPart.addComponent(new Button("New Sale"));
        VerticalLayout paymentPart = new VerticalLayout();
        HorizontalLayout totalField = new HorizontalLayout();
        totalField.addComponent(new Label("Total"));
        totalTextField = new TextField();
        totalTextField.setReadOnly(true);
        totalField.addComponent(totalTextField);
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

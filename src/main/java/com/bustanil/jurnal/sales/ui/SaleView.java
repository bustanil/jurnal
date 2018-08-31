package com.bustanil.jurnal.sales.ui;

import com.bustanil.jurnal.product.Product;
import com.bustanil.jurnal.product.ProductRepository;
import com.bustanil.jurnal.sales.SaleService;
import com.bustanil.jurnal.sales.domain.Sale;
import com.bustanil.jurnal.sales.domain.SaleItem;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToBigDecimalConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.event.ShortcutAction;
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
    ProductRepository productRepository;
    @Autowired
    SaleService saleService;

    private Binder<Sale> saleBinder;
    private final JnTextField quickAddProduct;

    public SaleView(){
        sale = new Sale();
        quickAddProduct = new JnTextField();
        quickAddProduct.addKeyPressedListener(charCode -> {
            if ((int) charCode == ShortcutAction.KeyCode.ENTER) {
                quickAddProduct.setComponentError(null);
                                String productCode = quickAddProduct.getValue();
                Optional<Product> maybeProduct = productRepository.findByCode(productCode);
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
        quickAddProduct.focus();
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
            Optional<Product> maybeProduct = productRepository.findByCode(saleItem.getProductCode());
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
        newSaleButton.addClickListener(clickEvent -> {
            VerticalLayout newSaleDialog = new VerticalLayout();
            newSaleDialog.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
            HorizontalLayout buttonBar = new HorizontalLayout();
            Button okButton = new Button("Yes");
            buttonBar.addComponent(okButton);
            Button cancelButton = new Button("No");
            buttonBar.addComponent(cancelButton);
            newSaleDialog.addComponent(new Label("Are you sure?"));
            newSaleDialog.addComponent(buttonBar);

            Window paymentDialog = new Window("New Sale", newSaleDialog);
            paymentDialog.setWidth(350, Unit.PIXELS);
            paymentDialog.setHeight(150, Unit.PIXELS);
            paymentDialog.setModal(true);
            UI.getCurrent().addWindow(paymentDialog);
            okButton.addClickListener(event -> {
                paymentDialog.close();
                newSale();
            });
            cancelButton.addClickListener(event -> {
                paymentDialog.close();
            });

        });


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
        Button completeButton = new Button("Complete Transaction");
        completeButton.addClickListener(e -> {
            VerticalLayout paymentForm = new VerticalLayout();
            paymentForm.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
            TextField total = new TextField("Total");
            saleBinder.forField(total).withConverter(new StringToBigDecimalConverter(null)).bind("total");
            saleBinder.readBean(sale);
            total.setReadOnly(true);
            paymentForm.addComponent(total);
            TextField paymentTextField = new TextField("Payment", "0");
            TextField changeTextField = new TextField("Change", "0");
            changeTextField.setReadOnly(true);
            paymentForm.addComponent(paymentTextField);
            paymentTextField.addValueChangeListener(event -> {
                BigDecimal payment = new BigDecimal(event.getValue());
                BigDecimal change = payment.subtract(sale.getTotal());
                changeTextField.setValue(change.toString());
            });

            paymentForm.addComponent(changeTextField);
            HorizontalLayout buttonBar = new HorizontalLayout();
            Button okButton = new Button("OK");
            buttonBar.addComponent(okButton);

            Button cancelButton = new Button("Cancel");

            buttonBar.addComponent(cancelButton);
            paymentForm.addComponent(buttonBar);
            paymentForm.setComponentAlignment(buttonBar, Alignment.BOTTOM_CENTER);
            Window paymentDialog = new Window("Complete Transaction", paymentForm);
            paymentDialog.setWidth(350, Unit.PIXELS);
            paymentDialog.setHeight(350, Unit.PIXELS);
            paymentDialog.setModal(true);
            UI.getCurrent().addWindow(paymentDialog);

            okButton.addClickListener(click -> {
                saleService.makeSale(sale);
                newSale();
                paymentDialog.close();
            });
            cancelButton.addClickListener(click -> {
                paymentDialog.close();
            });
        });

        paymentPart.addComponent(completeButton);
        bottomPart.addComponent(paymentPart);
        bottomPart.setComponentAlignment(paymentPart, Alignment.MIDDLE_RIGHT);
        bottomPart.setSizeFull();
        addComponent(bottomPart);
    }

}

package com.bustanil.jurnal.sales.ui;

import com.bustanil.jurnal.sales.domain.SaleItem;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;

@SpringView(name = SaleView.VIEW_NAME)
public class SaleView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "sale";

    public SaleView(){
        Grid<SaleItem> saleItemGrid = new Grid<>(SaleItem.class);
        saleItemGrid.setSizeFull();
        saleItemGrid.setColumnOrder("productCode", "productName", "quantity", "price", "subTotal");
        saleItemGrid.getColumn("id").setHidden(true);
        saleItemGrid.getColumn("productId").setHidden(true);
        addComponent(saleItemGrid);

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

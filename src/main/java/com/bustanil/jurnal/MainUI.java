package com.bustanil.jurnal;

import com.bustanil.jurnal.sales.ui.SaleView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI
@SpringViewDisplay
public class MainUI extends UI implements ViewDisplay {

    private Panel viewDisplay;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout root = new VerticalLayout();
        root.setSizeFull();
        setContent(root);

        viewDisplay = new Panel();
        viewDisplay.setSizeFull();
        root.addComponent(viewDisplay);
        root.setExpandRatio(viewDisplay, 1.0f);

        getUI().getNavigator().navigateTo(SaleView.VIEW_NAME);
    }

    @Override
    public void showView(View view) {
        viewDisplay.setContent((Component) view);
    }
}

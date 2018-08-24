package com.bustanil.jurnal.sales.ui.client;

import com.bustanil.jurnal.sales.ui.KeyPressExtension;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;

@Connect(KeyPressExtension.class)
public class KeyPressExtensionConnector extends AbstractExtensionConnector {

    @Override
    protected void extend(ServerConnector target) {
        // Get the extended widget
        final Widget pw = ((ComponentConnector) target).getWidget();

        // Add an event handler
        pw.addDomHandler(event -> {
            KeyPressRpc rpcProxy = getRpcProxy(KeyPressRpc.class);
            rpcProxy.keyPressed(event.getCharCode());

        }, KeyPressEvent.getType());
    }


}
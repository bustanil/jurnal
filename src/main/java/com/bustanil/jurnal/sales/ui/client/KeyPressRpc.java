package com.bustanil.jurnal.sales.ui.client;

import com.vaadin.shared.communication.ServerRpc;

public interface KeyPressRpc extends ServerRpc {

    void keyPressed(char charCode);

}

package com.bustanil.jurnal.sales.ui;

import com.bustanil.jurnal.sales.ui.client.KeyPressRpc;
import com.vaadin.server.AbstractExtension;

import java.util.function.Consumer;

public class KeyPressExtension extends AbstractExtension {

    public KeyPressExtension(Consumer<Character> handler){
        super();
        registerRpc(new KeyPressRpc() {
            @Override public void keyPressed(char charCode) {
                handler.accept(charCode);
            }
        });
    }

    // Or in an extend() method
    public void extend(JnTextField field) {
        super.extend(field);
    }

}
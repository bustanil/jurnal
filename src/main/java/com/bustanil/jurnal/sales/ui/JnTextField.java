package com.bustanil.jurnal.sales.ui;

import com.vaadin.ui.TextField;

import java.util.ArrayList;
import java.util.List;

public class JnTextField extends TextField {

    public JnTextField(){
        super();
        new KeyPressExtension(character -> {
            for (KeyPressedListener keyPressedListener : keyPressedListeners) {
                keyPressedListener.keyPressed(character);
            }
        }).extend(this);
    }

    private List<KeyPressedListener> keyPressedListeners = new ArrayList<>();

    public void addKeyPressedListener(KeyPressedListener keyPressedListener) {
        keyPressedListeners.add(keyPressedListener);
    }

    public void removeKeyPressedListener(KeyPressedListener keyPressedListener) {
        assert keyPressedListeners.remove(keyPressedListener);

    }

    public static interface KeyPressedListener {
        void keyPressed(char charCode);
    }

}

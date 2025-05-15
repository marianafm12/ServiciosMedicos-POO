package Utilidades;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PanelManager {
    private final JPanel contentPanel;
    private final CardLayout cardLayout;
    private final Map<String, PanelProvider> panels = new HashMap<>();

    public PanelManager(JPanel contentPanel) {
        this.contentPanel = contentPanel;
        this.cardLayout = (CardLayout) contentPanel.getLayout();
    }

    public void registerPanel(PanelProvider provider) {
        panels.put(provider.getPanelName(), provider);
        contentPanel.add(provider.getPanel(), provider.getPanelName());
    }

    public void showPanel(String panelName) {
        if (panels.containsKey(panelName)) {
            cardLayout.show(contentPanel, panelName);
        }
    }
}
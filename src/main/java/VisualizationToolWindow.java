import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.*;

public class VisualizationToolWindow {
    JPanel windowPanel;

    public VisualizationToolWindow(ToolWindow toolWindow) {
        windowPanel = new JPanel();
        windowPanel.setLayout(new BoxLayout(windowPanel, BoxLayout.Y_AXIS));
    }

    public void addClusterButtons(int clusterCount) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        JBScrollPane scrollPane = new JBScrollPane(buttonPanel);

        for (int clusterIdx = 0; clusterIdx < clusterCount; clusterIdx++){
            JPanel rowPanel = new JPanel();
            rowPanel.setLayout(new GridLayout(0, 10));

            JButton colorButton = new JButton("Color");
            colorButton.addActionListener(new ColorButtonListener("Cluster_"+clusterIdx, colorButton));
            colorButton.setEnabled(false);

            JCheckBox checkButton = new JCheckBox(new ClusterCheckboxAction("Cluster_"+clusterIdx, colorButton));

            rowPanel.add(checkButton);
            rowPanel.add(colorButton);

            buttonPanel.add(rowPanel, Component.LEFT_ALIGNMENT, clusterIdx);
        }
        buttonPanel.setVisible(true);
        scrollPane.setVisible(true);
        windowPanel.add(scrollPane);
    }

    public JPanel getContent() {
        return windowPanel;
    }
}

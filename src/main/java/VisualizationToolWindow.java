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
            rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
            setSize(rowPanel, 2000, 25);
            JButton colorButton = new JButton("Color");
            setSize(colorButton, 50, 20);
            colorButton.addActionListener(new ColorButtonListener("Cluster_"+clusterIdx, colorButton));
            colorButton.setEnabled(false);

            JCheckBox checkButton = new JCheckBox(new ClusterCheckboxAction("Cluster_"+clusterIdx, colorButton));
            setSize(checkButton, 100, 20);

            JTextField semanticLabelText = new JTextField();
            setSize(semanticLabelText, 200, 23);
            if (HighlightClusters.clusterSemanticLabels.containsKey("Cluster_"+clusterIdx)){
                semanticLabelText.setText(HighlightClusters.clusterSemanticLabels.get("Cluster_"+clusterIdx));
            }

            HighlightClusters.clusterSemanticLabelTextBoxes.put("Cluster_"+clusterIdx, semanticLabelText);

            rowPanel.add(checkButton);
            rowPanel.add(semanticLabelText);
            rowPanel.add(colorButton);

            buttonPanel.add(rowPanel, Component.LEFT_ALIGNMENT, clusterIdx);
        }

        JButton updateLabels = new JButton("Update Semantic Labels");
        updateLabels.addActionListener(new UpdateLabelButtonAction());
        updateLabels.setVisible(true);
        buttonPanel.setVisible(true);
        scrollPane.setVisible(true);
        windowPanel.add(scrollPane);
        windowPanel.add(updateLabels, Component.LEFT_ALIGNMENT);
        windowPanel.updateUI();
    }

    public void setSize(Component component, int width, int height){
        component.setSize(width, height);
        component.setPreferredSize(component.getSize());
        component.setMaximumSize(component.getSize());
    }

    public JPanel getContent() {
        return windowPanel;
    }
}

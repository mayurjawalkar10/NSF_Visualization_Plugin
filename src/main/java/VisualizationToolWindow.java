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
//            FlowLayout layout = new FlowLayout();
            rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
//            layout.setHgap(10);
            setSize(rowPanel, 2000, 25);
//            System.out.println(rowPanel.getSize());
            JButton colorButton = new JButton("Color");
            setSize(colorButton, 50, 20);
            colorButton.addActionListener(new ColorButtonListener("Cluster_"+clusterIdx, colorButton));
            colorButton.setEnabled(false);

            JCheckBox checkButton = new JCheckBox(new ClusterCheckboxAction("Cluster_"+clusterIdx, colorButton));
            setSize(checkButton, 100, 20);

            JTextField semanticLabelText = new JTextField();
//            semanticLabelText.addActionListener(new TextFieldAction("Cluster_"+clusterIdx, semanticLabelText));
            setSize(semanticLabelText, 200, 23);
//            semanticLabelText.setEnabled(false);
            HighlightClusters.clusterSemanticLabel.put("Cluster_"+clusterIdx, semanticLabelText);

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

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ClusterCheckboxAction extends AbstractAction {
    JButton colorButton;
    public ClusterCheckboxAction(String text, JButton colorButton) {
        super(text);
        this.colorButton = colorButton;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JCheckBox clusterCB = (JCheckBox) e.getSource();
        if (clusterCB.isSelected()) {
            System.out.println(clusterCB.getText()+ " selected");
            colorButton.setEnabled(true);
            HighlightClusters.selectedClusterList.add(clusterCB.getText());
        } else {
            colorButton.setEnabled(false);
            System.out.println(clusterCB.getText()+ " not selected");
            HighlightClusters.selectedClusterList.remove(clusterCB.getText());
        }
        System.out.println(HighlightClusters.selectedClusterList);
    }
}

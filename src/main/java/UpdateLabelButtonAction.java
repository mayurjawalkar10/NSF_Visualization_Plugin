import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class UpdateLabelButtonAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        for (Map.Entry<String,JTextField> entry : HighlightClusters.clusterSemanticLabel.entrySet())
            System.out.println(entry.getKey() + ", " + entry.getValue().getText());
    }
}

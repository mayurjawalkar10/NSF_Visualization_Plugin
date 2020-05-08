import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

public class UpdateLabelButtonAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Before printing data in label dict");
        for (Map.Entry<String,JTextField> entry : HighlightClusters.clusterSemanticLabelTextBoxes.entrySet()){
            HighlightClusters.clusterSemanticLabels.put((String)entry.getKey(), (String)entry.getValue().getText());
        }

        System.out.println("\nLables = \n"+HighlightClusters.clusterSemanticLabels);

        try {
            System.out.println("On update --"+ HighlightClusters.serialized_filename);
            FileOutputStream fout = new FileOutputStream(HighlightClusters.serialized_filename);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(HighlightClusters.clusterSemanticLabels);
            System.out.println("Written");
        }
        catch(Exception ex){
            System.out.print(ex.getMessage());
            System.out.println("Error in reading the serialized semantic label object.");
        }
    }
}

import com.intellij.ui.JBColor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ColorButtonListener implements ActionListener {
    Color hlt_color;
    JButton colorButton;
    String clusterID;
    ColorButtonListener (String clusterID, JButton colorButton){
        this.colorButton = colorButton;
        this.clusterID = clusterID;
        HighlightClusters.clusterColorMap.put(clusterID, Color.BLUE);
        Color textColor = getTextColor(Color.BLUE);
        colorButton.setForeground(textColor);
        colorButton.setBackground(JBColor.BLUE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        hlt_color = JColorChooser.showDialog(null, "Select Highlight Color", Color.BLUE);
        hlt_color = (hlt_color == null) ? Color.BLUE : hlt_color;
        System.out.println(hlt_color.toString());
        Color textColor = getTextColor(hlt_color);
        colorButton.setForeground(textColor);
        colorButton.setBackground(hlt_color);
        HighlightClusters.clusterColorMap.put(clusterID, hlt_color);
    }

    public static Color getTextColor(Color color){
        return ((color.getBlue()+color.getGreen()+color.getRed())/(3.0*255)) > 0.5 ? Color.BLACK : Color.WHITE;
    }
}

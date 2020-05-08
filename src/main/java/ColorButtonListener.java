import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ColorButtonListener implements ActionListener {
    Color hlt_color;
    JButton colorButton;
    String clusterID;
    ColorButtonListener (String clusterID, @NotNull JButton colorButton){
        this.colorButton = colorButton;
        this.clusterID = clusterID;

        if(! HighlightClusters.clusterColorMap.containsKey(clusterID)) {
            HighlightClusters.clusterColorMap.put(clusterID, Color.GRAY);
            hlt_color = Color.GRAY;
        }
        else{
            hlt_color = HighlightClusters.clusterColorMap.get(clusterID);
        }
        Color textColor = getTextColor(hlt_color);
        colorButton.setForeground(textColor);
        colorButton.setBackground(hlt_color);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        hlt_color = JColorChooser.showDialog(null, "Select Highlight Color", hlt_color);
        hlt_color = (hlt_color == null) ? Color.GRAY : hlt_color;
        System.out.println(hlt_color.toString());
        Color textColor = getTextColor(hlt_color);
        colorButton.setForeground(textColor);
        colorButton.setBackground(hlt_color);
        HighlightClusters.clusterColorMap.put(clusterID, hlt_color);
        System.out.println(HighlightClusters.clusterColorMap);
    }

    public static Color getTextColor(@NotNull Color color){
        return ((color.getBlue()+color.getGreen()+color.getRed())/(3.0*255)) > 0.5 ? Color.BLACK : Color.WHITE;
    }
}

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Set;

public class FileSearchVisualizationToolWindow {
    JPanel windowPanel;
    static JPanel fileListPanel;
    JBScrollPane scrollPane;
    ButtonGroup radioButtonGroup;
    Project project;

    public FileSearchVisualizationToolWindow(ToolWindow toolWindow) {
        windowPanel = new JPanel();
        windowPanel.setLayout(new BoxLayout(windowPanel, BoxLayout.Y_AXIS));
        fileListPanel = new JPanel();
        fileListPanel.setLayout(new BoxLayout(fileListPanel, BoxLayout.Y_AXIS));
        scrollPane = new JBScrollPane(fileListPanel);
        radioButtonGroup = new ButtonGroup();
    }

    public void initializeFilePanel(){
        windowPanel.remove(scrollPane);
        radioButtonGroup = new ButtonGroup();
        fileListPanel = new JPanel();
        fileListPanel.setLayout(new BoxLayout(fileListPanel, BoxLayout.Y_AXIS));
        scrollPane = new JBScrollPane(fileListPanel);
        scrollPane.setVisible(true);
        windowPanel.add(scrollPane);
        windowPanel.setVisible(true);
    }

    public void addSearchView(){
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
        setSize(searchPanel, 2000, 25);
        Label inputFileNameLabel = new Label(" Enter Filename to search : ");
        setSize(inputFileNameLabel, 150, 25);
        JTextField searchText = new JTextField();
        setSize(searchText, 200, 23);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new SearchButtonListener(searchText));
        setSize(searchButton, 50, 23);
        searchPanel.add(inputFileNameLabel);
        searchPanel.add(searchText);
        searchPanel.add(searchButton);
        searchPanel.setVisible(true);

        windowPanel.add(searchPanel, Component.CENTER_ALIGNMENT);
        scrollPane.setVisible(true);
        windowPanel.add(scrollPane);
        windowPanel.setVisible(true);
        windowPanel.updateUI();
    }

    public void addFilenames(String filename, int id){
        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        JRadioButton fileRadioButton = new JRadioButton(filename);
        radioButtonGroup.add(fileRadioButton);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        JBScrollPane scrollPane2 = new JBScrollPane(buttonPanel);

        fileRadioButton.addActionListener(new FileRadioButtonListener(filename, project, id));
        setSize(fileRadioButton, 100, 25);
        rowPanel.add(fileRadioButton, Component.LEFT_ALIGNMENT, 0);
        rowPanel.add(scrollPane2, Component.LEFT_ALIGNMENT, 1);

        rowPanel.setVisible(true);
        fileListPanel.add(rowPanel, Component.LEFT_ALIGNMENT, id);
        windowPanel.updateUI();
    }

    public void addClustersForSelectedFilename(String filename, int fileid){
        Map cluster_offset_map = (Map) HighlightClusters.fileClusterMap.get(filename);
        Set clusterNames = cluster_offset_map.keySet();
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
//        JBScrollPane scrollPane2 = new JBScrollPane(buttonPanel);

        JPanel fileRowPanel = (JPanel) fileListPanel.getComponent(fileid);
        JPanel buttonPanel = (JPanel) ((JBScrollPane) fileRowPanel.getComponent(1)).getViewport().getView();
        buttonPanel.setVisible(false);

        for (Object clusterName : clusterNames){
            JPanel rowPanel = new JPanel();
//            FlowLayout layout = new FlowLayout();
            rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
//            layout.setHgap(10);
            setSize(rowPanel, 1500, 25);
//            System.out.println(rowPanel.getSize());
            JButton colorButton = new JButton("Color");
            setSize(colorButton, 50, 20);
            colorButton.addActionListener(new ColorButtonListener((String)clusterName, colorButton));
            colorButton.setEnabled(false);

            JCheckBox checkButton = new JCheckBox(new ClusterCheckboxAction((String)clusterName, colorButton));
            setSize(checkButton, 100, 20);

            JTextField semanticLabelText = new JTextField();
//            semanticLabelText.addActionListener(new TextFieldAction("Cluster_"+clusterIdx, semanticLabelText));
            setSize(semanticLabelText, 200, 23);
//            semanticLabelText.setEnabled(false);
            if (HighlightClusters.clusterSemanticLabels.containsKey((String)clusterName)){
                semanticLabelText.setText(HighlightClusters.clusterSemanticLabels.get((String)clusterName));
            }
            HighlightClusters.clusterSemanticLabelTextBoxes.put((String)clusterName, semanticLabelText);

            rowPanel.add(checkButton, 0);
            rowPanel.add(semanticLabelText, 1);
            rowPanel.add(colorButton, 2);
            buttonPanel.add(rowPanel, Component.LEFT_ALIGNMENT);
        }
//        scrollPane2.getComponent(0).setEnabled(false);
//        JPanel fileRowPanel = (JPanel) fileListPanel.getComponent(fileid);
//        fileRowPanel.add(scrollPane2, Component.LEFT_ALIGNMENT, 1);
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

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileRadioButtonListener implements ActionListener {
    String filename;
    Project project;
    int fileId;

    public FileRadioButtonListener(String filename, Project project, int fileId){
        this.filename = filename;
        this.project = project;
        this.fileId = fileId;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JRadioButton button = (JRadioButton) e.getSource();
        PsiFile filelist[] = FilenameIndex.getFilesByName(this.project, filename,
                                                          GlobalSearchScope.allScope(this.project));
        VirtualFile file = LocalFileSystem.getInstance().findFileByPath(filelist[0].getVirtualFile().getPath());
        JPanel fileRowPanel = (JPanel) FileSearchVisualizationToolWindow.fileListPanel.getComponent(fileId);

        if (button.isSelected()){
            FileEditorManager.getInstance(project).openFile(file, true);
            HighlightClusters.selectedClusterList.clear();
            for (Component component : FileSearchVisualizationToolWindow.fileListPanel.getComponents()){
                JPanel rowPanel = (JPanel) component;
                JBScrollPane sp = (JBScrollPane) rowPanel.getComponent(1);
                JViewport vp = sp.getViewport();
                JPanel sp2 = (JPanel)vp.getView();

                for(Component component_2 : sp2.getComponents()){
                    JPanel rowpanel2 = (JPanel) component_2;
                    JCheckBox clusterCheckBox = ((JCheckBox)rowpanel2.getComponent(0));
                    if (clusterCheckBox.isSelected()) {
                        clusterCheckBox.setSelected(false);
                        String clusterName = clusterCheckBox.getText();
                        JButton colorButton = ((JButton) rowpanel2.getComponent(2));
                        colorButton.setForeground(Color.BLACK);
                        colorButton.setBackground(Color.GRAY);
                        colorButton.setEnabled(false);
                        HighlightClusters.clusterColorMap.put(clusterName, Color.GRAY);
                    }
                }

                if (fileRowPanel == rowPanel){
                    sp2.setVisible(true);
                }
                else {
                    sp2.setVisible(false);
                }
            }
        }
    }
}

import com.intellij.vcs.log.Hash;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class SearchButtonListener implements ActionListener {
    JTextField searchTextField;
    public SearchButtonListener(JTextField searchText) {
        searchTextField = searchText;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SearchFileForClusters.searchFileName = searchTextField.getText();
        System.out.println("Searching for : "+SearchFileForClusters.searchFileName);
        VisualizationToolWindowFactory.fileSearchVisualizationToolWindow.initializeFilePanel();
        Set filenames = HighlightClusters.fileClusterMap.keySet();
        Iterator it = filenames.iterator();
        int indexCount = 0;
        while (it.hasNext()){
            String filenameFromList = (String)it.next();
            if (filenameFromList.contains(SearchFileForClusters.searchFileName)){
                System.out.println("\tFile : "+filenameFromList);
                VisualizationToolWindowFactory.fileSearchVisualizationToolWindow.addFilenames(filenameFromList,
                        indexCount);
                VisualizationToolWindowFactory.fileSearchVisualizationToolWindow.addClustersForSelectedFilename(
                        filenameFromList, indexCount++);
            }
        }

    }
}

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Objects;

public class SearchFileForClusters extends AnAction {
    boolean firstClickFlag;
    static String searchFileName;
    Project project;

    public SearchFileForClusters(){
        this.firstClickFlag = true;
        this.project = null;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        project = e.getProject();
        VisualizationToolWindowFactory.fileSearchVisualizationToolWindow.project = project;

        if (project == null) {
            System.out.println("Project is null.");
            return;
        }

        if (firstClickFlag) {

            Path projectPath = Paths.get(Objects.requireNonNull(project.getBasePath()));
            Path ser_file_path = projectPath.resolve("semanticLabels.ser");
            HighlightClusters.serialized_filename = ser_file_path.toString();

            try {
                FileInputStream fis = new FileInputStream(HighlightClusters.serialized_filename);
                ObjectInputStream ois = new ObjectInputStream(fis);
                HighlightClusters.clusterSemanticLabels = (HashMap<String, String>) ois.readObject();
            }
            catch(Exception exp){
                System.out.println("No serialized object provided. Initializing the cluster labels to empty strings");
                System.out.println("Printing ");
                System.out.println(HighlightClusters.clusterSemanticLabels);
            }

            PsiFile[] files = FilenameIndex.getFilesByName(project, "clusters.json",
                    GlobalSearchScope.allScope(e.getProject()));

            PsiFile considered_cluster_json = null;

            for( PsiFile each_file : files){
                PsiDirectory parent = each_file.getParent();
                if(parent != null && parent.getName().equals("Clusters_Json")){
                    considered_cluster_json = each_file;
                    break;
                }
            }
            considered_cluster_json = files[0];
            if( considered_cluster_json == null){
                return;
            }

            System.out.println(considered_cluster_json.getName());
            String fileText = considered_cluster_json.getText();

            try {
                if (HighlightClusters.fileClusterMap == null) {
                    System.out.println("Reading from SearchFileForClusters");
                    HighlightClusters.readFiles(fileText);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            VisualizationToolWindowFactory.fileSearchVisualizationToolWindow.addSearchView();
            firstClickFlag = false;
        }
    }
}

import com.google.common.collect.Sets;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.*;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.GlobalSearchScopesCore;
import com.intellij.ui.JBColor;
import jdk.nashorn.internal.objects.Global;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class HighlightClusters extends AnAction {
    static int clusterCount;
    static Map fileClusterMap;
    boolean firstClickFlag;
    static HashMap<String, Color> clusterColorMap = new HashMap<>();
    static HashSet<String> selectedClusterList = new HashSet<>();
    static HashMap<String, JTextField> clusterSemanticLabelTextBoxes = new HashMap<>();
    static HashMap<String, String> clusterSemanticLabels = new HashMap<>();
    static String serialized_filename = "";

    public HighlightClusters() {
        firstClickFlag = true;
    }

    public static void readFiles(String text) throws Exception{
        Object parsingObj = new JSONParser().parse(text);
        JSONObject jsonObj = (JSONObject) parsingObj;
        long total_clusters = (long) jsonObj.get("total_clusters");
        clusterCount = (int) total_clusters;
        fileClusterMap = (Map) jsonObj.get("details");
        System.out.println("Reading File");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();

        if (project == null) {
            System.out.println("Project is null.");
            return;
        }

        if (firstClickFlag) {
            Path projectPath = Paths.get(Objects.requireNonNull(project.getBasePath()));
            Path ser_file_path = projectPath.resolve("semanticLabels.ser");
            serialized_filename = ser_file_path.toString();
            PsiFile[] files;

            try {
                FileInputStream fis = new FileInputStream(serialized_filename);
                ObjectInputStream ois = new ObjectInputStream(fis);
                clusterSemanticLabels = (HashMap<String, String>) ois.readObject();
            }
            catch(Exception exp){
                System.out.println("No serialized object provided. Initializing the cluster labels to empty strings");
                System.out.println("Printing ");
                System.out.println(clusterSemanticLabels);
            }

//            Path cluster_json_dir_path = projectPath.resolve("Clusters_Json");
//            Path cluster_json_file_path = cluster_json_dir_path.resolve("exp1-cs-BUYING2-0.9.json");
//
//            if (Files.isDirectory(cluster_json_dir_path)){
//                System.out.println("Directory is present");
////                cluster_json_file_path = cluster_json_dir_path.resolve("cluster_list.json");
//                File cluster_json_file = new File(cluster_json_file_path.toString());
//                if(cluster_json_file.exists()){
////                    file = FilenameIndex.
//                }
//
//            }
//            else {
//                try {
//                    Files.createDirectory(cluster_json_dir_path);
//                } catch (IOException ex) {
////                    ex.printStackTrace();
//                    System.out.println("Directory Creation Failed.");
//                }
//                return;
//            }

//            PsiDirectory cluster_json_dir =

            files = FilenameIndex.getFilesByName(project, "clusters.json",
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
//            GlobalSearchScope scope = GlobalSearchScopesCore.directoriesScope(project, false, );
//
//            file = FilenameIndex.getFilesByName(project, "exp1-cs-BUYING2-0.9.json",
//                    GlobalSearchScope.allScope(e.getProject()));

            System.out.println(considered_cluster_json.getName());
            String fileText = considered_cluster_json.getText();
//
//            System.out.println(files[0].getName());
//            String fileText = files[0].getText();

            try {
                if (fileClusterMap == null) {
                    System.out.println("Reading from HighlightClusters");
                    readFiles(fileText);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            VisualizationToolWindowFactory.visualizeToolWindow.addClusterButtons(clusterCount);
            firstClickFlag = false;
        }

        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();

        if (editor == null) {
            return;
        }

        MarkupModel markupModel = editor.getMarkupModel();
        Document doc = editor.getDocument();
        markupModel.removeAllHighlighters();
        PsiFile file = e.getData(CommonDataKeys.PSI_FILE);

        System.out.println(fileClusterMap);

        if (file != null && fileClusterMap.containsKey(file.getName())) {
            System.out.println("|"+file.getName()+"|");
            Map cluster_offset_map = (Map) fileClusterMap.get(file.getName());
            Set clusterList = cluster_offset_map.keySet();
            Set commonClusters = Sets.intersection(clusterList, HighlightClusters.selectedClusterList);
            System.out.println(commonClusters);

            for (Object cluster : commonClusters) {
                String line_offsets = (String) cluster_offset_map.get((String) cluster);
                //  splits string into start and end offset
                String[] offset_list = line_offsets.split("--", 2);

                String[] start_offset_details = offset_list[0].split(":", 2);
                int start_line_num = Integer.parseInt(start_offset_details[0]);

                //indices are zero based, so -1.
                int line_start_offset = doc.getLineStartOffset(start_line_num-1);
                int start_offset = Integer.parseInt(start_offset_details[1]);

                String[] end_offset_details = offset_list[1].split(":", 2);
                int end_line_num = Integer.parseInt(end_offset_details[0]);
                //indices are zero based, so -1.
                int line_end_offset = doc.getLineStartOffset(end_line_num-1);
                int end_offset = Integer.parseInt(end_offset_details[1]);
              System.out.println(start_line_num+", "+line_start_offset+", "+start_offset+", "+
                                 end_line_num+", "+line_end_offset+", "+end_offset);
//                System.out.println(clusterColorMap);
//                System.out.println(cluster);
                TextAttributes textAttributes = new TextAttributes(JBColor.BLACK, JBColor.WHITE, clusterColorMap.get(cluster),
                        EffectType.BOLD_LINE_UNDERSCORE, Font.BOLD);

                RangeHighlighter hlt = markupModel.addRangeHighlighter((line_start_offset+start_offset-1),
                        (line_end_offset+end_offset), HighlighterLayer.ADDITIONAL_SYNTAX,
                        textAttributes, HighlighterTargetArea.EXACT_RANGE);
            }
        }
    }
}

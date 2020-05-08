import com.google.common.collect.Sets;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.*;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
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

            PsiFile[] file = FilenameIndex.getFilesByName(project, "exp1-cs-BUYING2-0.9.json",
                    GlobalSearchScope.allScope(e.getProject()));
            System.out.println(file[0].getName());
            String fileText = file[0].getText();

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
        if (file != null) {
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
//              System.out.println(start_line_num+", "+line_start_offset+", "+start_offset+", "+
//                                 end_line_num+", "+line_end_offset+", "+end_offset);
                System.out.println(clusterColorMap);
                System.out.println(cluster);
                TextAttributes textAttributes = new TextAttributes(JBColor.BLACK, JBColor.WHITE, clusterColorMap.get(cluster),
                        EffectType.BOLD_LINE_UNDERSCORE, Font.BOLD);

                RangeHighlighter hlt = markupModel.addRangeHighlighter((line_start_offset+start_offset-1),
                        (line_end_offset+end_offset-1), HighlighterLayer.ADDITIONAL_SYNTAX,
                        textAttributes, HighlighterTargetArea.EXACT_RANGE);
            }
        }
    }
}

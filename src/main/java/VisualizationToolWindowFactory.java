import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class VisualizationToolWindowFactory implements ToolWindowFactory {
    public static VisualizationToolWindow visualizeToolWindow;
    public static FileSearchVisualizationToolWindow fileSearchVisualizationToolWindow;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        try {
            visualizeToolWindow = new VisualizationToolWindow(toolWindow);
            fileSearchVisualizationToolWindow = new FileSearchVisualizationToolWindow(toolWindow);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content highlightClustersContent = contentFactory.createContent(visualizeToolWindow.getContent(),
                "Cluster View", false);
        toolWindow.getContentManager().addContent(highlightClustersContent);

        Content searchFileForClustersContent = contentFactory.createContent(fileSearchVisualizationToolWindow.getContent(),
                "Search File for Clusters", false);
        toolWindow.getContentManager().addContent(searchFileForClustersContent);
    }
}

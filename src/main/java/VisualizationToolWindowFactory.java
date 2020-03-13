import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class VisualizationToolWindowFactory implements ToolWindowFactory {
    public static VisualizationToolWindow visualizeToolWindow;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        try {
            visualizeToolWindow = new VisualizationToolWindow(toolWindow);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(visualizeToolWindow.getContent(),
                "Cluster View", false);
        toolWindow.getContentManager().addContent(content);
    }
}

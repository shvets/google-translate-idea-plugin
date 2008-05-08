package org.google.code.translate;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.diagnostic.Logger;

/**
 * This class represents IDEA acrion that preform translation. It will appear in
 * "right-click" content menu.
 *
 * @author Alexander Shvets
 * @version 1.0 04/07/2007
 */
public class TranslateAction extends EditorAction {
  private static final Logger logger = Logger.getInstance(TranslateAction.class.getName());

  private static TranslateHelper translateHelper;

  /**
   * Creates new action.
   *
   * @throws Exception the exception
   */
  public TranslateAction() throws Exception {
    super(new PasteLineHandler());

    translateHelper = new TranslateHelper();
  }

  private static class PasteLineHandler extends EditorWriteActionHandler {
    public void executeWriteAction(Editor editor, DataContext dataContext) {
      if (editor == null) {
        return;
      }

      SelectionModel selectionModel = editor.getSelectionModel();

      String selectedText = selectionModel.getSelectedText();

      try {
        Project project = DataKeys.PROJECT.getData(dataContext);

        if(project != null) {
          TranslateConfiguration configuration = project.getComponent(TranslateConfiguration.class);

          String translatedText = translateHelper.translate(selectedText,
            configuration.getFromLanguage(), configuration.getToLanguage());

          EditorModificationUtil.deleteSelectedText(editor);

          EditorModificationUtil.insertStringAtCaret(editor, translatedText);
        }
      }
      catch (Exception e) {
        logger.error(e.getMessage());
      }
    }
  }

}


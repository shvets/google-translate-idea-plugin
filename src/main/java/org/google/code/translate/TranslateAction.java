package org.google.code.translate;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.project.Project;

/**
 * This class represents IDEA acrion that preform translation. It will appear in
 * "right-click" content menu.
 *
 * @author Alexander Shvets
 * @version 1.0 04/07/2007
 */
public class TranslateAction extends EditorAction {
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

      EditorModificationUtil.deleteSelectedText(editor);

      try {
        String response = translateHelper.translate(selectedText, getLangPair(dataContext));

        EditorModificationUtil.insertStringAtCaret(editor, response);
      }
      catch (Exception e) {
        ;
      }
    }

    private String getLangPair(DataContext dataContext) {
      String langPair = "en|en";

      Project project = (Project)dataContext.getData(DataConstants.PROJECT);

      if (project != null) {
        TranslateConfiguration configuration =
            (TranslateConfiguration) project.getComponent(TranslateConfiguration.class);

        langPair = configuration.getLangPair();
      }

      return langPair;
    }
  }

}

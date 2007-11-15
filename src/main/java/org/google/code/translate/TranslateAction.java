package org.google.code.translate;

import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.actionSystem.DataContext;
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

      try {

        Project project = (Project)dataContext.getData(DataConstants.PROJECT);

        String response = translateHelper.translate(selectedText, translateHelper.getLangPair(project));

        EditorModificationUtil.deleteSelectedText(editor);
          
        EditorModificationUtil.insertStringAtCaret(editor, response);
      }
      catch (Exception e) {
        //e.printStackTrace();
      }
    }
  }

}


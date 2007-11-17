package org.google.code.translate;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents IDEA intention acrion that preform translation preview. It will appear after selection has been done
 * in the editor or by pressing "Alt-Enter" key combination.
 *
 * @author Alexander Shvets
 * @version 1.0 11/17/2007
 */
public class TranslatePreviewIntentionAction extends EditorAction implements IntentionAction {

  public TranslatePreviewIntentionAction() {
    super(new TranslatePreviewHandler());
  }

  private static class TranslatePreviewHandler extends EditorActionHandler {

    public void execute(Editor editor, DataContext dataContext) {
      String searchText = null;

      SelectionModel selection = editor.getSelectionModel();
      if (selection.hasSelection()) {
        searchText = selection.getSelectedText();
      }

      if (searchText != null && searchText.trim().length() > 0) {
        Project project = DataKeys.PROJECT.getData(dataContext);

        showPopup(project, editor, searchText);
      }
    }
  }

  @NotNull
  public String getText() {
    return "Translation preview";
  }

  @NotNull
  public String getFamilyName() {
    return "Google Translate";
  }

  public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
    return editor.getSelectionModel().hasSelection();
  }

  public void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
    String searchText = null;

    SelectionModel selection = editor.getSelectionModel();

    if (selection.hasSelection()) {
      searchText = selection.getSelectedText();
    }

    showPopup(project, editor, searchText);
  }

  public boolean startInWriteAction() {
     return false;
   }

  private static void showPopup(Project project, Editor editor, String searchText) {
    TranslatePopupView popupView = new TranslatePopupView(project, searchText);

    JBPopup jbPopup = JBPopupFactory.getInstance()
        .createComponentPopupBuilder(popupView.mainPanel, popupView.mainPanel)
        .setDimensionServiceKey(project, "popup", false)
        .setRequestFocus(true)
        .setResizable(true)
        .setMovable(true)
        .setTitle("Translation Preview")
        .createPopup();

    jbPopup.showInBestPositionFor(editor);
  }

}


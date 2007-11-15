package org.google.code.translate;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents IDEA intention acrion that preform translation preview. It will appear after selection has been done
 *  in the editor or by pressing "Alt-Enter" key combination.
 *
 * @author Alexander Shvets
 * @version 1.0 11/17/2007
 */
public class TranslateIntentionAction implements IntentionAction {

  public TranslateIntentionAction() {}

  @NotNull
  public String getText() {
    return "Translation preview";
  }

  @NotNull
  public String getFamilyName() {
    return "popup";
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

  private void showPopup(Project project, Editor editor, String searchText) {
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

  public boolean startInWriteAction() {
    return false;
  }

}


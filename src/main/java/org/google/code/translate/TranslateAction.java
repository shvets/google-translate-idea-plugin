package org.google.code.translate;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.util.net.HttpConfigurable;

import java.io.IOException;

/**
 * This class represents IDEA action that preform translation.
 * It will appear in "right-click" content menu.
 *
 * @author Alexander Shvets
 * @version 1.0 04/07/2007
 */
public class TranslateAction extends EditorAction {
  private static final Logger logger = Logger.getInstance(TranslateAction.class.getName());

  private static TranslateHelper translateHelper = new TranslateHelper();

  /**
   * Creates new action.
   *
   * @throws IOException the I/O exception
   */
  public TranslateAction() throws IOException {
    super(new PasteLineHandler());

    HttpConfigurable httpConfigurable = (HttpConfigurable)
        ApplicationManager.getApplication().getComponent("HttpConfigurable");

    if (httpConfigurable == null) {
      httpConfigurable = HttpConfigurable.getInstance();
    }
    if (httpConfigurable != null) {
      if (httpConfigurable.USE_HTTP_PROXY) {
        System.getProperties().put("proxySet", Boolean.valueOf(httpConfigurable.USE_HTTP_PROXY).toString());
        System.getProperties().put("proxyPort", Integer.toString(httpConfigurable.PROXY_PORT));
        System.getProperties().put("proxyHost", httpConfigurable.PROXY_HOST);

        System.getProperties().put("http.proxySet", Boolean.valueOf(httpConfigurable.USE_HTTP_PROXY).toString());
        System.getProperties().put("http.proxyPort", Integer.toString(httpConfigurable.PROXY_PORT));
        System.getProperties().put("http.proxyHost", httpConfigurable.PROXY_HOST);
      }

      if(httpConfigurable.PROXY_AUTHENTICATION) {
        translateHelper.setProxyLogin(httpConfigurable.PROXY_LOGIN);
        translateHelper.setProxyPassword(httpConfigurable.getPlainProxyPassword());
      }
    }

    translateHelper.retrieveLanguagePairs();
  }

  private static class PasteLineHandler extends EditorWriteActionHandler {
    public void executeWriteAction(Editor editor, DataContext dataContext) {
      if (editor == null) {
        return;
      }

      SelectionModel selectionModel = editor.getSelectionModel();

      String selectedText = selectionModel.getSelectedText();

      if (selectedText != null && selectedText.trim().length() > 0) {
        try {
          Project project = DataKeys.PROJECT.getData(dataContext);

          if (project != null) {
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

}


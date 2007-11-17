package org.google.code.translate;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

import javax.swing.*;
import java.awt.*;

/**
 * This class represents panel for displaying pop-up window.
 *
 * @author Alexander Shvets
 * @version 1.0 11/17/2007
 */
public class TranslatePopupView {
  private static final Logger logger = Logger.getInstance(TranslatePopupView.class.getName());

  private JTextPane textPane = new JTextPane();
  public JPanel mainPanel = new JPanel();

  public TranslatePopupView(Project project, Editor editor, String searchText) {
    mainPanel.add(textPane);

    try {
      TranslateHelper translateHelper = new TranslateHelper();

      String translatedText = translateHelper.translate(searchText, TranslateHelper.getLangPair(project));

    Messages.showMessageDialog(
                        translatedText,
                        translatedText ,
                        Messages.getInformationIcon()
                    );      
       Font font = editor.getComponent().getFont();

      mainPanel.setFont(font);
      textPane.setFont(font);

      textPane.setBackground(Color.WHITE);

      textPane.setText(translatedText);
    }
    catch (Exception e) {
      logger.error(e.getMessage());
      textPane.setText(e.getMessage());
    }
  }
}

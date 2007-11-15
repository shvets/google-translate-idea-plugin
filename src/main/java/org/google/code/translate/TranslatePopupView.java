package org.google.code.translate;

import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.awt.*;

/**
 * This class represents panel for displaying pop-up window.
 *
 * @author Alexander Shvets
 * @version 1.0 11/17/2007
 */
public class TranslatePopupView {
    private JTextPane textPane = new JTextPane();
    public JPanel mainPanel = new JPanel();

    public TranslatePopupView(Project project, String searchText) {
      mainPanel.add(textPane);
      
        try {
            TranslateHelper translateHelper = new TranslateHelper();

            String translatedText = translateHelper.translate(searchText, translateHelper.getLangPair(project));

            textPane.setText(translatedText);

            textPane.setBackground(Color.WHITE);
        }
        catch (Exception e) {
            textPane.setText(e.getMessage());
        }
    }
}

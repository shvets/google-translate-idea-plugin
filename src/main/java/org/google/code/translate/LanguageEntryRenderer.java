package org.google.code.translate;

import javax.swing.*;
import java.awt.*;

/**
 * This class represents renderer for combobox. Model contains
 * list of pairs, but we want to display only second part (value).
 *
 * @author Alexander Shvets
 * @version 1.0 04/07/2007
 */
public class LanguageEntryRenderer extends JLabel implements ListCellRenderer  {

  /**
   * Creates new list renderer
   */
  public LanguageEntryRenderer() {
    this.setOpaque(true);
  }

  /**
   * Return a component that has been configured to display the
   * specified value. Contains main logic for the renderer,
   */
  public Component getListCellRendererComponent(JList listbox, Object value,
                      int index, boolean isSelected, boolean cellHasFocus) {
    KeyValuePair pair = (KeyValuePair)value;

    if(pair != null) {
      this.setText(pair.getValue());
    }

    if(isSelected) {
      this.setBackground(UIManager.getColor("ComboBox.selectionBackground"));
      this.setForeground(UIManager.getColor("ComboBox.selectionForeground"));
    }
    else {
      this.setBackground(UIManager.getColor("ComboBox.background"));
      this.setForeground(UIManager.getColor("ComboBox.foreground"));
    }
    
    return this;
  }

}

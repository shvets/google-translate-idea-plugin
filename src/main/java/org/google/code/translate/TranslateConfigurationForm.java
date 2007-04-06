package org.google.code.translate;

import org.google.code.translate.LanguageEntryRenderer;
import org.google.code.translate.TranslateConfiguration;

import javax.swing.*;
import java.util.List;
import java.util.ArrayList;


public class TranslateConfigurationForm {
  private JPanel rootComponent;

  private JComboBox comboBox;
  private JLabel label;

  public TranslateConfigurationForm() {
    comboBox.removeAllItems();
    comboBox.setModel(createModel());
    comboBox.setRenderer(new LanguageEntryRenderer());

    if(comboBox.getModel().getSize() > 0) {
      comboBox.setSelectedIndex(0);      
    }
  }

  public JComboBox getComboBox() {
    return comboBox;
  }

  private ComboBoxModel createModel() {
    List items;
    try {
      TranslateHelper translateHelper = new TranslateHelper();

      items = translateHelper.getLangPairs();
    }
    catch (Exception e) {
      items = new ArrayList();
    }

    return new DefaultComboBoxModel(items.toArray());
  }

  /**
   * Gets the root component of the form.
   *
   * @return root component of the form
   */
  public JComponent getRootComponent() {
    return rootComponent;
  }

  /**
   * Setter for property 'data'.
   *
   * @param data Value to set for property 'data'.
   */
  public void setData(TranslateConfiguration data) {
    String langPair = data.getLangPair();

    ComboBoxModel model = comboBox.getModel();

    boolean ok = false;

    for(int i=0; i < model.getSize() && !ok; i++) {
      KeyValuePair item = (KeyValuePair)model.getElementAt(i);

      if(item.getKey().equals(langPair)) {
        comboBox.setSelectedItem(item);
        ok = true;
      }
    }
  }

  public void getData(TranslateConfiguration data) {
    KeyValuePair selectedItem = (KeyValuePair)comboBox.getSelectedItem();

    if(selectedItem != null) {
      data.setLangPair(selectedItem.getKey());
    }
  }

  public boolean isModified(TranslateConfiguration data) {
    KeyValuePair selectedItem = (KeyValuePair)comboBox.getSelectedItem();

    return selectedItem != null ?
           !selectedItem.getKey().equals(data.getLangPair()) :
           data.getLangPair() != null;
  }

}

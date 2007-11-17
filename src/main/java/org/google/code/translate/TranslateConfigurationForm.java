package org.google.code.translate;

import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.*;


/**
 * This class represents form object for propagating changes in the configuration.
 *
 * @author Alexander Shvets
 * @version 1.0 11/17/2007
 */
public class TranslateConfigurationForm {
  private JPanel rootComponent;

  private JComboBox comboBox = new JComboBox();
  private JLabel label = new JLabel("Select translation:");

  public TranslateConfigurationForm() {
    rootComponent = new JPanel();

    rootComponent.setLayout(new FlowLayout());

    rootComponent.add(label);
    rootComponent.add(comboBox);

    comboBox.removeAllItems();
    comboBox.setModel(createModel());
    comboBox.setRenderer(new LanguageEntryRenderer());

    if (comboBox.getModel().getSize() > 0) {
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
  public void load(TranslateConfiguration data) {
    String langPair = data.getLangPair();

    ComboBoxModel model = comboBox.getModel();

    boolean ok = false;

    for (int i = 0; i < model.getSize() && !ok; i++) {
      KeyValuePair item = (KeyValuePair) model.getElementAt(i);

      if (item.getKey().equals(langPair)) {
        comboBox.setSelectedItem(item);
        ok = true;
      }
    }
  }

  public void save(TranslateConfiguration data) {
    KeyValuePair selectedItem = (KeyValuePair) comboBox.getSelectedItem();

    if (selectedItem != null) {
      data.setLangPair(selectedItem.getKey());
    }
  }

  public boolean isModified(TranslateConfiguration data) {
    KeyValuePair selectedItem = (KeyValuePair) comboBox.getSelectedItem();

    return selectedItem != null ?
        !selectedItem.getKey().equals(data.getLangPair()) :
        data.getLangPair() != null;
  }

}

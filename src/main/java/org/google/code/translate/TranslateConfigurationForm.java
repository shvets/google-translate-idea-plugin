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

  private JComboBox fromComboBox = new JComboBox();
  private JComboBox toComboBox = new JComboBox();

  public TranslateConfigurationForm() {
    rootComponent = new JPanel();

    rootComponent.setLayout(new FlowLayout());

    JLabel label = new JLabel("Select translation:");
    
    rootComponent.add(label);
    rootComponent.add(fromComboBox);
    rootComponent.add(toComboBox);

    fromComboBox.removeAllItems();
    fromComboBox.setModel(createFromModel());
    fromComboBox.setRenderer(new LanguageEntryRenderer());

    if (fromComboBox.getModel().getSize() > 0) {
      fromComboBox.setSelectedIndex(0);
    }

    toComboBox.removeAllItems();
    toComboBox.setModel(createToModel());
    toComboBox.setRenderer(new LanguageEntryRenderer());

    if (toComboBox.getModel().getSize() > 0) {
      toComboBox.setSelectedIndex(0);
    }
  }

  public JComboBox getFromComboBox() {
    return fromComboBox;
  }

  public JComboBox getToComboBox() {
    return toComboBox;
  }

  private ComboBoxModel createFromModel() {
    List items;
    try {
      TranslateHelper translateHelper = new TranslateHelper();

      items = translateHelper.getFromLanguage();
    }
    catch (Exception e) {
      items = new ArrayList();
    }

    return new DefaultComboBoxModel(items.toArray());
  }

  private ComboBoxModel createToModel() {
    List items;
    try {
      TranslateHelper translateHelper = new TranslateHelper();

      items = translateHelper.getToLanguage();
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
    String fromLanguage = data.getFromLanguage();

    ComboBoxModel fromModel = fromComboBox.getModel();

    boolean ok = false;

    for (int i = 0; i < fromModel.getSize() && !ok; i++) {
      KeyValuePair item = (KeyValuePair) fromModel.getElementAt(i);

      if (item.getKey().equals(fromLanguage)) {
        fromComboBox.setSelectedItem(item);
        ok = true;
      }
    }

    String toLanguage = data.getToLanguage();

    ComboBoxModel toModel = toComboBox.getModel();

    ok = false;

    for (int i = 0; i < toModel.getSize() && !ok; i++) {
      KeyValuePair item = (KeyValuePair) toModel.getElementAt(i);

      if (item.getKey().equals(toLanguage)) {
        toComboBox.setSelectedItem(item);
        ok = true;
      }
    }
  }

  public void save(TranslateConfiguration data) {
    KeyValuePair selectedItem = (KeyValuePair) fromComboBox.getSelectedItem();

    if (selectedItem != null) {
      data.setFromLanguage(selectedItem.getKey());
    }

    selectedItem = (KeyValuePair) toComboBox.getSelectedItem();

    if (selectedItem != null) {
      data.setToLanguage(selectedItem.getKey());
    }
  }

  public boolean isModified(TranslateConfiguration data) {
    KeyValuePair selectedItem1 = (KeyValuePair) fromComboBox.getSelectedItem();
    KeyValuePair selectedItem2 = (KeyValuePair) toComboBox.getSelectedItem();

    boolean isModified = selectedItem1 != null ?
        !selectedItem1.getKey().equals(data.getFromLanguage()) :
        data.getFromLanguage() != null;

    isModified |= selectedItem2 != null ?
        !selectedItem2.getKey().equals(data.getToLanguage()) :
        data.getToLanguage() != null;

    return isModified;
  }

}

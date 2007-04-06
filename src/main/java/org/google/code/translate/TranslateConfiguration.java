package org.google.code.translate;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.util.DefaultJDOMExternalizer;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.openapi.components.ProjectComponent;

import javax.swing.*;

import org.jdom.Element;

/**
 * This class represents configuration component for translate action.
 *
 * @author Alexander Shvets
 * @version 1.0 04/07/2007
 */
public class TranslateConfiguration
       implements ProjectComponent, Configurable, JDOMExternalizable {
  private final String COMPONENT_NAME = "Translate.Configuration";
//  private final ImageIcon CONFIG_ICON =
//          helper.getIcon("resources/icon.png", getClass());

  private TranslateConfigurationForm form;

  private String langPair = "en|ru";

  public String getLangPair() {
    return langPair;
  }

  public void setLangPair(final String langPair) {
    this.langPair = langPair;
  }

  public boolean isModified() {
    return form != null && form.isModified(this);
  }

  public void projectOpened() {
  }

  public void projectClosed() {
  }

  public String getComponentName() {
    return COMPONENT_NAME;
  }

  public void initComponent() {
  }

  public void disposeComponent() {
  }

  public String getDisplayName() {
    return "Translate";
  }

  public Icon getIcon() {
//    return CONFIG_ICON;
    return null;
  }

  public String getHelpTopic() {
    return null;
  }

  public JComponent createComponent() {
    if (form == null) {
      form = new TranslateConfigurationForm();
    }

    return form.getRootComponent();
  }

  /**
   * Stores settings from form to configuration bean.
   *
   * @throws ConfigurationException
   */
  public void apply() throws ConfigurationException {
    if (form != null) {
      form.getData(this);
    }
  }

  /**
   * Restores form values from configuration.
   */
  public void reset() {
    if (form != null) {
      form.setData(this);
    }
  }

  /**
   * Disposes UI resource.
   */
  public void disposeUIResources() {
    form = null;
  }

  public void readExternal(Element element) throws InvalidDataException {
    DefaultJDOMExternalizer.readExternal(this, element);
  }

  public void writeExternal(Element element) throws WriteExternalException {
    DefaultJDOMExternalizer.writeExternal(this, element);
  }

}

package org.google.code.translate;

import com.intellij.codeInsight.intention.IntentionManager;
import com.intellij.openapi.components.*;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * This class represents configuration component for translate action.
 *
 * @author Alexander Shvets
 * @version 1.0 04/07/2007
 */
@State(
    name = TranslateConfiguration.COMPONENT_NAME,
    storages = {@Storage(id = "translate", file = "$PROJECT_FILE$")}
)
public final class TranslateConfiguration
    implements ProjectComponent, Configurable, PersistentStateComponent<TranslateConfiguration> {
  public static final String COMPONENT_NAME = "Translate.Configuration";
//  private final ImageIcon CONFIG_ICON =
//          helper.getIcon("resources/icon.png", getClass());

  public static final String CONFIGURATION_LOCATION;
  //+"/.IntelliJIdea70/config/inspection";

  static {
    CONFIGURATION_LOCATION = System.getProperty("user.home");
    //+"/.IntelliJIdea70/config/inspection";
  }

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
    IntentionManager.getInstance().addAction(new TranslatePreviewIntentionAction());
  }

  public void projectClosed() {
  }

  @NotNull
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
      form.save(this);
    }
  }

  /**
   * Restores form values from configuration.
   */
  public void reset() {
    if (form != null) {
      form.load(this);
    }
  }

  /**
   * Disposes UI resource.
   */
  public void disposeUIResources() {
    form = null;
  }

  public TranslateConfiguration getState() {
    return this;
  }

  public void loadState(TranslateConfiguration state) {
    XmlSerializerUtil.copyBean(state, this);
  }

}

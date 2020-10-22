package com.appian.intellij.k.debugger;

import org.jetbrains.annotations.Nullable;

import com.appian.intellij.k.KIcons;
import com.intellij.execution.configurations.ConfigurationTypeBase;

public class QDebuggingConfigurationType extends ConfigurationTypeBase {

  public static final String Q_DEBUG_CONFIG_TYPE_ID = "q-debug";
  public static final String Q_DEBUG_CONFIG_TYPE_NAME = "Q";
  public static final String Q_DEBUG_CONFIG_TYPE_DESC = "Inline Debugging for Q";

  protected QDebuggingConfigurationType() {
    super(Q_DEBUG_CONFIG_TYPE_ID, Q_DEBUG_CONFIG_TYPE_NAME, Q_DEBUG_CONFIG_TYPE_DESC, KIcons.FILE);
  }

  @Nullable
  public static QDebuggingConfigurationType getInstance() {
    return CONFIGURATION_TYPE_EP.findExtension(QDebuggingConfigurationType.class);
  }
}

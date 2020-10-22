package com.appian.intellij.k.debugger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.appian.intellij.k.KIcons;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;

public class QDebuggingConfigurationType extends ConfigurationTypeBase {

  public static final String Q_DEBUG_CONFIG_TYPE_ID = "q-debug";
  public static final String Q_DEBUG_CONFIG_TYPE_NAME = "Q";
  public static final String Q_DEBUG_CONFIG_TYPE_DESC = "Inline Debugging for Q";

  protected QDebuggingConfigurationType() {
    super(Q_DEBUG_CONFIG_TYPE_ID, Q_DEBUG_CONFIG_TYPE_NAME, Q_DEBUG_CONFIG_TYPE_DESC, KIcons.FILE);

    addFactory(new ConfigurationFactory(this) {
      @NotNull
      @Override
      public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        return new QDebugConfiguration(project, this, "Q");
      }
    });
  }

  @Nullable
  public static QDebuggingConfigurationType getInstance() {
    return CONFIGURATION_TYPE_EP.findExtension(QDebuggingConfigurationType.class);
  }
}

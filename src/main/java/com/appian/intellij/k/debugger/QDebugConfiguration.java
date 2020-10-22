package com.appian.intellij.k.debugger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;

public class QDebugConfiguration extends RunConfigurationBase {
  protected QDebugConfiguration(@NotNull Project project, @Nullable ConfigurationFactory factory, @Nullable String name) {
    super(project, factory, name);
  }

  @NotNull
  @Override
  public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
    return new QSettingsEditor();
  }

  @Nullable
  @Override
  public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) throws ExecutionException {
    if (executor instanceof DefaultDebugExecutor) {
      return new QDebugRunProfileState(environment);
    }

    throw new ExecutionException("Configuration Not Enabled - Try Debug Instead");
  }
}

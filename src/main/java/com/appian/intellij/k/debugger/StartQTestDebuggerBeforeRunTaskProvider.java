package com.appian.intellij.k.debugger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.appian.intellij.k.KIcons;
import com.intellij.execution.BeforeRunTask;
import com.intellij.execution.BeforeRunTaskProvider;
import com.intellij.execution.JavaTestConfigurationBase;
import com.intellij.execution.ProgramRunnerUtil;
import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;

public class StartQTestDebuggerBeforeRunTaskProvider extends BeforeRunTaskProvider<StartQTestDebuggerBeforeRunTaskProvider.StartQTestDebuggerBeforeRunTask> {

  public static final String TEST_CONFIGURATION = "Tests";
  private static final Key<StartQTestDebuggerBeforeRunTask> KEY = Key.create("q.beforeRun.startQTestDebugger");
  private static final Logger LOGGER = Logger.getInstance(StartQTestDebuggerBeforeRunTaskProvider.class);

  public static StartQTestDebuggerBeforeRunTaskProvider getInstance(Project project) {
    return (StartQTestDebuggerBeforeRunTaskProvider)BeforeRunTaskProvider.getProvider(project, KEY);
  }

  public static RunConfiguration enableTask(RunConfiguration configuration) {
    return applyToTask(configuration, task -> task.setEnabled(true));
  }

  public static RunConfiguration disableTask(RunConfiguration configuration) {
    return applyToTask(configuration, task -> task.setEnabled(false));
  }

  public static RunConfiguration applyToTask(RunConfiguration configuration, Consumer<StartQTestDebuggerBeforeRunTask> code) {
    List<BeforeRunTask<?>> currentBeforeRunTasks = configuration.getBeforeRunTasks();
    Optional<StartQTestDebuggerBeforeRunTask> beforeRunTask = currentBeforeRunTasks.stream()
        .filter(a -> a instanceof StartQTestDebuggerBeforeRunTask)
        .map(a -> (StartQTestDebuggerBeforeRunTask)a)
        .findFirst();
    if (beforeRunTask.isPresent()) {
      code.accept(beforeRunTask.get());
    } else {
      StartQTestDebuggerBeforeRunTask newBeforeRunTask = getInstance(configuration.getProject()).createTask(
          configuration);
      if (newBeforeRunTask != null) {
        List<BeforeRunTask<?>> newBeforeRunTasks = new ArrayList<>(currentBeforeRunTasks);
        newBeforeRunTasks.add(newBeforeRunTask);
        configuration.setBeforeRunTasks(newBeforeRunTasks);
        code.accept(newBeforeRunTask);
      }
    }
    return configuration;
  }

  @Override
  public Key<StartQTestDebuggerBeforeRunTask> getId() {
    return KEY;
  }

  @Override
  public String getName() {
    return "Launch Q test debugger";
  }

  @Nullable
  @Override
  public Icon getIcon() {
    return KIcons.FILE;
  }

  @Nullable
  @Override
  public StartQTestDebuggerBeforeRunTask createTask(
      @NotNull
          RunConfiguration runConfiguration) {
    if (runConfiguration instanceof JavaTestConfigurationBase) {
      StartQTestDebuggerBeforeRunTask task = new StartQTestDebuggerBeforeRunTask(getId());
      return task;
    }
    return null;
  }

  @Override
  public boolean executeTask(
      @NotNull DataContext dataContext,
      @NotNull
          RunConfiguration runConfiguration,
      @NotNull ExecutionEnvironment executionEnvironment,
      @NotNull StartQTestDebuggerBeforeRunTask beforeRunTask) {

    return beforeRunTask.getSailDebugTestsConfiguration(runConfiguration.getProject()).map(sailDebugConfiguration -> {
      try {
        executeDebugConfiguration(sailDebugConfiguration);
        return true;
      } catch (Exception e) {
        LOGGER.error(e);
      }
      return false;
    }).orElse(false);
  }

  protected void executeDebugConfiguration(RunnerAndConfigurationSettings configuration) {
    ProgramRunnerUtil.executeConfiguration(configuration, DefaultDebugExecutor.getDebugExecutorInstance());
  }

  public static class StartQTestDebuggerBeforeRunTask extends BeforeRunTask<StartQTestDebuggerBeforeRunTask> {

    protected StartQTestDebuggerBeforeRunTask(
        @NotNull
            Key<StartQTestDebuggerBeforeRunTask> providerId) {
      super(providerId);
    }

    public Optional<RunnerAndConfigurationSettings> getSailDebugTestsConfiguration(Project project) {
      QDebuggingConfigurationType qDebuggingConfigurationType = QDebuggingConfigurationType.getInstance();
      if (project != null && qDebuggingConfigurationType != null) {
        RunnerAndConfigurationSettings qDebugConfiguration = RunManager.getInstance(project)
            .findConfigurationByTypeAndName(qDebuggingConfigurationType, TEST_CONFIGURATION);
        return Optional.ofNullable(qDebugConfiguration);
      }
      return Optional.empty();
    }
  }
}

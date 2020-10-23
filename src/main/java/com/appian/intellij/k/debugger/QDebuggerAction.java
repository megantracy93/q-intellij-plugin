package com.appian.intellij.k.debugger;

import static com.intellij.openapi.actionSystem.CommonDataKeys.PSI_FILE;

import java.util.Map;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import com.appian.intellij.k.KIcons;
import com.google.common.collect.Maps;
import com.intellij.codeInsight.hint.HintManager;
import com.intellij.execution.Executor;
import com.intellij.execution.ProgramRunnerUtil;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiFile;

public class QDebuggerAction extends AnAction {

  public static final String Q_DEBUG_CONFIG_DISPLAY_NAME = "Q Debug \'%s\'";
  private static final String JUNIT_CONFIG_ERROR = "Could not generate JUnit Run Configuration for test";
  static final String TESTS_CONFIG_ERROR = "Could not find Q Debug Configuration for test. " +
      "Please connect the Q Debugger manually when the test prompts you.";

  RunConfigurationCreator runConfigurationCreator;

  public QDebuggerAction() {
    super("Debug Q in JUnit Test", "Debug Q for a test using a JUnit runner", KIcons.FILE);
    runConfigurationCreator = new RunConfigurationCreator();
  }

  @Override
  public void update(@NotNull AnActionEvent event) {
    Presentation presentation = event.getPresentation();
    PsiFile file = event.getData(PSI_FILE);
    if (file != null && isActionAvailable(event, file)) {
      presentation.setEnabledAndVisible(true);
      presentation.setText(String.format(Q_DEBUG_CONFIG_DISPLAY_NAME, file.getName()), false);
      return;
    }
    presentation.setEnabledAndVisible(false);
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent event) {
    Optional<RunnerAndConfigurationSettings> configuration = createRunConfiguration(event);
    if (configuration.isPresent()) {
      Executor debugExecutor = DefaultDebugExecutor.getDebugExecutorInstance();
      ProgramRunnerUtil.executeConfiguration(configuration.get(), debugExecutor);
      System.out.println("AAA");
    } else {
      displayError(event, JUNIT_CONFIG_ERROR);
    }
  }

  private boolean isActionAvailable(@NotNull AnActionEvent event, @NotNull PsiFile file) {
    return true;
  }

  private Optional<RunnerAndConfigurationSettings> createRunConfiguration(@NotNull AnActionEvent event) {
    Optional<RunnerAndConfigurationSettings> result = Optional.empty();

    PsiFile file = event.getData(PSI_FILE);
    if (file != null) {
      String configName = String.format("Q Debug %s", file.getName());
      Map<String,String> configParams = Maps.newHashMap();
      result = runConfigurationCreator.createConfiguration(file, configName, configParams);
    }

    // SIP-48: adds sail tests debugger as before run task to the created configuration
    return result.map(configuration -> {
      StartQTestDebuggerBeforeRunTaskProvider.enableTask(configuration.getConfiguration());
      return configuration;
    });
  }

  private void displayError(AnActionEvent event, String message) {
    Editor editor = event.getData(CommonDataKeys.EDITOR);
    if (editor != null) {
      HintManager.getInstance().showErrorHint(editor, message);
    } else {
      Messages.showErrorDialog(event.getProject(), message, "Unable to SAIL Debug Test");
    }
  }
}

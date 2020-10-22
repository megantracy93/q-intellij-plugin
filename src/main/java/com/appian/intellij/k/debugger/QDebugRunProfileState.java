package com.appian.intellij.k.debugger;

import org.jetbrains.annotations.NotNull;

import com.intellij.execution.DefaultExecutionResult;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.project.Project;
import com.intellij.xdebugger.DefaultDebugProcessHandler;

public class QDebugRunProfileState extends CommandLineState {
  private final Project project;

  protected QDebugRunProfileState(ExecutionEnvironment environment) {
    super(environment);
    this.project = environment.getProject();
  }

  @NotNull
  @Override
  public ExecutionResult execute(@NotNull Executor executor, @NotNull ProgramRunner runner) {
    return new DefaultExecutionResult(
        TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole(),
        new DefaultDebugProcessHandler());
  }

  @NotNull
  @Override
  protected ProcessHandler startProcess() throws ExecutionException {
    return null;
  }
}

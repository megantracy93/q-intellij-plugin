package com.appian.intellij.k.debugger;

import org.jetbrains.annotations.NotNull;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.runners.DefaultProgramRunner;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugProcessStarter;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.XDebuggerManager;

public class QDebuggerProgramRunner extends DefaultProgramRunner {

  @NotNull
  @Override
  public String getRunnerId() {
    return "Q Debugger";
  }

  /** The Executor (created when you click the debug button) checks against all the canRun methods of
   *  available configurations. This method just confirms that the executor is the correct type so we
   *  know that we can run doExecute below**/
  @Override
  public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
    return executorId.equals(DefaultDebugExecutor.EXECUTOR_ID) &&
        profile instanceof QDebugConfiguration;
  }

  @NotNull
  @Override
  protected RunContentDescriptor doExecute(@NotNull RunProfileState state, @NotNull ExecutionEnvironment environment) throws ExecutionException {
    FileDocumentManager.getInstance().saveAllDocuments();

    XDebugSession xDebugSession = XDebuggerManager.getInstance(environment.getProject())
        .startSession(environment, new XDebugProcessStarter() {
          @NotNull
          @Override
          public XDebugProcess start(@NotNull XDebugSession session) throws ExecutionException {
            return new QDebugProcess(session, state,
                state.execute(environment.getExecutor(), QDebuggerProgramRunner.this));
          }
        });

    return xDebugSession.getRunContentDescriptor();
  }
}

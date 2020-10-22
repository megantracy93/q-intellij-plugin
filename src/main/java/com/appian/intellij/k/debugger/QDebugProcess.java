package com.appian.intellij.k.debugger;

import org.jetbrains.annotations.NotNull;

import com.intellij.execution.ExecutionResult;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.breakpoints.XBreakpointHandler;
import com.intellij.xdebugger.evaluation.XDebuggerEditorsProvider;

public class QDebugProcess extends XDebugProcess {
  private RunProfileState state;
  private ExecutionResult execute;
  private QBreakpointHandler breakpointHandler;

  public QDebugProcess(XDebugSession session, RunProfileState state, ExecutionResult execute) {
    super(session);
    this.state = state;
    this.execute = execute;
    this.breakpointHandler = new QBreakpointHandler(QBreakpointType.class);
  }

  @NotNull
  @Override
  public XBreakpointHandler<?>[] getBreakpointHandlers() {
    return new QBreakpointHandler[] {breakpointHandler};
  }

  @NotNull
  @Override
  public XDebuggerEditorsProvider getEditorsProvider() {
    return QDebuggerEditorsProvider.INSTANCE;
  }
}

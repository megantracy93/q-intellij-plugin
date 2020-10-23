package com.appian.intellij.k.debugger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.intellij.execution.ExecutionResult;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.breakpoints.XBreakpointHandler;
import com.intellij.xdebugger.evaluation.XDebuggerEditorsProvider;
import com.intellij.xdebugger.frame.XSuspendContext;

public class QDebugProcess extends XDebugProcess {
  private final QBreakpointReachedHandler qBreakpointReachedHandler;
  private RunProfileState state;
  private ExecutionResult execute;
  private QBreakpointHandler breakpointHandler;
  private Thread fileWatchThread;
  private final BreakpointService breakpointService;

  public QDebugProcess(XDebugSession session, RunProfileState state, ExecutionResult execute) {
    super(session);
    this.state = state;
    this.execute = execute;
    breakpointService = new BreakpointService();
    this.breakpointHandler = new QBreakpointHandler(QBreakpointType.class, breakpointService);
    qBreakpointReachedHandler = new QBreakpointReachedHandler(breakpointService, getSession());
    initializeWatch();
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

  @Override
  public void stop() {
    fileWatchThread.interrupt();
    breakpointHandler.unregisterAllBreakpoints();
  }

  @Override
  public void resume(@Nullable XSuspendContext context) {
    getSession().resume();
  }

  private void initializeWatch() {
    fileWatchThread = new QBreakpointReachedFileWatcher(qBreakpointReachedHandler);
    fileWatchThread.start();
  }

}

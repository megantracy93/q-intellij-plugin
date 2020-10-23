package com.appian.intellij.k.debugger;

import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.breakpoints.XLineBreakpoint;
import com.intellij.xdebugger.impl.breakpoints.XLineBreakpointImpl;

public class QBreakpointReachedHandler {

  private BreakpointService breakpointService;
  private XDebugSession debugSession;

  public QBreakpointReachedHandler(
      BreakpointService breakpointService, XDebugSession debugSession) {
    this.breakpointService = breakpointService;
    this.debugSession = debugSession;
  }

  public void process(String fileName, int lineNumber) {
    final XLineBreakpoint[] breakpointReached = new XLineBreakpoint[1];
    final QSuspendContext[] qSuspendContext = new QSuspendContext[1];
    for (XLineBreakpoint breakpoint : breakpointService.getXLineBreakpoints()) {
      XLineBreakpointImpl breakpointImpl = (XLineBreakpointImpl) breakpoint;
      if (fileName.equals(breakpointImpl.getFile().getName()) && breakpoint.getLine() == lineNumber) {
        qSuspendContext[0] = new QSuspendContext(breakpointImpl.getFile(), lineNumber);
        breakpointReached[0] = breakpoint;
      }
    }
    debugSession.breakpointReached(breakpointReached[0], null, qSuspendContext[0]);
    debugSession.positionReached(qSuspendContext[0]);
  }
}

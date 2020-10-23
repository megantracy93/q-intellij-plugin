package com.appian.intellij.k.debugger;

import java.util.Collections;

import org.jetbrains.annotations.Nullable;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.xdebugger.frame.XExecutionStack;
import com.intellij.xdebugger.frame.XSuspendContext;

public class QSuspendContext extends XSuspendContext {

  private final QExecutionStack executionStack;

  public QSuspendContext(VirtualFile virtualFile, int lineNumber) {
    executionStack = new QExecutionStack(virtualFile, lineNumber);
  }

  @Nullable
  @Override
  public XExecutionStack getActiveExecutionStack() {
    return executionStack;
  }

  @Override
  public void computeExecutionStacks(XExecutionStackContainer container) {
    container.addExecutionStack(Collections.singletonList(executionStack), true);
  }
}

package com.appian.intellij.k.debugger;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.xdebugger.frame.XExecutionStack;
import com.intellij.xdebugger.frame.XStackFrame;

public class QExecutionStack extends XExecutionStack {

  private final List<XStackFrame> stackFrames = new ArrayList<>();

  protected QExecutionStack(VirtualFile virtualFile, int lineNumber) {
    super("");
    stackFrames.add(new QStackFrame(virtualFile, lineNumber));
  }

  @Nullable
  @Override
  public XStackFrame getTopFrame() {
    return stackFrames.get(0);
  }

  @Override
  public void computeStackFrames(
      int firstFrameIndex, XStackFrameContainer container) {
    container.addStackFrames(stackFrames, true);
  }
}

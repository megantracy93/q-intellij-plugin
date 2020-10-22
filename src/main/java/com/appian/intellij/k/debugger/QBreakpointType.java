package com.appian.intellij.k.debugger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.xdebugger.breakpoints.XLineBreakpointType;

public class QBreakpointType extends XLineBreakpointType<QBreakpointProperties> {

  public QBreakpointType() {
    super("q_breakpoint", "Q Breakpoint");
  }

  @Nullable
  @Override
  public QBreakpointProperties createBreakpointProperties(@NotNull VirtualFile file, int line) {
    return new QBreakpointProperties();
  }

  @Override
  public boolean canPutAt(@NotNull VirtualFile file, int line, @NotNull Project project) {
    return true;
  }
}

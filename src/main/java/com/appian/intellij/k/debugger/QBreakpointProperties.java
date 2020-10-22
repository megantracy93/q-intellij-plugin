package com.appian.intellij.k.debugger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.intellij.xdebugger.breakpoints.XBreakpointProperties;

public class QBreakpointProperties extends XBreakpointProperties {
  @Nullable
  @Override
  public Object getState() {
    return null;
  }

  @Override
  public void loadState(@NotNull Object state) {

  }
}

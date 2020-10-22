package com.appian.intellij.k.debugger;

import java.io.File;
import java.io.IOException;

import org.jetbrains.annotations.NotNull;

import com.intellij.xdebugger.breakpoints.XBreakpoint;
import com.intellij.xdebugger.breakpoints.XBreakpointHandler;
import com.intellij.xdebugger.impl.breakpoints.XLineBreakpointImpl;

public class QBreakpointHandler extends XBreakpointHandler {
  protected QBreakpointHandler(@NotNull Class breakpointTypeClass) {
    super(breakpointTypeClass);

    File brkpointDir = new File("/tmp/breakpoint");
    brkpointDir.mkdir();
  }

  @Override
  public void registerBreakpoint(@NotNull XBreakpoint breakpoint) {
    String fileName = ((XLineBreakpointImpl) breakpoint).getFile().getName();
    int line = ((XLineBreakpointImpl) breakpoint).getLine();
    File f = new File("/tmp/breakpoint/" + fileName + "_" + line + ".brq");
    try {
      f.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void unregisterBreakpoint(@NotNull XBreakpoint breakpoint, boolean temporary) {
    String fileName = ((XLineBreakpointImpl) breakpoint).getFile().getName();
    int line = ((XLineBreakpointImpl) breakpoint).getLine();
    File f = new File("/tmp/breakpoint/" + fileName + "_" + line + ".brq");
    f.delete();
  }
}

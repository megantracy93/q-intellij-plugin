package com.appian.intellij.k.debugger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jetbrains.annotations.NotNull;

import com.intellij.xdebugger.breakpoints.XBreakpoint;
import com.intellij.xdebugger.breakpoints.XBreakpointHandler;

public class QBreakpointHandler extends XBreakpointHandler {
  protected QBreakpointHandler(@NotNull Class breakpointTypeClass) {
    super(breakpointTypeClass);
  }

  @Override
  public void registerBreakpoint(@NotNull XBreakpoint breakpoint) {
    File f = new File("/tmp/test_q/working.txt");
    try {
      FileOutputStream out = new FileOutputStream(f);
      out.write("Suprise".getBytes());
      out.flush();
      out.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("SUPRISE");
  }

  @Override
  public void unregisterBreakpoint(@NotNull XBreakpoint breakpoint, boolean temporary) {

  }
}

package com.appian.intellij.k.debugger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.xdebugger.breakpoints.XLineBreakpoint;

/**
 * This services owns the current list of breakpoints and allows different actions to update
 * and access the list as needed
 */
public class BreakpointService {
  private final List<XLineBreakpoint> breakpoints = new ArrayList<>();

  public BreakpointService() {
  }

  public Optional<XLineBreakpoint> findBreakpoint(String path, int lineNumber) {
    final List<XLineBreakpoint> result = new ArrayList<>();

    ApplicationManager.getApplication().runReadAction(() -> {
      VirtualFile virtualFile = VfsUtil.findFileByIoFile(new File(path), true);
      if (virtualFile != null) {
        String virtualFileUrl = virtualFile.getUrl();
        for (XLineBreakpoint breakpoint : breakpoints) {
          if (virtualFileUrl.equals(breakpoint.getFileUrl()) && breakpoint.getLine() == lineNumber) {
            result.add(breakpoint);
            return;
          }
        }
      }
    });
    return result.size() >= 1 ? Optional.of(result.get(0)) : Optional.empty();
  }

  public void addBreakpoint(XLineBreakpoint xLineBreakpoint) {
    synchronized (breakpoints) {
      breakpoints.add(xLineBreakpoint);
    }
  }

  public void removeBreakpoint(XLineBreakpoint xLineBreakpoint) {
    synchronized (breakpoints) {
      breakpoints.remove(xLineBreakpoint);
    }
  }

  public List<XLineBreakpoint> getXLineBreakpoints() {
    return breakpoints;
  }
}

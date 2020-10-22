package com.appian.intellij.k.debugger;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

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
  private Thread fileWatchThread;
  private WatchService ws;

  public QDebugProcess(XDebugSession session, RunProfileState state, ExecutionResult execute) {
    super(session);
    this.state = state;
    this.execute = execute;
    this.breakpointHandler = new QBreakpointHandler(QBreakpointType.class);
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
    try {
      ws.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    fileWatchThread.stop();
  }

  private void initializeWatch() {
    fileWatchThread = new Thread() {
      @Override
      public void start() {
        watchForBrqnFiles();
      }
    };

    fileWatchThread.start();
  }

  private void watchForBrqnFiles() {
    ws = null;
    try {
      ws = FileSystems.getDefault().newWatchService();
    } catch (IOException e) {
      e.printStackTrace();
    }

    Path dirToWatch = Paths.get("/tmp/breakpoint");
    try {
      dirToWatch.register(ws, ENTRY_CREATE);
    } catch (IOException e) {
      e.printStackTrace();
    }
    while (true) {
      try {
        assert ws != null;
        WatchKey watchKey = ws.take();
        for (WatchEvent<?> event : watchKey.pollEvents()) {
          if (event.kind() != ENTRY_CREATE) {
            continue;
          }

          WatchEvent<Path> pathEvent = (WatchEvent<Path>)event;
          Path filename = pathEvent.context();
          if (filename == null) {
            System.out.println("Skipping: null context");
            continue;
          }

          if (!filename.toString().endsWith(".brqn")) {
            continue;
          }

          String[] fileAndLine = filename.getFileName().toString().split("_");
          String fileName = fileAndLine[0];
          int line = Integer.parseInt(fileAndLine[1]);
          // at this point we know that a file with the extension .brqn has been created
          // Should we have them write the full filename to the file?
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}

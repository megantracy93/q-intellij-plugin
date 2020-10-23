package com.appian.intellij.k.debugger;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.atomic.AtomicInteger;

public class QBreakpointReachedFileWatcher extends Thread {

  public static final String BREAKPOINT_DIR = "/tmp/breakpoint";
  private final WatchService ws;
  private final QBreakpointReachedHandler qBreakpointReachedHandler;
  private AtomicInteger exitCode = new AtomicInteger(0);

  public QBreakpointReachedFileWatcher(QBreakpointReachedHandler qBreakpointReachedHandler) {
    this.qBreakpointReachedHandler = qBreakpointReachedHandler;
    try {
      ws = FileSystems.getDefault().newWatchService();
    } catch (IOException e) {
      throw new RuntimeException("Failed to initialize WatchService");
    }
  }

  @Override
  public void run() {
    registerBreakpointDir();
    pollForBreakpointReachedFiles();
  }

  @Override
  public void interrupt() {
    super.interrupt();
    exitCode.getAndSet(1);
    try {
      ws.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void registerBreakpointDir() {
    Path dirToWatch = Paths.get(BREAKPOINT_DIR);
    try {
      WatchKey key = dirToWatch.register(ws, ENTRY_CREATE);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void pollForBreakpointReachedFiles() {
    while (exitCode.get() != 1) {
      try {
        WatchKey watchKey = ws.take();
        try {
          for (WatchEvent<?> event : watchKey.pollEvents()) {
            if (event.kind() != ENTRY_CREATE) {
              continue;
            }

            WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;
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
            int lineNumber = Integer.parseInt(fileAndLine[1].split("\\.")[0]);
            qBreakpointReachedHandler.process(fileName, lineNumber);
          }
        } finally {
          // want to make sure we _always_ reset the watch key
          watchKey.reset();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}

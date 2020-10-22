package com.appian.intellij.k.debugger;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;

public class QSettingsEditor extends SettingsEditor {
  @Override
  protected void resetEditorFrom(@NotNull Object s) {

  }

  @Override
  protected void applyEditorTo(@NotNull Object s) throws ConfigurationException {

  }

  @NotNull
  @Override
  protected JComponent createEditor() {
    return new JPanel();
  }
}

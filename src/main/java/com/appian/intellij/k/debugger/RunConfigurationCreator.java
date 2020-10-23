package com.appian.intellij.k.debugger;

import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang.StringEscapeUtils;
import org.jetbrains.annotations.NotNull;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;
import com.intellij.execution.JavaTestConfigurationBase;
import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.psi.PsiFile;

import groovy.lang.Singleton;

@Singleton
public class RunConfigurationCreator {

  public RunConfigurationCreator() {}

  public Optional<RunnerAndConfigurationSettings> createConfiguration(
      PsiFile runnerFile, String configName, Map<String,String> newVmParameters) {
    ConfigurationContext context = new ConfigurationContext(runnerFile);
    RunnerAndConfigurationSettings configurationSettings = context.getConfiguration();
    if (configurationSettings != null) {
      RunConfiguration configuration = configurationSettings.getConfiguration();
      if (configuration instanceof JavaTestConfigurationBase) {
        JavaTestConfigurationBase javaRunConfiguration = (JavaTestConfigurationBase)configuration;
        RunManager runManager = RunManager.getInstance(runnerFile.getProject());
        RunnerAndConfigurationSettings qJunitConfiguration = createJunitConfiguration(runManager,
            configurationSettings.getFactory(), javaRunConfiguration, configName, newVmParameters);
        runManager.addConfiguration(qJunitConfiguration);
        runManager.setSelectedConfiguration(qJunitConfiguration);
        return Optional.of(qJunitConfiguration);
      }
    }
    return Optional.empty();
  }

  /* Creates a copy of the normal Java config for the specified test file, and adds to the JVM parameters */
  @NotNull
  @VisibleForTesting
  RunnerAndConfigurationSettings createJunitConfiguration(
      RunManager runManager,
      ConfigurationFactory configurationFactory,
      JavaTestConfigurationBase javaRunConfiguration,
      String configName,
      Map<String,String> newVmParameters) {
    JavaTestConfigurationBase newRunConfiguration = (JavaTestConfigurationBase)javaRunConfiguration.clone();
    newRunConfiguration.setVMParameters(
        getUpdatedVmParameters(javaRunConfiguration.getVMParameters(), newVmParameters));
    RunnerAndConfigurationSettings newConfigSettings = runManager.createConfiguration(newRunConfiguration,
        configurationFactory);
    newConfigSettings.setTemporary(false);
    newConfigSettings.setName(configName);
    return newConfigSettings;
  }

  private String getUpdatedVmParameters(
      String currentVMParameters,
      @NotNull Map<String,String> newVmParameters) {
    StringBuilder updatedParams = new StringBuilder();
    String nonNullVmParameters = Strings.nullToEmpty(currentVMParameters);
    updatedParams.append(nonNullVmParameters);
    for (Map.Entry<String,String> param : newVmParameters.entrySet()) {
      String paramKey = param.getKey();
      if (!Strings.isNullOrEmpty(paramKey) && !nonNullVmParameters.contains(paramKey)) {
        String value = Strings.nullToEmpty(param.getValue());
        String escapedValue = "\"" + StringEscapeUtils.escapeJava(value) + "\"";
        updatedParams.append(" -D").append(paramKey).append("=").append(escapedValue);
      }
    }
    return updatedParams.toString();
  }
}

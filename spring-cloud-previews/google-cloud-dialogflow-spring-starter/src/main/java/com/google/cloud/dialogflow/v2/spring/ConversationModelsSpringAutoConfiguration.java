/*
 * Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.dialogflow.v2.spring;

import com.google.api.core.BetaApi;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.ExecutorProvider;
import com.google.api.gax.retrying.RetrySettings;
import com.google.api.gax.rpc.HeaderProvider;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.cloud.dialogflow.v2.ConversationModelsClient;
import com.google.cloud.dialogflow.v2.ConversationModelsSettings;
import com.google.cloud.spring.autoconfigure.core.GcpContextAutoConfiguration;
import com.google.cloud.spring.core.DefaultCredentialsProvider;
import com.google.cloud.spring.core.Retry;
import com.google.cloud.spring.core.util.RetryUtil;
import java.io.IOException;
import java.util.Collections;
import javax.annotation.Generated;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

// AUTO-GENERATED DOCUMENTATION AND CLASS.
/**
 * Auto-configuration for {@link ConversationModelsClient}.
 *
 * <p>Provides auto-configuration for Spring Boot
 *
 * <p>The default instance has everything set to sensible defaults:
 *
 * <ul>
 *   <li>The default transport provider is used.
 *   <li>Credentials are acquired automatically through Application Default Credentials.
 *   <li>Retries are configured for idempotent methods but not for non-idempotent methods.
 * </ul>
 */
@Generated("by google-cloud-spring-generator")
@BetaApi("Autogenerated Spring autoconfiguration is not yet stable")
@AutoConfiguration
@AutoConfigureAfter(GcpContextAutoConfiguration.class)
@ConditionalOnClass(ConversationModelsClient.class)
@ConditionalOnProperty(
    value = "com.google.cloud.dialogflow.v2.conversation-models.enabled",
    matchIfMissing = true)
@EnableConfigurationProperties(ConversationModelsSpringProperties.class)
public class ConversationModelsSpringAutoConfiguration {
  private final ConversationModelsSpringProperties clientProperties;
  private final CredentialsProvider credentialsProvider;
  private static final Log LOGGER =
      LogFactory.getLog(ConversationModelsSpringAutoConfiguration.class);

  protected ConversationModelsSpringAutoConfiguration(
      ConversationModelsSpringProperties clientProperties, CredentialsProvider credentialsProvider)
      throws IOException {
    this.clientProperties = clientProperties;
    if (this.clientProperties.getCredentials().hasKey()) {
      if (LOGGER.isTraceEnabled()) {
        LOGGER.trace("Using credentials from ConversationModels-specific configuration");
      }
      this.credentialsProvider =
          ((CredentialsProvider) new DefaultCredentialsProvider(this.clientProperties));
    } else {
      this.credentialsProvider = credentialsProvider;
    }
  }

  /**
   * Provides a default transport channel provider bean. The default is gRPC and will default to it
   * unless the useRest option is provided to use HTTP transport instead
   *
   * @return a default transport channel provider.
   */
  @Bean
  @ConditionalOnMissingBean(name = "defaultConversationModelsTransportChannelProvider")
  public TransportChannelProvider defaultConversationModelsTransportChannelProvider() {
    if (this.clientProperties.getUseRest()) {
      return ConversationModelsSettings.defaultHttpJsonTransportProviderBuilder().build();
    }
    return ConversationModelsSettings.defaultTransportChannelProvider();
  }

  /**
   * Provides a ConversationModelsSettings bean configured to use a DefaultCredentialsProvider and
   * the client library's default transport channel provider
   * (defaultConversationModelsTransportChannelProvider()). It also configures the quota project ID
   * and executor thread count, if provided through properties.
   *
   * <p>Retry settings are also configured from service-level and method-level properties specified
   * in ConversationModelsSpringProperties. Method-level properties will take precedence over
   * service-level properties if available, and client library defaults will be used if neither are
   * specified.
   *
   * @param defaultTransportChannelProvider TransportChannelProvider to use in the settings.
   * @return a {@link ConversationModelsSettings} bean configured with {@link
   *     TransportChannelProvider} bean.
   */
  @Bean
  @ConditionalOnMissingBean
  public ConversationModelsSettings conversationModelsSettings(
      @Qualifier("defaultConversationModelsTransportChannelProvider")
          TransportChannelProvider defaultTransportChannelProvider)
      throws IOException {
    ConversationModelsSettings.Builder clientSettingsBuilder;
    if (this.clientProperties.getUseRest()) {
      clientSettingsBuilder = ConversationModelsSettings.newHttpJsonBuilder();
      if (LOGGER.isTraceEnabled()) {
        LOGGER.trace("Using REST (HTTP/JSON) transport.");
      }
    } else {
      clientSettingsBuilder = ConversationModelsSettings.newBuilder();
    }
    clientSettingsBuilder
        .setCredentialsProvider(this.credentialsProvider)
        .setTransportChannelProvider(defaultTransportChannelProvider)
        .setHeaderProvider(this.userAgentHeaderProvider());
    if (this.clientProperties.getQuotaProjectId() != null) {
      clientSettingsBuilder.setQuotaProjectId(this.clientProperties.getQuotaProjectId());
      if (LOGGER.isTraceEnabled()) {
        LOGGER.trace(
            "Quota project id set to "
                + this.clientProperties.getQuotaProjectId()
                + ", this overrides project id from credentials.");
      }
    }
    if (this.clientProperties.getExecutorThreadCount() != null) {
      ExecutorProvider executorProvider =
          ConversationModelsSettings.defaultExecutorProviderBuilder()
              .setExecutorThreadCount(this.clientProperties.getExecutorThreadCount())
              .build();
      clientSettingsBuilder.setBackgroundExecutorProvider(executorProvider);
      if (LOGGER.isTraceEnabled()) {
        LOGGER.trace(
            "Background executor thread count is "
                + this.clientProperties.getExecutorThreadCount());
      }
    }
    Retry serviceRetry = clientProperties.getRetry();
    if (serviceRetry != null) {
      RetrySettings getConversationModelRetrySettings =
          RetryUtil.updateRetrySettings(
              clientSettingsBuilder.getConversationModelSettings().getRetrySettings(),
              serviceRetry);
      clientSettingsBuilder
          .getConversationModelSettings()
          .setRetrySettings(getConversationModelRetrySettings);

      RetrySettings listConversationModelsRetrySettings =
          RetryUtil.updateRetrySettings(
              clientSettingsBuilder.listConversationModelsSettings().getRetrySettings(),
              serviceRetry);
      clientSettingsBuilder
          .listConversationModelsSettings()
          .setRetrySettings(listConversationModelsRetrySettings);

      RetrySettings getConversationModelEvaluationRetrySettings =
          RetryUtil.updateRetrySettings(
              clientSettingsBuilder.getConversationModelEvaluationSettings().getRetrySettings(),
              serviceRetry);
      clientSettingsBuilder
          .getConversationModelEvaluationSettings()
          .setRetrySettings(getConversationModelEvaluationRetrySettings);

      RetrySettings listConversationModelEvaluationsRetrySettings =
          RetryUtil.updateRetrySettings(
              clientSettingsBuilder.listConversationModelEvaluationsSettings().getRetrySettings(),
              serviceRetry);
      clientSettingsBuilder
          .listConversationModelEvaluationsSettings()
          .setRetrySettings(listConversationModelEvaluationsRetrySettings);

      RetrySettings listLocationsRetrySettings =
          RetryUtil.updateRetrySettings(
              clientSettingsBuilder.listLocationsSettings().getRetrySettings(), serviceRetry);
      clientSettingsBuilder.listLocationsSettings().setRetrySettings(listLocationsRetrySettings);

      RetrySettings getLocationRetrySettings =
          RetryUtil.updateRetrySettings(
              clientSettingsBuilder.getLocationSettings().getRetrySettings(), serviceRetry);
      clientSettingsBuilder.getLocationSettings().setRetrySettings(getLocationRetrySettings);

      if (LOGGER.isTraceEnabled()) {
        LOGGER.trace("Configured service-level retry settings from properties.");
      }
    }
    Retry getConversationModelRetry = clientProperties.getGetConversationModelRetry();
    if (getConversationModelRetry != null) {
      RetrySettings getConversationModelRetrySettings =
          RetryUtil.updateRetrySettings(
              clientSettingsBuilder.getConversationModelSettings().getRetrySettings(),
              getConversationModelRetry);
      clientSettingsBuilder
          .getConversationModelSettings()
          .setRetrySettings(getConversationModelRetrySettings);
      if (LOGGER.isTraceEnabled()) {
        LOGGER.trace(
            "Configured method-level retry settings for getConversationModel from properties.");
      }
    }
    Retry listConversationModelsRetry = clientProperties.getListConversationModelsRetry();
    if (listConversationModelsRetry != null) {
      RetrySettings listConversationModelsRetrySettings =
          RetryUtil.updateRetrySettings(
              clientSettingsBuilder.listConversationModelsSettings().getRetrySettings(),
              listConversationModelsRetry);
      clientSettingsBuilder
          .listConversationModelsSettings()
          .setRetrySettings(listConversationModelsRetrySettings);
      if (LOGGER.isTraceEnabled()) {
        LOGGER.trace(
            "Configured method-level retry settings for listConversationModels from properties.");
      }
    }
    Retry getConversationModelEvaluationRetry =
        clientProperties.getGetConversationModelEvaluationRetry();
    if (getConversationModelEvaluationRetry != null) {
      RetrySettings getConversationModelEvaluationRetrySettings =
          RetryUtil.updateRetrySettings(
              clientSettingsBuilder.getConversationModelEvaluationSettings().getRetrySettings(),
              getConversationModelEvaluationRetry);
      clientSettingsBuilder
          .getConversationModelEvaluationSettings()
          .setRetrySettings(getConversationModelEvaluationRetrySettings);
      if (LOGGER.isTraceEnabled()) {
        LOGGER.trace(
            "Configured method-level retry settings for getConversationModelEvaluation from properties.");
      }
    }
    Retry listConversationModelEvaluationsRetry =
        clientProperties.getListConversationModelEvaluationsRetry();
    if (listConversationModelEvaluationsRetry != null) {
      RetrySettings listConversationModelEvaluationsRetrySettings =
          RetryUtil.updateRetrySettings(
              clientSettingsBuilder.listConversationModelEvaluationsSettings().getRetrySettings(),
              listConversationModelEvaluationsRetry);
      clientSettingsBuilder
          .listConversationModelEvaluationsSettings()
          .setRetrySettings(listConversationModelEvaluationsRetrySettings);
      if (LOGGER.isTraceEnabled()) {
        LOGGER.trace(
            "Configured method-level retry settings for listConversationModelEvaluations from properties.");
      }
    }
    Retry listLocationsRetry = clientProperties.getListLocationsRetry();
    if (listLocationsRetry != null) {
      RetrySettings listLocationsRetrySettings =
          RetryUtil.updateRetrySettings(
              clientSettingsBuilder.listLocationsSettings().getRetrySettings(), listLocationsRetry);
      clientSettingsBuilder.listLocationsSettings().setRetrySettings(listLocationsRetrySettings);
      if (LOGGER.isTraceEnabled()) {
        LOGGER.trace("Configured method-level retry settings for listLocations from properties.");
      }
    }
    Retry getLocationRetry = clientProperties.getGetLocationRetry();
    if (getLocationRetry != null) {
      RetrySettings getLocationRetrySettings =
          RetryUtil.updateRetrySettings(
              clientSettingsBuilder.getLocationSettings().getRetrySettings(), getLocationRetry);
      clientSettingsBuilder.getLocationSettings().setRetrySettings(getLocationRetrySettings);
      if (LOGGER.isTraceEnabled()) {
        LOGGER.trace("Configured method-level retry settings for getLocation from properties.");
      }
    }
    return clientSettingsBuilder.build();
  }

  /**
   * Provides a ConversationModelsClient bean configured with ConversationModelsSettings.
   *
   * @param conversationModelsSettings settings to configure an instance of client bean.
   * @return a {@link ConversationModelsClient} bean configured with {@link
   *     ConversationModelsSettings}
   */
  @Bean
  @ConditionalOnMissingBean
  public ConversationModelsClient conversationModelsClient(
      ConversationModelsSettings conversationModelsSettings) throws IOException {
    return ConversationModelsClient.create(conversationModelsSettings);
  }

  private HeaderProvider userAgentHeaderProvider() {
    String springLibrary = "spring-autogen-conversation-models";
    String version = this.getClass().getPackage().getImplementationVersion();
    return () -> Collections.singletonMap("user-agent", springLibrary + "/" + version);
  }
}

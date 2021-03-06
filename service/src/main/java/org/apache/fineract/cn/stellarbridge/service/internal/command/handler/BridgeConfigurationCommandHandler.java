/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fineract.cn.stellarbridge.service.internal.command.handler;

import org.apache.fineract.cn.command.annotation.Aggregate;
import org.apache.fineract.cn.command.annotation.CommandHandler;
import org.apache.fineract.cn.command.annotation.CommandLogLevel;
import org.apache.fineract.cn.stellarbridge.api.v1.events.EventConstants;
import org.apache.fineract.cn.stellarbridge.service.internal.command.ChangeConfigurationCommand;
import org.apache.fineract.cn.stellarbridge.service.internal.command.ChangeStellarCurrencyIssuerCommand;
import org.apache.fineract.cn.stellarbridge.service.internal.mapper.BridgeConfigurationMapper;
import org.apache.fineract.cn.stellarbridge.service.internal.mapper.StellarCurrencyIssuerMapper;
import org.apache.fineract.cn.stellarbridge.service.internal.repository.BridgeConfigurationEntity;
import org.apache.fineract.cn.stellarbridge.service.internal.repository.BridgeConfigurationRepository;
import org.apache.fineract.cn.stellarbridge.service.internal.repository.StellarCurrencyIssuerEntity;
import org.apache.fineract.cn.stellarbridge.service.internal.repository.StellarCurrencyIssuerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("unused")
@Aggregate
public class BridgeConfigurationCommandHandler {

  private final BridgeConfigurationRepository bridgeConfigurationRepository;
  private final StellarCurrencyIssuerRepository stellarCurrencyIssuerRepository;
  private final EventHelper eventHelper;

  @Autowired
  public BridgeConfigurationCommandHandler(
      final BridgeConfigurationRepository bridgeConfigurationRepository,
      StellarCurrencyIssuerRepository stellarCurrencyIssuerRepository,
      final EventHelper eventHelper) {
    this.bridgeConfigurationRepository = bridgeConfigurationRepository;
    this.stellarCurrencyIssuerRepository = stellarCurrencyIssuerRepository;
    this.eventHelper = eventHelper;
  }

  @CommandHandler(logStart = CommandLogLevel.INFO, logFinish = CommandLogLevel.INFO)
  @Transactional
  public void handle(final ChangeConfigurationCommand changeConfigurationCommand) {

    final BridgeConfigurationEntity entity = BridgeConfigurationMapper.map(
        changeConfigurationCommand.tenantIdentifier(),
        changeConfigurationCommand.instance());
    this.bridgeConfigurationRepository.save(entity);

    eventHelper.sendEvent(EventConstants.PUT_CONFIG, changeConfigurationCommand.tenantIdentifier(), null);
  }

  @CommandHandler(logStart = CommandLogLevel.INFO, logFinish = CommandLogLevel.INFO)
  @Transactional
  public void handle(final ChangeStellarCurrencyIssuerCommand changeStellarCurrencyIssuerCommand) {

    final StellarCurrencyIssuerEntity entity = StellarCurrencyIssuerMapper.map(
        changeStellarCurrencyIssuerCommand.getTenantIdentifier(),
        changeStellarCurrencyIssuerCommand.getInstance());
    this.stellarCurrencyIssuerRepository.save(entity);

    eventHelper.sendEvent(EventConstants.PUT_STELLAR_CURRENCY_ISSUER, changeStellarCurrencyIssuerCommand.getTenantIdentifier(), null);
  }
}

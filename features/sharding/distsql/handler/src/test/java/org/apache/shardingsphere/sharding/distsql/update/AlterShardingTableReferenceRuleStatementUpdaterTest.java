/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.sharding.distsql.update;

import org.apache.shardingsphere.infra.distsql.exception.rule.MissingRequiredRuleException;
import org.apache.shardingsphere.infra.metadata.database.ShardingSphereDatabase;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableReferenceRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.distsql.handler.update.AlterShardingTableReferenceRuleStatementUpdater;
import org.apache.shardingsphere.sharding.distsql.parser.segment.table.TableReferenceRuleSegment;
import org.apache.shardingsphere.sharding.distsql.parser.statement.AlterShardingTableReferenceRuleStatement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public final class AlterShardingTableReferenceRuleStatementUpdaterTest {
    
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ShardingSphereDatabase database;
    
    @Test(expected = MissingRequiredRuleException.class)
    public void assertCheckWithoutCurrentRuleConfig() {
        new AlterShardingTableReferenceRuleStatementUpdater().checkSQLStatement(database, createSQLStatement("foo", "t_order,t_order_item"), null);
    }
    
    @Test(expected = MissingRequiredRuleException.class)
    public void assertCheckWithNotExistedRule() {
        new AlterShardingTableReferenceRuleStatementUpdater().checkSQLStatement(database, createSQLStatement("notExisted", "t_1,t_2"), createCurrentRuleConfiguration());
    }
    
    @Test(expected = MissingRequiredRuleException.class)
    public void assertCheckWithNotExistedTables() {
        new AlterShardingTableReferenceRuleStatementUpdater().checkSQLStatement(database, createSQLStatement("reference_0", "t_3,t_4"), createCurrentRuleConfiguration());
    }
    
    @Test
    public void assertUpdate() {
        ShardingRuleConfiguration currentRuleConfig = createCurrentRuleConfiguration();
        new AlterShardingTableReferenceRuleStatementUpdater().updateCurrentRuleConfiguration(currentRuleConfig, createToBeAlteredRuleConfig());
        assertThat(currentRuleConfig.getBindingTableGroups().size(), is(1));
        assertThat(currentRuleConfig.getBindingTableGroups().iterator().next().getName(), is("reference_0"));
        assertThat(currentRuleConfig.getBindingTableGroups().iterator().next().getReference(), is("t_order,t_order_item,t_1,t_2"));
    }
    
    private AlterShardingTableReferenceRuleStatement createSQLStatement(final String name, final String reference) {
        return new AlterShardingTableReferenceRuleStatement(Collections.singletonList(new TableReferenceRuleSegment(name, reference)));
    }
    
    private ShardingRuleConfiguration createCurrentRuleConfiguration() {
        ShardingRuleConfiguration result = new ShardingRuleConfiguration();
        result.getTables().add(new ShardingTableRuleConfiguration("t_order"));
        result.getTables().add(new ShardingTableRuleConfiguration("t_order_item"));
        result.getTables().add(new ShardingTableRuleConfiguration("t_1"));
        result.getTables().add(new ShardingTableRuleConfiguration("t_2"));
        result.getBindingTableGroups().add(new ShardingTableReferenceRuleConfiguration("reference_0", "t_order,t_order_item"));
        return result;
    }
    
    private ShardingRuleConfiguration createToBeAlteredRuleConfig() {
        ShardingRuleConfiguration result = new ShardingRuleConfiguration();
        result.getBindingTableGroups().add(new ShardingTableReferenceRuleConfiguration("reference_0", "t_order,t_order_item,t_1,t_2"));
        return result;
    }
}

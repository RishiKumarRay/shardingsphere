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

package org.apache.shardingsphere.test.sql.parser.external.engine;

import org.apache.shardingsphere.infra.database.type.DatabaseType;
import org.apache.shardingsphere.infra.util.exception.external.ShardingSphereExternalException;
import org.apache.shardingsphere.sql.parser.api.CacheOption;
import org.apache.shardingsphere.sql.parser.api.SQLParserEngine;
import org.apache.shardingsphere.sql.parser.api.SQLVisitorEngine;
import org.apache.shardingsphere.sql.parser.core.ParseASTNode;
import org.apache.shardingsphere.test.sql.parser.external.engine.param.ExternalSQLParserParameterizedArray;
import org.apache.shardingsphere.test.sql.parser.external.result.SQLParseResultReporter;
import org.apache.shardingsphere.test.sql.parser.external.result.SQLParseResultReporterCreatorFactory;
import org.junit.Test;

import java.util.Properties;

public abstract class ExternalSQLParserParameterizedIT {
    
    private final String sqlCaseId;
    
    private final String sql;
    
    private final DatabaseType databaseType;
    
    private final SQLParseResultReporter resultReporter;
    
    protected ExternalSQLParserParameterizedIT(final ExternalSQLParserParameterizedArray parameterizedArray) {
        sqlCaseId = parameterizedArray.getSqlCaseId();
        sql = parameterizedArray.getSql();
        databaseType = parameterizedArray.getDatabaseType();
        resultReporter = SQLParseResultReporterCreatorFactory.newInstance(parameterizedArray.getReportType()).create(databaseType);
    }
    
    @Test
    public final void assertParseSQL() {
        boolean isSuccess = true;
        try {
            ParseASTNode parseASTNode = new SQLParserEngine(databaseType.getType(), new CacheOption(128, 1024L)).parse(sql, false);
            new SQLVisitorEngine(databaseType.getType(), "STATEMENT", true, new Properties()).visit(parseASTNode);
        } catch (final ShardingSphereExternalException | ClassCastException | NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ignore) {
            isSuccess = false;
        }
        resultReporter.printResult(sqlCaseId, databaseType, isSuccess, sql);
    }
}

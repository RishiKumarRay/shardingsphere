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

package org.apache.shardingsphere.test.sql.parser.internal.asserts;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.shardingsphere.test.sql.parser.internal.cases.sql.type.SQLCaseType;

import java.util.List;

/**
 * SQL case assert context.
 */
@RequiredArgsConstructor
public final class SQLCaseAssertContext {
    
    private final String caseId;
    
    private final String sql;
    
    private final List<String> params;
    
    @Getter
    private final SQLCaseType caseType;
    
    /**
     * Get message text.
     * 
     * @param failureMessage failure message
     * @return got message text
     */
    public String getText(final String failureMessage) {
        StringBuilder result = new StringBuilder(System.lineSeparator());
        appendSQLCaseId(result);
        appendSQL(result);
        appendFailureMessage(failureMessage, result);
        return result.toString();
    }
    
    private void appendSQLCaseId(final StringBuilder builder) {
        builder.append("SQL Case ID : ");
        builder.append(caseId);
        builder.append(System.lineSeparator());
    }
    
    private void appendSQL(final StringBuilder builder) {
        builder.append("SQL         : ");
        if (SQLCaseType.Placeholder == caseType) {
            builder.append(sql);
            builder.append(System.lineSeparator());
            builder.append("SQL Params  : ");
            builder.append(params);
            builder.append(System.lineSeparator());
        } else {
            builder.append(sql);
        }
    }
    
    private void appendFailureMessage(final String failureMessage, final StringBuilder builder) {
        builder.append(System.lineSeparator());
        builder.append(failureMessage);
        builder.append(System.lineSeparator());
    }
}

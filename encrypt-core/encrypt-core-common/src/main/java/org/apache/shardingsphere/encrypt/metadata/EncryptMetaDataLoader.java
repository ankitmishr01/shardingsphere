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

package org.apache.shardingsphere.encrypt.metadata;

import org.apache.shardingsphere.encrypt.rule.EncryptRule;
import org.apache.shardingsphere.sql.parser.binder.metadata.schema.SchemaMetaData;
import org.apache.shardingsphere.sql.parser.binder.metadata.table.TableMetaData;
import org.apache.shardingsphere.sql.parser.binder.metadata.table.TableMetaDataLoader;
import org.apache.shardingsphere.underlying.common.config.properties.ConfigurationProperties;
import org.apache.shardingsphere.underlying.common.database.type.DatabaseType;
import org.apache.shardingsphere.underlying.common.metadata.schema.spi.RuleMetaDataLoader;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Meta data loader for encrypt.
 */
public final class EncryptMetaDataLoader implements RuleMetaDataLoader<EncryptRule> {
    
    @Override
    public SchemaMetaData load(final DatabaseType databaseType, final Map<String, DataSource> dataSourceMap,
                               final EncryptRule encryptRule, final ConfigurationProperties properties, final Collection<String> excludedTableNames) throws SQLException {
        DataSource dataSource = dataSourceMap.values().iterator().next();
        Collection<String> encryptTableNames = encryptRule.getEncryptTableNames();
        Map<String, TableMetaData> result = new HashMap<>(encryptTableNames.size(), 1);
        for (String each : encryptTableNames) {
            if (!excludedTableNames.contains(each)) {
                result.put(each, TableMetaDataLoader.load(dataSource, each, databaseType.getName()));
            }
        }
        return new SchemaMetaData(result);
    }
    
    @Override
    public Optional<TableMetaData> load(final DatabaseType databaseType, final Map<String, DataSource> dataSourceMap,
                              final String tableName, final EncryptRule encryptRule, final ConfigurationProperties properties) throws SQLException {
        return encryptRule.findEncryptTable(tableName).isPresent()
                ? Optional.of(TableMetaDataLoader.load(dataSourceMap.values().iterator().next(), tableName, databaseType.getName())) : Optional.empty();
    }
    
    @Override
    public int getOrder() {
        return 5;
    }
    
    @Override
    public Class<EncryptRule> getTypeClass() {
        return EncryptRule.class;
    }
}

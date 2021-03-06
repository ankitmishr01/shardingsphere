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

package org.apache.shardingsphere.shardingscaling.core.execute.engine;

import org.apache.shardingsphere.shardingscaling.core.execute.executor.ShardingScalingExecutor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

@RunWith(MockitoJUnitRunner.class)
public final class DefaultSyncTaskExecuteEngineTest {
    
    @Mock
    private ShardingScalingExecutor shardingScalingExecutor;
    
    @Test(expected = RejectedExecutionException.class)
    public void assertSubmitThrowRejectedExecutionException() {
        List<ShardingScalingExecutor> executors = new ArrayList<>(11);
        for (int i = 0; i < 11; i++) {
            executors.add(shardingScalingExecutor);
        }
        new DefaultSyncTaskExecuteEngine(10).submit(executors);
    }
}

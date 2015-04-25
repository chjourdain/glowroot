/*
 * Copyright 2011-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.glowroot.local.store;

import java.io.File;

import com.google.common.base.Stopwatch;
import com.google.common.io.CharSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.glowroot.collector.Trace;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class TraceDaoPerformanceMain {

    private static final Logger logger = LoggerFactory.getLogger(TraceDaoPerformanceMain.class);

    private TraceDaoPerformanceMain() {}

    public static void main(String... args) throws Exception {
        DataSource dataSource = new DataSource();
        CappedDatabase cappedDatabase = new CappedDatabase(new File("glowroot.capped.db"), 1000000);
        TraceDao traceDao = new TraceDao(dataSource, cappedDatabase);

        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < 1000; i++) {
            Trace trace = TraceTestData.createTrace();
            CharSource entries = TraceTestData.createEntries();
            traceDao.store(trace, entries, null);
        }
        logger.info("elapsed time: {}", stopwatch.elapsed(MILLISECONDS));
        logger.info("num traces: {}", traceDao.count());
    }
}

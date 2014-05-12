/*
 * Copyright 2014 Martin Tamme
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.signalr.client.util.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a {@link ScheduledExecutorService} based scheduler.
 */
public final class ScheduledExecutorServiceScheduler implements Scheduler {

    /**
     * The default thread pool size.
     */
    private static final int DEFAULT_THREAD_POOL_SIZE = 1;

    /**
     * The private logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(ScheduledExecutorServiceScheduler.class);

    /**
     * The scheduled executor service.
     */
    private final ScheduledExecutorService _executorService;

    /**
     * Initializes a new instance of the {@link ScheduledExecutorServiceScheduler} class.
     */
    public ScheduledExecutorServiceScheduler() {
        this(Executors.newScheduledThreadPool(DEFAULT_THREAD_POOL_SIZE));
    }

    /**
     * Initializes a new instance of the {@link ScheduledExecutorServiceScheduler} class.
     * 
     * @param executorService The executor service.
     */
    public ScheduledExecutorServiceScheduler(final ScheduledExecutorService executorService) {
        if (executorService == null) {
            throw new IllegalArgumentException("Executor service must not be null");
        }

        _executorService = executorService;
    }

    @Override
    public Job scheduleJob(final Schedulable schedulable, final long period, final TimeUnit timeUnit) {
        if (schedulable == null) {
            throw new IllegalArgumentException("Schedulable must not be null");
        }
        if (timeUnit == null) {
            throw new IllegalArgumentException("Time unit must not be null");
        }

        logger.info("Scheduling '{}' @{}s", schedulable, timeUnit.toMillis(period) / 1000.0);

        final Runnable runnable = new ScheduledExecutorServiceRunnable(schedulable);
        final ScheduledFuture<?> future = _executorService.scheduleAtFixedRate(runnable, period, period, timeUnit);

        schedulable.onScheduled();

        return new ScheduledExecutorServiceJob(future, schedulable);
    }

    @Override
    public void shutdown() {
        _executorService.shutdown();
    }

    /**
     * Represents a runnable.
     */
    private static final class ScheduledExecutorServiceRunnable implements Runnable {

        /**
         * The runnable.
         */
        private final Runnable _runnable;

        /**
         * Initializes a new instance of the {@link ScheduledExecutorServiceRunnable} class.
         * 
         * @param runnable The runnable.
         */
        public ScheduledExecutorServiceRunnable(final Runnable runnable) {
            _runnable = runnable;
        }

        @Override
        public void run() {
            try {
                _runnable.run();
            } catch (final Throwable t) {
                logger.warn("Job execution failed", t);
            }
        }
    }

    /**
     * Represents a job.
     */
    private static final class ScheduledExecutorServiceJob implements Job {

        /**
         * The scheduled future.
         */
        private final ScheduledFuture<?> _future;

        /**
         * The schedulable.
         */
        private final Schedulable _schedulable;

        /**
         * Initializes a new instance of the {@link ScheduledExecutorServiceJob} class.
         * 
         * @param future The scheduled future.
         * @param schedulable The schedulable.
         */
        public ScheduledExecutorServiceJob(final ScheduledFuture<?> future, final Schedulable schedulable) {
            _future = future;
            _schedulable = schedulable;
        }

        @Override
        public boolean cancel() {
            logger.info("Canceling '{}'", _schedulable);

            try {
                return _future.cancel(false);
            } finally {
                _schedulable.onCancelled();
            }
        }
    }
}

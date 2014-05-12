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

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a {@link Timer} based scheduler.
 */
public final class TimerScheduler implements Scheduler {

    /**
     * The default timer name.
     */
    private static final String DEFAULT_TIMER_NAME = "Scheduler-Timer";

    /**
     * The private logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(TimerScheduler.class);

    /**
     * The timer.
     */
    private final Timer _timer;

    /**
     * Initializes a new instance of the {@link TimerScheduler} class.
     */
    public TimerScheduler() {
        this(new Timer(DEFAULT_TIMER_NAME));
    }

    /**
     * Initializes a new instance of the {@link TimerScheduler} class.
     * 
     * @param timer The timer.
     */
    public TimerScheduler(final Timer timer) {
        if (timer == null) {
            throw new IllegalArgumentException("Timer must not be null");
        }

        _timer = timer;
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

        final TimerTaskJob job = new TimerTaskJob(schedulable);

        _timer.scheduleAtFixedRate(job, timeUnit.toMillis(period), timeUnit.toMillis(period));
        schedulable.onScheduled();

        return job;
    }

    @Override
    public void shutdown() {
        _timer.cancel();
    }

    /**
     * Represents a job.
     */
    private static final class TimerTaskJob extends TimerTask implements Job {

        /**
         * The schedulable.
         */
        private final Schedulable _schedulable;

        /**
         * Initializes a new instance of the {@link TimerTaskJob} class.
         * 
         * @param schedulable The schedulable.
         */
        public TimerTaskJob(final Schedulable schedulable) {
            _schedulable = schedulable;
        }

        @Override
        public void run() {
            try {
                _schedulable.run();
            } catch (final Throwable t) {
                logger.warn("Job execution failed", t);
            }
        }

        @Override
        public boolean cancel() {
            logger.info("Canceling '{}'", _schedulable);

            try {
                return super.cancel();
            } finally {
                _schedulable.onCancelled();
            }
        }
    }
}

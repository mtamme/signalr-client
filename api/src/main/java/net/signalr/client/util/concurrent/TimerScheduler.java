/*
 * Copyright © Martin Tamme
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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

        logger.info("Scheduling '{}'", schedulable);

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
            try {
                return super.cancel();
            } finally {
                _schedulable.onCancelled();
            }
        }
    }
}

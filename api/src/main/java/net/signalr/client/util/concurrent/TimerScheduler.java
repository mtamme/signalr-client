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

import java.util.HashMap;
import java.util.Map;
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
     * The private logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(TimerScheduler.class);

    /**
     * The timer.
     */
    private final Timer _timer;

    /**
     * The scheduled jobs.
     */
    private final Map<String, Job> _jobs;

    /**
     * Initializes a new instance of the {@link TimerScheduler} class.
     */
    public TimerScheduler() {
        _timer = new Timer("Scheduler-Timer");
        _jobs = new HashMap<String, Job>();
    }

    /**
     * Adds a new job to the scheduler.
     * 
     * @param name The job name.
     * @param runnable The runnable.
     * @return A new job.
     */
    private Job addJob(final String name, final Runnable runnable) {
        final Job job = new Job(runnable);

        synchronized (_jobs) {
            if (_jobs.containsKey(name)) {
                throw new IllegalArgumentException("Job with the same name already exists");
            }
            _jobs.put(name, job);
        }

        return job;
    }

    /**
     * Removes a job from the scheduler.
     * 
     * @param name The job name.
     * @return The job.
     */
    private Job removeJob(final String name) {
        final Job job;

        synchronized (_jobs) {
            job = _jobs.remove(name);
        }

        if (job == null) {
            throw new IllegalArgumentException("Job does not exist");
        }

        return job;
    }

    @Override
    public void scheduleJob(final String name, final Runnable runnable, final long initialDelay, final long period, final TimeUnit timeUnit) {
        if (name == null) {
            throw new IllegalArgumentException("Name must not be null");
        }
        if (runnable == null) {
            throw new IllegalArgumentException("Runnable must not be null");
        }
        if (timeUnit == null) {
            throw new IllegalArgumentException("Time unit must not be null");
        }

        logger.info("Scheduling '{}' job...", name);

        final Job job = addJob(name, runnable);

        _timer.scheduleAtFixedRate(job, timeUnit.toMillis(initialDelay), timeUnit.toMillis(period));
    }

    @Override
    public void unscheduleJob(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name must not be null");
        }

        logger.info("Unscheduling '{}' job...", name);

        final Job job = removeJob(name);

        job.cancel();
    }

    /**
     * Represents a scheduled job.
     */
    private static final class Job extends TimerTask {

        /**
         * The runnable.
         */
        private final Runnable _runnable;

        /**
         * Initializes a new instance of the {@link Job} class.
         * 
         * @param runnable The runnable.
         */
        public Job(final Runnable runnable) {
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
}
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

import java.util.concurrent.TimeUnit;

/**
 * Defines a scheduler.
 */
public interface Scheduler {

    /**
     * Adds a new job to the scheduler.
     * 
     * @param name The job name.
     * @param runnable The runnable.
     * @param initialDelay The initial delay.
     * @param period The period.
     * @param timeUnit The time unit.
     */
    void scheduleJob(String name, Runnable runnable, long initialDelay, long period, TimeUnit timeUnit);

    /**
     * Removes a job from the scheduler.
     * 
     * @param name The job name.
     */
    void unscheduleJob(String name);
}

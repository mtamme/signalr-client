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

import java.util.concurrent.TimeUnit;

/**
 * Defines a scheduler.
 */
public interface Scheduler {

    /**
     * Adds a new job to the scheduler.
     * 
     * @param schedulable The schedulable.
     * @param period The period.
     * @param timeUnit The time unit.
     * @return The scheduled job.
     */
    Job scheduleJob(Schedulable schedulable, long period, TimeUnit timeUnit);

    /**
     * Initiates an orderly shutdown.
     */
    void shutdown();
}

/*
 * Copyright (c) 2012 Petter Holmström
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
package net.pkhsolutions.ceres.eventbus;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * An asynchronous implementation of {@link EventBus} that invokes the listeners
 * using an {@link ExecutorService}. Every listener invocation is submitted as a
 * separate job. By default, a cached thread pool is used to execute the jobs.
 * Subclasses can change this by overriding {@link #createExecutorService() }.
 * <p> <b>Note!</b> When using this event bus, make sure that the listeners are
 * thread safe!
 *
 * @author Petter Holmström
 * @since 1.0
 */
public class AsynchronousEventBus extends AbstractEventBus {

    @Override
    protected void doPublishEvent(final Event<?> event) {
        final ExecutorService execService = createExecutorService();
        for (final EventListener listener : getEventListeners()) {
            if (listener.supports(event)) {
                execService.submit(new Runnable() {

                    @Override
                    public void run() {
                        listener.handleEvent(event);
                    }
                });
            }
        }
        execService.shutdown();
    }

    /**
     * Creates the executor service to use when notifying the event listeners.
     * The default implementation calls {@link Executors#newCachedThreadPool()
     * }, subclasses may override.
     */
    protected ExecutorService createExecutorService() {
        return Executors.newCachedThreadPool();
    }
}

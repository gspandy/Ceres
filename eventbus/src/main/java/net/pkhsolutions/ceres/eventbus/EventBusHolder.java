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

/**
 * This class provides static methods for setting and accessing an event bus. It
 * can be used to simplify code where multiple objects need access to the event
 * bus. Instead of passing in the event bus instance to all objects, they can
 * look it up themselves. <p> Note, however, that if you are using a dependency
 * injection framework (such as CDI or Spring), it is probably better to use
 * that to inject the event bus reference into the objects that need it. <p>
 * This class is thread safe.
 *
 * @author Petter Holmström
 * @since 1.0
 */
public class EventBusHolder {

    private static volatile EventBusHolderStrategy strategy = new ThreadLocalEventBusHolderStrategy();

    private EventBusHolder() {
    }

    /**
     * Gets the event bus instance.
     *
     * @return the event bus instance, or null if none has been set.
     */
    public static EventBus getEventBus() {
        return strategy.get();
    }

    /**
     * Sets the event bus instance.
     *
     * @param eventBus the event bus instance, may be null.
     */
    public static void setEventBus(EventBus eventBus) {
        strategy.set(eventBus);
    }

    /**
     * Sets the strategy to use for storing and retrieving the event bus
     * instance.
     *
     * @param strategy the strategy to use, or null to use a {@link ThreadLocalEventBusHolderStrategy}
     * (the default).
     */
    public static synchronized void setStrategy(EventBusHolderStrategy strategy) {
        if (strategy == null) {
            strategy = new ThreadLocalEventBusHolderStrategy();
        }
        EventBusHolder.strategy = strategy;
    }
}

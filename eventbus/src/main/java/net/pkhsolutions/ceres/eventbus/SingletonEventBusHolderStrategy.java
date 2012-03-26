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
 * {@link EventBusHolderStrategy} that stores the event bus instance in a local
 * field that is accessible by all threads. This class is thread safe.
 *
 * @author Petter Holmström
 */
public class SingletonEventBusHolderStrategy implements EventBusHolderStrategy {

    private EventBus eventBus;

    @Override
    public synchronized EventBus get() {
        return eventBus;
    }

    @Override
    public synchronized void set(EventBus eventBus) {
        this.eventBus = eventBus;
    }
}

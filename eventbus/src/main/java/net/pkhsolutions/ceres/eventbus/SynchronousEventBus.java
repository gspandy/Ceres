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
 * A synchronous implementation of {@link EventBus} that invokes the listeners
 * in the same thread that published the event, immediately after the event is
 * published.
 *
 * @author Petter Holmström
 * @since 1.0
 */
public class SynchronousEventBus extends AbstractEventBus {

    @Override
    protected void doPublishEvent(Event<?> event) {
        for (EventListener listener : getEventListeners()) {
            if (listener.supports(event)) {
                listener.handleEvent(event);
            }
        }
    }
}

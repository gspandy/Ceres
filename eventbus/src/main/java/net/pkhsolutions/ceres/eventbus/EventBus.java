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
 * This interface defines an event bus that publishes events to listeners. Event
 * buses can be chained in a parent-child hierarchy. Events will always
 * propagate down to the child buses, but only up to the parent bus if the scope
 * is {@link EventScope#GLOBAL}. Any object whose class contains a method with
 * the {@link EventListenerMethod} annotation can register itself as a listener
 * of the event bus.
 *
 * @author Petter Holmström
 * @since 1.0
 */
public interface EventBus {

    /**
     * Gets the parent event bus of this event bus. Events published on the
     * parent bus will also be published on this bus. Global events published on
     * this bus will also be published on the parent bus.
     *
     * @return the parent event bus, or null if there is none.
     */
    EventBus getParentBus();

    /**
     * Sets the parent event bus of this event bus.
     *
     * @see #getParentBus()
     *
     * @param parentBus the parent event bus, may be null.
     */
    void setParentBus(EventBus parentBus);

    /**
     * Publishes an event with the specified payload and scope.
     *
     * @see Event#getPayload()
     * @see EventScope
     *
     * @param payload the event payload, must not be null.
     * @param scope the scope of the event, must not be null.
     */
    <T> void publishEvent(T payload, EventScope scope);

    /**
     * Publishes the specified event. If the event is null, nothing happens.
     *
     * @param event the event to publish, may be null.
     */
    void publishEvent(Event<?> event);

    /**
     * Registers the specified listener. Unless stated otherwise in the
     * implementation's documentation, a weak reference will be used to store
     * the listener, so forgetting to unregister the listener will not prevent
     * it from being garbage collected. To actually get notified of events, the
     * listener should define one or more methods and annotate them with {@link EventListenerMethod}.
     *
     * @param listener the listener to register, may be null.
     */
    void registerEventListener(Object listener);

    /**
     * Unregisters the specified listener. If the listener is null, nothing
     * happens.
     *
     * @param listener the listener to register, may be null.
     */
    void unregisterEventListener(Object listener);
}

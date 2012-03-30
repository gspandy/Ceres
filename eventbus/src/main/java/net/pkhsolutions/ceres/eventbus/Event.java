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

import java.util.LinkedList;
import java.util.Queue;

/**
 * This class represents an event published on an event bus. The event has a
 * payload that can be any object. An event listener must know which payload
 * type to listen for by declaring the {@link EventListenerMethod} properly.
 *
 * @see EventListenerMethod
 *
 * @author Petter Holmström
 * @since 1.0
 */
public class Event<T> {

    private final T payload;
    private final EventScope scope;
    private final long timestamp;
    private final Class<T> payloadType;
    private final Queue<EventBus> publicationHistory = new LinkedList<EventBus>();

    /**
     * Creates a new
     * <code>Event</code>.
     *
     * @param payload the payload of the event, must not be null.
     * @param originalEventBus the event bus that originally published the
     * event, must not be null.
     * @param scope the scope of the event, must not be null.
     */
    public Event(T payload, EventBus originalEventBus, EventScope scope) {
        assert payload != null : "payload must not be null";
        assert originalEventBus != null : "originalEventBus must not be null";
        assert scope != null : "scope must not be null";

        this.payload = payload;
        publicationHistory.add(originalEventBus);
        this.scope = scope;
        this.timestamp = System.currentTimeMillis();
        payloadType = (Class<T>) payload.getClass();
    }

    /**
     * Gets the payload of the event. This can be any object.
     *
     * @return the payload, never null.
     */
    public T getPayload() {
        return payload;
    }

    /**
     * Gets the type of the payload.
     *
     * @see #getPayload()
     *
     * @return the payload type, never null.
     */
    public Class<T> getPayloadType() {
        return payloadType;
    }

    /**
     * Gets the event bus on which the event was originally published.
     *
     * @return the event bus, never null.
     */
    public synchronized EventBus getOriginalEventBus() {
        return publicationHistory.peek();
    }

    /**
     * Gets the publication history of the event in the form of a queue
     * containing all event buses on which the event has been published. The
     * head of the queue will contain the original event bus.
     *
     * @see #getOriginalEventBus()
     * @see
     * #addEventBusToPublicationHistory(net.pkhsolutions.ceres.eventbus.EventBus)
     *
     * @return a queue of event buses, never null.
     */
    public synchronized Queue<EventBus> getPublicationHistory() {
        return new LinkedList<EventBus>(publicationHistory);
    }

    /**
     * Adds the specified event bus to the publication history queue. All event
     * buses that publish this event should call this method. To prevent an even
     * from being published multiple times on the same bus, the bus should also
     * check that it has not been added to the publication history earlier.
     *
     * @param eventBus the event bus to add, must not be null.
     */
    protected synchronized void addEventBusToPublicationHistory(EventBus eventBus) {
        assert eventBus != null : "eventBus must not be null";
        publicationHistory.add(eventBus);
    }

    /**
     * Gets the scope of the event.
     *
     * @return the event scope, never null.
     */
    public EventScope getScope() {
        return scope;
    }

    /**
     * Gets the timestamp of when this event was created.
     *
     * @see System#currentTimeMillis()
     * @return the timestamp in milliseconds.
     */
    public long getTimestamp() {
        return timestamp;
    }
}

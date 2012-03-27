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
    private final EventBus originalEventBus;
    private final EventScope scope;
    private final long timestamp;
    private final Class<T> payloadType;

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
        this.originalEventBus = originalEventBus;
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
    public EventBus getOriginalEventBus() {
        return originalEventBus;
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

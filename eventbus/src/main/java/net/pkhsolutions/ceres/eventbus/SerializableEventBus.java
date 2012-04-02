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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * This is a wrapper class that makes the wrapped {@link EventBus} serializable.
 * Any serializable listeners and/or serializable parent bus are retained.
 * Please note though, that the serializable listeners will be stored using
 * strong references even though the underlying implementation might use weak
 * references for storing them.
 *
 * @see #createAsynchronousSerializableEventBus()
 * @see #createSynchronousSerializableEventBus()
 *
 * @author Petter Holmström
 * @since 1.0
 */
public class SerializableEventBus implements EventBus, Serializable {

    private static final long serialVersionUID = -5653511946728206137L;

    /**
     * Factory interface to be used by {@link SerializableEventBus} to create
     * new wrapped event buses upon object creation and deserialization.
     *
     * @author Petter Holmström
     * @since 1.0
     */
    public interface EventBusFactory extends Serializable {

        /**
         * Creates a new event bus.
         *
         * @return a new event bus instance, never null.
         */
        EventBus createEventBus();
    }
    private final EventBusFactory eventBusFactory;
    private final Set<Serializable> serializableListeners = new HashSet<Serializable>();
    private EventBus serializableParentBus;
    private transient EventBus wrappedEventBus;

    /**
     * Creates a new
     * <code>SerializableEventBus</code>, using the specified event bus factory
     * to create the wrapped event bus instance.
     *
     * @param eventBusFactory the event bus factory, must not be null.
     */
    public SerializableEventBus(EventBusFactory eventBusFactory) {
        assert eventBusFactory != null : "eventBusFactory must not be null";
        this.eventBusFactory = eventBusFactory;
        wrappedEventBus = eventBusFactory.createEventBus();
    }

    private void readObject(ObjectInputStream is) throws ClassNotFoundException, IOException {
        is.defaultReadObject();
        wrappedEventBus = eventBusFactory.createEventBus();
        for (Object serializableListener : serializableListeners) {
            wrappedEventBus.registerEventListener(serializableListener);
        }
        wrappedEventBus.setParentBus(serializableParentBus);
    }

    /**
     * Gets the wrapped event bus.
     *
     * @return the wrapped event bus, never null.
     */
    protected synchronized EventBus getWrappedEventBus() {
        return wrappedEventBus;
    }

    @Override
    public EventBus getParentBus() {
        return getWrappedEventBus().getParentBus();
    }

    @Override
    public synchronized void setParentBus(EventBus parentBus) {
        getWrappedEventBus().setParentBus(parentBus);
        if (parentBus instanceof Serializable) {
            serializableParentBus = parentBus;
        } else {
            serializableParentBus = null;
        }
    }

    @Override
    public <T> void publishEvent(T payload, EventScope scope) {
        getWrappedEventBus().publishEvent(payload, scope);
    }

    @Override
    public void publishEvent(Event<?> event) {
        getWrappedEventBus().publishEvent(event);
    }

    /**
     * <b>Note! In this implementation, if the listener is serializable, a
     * strong reference will be used to store it!</b> In other words, remember
     * to unregister the listener if you want to dispose of it! <p> {@inheritDoc
     * }
     */
    @Override
    public synchronized void registerEventListener(Object listener) {
        getWrappedEventBus().registerEventListener(listener);
        if (listener instanceof Serializable) {
            serializableListeners.add((Serializable) listener);
        }
    }

    @Override
    public synchronized void unregisterEventListener(Object listener) {
        getWrappedEventBus().unregisterEventListener(listener);
        if (listener instanceof Serializable) {
            serializableListeners.remove((Serializable) listener);
        }
    }

    /**
     * Creates and returns a new serializable {@link SynchronousEventBus}.
     *
     * @return a new event bus instance, never null.
     */
    public static SerializableEventBus createSynchronousSerializableEventBus() {
        return new SerializableEventBus(new EventBusFactory() {

            private static final long serialVersionUID = 8651545956045591819L;

            @Override
            public EventBus createEventBus() {
                return new SynchronousEventBus();
            }
        });
    }

    /**
     * Creates and returns a new serializable {@link AsynchronousEventBus}.
     *
     * @return a new event bus instance, never null.
     */
    public static SerializableEventBus createAsynchronousSerializableEventBus() {
        return new SerializableEventBus(new EventBusFactory() {

            private static final long serialVersionUID = 8651545956045591819L;

            @Override
            public EventBus createEventBus() {
                return new AsynchronousEventBus();
            }
        });
    }
}

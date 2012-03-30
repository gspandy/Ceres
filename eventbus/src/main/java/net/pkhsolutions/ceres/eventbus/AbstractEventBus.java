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

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is an abstract base class for {@link EventBus} implementations. It
 * supports registration and unregistration of event listeners, detection and
 * invocation of event listener methods and event propagation to/from a parent
 * event bus.<p>This class has been specifically designed to act as a super
 * class for {@link SynchronousEventBus} and {@link AsynchronousEventBus} and
 * may not be that useful for other event bus implementations.
 *
 * @see SynchronousEventBus
 * @see AsynchronousEventBus
 *
 * @author Petter Holmström
 * @since 1.0
 */
public abstract class AbstractEventBus implements EventBus {

    private EventBus parentBus;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final WeakHashMap<Object, EventListener> listeners = new WeakHashMap<Object, EventListener>();

    /**
     * Event listener method that is called when a new event is published on the
     * parent bus. If the event has not been published on this particular bus
     * before, it is published. Otherwise nothing happens.
     *
     * @see EventListenerMethod
     * @see Event#getPublicationHistory()
     *
     * @param event the published event, must not be null.
     */
    @EventListenerMethod
    protected void handleParentEvent(Event<Object> event) {
        assert event != null : "event must not be null";
        if (!event.getPublicationHistory().contains(this)) {
            logger.debug("Received event {} from parent, publishing it", event);
            event.addEventBusToPublicationHistory(this);
            doPublishEvent(event);
        }
    }

    @Override
    public EventBus getParentBus() {
        synchronized (this) {
            return parentBus;
        }
    }

    @Override
    public void registerEventListener(Object listener) {
        if (listener == null) {
            return;
        }
        EventListener eventListener = new EventListener(listener);
        synchronized (this) {
            listeners.put(listener, eventListener);
        }
    }

    @Override
    public void setParentBus(EventBus parentBus) {
        synchronized (this) {
            if (this.parentBus != null) {
                this.parentBus.unregisterEventListener(this);
            }
            this.parentBus = parentBus;
            if (this.parentBus != null) {
                this.parentBus.registerEventListener(this);
            }
            logger.debug("Using parent {}", parentBus);
        }
    }

    @Override
    public void unregisterEventListener(Object listener) {
        if (listener == null) {
            return;
        }
        synchronized (this) {
            listeners.remove(listener);
        }
    }

    @Override
    public <T> void publishEvent(T payload, EventScope scope) {
        assert payload != null : "payload must not be null";
        assert scope != null : "scope must not be null";
        Event<T> event = new Event<T>(payload, this, scope);
        publishEvent(event);
    }

    @Override
    public void publishEvent(Event<?> event) {
        if (event == null) {
            return;
        }
        logger.debug("Publishing event {}", event);
        if (event.getOriginalEventBus() != this) {
            event.addEventBusToPublicationHistory(this);
        }
        doPublishEvent(event);
        /*
         * Store a local reference to the parent bus in case another thread
         * changes it in the middle of the execution of this method.
         */
        EventBus parentBusToPublishTo;
        synchronized (this) {
            parentBusToPublishTo = this.parentBus;
        }
        if (event.getScope().equals(EventScope.GLOBAL) && parentBusToPublishTo != null) {
            logger.debug("Scope of {} is GLOBAL, publishing event on parent bus {}", event, parentBusToPublishTo);
            parentBusToPublishTo.publishEvent(event);
        }
    }

    /**
     * Gets a collection of all registered event listeners
     *
     * @return a copy of the collection of event listeners, never null.
     */
    protected Collection<EventListener> getEventListeners() {
        synchronized (this) {
            return new HashSet<EventListener>(listeners.values());
        }
    }

    /**
     * Publishes the specified event on this bus, notifying the registered
     * listeners.
     *
     * @see #getEventListeners()
     * @see EventListener#handleEvent(net.pkhsolutions.ceres.eventbus.Event)
     * @see EventListener#supports(net.pkhsolutions.ceres.eventbus.Event)
     *
     * @param event the event to publish, must not be null.
     */
    protected abstract void doPublishEvent(Event<?> event);

    /**
     * This class is used to wrap real event listener objects. It contains
     * methods for analyzing ad invoking event listener methods.
     *
     * @see EventListenerMethod
     *
     * @author Petter Holmström
     * @since 1.0
     */
    protected static class EventListener {

        private final WeakReference<Object> listenerRef;
        private final Set<Method> listenerMethods;

        /**
         * Creates a new
         * <code>EventListener</code>.
         *
         * @param listener the real event listener object, must not be null.
         */
        public EventListener(Object listener) {
            assert listener != null : "listener must not be null";
            listenerRef = new WeakReference<Object>(listener);
            listenerMethods = Collections.unmodifiableSet(findListenerMethods(listener.getClass()));
        }

        /**
         * Checks if the event listener supports the specified event. That is,
         * the wrapped event listener has at least one event listener method
         * that takes the given event as a parameter.
         *
         * @see EventListenerMethod
         *
         * @param event the event to check, must not be null.
         * @return true if the event listener supports the event, false
         * otherwise.
         */
        public boolean supports(Event<?> event) {
            assert event != null : "event must not be null";
            return !getListenerMethods(event.getPayloadType()).isEmpty();
        }

        /**
         * Handles the specified event by invoking all event listener methods of
         * the wrapped event listener that take the given event as a parameter.
         * If the event is not supported by the listener, nothing happens.
         *
         * @see #supports(net.pkhsolutions.ceres.eventbus.Event)
         * @see EventListenerMethod
         *
         * @param event the event to handle, must not be null.
         */
        public void handleEvent(Event<?> event) {
            assert event != null : "event must not be null";
            final Set<Method> listenerMethodsToInvoke = getListenerMethods(event.getPayloadType());
            if (listenerMethodsToInvoke.isEmpty()) {
                return;
            }
            final Object target = listenerRef.get();
            if (target != null) {
                for (Method listenerMethod : listenerMethodsToInvoke) {
                    invokeMethod(listenerMethod, target, event);
                }
            }
        }

        private static void invokeMethod(Method m, Object target, Object... args) {
            final boolean oldAccessible = m.isAccessible();
            try {
                m.setAccessible(true);
                m.invoke(target, args);
            } catch (Exception e) {
                throw new RuntimeException("Error invoking method " + m.getName(), e);
            } finally {
                m.setAccessible(oldAccessible);
            }
        }

        private Set<Method> getListenerMethods(Class<?> payloadType) {
            HashSet<Method> methods = new HashSet<Method>();
            for (Method listenerMethod : listenerMethods) {
                ParameterizedType eventType = (ParameterizedType) listenerMethod.getGenericParameterTypes()[0];
                Type acceptedPayloadType = eventType.getActualTypeArguments()[0];
                if (acceptedPayloadType instanceof Class) {
                    if (((Class<?>) acceptedPayloadType).isAssignableFrom(payloadType)) {
                        methods.add(listenerMethod);
                    }
                }
            }
            return methods;
        }

        private static Set<Method> findListenerMethods(Class<?> listenerClass) {
            HashSet<Method> methods = new HashSet<Method>();
            Class<?> classToInspect = listenerClass;
            while (classToInspect != Object.class && classToInspect != null) {
                for (Method m : classToInspect.getDeclaredMethods()) {
                    if (m.isAnnotationPresent(EventListenerMethod.class)
                            && m.getParameterTypes().length == 1
                            && m.getParameterTypes()[0] == Event.class) {
                        methods.add(m);
                    }
                }
                if (!classToInspect.isInterface()) {
                    for (Class<?> interfaceToInspect : classToInspect.getInterfaces()) {
                        methods.addAll(findListenerMethods(interfaceToInspect));
                    }
                }
                classToInspect = classToInspect.getSuperclass();
            }
            return methods;
        }
    }
}

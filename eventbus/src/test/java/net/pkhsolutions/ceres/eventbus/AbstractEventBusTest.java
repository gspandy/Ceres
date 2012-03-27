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

import org.apache.commons.lang.ObjectUtils;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

/**
 * Abstract base class for tests of classes that extend {@link AbstractEventBus}.
 *
 * @author Petter Holmström
 */
public abstract class AbstractEventBusTest {

    interface ExampleListener {

        @EventListenerMethod
        void allEvents(Event<Object> event);

        @EventListenerMethod
        void stringEventsOnly(Event<String> event);

        @EventListenerMethod
        void integerEventsOnly(Event<Integer> event);
    }
    ExampleListener childListener1;
    ExampleListener childListener2;
    ExampleListener parentListener;
    EventBus parentBus;
    EventBus childBus1;
    EventBus childBus2;

    @Before
    public void setUp() {
        parentBus = createEventBus();

        childBus1 = createEventBus();
        childBus1.setParentBus(parentBus);

        childBus2 = createEventBus();
        childBus2.setParentBus(parentBus);

        parentListener = mock(ExampleListener.class);
        parentBus.registerEventListener(parentListener);

        childListener1 = mock(ExampleListener.class);
        childBus1.registerEventListener(childListener1);

        childListener2 = mock(ExampleListener.class);
        childBus2.registerEventListener(childListener2);
    }

    protected abstract EventBus createEventBus();

    static class EventMatcher<T> extends BaseMatcher<Event<T>> {

        private final EventBus expectedEventBus;
        private final T expectedPayload;

        public EventMatcher(EventBus expectedEventBus, T expectedPayload) {
            this.expectedEventBus = expectedEventBus;
            this.expectedPayload = expectedPayload;
        }

        @Override
        public boolean matches(Object item) {
            if (item instanceof Event) {
                Event<T> event = (Event<T>) item;
                return event.getOriginalEventBus() == expectedEventBus
                        && ObjectUtils.equals(event.getPayload(), expectedPayload);
            }
            return false;
        }

        @Override
        public void describeTo(Description description) {
        }
    }

    static <T> EventMatcher<T> matchesEvent(EventBus expectedEventBus, T expectedPayload) {
        return new EventMatcher<T>(expectedEventBus, expectedPayload);
    }

    @Test
    public void localStringEvent() {
        childBus1.publishEvent("hello world", EventScope.LOCAL);
        verify(childListener1).stringEventsOnly(argThat(matchesEvent(childBus1, "hello world")));
        verify(childListener1).allEvents(argThat(matchesEvent(childBus1, (Object) "hello world")));
        verifyNoMoreInteractions(childListener1);
        verifyZeroInteractions(childListener2, parentListener);
    }

    @Test
    public void localIntegerEvent() {
        childBus1.publishEvent(1234, EventScope.LOCAL);
        verify(childListener1).integerEventsOnly(argThat(matchesEvent(childBus1, 1234)));
        verify(childListener1).allEvents(argThat(matchesEvent(childBus1, (Object) 1234)));
        verifyNoMoreInteractions(childListener1);
        verifyZeroInteractions(childListener2, parentListener);
    }

    @Test
    public void localBooleanEvent() {
        childBus1.publishEvent(true, EventScope.LOCAL);
        verify(childListener1).allEvents(argThat(matchesEvent(childBus1, (Object) true)));
        verifyNoMoreInteractions(childListener1);
        verifyZeroInteractions(childListener2, parentListener);
    }

    @Test
    public void globalStringEvent() {
        childBus1.publishEvent("hello world", EventScope.GLOBAL);

        verify(childListener1).stringEventsOnly(argThat(matchesEvent(childBus1, "hello world")));
        verify(childListener1).allEvents(argThat(matchesEvent(childBus1, (Object) "hello world")));

        verify(parentListener).stringEventsOnly(argThat(matchesEvent(childBus1, "hello world")));
        verify(parentListener).allEvents(argThat(matchesEvent(childBus1, (Object) "hello world")));

        verify(childListener2).stringEventsOnly(argThat(matchesEvent(childBus1, "hello world")));
        verify(childListener2).allEvents(argThat(matchesEvent(childBus1, (Object) "hello world")));

        verifyNoMoreInteractions(childListener1, childListener2, parentListener);
    }
}

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

import java.lang.annotation.*;

/**
 * This is an annotation used to mark methods of a listener class that get
 * invoked by an event bus when events are published. The annotated method can
 * have any visibility (even private), but must accept exactly one parameter of
 * type {@link Event}. The generic payload parameter indicates what kind of
 * events the method is able to process. For example, this method will be
 * invoked for all events whose payload is of type
 * <code>String</code>: <p>
 * <code>@EventListenerMethod handleStringEvents(Event&lt;String&gt; event)</code>
 * <p> The payload type can also be a super type of the actual event payload
 * type. For example, a method with a payload type of
 * <code>Object</code> will receive all events: <p>
 * <code>@EventListenerMethod handleAllEvents(Event&lt;Object&gt; event)</code>
 * <p> Wildcards may <b>not</b> be used in the parameter. For example, the
 * following <b>will not work</b>: <p>
 * <code>@EventListenerMethod illegalEventHandler(Event&lt;?&gt; event)</code>
 * <p> If the annotated method does not conform to these specifications, it will
 * be ignored by the event bus.
 *
 * @author Petter Holmström
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventListenerMethod {
}

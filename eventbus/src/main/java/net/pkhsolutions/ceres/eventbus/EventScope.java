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
 * Enumeration defining the scope of an event published on an event bus. Events
 * can be either restricted to the local event bus (i.e. the bus on which the
 * event was originally published) or propagate to all event buses.
 *
 * @author Petter Holmström
 */
public enum EventScope {

    /**
     * The event is published on the local event bus only.
     */
    LOCAL,
    /**
     * The event is published on the local event bus and the parent event bus,
     * if available. If there are multiple chained event buses, the event will
     * eventually propagate to all of them.
     */
    GLOBAL
}

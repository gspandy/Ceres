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
package net.pkhsolutions.ceres.ddd;

import java.io.Serializable;

/**
 * Interface to be implemented by value objects. Value objects do not have
 * identity, only their value is of interest. In theory, two value objects with
 * the same value are interchangeable. However, this only applies to immutable
 * value objects. For mutable value objects, a copy of the object should always
 * be returned (or stored) instead of the object itself.
 *
 * @author Petter Holmström
 * @since 1.0
 * @param <T> the value object type.
 */
public interface ValueObject<T extends ValueObject<T>> extends Serializable {

    /**
     * Creates a deep copy of the value object.
     *
     * @return a copy of the value object, never null.
     */
    T copy();

    /**
     * Checks if this value object has the same value as the specified value
     * object.
     *
     * @param other the value object to compare to, must not be null.
     * @return true if the objects have the same value, false otherwise.
     */
    boolean sameValueAs(T other);
}

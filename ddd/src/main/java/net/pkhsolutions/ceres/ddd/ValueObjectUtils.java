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

/**
 * Utility class with methods for working with {@link ValueObject}s.
 *
 * @author Petter Holmström
 * @since 1.0
 */
public final class ValueObjectUtils {

    private ValueObjectUtils() {
    }

    /**
     * Returns a copy of the specified object, or null if the object is null.
     *
     * @param <T> the type of value object.
     * @param object the object to copy, may be null.
     * @return a value object or null.
     */
    public static <T extends ValueObject<T>> T copyOrNull(T object) {
        return object == null ? null : object.copy();
    }

    /**
     * Returns a copy of the specified object, or a new instance if the object
     * is null.
     *
     * @param <T> the type of value object.
     * @param object the object to copy, may be null.
     * @param type the value object class, must not be null.
     * @return a value object, never null.
     */
    public static <T extends ValueObject<T>> T copyOrNewInstance(T object, Class<T> type) {
        assert type != null : "type must not be null";
        if (object != null) {
            return object.copy();
        } else {
            try {
                return type.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Could not create new instance of value object", e);
            }
        }
    }
}

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
package net.pkhsolutions.ceres.common.holder;

/**
 * A holder strategy is used by {@link Holder} to actually store/retrieve
 * objects.
 *
 * @author Petter Holmström
 * @since 1.0
 * @param <T> the type of object stored by the strategy.
 */
public interface HolderStrategy<T> {

    /**
     * Retrieves the object.
     *
     * @return the object, or null if no object has been set.
     */
    T get();

    /**
     * Stores the object.
     *
     * @param object the object, or null to remove it from the strategy.
     */
    void set(T object);
}

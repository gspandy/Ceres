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
package net.pkhsolutions.ceres.common.visitor;

import java.util.Collection;
import net.pkhsolutions.ceres.common.annotations.NeverReturnsNull;

/**
 * This interface represents a collection of items that can be visited by a
 * visitor (as in the Visitor pattern).
 *
 * @author Petter Holmström
 * @since 1.0
 * @param <T> the type of the visitable items.
 */
public interface VisitableCollection<T> {

    /**
     * Adds the specified item to the collection. You can also invoke the
     * {@link Collection#add(Object)} method on the {@link #getItems()}
     * collection directly.
     *
     * @param item the item to add, must not be null.
     */
    void add(T item);

    /**
     * Removes the specified item from the collection. You can also invoke the
     * {@link Collection#remove(Object)} method on the {@link #getItems()}
     * collection directly.
     *
     * @param item the item to remove, must not be null.
     */
    void remove(T item);

    /**
     * Returns the collection of visitable items. The collection can be
     * modified.
     *
     * @return a collection, never null.
     */
    @NeverReturnsNull
    Collection<T> getItems();

    /**
     * Visits the collection of visitable items with the specified visitor.
     *
     * @param visitor the visitor, must not be null.
     */
    void visitItems(Visitor<T> visitor);
}

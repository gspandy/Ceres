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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Utility class for working with the Visitor pattern.
 *
 * @author Petter Holmström
 * @since 1.0
 */
public final class VisitorUtils {

    private VisitorUtils() {
    }

    /**
     * Visits the specified collection with the specified visitor.
     *
     * @param <T> the type of visitable items.
     * @param visitor the visitor, must not be null.
     * @param collection the collection to visit, must not be null.
     */
    public static <T> void visitCollection(Visitor<T> visitor, Collection<T> collection) {
        new VisitableCollectionWrapper<T>(collection).visitItems(visitor);
    }

    /**
     * Creates a new visitable collection that uses a {@link LinkedList} as the
     * backend.
     *
     * @param <T> the type of visitable items.
     * @return a new visitable collection, never null.
     */
    public static <T> VisitableCollection<T> visitableLinkedList() {
        return new VisitableCollectionWrapper<T>(new LinkedList<T>());
    }

    /**
     * Creates a new visitable collection that uses an {@link ArrayList} as the
     * backend.
     *
     * @param <T> the type of visitable items.
     * @return a new visitable collection, never null.
     */
    public static <T> VisitableCollection<T> visitableArrayList() {
        return new VisitableCollectionWrapper<T>(new ArrayList<T>());
    }

    /**
     * Creates a new visitable collection that uses a {@link HashSet} as the
     * backend.
     *
     * @param <T> the type of visitable items.
     * @return a new visitable collection, never null.
     */
    public static <T> VisitableCollection<T> visitableHashSet() {
        return new VisitableCollectionWrapper<T>(new HashSet<T>());
    }
}

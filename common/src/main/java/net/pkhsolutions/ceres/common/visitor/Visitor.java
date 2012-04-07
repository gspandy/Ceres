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

/**
 * Interface to be implemented by a visitor of a {@link VisitableCollection}.
 *
 * @author Petter Holmström
 * @since 1.0
 * @param <T> the type of the visitable item.
 */
public interface Visitor<T> {

    /**
     * Visits the specified item.
     *
     * @param visitable the item to visit, must not be null.
     */
    void visit(T visitable);
}

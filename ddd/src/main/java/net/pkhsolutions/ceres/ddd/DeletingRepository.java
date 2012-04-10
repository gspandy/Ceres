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
 * Repository interface that also supports deleting entities.
 *
 * @author Petter Holmström
 * @since 1.0
 * @param <ID> the type of the entity identity.
 * @param <T> the type of the entities stored in this repository.
 */
public interface DeletingRepository<ID extends Serializable, T extends Entity<ID, T>> extends Repository<ID, T> {

    /**
     * Deletes the specified entity from the repository. If the entity does not
     * exist, nothing happens. If the entity exists, but cannot be deleted for
     * some reason, an implementation specific unchecked exception will be
     * thrown.
     *
     * @param entity the entity to delete, must not be null.
     * @throws ConcurrentModificationException if the entity has been modified
     * by another user after it was retrieved from the repository.
     */
    void delete(T entity) throws ConcurrentModificationException;
}

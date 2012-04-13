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
package net.pkhsolutions.ceres.ddd.jpa;

import java.lang.reflect.Field;
import javax.persistence.Embeddable;
import net.pkhsolutions.ceres.ddd.ValueObject;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Base class for value objects that are persisted as JPA {@link Embeddable}
 * types.
 *
 * @author Petter Holmström
 * @since 1.0
 * @param <T> the value object type.
 */
public abstract class AbstractJpaEmbeddableValueObject<T extends AbstractJpaEmbeddableValueObject<T>> implements ValueObject<T> {

    private static final long serialVersionUID = 3220995639543585720L;

    /**
     * This implementation invokes {@link #sameValueAs(net.pkhsolutions.ceres.ddd.ValueObject)
     * } to perform the equality check. <p> {@inheritDoc }
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        return sameValueAs((T) obj);
    }

    /**
     * This implementation uses reflection to calculate the hash code based on
     * all declared fields in this class and all super classes except
     * <code>Object</code>. Subclasses that do not want to include all fields in
     * the calculation, or that do no want to use reflection, may override. <p>
     * {@inheritDoc }
     */
    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        try {
            Class<?> clazz = getClass();
            while (clazz != Object.class) {
                for (Field f : clazz.getDeclaredFields()) {
                    final boolean oldAccessible = f.isAccessible();
                    f.setAccessible(true);
                    try {
                        builder.append(f.get(this));
                    } finally {
                        f.setAccessible(oldAccessible);
                    }
                }
                clazz = clazz.getSuperclass();
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not calculate hashcode", e);
        }
        return builder.toHashCode();
    }
}

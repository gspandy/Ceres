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
package net.pkhsolutions.ceres.common.cloning;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;
import java.util.Map;

/**
 * This annotation is used to annotate fields that should be cloned when their
 * owning object is cloned (i.e. a deep clone instead of a shallow clone). The
 * actual method that performs the cloning is
 * {@link CloneUtil#deepClone(Cloneable)}.
 *
 * @author Petter Holmström
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CloneThis {

    /**
     * This attribute applies to {@link Collection}s, {@link Map}s and arrays
     * only and is ignored if set on fields of other types. This attribute
     * specifies whether the fields should be deeply cloned (i.e. the
     * collection/map/array items are also cloned) or shallowly cloned (i.e. the
     * collection/map/array instance is cloned, but the items are not).
     *
     * @return true if the field should be deeply cloned, false if it should be
     * shallowly cloned.
     */
    boolean deepClone() default false;
}

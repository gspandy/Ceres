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
package net.pkhsolutions.ceres.common.builder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be placed on either fields or constructor parameters
 * (depending on the data population strategy used) to inform the annotation
 * processor of attributes that must be set before a valid object instance can
 * be built. <p> For example, consider the following code:
 * <code>new MyBuilder().build()</code>. If there are no
 * <code>&#64;Required</code> annotations, a new, valid instance of the
 * buildable class will be returned. On the other hand, if there are
 * <code>&#64;Required</code> annotations, an exception will be thrown.
 *
 * @see Buildable#populationStrategy() 
 *
 * @author Petter Holmström
 * @see 1.0
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.CLASS)
public @interface Required {

    /**
     * Specifies whether the attribute can take null values. If true, the
     * required attribute must be set before an object can be built, but it may
     * be set to null. For objects that cannot be set to null, this parameter is
     * ignored.
     */
    boolean allowNulls() default false;
}

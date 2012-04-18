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
 * This annotation is optionally used on the parameters of a builder constructor
 * to specify the names of the methods that can be used to retrieve the
 * parameter values after the object has been created.
 *
 * @see Buildable#populationStrategy()
 * @see BuilderConstructor
 *
 * @author Petter Holmström
 * @since 1.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.CLASS)
public @interface Getter {

    /**
     * The name of the method that will return the value of the annotated
     * constructor parameter once the object has been created.
     */
    String methodName();
}

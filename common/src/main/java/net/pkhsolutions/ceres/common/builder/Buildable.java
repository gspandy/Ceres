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

import java.beans.PropertyChangeEvent;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to instruct the annotation processor that a builder
 * should be generated for the annotated class. The idea is that you can use
 * complex, immutable domain objects in your program and automatically generate
 * mutable builders for them.
 *
 * @see Builder
 * @see <a href="http://en.wikipedia.org/wiki/Immutable_object">Immutable object
 * on Wikipedia</a>
 *
 * @author Petter Holmström
 * @since 1.0
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Buildable {

    /**
     * Enumeration of the data population strategies that a builder can use.
     *
     * @see Buildable#populationStrategy()
     *
     * @author Petter Holmström
     * @since 1.0
     */
    enum DataPopulationStrategy {

        /**
         * The builder uses getter methods and constructor parameters to read
         * and write data.
         */
        USE_CONSTRUCTOR_PARAMETERS,
        /**
         * The builder accesses the fields directly (using reflection) to read
         * and write data.
         */
        USE_FIELDS
    }

    /**
     * Specifies the data population strategy. The annotation processor can
     * either analyze the constructor parameters of the annotated class, or the
     * fields of the annotated class (including fields from super classes). <p>
     * If constructor parameters are used, the constructor with the {@link BuilderConstructor}
     * annotation will be analyzed. If no such annotation is present, the
     * annotation processor will throw an exception. For each parameter in the
     * constructor, the annotated class should also have a getter method that is
     * used during object deriving. By default, this method is assumed to be
     * named
     * <code>get[CapitalizedParameterName]</code>, or
     * <code>is[CapitalizedParameterName]</code> for boolean parameters. This
     * can be overridden by annotating the parameter with {@link Getter} and
     * explicitly providing the name of the getter method. For each parameter
     * name, a
     * <code>set[CapitalizedParameterName]</code> method will be generated in
     * the builder class. <p> If fields are used, the builder will access the
     * fields directly to read and write data. For each field, a
     * <code>set[CapitalizedFieldName]</code> will be generated in the builder
     * class. Fields that are annotated with {@link Ignore} will not be
     * included. The super classes of the annotated class will also be analyzed
     * up to and excluding {@link Object}. <b>Please note, however, that this
     * approach will not work if the fields are final</b> (which they preferably
     * should be to be truly immutable). This approach is mainly intended for
     * classes that expose an immutable API, but are in fact mutable under the
     * hood. This could be the case if you want your objects to be mappable
     * to/from XML using JAXB or to/from a relational database using JPA.
     *
     * @see Builder#deriveFrom(java.lang.Object)
     *
     */
    DataPopulationStrategy populationStrategy() default DataPopulationStrategy.USE_CONSTRUCTOR_PARAMETERS;

    /**
     * Specifies whether {@link PropertyChangeEvent}s should be fired every time
     * an attribute value changes. If this is set to true, the generated builder
     * will implement the {@link BindableBuilder} instead of the {@link Builder}
     * interface.
     */
    boolean bindable() default false;

    /**
     * Specifies whether the annotation processor should generate getter methods
     * for every attribute. Setter methods will always be generated. Getter
     * methods can come in handy if you use the builder object as a data source
     * with a form binding framework that binds UI controls to Java Bean
     * properties.
     */
    boolean generateGetters() default false;
}

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
package net.pkhsolutions.ceres.common.builder.tests;

import net.pkhsolutions.ceres.common.builder.Buildable;
import net.pkhsolutions.ceres.common.builder.BuilderConstructor;
import net.pkhsolutions.ceres.common.builder.Getter;
import net.pkhsolutions.ceres.common.builder.Required;

/**
 * Example class whose auto-generated builder fires property change events every
 * time a property is changed.
 *
 * @author Petter Holmström
 * @since 1.0
 */
@Buildable(bindable = true)
public class ExampleConstructorBindableBuildable extends ExampleConstructorBuildable {

    @BuilderConstructor
    public ExampleConstructorBindableBuildable(@Required String myStringProp, @Getter(methodName = "myIntProp") @Required int myIntProp, boolean myBoolProp) {
        super(myStringProp, myIntProp, myBoolProp);
    }
}

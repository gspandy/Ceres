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
import net.pkhsolutions.ceres.common.builder.Ignore;
import net.pkhsolutions.ceres.common.builder.Required;

/**
 * Example class whose auto-generated builder uses fields to initialize new
 * objects.
 *
 * @author Petter Holmström
 * @since 1.0
 */
@Buildable(populationStrategy = Buildable.DataPopulationStrategy.USE_FIELDS)
public class ExampleFieldsBuildable {

    @Required(allowNulls = true)
    private String myStringProp;
    private int myIntProp;
    private boolean myBoolProp;
    @Ignore
    private String myIgnoredProp;

    public ExampleFieldsBuildable() {
    }

    public ExampleFieldsBuildable(String myStringProp, int myIntProp, boolean myBoolProp, String myIgnoredProp) {
        this.myStringProp = myStringProp;
        this.myIntProp = myIntProp;
        this.myBoolProp = myBoolProp;
        this.myIgnoredProp = myIgnoredProp;
    }

    public int myIntProp() {
        return myIntProp;
    }

    public String getMyStringProp() {
        return myStringProp;
    }

    public boolean isMyBoolProp() {
        return myBoolProp;
    }

    public String getMyIgnoredProp() {
        return myIgnoredProp;
    }
}

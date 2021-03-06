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
package net.pkhsolutions.ceres.i18n.tests.pkga;

import net.pkhsolutions.ceres.i18n.annotations.Message;
import net.pkhsolutions.ceres.i18n.annotations.Messages;

/**
 * Example class using the {@link Messages} annotation.
 *
 * @author Petter Holmström
 * @since 1.0
 */
@Messages({
    @Message(key = "ExampleLocalizedClass2.key1", defaultValue = "Hello world ExampleLocalizedClass2.1"),
    @Message(key = "ExampleLocalizedClass2.key2", defaultValue = "Hello world ExampleLocalizedClass2.2")
})
public class ExampleLocalizedClass2 {

    @Messages({
        @Message(key = "ExampleLocalizedClass2.aField.key1", defaultValue = "Hello world ExampleLocalizedClass2.aField1"),
        @Message(key = "ExampleLocalizedClass2.aField.key2", defaultValue = "Hello world ExampleLocalizedClass2.aField2")
    })
    protected String aField;

    @Messages({
        @Message(key = "ExampleLocalizedClass2.aMethod.key1", defaultValue = "Hello world ExampleLocalizedClass2.aMethod1"),
        @Message(key = "ExampleLocalizedClass2.aMethod.key2", defaultValue = "Hello world ExampleLocalizedClass2.aMethod2")
    })
    public void aMethod() {
    }
}

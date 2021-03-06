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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Test cases for the auto-generated builders using the
 * <code>USE_CONSTRUCTOR_PARAMETERS</code> data population strategy.
 *
 * @author Petter Holmström
 * @since 1.0
 */
public class ExampleConstructorBuildableTest {

    @Test
    public void builder() {
        ExampleConstructorBuildableBuilder builder = ExampleConstructorBuildableBuilder.create();
        ExampleConstructorBuildable obj = builder.setMyStringProp("hello").setMyIntProp(123).setMyBoolProp(true).build();

        assertEquals("hello", obj.getMyStringProp());
        assertEquals(123, obj.myIntProp());
        assertTrue(obj.isMyBoolProp());
    }

    @Test(expected = IllegalStateException.class)
    public void builderWithoutRequiredProperty() {
        ExampleConstructorBuildableBuilder builder = ExampleConstructorBuildableBuilder.create();
        builder.build();
    }

    @Test
    public void derivingBuilderWithGetters() {
        ExampleConstructorBuildableWithGetters original = new ExampleConstructorBuildableWithGetters("hello", 123, true);
        ExampleConstructorBuildableWithGettersBuilder builder = ExampleConstructorBuildableWithGettersBuilder.create(original);

        assertEquals("hello", builder.getMyStringProp());
        assertEquals(123, builder.getMyIntProp());
        assertTrue(builder.isMyBoolProp());

        ExampleConstructorBuildableWithGetters obj = builder.setMyBoolProp(false).build();

        assertEquals("hello", obj.getMyStringProp());
        assertEquals(123, obj.myIntProp());
        assertFalse(obj.isMyBoolProp());
    }

    @Test
    public void bindableBuilder() {
        ExampleConstructorBindableBuildableBuilder builder = ExampleConstructorBindableBuildableBuilder.create();
        final int[] listenerFired = new int[1];
        builder.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                assertEquals("myStringProp", evt.getPropertyName());
                assertEquals("hello", evt.getNewValue());
                assertNull(evt.getOldValue());
                listenerFired[0]++;
            }
        });

        builder.setMyStringProp("hello");

        assertEquals(1, listenerFired[0]);
    }
}

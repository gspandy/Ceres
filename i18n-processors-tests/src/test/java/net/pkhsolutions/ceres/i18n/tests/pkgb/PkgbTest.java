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
package net.pkhsolutions.ceres.i18n.tests.pkgb;

import java.util.Arrays;
import java.util.Locale;
import net.pkhsolutions.ceres.common.holder.GlobalHolderStrategy;
import net.pkhsolutions.ceres.i18n.DefaultI18N;
import net.pkhsolutions.ceres.i18n.I18N;
import net.pkhsolutions.ceres.i18n.I18NHolder;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 * This test checks that the auto-generated artifacts in the {@code pkgb}
 * package are working as they should.
 *
 * @author Petter Holmström
 * @since 1.0
 */
public class PkgbTest {

    @Before
    public void setUp() {
        I18N i18n = new DefaultI18N(Arrays.asList(Locale.ENGLISH));
        I18NHolder.setStrategy(I18NHolder.class, new GlobalHolderStrategy<I18N>());
        I18NHolder.set(i18n);
    }

    @Test
    public void messages_Class3() {
        assertEquals("Hello world Joe Cool!", Bundle.ExampleLocalizedClass3_key("Joe Cool"));
    }
}

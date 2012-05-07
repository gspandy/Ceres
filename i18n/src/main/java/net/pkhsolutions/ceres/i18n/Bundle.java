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
package net.pkhsolutions.ceres.i18n;

import net.pkhsolutions.ceres.i18n.annotations.Message;

/**
 * Abstract base class for auto-generated Bundles. See {@link Message} for more
 * information.
 *
 * @author Petter Holmström
 * @since 1.0
 */
public abstract class Bundle {

    /**
     * Returns the current {@link I18N} instance.
     *
     * @see I18NHolder#get()
     * @throws IllegalStateException if no I18N has currently been set.
     */
    protected static I18N getI18N() {
        final I18N i18n = I18NHolder.get();
        if (i18n == null) {
            throw new IllegalStateException("No I18N currently available");
        }
        return i18n;
    }
}

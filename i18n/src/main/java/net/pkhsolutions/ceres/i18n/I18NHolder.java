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

import net.pkhsolutions.ceres.common.holder.Holder;

/**
 * Holder for the current {@link I18N}.
 *
 * @author Petter Holmström
 * @since 1.0
 */
public class I18NHolder extends Holder<I18N> {

    private I18NHolder() {
    }

    /**
     * Gets the current {@link I18N}.
     *
     * @return the I18N, or null if not set.
     */
    public static I18N get() {
        return getStrategy(I18NHolder.class).get();
    }

    /**
     * Sets the current {@link I18N}.
     *
     * @param i18n the I18N to set, may be null.
     */
    public static void set(I18N i18n) {
        getStrategy(I18NHolder.class).set(i18n);
    }
}

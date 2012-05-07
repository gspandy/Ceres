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

import java.util.Collection;
import java.util.Locale;
import net.pkhsolutions.ceres.eventbus.EventBus;
import net.pkhsolutions.ceres.eventbus.EventBusHolder;

/**
 * The I18N instance maintains information about the supported locales and the
 * currently selected locale. <p> Clients can use {@link I18NHolder} to access
 * the current I18N. Applications must remember to populate the holder
 * accordingly. For example, in a web application, the holder would use a
 * thread-local strategy and the I18N would be set in the beginning of each
 * request and cleared at the end. <p> When the locale changes, the I18N must
 * post a {@link LocaleChangedEvent} on the {@link EventBus} retrieved from {@link EventBusHolder}.
 * The scope of the event is implementation-specific.
 *
 * @author Petter Holmström
 * @since 1.0
 */
public interface I18N {

    /**
     * Gets the current locale.
     *
     * @return the current locale, never null.
     */
    Locale getCurrentLocale();

    /**
     * Changes the current locale.
     *
     * @param locale the locale to set, must not be null.
     * @throws IllegalArgumentException if {@code locale} is null or not
     * supported.
     */
    void setCurrentLocale(Locale locale);

    /**
     * Gets a collection of supported locales.
     *
     * @return a collection containing at least one locale, never null.
     */
    Collection<Locale> getSupportedLocales();
}

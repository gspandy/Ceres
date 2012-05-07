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

import java.util.Locale;

/**
 * Event fired by an {@link I18N} when the current locale changes.
 *
 * @author Petter Holmström
 * @since 1.0
 */
public class LocaleChangedEvent implements java.io.Serializable {

    private final I18N sender;
    private final Locale newLocale;
    private final Locale oldLocale;

    /**
     * Constructs a new
     * <code>LocaleChangedEvent</code>.
     *
     * @param sender the {@code I18N} that fired the event, must not be null.
     * @param oldLocale the old locale, must not be null.
     * @param newLocale the new locale, must not be null.
     */
    public LocaleChangedEvent(I18N sender, Locale oldLocale, Locale newLocale) {
        assert sender != null : "sender must not be null";
        assert oldLocale != null : "oldLocale must not be null";
        assert newLocale != null : "newLocale must not be null";

        this.sender = sender;
        this.oldLocale = oldLocale;
        this.newLocale = newLocale;
    }

    /**
     * Returns the new locale, never null.
     */
    public Locale getNewLocale() {
        return newLocale;
    }

    /**
     * Returns the old locale, never null.
     */
    public Locale getOldLocale() {
        return oldLocale;
    }

    /**
     * Returns the {@code I18N} that fired the event, never null.
     */
    public I18N getSender() {
        return sender;
    }
}

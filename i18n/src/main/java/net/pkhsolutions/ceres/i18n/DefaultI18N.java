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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;
import net.pkhsolutions.ceres.eventbus.EventBus;
import net.pkhsolutions.ceres.eventbus.EventBusHolder;
import net.pkhsolutions.ceres.eventbus.EventScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link I18N}. This class is not thread-safe.
 *
 * @author Petter Holmström
 * @since 1.0
 */
public class DefaultI18N implements I18N {

    private final Set<Locale> supportedLocales;
    private transient Logger logger = LoggerFactory.getLogger(getClass());
    private Locale currentLocale;

    /**
     * Constructs a new
     * <code>DefaultI18N</code> with the specified supported locales
     *
     * @param supportedLocales a collection containing at least one supported
     * locale, must not be null.
     * @throws IllegalArgumentException if {@code supportedLocales} is null or
     * empty.
     */
    public DefaultI18N(final Collection<Locale> supportedLocales) {
        assert supportedLocales != null : "supportedLocales must not be null";
        if (supportedLocales.isEmpty()) {
            throw new IllegalArgumentException("At least one locale must be specified");
        }
        this.supportedLocales = Collections.unmodifiableSet(new HashSet<Locale>(supportedLocales));
        currentLocale = this.supportedLocales.iterator().next();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    public Locale getCurrentLocale() {
        return currentLocale;
    }

    @Override
    public void setCurrentLocale(final Locale locale) {
        if (locale == null || !supportedLocales.contains(locale)) {
            throw new IllegalArgumentException("The specified locale is either null or not supported");
        }
        final Locale old = currentLocale;
        logger.debug("Changing locale from {} to {}", old, locale);
        currentLocale = locale;
        final EventBus eventBus = EventBusHolder.getEventBus();
        if (eventBus != null) {
            eventBus.publishEvent(new LocaleChangedEvent(this, old, locale), EventScope.LOCAL);
        } else {
            logger.warn("Could not publish LocaleChangeEvent because no EventBus was available");
        }
    }

    @Override
    public Collection<Locale> getSupportedLocales() {
        return supportedLocales;
    }
}

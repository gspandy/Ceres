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
package net.pkhsolutions.fenix.common.application;

import com.vaadin.Application;

/**
 * Utility class for binding a Vaadin application instance to the current thread
 * using an {@link InheritableThreadLocal}. Vaadin applications must remember to
 * set and reset this value when a request starts and ends, respectively.
 *
 * @see ApplicationHolderRequestListenerDelegate
 * @see https://vaadin.com/wiki/-/wiki/Main/ThreadLocal%20Pattern
 * @author Petter Holmström
 * @since 1.0
 */
public class ApplicationHolder {

    private static final InheritableThreadLocal<Application> application = new InheritableThreadLocal<Application>();

    private ApplicationHolder() {
    }

    /**
     * Gets the Vaadin application instance bound to the current thread.
     *
     * @return the application instance, or null if no application has been
     * bound.
     */
    public static Application getApplication() {
        return application.get();
    }

    /**
     * Bounds the specified Vaadin application instance to the current thread.
     *
     * @param app the application instance, or null to unbound any previously
     * bound application instance.
     */
    public static void setApplication(Application app) {
        if (app == null) {
            application.remove();
        } else {
            application.set(app);
        }
    }
}

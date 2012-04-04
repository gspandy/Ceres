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
import com.vaadin.terminal.gwt.server.HttpServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Implementation of {@link HttpServletRequestListener} designed to act as a
 * delegate owned by a Vaadin application. The {@link Application} must
 * implement
 * {@link HttpServletRequestListener} and then delegate the method calls to this
 * class. The class will set the {@link ApplicationHolder} upon request start
 * and reset it upon request end.
 *
 * @author Petter Holmström
 * @since 1.0
 */
public class ApplicationHolderRequestListenerDelegate<A extends Application & HttpServletRequestListener> implements HttpServletRequestListener {

    private static final long serialVersionUID = 3618581382439656991L;
    private final A application;

    /**
     * Creates a new
     * <code>ApplicationHolderRequestDelegate</code>.
     *
     * @param application the owning application instance, must not be null.
     */
    public ApplicationHolderRequestListenerDelegate(A application) {
        assert application != null : "application must not be null";
        this.application = application;
    }

    @Override
    public void onRequestStart(HttpServletRequest request, HttpServletResponse response) {
        ApplicationHolder.setApplication(application);
    }

    @Override
    public void onRequestEnd(HttpServletRequest request, HttpServletResponse response) {
        ApplicationHolder.setApplication(null);
    }
}

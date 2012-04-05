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
package net.pkhsolutions.ceres.common.cdi;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * This is an extended version of {@link AbstractApplicationServlet} that has to
 * be used in order for the Vaadin CDI scope to work. <p> You use this servlet
 * in your applications like this:
 * <pre>
 * &#64;WebServlet(urlPatterns = "/*")
 * public class MVPDemoAppServlet extends CDIAwareAbstractApplicationServlet {
 *
 *     &#64;Inject
 *     Instance&lt;MyApp&gt; application;
 *
 *     protected Application getNewApplication(HttpServletRequest request) throws ServletException {
 *         return application.get();
 *     }
 *
 *     protected Class&lt;? extends Application&gt; getApplicationClass() throws ClassNotFoundException {
 *         return MyApp.class;
 *     }
 * }
 * </pre>
 *
 * @author Petter Holmström
 * @since 1.0
 */
public abstract class CDIAwareAbstractApplicationServlet extends AbstractApplicationServlet {

    // TODO Add logging to make debugging easier
    private static final long serialVersionUID = 4156259397393758591L;

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final URL applicationUrl = getApplicationUrl(request);
        final Map<Object, Object> applicationBeans = getApplicationBeans(applicationUrl, request);

        VaadinContext.setCurrentApplicationUrl(applicationUrl);
        VaadinContext.setCurrentApplicationBeans(applicationBeans);
        super.service(request, response);
        VaadinContext.setCurrentApplicationUrl(null);
        VaadinContext.setCurrentApplicationBeans(null);
    }

    private Map<Object, Object> getApplicationBeans(URL applicationUrl, HttpServletRequest request) {
        Map<Object, Object> beans = getApplicationContext(request.getSession()).getApplicationBeansByUrl(applicationUrl);
        if (beans == null) {
            beans = new HashMap<Object, Object>();
        }
        return beans;
    }

    @Override
    protected CDIAwareApplicationContext getApplicationContext(HttpSession session) {
        return CDIAwareApplicationContext.getApplicationContext(session);
    }

    /**
     * CDI-aware extension of {@link WebApplicationContext} used by {@link CDIAwareAbstractApplicationServlet}.
     * In addition to doing everything that the super class does, this class
     * also maintains the bean maps that are passed to {@link VaadinContext} in
     * the beginning of every servlet request.
     *
     * @author Petter Holmström
     * @since 1.0
     */
    protected static class CDIAwareApplicationContext extends WebApplicationContext {

        private static final long serialVersionUID = -3447283939845928880L;
        private Map<URL, Map<Object, Object>> urlToBeans = new HashMap<URL, Map<Object, Object>>();

        protected Map<Object, Object> getApplicationBeansByUrl(URL url) {
            return urlToBeans.get(url);
        }

        @Override
        protected void addApplication(Application application) {
            super.addApplication(application);
            final Map<Object, Object> applicationBeans = VaadinContext.getCurrentApplicationBeans();
            final URL applicationUrl = VaadinContext.getCurrentApplicationUrl();

            assert applicationBeans != null : "applicationBeans should never be null at this point";

            urlToBeans.put(applicationUrl, applicationBeans);
        }

        @Override
        protected void removeApplication(Application application) {
            super.removeApplication(application);
            urlToBeans.remove(application.getURL());
            VaadinContext.setCurrentApplicationBeans(new HashMap<Object, Object>());
        }

        /**
         *
         * @param session
         * @return
         */
        public static CDIAwareApplicationContext getApplicationContext(
                HttpSession session) {
            CDIAwareApplicationContext cx = (CDIAwareApplicationContext) session.getAttribute(WebApplicationContext.class.getName());
            if (cx == null) {
                cx = new CDIAwareApplicationContext();
                session.setAttribute(WebApplicationContext.class.getName(), cx);
            }
            if (cx.session == null) {
                cx.session = session;
            }
            return cx;
        }
    }
}

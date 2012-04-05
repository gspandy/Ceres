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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author petter
 */
public abstract class CDIAwareAbstractApplicationServlet extends AbstractApplicationServlet {

    private static final long serialVersionUID = 4156259397393758591L;

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String applicationUuid = getApplicationUuid(request);
        final Map<Object, Object> applicationBeans = getApplicationBeans(applicationUuid, request);

        System.out.println("application uuid = " + applicationUuid + ", application beans = " + applicationBeans);

        VaadinContext.setCurrentApplicationUuid(applicationUuid);
        VaadinContext.setCurrentApplicationBeans(applicationBeans);
        super.service(request, response);
        VaadinContext.setCurrentApplicationUuid(null);
        VaadinContext.setCurrentApplicationBeans(null);
    }

    private String getApplicationUuid(HttpServletRequest request) throws MalformedURLException {
        URL applicationURL = getApplicationUrl(request);
        String applicationUuid = getApplicationContext(request.getSession()).getApplicationUuid(applicationURL);
        if (applicationUuid == null) {
            applicationUuid = UUID.randomUUID().toString();
        }
        return applicationUuid;
    }

    private Map<Object, Object> getApplicationBeans(String applicationUuid, HttpServletRequest request) {
        Map<Object, Object> beans = getApplicationContext(request.getSession()).getApplicationBeansByUuid(applicationUuid);
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
     *
     */
    protected static class CDIAwareApplicationContext extends WebApplicationContext {

        private static final long serialVersionUID = -3447283939845928880L;
        //private Map<URL, String> applicationUrlToUuid = new HashMap<URL, String>();
        private Map<String, Map<Object, Object>> uuidToBeans = new HashMap<String, Map<Object, Object>>();

        /**
         *
         * @param application
         * @return
         */
        public String getApplicationUuid(URL applicationURL) {
            return applicationUrlToUuid.get(applicationURL);
        }

        public Map<Object, Object> getApplicationBeansByUrl(URL url) {
            return uuidToBeans.get(uuid);
        }

        @Override
        protected void addApplication(Application application) {
            super.addApplication(application);
            final String applicationUuid = VaadinContext.getCurrentApplicationUuid();
            final Map<Object, Object> applicationBeans = VaadinContext.getCurrentApplicationBeans();
            final URL applicationUrl = VaadinContext.getCurrentApplicationUrl();

            System.out.println("adding application " + application + " with URL " + applicationUrl + " using uuid " + applicationUuid + " and beans " + applicationBeans);

            assert applicationUuid != null : "applicationUuid should never be null at this point";
            assert applicationBeans != null : "applicationBeans should never be null at this point";

            applicationUrlToUuid.put(applicationUrl, applicationUuid);
            uuidToBeans.put(applicationUuid, applicationBeans);
        }

        @Override
        protected void removeApplication(Application application) {
            super.removeApplication(application);
            String applicationUuid = applicationUrlToUuid.remove(application.getURL());
            if (applicationUuid != null) {
                uuidToBeans.remove(applicationUuid);
            }
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

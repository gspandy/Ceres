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

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Map;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

/**
 * {@link Context} implementation for the Vaadin scope. The bean map for the
 * current Vaadin application is stored in a thread local variable that is
 * populated by {@link CDIAwareAbstractApplicationServlet}.
 *
 * @author Petter Holmström
 * @since 1.0
 */
public class VaadinContext implements Context {

    private static final InheritableThreadLocal<Map<Object, Object>> applicationBeans = new InheritableThreadLocal<Map<Object, Object>>();
    private static final InheritableThreadLocal<URL> applicationUrl = new InheritableThreadLocal<URL>();

    static void setCurrentApplicationBeans(Map<Object, Object> beans) {
        if (beans == null) {
            applicationBeans.remove();
        } else {
            applicationBeans.set(beans);
        }
    }

    static Map<Object, Object> getCurrentApplicationBeans() {
        return applicationBeans.get();
    }

    static URL getCurrentApplicationUrl() {
        return applicationUrl.get();
    }

    static void setCurrentApplicationUrl(URL url) {
        if (url == null) {
            applicationUrl.remove();
        } else {
            applicationUrl.set(url);
        }
    }

    @Override
    public Class<? extends Annotation> getScope() {
        return VaadinScoped.class;
    }

    @Override
    public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {
        T bean = get(contextual);
        if (bean == null) {
            bean = contextual.create(creationalContext);
            getBeans().put(getBeanName(contextual), bean);
        }
        return bean;
    }

    @Override
    public <T> T get(Contextual<T> contextual) {
        return (T) getBeans().get(getBeanName(contextual));
    }

    /**
     * Gets the current bean map.
     *
     * @return the bean map, never null.
     * @throws IllegalStateException if no bean map has been bound to the
     * current thread.
     */
    private Map<Object, Object> getBeans() {
        final Map<Object, Object> beans = getCurrentApplicationBeans();
        if (beans == null) {
            throw new IllegalStateException("No Vaadin application bound to current thread");
        }
        return beans;
    }

    /**
     * Constructs a bean name of the specified contextual object (has to be an
     * instance of {@link Bean}). This name will then be used to look up the
     * correct bean from the bean map.
     *
     * @param contextual the contextual object, must not be null.
     * @return the bean name.
     */
    private String getBeanName(Contextual<?> contextual) {
        assert contextual != null : "contextual must not be null";
        if (contextual instanceof Bean) {
            Bean<?> bean = (Bean) contextual;
            StringBuilder sb = new StringBuilder();
            sb.append(bean.getBeanClass().getName());
            sb.append(":");
            sb.append(bean.getName());
            sb.append(":");
            for (Class<?> stereotype : bean.getStereotypes()) {
                sb.append(stereotype.getName());
                sb.append(":");
            }
            for (Annotation qualifier : bean.getQualifiers()) {
                sb.append(qualifier.annotationType().getName());
                sb.append(":");
            }
            return sb.toString();
        } else {
            throw new UnsupportedOperationException("Only instances of Bean can be stored in a VaadinContext");
        }
    }

    @Override
    public boolean isActive() {
        return getCurrentApplicationUrl() != null;
    }
}

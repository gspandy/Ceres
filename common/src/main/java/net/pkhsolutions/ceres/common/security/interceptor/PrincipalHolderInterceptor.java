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
package net.pkhsolutions.ceres.common.security.interceptor;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptors;
import javax.interceptor.InvocationContext;
import net.pkhsolutions.ceres.common.security.PrincipalHolder;

/**
 * This is an EJB interceptor that will set the {@link PrincipalHolder} to the
 * caller principal of the current {@link EJBContext} before proceeding and
 * clear it before returning. You use it in the following way:
 * <pre>
 * &#64;Stateless
 * &#64;Interceptors({PrincipalHolderInterceptor.class})
 * public class MyEJB {
 *   ...
 * }
 * </pre>
 *
 * @see EJBContext#getCallerPrincipal()
 * @see Interceptors
 *
 * @author Petter Holmström
 * @since 1.0
 */
public class PrincipalHolderInterceptor {

    @Resource
    EJBContext context;

    @AroundInvoke
    public Object populatePrincipalHolder(InvocationContext ctx) throws Exception {
        PrincipalHolder.set(context.getCallerPrincipal());
        try {
            return ctx.proceed();
        } finally {
            PrincipalHolder.set(null);
        }
    }
}

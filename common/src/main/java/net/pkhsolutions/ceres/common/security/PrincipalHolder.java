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
package net.pkhsolutions.ceres.common.security;

import java.security.Principal;
import net.pkhsolutions.ceres.common.holder.Holder;

/**
 * Holder for the current principal.
 *
 * @author Petter Holmström
 * @since 1.0
 */
public class PrincipalHolder extends Holder<Principal> {

    private PrincipalHolder() {
    }

    /**
     * Gets the current principal.
     *
     * @return the principal, or null if not set.
     */
    public static Principal get() {
        return getStrategy(PrincipalHolder.class).get();
    }

    /**
     * Gets the name of the current principal.
     *
     * @see Principal#getName()
     * @return the principal name, or null if not set.
     */
    public static String getName() {
        Principal principal = get();
        return principal == null ? null : principal.getName();
    }

    /**
     * Sets the current principal.
     *
     * @param principal the principal to set, may be null.
     */
    public static void set(Principal principal) {
        getStrategy(PrincipalHolder.class).set(principal);
    }
}

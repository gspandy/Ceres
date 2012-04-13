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
package net.pkhsolutions.ceres.common.holder;

import java.util.HashMap;
import java.util.Map;
import net.pkhsolutions.ceres.common.annotations.NeverReturnsNull;

/**
 * A holder is a class that makes a certain object available using a static
 * getter (and setter) method. The strategy pattern is used to encapsulate the
 * way the object is actually stored. <p> You would implement a holder in this
 * way:
 * <pre>
 * public class CurrentUserNameHolder extends Holder&lt;String&gt; {
 *
 *   private CurrentUserNameHolder() {
 *   }
 *
 *   public static String get() {
 *       return getStrategy(CurrentUserNameHolder.class).get();
 *   }
 *
 *   public static void set(String username) {
 *       getStrategy(CurrentUserNameHolder.class).set(username);
 *   }
 * }
 * </pre> Now a method that needs the name of the current user can call
 * <code>CurrentUserNameHolder.get()</code> to retrieve it.
 *
 * @see HolderStrategy
 * @see GlobalHolderStrategy
 * @see ThreadLocalHolderStrategy
 *
 * @author Petter Holmström
 * @since 1.0
 */
public abstract class Holder<T> {

    private static final Map<Class<? extends Holder<?>>, HolderStrategy<?>> strategies = new HashMap<Class<? extends Holder<?>>, HolderStrategy<?>>();

    /**
     * Sets the strategy for the specified holder class.
     *
     * @param <T> the type of object stored in the holder.
     * @param holderClass the holder class, must not be null.
     * @param strategy the strategy to set, may be null.
     */
    public synchronized static <T> void setStrategy(Class<? extends Holder<T>> holderClass, HolderStrategy<T> strategy) {
        assert holderClass != null : "holderClass must not be null";
        if (strategy == null) {
            strategies.remove(holderClass);
        } else {
            strategies.put(holderClass, strategy);
        }
    }

    /**
     * Gets the strategy for the specified holder class. If no strategy has been
     * set, a {@link ThreadLocalHolderStrategy} is created and returned.
     *
     * @param <T> the type of object stored in the holder.
     * @param holderClass the holder class, must not be null.
     * @return the holder strategy, never null.
     */
    @NeverReturnsNull
    public synchronized static <T> HolderStrategy<T> getStrategy(Class<? extends Holder<T>> holderClass) {
        assert holderClass != null : "holderClass must not be null";
        HolderStrategy<T> strategy = (HolderStrategy<T>) strategies.get(holderClass);
        if (strategy == null) {
            strategy = new ThreadLocalHolderStrategy<T>();
            strategies.put(holderClass, strategy);
        }
        return strategy;
    }
}

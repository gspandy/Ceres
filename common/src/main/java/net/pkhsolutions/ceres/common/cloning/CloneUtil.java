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
package net.pkhsolutions.ceres.common.cloning;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * This is a utility class for object cloning.
 *
 * @author Petter Holmström
 * @since 1.0
 */
public final class CloneUtil {

    private CloneUtil() {
    }

    /**
     * The default clone method always creates a shallow clone, meaning that all
     * the field values are copied directly from the original to the clone. If
     * the fields are immutable this is not a problem, but mutable fields
     * require some additional handling, especially if they are considered to be
     * a part of the object that is being cloned. <p> This method goes through
     * the fields of the
     * <code>shallowClone</code> object (both fields declared by the object's
     * class and fields declared by superclasses) and clones all fields that are
     * annotated with
     * {@link CloneThis}. The
     * <code>shallowClone</code> object should be retrieved from
     * <code>Object.clone()</code> like this:
     *
     * <pre>
     * public Object clone() throws CloneNotSupportedException {
     *     return CloneUtil.deepClone((MyClass) super.clone());
     * }
     * </pre>
     *
     * @param <T> the type of the shallow clone
     * @param shallowClone the object whose annotated fields are to be cloned.
     * @return the
     * <code>shallowClone</code> instance.
     * @throws CloneNotSupportedException if the deep clone could not be
     * completed.
     */
    public static <T extends Cloneable> T deepClone(T shallowClone)
            throws CloneNotSupportedException {
        assert shallowClone != null : "shallowClone must not be null";

        Class<?> currentClass = shallowClone.getClass();
        while (currentClass != Object.class) {
            cloneDeclaredFields(currentClass, shallowClone);
            currentClass = currentClass.getSuperclass();
        }

        return shallowClone;
    }

    private static void cloneDeclaredFields(Class<?> declaringClass,
            Object owner) throws CloneNotSupportedException {
        for (Field field : declaringClass.getDeclaredFields()) {
            CloneThis cloneAnnotation = field.getAnnotation(CloneThis.class);
            if (cloneAnnotation != null) {
                cloneField(field, owner, cloneAnnotation.deepClone());
            }
        }
    }

    private static void cloneField(Field field, Object owner, boolean deepClone)
            throws CloneNotSupportedException {
        boolean oldAccessible = field.isAccessible();
        field.setAccessible(true);
        try {
            if (field.getType().isArray()) {
                cloneArrayField(field, owner, deepClone);
            } else {
                cloneOrdinaryField(field, owner, deepClone);
            }
        } catch (IllegalAccessException e) {
            throw new CloneNotSupportedException("Could not access field "
                    + field + " on object " + owner);
        } finally {
            field.setAccessible(oldAccessible);
        }
    }

    @SuppressWarnings("unchecked")
    private static void cloneOrdinaryField(Field field, Object owner,
            boolean deepClone) throws IllegalAccessException,
            CloneNotSupportedException {
        Object oldValue = field.get(owner);
        Object newValue = callCloneMethod(oldValue);
        if (newValue != null && deepClone) {
            if (newValue instanceof Collection) {
                cloneCollection((Collection<Object>) oldValue,
                        (Collection<Object>) newValue);
            } else if (newValue instanceof Map) {
                cloneMap((Map<Object, Object>) oldValue,
                        (Map<Object, Object>) newValue);
            }
        }
        field.set(owner, newValue);
    }

    private static void cloneArrayField(Field field, Object owner,
            boolean deepClone) throws IllegalAccessException,
            CloneNotSupportedException {
        Object[] fieldValue = (Object[]) field.get(owner);
        if (fieldValue != null) {
            Object[] newArray = Arrays.copyOf(fieldValue, fieldValue.length);
            if (deepClone) {
                for (int i = 0; i < fieldValue.length; i++) {
                    if (fieldValue[i] instanceof Cloneable) {
                        newArray[i] = callCloneMethod(fieldValue[i]);
                    }
                }
            }
            field.set(owner, newArray);
        }
    }

    private static void cloneCollection(Collection<Object> source,
            Collection<Object> destination) throws CloneNotSupportedException {
        destination.clear();
        for (Object sourceItem : source) {
            destination.add(callCloneMethod(sourceItem));
        }
    }

    private static void cloneMap(Map<Object, Object> source,
            Map<Object, Object> destination) throws CloneNotSupportedException {
        destination.clear();
        for (Map.Entry<Object, Object> sourceEntry : source.entrySet()) {
            destination.put(sourceEntry.getKey(),
                    callCloneMethod(sourceEntry.getValue()));
        }
    }

    private static Object callCloneMethod(Object object)
            throws CloneNotSupportedException {
        if (object == null) {
            return null;
        }
        try {
            Method cloneMethod = object.getClass().getMethod("clone");
            return cloneMethod.invoke(object);
        } catch (Exception e) {
            throw new CloneNotSupportedException(
                    "Could not invoke clone method on object " + object);
        }
    }
}

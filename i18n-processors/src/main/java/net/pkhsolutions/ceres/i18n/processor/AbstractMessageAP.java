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
package net.pkhsolutions.ceres.i18n.processor;

import java.io.IOException;
import java.util.*;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import net.pkhsolutions.ceres.i18n.annotations.Message;
import net.pkhsolutions.ceres.i18n.annotations.Messages;

/**
 * Base class for all I18N annotation processors. Clients should never use this
 * class directly.∫
 *
 * @author Petter Holmström
 * @since 1.0
 */
public abstract class AbstractMessageAP extends AbstractProcessor {

    private static final HashMap<String, FileObject> fileObjects = new HashMap<String, FileObject>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        final HashMap<PackageElement, List<Message>> messages = new HashMap<PackageElement, List<Message>>();
        for (final Element element : roundEnv.getElementsAnnotatedWith(Message.class)) {
            addMessageToMap(element.getAnnotation(Message.class), getPackage(element), messages);
        }
        for (final Element element : roundEnv.getElementsAnnotatedWith(Messages.class)) {
            final PackageElement pkg = getPackage(element);
            final Messages messagesAnnotation = element.getAnnotation(Messages.class);
            if (messagesAnnotation != null) {
                for (Message message : messagesAnnotation.value()) {
                    addMessageToMap(message, pkg, messages);
                }
            }
        }
        process(messages);
        return true;
    }

    private static void addMessageToMap(final Message message, final PackageElement pkg, final Map<PackageElement, List<Message>> messages) {
        List<Message> list = messages.get(pkg);
        if (list == null) {
            list = new LinkedList<Message>();
            messages.put(pkg, list);
        }
        list.add(message);
    }

    /**
     * Processes the specified {@link Message}s, mapped by package.
     */
    protected abstract void process(final Map<PackageElement, List<Message>> messages);

    /**
     * Returns the package that contains the specified element.
     *
     * @param element the element whose package to fetch.
     * @return the package, or null if the element is not in a package.
     */
    protected static PackageElement getPackage(final Element element) {
        if (element == null) {
            return null;
        } else if (element.getEnclosingElement().getKind() == ElementKind.PACKAGE) {
            return (PackageElement) element.getEnclosingElement();
        } else {
            return getPackage(element.getEnclosingElement());
        }
    }

    /**
     * Returns the {@code messages.properties} file for the specified package,
     * opened for reading.
     */
    protected FileObject getBundleForReading(final PackageElement pkg) throws IOException {
        final Filer filer = processingEnv.getFiler();
        final String packageName = pkg.getQualifiedName().toString();
        final String fileName = "messages.properties";
        final String mapKey = packageName + "." + fileName + ":read";
        synchronized (AbstractMessageAP.class) {
            FileObject fo = fileObjects.get(mapKey);
            if (fo == null) {
                fo = filer.getResource(StandardLocation.SOURCE_OUTPUT, packageName, fileName);
                fileObjects.put(mapKey, fo);
            }
            return fo;
        }
    }

    /**
     * Returns the {@code messages.properties} file for the specified package,
     * opened for writing.
     */
    protected FileObject getBundleForWriting(final PackageElement pkg) throws IOException {
        final Filer filer = processingEnv.getFiler();
        final String packageName = pkg.getQualifiedName().toString();
        final String fileName = "messages.properties";
        final String mapKey = packageName + "." + fileName + ":write";
        synchronized (AbstractMessageAP.class) {
            FileObject fo = fileObjects.get(mapKey);
            if (fo == null) {
                fo = filer.createResource(StandardLocation.SOURCE_OUTPUT, packageName, fileName);
                fileObjects.put(mapKey, fo);
            }
            return fo;
        }
    }
}

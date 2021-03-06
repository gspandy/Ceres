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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import net.pkhsolutions.ceres.i18n.annotations.Message;
import net.pkhsolutions.ceres.i18n.annotations.Messages;

/**
 * This is an annotation processor that goes through {@link Message} and {@link Messages}
 * annotations and compiles {@code messages.properties}-files for each package.
 * Clients should never use this class directly.
 *
 * @author Petter Holmström
 * @since 1.0
 */
@SupportedAnnotationTypes({"net.pkhsolutions.ceres.i18n.annotations.Message", "net.pkhsolutions.ceres.i18n.annotations.Messages"})
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public final class MessageBundleAP extends AbstractMessageAP {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        super.process(annotations, roundEnv);
        return false;
    }

    @Override
    protected void process(final Map<PackageElement, List<Message>> messages) {
        for (final PackageElement pkg : messages.keySet()) {
            try {
                final Properties props = new Properties();
                final FileObject originalBundle = getBundleForReading(pkg);
                if (originalBundle.getLastModified() > 0) {
                    final InputStream in = originalBundle.openInputStream();
                    props.load(in);
                    in.close();
                }
                // TODO Line breaks?
                for (final Message message : messages.get(pkg)) {
                    props.put(message.key(), message.defaultValue());
                }
                final OutputStream out = getBundleForWriting(pkg).openOutputStream();
                props.store(out, "Auto-generated by net.pkhsolutions.ceres.i18n.processor.MessageBundleAP");
                out.close();
            } catch (final IOException e) {
                throw new RuntimeException("Could not write to bundle file", e);
            }
        }
    }
}

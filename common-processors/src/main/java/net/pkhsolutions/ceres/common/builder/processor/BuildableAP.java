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
package net.pkhsolutions.ceres.common.builder.processor;

import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import net.pkhsolutions.ceres.common.builder.Buildable;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 *
 * @author petter
 */
@SupportedAnnotationTypes("net.pkhsolutions.ceres.common.builder.Buildable")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class BuildableAP extends AbstractProcessor {

    private VelocityEngine velocityEngine;

    private Template constructorBuilderTemplate;

    public BuildableAP() {
        super();
        
        URL url = this.getClass().getClassLoader().getResource("net/pkhsolutions/ceres/common/builder/processor/velocity.properties");
        Properties props = new Properties();
        try {
            props.load(url.openStream());
        } catch (IOException e) {
            throw new RuntimeException("Could not load velocity properties", e);
        }
        velocityEngine = new VelocityEngine(props);
        velocityEngine.init();

        constructorBuilderTemplate = velocityEngine.getTemplate("net/pkhsolutions/ceres/common/builder/processor/constructor_builder.vm");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Buildable.class)) {
            final TypeElement classElement = (TypeElement) element;
            try {
                JavaFileObject jfo = processingEnv.getFiler().createSourceFile(classElement.getQualifiedName() + "Builder");

                VelocityContext vc = new VelocityContext();
                vc.put("className", classElement.getSimpleName().toString());
                vc.put("generationDate", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(new Date()));
                vc.put("packageName", ((PackageElement) classElement.getEnclosingElement()).getQualifiedName().toString());

                Writer writer = jfo.openWriter();
                constructorBuilderTemplate.merge(vc, writer);
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(BuildableAP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return true;
    }

    /*
     * Properties props = new Properties();
                URL url = this.getClass().getClassLoader().getResource("velocity.properties");
                props.load(url.openStream());

                VelocityEngine ve = new VelocityEngine(props);
                ve.init();

                VelocityContext vc = new VelocityContext();

                vc.put("className", className);
                vc.put("packageName", packageName);
                vc.put("fields", fields);
                vc.put("methods", methods);

                Template vt = ve.getTemplate("beaninfo.vm");
     */


}

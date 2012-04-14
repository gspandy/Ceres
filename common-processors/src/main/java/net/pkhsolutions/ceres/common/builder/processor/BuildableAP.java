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
import java.util.*;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.tools.JavaFileObject;
import net.pkhsolutions.ceres.common.builder.Buildable;
import net.pkhsolutions.ceres.common.builder.Buildable.DataPopulationStrategy;
import net.pkhsolutions.ceres.common.builder.BuilderConstructor;
import net.pkhsolutions.ceres.common.builder.Getter;
import net.pkhsolutions.ceres.common.builder.Required;
import org.apache.commons.lang.StringUtils;
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
            processType((TypeElement) element);
        }
        return true;
    }

    private void processType(TypeElement type) {
        final DataPopulationStrategy strategy = type.getAnnotation(Buildable.class).value();
        if (strategy.equals(DataPopulationStrategy.USE_CONSTRUCTOR_PARAMETERS)) {
            createConstructorBuilder(type);
        } else {
            createFieldBuilder(type);
        }
    }

    private void createConstructorBuilder(TypeElement type) {
        ExecutableElement constructor = getBuilderConstructor(type);
        VelocityContext vc = createAndInitializeVelocityContext(type);
        vc.put("properties", createPropertyList(constructor.getParameters()));
        createSourceFile(type.getQualifiedName() + "Builder", constructorBuilderTemplate, vc);
    }

    private ExecutableElement getBuilderConstructor(TypeElement type) {
        for (Element e : type.getEnclosedElements()) {
            if (e.getKind().equals(ElementKind.CONSTRUCTOR) && e.getAnnotation(BuilderConstructor.class) != null) {
                return (ExecutableElement) e;
            }
        }
        throw new RuntimeException("No constructor annotated with @BuilderConstructor found");
    }

    private void createFieldBuilder(TypeElement type) {
        // TODO Implement me!
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private VelocityContext createAndInitializeVelocityContext(TypeElement type) {
        final VelocityContext vc = new VelocityContext();
        vc.put("className", type.getSimpleName().toString());
        vc.put("generationDate", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(new Date()));
        vc.put("packageName", getPackage(type).getQualifiedName().toString());
        return vc;
    }

    private PackageElement getPackage(TypeElement type) {
        if (type.getEnclosingElement() instanceof PackageElement) {
            return (PackageElement) type.getEnclosingElement();
        } else {
            throw new RuntimeException("The @Buildable annotation can only be used on top-level types");
        }
    }

    private void createSourceFile(String fileName, Template template, VelocityContext vc) {
        try {
            JavaFileObject jfo = processingEnv.getFiler().createSourceFile(fileName);
            Writer writer = jfo.openWriter();
            template.merge(vc, writer);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Could not create source file", e);
        }
    }

    private List<Property> createPropertyList(Collection<? extends VariableElement> elements) {
        ArrayList<Property> list = new ArrayList<Property>(elements.size());
        for (VariableElement element : elements) {
            final String name = element.getSimpleName().toString();
            final boolean required = element.getAnnotation(Required.class) != null;
            final String typeName = element.asType().toString();
            final boolean nullable = element.asType().getKind() == TypeKind.DECLARED;
            final Getter getter = element.getAnnotation(Getter.class);
            if (getter == null) {
                list.add(new Property(name, typeName, required, nullable));
            } else {
                list.add(new Property(name, typeName, required, nullable, getter.methodName()));
            }
        }
        return list;
    }

    /**
     *
     */
    public static class Property {
        private final String name;
        private final String typeName;
        private final boolean required;
        private final boolean nullable;
        private final String getterName;

        Property(String name, String typeName, boolean required,  boolean nullable, String getterName) {
            this.name = name;
            this.typeName = typeName;
            this.required = required;
            this.nullable = nullable;
            this.getterName = getterName;
        }

        Property(String name, String typeName, boolean required, boolean nullable) {
            this(name, typeName, required, nullable, null);
        }

        /**
         *
         * @return
         */
        public String getName() {
            return name;
        }

        /**
         *
         * @return
         */
        public String getCapitalizedName() {
            return StringUtils.capitalize(name);
        }

        /**
         *
         * @return
         */
        public String getTypeName() {
            return typeName;
        }

        /**
         *
         * @return
         */
        public boolean isRequired() {
            return required;
        }

        /**
         *
         */
        public boolean isNullable() {
            return nullable;
        }

        /**
         *
         * @return
         */
        public String getGetterName() {
            if (getterName == null) {
                if (typeName.equals("boolean")) {
                    return "is" + getCapitalizedName();
                } else {
                    return "get" + getCapitalizedName();
                }
            }
            return getterName;
        }
    }

}

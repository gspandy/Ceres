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
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;
import net.pkhsolutions.ceres.common.builder.Buildable.DataPopulationStrategy;
import net.pkhsolutions.ceres.common.builder.*;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 * This is an annotation processor that generates builders for classes annotated
 * with
 * {@link Buildable}. Clients should never use this class directly.
 *
 * @author Petter Holmström
 * @since 1.0
 */
@SupportedAnnotationTypes("net.pkhsolutions.ceres.common.builder.Buildable")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public final class BuildableAP extends AbstractProcessor {

    // TODO Some of the methods in this class could use some refactoring.
    private VelocityEngine velocityEngine;
    private Template constructorBuilderTemplate;
    private Template fieldBuilderTemplate;

    /**
     * Constructs a new
     * <code>BuildableAP</code>. Clients should never need to create instances
     * of this class.
     */
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
        fieldBuilderTemplate = velocityEngine.getTemplate("net/pkhsolutions/ceres/common/builder/processor/field_builder.vm");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Buildable.class)) {
            processType((TypeElement) element);
        }
        return true;
    }

    private void processType(TypeElement type) {
        final DataPopulationStrategy strategy = type.getAnnotation(Buildable.class).populationStrategy();
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
        VelocityContext vc = createAndInitializeVelocityContext(type);
        vc.put("properties", createPropertyList(getFields(type)));
        createSourceFile(type.getQualifiedName() + "Builder", fieldBuilderTemplate, vc);
    }

    private Collection<VariableElement> getFields(TypeElement type) {
        final Set<Element> elements = new HashSet<Element>();
        elements.addAll(type.getEnclosedElements());
        TypeMirror supertype = type.getSuperclass();
        while (supertype.getKind() != TypeKind.NONE) {
            final TypeElement element = (TypeElement) processingEnv.getTypeUtils().asElement(supertype);
            elements.addAll(element.getEnclosedElements());
            supertype = element.getSuperclass();
        }
        return ElementFilter.fieldsIn(elements);
    }

    private VelocityContext createAndInitializeVelocityContext(TypeElement type) {
        final VelocityContext vc = new VelocityContext();
        vc.put("className", type.getSimpleName().toString());
        vc.put("generationDate", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(new Date()));
        vc.put("packageName", getPackage(type).getQualifiedName().toString());
        vc.put("bindable", type.getAnnotation(Buildable.class).bindable());
        vc.put("generateGetters", type.getAnnotation(Buildable.class).generateGetters());
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
            if (isValidPropertyElement(element)) {
                list.add(new Property(element));
            }
        }
        return list;
    }

    private boolean isValidPropertyElement(VariableElement element) {
        if (element.getEnclosingElement().getKind() == ElementKind.CONSTRUCTOR) {
            return true;
        } else {
            return ((element.getAnnotation(Ignore.class) == null)
                    && (!element.getModifiers().contains(Modifier.STATIC))
                    && (!element.getModifiers().contains(Modifier.FINAL)));
        }
    }

    /**
     * This class is used to pass information from the annotation processor to
     * the template engine. It is intended for internal use only and should
     * never be used by clients.
     *
     * @author Petter Holmström
     * @since 1.0
     */
    public final class Property {

        private final VariableElement element;

        Property(VariableElement element) {
            this.element = element;
        }

        /**
         * Returns the name of the property.
         */
        public String getName() {
            return element.getSimpleName().toString();
        }

        /**
         * Returns the capitalized name of the property.
         */
        public String getCapitalizedName() {
            return StringUtils.capitalize(getName());
        }

        /**
         * Returns the name of the type of the property.
         */
        public String getTypeName() {
            return element.asType().toString();
        }

        /**
         * Returns whether the property is required or not.
         */
        public boolean isRequired() {
            return element.getAnnotation(Required.class) != null;
        }

        /**
         * Returns whether the required property can be set to null.
         */
        public boolean isNullAllowed() {
            final Required required = element.getAnnotation(Required.class);
            return required != null && required.allowNulls();
        }

        /**
         * Returns whether the property can be set to null or not (it is not a
         * primitive type).
         */
        public boolean isNullable() {
            return element.asType().getKind() == TypeKind.DECLARED;
        }

        /**
         * Returns the name of the getter method that returns the value of the
         * property.
         */
        public String getGetterName() {
            final Getter getter = element.getAnnotation(Getter.class);
            if (getter == null) {
                if (getTypeName().equals("boolean")) {
                    return "is" + getCapitalizedName();
                } else {
                    return "get" + getCapitalizedName();
                }
            }
            return getter.methodName();
        }

        /**
         * Returns the boxed type name, or the type name if it cannot be boxed.
         */
        public String getBoxedTypeName() {
            return getBoxedTypeName(element);
        }

        private String getBoxedTypeName(VariableElement element) {
            try {
                final Types utils = processingEnv.getTypeUtils();
                return utils.boxedClass(utils.getPrimitiveType(element.asType().getKind())).asType().toString();
            } catch (IllegalArgumentException e) {
            }
            return element.asType().toString();
        }
    }
}

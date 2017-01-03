package cc.corecoders.codegen4j.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class Builder extends Api {
  private static final String observer = "observer";
  private static final String bean = "bean";

  private final ClassName observerName;
  private TypeSpec.Builder builderSpec;
  private MethodSpec.Builder constructor;
  private MethodSpec.Builder observerConstructor;
  private Map<String, MethodSpec.Builder> withMethods = new HashMap<>();
  private MethodSpec.Builder buildMethod;

  Builder(ApiGenerator api) {
    this(api, null);
  }

  Builder(ApiGenerator api, ClassName observerName) {
    super(api, ClassName.get(api.getInterfaceName().packageName(), api.getInterfaceName().simpleName() + ApiGenerator.BuilderSufix));
    this.observerName = observerName;

    ParameterizedTypeName optionalObserverName = ParameterizedTypeName.get(ClassName.get(Optional.class), this.observerName);
    this.builderSpec = TypeSpec.classBuilder(className.simpleName());

    this.constructor = MethodSpec.constructorBuilder();
    this.buildMethod = MethodSpec.methodBuilder("build")
        .addModifiers(Modifier.PUBLIC)
        .returns(api.getInterfaceName())
        .addStatement("$T $L = new $T()", api.getBeanName(), bean, api.getBeanName());

    if(isObservable()) {
      this.builderSpec.addField(optionalObserverName, Builder.observer, Modifier.PRIVATE);
      this.constructor.addStatement("this.$L = Optional.empty()", Builder.observer);
      this.observerConstructor = MethodSpec.constructorBuilder();
      this.observerConstructor.addParameter(this.observerName, Builder.observer);
      this.observerConstructor.addStatement("this.$L = Optional.of($L)", Builder.observer, Builder.observer);
    }
  }

  @Override
  TypeSpec.Builder builder() {
    return builderSpec;
  }

  @Override
  void addProperty(Property property) {
    String fieldName = property.field.getName();
    builderSpec.addField(property.field.getGenericType(), fieldName, Modifier.PRIVATE);

    if(property.annotation.required()) {
      constructor.addParameter(property.field.getType(), fieldName);
      constructor.addStatement("this.$L = $L", fieldName, fieldName);

      buildMethod.addStatement("if($L == $L) throw new $T(\"Required field missing ($L)\")", fieldName, property.annotation.init(), IllegalArgumentException.class, fieldName);

    } else {
      constructor.addStatement("this.$L = $L", fieldName, property.annotation.init());
    }

    if(isObservable()) {
      observerConstructor.addStatement("this.$L = $L.get$L().get$L()",
          fieldName, observer, BuilderObserver.Reference, Generators.mCase(fieldName)
      );
    }

    withMethod(property);

    buildMethod.addStatement("$L.set$L($L)", bean, Generators.mCase(fieldName), fieldName);
  }

  @Override
  void addProperties(List<Property> properties) {
    builderSpec.addMethod(constructor.build());
    if(isObservable())
      builderSpec.addMethod(observerConstructor.build());

    for(MethodSpec.Builder withMethod: withMethods.values()) {
      withMethod.addStatement("return this");
      builderSpec.addMethod(withMethod.build());
    }

    builderSpec.addMethod(buildMethod.addStatement("return $L", bean).build());
  }

  private void withMethod(Property property) {

    String name = property.field.getName();
    String Name = Generators.mCase(name);
    MethodSpec.Builder withMethod;
    if(property.annotation.group().isEmpty()) {
      withMethod = MethodSpec.methodBuilder("with" + Name)
          .addModifiers(Modifier.PUBLIC)
          .returns(className);
      withMethods.put(Name, withMethod);

    } else {
        withMethod = withMethods.get(property.annotation.group());
        if (withMethod == null) {
          withMethod = MethodSpec.methodBuilder("with" + property.annotation.group())
              .addModifiers(Modifier.PUBLIC)
              .returns(className);
          withMethods.put(property.annotation.group(), withMethod);
        }
    }

    withMethod.addParameter(property.field.getGenericType(), name)
        .addStatement("this.$L = $L", name, name);
    if (isObservable())
      withMethod.addStatement("this.$L.ifPresent(d -> d.$L$L($L))", observer, BuilderObserver.notify, Name, name);
  }

  private boolean isObservable() {
    return observerName != null;
  }
}

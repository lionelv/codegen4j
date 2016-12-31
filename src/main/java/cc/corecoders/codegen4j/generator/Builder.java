package cc.corecoders.codegen4j.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

public class Builder extends Api {
  static final String observer = "observer";

  private final ClassName observerName;
  private final ParameterizedTypeName optionalObserverName;
  private TypeSpec.Builder builderSpec;
  private String ctorArgs;
  private MethodSpec.Builder constructor;
  private MethodSpec.Builder observerConstructor;

  Builder(ApiGenerator api) {
    this(api, null);
  }

  Builder(ApiGenerator api, ClassName observerName) {
    super(api, ClassName.get(api.getInterfaceName().packageName(), api.getInterfaceName().simpleName() + ApiGenerator.BuilderSufix));
    this.observerName = observerName;

    this.optionalObserverName = ParameterizedTypeName.get(ClassName.get(Optional.class), this.observerName);
    this.builderSpec = TypeSpec.classBuilder(className.simpleName());

    this.ctorArgs = "";

    this.constructor = MethodSpec.constructorBuilder();

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
    builderSpec.addMethod(withMethod(property.field));

    ctorArgs += ", " + fieldName;

    if(property.annotation.required()) {
      constructor.addParameter(property.field.getType(), fieldName);
      constructor.addStatement("this.$L = $L", fieldName, fieldName);
    } else {
      constructor.addStatement("this.$L = $L", fieldName, property.annotation.init());
    }

    if(isObservable()) {
      observerConstructor.addStatement("this.$L = $L.get$L().get$L()",
          fieldName, observer, BuilderObserver.Reference, Generators.mCase(fieldName)
      );
    }
  }

  @Override
  void addProperties(List<Property> properties) {
    builderSpec.addMethod(constructor.build());
    if(isObservable())
      builderSpec.addMethod(observerConstructor.build());
    builderSpec.addMethod(buildMethod());
  }

  private MethodSpec withMethod(Field field) {
    String name = field.getName();
    String Name = Generators.mCase(name);
    MethodSpec.Builder withMethod = MethodSpec.methodBuilder("with" + Name)
        .addModifiers(Modifier.PUBLIC)
        .returns(className)
        .addParameter(field.getGenericType(), name)
        .addStatement("this.$L = $L", name, name);
    if(isObservable())
      withMethod.addStatement("this.$L.ifPresent(d -> d.$L$L($L))", observer, BuilderObserver.notify, Name, name);

    return withMethod.addStatement("return this").build();
  }

  private MethodSpec buildMethod() {
    ctorArgs = ctorArgs.substring(2);

    return MethodSpec.methodBuilder("build")
               .addModifiers(Modifier.PUBLIC)
               .returns(api.getInterfaceName())
               .addStatement("return new $T($L)", className, ctorArgs)
               .build();
  }

  private boolean isObservable() {
    return observerName != null;
  }
}

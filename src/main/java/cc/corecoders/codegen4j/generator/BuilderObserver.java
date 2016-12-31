package cc.corecoders.codegen4j.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

public class BuilderObserver extends Api {

  private TypeSpec.Builder observerSpec;

  static final String reference = "reference";
  static final String Reference = Generators.mCase(reference);
  static final String notify = "notify";

  BuilderObserver(ApiGenerator api) {
    super(api, ClassName.get(api.getInterfaceName().packageName(), api.getInterfaceName().simpleName() + ApiGenerator.BuilderObserverSufix));
    this.observerSpec = TypeSpec.classBuilder(className.simpleName());
    this.observerSpec.addField(api.getInterfaceName(), reference, Modifier.PRIVATE, Modifier.FINAL);
    this.observerSpec.addMethod(MethodSpec.methodBuilder("get" + Reference)
        .returns(api.getInterfaceName())
        .addStatement("return this.$L", reference)
        .build());
  }

  @Override
  TypeSpec.Builder builder() {
    return observerSpec;
  }

  @Override
  void addProperty(Property property) {
    FieldSpec fieldSpec = FieldSpec.builder(boolean.class, property.field.getName(), Modifier.PRIVATE).initializer("true").build();
    observerSpec.addField(fieldSpec);

    observerSpec.addMethod(equalsMethod(property.field));
    observerSpec.addMethod(notifyMethod(property.field));
  }

  @Override
  void addProperties(List<Property> properties) {
    observerSpec.addMethod(constructorCopyMethod());
    observerSpec.addMethod(globalEqualsMethod(properties));
  }
  
  private MethodSpec constructorCopyMethod() {
    MethodSpec.Builder constructor = MethodSpec.constructorBuilder();
        constructor.addParameter(api.getInterfaceName(), reference);
        constructor.addStatement("this.$L = $L", reference, reference);
    return constructor.build();
  }

  private MethodSpec notifyMethod(Field field) {
    return MethodSpec.methodBuilder(notify + Generators.mCase(field.getName()))
               .addModifiers(Modifier.PUBLIC)
               .returns(boolean.class)
               .addParameter(field.getGenericType(), field.getName())
               .addStatement("this.$L = $T.equals($L.get$L(), $L)", field.getName(), Objects.class, reference, Generators.mCase(field.getName()), field.getName())
               .addStatement("return this.$L", field.getName())
               .build();
  }

  private MethodSpec equalsMethod(Field field) {
    return MethodSpec.methodBuilder("equals" + Generators.mCase(field.getName()))
               .addModifiers(Modifier.PUBLIC)
               .returns(boolean.class)
               .addStatement("return this.$L", field.getName())
               .build();
  }

  private MethodSpec globalEqualsMethod(List<Property> allFields) {
    String andStatement = "";
    for(Property p: allFields)
      andStatement += " && " + p.field.getName();
    andStatement = andStatement.substring(4);

    return MethodSpec.methodBuilder("equals")
               .addModifiers(Modifier.PUBLIC)
               .returns(boolean.class)
               .addStatement("return $L", andStatement)
               .build();
  }

}

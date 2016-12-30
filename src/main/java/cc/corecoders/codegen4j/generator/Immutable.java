package cc.corecoders.codegen4j.generator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

class Immutable extends Api {

  private TypeSpec.Builder immutableSpec;

  private final MethodSpec.Builder constructorMethod;
  private final String source = "source";

  Immutable(ApiGenerator api) {
    super(api, ClassName.get(api.getInterfaceName().packageName(), ApiGenerator.ImmutablePrefix + api.getInterfaceName().simpleName()));
    immutableSpec = TypeSpec.classBuilder(className.simpleName());
    immutableSpec.addSuperinterface(api.getInterfaceName());

    this.constructorMethod = MethodSpec.constructorBuilder()
        .addParameter(api.getInterfaceName(), source);
  }

  @Override
  TypeSpec.Builder builder() {
    return immutableSpec;
  }

  @Override
  void addProperty(Property property) {
    immutableSpec.addField(property.field.getGenericType(), property.field.getName(), Modifier.PRIVATE, Modifier.FINAL);
    immutableSpec.addMethod(getterMethod(property.field));
    addConstructorStatement(property.field);
  }

  @Override
  void addProperties(List<Property> properties) {
    immutableSpec.addMethod(constructorMethod.build());
  }

  private MethodSpec getterMethod(Field field) {
    String name = field.getName();
    String Name = Generators.mCase(name);
    MethodSpec.Builder method = MethodSpec.methodBuilder("get" + Name)
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(Override.class)
        .returns(field.getGenericType())
        .addStatement("return $L", name);
    return method.build();
  }


  private void addConstructorStatement(Field field) {
    String name = field.getName();
    String Name = Generators.mCase(name);
    String returnType = field.getType().getName();
    if(Generators.immutableTypes.contains(returnType))
      constructorMethod.addStatement("this.$L = $L.get$L()", name, source, Name);
    else if (List.class.getName().equals(returnType))
      immutableStatement(name, Name, ImmutableList.class);
    else if (Set.class.getName().equals(returnType))
      immutableStatement(name, Name, ImmutableSet.class);
    else if (Map.class.getName().equals(returnType))
      immutableStatement(name, Name, ImmutableMap.class);
    else {
      constructorMethod.addStatement("return $L.get$L()", source, Name);
    }
  }

  private void immutableStatement(String name, String Name, Class<?> clazz) {
    constructorMethod.addStatement("this.$L = $T.copyOf($L.get$L())", name, clazz, source, Name);
  }

}

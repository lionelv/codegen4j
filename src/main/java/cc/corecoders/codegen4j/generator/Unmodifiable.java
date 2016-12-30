package cc.corecoders.codegen4j.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Unmodifiable extends Api {
  private static final String source = "source";

  private final TypeSpec.Builder unmodifiableSpec;

  Unmodifiable(ApiGenerator api) {
    super(api, ClassName.get(api.getInterfaceName().packageName(), ApiGenerator.UnmodifiablePrefix + api.getInterfaceName().simpleName()));
    unmodifiableSpec = TypeSpec.classBuilder(className.simpleName());
    unmodifiableSpec.addSuperinterface(api.getInterfaceName());
    unmodifiableSpec.addField(api.getInterfaceName(), source, Modifier.PRIVATE, Modifier.FINAL);
    unmodifiableSpec.addMethod(MethodSpec.constructorBuilder()
        .addParameter(api.getInterfaceName(), source)
        .addStatement("this.$L = $L", source, source)
        .build());
  }

  @Override
  TypeSpec.Builder builder() {
    return unmodifiableSpec;
  }

  @Override
  void addProperty(Property property) {
    unmodifiableSpec.addMethod(getterMethod(property.field));
  }

  private MethodSpec getterMethod(Field field) {
    String Name = Generators.mCase(field.getName());
    MethodSpec.Builder method = MethodSpec.methodBuilder("get" + Name)
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(Override.class)
        .returns(field.getGenericType());

    String returnType = field.getType().getName();
    if(Generators.immutableTypes.contains(returnType))
      method.addStatement("return $L.get$L()", source, Name);
    else if (List.class.getName().equals(returnType))
      unmodifiableStatement(Name, method, List.class.getSimpleName());
    else if (Set.class.getName().equals(returnType))
      unmodifiableStatement(Name, method, Set.class.getSimpleName());
    else if (Map.class.getName().equals(returnType))
      unmodifiableStatement(Name, method, Map.class.getSimpleName());
    else {
      method.addComment("unmodifiable contract not fulfilled");
      method.addStatement("return $L.get$L()", source, Name);
    }

    return method.build();
  }

  private void unmodifiableStatement(String name, MethodSpec.Builder method, String simpleName) {
    method.addStatement("return $T.unmodifiable$L($L.get$L())", Collections.class, simpleName, source, name);
  }

}

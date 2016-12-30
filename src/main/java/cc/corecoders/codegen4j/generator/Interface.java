package cc.corecoders.codegen4j.generator;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Field;

class Interface extends Api {
  private TypeSpec.Builder interfaceSpec;

  Interface(ApiGenerator api) {
    super(api, api.name);
    api.setInterfaceName(api.name);

    interfaceSpec = TypeSpec.interfaceBuilder(className.simpleName());
    interfaceSpec.addModifiers(Modifier.PUBLIC);
  }

  @Override
  TypeSpec.Builder builder() {
    return interfaceSpec;
  }

  @Override
  void addProperty(Property property) {
    interfaceSpec.addMethod(getterMethod(property.field));
  }

  private MethodSpec getterMethod(Field field) {
    return MethodSpec.methodBuilder("get" + Generators.mCase(field.getName()))
               .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
               .returns(field.getGenericType())
               .build();
  }
}

package cc.corecoders.codegen4j.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Field;

class Bean extends Api {
  private TypeSpec.Builder beanSpec;

  Bean(ApiGenerator api) {
    super(api, ClassName.get(api.getInterfaceName().packageName(), api.getInterfaceName().simpleName() + ApiGenerator.BeanSuffix));
    beanSpec = TypeSpec.classBuilder(className.simpleName());
    beanSpec.addSuperinterface(api.getInterfaceName());
    beanSpec.addMethod(MethodSpec.constructorBuilder().build());
  }

  @Override
  TypeSpec.Builder builder() {
    return beanSpec;
  }

  @Override
  void addProperty(Property property) {
    beanSpec.addField(property.field.getGenericType(), property.field.getName(), Modifier.PRIVATE);
    beanSpec.addMethod(getterMethod(property.field));
    beanSpec.addMethod(setterMethod(property.field));
  }


  private MethodSpec getterMethod(Field field) {
    String name = field.getName();
    return MethodSpec.methodBuilder("get" + Generators.mCase(name))
               .addModifiers(Modifier.PUBLIC)
               .addAnnotation(Override.class)
               .returns(field.getGenericType())
               .addStatement("return $L", name)
               .build();
  }

  private MethodSpec setterMethod(Field field) {
    String name = field.getName();
    return MethodSpec.methodBuilder("set" + Generators.mCase(name))
               .addModifiers(Modifier.PUBLIC)
               .addParameter(field.getGenericType(), name)
               .addStatement("this.$L = $L", name, name)
               .build();
  }
}

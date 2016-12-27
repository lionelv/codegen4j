package cc.corecoders.codegen4j.generator;

import cc.corecoders.codegen4j.annotation.ApiClass;
import cc.corecoders.codegen4j.annotation.ApiProperty;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Bean extends AbstractGenerator {
  private final ClassName apiName;
  private final ClassName beanName;

  public Bean(Class<?> clazz, ClassName apiName, String bean) {
    super(clazz);
    this.apiName = apiName;
    this.beanName = ClassName.get(apiName.packageName(), apiName.simpleName() + bean);
  }

  @Override
  public List<JavaFile> generate() {
    List<JavaFile> javaFiles = new ArrayList<>();
    ApiClass classAnnotation = clazz.getAnnotation(ApiClass.class);
    if(classAnnotation == null)
      return javaFiles;

    TypeSpec.Builder classSpec = TypeSpec.classBuilder(beanName.simpleName());
    classSpec.addSuperinterface(apiName);
    classSpec.addMethod(MethodSpec.constructorBuilder().build());

    for(Field field: clazz.getDeclaredFields()) {
      ApiProperty fieldAnnotation = field.getAnnotation(ApiProperty.class);
      if(fieldAnnotation == null)
        continue;

      classSpec.addField(field.getGenericType(), field.getName(), Modifier.PRIVATE);
      classSpec.addMethod(getterMethod(field));
      classSpec.addMethod(setterMethod(field));
    }

    JavaFile.Builder fileBuilder = JavaFile.builder(beanName.packageName(), classSpec.build());
    fileBuilder.addFileComment("Generated file, any modification can be lost...");

    javaFiles.add(fileBuilder.build());
    return javaFiles;

  }

  private MethodSpec getterMethod(Field field) {
    String name = field.getName();
    return MethodSpec.methodBuilder("get" + mCase(name))
               .addModifiers(Modifier.PUBLIC)
               .returns(field.getGenericType())
               .addStatement("return $L", name)
               .build();
  }

  private MethodSpec setterMethod(Field field) {
    String name = field.getName();
    return MethodSpec.methodBuilder("set" + mCase(name))
               .addModifiers(Modifier.PUBLIC)
               .addParameter(field.getGenericType(), name)
               .addStatement("this.$L = $L", name, name)
               .build();
  }
}

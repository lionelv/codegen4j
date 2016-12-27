package cc.corecoders.codegen4j.generator;

import cc.corecoders.codegen4j.annotation.ApiClass;
import cc.corecoders.codegen4j.annotation.ApiProperty;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Interface extends AbstractGenerator {
  private final ClassName interfaceName;

  public Interface(Class<?> clazz, ClassName apiName) {
    super(clazz);
    this.interfaceName = apiName;
  }

  @Override
  public List<JavaFile> generate() {
    List<JavaFile> javaFiles = new ArrayList<>();
    ApiClass classAnnotation = clazz.getAnnotation(ApiClass.class);
    if(classAnnotation == null)
      return javaFiles;

    TypeSpec.Builder classSpec = TypeSpec.interfaceBuilder(interfaceName.simpleName());
    classSpec.addModifiers(Modifier.PUBLIC);

    for(Field field: clazz.getDeclaredFields()) {
      ApiProperty fieldAnnotation = field.getAnnotation(ApiProperty.class);
      if(fieldAnnotation == null)
        continue;

      classSpec.addMethod(getterMethod(field));
    }

    JavaFile.Builder fileBuilder = JavaFile.builder(interfaceName.packageName(), classSpec.build());
    fileBuilder.addFileComment("Generated file, any modification can be lost...");

    javaFiles.add(fileBuilder.build());
    return javaFiles;

  }

  private MethodSpec getterMethod(Field field) {
    String name = field.getName();
    return MethodSpec.methodBuilder("get" + mCase(name))
               .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
               .returns(field.getGenericType())
               .build();
  }
}

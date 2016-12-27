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

public class Unmodifiable extends AbstractGenerator {

  static final String source = "source";

  private final ClassName apiName;
  private final ClassName unmodifiableName;

  public Unmodifiable(Class<?> clazz, ClassName apiName, String unmodifiable) {
    super(clazz);
    this.apiName = apiName;
    this.unmodifiableName = ClassName.get(apiName.packageName(), apiName.simpleName() + unmodifiable);
  }

  @Override
  public List<JavaFile> generate() {
    List<JavaFile> javaFiles = new ArrayList<>();
    ApiClass classAnnotation = clazz.getAnnotation(ApiClass.class);
    if(classAnnotation == null)
      return javaFiles;

    System.out.println("Generating unmodifiable");

    TypeSpec.Builder classSpec = TypeSpec.classBuilder(unmodifiableName.simpleName());
    classSpec.addField(apiName, source, Modifier.PRIVATE, Modifier.FINAL);
    classSpec.addMethod(MethodSpec.constructorBuilder()
        .addParameter(apiName, source)
        .addStatement("this.$L = $L", source, source)
        .build()
    );

    for(Field field: clazz.getDeclaredFields()) {
      ApiProperty fieldAnnotation = field.getAnnotation(ApiProperty.class);
      if(fieldAnnotation == null)
        continue;

      classSpec.addMethod(getterMethod(field));
    }

    JavaFile.Builder fileBuilder = JavaFile.builder(unmodifiableName.packageName(), classSpec.build());
    fileBuilder.addFileComment("Generated file, any modification can be lost...");

    javaFiles.add(fileBuilder.build());
    return javaFiles;

  }

  private MethodSpec getterMethod(Field field) {
    String Name = mCase(field.getName());
    return MethodSpec.methodBuilder("get" + Name)
               .addModifiers(Modifier.PUBLIC)
               .returns(field.getGenericType())
               .addStatement("return $L.get$L()", source, Name)
               .build();
  }
}

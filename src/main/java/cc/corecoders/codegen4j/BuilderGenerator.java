package cc.corecoders.codegen4j;

import cc.corecoders.codegen4j.annotation.BuilderClassGeneration;
import cc.corecoders.codegen4j.annotation.BuilderFieldSpec;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class BuilderGenerator extends CodeGenerator {
  private static final String THAT = "that";

  private final ClassName className;
  private final ClassName builderName;

  BuilderGenerator(Class<?> clazz) {
    super(clazz);
    this.className = ClassName.get(clazz.getPackage().getName(), clazz.getSimpleName());
    this.builderName = ClassName.get(clazz.getPackage().getName(), clazz.getSimpleName() + "Builder");
  }

  @Override
  public List<JavaFile> generate() {
    List<JavaFile> javaFiles = new ArrayList<>();
    BuilderClassGeneration classAnnotation = clazz.getAnnotation(BuilderClassGeneration.class);
    if(classAnnotation == null)
      return javaFiles;

    System.out.println("Generating builder");

    TypeSpec.Builder builderSpec = TypeSpec.classBuilder(builderName.simpleName());
    builderSpec.addField(className, THAT, Modifier.PRIVATE);

    List<MethodParam> allFields = new ArrayList<>();
    for(Field field:clazz.getDeclaredFields()) {
      BuilderFieldSpec fieldAnnotation = field.getAnnotation(BuilderFieldSpec.class);
      if(fieldAnnotation == null)
        continue;

      builderSpec.addField(field.getGenericType(), field.getName(), Modifier.PRIVATE);
      builderSpec.addField(boolean.class, field.getName() + "Change", Modifier.PRIVATE);

      MethodParam param = new MethodParam(fieldAnnotation, field);
      allFields.add(param);

      if(fieldAnnotation.immutable())
        continue;

      builderSpec.addMethod(withMethod(field));
    }

    allFields.sort(Comparator.comparingInt(p -> p.spec.order()));

    builderSpec.addMethod(constructorCopyMethod());
    builderSpec.addMethod(constructorMethod(allFields));
    builderSpec.addMethod(buildMethod(allFields));

    JavaFile.Builder fileBuilder = JavaFile.builder(builderName.packageName(), builderSpec.build());
    fileBuilder.addFileComment("Generated file, any modification can be lost...");

    javaFiles.add(fileBuilder.build());
    return javaFiles;

  }

  private MethodSpec constructorCopyMethod() {
    MethodSpec.Builder constructor = MethodSpec.constructorBuilder();
        constructor.addParameter(className, THAT);
        constructor.addStatement("this.$L = $L", THAT, THAT);
    return constructor.build();
  }

  private MethodSpec constructorMethod(List<MethodParam> fields) {
    MethodSpec.Builder constructor = MethodSpec.constructorBuilder();
    for(MethodParam param: fields) {
      if(param.spec.required()) {
        constructor.addParameter(param.field.getType(), param.field.getName());
        constructor.addStatement("this.$L = $L", param.field.getName(), param.field.getName());
      } else {
        constructor.addStatement("this.$L = $L", param.field.getName(), param.spec.init());
      }
    }
    return constructor.build();
  }

  private MethodSpec withMethod(Field field) {
    return MethodSpec.methodBuilder("with" + firstCharacterToUpperCase(field.getName()))
               .addModifiers(Modifier.PUBLIC)
               .returns(builderName)
               .addParameter(field.getGenericType(), field.getName())
               .addStatement("this.$L = $L", field.getName(), field.getName())
               .addStatement("this.$LChange = !$T.equals($L.get$L(), $L)", field.getName(), Objects.class, THAT, firstCharacterToUpperCase(field.getName()), field.getName())
               .addStatement("return this")
               .build();
  }

  private MethodSpec buildMethod(List<MethodParam> allFields) {
    String ctorArgs = "";
    for(MethodParam p: allFields)
      ctorArgs += ", " + p.field.getName();
    ctorArgs = ctorArgs.substring(2);

    return MethodSpec.methodBuilder("build")
               .addModifiers(Modifier.PUBLIC)
               .returns(className)
               .addStatement("return new $T($L)", className, ctorArgs)
               .build();
  }
}

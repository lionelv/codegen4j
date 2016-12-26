package cc.corecoders.codegen4j.generator;

import cc.corecoders.codegen4j.annotation.BuilderClassGeneration;
import cc.corecoders.codegen4j.annotation.BuilderFieldSpec;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Field;
import java.util.*;

public class Builder extends AbstractGenerator {
  final String ClassExtension = "Builder";
  private static final String DIFF = "diff";

  private final ClassName className;
  private final ClassName builderName;
  private final ClassName diffName;
  private final ParameterizedTypeName optionalDiffName;

  Builder(Class<?> clazz) {
    super(clazz);
    this.className = ClassName.get(clazz.getPackage().getName(), clazz.getSimpleName());
    this.builderName = ClassName.get(clazz.getPackage().getName(), clazz.getSimpleName() + ClassExtension);
    this.diffName = ClassName.get(clazz.getPackage().getName(), clazz.getSimpleName() + BuilderObserver.ClassExtension);
    this.optionalDiffName = ParameterizedTypeName.get(ClassName.get(Optional.class), diffName);
  }

  @Override
  public List<JavaFile> generate() {
    List<JavaFile> javaFiles = new ArrayList<>();
    BuilderClassGeneration classAnnotation = clazz.getAnnotation(BuilderClassGeneration.class);
    if(classAnnotation == null)
      return javaFiles;

    System.out.println("Generating builder");

    TypeSpec.Builder builderSpec = TypeSpec.classBuilder(builderName.simpleName());
    builderSpec.addField(optionalDiffName, DIFF, Modifier.PRIVATE);

    List<MethodParam> allFields = new ArrayList<>();
    for(Field field:clazz.getDeclaredFields()) {
      BuilderFieldSpec fieldAnnotation = field.getAnnotation(BuilderFieldSpec.class);
      if(fieldAnnotation == null)
        continue;

      builderSpec.addField(field.getGenericType(), field.getName(), Modifier.PRIVATE);

      MethodParam param = new MethodParam(fieldAnnotation, field);
      allFields.add(param);

      if(fieldAnnotation.immutable())
        continue;

      builderSpec.addMethod(withMethod(field));
    }

    allFields.sort(Comparator.comparingInt(p -> p.spec.order()));

    builderSpec.addMethod(diffConstructorMethod(allFields));
    builderSpec.addMethod(constructorMethod(allFields));
    builderSpec.addMethod(buildMethod(allFields));

    JavaFile.Builder fileBuilder = JavaFile.builder(builderName.packageName(), builderSpec.build());
    fileBuilder.addFileComment("Generated file, any modification can be lost...");

    javaFiles.add(fileBuilder.build());
    return javaFiles;

  }

  private MethodSpec diffConstructorMethod(List<MethodParam> fields) {
    MethodSpec.Builder constructor = MethodSpec.constructorBuilder();
    constructor.addParameter(diffName, DIFF);
    constructor.addStatement("this.$L = Optional.of($L)", DIFF, DIFF);
    for(MethodParam param: fields) {
      constructor.addStatement(
          "this.$L = $L.get$L().get$L()",
          param.field.getName(), DIFF, mCase(BuilderObserver.ReferenceField), mCase(param.field.getName())
      );
    }
    return constructor.build();
  }

  private MethodSpec constructorMethod(List<MethodParam> fields) {
    MethodSpec.Builder constructor = MethodSpec.constructorBuilder();
    constructor.addStatement("this.$L = Optional.empty()", DIFF);
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
    String name = field.getName();
    String Name = mCase(name);
    return MethodSpec.methodBuilder("with" + Name)
               .addModifiers(Modifier.PUBLIC)
               .returns(builderName)
               .addParameter(field.getGenericType(), name)
               .addStatement("this.$L = $L", name, name)
               .addStatement("this.$L.ifPresent(d -> d.diff$L($L))", DIFF, Name, name)
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

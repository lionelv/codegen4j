package cc.corecoders.codegen4j.generator;

import cc.corecoders.codegen4j.annotation.BuilderClassGeneration;
import cc.corecoders.codegen4j.annotation.BuilderFieldSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class DiffGenerator extends CodeGenerator {
  static final String ClassExtension = "Diff";
  static final String ReferenceField = "ref";

  private final ClassName className;
  private final ClassName diffName;

  DiffGenerator(Class<?> clazz) {
    super(clazz);
    this.className = ClassName.get(clazz.getPackage().getName(), clazz.getSimpleName());
    this.diffName = ClassName.get(clazz.getPackage().getName(), clazz.getSimpleName() + ClassExtension);
  }

  @Override
  public List<JavaFile> generate() {
    List<JavaFile> javaFiles = new ArrayList<>();
    BuilderClassGeneration classAnnotation = clazz.getAnnotation(BuilderClassGeneration.class);
    if(classAnnotation == null)
      return javaFiles;

    System.out.println("Generating auditor");

    TypeSpec.Builder diffSpec = TypeSpec.classBuilder(diffName.simpleName());
    diffSpec.addField(className, ReferenceField, Modifier.PRIVATE);
    diffSpec.addMethod(getterMethod(className, ReferenceField));

    List<MethodParam> allFields = new ArrayList<>();
    for(Field field:clazz.getDeclaredFields()) {
      BuilderFieldSpec fieldAnnotation = field.getAnnotation(BuilderFieldSpec.class);
      if(fieldAnnotation == null)
        continue;

      diffSpec.addField(boolean.class, field.getName(), Modifier.PRIVATE);

      MethodParam param = new MethodParam(fieldAnnotation, field);
      allFields.add(param);

      diffSpec.addMethod(equalsMethod(field));
      diffSpec.addMethod(diffMethod(field));
    }

    allFields.sort(Comparator.comparingInt(p -> p.spec.order()));

    diffSpec.addMethod(constructorCopyMethod());
    MethodSpec diffMethod = globalDiffMethod(allFields);
    diffSpec.addMethod(diffMethod);
    diffSpec.addMethod(globalEqualsMethod(diffMethod));

    JavaFile.Builder fileBuilder = JavaFile.builder(diffName.packageName(), diffSpec.build());
    fileBuilder.addFileComment("Generated file, any modification can be lost...");

    javaFiles.add(fileBuilder.build());
    return javaFiles;

  }

  private MethodSpec constructorCopyMethod() {
    MethodSpec.Builder constructor = MethodSpec.constructorBuilder();
        constructor.addParameter(className, ReferenceField);
        constructor.addStatement("this.$L = $L", ReferenceField, ReferenceField);
    return constructor.build();
  }

  private MethodSpec diffMethod(Field field) {
    return MethodSpec.methodBuilder("diff" + mCase(field.getName()))
               .addModifiers(Modifier.PUBLIC)
               .returns(boolean.class)
               .addParameter(field.getGenericType(), field.getName())
               .addStatement("this.$L = !$T.equals($L.get$L(), $L)", field.getName(), Objects.class, ReferenceField, mCase(field.getName()), field.getName())
               .addStatement("return this.$L", field.getName())
               .build();
  }

  private MethodSpec equalsMethod(Field field) {
    return MethodSpec.methodBuilder("equals" + mCase(field.getName()))
               .addModifiers(Modifier.PUBLIC)
               .returns(boolean.class)
               .addStatement("return !this.$L", field.getName())
               .build();
  }

  private MethodSpec globalDiffMethod(List<MethodParam> allFields) {
    String orStatement = "";
    for(MethodParam p: allFields)
      orStatement += " || " + p.field.getName();
    orStatement = orStatement.substring(4);

    return MethodSpec.methodBuilder("diff")
               .addModifiers(Modifier.PUBLIC)
               .returns(boolean.class)
               .addStatement("return $L", orStatement)
               .build();
  }

  private MethodSpec globalEqualsMethod(MethodSpec diffMethod) {
    return MethodSpec.methodBuilder("equals")
               .addModifiers(Modifier.PUBLIC)
               .returns(boolean.class)
               .addStatement("return !$L()", diffMethod.name)
               .build();
  }

}

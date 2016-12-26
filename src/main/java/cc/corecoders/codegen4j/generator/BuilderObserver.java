package cc.corecoders.codegen4j.generator;

import cc.corecoders.codegen4j.annotation.BuilderClassGeneration;
import cc.corecoders.codegen4j.annotation.BuilderFieldSpec;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class BuilderObserver extends AbstractGenerator {
  static final String ClassExtension = "Observer";
  static final String reference = "Reference";
  static final String Reference = mCase(reference);
  static final String notify = "notify";

  private final ClassName className;
  private final ClassName observerName;

  public BuilderObserver(Class<?> clazz) {
    super(clazz);
    this.className = ClassName.get(clazz.getPackage().getName(), clazz.getSimpleName());
    this.observerName = ClassName.get(clazz.getPackage().getName(), clazz.getSimpleName() + ClassExtension);
  }

  @Override
  public List<JavaFile> generate() {
    List<JavaFile> javaFiles = new ArrayList<>();
    BuilderClassGeneration classAnnotation = clazz.getAnnotation(BuilderClassGeneration.class);
    if(classAnnotation == null)
      return javaFiles;

    System.out.println("Generating auditor");

    TypeSpec.Builder notifySpec = TypeSpec.classBuilder(observerName.simpleName());
    notifySpec.addField(className, reference, Modifier.PRIVATE, Modifier.FINAL);
    notifySpec.addMethod(getterMethod(className, reference));

    List<MethodParam> allFields = new ArrayList<>();
    for(Field field:clazz.getDeclaredFields()) {
      BuilderFieldSpec fieldAnnotation = field.getAnnotation(BuilderFieldSpec.class);
      if(fieldAnnotation == null)
        continue;

      FieldSpec fieldSpec = FieldSpec.builder(boolean.class, field.getName(), Modifier.PRIVATE).initializer("true;").build();
      notifySpec.addField(fieldSpec);

      MethodParam param = new MethodParam(fieldAnnotation, field);
      allFields.add(param);

      notifySpec.addMethod(equalsMethod(field));
      notifySpec.addMethod(notifyMethod(field));
    }

    allFields.sort(Comparator.comparingInt(p -> p.spec.order()));

    notifySpec.addMethod(constructorCopyMethod());
    notifySpec.addMethod(globalEqualsMethod(allFields));

    JavaFile.Builder fileBuilder = JavaFile.builder(observerName.packageName(), notifySpec.build());
    fileBuilder.addFileComment("Generated file, any modification can be lost...");

    javaFiles.add(fileBuilder.build());
    return javaFiles;

  }

  private MethodSpec constructorCopyMethod() {
    MethodSpec.Builder constructor = MethodSpec.constructorBuilder();
        constructor.addParameter(className, reference);
        constructor.addStatement("this.$L = $L", reference, reference);
    return constructor.build();
  }

  private MethodSpec notifyMethod(Field field) {
    return MethodSpec.methodBuilder(notify + mCase(field.getName()))
               .addModifiers(Modifier.PUBLIC)
               .returns(boolean.class)
               .addParameter(field.getGenericType(), field.getName())
               .addStatement("this.$L = $T.equals($L.get$L(), $L)", field.getName(), Objects.class, reference, mCase(field.getName()), field.getName())
               .addStatement("return this.$L", field.getName())
               .build();
  }

  private MethodSpec equalsMethod(Field field) {
    return MethodSpec.methodBuilder("equals" + mCase(field.getName()))
               .addModifiers(Modifier.PUBLIC)
               .returns(boolean.class)
               .addStatement("return this.$L", field.getName())
               .build();
  }

  private MethodSpec globalEqualsMethod(List<MethodParam> allFields) {
    String andStatement = "";
    for(MethodParam p: allFields)
      andStatement += " && " + p.field.getName();
    andStatement = andStatement.substring(4);

    return MethodSpec.methodBuilder("equals")
               .addModifiers(Modifier.PUBLIC)
               .returns(boolean.class)
               .addStatement("return $L", andStatement)
               .build();
  }

}

package cc.corecoders.codegen4j.generator;


import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Field;
import java.util.List;

public abstract class AbstractGenerator {
  static final String SpecExtension = "Spec";

  static String className(String simpleName) {
    return simpleName.substring(0, simpleName.length() - SpecExtension.length());
  }

  final Class<?> clazz;

  AbstractGenerator(Class<?> clazz) {
    this.clazz = clazz;
  }

  MethodSpec getterMethod(ClassName className, String ref) {
    return MethodSpec.methodBuilder("get" + Generators.mCase(ref))
               .addModifiers(Modifier.PUBLIC)
               .returns(className)
               .addStatement("return this.$L", ref)
               .build();
  }

  public abstract List<JavaFile> generate();


}

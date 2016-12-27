package cc.corecoders.codegen4j.generator;


import cc.corecoders.codegen4j.annotation.BuilderFieldSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Field;
import java.util.List;

public abstract class AbstractGenerator {
  static final String SpecExtension = "Spec";

  static String mCase(String str) {
    char c = Character.toUpperCase(str.charAt(0));
    return c + str.substring(1);
  }

  static String className(String simpleName) {
    return simpleName.substring(0, simpleName.length() - SpecExtension.length());
  }

  class MethodParam {
    final BuilderFieldSpec spec;
    final Field field;

    MethodParam(BuilderFieldSpec spec, Field field) {
      this.spec = spec;
      this.field = field;
    }
  }

  final Class<?> clazz;

  AbstractGenerator(Class<?> clazz) {
    this.clazz = clazz;
  }

  MethodSpec getterMethod(ClassName className, String ref) {
    return MethodSpec.methodBuilder("get" + mCase(ref))
               .addModifiers(Modifier.PUBLIC)
               .returns(className)
               .addStatement("return this.$L", ref)
               .build();
  }

  public abstract List<JavaFile> generate();

}

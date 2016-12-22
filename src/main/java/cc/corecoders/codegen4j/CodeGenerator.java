package cc.corecoders.codegen4j;


import cc.corecoders.codegen4j.annotation.BuilderFieldSpec;
import com.squareup.javapoet.JavaFile;

import java.lang.reflect.Field;
import java.util.List;

public abstract class CodeGenerator {

  final Class<?> clazz;

  CodeGenerator(Class<?> clazz) {
    this.clazz = clazz;
  }

  class MethodParam {
    final BuilderFieldSpec spec;
    final Field field;

    MethodParam(BuilderFieldSpec spec, Field field) {
      this.spec = spec;
      this.field = field;
    }
  }

  public abstract List<JavaFile> generate();

  String firstCharacterToUpperCase(String str) {
    char c = Character.toUpperCase(str.charAt(0));
    return c + str.substring(1);
  }

}

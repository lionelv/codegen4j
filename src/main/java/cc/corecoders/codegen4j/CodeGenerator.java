package cc.corecoders.codegen4j;


import cc.corecoders.codegen4j.annotation.BuilderFieldSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Modifier;
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


  MethodSpec getterMethod(ClassName className, String ref) {
    return MethodSpec.methodBuilder("get" + mCase(ref))
               .addModifiers(Modifier.PUBLIC)
               .returns(className)
               .addStatement("return this.$L", ref)
               .build();
  }
  String mCase(String str) {
    char c = Character.toUpperCase(str.charAt(0));
    return c + str.substring(1);
  }

}

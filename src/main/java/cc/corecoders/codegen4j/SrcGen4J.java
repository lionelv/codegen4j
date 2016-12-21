package cc.corecoders.codegen4j;


import cc.corecoders.codegen4j.annotation.BuilderClassGeneration;
import cc.corecoders.codegen4j.annotation.BuilderFieldSpec;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class SrcGen4J {

  public static void main(String[] args) throws IOException, ClassNotFoundException {
    if (args.length != 2) {
      System.out.println("usage : cmd <file.jar> <path/to/package>");
      return;
    }

    String jarFile = args[0];
    String namespace = args[1];

    new SrcGen4J(jarFile, namespace);
  }

  public class MethodParam {
    public final BuilderFieldSpec spec;
    public final Field field;

    public MethodParam(BuilderFieldSpec spec, Field field) {
      this.spec = spec;
      this.field = field;
    }
  }

  private final URLClassLoader loader;

  public SrcGen4J(String jarPath, String namespace) throws IOException, ClassNotFoundException {
    JarFile jarFile = new JarFile(jarPath);
    URL jarUrl = new URL("jar", "file://", jarPath + "!/");
    loader = new URLClassLoader(new URL[] {jarUrl});

    Enumeration<JarEntry> entries = jarFile.entries();
    while(entries.hasMoreElements()) {
      JarEntry entry = entries.nextElement();
      String classPath = entry.getName();
      if (!entry.isDirectory() && classPath.startsWith(namespace) && classPath.endsWith(".class")) {
        String className = classPath.substring(0, classPath.length() - 6).replace('/', '.');
        Class<?> clazz = loader.loadClass(className);

        List<JavaFile> javaFiles = parseClass(clazz);

        for(JavaFile javaFile: javaFiles) {
          System.out.println(javaFile.toString());
        }
      }
    }
  }

  private List<JavaFile> parseClass(Class<?> clazz) {
    ArrayList<JavaFile> builders = new ArrayList<>();
    generateBuilderClass(clazz).ifPresent(builders::add);
    return builders;
  }


  private Optional<JavaFile> generateBuilderClass(Class<?> clazz) {
    BuilderClassGeneration classAnnotation = clazz.getAnnotation(BuilderClassGeneration.class);
    if(classAnnotation == null)
      return Optional.empty();

    System.out.println("Generating builder");

    ClassName className = ClassName.get(clazz.getPackage().getName(), clazz.getSimpleName());
    ClassName builderName = ClassName.get(clazz.getPackage().getName(), clazz.getSimpleName() + "Builder");
    TypeSpec.Builder builderSpec = TypeSpec.classBuilder(builderName.simpleName());

    String classCtorArgs = "";
    ArrayList<MethodParam> requiredFields = new ArrayList<>();
    for(Field field:clazz.getDeclaredFields()) {
      BuilderFieldSpec fieldAnnotation = field.getAnnotation(BuilderFieldSpec.class);
      if(fieldAnnotation == null)
        continue;

      builderSpec.addField(field.getGenericType(), field.getName(), Modifier.PRIVATE);
      classCtorArgs += ", " + field.getName();

      if(fieldAnnotation.required())
        requiredFields.add(new MethodParam(fieldAnnotation, field));

      if(fieldAnnotation.immutable())
        continue;

      builderSpec.addMethod(withMethod(builderName, field));
    }

    builderSpec.addMethod(requiredConstructor(requiredFields));
    builderSpec.addMethod(buildMethod(className, classCtorArgs));

    JavaFile.Builder fileBuilder = JavaFile.builder(builderName.packageName(), builderSpec.build());
    fileBuilder.addFileComment("Generated file, any modification can be lost...");
    return Optional.of(fileBuilder.build());

  }

  private MethodSpec requiredConstructor(ArrayList<MethodParam> requiredFields) {
    MethodSpec.Builder constructor = MethodSpec.constructorBuilder();
    requiredFields.sort(Comparator.comparingInt(l -> l.spec.order()));
    for(MethodParam param: requiredFields) {
      constructor.addParameter(param.field.getType(), param.field.getName());
      constructor.addStatement("this.$L = $L", param.field.getName(), param.field.getName());
    }
    return constructor.build();
  }

  private MethodSpec withMethod(ClassName builderName, Field field) {
    return MethodSpec.methodBuilder("with" + firstCharacterToUpperCase(field.getName()))
          .addModifiers(Modifier.PUBLIC)
          .returns(builderName)
          .addParameter(field.getGenericType(), field.getName())
          .addStatement("this.$L = $L", field.getName(), field.getName())
          .addStatement("return this")
          .build();
  }

  private MethodSpec buildMethod(ClassName className, String ctorArgs) {
    MethodSpec.Builder buildMethod = MethodSpec.methodBuilder("build");
    buildMethod.addModifiers(Modifier.PUBLIC);
    buildMethod.returns(className);
    buildMethod.addStatement("return new $T($L)", className, ctorArgs.substring(2));
    return buildMethod.build();
  }

  private String firstCharacterToUpperCase(String str) {
    char c = Character.toUpperCase(str.charAt(0));
    return c + str.substring(1);
  }
}

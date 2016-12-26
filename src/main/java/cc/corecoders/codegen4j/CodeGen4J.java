package cc.corecoders.codegen4j;


import cc.corecoders.codegen4j.generator.Builder;
import cc.corecoders.codegen4j.generator.BuilderObserver;
import com.squareup.javapoet.JavaFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class CodeGen4J {

  private final String namespace;
  private final String generationPath;
  private final JarFile jarFile;
  private final URLClassLoader loader;

  public static void main(String[] args) throws IOException, ClassNotFoundException {
    if (args.length != 3) {
      System.out.println("usage : cmd <source.jar> <filtered.namespace> <generation/folder> ");
      return;
    }

    CodeGen4J codeGen4J = new CodeGen4J(args[0], args[1].replace('.', '/'), args[2]);
    codeGen4J.parseJar();
  }


  private CodeGen4J(String jarPath, String namespace, String generationPath) throws IOException, ClassNotFoundException {
    this.jarFile = new JarFile(jarPath);
    this.namespace = namespace;
    this.generationPath = generationPath;
    URL jarUrl = new URL("jar", "file://", jarPath + "!/");
    this.loader = new URLClassLoader(new URL[]{jarUrl});
  }

  private void parseJar() throws ClassNotFoundException, IOException {
    Enumeration<JarEntry> entries = jarFile.entries();
    while(entries.hasMoreElements()) {
      JarEntry entry = entries.nextElement();
      String classPath = entry.getName();
      if (!entry.isDirectory() && classPath.startsWith(namespace) && classPath.endsWith(".class")) {
        String className = classPath.substring(0, classPath.length() - 6).replace('/', '.');
        parseClass(loader.loadClass(className));
      }
    }
  }

  private void parseClass(Class<?> clazz) throws IOException {
    // remplacer le Diff generator par un BuilderObserver dont une implementation peut etre un diff... ou autre
    BuilderObserver diff = new BuilderObserver(clazz);
    generateFile(diff.generate());
    Builder builder = new Builder(clazz);
    generateFile(builder.generate());
  }

  private void generateFile(List<JavaFile> javaFiles) throws IOException {
    File generationSourceFile = new File(generationPath);
    for(JavaFile javaFile: javaFiles)
      javaFile.writeTo(generationSourceFile);
  }

}

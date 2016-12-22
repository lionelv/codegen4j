package cc.corecoders.codegen4j;


import com.squareup.javapoet.JavaFile;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class CodeGen4J {

  private String namespace;
  private JarFile jarFile;
  private URLClassLoader loader;

  public static void main(String[] args) throws IOException, ClassNotFoundException {
    if (args.length != 2) {
      System.out.println("usage : cmd <file.jar> <path/to/package>");
      return;
    }

    CodeGen4J codeGen4J = new CodeGen4J(args[0], args[1]);
    codeGen4J.parseJar();
  }


  private CodeGen4J(String jarPath, String namespace) throws IOException, ClassNotFoundException {
    this.jarFile = new JarFile(jarPath);
    this.namespace = namespace;
    URL jarUrl = new URL("jar", "file://", jarPath + "!/");
    this.loader = new URLClassLoader(new URL[]{jarUrl});
  }

  private void parseJar() throws ClassNotFoundException {
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
    BuilderGenerator builder = new BuilderGenerator(clazz);
    builders.addAll(builder.generate());
    return builders;
  }

}

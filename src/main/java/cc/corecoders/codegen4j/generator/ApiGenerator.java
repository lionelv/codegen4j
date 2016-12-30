package cc.corecoders.codegen4j.generator;

import cc.corecoders.codegen4j.annotation.ApiClass;
import cc.corecoders.codegen4j.annotation.ApiProperty;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ApiGenerator extends AbstractGenerator {
  static final String BeanSuffix = "Bean";
  static final String UnmodifiablePrefix = "Unmodifiable";
  static final String ImmutablePrefix = "Immutable";
  static final String BuilderSufix= "Builder";


  final ClassName name;

  private ClassName interfaceName;

  List<Property> properties;

  public ApiGenerator(Class<?> clazz) {
    super(clazz);
    this.name = ClassName.get(clazz.getPackage().getName(), className(clazz.getSimpleName()));
    this.properties = new ArrayList<>();
  }

  public ClassName getInterfaceName() {
    return interfaceName;
  }

  public void setInterfaceName(ClassName interfaceName) {
    this.interfaceName = interfaceName;
  }


  @Override
  public List<JavaFile> generate() {
    List<JavaFile> javaFiles = new ArrayList<>();
    ApiClass classAnnotation = clazz.getAnnotation(ApiClass.class);
    if(classAnnotation == null)
      return javaFiles;

    List<Api> generators = new ArrayList<>();
    generators.add(new Interface(this));
    if(classAnnotation.bean())
      generators.add(new Bean(this));
    if(classAnnotation.unmodifiable())
      generators.add(new Unmodifiable(this));
    if(classAnnotation.immutable())
      generators.add(new Immutable(this));

    for(Field field: clazz.getDeclaredFields()) {
      ApiProperty fieldAnnotation = field.getAnnotation(ApiProperty.class);
      if(fieldAnnotation == null)
        continue;
      properties.add(new Property(fieldAnnotation, field));
    }
    properties.sort(Comparator.comparingInt(p -> p.annotation.order()));

    for(Api generator: generators) {
      for(Property p: properties)
        generator.addProperty(p);
      generator.addProperties(properties);

      JavaFile.Builder fileBuilder = JavaFile.builder(generator.className.packageName(), generator.builder().build());
      fileBuilder.addFileComment("Generated file, any modification can be lost...");
      javaFiles.add(fileBuilder.build());
    }

    return javaFiles;
  }

}

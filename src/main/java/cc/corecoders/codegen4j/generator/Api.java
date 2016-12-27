package cc.corecoders.codegen4j.generator;

import cc.corecoders.codegen4j.annotation.ApiClass;
import cc.corecoders.codegen4j.annotation.ApiProperty;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Api extends AbstractGenerator {
  private final ClassName apiName;
  private List<Property> properties;

  public Api(Class<?> clazz) {
    super(clazz);
    this.apiName = ClassName.get(clazz.getPackage().getName(), className(clazz.getSimpleName()));
    this.properties = new ArrayList<>();
  }

  @Override
  public List<JavaFile> generate() {
    List<JavaFile> javaFiles = new ArrayList<>();
    ApiClass classAnnotation = clazz.getAnnotation(ApiClass.class);
    if(classAnnotation == null)
      return javaFiles;

    List<AbstractGenerator> generators = new ArrayList<>();
    generators.add(new Interface(clazz, apiName));
    if(!classAnnotation.bean().isEmpty())
      generators.add(new Bean(clazz, apiName, classAnnotation.bean()));
    if(!classAnnotation.unmodifiable().isEmpty())
      generators.add(new Unmodifiable(clazz, apiName, classAnnotation.unmodifiable()));
    if(!classAnnotation.bean().isEmpty())
      generators.add(new Bean(clazz, apiName, classAnnotation.bean()));

    for(Field field: clazz.getDeclaredFields()) {
      ApiProperty fieldAnnotation = field.getAnnotation(ApiProperty.class);
      if(fieldAnnotation == null)
        continue;
      properties.add(new Property(fieldAnnotation, field));
    }
    properties.sort(Comparator.comparingInt(p -> p.annotation.order()));

    for(AbstractGenerator generator: generators)
      javaFiles.addAll(generator.generate());

    return javaFiles;
  }

}

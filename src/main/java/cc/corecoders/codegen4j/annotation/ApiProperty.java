package cc.corecoders.codegen4j.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiProperty {

  /**
   * The order for code generation, making it more diff friendly
   * @return the ordinal of the property inside the class
   */
  int order();

  /**
   * Mainly used in the generated builder :
   * a required property will be a parameter of the builder constructor
   * the usual withXxxProperty won't be available on the builder
   * the builder.build() method will throw an exception if the field is null
   */
  boolean required() default false;

  /**
   * Mainly used in the generated builder :
   * the init value will be used in the builder constructor
   * String values must start and end with \"
   */
  String init() default "null";

  /**
   * Mainly used in the generated builder :
   * Properties belonging to the same group will be in the same withXxx() method
   * ex: having Bar b and Foo f in the same Dummies group,
   * the builder will provide a withDummies(Bar b, Foo f) method
   */
  String group() default "";
}

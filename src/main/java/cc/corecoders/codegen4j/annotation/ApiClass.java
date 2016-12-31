package cc.corecoders.codegen4j.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiClass {
  enum BuilderSpec {
    Standard,
    Observable,
    None
  }

  boolean implementation() default true;
  boolean immutable() default true;
  boolean bean() default true;
  boolean unmodifiable() default true;
  BuilderSpec builder() default BuilderSpec.Standard;
}

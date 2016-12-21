package cc.corecoders.codegen4j.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BuilderFieldSpec {
  int order(); // for builder constructor retro-compatibility
  boolean immutable() default false;
  String init() default "null";
  boolean required() default false;
  boolean audit() default true;
}

package cc.corecoders.codegen4j.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiClass {
  String implementation() default "Impl";
  String immutable() default "Immutable";
  String bean() default "Bean";
  String unmodifiable() default "Unmodifiable";
  String builder() default "Builder";
}

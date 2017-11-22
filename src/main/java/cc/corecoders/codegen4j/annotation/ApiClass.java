package cc.corecoders.codegen4j.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Generates an interface cleaned of any annotation used on the specification
 * Several implementations of this interface are generated according to the parameters below
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiClass {
  enum BuilderSpec {
    Standard,
    Observable,
    None
  }

  /**
   * Generates an immutable implementation of the generated interface
   * A copy constructor is provided
   * List, Set and Map rely on their respective google immutable collections
   * The attributes known as immutable are set by reference, others use the generated copy constructor
   *
   * @see cc.corecoders.codegen4j.generator.Generators#immutableTypes
   *
   * Developers may prefer the reference based unmodifiable implementation depending on their needs
   */
  boolean immutable() default true;

  /**
   * Generates a typical bean implementation of the generated interface
   * private attributes
   * default empty constructor
   * getters from the interface
   * and additionnal setters
   */
  boolean bean() default true;

  /**
   * Generates an unmidifable implementation of the interface
   * Regardless of the implementation provided to the constructor, this implemntation hides its setters
   *
   * Developers may prefer the copy based immutable implementation depending on their needs
   */
  boolean unmodifiable() default true;

  /**
   * Produces a builder according with the fields annotated with ApiProperty
   *
   * The Standard Builder
   * has a constructor with all mandatory attributes
   * provides a withXxx(Xxx x) method for every optional attributes
   * has a build() method returning a bean implementation
   *
   * The Observer Builder
   * is still under construction, the intent is to have notification when the builder witnesses differences
   *
   * @see ApiProperty for code generation customization
   */
  BuilderSpec builder() default BuilderSpec.Standard;
}

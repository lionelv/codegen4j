package cc.corecoders.codegen4j.generator;

import cc.corecoders.codegen4j.annotation.ApiProperty;

import java.lang.reflect.Field;

public class Property {
  ApiProperty annotation;
  Field field;

  public Property(ApiProperty annotation, Field field) {
    this.annotation = annotation;
    this.field = field;
  }

  public String initialization() {
    if(field.getType() == String.class)
      return "\"" + annotation.init() + "\"";

    return annotation.init();
  }
}

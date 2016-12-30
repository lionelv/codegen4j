package cc.corecoders.codegen4j.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;

import java.util.List;

abstract class Api {
  final ApiGenerator api;
  final ClassName className;

  Api(ApiGenerator api, ClassName className) {
    this.api = api;
    this.className = className;
  }

  ClassName getClassName() {
    return className;
  }

  abstract TypeSpec.Builder builder();

  void addProperty(Property property) {
  }

  void addProperties(List<Property> properties) {
  }
}

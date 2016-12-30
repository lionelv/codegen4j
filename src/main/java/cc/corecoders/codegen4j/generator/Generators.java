package cc.corecoders.codegen4j.generator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class Generators {

  static final List<String> immutableTypes = Arrays.asList(
      boolean.class.getName(),
      int.class.getName(),
      long.class.getName(),
      Boolean.class.getName(),
      Integer.class.getName(),
      Long.class.getName(),
      String.class.getName(),
      LocalDate.class.getName(),
      LocalDateTime.class.getName()
  );

  static String mCase(String str) {
    char c = Character.toUpperCase(str.charAt(0));
    return c + str.substring(1);
  }

}

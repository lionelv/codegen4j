// Generated file, any modification can be lost...
package cc.corecoders.codegen4j.test;

import java.lang.Long;
import java.lang.String;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class Sample01Unmodifiable implements Sample01 {
  private final Sample01 source;

  Sample01Unmodifiable(Sample01 source) {
    this.source = source;
  }

  public int getId() {
    return source.getId();
  }

  public Long getUuid() {
    return source.getUuid();
  }

  public String getName() {
    return source.getName();
  }

  public LocalDate getDatestamp() {
    return source.getDatestamp();
  }

  public LocalDateTime getTimestamp() {
    return source.getTimestamp();
  }

  public List<String> getList() {
    return Collections.unmodifiableList(source.getList());
  }

  public Map<String, String> getMap() {
    return Collections.unmodifiableMap(source.getMap());
  }
}

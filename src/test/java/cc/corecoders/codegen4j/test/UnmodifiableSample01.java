// Generated file, any modification can be lost...
package cc.corecoders.codegen4j.test;

import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class UnmodifiableSample01 implements Sample01 {
  private final Sample01 source;

  UnmodifiableSample01(Sample01 source) {
    this.source = source;
  }

  @Override
  public int getId() {
    return source.getId();
  }

  @Override
  public Long getUuid() {
    return source.getUuid();
  }

  @Override
  public String getName() {
    return source.getName();
  }

  @Override
  public String getMnemo() {
    return source.getMnemo();
  }

  @Override
  public LocalDate getDatestamp() {
    return source.getDatestamp();
  }

  @Override
  public LocalDateTime getListTimestamp() {
    return source.getListTimestamp();
  }

  @Override
  public List<String> getList() {
    return Collections.unmodifiableList(source.getList());
  }

  @Override
  public Map<String, String> getMap() {
    return Collections.unmodifiableMap(source.getMap());
  }
}

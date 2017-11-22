// Generated file, any modification can be lost...
package cc.corecoders.codegen4j.test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

class ImmutableSample01 implements Sample01 {
  private final int id;

  private final Long uuid;

  private final String name;

  private final String mnemo;

  private final LocalDate datestamp;

  private final LocalDateTime listTimestamp;

  private final List<String> list;

  private final Map<String, String> map;

  ImmutableSample01(Sample01 source) {
    this.id = source.getId();
    this.uuid = source.getUuid();
    this.name = source.getName();
    this.mnemo = source.getMnemo();
    this.datestamp = source.getDatestamp();
    this.listTimestamp = source.getListTimestamp();
    this.list = ImmutableList.copyOf(source.getList());
    this.map = ImmutableMap.copyOf(source.getMap());
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public Long getUuid() {
    return uuid;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getMnemo() {
    return mnemo;
  }

  @Override
  public LocalDate getDatestamp() {
    return datestamp;
  }

  @Override
  public LocalDateTime getListTimestamp() {
    return listTimestamp;
  }

  @Override
  public List<String> getList() {
    return list;
  }

  @Override
  public Map<String, String> getMap() {
    return map;
  }
}

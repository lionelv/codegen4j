// Generated file, any modification can be lost...
package cc.corecoders.codegen4j.test;

import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

class Sample01Bean implements Sample01 {
  private int id;

  private Long uuid;

  private String name;

  private LocalDate datestamp;

  private LocalDateTime timestamp;

  private List<String> list;

  private Map<String, String> map;

  Sample01Bean() {
  }

  @Override
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Override
  public Long getUuid() {
    return uuid;
  }

  public void setUuid(Long uuid) {
    this.uuid = uuid;
  }

  @Override
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public LocalDate getDatestamp() {
    return datestamp;
  }

  public void setDatestamp(LocalDate datestamp) {
    this.datestamp = datestamp;
  }

  @Override
  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public List<String> getList() {
    return list;
  }

  public void setList(List<String> list) {
    this.list = list;
  }

  @Override
  public Map<String, String> getMap() {
    return map;
  }

  public void setMap(Map<String, String> map) {
    this.map = map;
  }
}

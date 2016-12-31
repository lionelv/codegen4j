package cc.corecoders.codegen4j.test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class Sample02 {

  private int id;
  private Long uuid;
  private String name;
  private LocalDate datestamp;
  private LocalDateTime timestamp;
  private List<String> list;
  private Map<String, String> map;

  public Sample02(int id, Long uuid, String name, LocalDate datestamp, LocalDateTime timestamp, List<String> list, Map<String, String> map) {
    this.id = id;
    this.uuid = uuid;
    this.name = name;
    this.datestamp = datestamp;
    this.timestamp = timestamp;
    this.list = list;
    this.map = map;
  }

  public int getId() {
    return id;
  }

  public Long getUuid() {
    return uuid;
  }

  public String getName() {
    return name;
  }

  public LocalDate getDatestamp() {
    return datestamp;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public List<String> getList() {
    return list;
  }

  public Map<String, String> getMap() {
    return map;
  }

  public String method() {
    return null;
  }
}

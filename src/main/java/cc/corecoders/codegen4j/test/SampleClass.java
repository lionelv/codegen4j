package cc.corecoders.codegen4j.test;

import cc.corecoders.codegen4j.annotation.BuilderFieldSpec;
import cc.corecoders.codegen4j.annotation.BuilderClassGeneration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@BuilderClassGeneration
public class SampleClass {

  @BuilderFieldSpec(order = 0, immutable = true, init = "0")
  private int id;
  @BuilderFieldSpec(order = 1, immutable = true)
  private Long uuid;
  @BuilderFieldSpec(order = 2, required = true)
  private String name;
  @BuilderFieldSpec(order = 3)
  private LocalDate datestamp;
  @BuilderFieldSpec(order = 4)
  private LocalDateTime timestamp;
  @BuilderFieldSpec(order = 5)
  private List<String> list;
  @BuilderFieldSpec(order = 6)
  private Map<String, String> map;

  public SampleClass(int id, Long uuid, String name, LocalDate datestamp, LocalDateTime timestamp, List<String> list, Map<String, String> map) {
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

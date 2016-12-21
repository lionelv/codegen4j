package cc.corecoders.codegen4j.snippet;

import cc.corecoders.codegen4j.test.SampleClass;

import java.lang.Long;
import java.lang.String;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

class SampleClassBuilder {
  private int id;

  private Long uuid;

  private String name;

  private LocalDate datestamp;

  private LocalDateTime timestamp;

  private List<String> list;

  private Map<String, String> map;

  SampleClassBuilder(String name) {
    this.name = name;
  }

  public SampleClassBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public SampleClassBuilder withDatestamp(LocalDate datestamp) {
    this.datestamp = datestamp;
    return this;
  }

  public SampleClassBuilder withTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  public SampleClassBuilder withList(List<String> list) {
    this.list = list;
    return this;
  }

  public SampleClassBuilder withMap(Map<String, String> map) {
    this.map = map;
    return this;
  }

  public SampleClass build() {
    return new SampleClass(id, uuid, name, datestamp, timestamp, list, map);
  }
}

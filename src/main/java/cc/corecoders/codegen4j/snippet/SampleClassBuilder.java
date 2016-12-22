package cc.corecoders.codegen4j.snippet;

import cc.corecoders.codegen4j.test.SampleClass;

import java.lang.Long;
import java.lang.String;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

class SampleClassBuilder {
  private SampleClass that;

  private int id;

  private boolean idChange;

  private Long uuid;

  private boolean uuidChange;

  private String name;

  private boolean nameChange;

  private LocalDate datestamp;

  private boolean datestampChange;

  private LocalDateTime timestamp;

  private boolean timestampChange;

  private List<String> list;

  private boolean listChange;

  private Map<String, String> map;

  private boolean mapChange;

  SampleClassBuilder(SampleClass that) {
    this.that = that;
  }

  SampleClassBuilder(String name) {
    this.id = 0;
    this.uuid = null;
    this.name = name;
    this.datestamp = null;
    this.timestamp = null;
    this.list = null;
    this.map = null;
  }

  public SampleClassBuilder withName(String name) {
    this.name = name;
    this.nameChange = !Objects.equals(that.getName(), name);
    return this;
  }

  public SampleClassBuilder withDatestamp(LocalDate datestamp) {
    this.datestamp = datestamp;
    this.datestampChange = !Objects.equals(that.getDatestamp(), datestamp);
    return this;
  }

  public SampleClassBuilder withTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
    this.timestampChange = !Objects.equals(that.getTimestamp(), timestamp);
    return this;
  }

  public SampleClassBuilder withList(List<String> list) {
    this.list = list;
    this.listChange = !Objects.equals(that.getList(), list);
    return this;
  }

  public SampleClassBuilder withMap(Map<String, String> map) {
    this.map = map;
    this.mapChange = !Objects.equals(that.getMap(), map);
    return this;
  }

  public SampleClass build() {
    return new SampleClass(id, uuid, name, datestamp, timestamp, list, map);
  }
}

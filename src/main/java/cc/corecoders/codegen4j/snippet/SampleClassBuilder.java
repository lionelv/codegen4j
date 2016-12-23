package cc.corecoders.codegen4j.snippet;

import cc.corecoders.codegen4j.test.SampleClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class SampleClassBuilder {
  private Optional<SampleClassDiff> diff;

  private int id;

  private Long uuid;

  private String name;

  private LocalDate datestamp;

  private LocalDateTime timestamp;

  private List<String> list;

  private Map<String, String> map;

  SampleClassBuilder(SampleClassDiff diff) {
    this.diff = Optional.of(diff);
    this.id = diff.getRef().getId();
    this.uuid = diff.getRef().getUuid();
    this.name = diff.getRef().getName();
    this.datestamp = diff.getRef().getDatestamp();
    this.timestamp = diff.getRef().getTimestamp();
    this.list = diff.getRef().getList();
    this.map = diff.getRef().getMap();
  }

  SampleClassBuilder(String name) {
    this.diff = Optional.empty();
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
    this.diff.ifPresent(d -> d.diffName(name));
    return this;
  }

  public SampleClassBuilder withDatestamp(LocalDate datestamp) {
    this.datestamp = datestamp;
    this.diff.ifPresent(d -> d.diffDatestamp(datestamp));
    return this;
  }

  public SampleClassBuilder withTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
    this.diff.ifPresent(d -> d.diffTimestamp(timestamp));
    return this;
  }

  public SampleClassBuilder withList(List<String> list) {
    this.list = list;
    this.diff.ifPresent(d -> d.diffList(list));
    return this;
  }

  public SampleClassBuilder withMap(Map<String, String> map) {
    this.map = map;
    this.diff.ifPresent(d -> d.diffMap(map));
    return this;
  }

  public SampleClass build() {
    return new SampleClass(id, uuid, name, datestamp, timestamp, list, map);
  }
}

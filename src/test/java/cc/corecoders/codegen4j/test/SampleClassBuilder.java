// Generated file, any modification can be lost...
package cc.corecoders.codegen4j.test;

import java.lang.Long;
import java.lang.String;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class SampleClassBuilder {
  private Optional<SampleClassObserver> observer;

  private int id;

  private Long uuid;

  private String name;

  private LocalDate datestamp;

  private LocalDateTime timestamp;

  private List<String> list;

  private Map<String, String> map;

  SampleClassBuilder(SampleClassObserver observer) {
    this.observer = Optional.of(observer);
    this.id = observer.getReference().getId();
    this.uuid = observer.getReference().getUuid();
    this.name = observer.getReference().getName();
    this.datestamp = observer.getReference().getDatestamp();
    this.timestamp = observer.getReference().getTimestamp();
    this.list = observer.getReference().getList();
    this.map = observer.getReference().getMap();
  }

  SampleClassBuilder(String name) {
    this.observer = Optional.empty();
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
    this.observer.ifPresent(d -> d.notifyName(name));
    return this;
  }

  public SampleClassBuilder withDatestamp(LocalDate datestamp) {
    this.datestamp = datestamp;
    this.observer.ifPresent(d -> d.notifyDatestamp(datestamp));
    return this;
  }

  public SampleClassBuilder withTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
    this.observer.ifPresent(d -> d.notifyTimestamp(timestamp));
    return this;
  }

  public SampleClassBuilder withList(List<String> list) {
    this.list = list;
    this.observer.ifPresent(d -> d.notifyList(list));
    return this;
  }

  public SampleClassBuilder withMap(Map<String, String> map) {
    this.map = map;
    this.observer.ifPresent(d -> d.notifyMap(map));
    return this;
  }

  public SampleClass build() {
    return new SampleClass(id, uuid, name, datestamp, timestamp, list, map);
  }
}

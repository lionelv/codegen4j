// Generated file, any modification can be lost...
package cc.corecoders.codegen4j.test;

import java.lang.IllegalArgumentException;
import java.lang.Long;
import java.lang.String;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class Sample01Builder {
  private Optional<Sample01BuilderObserver> observer;

  private int id;

  private Long uuid;

  private String name;

  private LocalDate datestamp;

  private LocalDateTime listTimestamp;

  private List<String> list;

  private Map<String, String> map;

  Sample01Builder(String name) {
    this.observer = Optional.empty();
    this.id = 0;
    this.uuid = null;
    this.name = name;
    this.datestamp = null;
    this.listTimestamp = null;
    this.list = null;
    this.map = null;
  }

  Sample01Builder(Sample01BuilderObserver observer) {
    this.observer = Optional.of(observer);
    this.id = observer.getReference().getId();
    this.uuid = observer.getReference().getUuid();
    this.name = observer.getReference().getName();
    this.datestamp = observer.getReference().getDatestamp();
    this.listTimestamp = observer.getReference().getListTimestamp();
    this.list = observer.getReference().getList();
    this.map = observer.getReference().getMap();
  }

  public Sample01Builder withUuid(Long uuid) {
    this.uuid = uuid;
    this.observer.ifPresent(d -> d.notifyUuid(uuid));
    return this;
  }

  public Sample01Builder withDatestamp(LocalDate datestamp) {
    this.datestamp = datestamp;
    this.observer.ifPresent(d -> d.notifyDatestamp(datestamp));
    return this;
  }

  public Sample01Builder withTheList(LocalDateTime listTimestamp, List<String> list) {
    this.listTimestamp = listTimestamp;
    this.observer.ifPresent(d -> d.notifyListTimestamp(listTimestamp));
    this.list = list;
    this.observer.ifPresent(d -> d.notifyList(list));
    return this;
  }

  public Sample01Builder withId(int id) {
    this.id = id;
    this.observer.ifPresent(d -> d.notifyId(id));
    return this;
  }

  public Sample01Builder withMap(Map<String, String> map) {
    this.map = map;
    this.observer.ifPresent(d -> d.notifyMap(map));
    return this;
  }

  public Sample01Builder withName(String name) {
    this.name = name;
    this.observer.ifPresent(d -> d.notifyName(name));
    return this;
  }

  public Sample01 build() {
    Sample01Bean bean = new Sample01Bean();
    bean.setId(id);
    bean.setUuid(uuid);
    if(name == null) throw new IllegalArgumentException("Required field missing (name)");
    bean.setName(name);
    bean.setDatestamp(datestamp);
    bean.setListTimestamp(listTimestamp);
    bean.setList(list);
    bean.setMap(map);
    return bean;
  }
}

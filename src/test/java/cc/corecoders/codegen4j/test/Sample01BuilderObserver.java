// Generated file, any modification can be lost...
package cc.corecoders.codegen4j.test;

import java.lang.Long;
import java.lang.String;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

class Sample01BuilderObserver {
  private final Sample01 reference;

  private boolean id = true;

  private boolean uuid = true;

  private boolean name = true;

  private boolean datestamp = true;

  private boolean timestamp = true;

  private boolean list = true;

  private boolean map = true;

  Sample01BuilderObserver(Sample01 reference) {
    this.reference = reference;
  }

  Sample01 getReference() {
    return this.reference;
  }

  public boolean equalsId() {
    return this.id;
  }

  public boolean notifyId(int id) {
    this.id = Objects.equals(reference.getId(), id);
    return this.id;
  }

  public boolean equalsUuid() {
    return this.uuid;
  }

  public boolean notifyUuid(Long uuid) {
    this.uuid = Objects.equals(reference.getUuid(), uuid);
    return this.uuid;
  }

  public boolean equalsName() {
    return this.name;
  }

  public boolean notifyName(String name) {
    this.name = Objects.equals(reference.getName(), name);
    return this.name;
  }

  public boolean equalsDatestamp() {
    return this.datestamp;
  }

  public boolean notifyDatestamp(LocalDate datestamp) {
    this.datestamp = Objects.equals(reference.getDatestamp(), datestamp);
    return this.datestamp;
  }

  public boolean equalsTimestamp() {
    return this.timestamp;
  }

  public boolean notifyTimestamp(LocalDateTime timestamp) {
    this.timestamp = Objects.equals(reference.getTimestamp(), timestamp);
    return this.timestamp;
  }

  public boolean equalsList() {
    return this.list;
  }

  public boolean notifyList(List<String> list) {
    this.list = Objects.equals(reference.getList(), list);
    return this.list;
  }

  public boolean equalsMap() {
    return this.map;
  }

  public boolean notifyMap(Map<String, String> map) {
    this.map = Objects.equals(reference.getMap(), map);
    return this.map;
  }

  public boolean equals() {
    return id && uuid && name && datestamp && timestamp && list && map;
  }
}

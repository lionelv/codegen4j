// Generated file, any modification can be lost...
package cc.corecoders.codegen4j.test;

import java.lang.Long;
import java.lang.String;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

class SampleClassObserver {
  private final SampleClass Reference;

  private boolean id = true;;

  private boolean uuid = true;;

  private boolean name = true;;

  private boolean datestamp = true;;

  private boolean timestamp = true;;

  private boolean list = true;;

  private boolean map = true;;

  SampleClassObserver(SampleClass Reference) {
    this.Reference = Reference;
  }

  public SampleClass getReference() {
    return this.Reference;
  }

  public boolean equalsId() {
    return this.id;
  }

  public boolean notifyId(int id) {
    this.id = Objects.equals(Reference.getId(), id);
    return this.id;
  }

  public boolean equalsUuid() {
    return this.uuid;
  }

  public boolean notifyUuid(Long uuid) {
    this.uuid = Objects.equals(Reference.getUuid(), uuid);
    return this.uuid;
  }

  public boolean equalsName() {
    return this.name;
  }

  public boolean notifyName(String name) {
    this.name = Objects.equals(Reference.getName(), name);
    return this.name;
  }

  public boolean equalsDatestamp() {
    return this.datestamp;
  }

  public boolean notifyDatestamp(LocalDate datestamp) {
    this.datestamp = Objects.equals(Reference.getDatestamp(), datestamp);
    return this.datestamp;
  }

  public boolean equalsTimestamp() {
    return this.timestamp;
  }

  public boolean notifyTimestamp(LocalDateTime timestamp) {
    this.timestamp = Objects.equals(Reference.getTimestamp(), timestamp);
    return this.timestamp;
  }

  public boolean equalsList() {
    return this.list;
  }

  public boolean notifyList(List<String> list) {
    this.list = Objects.equals(Reference.getList(), list);
    return this.list;
  }

  public boolean equalsMap() {
    return this.map;
  }

  public boolean notifyMap(Map<String, String> map) {
    this.map = Objects.equals(Reference.getMap(), map);
    return this.map;
  }

  public boolean equals() {
    return id && uuid && name && datestamp && timestamp && list && map;
  }
}

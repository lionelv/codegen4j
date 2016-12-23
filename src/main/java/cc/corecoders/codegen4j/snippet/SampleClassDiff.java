package cc.corecoders.codegen4j.snippet;

import cc.corecoders.codegen4j.test.SampleClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

class SampleClassDiff {
  private SampleClass ref;

  private boolean id;

  private boolean uuid;

  private boolean name;

  private boolean datestamp;

  private boolean timestamp;

  private boolean list;

  private boolean map;

  SampleClassDiff(SampleClass ref) {
    this.ref = ref;
  }

  public SampleClass getRef() {
    return this.ref;
  }

  public boolean equalsId() {
    return !this.id;
  }

  public boolean diffId(int id) {
    this.id = !Objects.equals(ref.getId(), id);
    return this.id;
  }

  public boolean equalsUuid() {
    return !this.uuid;
  }

  public boolean diffUuid(Long uuid) {
    this.uuid = !Objects.equals(ref.getUuid(), uuid);
    return this.uuid;
  }

  public boolean equalsName() {
    return !this.name;
  }

  public boolean diffName(String name) {
    this.name = !Objects.equals(ref.getName(), name);
    return this.name;
  }

  public boolean equalsDatestamp() {
    return !this.datestamp;
  }

  public boolean diffDatestamp(LocalDate datestamp) {
    this.datestamp = !Objects.equals(ref.getDatestamp(), datestamp);
    return this.datestamp;
  }

  public boolean equalsTimestamp() {
    return !this.timestamp;
  }

  public boolean diffTimestamp(LocalDateTime timestamp) {
    this.timestamp = !Objects.equals(ref.getTimestamp(), timestamp);
    return this.timestamp;
  }

  public boolean equalsList() {
    return !this.list;
  }

  public boolean diffList(List<String> list) {
    this.list = !Objects.equals(ref.getList(), list);
    return this.list;
  }

  public boolean equalsMap() {
    return !this.map;
  }

  public boolean diffMap(Map<String, String> map) {
    this.map = !Objects.equals(ref.getMap(), map);
    return this.map;
  }

  public boolean diff() {
    return id || uuid || name || datestamp || timestamp || list || map;
  }

  public boolean equals() {
    return !diff();
  }
}

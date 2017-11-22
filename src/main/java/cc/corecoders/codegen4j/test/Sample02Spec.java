package cc.corecoders.codegen4j.test;

import cc.corecoders.codegen4j.annotation.ApiClass;
import cc.corecoders.codegen4j.annotation.ApiProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@ApiClass(builder = ApiClass.BuilderSpec.Standard)
public class Sample02Spec {
  private final String listGroup = "TheList";

  @ApiProperty(order = 1, init = "0")
  private int id;

  @ApiProperty(order = 2)
  private Long uuid;

  @ApiProperty(order = 3, required = true)
  private String name;

  @ApiProperty(order = 4, init = "\"none\"")
  private String mnemo;

  @ApiProperty(order = 5)
  private LocalDate datestamp;

  @ApiProperty(order = 6, group = listGroup)
  private LocalDateTime listTimestamp;

  @ApiProperty(order = 7, group = listGroup)
  private List<String> list;

  @ApiProperty(order = 8)
  private Map<String, String> map;

}

package cc.corecoders.codegen4j.test;

import cc.corecoders.codegen4j.annotation.ApiClass;
import cc.corecoders.codegen4j.annotation.ApiProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@ApiClass
public class Sample01Spec {

  @ApiProperty(order = 1)
  private int id;

  @ApiProperty(order = 2)
  private Long uuid;

  @ApiProperty(order = 3)
  private String name;

  @ApiProperty(order = 4)
  private LocalDate datestamp;

  @ApiProperty(order = 5)
  private LocalDateTime timestamp;

  @ApiProperty(order = 6)
  private List<String> list;

  @ApiProperty(order = 7)
  private Map<String, String> map;

}

package cc.corecoders.codegen4j.test;

import cc.corecoders.codegen4j.annotation.ApiClass;
import cc.corecoders.codegen4j.annotation.ApiProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;



@ApiClass(builder = ApiClass.BuilderSpec.None)
public class Sample03Spec {
  @ApiProperty(order = 1)
  private Sample01Spec sample;

  @ApiProperty(order = 2)
  private List<Sample01Spec> sampleList;


}

package com.felpeto.random.generator.entity;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Property {

  private UUID uuid;
  private PropertyKind propertyKind;
  private String country;
  private String state;
  private String city;
  private String zipcode;
  private String streetName;
  private Integer houseNumber;
  private String complement;
  private String tenant;

}

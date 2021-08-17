package com.bluntsoftware.qovery.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person {
  @Id
  String id;
  String firstName;
  String lastName;
  Integer age;
}

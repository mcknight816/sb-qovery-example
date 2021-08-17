package com.bluntsoftware.qovery.model;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Scope;

@Scope("test")
class PersonTest {

  @Test
  void shouldCreatePerson(){
    EasyRandom generator = new EasyRandom();
    Assertions.assertNotNull(generator.nextObject(Person.class));
  }

  @Test
  void shouldBuildPerson(){
    Assertions.assertNotNull(Person.builder().build());
  }
}

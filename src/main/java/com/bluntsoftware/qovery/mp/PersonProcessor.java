package com.bluntsoftware.qovery.mp;

import com.bluntsoftware.qovery.model.Person;
import com.bluntsoftware.qovery.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;


@Slf4j
@Component
public class PersonProcessor {
  private final ObjectMapper mapper;
  private final PersonService service;

  public PersonProcessor(PersonService service) {
    this.service = service;
    this.mapper = new ObjectMapper();
  }

  @Bean
  public Function<Map<String,Object>, Person> processPerson() {
    return map -> {
      log.info("processing: {}", map);
      return service.save(mapper.convertValue(map,Person.class));
    };
  }

}


package com.bluntsoftware.qovery.controller;

import com.bluntsoftware.qovery.model.Person;
import com.bluntsoftware.qovery.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/rest")
@CrossOrigin(origins = "http://localhost:4200")
public class PersonController {

  private final PersonService service;

  public PersonController(PersonService service) {
    this.service = service;
  }

  @PostMapping(value="/person",produces = MediaType.APPLICATION_JSON_VALUE)
  public Person save(@RequestBody Map<String,Object> dto){
    var mapper = new ObjectMapper();
    return this.service.save(mapper.convertValue(dto,Person.class));
  }

  @GetMapping(value = "/person/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
  public Optional<Person> findById(@PathVariable("id") String id ){
    return this.service.findById(String.valueOf(id));
  }

  @GetMapping(value = "/person",produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Person> findAll(){
    return this.service.findAll();
  }

  @DeleteMapping(value = "/person/{id}")
  public void deleteById(@PathVariable("id") String id ){
   this.service.deleteById(String.valueOf(id));
  }
}

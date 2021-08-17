package com.bluntsoftware.qovery.service;

import com.bluntsoftware.qovery.model.Person;
import com.bluntsoftware.qovery.repository.PersonRepo;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PersonService{

  private final PersonRepo repo;

  public PersonService(PersonRepo repo) {
    this.repo = repo;
  }

  public  Person save(Person item) {
    return repo.save(item);
  }

  public void deleteById(String id) {
      repo.deleteById(id);
  }

  public Optional<Person> findById(String id) {
    return repo.findById(id);
  }

  public Iterable<Person> findAll() {
    return repo.findAll();
  }
}

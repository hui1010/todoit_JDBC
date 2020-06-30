package se.lexicon.data.people;

import se.lexicon.model.Person;

import java.util.Collection;

public interface People {
    Person create (Person person);
    Collection<Person> findAll();
    Person findById(int personId);
    Collection<Person> findByName(String name);
    Person update(Person person);
    boolean deleteById(int personId);
}

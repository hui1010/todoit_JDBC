package se.lexicon.model;

import java.util.Objects;

public class Person {
    private int person_id;
    private String first_name;
    private String last_name;

    public Person(int id, String firstName, String lastName) {
        this.person_id = id;
        this.first_name = firstName;
        this.last_name = lastName;
    }

    public Person(String firstName, String lastName) {
        this(0, firstName, lastName);
    }

    public int getPerson_id() {
        return person_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return person_id == person.person_id &&
                Objects.equals(first_name, person.first_name) &&
                Objects.equals(last_name, person.last_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(person_id, first_name, last_name);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + person_id +
                ", firstName='" + first_name + '\'' +
                ", lastName='" + last_name + '\'' +
                '}';
    }
}

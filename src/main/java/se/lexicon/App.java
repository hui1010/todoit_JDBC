package se.lexicon;

import se.lexicon.data.MyDataSource;
import se.lexicon.data.people.PeopleRepository;
import se.lexicon.data.todoItems.TodoItemsRepository;
import se.lexicon.model.Person;
import se.lexicon.model.Todo;


import java.sql.SQLException;
import java.time.LocalDate;


public class App {

    public static void main( String[] args ) throws SQLException {
        PeopleRepository pr = new PeopleRepository();
        pr.create(new Person("Huiyi", "Skarner"));
        //pr.create(new Person(1, "Niklas", "Skarner"));//IllegalArgumentException: use update instead
        pr.create(new Person("Niklas", "Skarner"));
        pr.create(new Person("Malin", "Skåner"));
        pr.create(new Person("Anna", "Carlsson"));
        pr.create(new Person("Emil", "Carlsson"));

        pr.deleteById(3);//works well
        //System.out.println(pr.findByName("Huiyi"));
        //System.out.println(pr.findById(7)); //start before of result set
        //System.out.println(pr.findAll());



        TodoItemsRepository tr = new TodoItemsRepository();
        tr.create(new Todo("cleaning", "clean the classroom", LocalDate.parse("2020-02-01"), false, 5));
        tr.create(new Todo("shopping", "buy milk", LocalDate.parse("2020-02-01"), false, 10));
        System.out.println(tr.findById(13));
    }
}

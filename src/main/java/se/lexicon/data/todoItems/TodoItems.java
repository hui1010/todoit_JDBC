package se.lexicon.data.todoItems;

import se.lexicon.model.Person;
import se.lexicon.model.Todo;

import java.util.Collection;

public interface TodoItems {
    Todo create (Todo todo);
    Collection<Todo> findAll();
    Todo findById(int todoId);
    Collection<Todo> findByDoneStatus(boolean status);
    Collection<Todo> findByAssignee(int assigneeId);
    Collection<Todo> findByAssignee(Person person);
    Collection<Todo> findByUnassignedTodoItems();
    boolean deleteById(int todoId);
}

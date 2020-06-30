package se.lexicon.model;

import java.time.LocalDate;
import java.util.Objects;

public class Todo {
    private int todo_id;
    private String title;
    private String description;
    private LocalDate deadline;
    private boolean done;
    private int assignee_id;

    public Todo(int todo_id, String title, String description, LocalDate deadline, boolean done, int assignee_id) {
        this.todo_id = todo_id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.done = done;
        this.assignee_id = assignee_id;
    }

    public Todo(String title, String description, LocalDate deadline, boolean done, int assignee_id) {
        this(0, title, description, deadline, done, assignee_id);
    }

    public Todo(String title, String description, LocalDate deadline, boolean done) {
        this(0, title, description, deadline, done, 0);
    }

    public Todo(String title, String description, LocalDate deadline, int assignee_id) {
        this(0, title, description, deadline, true, assignee_id);
    }

    public int getTodo_id() {
        return todo_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getAssignee_id() {
        return assignee_id;
    }

    public void setAssignee_id(int assignee_id) {
        this.assignee_id = assignee_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return todo_id == todo.todo_id &&
                done == todo.done &&
                assignee_id == todo.assignee_id &&
                Objects.equals(title, todo.title) &&
                Objects.equals(description, todo.description) &&
                Objects.equals(deadline, todo.deadline);
    }

    @Override
    public int hashCode() {
        return Objects.hash(todo_id, title, description, deadline, done, assignee_id);
    }

    @Override
    public String toString() {
        return "Todo{" +
                "todo_id=" + todo_id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", deadline=" + deadline +
                ", done=" + done +
                ", assignee_id=" + assignee_id +
                '}';
    }
}

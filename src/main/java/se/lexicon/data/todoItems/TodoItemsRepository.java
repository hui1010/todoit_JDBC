package se.lexicon.data.todoItems;

import se.lexicon.data.MyDataSource;
import se.lexicon.model.Person;
import se.lexicon.model.Todo;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class TodoItemsRepository implements TodoItems {

    private Todo createTodoFromResultSet(ResultSet resultSet) throws SQLException {
        return new Todo(
                resultSet.getInt("todo_id"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                resultSet.getDate("deadline").toLocalDate(),
                // resultSet.getObject("deadline", LocalDate.class), also works
                resultSet.getBoolean("done"),
                resultSet.getInt("assignee_id")
        );
    }

    public static final String CREATE_TODO = "INSERT INTO todo_item (title, description, deadline, done, assignee_id) " +
            "VALUES (?,?,?,?,?)";
    @Override
    public Todo create(Todo todo) {
        if (todo.getTodo_id() != 0)
            throw new IllegalArgumentException("use update instead");
        Todo persisted = null;
        ResultSet keySet = null;
        try(Connection connection = MyDataSource.getCollection();
            PreparedStatement statement = connection.prepareStatement(CREATE_TODO, Statement.RETURN_GENERATED_KEYS);
            ){
            statement.setString(1, todo.getTitle());
            statement.setString(2, todo.getDescription());
            statement.setObject(3, todo.getDeadline());
            statement.setBoolean(4, todo.isDone());
            statement.setInt(5, todo.getAssignee_id());

            statement.executeUpdate();
            keySet = statement.getGeneratedKeys();
            while(keySet.next()){
                persisted = new Todo(
                        keySet.getInt(1),
                        todo.getTitle(),
                        todo.getDescription(),
                        todo.getDeadline(),
                        todo.isDone(),
                        todo.getAssignee_id()
                );
            }

        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if (keySet != null){
                    keySet.close();
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return persisted == null ? todo : persisted ;
    }

    @Override
    public Collection<Todo> findAll() {
        Collection<Todo> result = new ArrayList<>();
        try(Connection connection = MyDataSource.getCollection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM person");
            ResultSet resultSet = statement.executeQuery()){

            while(resultSet.next()){
                result.add(createTodoFromResultSet(resultSet));
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public static final String FIND_BY_ID = "SELECT * FROM todo_item WHERE todo_id = ?";
    @Override
    public Todo findById(int todoId) {
        Todo target = null;
        try(Connection connection = MyDataSource.getCollection();
            PreparedStatement statement = createFindByIdStatement(connection, FIND_BY_ID, todoId);
            ResultSet resultSet = statement.executeQuery()){
            while(resultSet.next()){
                target = createTodoFromResultSet(resultSet);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return target;
    }

    private PreparedStatement createFindByIdStatement(Connection connection, String findById, int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(findById);
        statement.setInt(1, id);
        return statement;
    }

    public static final String FIND_BY_DONE_STATUS = "SELECT * FROM todo_item WHERE done = ?";
    @Override
    public Collection<Todo> findByDoneStatus(boolean status) {
        Collection<Todo> result = new ArrayList<>();
        try(Connection connection = MyDataSource.getCollection();
            PreparedStatement statement = createFindByDoneStatusStatement(connection, FIND_BY_DONE_STATUS, status);
            ResultSet resultSet = statement.executeQuery()){

            while(resultSet.next()){
                result.add(createTodoFromResultSet(resultSet));
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    private PreparedStatement createFindByDoneStatusStatement(Connection connection, String findByDoneStatus, boolean status) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(findByDoneStatus);
        statement.setBoolean(1, status);
        return statement;
    }

    public static final String FIND_BY_ASSIGNEE = "SELECT * FROM todo_item WHERE assignee_id = ?";
    @Override
    public Collection<Todo> findByAssignee(int assigneeId) {
        Collection<Todo> result = new ArrayList<>();
        try(Connection connection = MyDataSource.getCollection();
            PreparedStatement statement = createFindByAssigneeStatement(connection, FIND_BY_ASSIGNEE, assigneeId);
            ResultSet resultSet = statement.executeQuery()){

            while(resultSet.next()){
                result.add(createTodoFromResultSet(resultSet));
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    private PreparedStatement createFindByAssigneeStatement(Connection connection, String findByAssignee, int assigneeId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(findByAssignee);
        statement.setInt(1,assigneeId);
        return statement;
    }

    @Override
    public Collection<Todo> findByAssignee(Person person) {
        Collection<Todo> result = new ArrayList<>();
        try(Connection connection = MyDataSource.getCollection();
            PreparedStatement statement = createFindByAssigneeStatement(connection, FIND_BY_ASSIGNEE, person);
            ResultSet resultSet = statement.executeQuery()){

            while(resultSet.next()){
                result.add(createTodoFromResultSet(resultSet));
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    private PreparedStatement createFindByAssigneeStatement(Connection connection, String findByAssignee, Person person) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(findByAssignee);
        statement.setInt(1, person.getPerson_id());
        return statement;
    }

    public static final String FIND_BY_UNASSIGNED_TODO_ITEMS = "SELECT * FROM todo_item WHERE assignee_id = 0";
    @Override
    public Collection<Todo> findByUnassignedTodoItems() {
        Collection<Todo> result = new ArrayList<>();
        try(Connection connection = MyDataSource.getCollection();
            PreparedStatement statement = connection.prepareStatement(FIND_BY_UNASSIGNED_TODO_ITEMS);
            ResultSet resultSet = statement.executeQuery()){

            while(resultSet.next()){
                result.add(createTodoFromResultSet(resultSet));
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public static final String DELETE_BY_ID = "DELETE FROM todo_item WHERE todo_id = ?";
    @Override
    public boolean deleteById(int todoId) {
        boolean delete = false;
        try(Connection connection = MyDataSource.getCollection();
            PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID)){

            statement.setInt(1, todoId);
            statement.executeUpdate();
            delete = true;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return delete;
    }

}

package se.lexicon.data.people;

import se.lexicon.data.MyDataSource;
import se.lexicon.model.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;


public class PeopleRepository implements People {

    private Person createPersonFromResultSet(ResultSet resultSet) throws SQLException {
        return new Person(
                resultSet.getInt("person_id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name")
        );
    }

    @Override
    public Person create(Person person) {
        if (person.getPerson_id() != 0)
            throw new IllegalArgumentException("use update instead");
        Person persisted = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet keySet = null;
        try{
            connection = MyDataSource.getCollection();
            statement = connection.prepareStatement(
                    "INSERT INTO person (first_name, last_name) VALUES (?,?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setString(1, person.getFirst_name());
            statement.setString(2, person.getLast_name());
            statement.executeUpdate();

            keySet = statement.getGeneratedKeys();

            while(keySet.next()){
                persisted = new Person(
                        keySet.getInt(1),
                        person.getFirst_name(),
                        person.getLast_name()
                );
            }

        }catch (SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if (keySet != null)
                    keySet.close();
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return persisted == null ? person : persisted;
    }

    @Override
    public Collection<Person> findAll() {
        Collection<Person> result = new ArrayList<>();
        try(Connection connection = MyDataSource.getCollection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM person");
            ResultSet resultSet = statement.executeQuery()) {

            while(resultSet.next()){
                result.add(createPersonFromResultSet(resultSet));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        return result;
    }


    private static final String FIND_BY_ID = "SELECT * FROM person WHERE person_id = ?";
    @Override
    public Person findById(int personId) {
        if (personId <= 0)
            throw new IllegalArgumentException("invalid id");

        Person target = null;
        try(Connection connection = MyDataSource.getCollection();
            PreparedStatement statement = createFindByIdStatement(connection, FIND_BY_ID, personId);
            ResultSet resultSet = statement.executeQuery()) {

            while(resultSet.next()){
                target = createPersonFromResultSet(resultSet);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return target;
    }

    private PreparedStatement createFindByIdStatement(Connection connection, String findById, int personId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(findById);
        statement.setInt(1, personId);
        return statement;
    }


    private static final String FIND_BY_NAME_LIKE = "SELECT * FROM person WHERE first_name LIKE ? " ;
    @Override
    public Collection<Person> findByName(String name) {
        Collection<Person> result = new ArrayList<>();
        try(Connection connection = MyDataSource.getCollection();
            PreparedStatement statement = createFindByNameStatement(connection, FIND_BY_NAME_LIKE, name);
            ResultSet resultSet = statement.executeQuery()){

            while(resultSet.next()){
                result.add(createPersonFromResultSet(resultSet));
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

        return result;
    }

    private PreparedStatement createFindByNameStatement(Connection connection, String findByName, String name) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(findByName);
        statement.setString(1,"%".concat(name).concat("%"));
        return statement;
    }

    private static final String UPDATE_PERSON = "UPDATE person SET first_name = ?, last_name = ? WHERE person_id = ?";
    @Override
    public Person update(Person person) {
        if (person.getPerson_id() == 0)
            throw new IllegalArgumentException("can't update it is not persisted yet, try create first");
        try(Connection connection = MyDataSource.getCollection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_PERSON)){

            statement.setString(1, person.getFirst_name());
            statement.setString(2, person.getLast_name());
            statement.setInt(3, person.getPerson_id());

            statement.execute();

        }catch(SQLException e){
            e.printStackTrace();
        }
        return person;
    }

    private static final String DELETE_PERSON = "DELETE FROM person WHERE person_id = ?";
    @Override
    public boolean deleteById(int personId) {
        boolean delete = false;
        try(Connection connection = MyDataSource.getCollection();
            PreparedStatement statement = connection.prepareStatement(DELETE_PERSON)){

            statement.setInt(1, personId);
            statement.executeUpdate();
            delete = true;
        }catch(SQLException e){
            e.printStackTrace();
        }

        return delete;
    }
}

package by.mironovich.application.crudapplication.dao;

import by.mironovich.application.crudapplication.models.Person;
import by.mironovich.application.crudapplication.models.exceptions.PersonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO {


    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() {
        return jdbcTemplate.query("SELECT * FROM people", new PersonMapper());
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO people(name, age, email) VALUES(?, ?, ?)", person.getName(), person.getAge(), person.getEmail());
    }

    public Person show(int id) throws PersonNotFoundException{
        List<Person> result = jdbcTemplate.query("SELECT * FROM people WHERE id = ?", new PersonMapper(), id);
        if (result.size() != 0) {
            return result.get(0);
        }
        throw new PersonNotFoundException("Person with such id doesn't exist");
        //return jdbcTemplate.query("SELECT * FROM people WHERE id=?", new PersonMapper(), id).stream().findAny().orElse(null);
    }

    public void update(int id, Person person) {
        jdbcTemplate.update("UPDATE people SET name = ?, age = ?, email = ? WHERE id = ?",
                person.getName(), person.getAge(), person.getEmail(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM people WHERE id = ?", id);
    }
}

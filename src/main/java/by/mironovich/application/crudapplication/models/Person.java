package by.mironovich.application.crudapplication.models;

import javax.validation.constraints.*;

public class Person {

    private int id;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name length should be greater than 1 and less than 31")
    private String name;

    @Positive(message = "age should be greater than 0")
    // Можно так же использовать @Min(value=0)
    private int age;

    @NotEmpty(message = "email should not be empty")
    @Email(message = "this should be a valid email")
    private String email;

    public Person(int id, String name, int age, String email) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
    }

    public Person() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

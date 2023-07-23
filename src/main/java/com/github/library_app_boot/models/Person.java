package com.github.library_app_boot.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.List;

@Entity
@Table(name = "person")
public class Person {
    @Id
    @Column(name = "person_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int personId;

    @NotEmpty(message = "Name should not be empty")
    @Pattern(regexp = "[А-Я][a-я]+\\s[А-Я][а-я]+\\s[А-Я][а-я]+",
            message = "The name is incorrect. Correct name format: Фамилия, Имя, Отчество")
    @Column(name = "full_name")
    private String fullName;

    @Min(value = 1920, message = "The age is too low!")
    @Max(value = 2023, message = "The age is too high!")
    @Column(name = "age_of_birth")
    private int ageOfBirth;

    @OneToMany(mappedBy = "owner")
    private List<Book> books;

    public Person() {

    }

    public Person(String fullName, int ageOfBirth) {
        this.fullName = fullName;
        this.ageOfBirth = ageOfBirth;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAgeOfBirth() {
        return ageOfBirth;
    }

    public void setAgeOfBirth(int ageOfBirth) {
        this.ageOfBirth = ageOfBirth;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}

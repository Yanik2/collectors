package org.example.user;

import java.util.Objects;

public class User {
  private String firstName;
  private String lastName;
  private int age;
  private int kids;

  public User(String firstName, String lastName, int age, int kids) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.age = age;
    this.kids = kids;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public int getKids() {
    return kids;
  }

  public void setKids(int kids) {
    this.kids = kids;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return age == user.age && kids == user.kids &&
        Objects.equals(firstName, user.firstName) &&
        Objects.equals(lastName, user.lastName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstName, lastName, age, kids);
  }
}

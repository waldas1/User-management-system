package com.company.Users;

import com.company.Role;

public class User {

    private String username;
    private String password;
    private Role role;
    private String name;
    private String surname;
    private int age;

    public User(String username, String password, Role role, String name, String surname, int  age) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = name;
        this.surname = surname;
        this.age= age;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getAge() {
        return age;
    }



}

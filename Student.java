package com.library;

public class Student extends User {

    public Student(String userId, String password, String name) {
        super(userId, password, name, "Student");
    }

    @Override
    public String getRoleWelcomeMessage() {
        return "Welcome to the library:): " + getName() + "！You can borrow books and view your history here.";
    }
}
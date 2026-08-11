public class Student extends User {

    public Student(String userId, String password, String name) {
        super(userId, password, name, "Student");
    }

    @Override
    public String getRoleWelcomeMessage() {
        return "Welcome to the library, " + getName() + "! You can search and borrow books here.";
    }

    @Override
    public boolean canBorrowBooks() {
        return true;
    }
}
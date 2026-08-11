public class Librarian extends User {

    public Librarian(String userId, String password, String name) {
        super(userId, password, name, "Librarian");
    }

    @Override
    public String getRoleWelcomeMessage() {
        return "Administrator login successful! Welcome " + getName() + ". You have unlocked the book entry and permission management functions.";
    }

    @Override
    public boolean canBorrowBooks() {
        return false;
    }
}
public class Book {
    private final String bookId;
    private String title;
    private String author;
    private boolean isBorrowed;

    public Book(String bookId, String title, String author) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isBorrowed = false;
    }

    public String getBookId() { return bookId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public boolean isBorrowed() { return isBorrowed; }
    public void setBorrowed(boolean borrowed) { isBorrowed = borrowed; }

    @Override
    public String toString() {
        return "[" + bookId + "] " + title + " - " + author + " | Status: " + (isBorrowed ? "Borrowed" : "Available");
    }
}
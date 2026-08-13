public class Book {
    private final String bookId;
    private String title;
    private String author;
    private boolean isBorrowed;
    private String borrowedBy;

    public Book(String bookId, String title, String author) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isBorrowed = false;
        this.borrowedBy = null;
    }

    public String getBookId() { return bookId; }

    public String getTitle() { return title; }
    public void setTitle(String title) {
        if (title != null && !title.trim().isEmpty()) {
            this.title = title;
        }
    }

    public String getAuthor() { return author; }
    public void setAuthor(String author) {
        if (author != null && !author.trim().isEmpty()) {
            this.author = author;
        }
    }

    public boolean isBorrowed() { return isBorrowed; }
    public void setBorrowed(boolean borrowed) { isBorrowed = borrowed; }

    public String getBorrowedBy() { return borrowedBy; }
    public void setBorrowedBy(String borrowedBy) { this.borrowedBy = borrowedBy; }

    @Override
    public String toString() {
        String status = isBorrowed ? "Borrowed by " + borrowedBy : "Available";
        return "[" + bookId + "] " + title + " - " + author + " | Status: " + status;
    }
}
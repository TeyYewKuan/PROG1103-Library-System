public class Book {
    private String bookId;
    private String title;
    private String author;
    private boolean isBorrowed;

    public Book(String bookId, String title, String author) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isBorrowed = false; // 默认新书未借出
    }

    // Getters and Setters (封装)
    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public boolean isBorrowed() { return isBorrowed; }
    public void setBorrowed(boolean borrowed) { isBorrowed = borrowed; }

    @Override
    public String toString() {
        return "[" + bookId + "] " + title + " - " + author + " | 状态: " + (isBorrowed ? "已被借出" : "可借阅");
    }
}

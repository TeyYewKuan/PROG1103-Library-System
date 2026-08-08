public class LibraryService implements Searchable {

    private Book[] bookList;
    private int bookCount;
    private Book[] lastSearchResult;

    public LibraryService() {
        bookList = new Book[10];
        bookCount = 0;
        lastSearchResult = new Book[0];

        addBook(new Book("B001", "Java Programming", "James Gosling"));
        addBook(new Book("B002", "Object-Oriented Design", "Robert Martin"));
        addBook(new Book("B003", "Data Structures & Algorithms", "Mark Allen"));
    }

    private void addBook(Book book) {
        if (bookCount < bookList.length) {
            bookList[bookCount] = book;
            bookCount++;
        }
    }

    @Override
    public void search(String keyword) {
        Book[] temp = new Book[bookCount];
        int count = 0;

        for (int i = 0; i < bookCount; i++) {
            Book book = bookList[i];
            if (book.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                book.getAuthor().toLowerCase().contains(keyword.toLowerCase()) ||
                book.getBookId().equalsIgnoreCase(keyword)) {
                temp[count] = book;
                count++;
            }
        }

        Book[] results = new Book[count];
        for (int i = 0; i < count; i++) {
            results[i] = temp[i];
        }
        lastSearchResult = results;
    }

    public Book[] searchBooks(String keyword) {
        search(keyword);
        return lastSearchResult;
    }

    public String borrowBook(String bookId) {
        for (int i = 0; i < bookCount; i++) {
            Book book = bookList[i];
            if (book.getBookId().equalsIgnoreCase(bookId)) {
                if (book.isBorrowed()) {
                    return "Failed: The book \"" + book.getTitle() + "\" is already borrowed.";
                } else {
                    book.setBorrowed(true);
                    return "Success: You have borrowed \"" + book.getTitle() + "\".";
                }
            }
        }
        return "Error: No book found with ID " + bookId + ".";
    }

    public String returnBook(String bookId) {
        for (int i = 0; i < bookCount; i++) {
            Book book = bookList[i];
            if (book.getBookId().equalsIgnoreCase(bookId)) {
                if (!book.isBorrowed()) {
                    return "Notice: The book \"" + book.getTitle() + "\" was not borrowed.";
                } else {
                    book.setBorrowed(false);
                    return "Success: The book \"" + book.getTitle() + "\" has been returned.";
                }
            }
        }
        return "Error: No book found with ID " + bookId + ".";
    }

    public Book[] getAllBooks() {
        Book[] result = new Book[bookCount];
        for (int i = 0; i < bookCount; i++) {
            result[i] = bookList[i];
        }
        return result;
    }
}
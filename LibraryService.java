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

    public String addNewBook(String bookId, String title, String author) {
        if (bookId.isEmpty() || title.isEmpty() || author.isEmpty()) {
            return "Error: All fields must be filled.";
        }

        if (bookCount >= bookList.length) {
            return "Error: Book list is full.";
        }

        for (int i = 0; i < bookCount; i++) {
            if (bookList[i].getBookId().equalsIgnoreCase(bookId)) {
                return "Error: Book ID \"" + bookId + "\" already exists.";
            }
        }

        addBook(new Book(bookId, title, author));
        return "Success: \"" + title + "\" has been added to the library.";
    }

    public String updateBook(String bookId, String newTitle, String newAuthor) {
        if (bookId == null || bookId.isEmpty()) {
            return "Error: Please select a book to edit.";
        }
        if (newTitle.isEmpty() || newAuthor.isEmpty()) {
            return "Error: Title and author cannot be empty.";
        }

        for (int i = 0; i < bookCount; i++) {
            if (bookList[i].getBookId().equalsIgnoreCase(bookId)) {
                bookList[i].setTitle(newTitle);
                bookList[i].setAuthor(newAuthor);
                return "Success: Book \"" + bookId + "\" has been updated.";
            }
        }
        return "Error: No book found with ID " + bookId + ".";
    }

    public String deleteBookById(String bookId) {
        if (bookId == null || bookId.isEmpty()) {
            return "Error: Please select a book to delete.";
        }

        int indexToRemove = -1;
        for (int i = 0; i < bookCount; i++) {
            if (bookList[i].getBookId().equalsIgnoreCase(bookId)) {

                if (bookList[i].isBorrowed()) {
                    return "Error: Cannot delete \"" + bookList[i].getTitle() + "\" - it is currently borrowed.";
                }

                indexToRemove = i;
                break;
            }
        }

        if (indexToRemove == -1) {
            return "Error: No book found with ID " + bookId + ".";
        }

        String removedTitle = bookList[indexToRemove].getTitle();

        for (int i = indexToRemove; i < bookCount - 1; i++) {
            bookList[i] = bookList[i + 1];
        }
        bookList[bookCount - 1] = null;
        bookCount--;

        return "Success: \"" + removedTitle + "\" has been removed from the library.";
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
        Searchable searchable = this;
        searchable.search(keyword);
        return lastSearchResult;
    }

    public String borrowBook(String bookId, String borrowerName) {
        for (int i = 0; i < bookCount; i++) {
            Book book = bookList[i];
            if (book.getBookId().equalsIgnoreCase(bookId)) {
                if (book.isBorrowed()) {
                    return "Failed: The book \"" + book.getTitle() + "\" is already borrowed.";
                } else {
                    book.setBorrowed(true);
                    book.setBorrowedBy(borrowerName);
                    return "Success: You have borrowed \"" + book.getTitle() + "\".";
                }
            }
        }
        return "Error: No book found with ID " + bookId + ".";
    }

    public String returnBook(String bookId, String requesterName) {
        for (int i = 0; i < bookCount; i++) {
            Book book = bookList[i];
            if (book.getBookId().equalsIgnoreCase(bookId)) {
                if (!book.isBorrowed()) {
                    return "Notice: The book \"" + book.getTitle() + "\" was not borrowed.";
                } else if (!book.getBorrowedBy().equalsIgnoreCase(requesterName)) {
                    return "Error: This book was borrowed by " + book.getBorrowedBy() + ", you cannot return it.";
                } else {
                    book.setBorrowed(false);
                    book.setBorrowedBy(null);
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
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CoreFeatureFormFX {

    private final LibraryService libraryService = new LibraryService();
    private final ListView<String> bookListView = new ListView<>();
    private final ComboBox<String> bookIdComboBox = new ComboBox<>();
    private final Label statusLabel = new Label();

    public VBox getCoreFeatureView() {
        Label headerLabel = new Label("Book Search and Borrowing");
        headerLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));

        TextField searchField = new TextField();
        searchField.setPromptText("Enter title / author / book ID to search...");
        searchField.setPrefWidth(260);

        Button searchBtn = new Button("Search");
        Button showAllBtn = new Button("Show All Books");

        HBox searchBox = new HBox(10);
        searchBox.setPadding(new Insets(5, 0, 5, 0));
        searchBox.getChildren().addAll(searchField, searchBtn, showAllBtn);

        bookIdComboBox.setPromptText("Select Book ID");
        bookIdComboBox.setPrefWidth(160);
        refreshComboBox();

        Button borrowBtn = new Button("Borrow Book");
        Button returnBtn = new Button("Return Book");

        HBox actionBox = new HBox(10);
        actionBox.setPadding(new Insets(5, 0, 5, 0));
        actionBox.getChildren().addAll(new Label("Book ID:"), bookIdComboBox, borrowBtn, returnBtn);

        statusLabel.setStyle("-fx-font-weight: bold;");

        bookListView.setPrefHeight(150);

        searchBtn.setOnAction(e -> {
            String keyword = searchField.getText().trim();
            if (keyword.isEmpty()) {
                updateListView(libraryService.getAllBooks());
                statusLabel.setStyle("-fx-text-fill: orange;");
                statusLabel.setText("Showing all books.");
            } else {
                Book[] results = libraryService.searchBooks(keyword);
                updateListView(results);
                statusLabel.setStyle("-fx-text-fill: blue;");
                statusLabel.setText("Search complete. Found " + results.length + " result(s).");
            }
        });

        showAllBtn.setOnAction(e -> {
            searchField.clear();
            updateListView(libraryService.getAllBooks());
            statusLabel.setStyle("-fx-text-fill: black;");
            statusLabel.setText("Showing all books.");
        });

        borrowBtn.setOnAction(e -> {
            String bookId = bookIdComboBox.getValue();
            if (bookId == null || bookId.isEmpty()) {
                statusLabel.setStyle("-fx-text-fill: red;");
                statusLabel.setText("Please select a book ID to borrow.");
            } else {
                String resultMessage = libraryService.borrowBook(bookId);
                statusLabel.setStyle(resultMessage.startsWith("Success") ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
                statusLabel.setText(resultMessage);
                updateListView(libraryService.getAllBooks());
            }
        });

        returnBtn.setOnAction(e -> {
            String bookId = bookIdComboBox.getValue();
            if (bookId == null || bookId.isEmpty()) {
                statusLabel.setStyle("-fx-text-fill: red;");
                statusLabel.setText("Please select a book ID to return.");
            } else {
                String resultMessage = libraryService.returnBook(bookId);
                statusLabel.setStyle(resultMessage.startsWith("Success") ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
                statusLabel.setText(resultMessage);
                updateListView(libraryService.getAllBooks());
            }
        });

        updateListView(libraryService.getAllBooks());

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(
                headerLabel,
                searchBox,
                new Label("Book List and Status:"),
                bookListView,
                actionBox,
                statusLabel
        );

        return layout;
    }

    private void updateListView(Book[] books) {
        bookListView.getItems().clear();
        for (Book b : books) {
            bookListView.getItems().add(b.toString());
        }
    }

    private void refreshComboBox() {
        bookIdComboBox.getItems().clear();
        for (Book b : libraryService.getAllBooks()) {
            bookIdComboBox.getItems().add(b.getBookId());
        }
    }
}
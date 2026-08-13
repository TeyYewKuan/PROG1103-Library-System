import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CoreFeatureFormFX {

    private final LibraryService libraryService;
    private final ReportingService reportingService;
    private final String borrowerName;
    private final ListView<String> bookListView = new ListView<>();
    private final ComboBox<String> bookIdComboBox = new ComboBox<>();
    private final Label statusLabel = new Label();

    public CoreFeatureFormFX(LibraryService libraryService, ReportingService reportingService, String borrowerName) {
        this.libraryService = libraryService;
        this.reportingService = reportingService;
        this.borrowerName = borrowerName;
    }

    public VBox getCoreFeatureView() {
        Label headerLabel = new Label("Book Search and Borrowing");
        headerLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        headerLabel.setStyle("-fx-text-fill: " + MainAppFX.COLOR_TEXT + ";");

        Label headerCaption = new Label("Find a book and borrow or return it.");
        headerCaption.setStyle("-fx-text-fill: " + MainAppFX.COLOR_TEXT_SECONDARY + "; -fx-font-size: 12px;");

        TextField searchField = new TextField();
        searchField.setPromptText("Enter title / author / book ID to search...");
        searchField.setPrefWidth(260);
        searchField.setStyle(MainAppFX.INPUT_STYLE);

        Button searchBtn = new Button("Search");
        searchBtn.setStyle(MainAppFX.PRIMARY_BTN_STYLE);

        Button showAllBtn = new Button("Show All Books");
        showAllBtn.setStyle(MainAppFX.SECONDARY_BTN_STYLE);

        HBox searchBox = new HBox(10, searchField, searchBtn, showAllBtn);
        searchBox.setPadding(new Insets(14, 0, 5, 0));

        bookIdComboBox.setPromptText("Select Book ID");
        bookIdComboBox.setPrefWidth(160);
        bookIdComboBox.setStyle(MainAppFX.INPUT_STYLE);
        refreshComboBox();

        Button borrowBtn = new Button("Borrow Book");
        borrowBtn.setStyle(MainAppFX.PRIMARY_BTN_STYLE);

        Button returnBtn = new Button("Return Book");
        returnBtn.setStyle(MainAppFX.SECONDARY_BTN_STYLE);

        Label bookIdCaption = new Label("Book ID:");
        bookIdCaption.setStyle("-fx-text-fill: " + MainAppFX.COLOR_TEXT_SECONDARY + ";");

        HBox actionBox = new HBox(10, bookIdCaption, bookIdComboBox, borrowBtn, returnBtn);
        actionBox.setPadding(new Insets(5, 0, 5, 0));

        statusLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");

        bookListView.setPrefHeight(150);
        bookListView.setStyle(MainAppFX.INPUT_STYLE);

        bookListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.startsWith("[")) {
                String selectedId = newVal.substring(1, newVal.indexOf("]"));
                bookIdComboBox.setValue(selectedId);
            }
        });

        searchBtn.setOnAction(e -> {
            String keyword = searchField.getText().trim();
            if (keyword.isEmpty()) {
                updateListView(libraryService.getAllBooks());
                statusLabel.setStyle("-fx-text-fill: #B45309; -fx-font-weight: bold; -fx-font-size: 12px;");
                statusLabel.setText("Showing all books.");
            } else {
                Book[] results = libraryService.searchBooks(keyword);
                updateListView(results);
                statusLabel.setStyle("-fx-text-fill: " + MainAppFX.COLOR_PRIMARY + "; -fx-font-weight: bold; -fx-font-size: 12px;");
                statusLabel.setText("Search complete. Found " + results.length + " result(s).");
            }
        });

        showAllBtn.setOnAction(e -> {
            searchField.clear();
            updateListView(libraryService.getAllBooks());
            statusLabel.setStyle("-fx-text-fill: " + MainAppFX.COLOR_TEXT + "; -fx-font-weight: bold; -fx-font-size: 12px;");
            statusLabel.setText("Showing all books.");
        });

        borrowBtn.setOnAction(e -> {
            if (borrowerName == null || borrowerName.isEmpty()) {
                statusLabel.setStyle("-fx-text-fill: " + MainAppFX.COLOR_ERROR + "; -fx-font-weight: bold; -fx-font-size: 12px;");
                statusLabel.setText("Please login first before borrowing a book.");
                return;
            }

            String bookId = bookIdComboBox.getValue();
            if (bookId == null || bookId.isEmpty()) {
                statusLabel.setStyle("-fx-text-fill: " + MainAppFX.COLOR_ERROR + "; -fx-font-weight: bold; -fx-font-size: 12px;");
                statusLabel.setText("Please select a book ID to borrow.");
            } else {
                String resultMessage = libraryService.borrowBook(bookId, borrowerName);
                statusLabel.setStyle((resultMessage.startsWith("Success") ? "-fx-text-fill: " + MainAppFX.COLOR_SUCCESS : "-fx-text-fill: " + MainAppFX.COLOR_ERROR) + "; -fx-font-weight: bold; -fx-font-size: 12px;");
                statusLabel.setText(resultMessage);

                if (resultMessage.startsWith("Success")) {
                    String bookTitle = findBookTitle(bookId);
                    String newId = reportingService.generateTransactionId();
                    Transaction newTrans = new Transaction(newId, borrowerName, bookTitle, 0);
                    reportingService.addTransaction(newTrans);
                }

                updateListView(libraryService.getAllBooks());
            }
        });

        returnBtn.setOnAction(e -> {
            String bookId = bookIdComboBox.getValue();
            if (bookId == null || bookId.isEmpty()) {
                statusLabel.setStyle("-fx-text-fill: " + MainAppFX.COLOR_ERROR + "; -fx-font-weight: bold; -fx-font-size: 12px;");
                statusLabel.setText("Please select a book ID to return.");
            } else {
                String resultMessage = libraryService.returnBook(bookId, borrowerName);
                statusLabel.setStyle((resultMessage.startsWith("Success") ? "-fx-text-fill: " + MainAppFX.COLOR_SUCCESS : "-fx-text-fill: " + MainAppFX.COLOR_ERROR) + "; -fx-font-weight: bold; -fx-font-size: 12px;");
                statusLabel.setText(resultMessage);
                updateListView(libraryService.getAllBooks());
            }
        });

        updateListView(libraryService.getAllBooks());

        Label listCaption = new Label("Book List and Status");
        listCaption.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: " + MainAppFX.COLOR_TEXT + ";");

        VBox layout = new VBox(12);
        layout.setPadding(new Insets(24));
        layout.setStyle(MainAppFX.CARD_STYLE);
        layout.getChildren().addAll(
                headerLabel,
                headerCaption,
                searchBox,
                listCaption,
                bookListView,
                actionBox,
                statusLabel
        );

        return layout;
    }

    private String findBookTitle(String bookId) {
        for (Book b : libraryService.getAllBooks()) {
            if (b.getBookId().equalsIgnoreCase(bookId)) {
                return b.getTitle();
            }
        }
        return "Unknown Book";
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
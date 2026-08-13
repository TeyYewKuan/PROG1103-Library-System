import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class AddBookFormFX {

    private final LibraryService libraryService;
    private final ListView<String> bookListView = new ListView<>();
    private final ComboBox<String> editBookIdComboBox = new ComboBox<>();
    private final Label statusLabel = new Label();

    public AddBookFormFX(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    public VBox getAddBookView() {
        Label headerLabel = new Label("Add New Book");
        headerLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        headerLabel.setStyle("-fx-text-fill: " + MainAppFX.COLOR_TEXT + ";");

        Label headerCaption = new Label("Librarian only");
        headerCaption.setStyle("-fx-text-fill: " + MainAppFX.COLOR_TEXT_SECONDARY + "; -fx-font-size: 12px;");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(14, 0, 10, 0));

        Label idLabel = new Label("Book ID:");
        TextField idIn = new TextField();
        idIn.setPrefWidth(150);
        idIn.setStyle(MainAppFX.INPUT_STYLE);

        Label titleLabel = new Label("Book Title:");
        TextField titleIn = new TextField();
        titleIn.setPrefWidth(220);
        titleIn.setStyle(MainAppFX.INPUT_STYLE);

        Label authorLabel = new Label("Author:");
        TextField authorIn = new TextField();
        authorIn.setPrefWidth(220);
        authorIn.setStyle(MainAppFX.INPUT_STYLE);

        Button addBookBtn = new Button("Add Book");
        addBookBtn.setStyle(MainAppFX.PRIMARY_BTN_STYLE);

        form.add(idLabel, 0, 0);
        form.add(idIn, 1, 0);
        form.add(titleLabel, 0, 1);
        form.add(titleIn, 1, 1);
        form.add(authorLabel, 0, 2);
        form.add(authorIn, 1, 2);
        form.add(addBookBtn, 1, 3);

        statusLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
        bookListView.setPrefHeight(150);
        bookListView.setStyle(MainAppFX.INPUT_STYLE);
        refreshAll();

        addBookBtn.setOnAction(e -> {
            String bookId = idIn.getText().trim();
            String title = titleIn.getText().trim();
            String author = authorIn.getText().trim();

            String resultMessage = libraryService.addNewBook(bookId, title, author);
            statusLabel.setStyle((resultMessage.startsWith("Success") ? "-fx-text-fill: " + MainAppFX.COLOR_SUCCESS : "-fx-text-fill: " + MainAppFX.COLOR_ERROR) + "; -fx-font-weight: bold; -fx-font-size: 12px;");
            statusLabel.setText(resultMessage);

            if (resultMessage.startsWith("Success")) {
                idIn.clear();
                titleIn.clear();
                authorIn.clear();
                refreshAll();
            }
        });

        Label manageHeaderLabel = new Label("Edit or Delete Existing Book");
        manageHeaderLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        manageHeaderLabel.setStyle("-fx-text-fill: " + MainAppFX.COLOR_TEXT + ";");

        GridPane manageForm = new GridPane();
        manageForm.setHgap(10);
        manageForm.setVgap(10);
        manageForm.setPadding(new Insets(10, 0, 10, 0));

        Label selectLabel = new Label("Select Book ID:");
        editBookIdComboBox.setPromptText("Select Book ID");
        editBookIdComboBox.setPrefWidth(150);
        editBookIdComboBox.setStyle(MainAppFX.INPUT_STYLE);

        Label newTitleLabel = new Label("New Title:");
        TextField newTitleIn = new TextField();
        newTitleIn.setPrefWidth(220);
        newTitleIn.setStyle(MainAppFX.INPUT_STYLE);

        Label newAuthorLabel = new Label("New Author:");
        TextField newAuthorIn = new TextField();
        newAuthorIn.setPrefWidth(220);
        newAuthorIn.setStyle(MainAppFX.INPUT_STYLE);

        Button editBookBtn = new Button("Save Changes");
        editBookBtn.setStyle(MainAppFX.PRIMARY_BTN_STYLE);

        Button deleteBookBtn = new Button("Delete Book");
        deleteBookBtn.setStyle(MainAppFX.SECONDARY_BTN_STYLE);

        HBox manageBtnBox = new HBox(10, editBookBtn, deleteBookBtn);

        manageForm.add(selectLabel, 0, 0);
        manageForm.add(editBookIdComboBox, 1, 0);
        manageForm.add(newTitleLabel, 0, 1);
        manageForm.add(newTitleIn, 1, 1);
        manageForm.add(newAuthorLabel, 0, 2);
        manageForm.add(newAuthorIn, 1, 2);
        manageForm.add(manageBtnBox, 1, 3);

        editBookBtn.setOnAction(e -> {
            String bookId = editBookIdComboBox.getValue();
            String newTitle = newTitleIn.getText().trim();
            String newAuthor = newAuthorIn.getText().trim();

            String resultMessage = libraryService.updateBook(bookId, newTitle, newAuthor);
            statusLabel.setStyle((resultMessage.startsWith("Success") ? "-fx-text-fill: " + MainAppFX.COLOR_SUCCESS : "-fx-text-fill: " + MainAppFX.COLOR_ERROR) + "; -fx-font-weight: bold; -fx-font-size: 12px;");
            statusLabel.setText(resultMessage);

            if (resultMessage.startsWith("Success")) {
                newTitleIn.clear();
                newAuthorIn.clear();
                refreshAll();
            }
        });

        deleteBookBtn.setOnAction(e -> {
            String bookId = editBookIdComboBox.getValue();
            String resultMessage = libraryService.deleteBookById(bookId);
            statusLabel.setStyle((resultMessage.startsWith("Success") ? "-fx-text-fill: " + MainAppFX.COLOR_SUCCESS : "-fx-text-fill: " + MainAppFX.COLOR_ERROR) + "; -fx-font-weight: bold; -fx-font-size: 12px;");
            statusLabel.setText(resultMessage);

            if (resultMessage.startsWith("Success")) {
                refreshAll();
            }
        });

        Label listCaption = new Label("Current Book List");
        listCaption.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: " + MainAppFX.COLOR_TEXT + ";");

        VBox layout = new VBox(16);
        layout.setPadding(new Insets(24));
        layout.setStyle(MainAppFX.CARD_STYLE);
        layout.getChildren().addAll(
                headerLabel,
                headerCaption,
                form,
                manageHeaderLabel,
                manageForm,
                statusLabel,
                listCaption,
                bookListView
        );

        return layout;
    }

    private void refreshAll() {
        bookListView.getItems().clear();
        editBookIdComboBox.getItems().clear();
        for (Book b : libraryService.getAllBooks()) {
            bookListView.getItems().add(b.toString());
            editBookIdComboBox.getItems().add(b.getBookId());
        }
    }
}
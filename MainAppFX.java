import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainAppFX extends Application {

    private final ReportingService reportingService = new ReportingService();
    private final ListView<String> transactionListView = new ListView<>();
    private final Label totalFineLabel = new Label();
    private final Label formStatusLabel = new Label();
    private final BorderPane root = new BorderPane();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("PROG1103 Library Management System - Main Dashboard");

        VBox header = new VBox();
        header.setPadding(new Insets(15));
        header.setAlignment(Pos.CENTER);

        Label schoolLabel = new Label("Southern University College");
        schoolLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));

        Label systemLabel = new Label("Library System Dashboard");
        systemLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));

        header.getChildren().addAll(schoolLabel, systemLabel);

        Label navHeader = new Label("Navigation");
        navHeader.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));

        Button userModuleBtn = new Button("User Management");
        Button coreModuleBtn = new Button("Book Search & Borrowing");
        Button reportModuleBtn = new Button("Reporting & Fine");

        userModuleBtn.setPrefWidth(180);
        coreModuleBtn.setPrefWidth(180);
        reportModuleBtn.setPrefWidth(180);

        VBox navBox = new VBox(10);
        navBox.setPadding(new Insets(20));
        navBox.getChildren().addAll(navHeader, userModuleBtn, coreModuleBtn, reportModuleBtn);

        userModuleBtn.setOnAction(e -> root.setCenter(new LoginFormFX().getLoginView()));
        coreModuleBtn.setOnAction(e -> root.setCenter(new CoreFeatureFormFX().getCoreFeatureView()));
        reportModuleBtn.setOnAction(e -> root.setCenter(createReportingUI()));

        root.setTop(header);
        root.setLeft(navBox);
        root.setCenter(createReportingUI());

        Scene scene = new Scene(root, 750, 550);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createReportingUI() {
        Label titleLabel = new Label("Transaction History and Fine Calculator");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));

        refreshTransactionList();

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10, 0, 10, 0));

        Label nameLabel = new Label("Borrower Name:");
        TextField userIn = new TextField();
        userIn.setPrefWidth(220);

        Label bookLabel = new Label("Book Title:");
        TextField bookIn = new TextField();
        bookIn.setPrefWidth(220);

        Label daysLabel = new Label("Overdue Days:");
        TextField daysIn = new TextField();
        daysIn.setPrefWidth(100);

        Button addRecordBtn = new Button("Add Transaction");

        form.add(nameLabel, 0, 0);
        form.add(userIn, 1, 0);
        form.add(bookLabel, 0, 1);
        form.add(bookIn, 1, 1);
        form.add(daysLabel, 0, 2);
        form.add(daysIn, 1, 2);
        form.add(addRecordBtn, 1, 3);

        addRecordBtn.setOnAction(e -> {
            String uName = userIn.getText().trim();
            String bTitle = bookIn.getText().trim();
            String daysText = daysIn.getText().trim();

            if (uName.isEmpty() || bTitle.isEmpty() || daysText.isEmpty()) {
                formStatusLabel.setStyle("-fx-text-fill: red;");
                formStatusLabel.setText("Please fill in all fields.");
                return;
            }

            try {
                int days = Integer.parseInt(daysText);
                String newId = reportingService.generateTransactionId();
                Transaction newTrans = new Transaction(newId, uName, bTitle, days);
                reportingService.addTransaction(newTrans);

                refreshTransactionList();
                userIn.clear();
                bookIn.clear();
                daysIn.clear();
                formStatusLabel.setStyle("-fx-text-fill: green;");
                formStatusLabel.setText("Transaction added successfully.");
            } catch (NumberFormatException ex) {
                formStatusLabel.setStyle("-fx-text-fill: red;");
                formStatusLabel.setText("Overdue days must be a valid number.");
            }
        });

        totalFineLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        totalFineLabel.setStyle("-fx-text-fill: red;");
        updateTotalFine();

        VBox layout = new VBox(12, titleLabel, transactionListView, form, formStatusLabel, totalFineLabel);
        layout.setPadding(new Insets(20));
        return layout;
    }

    private void refreshTransactionList() {
        transactionListView.getItems().clear();
        for (Transaction t : reportingService.getAllTransactions()) {
            transactionListView.getItems().add(t.toString());
        }
        updateTotalFine();
    }

    private void updateTotalFine() {
        totalFineLabel.setText("Total Outstanding Fines: RM " + String.format("%.2f", reportingService.getTotalFinesCollected()));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
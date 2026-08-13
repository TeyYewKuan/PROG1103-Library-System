import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainAppFX extends Application {

    public static final String COLOR_BG = "#F7F8FA";
    public static final String COLOR_PRIMARY = "#2563EB";
    public static final String COLOR_PRIMARY_LIGHT = "#EFF4FF";
    public static final String COLOR_TEXT = "#111827";
    public static final String COLOR_TEXT_SECONDARY = "#6B7280";
    public static final String COLOR_BORDER = "#E5E7EB";
    public static final String COLOR_HEADER_BG = "#DCE6F5";
    public static final String COLOR_SUCCESS = "#15803D";
    public static final String COLOR_ERROR = "#DC2626";

    public static final String CARD_STYLE =
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-border-color: " + COLOR_BORDER + ";" +
            "-fx-border-width: 1;";

    public static final String PRIMARY_BTN_STYLE =
            "-fx-background-color: " + COLOR_PRIMARY + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 6;" +
            "-fx-padding: 8 22 8 22;" +
            "-fx-cursor: hand;";

    public static final String SECONDARY_BTN_STYLE =
            "-fx-background-color: #F3F4F6;" +
            "-fx-text-fill: " + COLOR_TEXT + ";" +
            "-fx-background-radius: 6;" +
            "-fx-padding: 8 22 8 22;" +
            "-fx-border-color: " + COLOR_BORDER + ";" +
            "-fx-border-radius: 6;" +
            "-fx-cursor: hand;";

    public static final String INPUT_STYLE =
            "-fx-background-color: #F9FAFB;" +
            "-fx-background-radius: 6;" +
            "-fx-border-color: " + COLOR_BORDER + ";" +
            "-fx-border-radius: 6;" +
            "-fx-padding: 8;";

    public static final String SECTION_LABEL_STYLE =
            "-fx-font-size: 12px; -fx-text-fill: " + COLOR_TEXT_SECONDARY + "; -fx-font-weight: bold;";

    private final ReportingService reportingService = new ReportingService();
    private final LibraryService libraryService = new LibraryService();
    private final ListView<String> transactionListView = new ListView<>();
    private final Label totalFineLabel = new Label();
    private final Label formStatusLabel = new Label();
    private final BorderPane root = new BorderPane();

    private User currentUser;
    private Label navStatusLabel;

    private Button dashboardBtn;
    private Button userModuleBtn;
    private Button coreModuleBtn;
    private Button reportModuleBtn;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("PROG1103 Library Management System");

        root.setStyle("-fx-background-color: " + COLOR_BG + ";");
        root.setTop(buildHeader());
        root.setLeft(buildSidebar());

        showDashboard();

        Scene scene = new Scene(root, 850, 600);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(750);
        primaryStage.setMinHeight(500);
        primaryStage.show();
    }

    private HBox buildHeader() {
        Label appTitle = new Label("Southern University College");
        appTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        appTitle.setStyle("-fx-text-fill: " + COLOR_TEXT + ";");

        Label appSubtitle = new Label("Library Management System");
        appSubtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
        appSubtitle.setStyle("-fx-text-fill: " + COLOR_TEXT_SECONDARY + ";");

        VBox titleBox = new VBox(2, appTitle, appSubtitle);

        HBox header = new HBox(titleBox);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(24, 24, 20, 24));
        header.setStyle("-fx-background-color: " + COLOR_HEADER_BG + "; -fx-border-color: " + COLOR_BORDER + "; -fx-border-width: 0 0 1 0;");
        return header;
    }

    private VBox buildSidebar() {
        Label libraryHeader = new Label("LIBRARY");
        libraryHeader.setStyle(SECTION_LABEL_STYLE);

        dashboardBtn = createNavButton("Dashboard");

        Label mgmtHeader = new Label("MANAGEMENT");
        mgmtHeader.setStyle(SECTION_LABEL_STYLE);

        userModuleBtn = createNavButton("User Management");
        coreModuleBtn = createNavButton("Book Search & Borrowing");

        Label reportsHeader = new Label("REPORTS");
        reportsHeader.setStyle(SECTION_LABEL_STYLE);

        reportModuleBtn = createNavButton("Reporting & Fine");

        navStatusLabel = new Label("Not logged in");
        navStatusLabel.setStyle("-fx-text-fill: " + COLOR_TEXT_SECONDARY + "; -fx-font-size: 11px;");
        navStatusLabel.setWrapText(true);
        navStatusLabel.setMaxWidth(180);

        VBox navBox = new VBox(6);
        navBox.setPadding(new Insets(20, 16, 20, 16));
        navBox.getChildren().addAll(
                libraryHeader, dashboardBtn,
                spacer(10),
                mgmtHeader, userModuleBtn, coreModuleBtn,
                spacer(10),
                reportsHeader, reportModuleBtn,
                spacer(20),
                navStatusLabel
        );
        navBox.setPrefWidth(210);
        navBox.setStyle("-fx-background-color: white; -fx-border-color: " + COLOR_BORDER + "; -fx-border-width: 0 1 0 0;");

        dashboardBtn.setOnAction(e -> showDashboard());

        userModuleBtn.setOnAction(e -> {
            setActiveNav(userModuleBtn);
            root.setCenter(wrapInPage(new LoginFormFX(root, libraryService, reportingService, this).getLoginView()));
        });

        coreModuleBtn.setOnAction(e -> {
            if (currentUser == null) {
                showLoginRequiredMessage();
                return;
            }
            setActiveNav(coreModuleBtn);
            root.setCenter(wrapInPage(new CoreFeatureFormFX(libraryService, reportingService, currentUser.getName()).getCoreFeatureView()));
        });

        reportModuleBtn.setOnAction(e -> {
            if (currentUser == null) {
                showLoginRequiredMessage();
                return;
            }
            setActiveNav(reportModuleBtn);
            root.setCenter(wrapInPage(createReportingUI()));
        });

        return navBox;
    }

    private VBox spacer(double height) {
        VBox v = new VBox();
        v.setPrefHeight(height);
        return v;
    }

    private Button createNavButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(180);
        btn.setAlignment(Pos.CENTER_LEFT);
        applyInactiveNavStyle(btn);
        return btn;
    }

    private void applyInactiveNavStyle(Button btn) {
        btn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: " + COLOR_TEXT + ";" +
                "-fx-font-size: 13px;" +
                "-fx-padding: 8 10 8 10;" +
                "-fx-background-radius: 6;" +
                "-fx-cursor: hand;"
        );
    }

    private void applyActiveNavStyle(Button btn) {
        btn.setStyle(
                "-fx-background-color: " + COLOR_PRIMARY_LIGHT + ";" +
                "-fx-text-fill: " + COLOR_PRIMARY + ";" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 13px;" +
                "-fx-padding: 8 10 8 10;" +
                "-fx-background-radius: 6;" +
                "-fx-cursor: hand;"
        );
    }

    private void setActiveNav(Button active) {
        for (Button b : new Button[]{dashboardBtn, userModuleBtn, coreModuleBtn, reportModuleBtn}) {
            if (b == active) {
                applyActiveNavStyle(b);
            } else {
                applyInactiveNavStyle(b);
            }
        }
    }

    private ScrollPane wrapInPage(VBox content) {
        return createScrollablePage(content);
    }

    public static ScrollPane createScrollablePage(VBox content) {
        VBox page = new VBox(content);
        page.setPadding(new Insets(24));
        page.setStyle("-fx-background-color: " + COLOR_BG + ";");

        ScrollPane scrollPane = new ScrollPane(page);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: " + COLOR_BG + "; -fx-background: " + COLOR_BG + ";");
        return scrollPane;
    }

    private void showDashboard() {
        setActiveNav(dashboardBtn);

        Label welcome = new Label("Welcome back");
        welcome.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        welcome.setStyle("-fx-text-fill: " + COLOR_TEXT + ";");

        Label subtitle = new Label("Manage books, borrowing, and fines from one place.");
        subtitle.setStyle("-fx-text-fill: " + COLOR_TEXT_SECONDARY + ";");

        HBox statRow = new HBox(16,
                buildStatCard("Total Books", String.valueOf(libraryService.getAllBooks().length)),
                buildStatCard("Transactions", String.valueOf(reportingService.getAllTransactions().length)),
                buildStatCard("Outstanding Fines", "RM " + String.format("%.2f", reportingService.getTotalOutstandingFines()))
        );

        VBox loginPrompt = new VBox(8,
                new Label("Not logged in yet? Click \"User Management\" on the left to sign in."));
        loginPrompt.setPadding(new Insets(16));
        loginPrompt.setStyle(CARD_STYLE);

        VBox content = new VBox(20, welcome, subtitle, statRow, loginPrompt);
        root.setCenter(wrapInPage(content));
    }

    private VBox buildStatCard(String label, String value) {
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        valueLabel.setStyle("-fx-text-fill: " + COLOR_PRIMARY + ";");

        Label captionLabel = new Label(label);
        captionLabel.setStyle("-fx-text-fill: " + COLOR_TEXT_SECONDARY + "; -fx-font-size: 12px;");

        VBox card = new VBox(6, valueLabel, captionLabel);
        card.setPadding(new Insets(18));
        card.setPrefWidth(180);
        card.setStyle(CARD_STYLE);
        return card;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        navStatusLabel.setStyle("-fx-text-fill: " + COLOR_SUCCESS + "; -fx-font-size: 11px; -fx-font-weight: bold;");
        navStatusLabel.setText("Logged in as: " + user.getName() + " (" + user.getRole() + ")");
    }

    private void showLoginRequiredMessage() {
        Label lockedLabel = new Label("Please login first (click \"User Management\") to access this module.");
        lockedLabel.setStyle("-fx-text-fill: " + COLOR_ERROR + "; -fx-font-weight: bold;");
        VBox lockedCard = new VBox(15, lockedLabel);
        lockedCard.setPadding(new Insets(20));
        lockedCard.setStyle(CARD_STYLE);
        root.setCenter(wrapInPage(lockedCard));
    }

    private VBox createReportingUI() {
        Label titleLabel = new Label("Transaction History and Fine Calculator");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        titleLabel.setStyle("-fx-text-fill: " + COLOR_TEXT + ";");

        refreshTransactionList();
        transactionListView.setStyle(INPUT_STYLE);

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(16, 0, 10, 0));

        Label nameLabel = new Label("Borrower Name:");
        TextField userIn = new TextField();
        userIn.setPrefWidth(220);
        userIn.setStyle(INPUT_STYLE);

        Label bookLabel = new Label("Book Title:");
        TextField bookIn = new TextField();
        bookIn.setPrefWidth(220);
        bookIn.setStyle(INPUT_STYLE);

        Label daysLabel = new Label("Overdue Days:");
        TextField daysIn = new TextField();
        daysIn.setPrefWidth(100);
        daysIn.setStyle(INPUT_STYLE);

        Button addRecordBtn = new Button("Add Transaction");
        addRecordBtn.setStyle(PRIMARY_BTN_STYLE);

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
                formStatusLabel.setStyle("-fx-text-fill: " + COLOR_ERROR + ";");
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
                formStatusLabel.setStyle("-fx-text-fill: " + COLOR_SUCCESS + ";");
                formStatusLabel.setText("Transaction added successfully.");
            } catch (NumberFormatException ex) {
                formStatusLabel.setStyle("-fx-text-fill: " + COLOR_ERROR + ";");
                formStatusLabel.setText("Overdue days must be a valid number.");
            }
        });

        totalFineLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        totalFineLabel.setStyle("-fx-text-fill: " + COLOR_ERROR + ";");
        updateTotalFine();

        VBox layout = new VBox(14, titleLabel, transactionListView, form, formStatusLabel, totalFineLabel);
        layout.setPadding(new Insets(20));
        layout.setStyle(CARD_STYLE);
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
        totalFineLabel.setText("Total Outstanding Fines: RM " + String.format("%.2f", reportingService.getTotalOutstandingFines()));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
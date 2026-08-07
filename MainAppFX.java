package com.library;

import java.time.LocalDate;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainAppFX extends Application {

    private ReportingService reportingService = new ReportingService();
    private TableView<Transaction> tableView = new TableView<>();
    private Label totalFineLabel = new Label();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("PROG1103 图书馆综合管理系统 - (Group Project Main Entrance)");

        // 顶栏标题
        Label header = new Label("南方大学学院 - 图书馆系统综合控制台 (Main Dashboard)");
        header.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        header.setPadding(new Insets(10));

        // 创建选项卡 (TabPane) 来整合全组的所有模块
        TabPane tabPane = new TabPane();

        // --- Tab 1: 组员 A 的功能预览/测试 (User Management) ---
        Tab tabUser = new Tab("1. 用户与权限管理 (组员 A)");
        tabUser.setClosable(false);
        VBox userBox = new VBox(10, new Label("👤 组员 A 模块：账号登录与角色响应测试"), new Button("点击前往登录测试 (LoginFormFX)"));
        userBox.setPadding(new Insets(20));
        tabUser.setContent(userBox);

        // --- Tab 2: 组员 B 的功能预览/测试 (Core Feature) ---
        Tab tabCore = new Tab("2. 图书检索与借还 (组员 B)");
        tabCore.setClosable(false);
        VBox coreBox = new VBox(10, new Label("📚 组员 B 模块：图书检索与状态追踪"), new Button("点击前往核心业务 (CoreFeatureFormFX)"));
        coreBox.setPadding(new Insets(20));
        tabCore.setContent(coreBox);

        // --- Tab 3: 组员 C 的核心功能 (Reporting & Payment) ---
        Tab tabReport = new Tab("3. 财务报表与罚款清算 (组员 C)");
        tabReport.setClosable(false);
        tabReport.setContent(createReportingUI());

        tabPane.getTabs().addAll(tabUser, tabCore, tabReport);

        BorderPane root = new BorderPane();
        root.setTop(header);
        root.setCenter(tabPane);

        Scene scene = new Scene(root, 680, 520);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // 组员 C 负责构建的表格与报表 UI
    private VBox createReportingUI() {
        Label titleLabel = new Label("📊 借阅记录明细与逾期罚款清算 (Fine Calculator)");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));

        // 1. 设置 TableView 的各列数据映射
        TableColumn<Transaction, String> colId = new TableColumn<>("交易 ID");
        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTransactionId()));

        TableColumn<Transaction, String> colUser = new TableColumn<>("借阅人");
        colUser.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUserName()));

        TableColumn<Transaction, String> colBook = new TableColumn<>("书名");
        colBook.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBookTitle()));

        TableColumn<Transaction, String> colDate = new TableColumn<>("借阅日期");
        colDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBorrowDate().toString()));

        TableColumn<Transaction, Number> colDays = new TableColumn<>("逾期天数");
        colDays.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getOverdueDays()));

        TableColumn<Transaction, Number> colFine = new TableColumn<>("罚款金额 (RM)");
        colFine.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getFineAmount()));

        tableView.getColumns().addAll(colId, colUser, colBook, colDate, colDays, colFine);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // 载入报表初始数据
        refreshTableData();

        // 2. 模拟计算新罚款操作区
        TextField userIn = new TextField(); userIn.setPromptText("读者姓名"); userIn.setPrefWidth(100);
        TextField bookIn = new TextField(); bookIn.setPromptText("图书名称"); bookIn.setPrefWidth(120);
        TextField daysIn = new TextField(); daysIn.setPromptText("逾期天数"); daysIn.setPrefWidth(80);

        Button addRecordBtn = new Button("➕ 生成账单并记录");

        addRecordBtn.setOnAction(e -> {
            try {
                String uName = userIn.getText().trim();
                String bTitle = bookIn.getText().trim();
                int days = Integer.parseInt(daysIn.getText().trim());

                if (!uName.isEmpty() && !bTitle.isEmpty()) {
                    String newId = "T" + (1000 + reportingService.getAllTransactions().size() + 1);
                    Transaction newTrans = new Transaction(newId, uName, bTitle, LocalDate.now(), days);
                    reportingService.addTransaction(newTrans);

                    refreshTableData(); // 刷新表格
                    userIn.clear(); bookIn.clear(); daysIn.clear();
                }
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "请输入有效的逾期天数数字！");
                alert.show();
            }
        });

        HBox inputForm = new HBox(10, userIn, bookIn, daysIn, addRecordBtn);
        inputForm.setPadding(new Insets(10, 0, 10, 0));

        // 3. 统计总输出
        totalFineLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        totalFineLabel.setStyle("-fx-text-fill: #d9534f;");
        updateTotalFine();

        VBox layout = new VBox(10, titleLabel, tableView, inputForm, totalFineLabel);
        layout.setPadding(new Insets(15));
        return layout;
    }

    private void refreshTableData() {
        tableView.getItems().clear();
        tableView.getItems().addAll(reportingService.getAllTransactions());
        updateTotalFine();
    }

    private void updateTotalFine() {
        totalFineLabel.setText("💰 系统当前预估累计待缴罚款总额: RM " + String.format("%.2f", reportingService.getTotalFinesCollected()));
    }

    // 全组的核心总启动钥匙
    public static void main(String[] args) {
        launch(args);
    }
}
package com.library;

import java.util.List;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class CoreFeatureFormFX extends Application {

    private LibraryService libraryService = new LibraryService();
    private ListView<String> bookListView = new ListView<>();
    private Label statusLabel = new Label();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Core Feature Module - 图书检索与借还系统");

        // 1. 标题
        Label headerLabel = new Label("📚 图书馆核心业务管理 (Core Feature)");
        headerLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));

        // 2. 检索区域
        TextField searchField = new TextField();
        searchField.setPromptText("输入书名 / 作者 / 图书ID 进行搜索...");
        searchField.setPrefWidth(260);

        Button searchBtn = new Button("🔍 搜索");
        Button showAllBtn = new Button("显示全部图书");

        HBox searchBox = new HBox(10);
        searchBox.getChildren().addAll(searchField, searchBtn, showAllBtn);

        // 3. 操作区域 (输入 ID 借书/还书)
        TextField idField = new TextField();
        idField.setPromptText("输入图书 ID (如 B001)");
        idField.setPrefWidth(160);

        Button borrowBtn = new Button("📖 借阅图书");
        Button returnBtn = new Button("↩️ 归还图书");

        HBox actionBox = new HBox(10);
        actionBox.getChildren().addAll(new Label("图书 ID:"), idField, borrowBtn, returnBtn);

        // 状态提示文字样式
        statusLabel.setStyle("-fx-font-weight: bold;");

        // --- 事件绑定 ---

        // 事件 1: 点击搜索按钮
        searchBtn.setOnAction(e -> {
            String keyword = searchField.getText().trim();
            if (keyword.isEmpty()) {
                updateListView(libraryService.getAllBooks());
                statusLabel.setStyle("-fx-text-fill: orange;");
                statusLabel.setText("提示: 显示所有图书。");
            } else {
                List<Book> results = libraryService.searchBooks(keyword);
                updateListView(results);
                statusLabel.setStyle("-fx-text-fill: blue;");
                statusLabel.setText("查询完毕，共找到 " + results.size() + " 条记录。");
            }
        });

        // 事件 2: 点击显示全部
        showAllBtn.setOnAction(e -> {
            searchField.clear();
            updateListView(libraryService.getAllBooks());
            statusLabel.setStyle("-fx-text-fill: black;");
            statusLabel.setText("已刷新，显示全部图书。");
        });

        // 事件 3: 点击借阅
        borrowBtn.setOnAction(e -> {
            String bookId = idField.getText().trim();
            if (bookId.isEmpty()) {
                statusLabel.setStyle("-fx-text-fill: red;");
                statusLabel.setText("请先输入要借阅的图书 ID！");
            } else {
                String resultMessage = libraryService.borrowBook(bookId);
                statusLabel.setStyle(resultMessage.startsWith("成功") ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
                statusLabel.setText(resultMessage);
                updateListView(libraryService.getAllBooks()); // 刷界面
            }
        });

        // 事件 4: 点击归还
        returnBtn.setOnAction(e -> {
            String bookId = idField.getText().trim();
            if (bookId.isEmpty()) {
                statusLabel.setStyle("-fx-text-fill: red;");
                statusLabel.setText("请先输入要归还的图书 ID！");
            } else {
                String resultMessage = libraryService.returnBook(bookId);
                statusLabel.setStyle(resultMessage.startsWith("成功") ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
                statusLabel.setText(resultMessage);
                updateListView(libraryService.getAllBooks()); // 刷新界面
            }
        });

        // 初始化加载图书列表
        updateListView(libraryService.getAllBooks());

        // 主布局构建
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.getChildren().addAll(
                headerLabel, 
                searchBox, 
                new Label("图书列表与实时状态:"), 
                bookListView, 
                actionBox, 
                statusLabel
        );

        Scene scene = new Scene(root, 520, 480);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // 辅助方法：更新 ListView 显示内容
    private void updateListView(List<Book> books) {
        bookListView.getItems().clear();
        for (Book b : books) {
            bookListView.getItems().add(b.toString());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
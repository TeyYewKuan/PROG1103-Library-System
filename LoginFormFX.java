import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class LoginFormFX extends Application {

    // 模拟数据库存有预设的用户（包括学生和管理员）
    private List<User> userDatabase = new ArrayList<>();

    public LoginFormFX() {
        // 初始化预置账号 (测试用)
        userDatabase.add(new Student("student1", "1234", "Tey Yew Kuan"));
        userDatabase.add(new Librarian("admin1", "admin123", "Madam Roslinda"));
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Library Management System - User Login");

        // 布局构建
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(12);
        grid.setVgap(15);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // 标题
        Label titleLabel = new Label("南方大学学院 图书馆系统");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        grid.add(titleLabel, 0, 0, 2, 1);

        // 账号输入
        Label userLabel = new Label("用户账号 (ID):");
        grid.add(userLabel, 0, 1);

        TextField userTextField = new TextField();
        userTextField.setPromptText("例如: student1 或 admin1");
        grid.add(userTextField, 1, 1);

        // 密码输入
        Label pwLabel = new Label("密码 (Password):");
        grid.add(pwLabel, 0, 2);

        PasswordField pwBox = new PasswordField();
        pwBox.setPromptText("输入密码");
        grid.add(pwBox, 1, 2);

        // 登录与重置按钮
        Button loginBtn = new Button("登录 (Login)");
        Button clearBtn = new Button("重置 (Clear)");
        HBox btnBox = new HBox(10);
        btnBox.setAlignment(Pos.BOTTOM_RIGHT);
        btnBox.getChildren().addAll(clearBtn, loginBtn);
        grid.add(btnBox, 1, 3);

        // 消息提示提示框
        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-font-weight: bold;");
        grid.add(messageLabel, 0, 4, 2, 1);

        // 事件处理 1: 点击登录
        loginBtn.setOnAction(e -> {
            String inputId = userTextField.getText().trim();
            String inputPw = pwBox.getText().trim();

            if (inputId.isEmpty() || inputPw.isEmpty()) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("提示: 请填写完整账号和密码！");
                return;
            }

            User authenticatedUser = authenticate(inputId, inputPw);

            if (authenticatedUser != null) {
                messageLabel.setStyle("-fx-text-fill: green;");
                // 调用多态方法，根据账号角色显示不同的欢迎词
                messageLabel.setText(authenticatedUser.getRoleWelcomeMessage());
            } else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("错误: 账号或密码不正确！");
            }
        });

        // 事件处理 2: 点击重置
        clearBtn.setOnAction(e -> {
            userTextField.clear();
            pwBox.clear();
            messageLabel.setText("");
        });

        Scene scene = new Scene(grid, 450, 320);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // 用户验证逻辑
    private User authenticate(String userId, String password) {
        for (User user : userDatabase) {
            if (user.getUserId().equalsIgnoreCase(userId) && user.getPassword().equals(password)) {
                return user; // 验证成功，返回相应的 User 子类对象
            }
        }
        return null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
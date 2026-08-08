import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LoginFormFX {

    private User[] userDatabase = new User[2];

    public LoginFormFX() {
        userDatabase[0] = new Student("student1", "1234", "Tey Yew Kuan");
        userDatabase[1] = new Librarian("admin1", "admin123", "Madam Roslinda");
    }

    public VBox getLoginView() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(12);
        grid.setVgap(15);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label titleLabel = new Label("Library User Login");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        grid.add(titleLabel, 0, 0, 2, 1);

        Label userLabel = new Label("User ID:");
        grid.add(userLabel, 0, 1);

        TextField userTextField = new TextField();
        userTextField.setPromptText("e.g. student1 or admin1");
        grid.add(userTextField, 1, 1);

        Label pwLabel = new Label("Password:");
        grid.add(pwLabel, 0, 2);

        PasswordField pwBox = new PasswordField();
        pwBox.setPromptText("Enter password");
        grid.add(pwBox, 1, 2);

        Button loginBtn = new Button("Login");
        Button clearBtn = new Button("Clear");
        HBox btnBox = new HBox(10);
        btnBox.setAlignment(Pos.BOTTOM_RIGHT);
        btnBox.getChildren().addAll(clearBtn, loginBtn);
        grid.add(btnBox, 1, 3);

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-font-weight: bold;");
        grid.add(messageLabel, 0, 4, 2, 1);

        loginBtn.setOnAction(e -> {
            String inputId = userTextField.getText().trim();
            String inputPw = pwBox.getText().trim();

            if (inputId.isEmpty() || inputPw.isEmpty()) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Please fill in both ID and password.");
                return;
            }

            User authenticatedUser = authenticate(inputId, inputPw);

            if (authenticatedUser != null) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText(authenticatedUser.getRoleWelcomeMessage());
            } else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Incorrect ID or password.");
            }
        });

        clearBtn.setOnAction(e -> {
            userTextField.clear();
            pwBox.clear();
            messageLabel.setText("");
        });

        VBox wrapper = new VBox(grid);
        wrapper.setAlignment(Pos.TOP_CENTER);
        wrapper.setPadding(new Insets(40, 0, 0, 0));

        return wrapper;
    }

    private User authenticate(String userId, String password) {
        if (userId == null || password == null) return null;

        String cleanId = userId.trim();
        String cleanPw = password.trim();

        for (User u : userDatabase) {
            if (u.getUserId().equalsIgnoreCase(cleanId) && u.getPassword().equals(cleanPw)) {
                return u;
            }
        }
        return null;
    }
}
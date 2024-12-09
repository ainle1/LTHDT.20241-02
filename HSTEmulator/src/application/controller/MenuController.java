package application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;


public class MenuController {

    // Các nút được khai báo trong FXML
    @FXML
    private Button btnStart;
    @FXML
    private Button btnHelp;
    @FXML
    private Button btnExit;

    // Phương thức xử lý sự kiện khi nhấn nút "Start"
    @FXML
    private void onStartClick() {
        System.out.println("Start clicked");

        // Tạo cửa sổ mới (Stage)
        Stage stage = new Stage();
        stage.setTitle("Choose Simulation Mode");

        // Tạo VBox để chứa các nút và tiêu đề
        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(20));

        // Thêm tiêu đề
        Label titleLabel = new Label("Please choose a simulation mode:");
        titleLabel.setStyle("-fx-font-size: 14;");

        double buttonWidth = 200;

        // Các nút tùy chọn
        Button balancedButton = new Button("Hệ sinh thái cân bằng");
        balancedButton.setPrefWidth(buttonWidth);

        Button overpopulationButton = new Button("Quá tải dân số");
        overpopulationButton.setPrefWidth(buttonWidth);

        Button extinctionButton = new Button("Tuyệt chủng");
        extinctionButton.setPrefWidth(buttonWidth);

        // Sự kiện cho các nút
        balancedButton.setOnAction(e -> {
            System.out.println("Balanced Ecosystem selected");
            loadEmulatorView("balanced");
            stage.close(); // Đóng cửa sổ
        });

        overpopulationButton.setOnAction(e -> {
            System.out.println("Overpopulation selected");
            loadEmulatorView("overpopulation");
            stage.close(); // Đóng cửa sổ
        });

        extinctionButton.setOnAction(e -> {
            System.out.println("Extinction selected");
            loadEmulatorView("extinction");
            stage.close(); // Đóng cửa sổ
        });

        // Thêm các nút vào VBox
        content.getChildren().addAll(titleLabel, balancedButton, overpopulationButton, extinctionButton);

        // Tạo Scene và gán vào Stage
        Scene scene = new Scene(content, 300, 200);
        stage.setScene(scene);

        // Xử lý sự kiện đóng cửa sổ bằng nút 'X'
        stage.setOnCloseRequest(e -> {
            System.out.println("Alert closed using the 'X' button.");
            stage.close(); // Đảm bảo đóng cửa sổ khi bấm nút 'X'
        });

        // Hiển thị cửa sổ
        stage.show();
    }

    // Phương thức load emulator-view.fxml
    private void loadEmulatorView(String mode) {
    	EmulatorController emulatorcontroller = new EmulatorController();
    	emulatorcontroller.initialize(mode);
    }

    // Phương thức xử lý sự kiện khi nhấn nút "Help"
    @FXML
    private void onHelpClick() {
        System.out.println("Help clicked");
        // Hiển thị hướng dẫn chơi game
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("How to Play");
        alert.setHeaderText("Game Instructions");
        alert.setContentText("Here are the instructions for playing the game.");
        alert.showAndWait();
    }

    // Phương thức xử lý sự kiện khi nhấn nút "Exit"
    @FXML
    private void onExitClick() {
        System.out.println("Exit clicked");
        // Hiển thị hộp thoại xác nhận thoát
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Exit Confirmation");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.setContentText("Click OK to exit, or Cancel to stay.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("Exiting application...");
            System.exit(0); // Thoát ứng dụng
        } else {
            System.out.println("Exit canceled.");
        }
    }
}

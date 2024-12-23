package application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
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
        VBox content = new VBox(15);  // Thêm khoảng cách giữa các phần tử
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(30));  // Tăng padding cho content

        // Thêm tiêu đề
        Label titleLabel = new Label("Please choose a simulation mode:");
        titleLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #333333;");

        double buttonWidth = 200;

        // Các nút tùy chọn
        Button balancedButton = new Button("Balanced Ecosystem");
        styledButton(balancedButton, buttonWidth);

        Button overpopulationButton = new Button("Overpopulation");
        styledButton(overpopulationButton, buttonWidth);

        Button extinctionButton = new Button("Extinction");
        styledButton(extinctionButton, buttonWidth);

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
        Scene scene = new Scene(content, 400, 250);
        stage.setScene(scene);

        // Xử lý sự kiện đóng cửa sổ bằng nút 'X'
        stage.setOnCloseRequest(e -> {
            System.out.println("Alert closed using the 'X' button.");
            stage.close(); // Đảm bảo đóng cửa sổ khi bấm nút 'X'
        });

        // Hiển thị cửa sổ
        stage.show();
    }

    // Phương thức hỗ trợ tạo kiểu cho các nút
    private void styledButton(Button button, double width) {
        button.setPrefWidth(width);
        button.setStyle(
                "-fx-background-color: #81C784; " +  // Màu xanh nhạt hơn
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-font-family: Arial; " +
                "-fx-font-weight: bold; " +  // Đặt font chữ là bold
                "-fx-border-radius: 5px; " +
                "-fx-padding: 10px; " +
                "-fx-background-radius: 5px;"
        );

        // Thêm hiệu ứng hover
        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: #66BB6A; " +  // Màu xanh nhạt hơn khi hover
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-font-family: Arial; " +
                "-fx-font-weight: bold; " +  // Đảm bảo font chữ vẫn là bold khi hover
                "-fx-border-radius: 5px; " +
                "-fx-padding: 10px; " +
                "-fx-background-radius: 5px;"
        ));

        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: #81C784; " +  // Màu xanh nhạt hơn khi không hover
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-font-family: Arial; " +
                "-fx-font-weight: bold; " +  // Đảm bảo font chữ vẫn là bold khi không hover
                "-fx-border-radius: 5px; " +
                "-fx-padding: 10px; " +
                "-fx-background-radius: 5px;"
        ));
    }



    // Phương thức load emulator-view.fxml
    private void loadEmulatorView(String mode) {
        try {
            // Tải file FXML cho màn hình emulator-view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/emulator-view.fxml"));
            Stage stage = new Stage(); // Tạo một cửa sổ mới
            stage.setTitle(mode + " - Emulator View");

            // Tạo scene từ FXML
            Parent root = loader.load();

            // Bọc Root Node trong một StackPane để thêm padding
            StackPane paddedRoot = new StackPane(root);
            paddedRoot.setPadding(new Insets(20, 20, 30, 20)); // Top, Right, Bottom, Left

            // Tạo scene từ StackPane bọc Root Node
            Scene scene = new Scene(paddedRoot);

            // Set scene vào cửa sổ
            stage.setScene(scene);

            // Lấy controller từ loader và gọi setup(mode)
            EmulatorController emulatorController = loader.getController();
            emulatorController.setup(mode);

            // Hiển thị màn hình mới
            stage.show();

            // Đóng cửa sổ hiện tại (nếu cần)
            Stage currentStage = (Stage) btnStart.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            // Hiển thị lỗi nếu có sự cố khi tải FXML
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load Emulator View");
            alert.setContentText("An error occurred while loading the new view.");
            alert.showAndWait();
        }
    }



    // Phương thức xử lý sự kiện khi nhấn nút "Help"
    @FXML
    private void onHelpClick() {
        System.out.println("Help clicked");

        // Tải tệp FXML cho màn hình Help
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/help.fxml"));
            BorderPane helpRoot = loader.load(); // Load FXML vào root

            // Tạo một cửa sổ mới (Stage)
            Stage helpStage = new Stage();
            helpStage.setTitle("Help - Game Instructions");  // Tiêu đề cửa sổ

            // Tạo Scene với root là BorderPane chứa FXML đã tải
            Scene helpScene = new Scene(helpRoot);
            helpStage.setScene(helpScene);

            // Tự động điều chỉnh kích thước cửa sổ theo kích thước của ảnh
            helpStage.sizeToScene();

            // Hiển thị cửa sổ giúp
            helpStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading help screen");
        }
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

package application.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import application.model.HeSinhThai;
import application.utilites.EcosystemGenerator;

public class EmulatorController {
    private static HeSinhThai hesinhthai;
    @FXML
    private GridPane gridPane;  // Liên kết với GridPane trong FXML

    @FXML
    private Label slsvSanXuatLabel;  // Liên kết với Label trong FXML
    @FXML
    private Label slsvAnCoLabel;
    @FXML
    private Label slsvAnThitLabel;

    // Phương thức khởi tạo
    public void initialize(String mode) {
        EcosystemGenerator ecosystemGenerator = new EcosystemGenerator();
        
        // Giả sử generateEcosystem trả về một mảng hoặc danh sách có 3 giá trị
        int[] ecosystemValues = ecosystemGenerator.generateEcosystem(mode);

        if (ecosystemValues.length == 3) {
            // Lấy các giá trị từ mảng trả về
            int slsvSanXuat = ecosystemValues[0];
            int slsvAnCo = ecosystemValues[1];
            int slsvAnThit = ecosystemValues[2];

            // Khởi tạo đối tượng HeSinhThai với các giá trị lấy từ ecosystemGenerator
            hesinhthai = new HeSinhThai(slsvSanXuat, slsvAnCo, slsvAnThit);

            // Cập nhật giá trị vào các Label trong FXML
            slsvSanXuatLabel.setText(String.valueOf(slsvSanXuat));
            slsvAnCoLabel.setText(String.valueOf(slsvAnCo));
            slsvAnThitLabel.setText(String.valueOf(slsvAnThit));
        } else {
            // Nếu phương thức generateEcosystem không trả về đúng 3 giá trị
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Ecosystem Data");
            alert.setContentText("Ecosystem data is invalid.");
            alert.showAndWait();
            return;
        }

        try {
            // Tải file FXML cho màn hình emulator-view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/emulator-view.fxml"));
            Stage stage = new Stage(); // Tạo một cửa sổ mới
            stage.setTitle(mode + " - Emulator View");

            // Tạo scene từ FXML và set vào cửa sổ
            Scene scene = new Scene(loader.load());  // Load FXML và tạo scene
            stage.setScene(scene);

            // Hiển thị màn hình mới
            stage.show();

            // Đóng cửa sổ hiện tại (nếu cần)
            Stage currentStage = (Stage) gridPane.getScene().getWindow();  // Sử dụng gridPane để lấy Stage hiện tại
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

        createGrid(gridPane, 20, 10);  // Tạo lưới 20x10
    }

    // Phương thức tạo GridPane động
    public void createGrid(GridPane grid, int columns, int rows) {
        grid.getChildren().clear(); // Xóa các thành phần cũ
        
        // Tạo các ô trong grid
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                Rectangle rectangle = new Rectangle(50, 50);
                rectangle.setFill(Color.WHITE);  // Màu ô
                rectangle.setStroke(Color.GRAY); // Viền ô
                grid.add(rectangle, col, row);  // Thêm ô vào GridPane tại (col, row)
            }
        }
    }

    // Phương thức xử lý khi nhấn vào Exit
    @FXML
    private void handleExitClick(javafx.scene.input.MouseEvent event) {
        try {
            // Load màn hình menu
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menu-view.fxml"));
            Scene menuScene = new Scene(loader.load(), 800, 500); // Thiết lập kích thước scene

            // Lấy stage hiện tại
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Đặt kích thước màn hình
            currentStage.setWidth(800); // Chiều rộng
            currentStage.setHeight(500); // Chiều cao

            // Đặt scene mới là menu
            currentStage.setScene(menuScene);

            // Căn giữa theo cả chiều ngang và dọc
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds(); // Lấy kích thước màn hình
            currentStage.setX((screenBounds.getWidth() - 800) / 2); // Căn giữa theo chiều ngang
            currentStage.setY((screenBounds.getHeight() - 500) / 2); // Căn giữa theo chiều dọc

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

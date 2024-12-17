package application.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.application.Platform; // Import Platform for thread handling
import javafx.stage.WindowEvent;  // Import for window close event
import javafx.scene.control.Button;
import java.io.IOException;

import application.model.HeSinhThai;
import application.model.O;
import application.model.SinhVat;
import application.model.SinhVatAnCo;
import application.model.SinhVatAnThit;
import application.model.SinhVatSanXuat;
import application.utilites.EcosystemGenerator;

public class EmulatorController {
    private static HeSinhThai hesinhthai;

    @FXML
    private GridPane gridPane;  // Liên kết với GridPane trong FXML

    @FXML
    private Label deathRateLabel;  // Liên kết với Label tỉ lệ tử vong
    
    @FXML
    private Label birthRateLabel;  // Liên kết với Label tỉ lệ tử vong
    
    @FXML
    private Label dayLabel;  // Liên kết với Label ngày (bước thời gian)
    @FXML
    private Label slsvSanXuatLabel;  // Liên kết với Label trong FXML
    @FXML
    private Label slsvAnCoLabel;
    @FXML
    private Label slsvAnThitLabel;

    @FXML
    private Button btnExit;
    
    @FXML
    private Button btnStopResume;
    @FXML
    private ImageView btnIcon;

    private boolean isStopped = false;
    // Phương thức khởi tạo
    public void initialize() {
        System.out.println("Initializing EmulatorController...");

        // Kiểm tra các Label và GridPane đã được khởi tạo chưa
        if (slsvSanXuatLabel == null || slsvAnCoLabel == null || slsvAnThitLabel == null || dayLabel == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("UI elements not initialized");
            alert.setContentText("There was a problem initializing the UI elements.");
            alert.showAndWait();
        }

        if (gridPane == null) {
            System.out.println("GridPane is not initialized properly.");
        }

        // Lấy cửa sổ hiện tại và thêm xử lý sự kiện đóng cửa sổ
        Platform.runLater(() -> {
            Stage stage = (Stage) gridPane.getScene().getWindow();
            stage.setOnCloseRequest((WindowEvent event) -> {
                // Thoát toàn bộ ứng dụng
                Platform.exit();
            });
        });
    }

    // Phương thức setup với mode được truyền vào
    public void setup(String mode) {
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
            // Đảm bảo `batDauCapnhatTheoThoiGian` chỉ chạy sau khi UI đã hiển thị và chờ 2 giây
            hesinhthai.batDauCapnhatTheoThoiGian();
            
            // Liên kết các Label với các property trong model
            slsvSanXuatLabel.textProperty().bind(hesinhthai.slsvSanXuatProperty().asString());
            slsvAnCoLabel.textProperty().bind(hesinhthai.slsvAnCoProperty().asString());
            slsvAnThitLabel.textProperty().bind(hesinhthai.slsvAnThitProperty().asString());
            deathRateLabel.textProperty().bind(hesinhthai.deathRateProperty().asString());
            birthRateLabel.textProperty().bind(hesinhthai.birthRateProperty().asString());
            // Liên kết Label bước thời gian với property buocThoiGian
            dayLabel.textProperty().bind(hesinhthai.buocThoiGianProperty().asString());
            
        } else {
            // Nếu phương thức generateEcosystem không trả về đúng 3 giá trị
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Ecosystem Data");
            alert.setContentText("Ecosystem data is invalid.");
            alert.showAndWait();
            return;
        }

        // Kiểm tra gridPane trước khi gọi createGrid
        if (gridPane != null) {
            createGrid(gridPane, 20, 10);  // Tạo lưới 20x10
        } else {
            System.out.println("GridPane is not initialized. Unable to create grid.");
        }
    }

    // Phương thức tạo GridPane động
    public void createGrid(GridPane grid, int columns, int rows) {
        grid.getChildren().clear(); // Xóa các thành phần cũ

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                // Tạo Rectangle hoặc ImageView
                StackPane cellPane = new StackPane();
                cellPane.setPrefSize(50, 50);
                cellPane.setStyle("-fx-border-color: grey; -fx-border-width: 0.5; -fx-border-style: solid;");

                // Sửa lại cách lấy O từ luoi để khớp với chỉ số
                O o = hesinhthai.getO(col, row); // Đảm bảo row là chỉ số hàng, col là chỉ số cột

                o.sinhvatProperty().addListener((obs, oldVal, newVal) -> {
                    // Đảm bảo thao tác cập nhật giao diện luôn thực hiện trên thread chính
                    Platform.runLater(() -> {
                        if (newVal == null) {
                            cellPane.getChildren().clear();
                        } else if (newVal instanceof SinhVatSanXuat) {
                            cellPane.getChildren().setAll(createImageView("/application/resources/grass.png"));
                        } else if (newVal instanceof SinhVatAnCo) {
                            cellPane.getChildren().setAll(createImageView("/application/resources/cow.png"));
                        } else if (newVal instanceof SinhVatAnThit) {
                            cellPane.getChildren().setAll(createImageView("/application/resources/wolf.png"));
                        }
                    });
                });

                // Cập nhật trạng thái ban đầu
                if (!o.coTrong()) {
                    SinhVat sv = o.getSinhvat();
                    if (sv instanceof SinhVatSanXuat) {
                        cellPane.getChildren().add(createImageView("/application/resources/grass.png"));
                    } else if (sv instanceof SinhVatAnCo) {
                        cellPane.getChildren().add(createImageView("/application/resources/cow.png"));
                    } else if (sv instanceof SinhVatAnThit) {
                        cellPane.getChildren().add(createImageView("/application/resources/wolf.png"));
                    }
                }

                grid.add(cellPane, col, row); // Đảm bảo add đúng cột và hàng
            }
        }
    }

    // Hàm tạo ImageView cho một ô
    private ImageView createImageView(String imagePath) {
        try {
            // Tạo Image với đường dẫn từ resources
            Image image = new Image(getClass().getResource(imagePath).toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(40);
            imageView.setFitWidth(40);
            return imageView;
        } catch (Exception e) {
            System.err.println("Error loading image: " + imagePath);
            e.printStackTrace();
            return new ImageView(); // Trả về ImageView rỗng nếu có lỗi
        }
    }

    // Phương thức xử lý khi nhấn vào Exit
    @FXML
    private void handleExitClick() {
        // Tạo một hộp thoại xác nhận
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận thoát");
        alert.setHeaderText("Bạn có chắc chắn muốn thoát?");
        alert.setContentText("Tất cả các thay đổi chưa lưu sẽ bị mất.");

        // Hiển thị hộp thoại và lấy kết quả người dùng
        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

        // Kiểm tra nếu người dùng chọn OK (Xác nhận)
        if (result == ButtonType.OK) {
            hesinhthai.dungLai();  // Gọi phương thức dungLai để dừng hệ sinh thái

            // Tạo Stage và Scene mới cho emulator-view
            try {
                // Tải file FXML cho emulator-view
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menu-view.fxml"));
                Parent root = loader.load();

                // Tạo một Scene mới với kích thước 800x500
                Scene newScene = new Scene(root, 800, 500);

                // Lấy Stage từ nút btnExit và thiết lập Scene mới
                Stage stage = (Stage) btnExit.getScene().getWindow();
                stage.setTitle("Ecosystem Emulator");  // Đặt title cho Stage mới
                stage.setScene(newScene);  // Thay thế Scene hiện tại bằng Scene mới

                // Lấy kích thước của màn hình chính
                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

                // Tính toán vị trí trung tâm của màn hình
                double centerX = screenBounds.getMinX() + (screenBounds.getWidth() - 800) / 2;
                double centerY = screenBounds.getMinY() + (screenBounds.getHeight() - 600) / 2;

                // Đặt cửa sổ ở vị trí trung tâm màn hình
                stage.setX(centerX);
                stage.setY(centerY);

                stage.show();  // Hiển thị Stage mới
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
 // Phương thức xử lý khi nhấn vào Stop Resume Button
    @FXML
    private void handleStopResume() {
        if (!isStopped) {
            // Chuyển sang trạng thái Stop
            hesinhthai.dungLai();
            btnIcon.setImage(new Image("/application/resources/resume.png")); // Thay đổi icon
            isStopped = true;
        } else {
            // Chuyển sang trạng thái Resume
            hesinhthai.tieptuc(); // Giả sử bạn có phương thức khởi động lại
            btnIcon.setImage(new Image("/application/resources/stop.png")); // Thay đổi icon
            isStopped = false;
        }
    }
}

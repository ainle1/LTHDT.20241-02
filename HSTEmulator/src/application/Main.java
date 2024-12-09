package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Tải tệp FXML và kết nối controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menu-view.fxml"));
            BorderPane root = loader.load();  // Load FXML và gán vào root

            // Tạo scene và gán vào stage
            Scene scene = new Scene(root, 800, 500); // Kích thước màn hình
            primaryStage.setScene(scene);
            primaryStage.setTitle("Ecosystem Emulator");  // Tiêu đề của cửa sổ
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

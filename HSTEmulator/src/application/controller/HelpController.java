package application.controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HelpController {

    @FXML
    private ImageView imageView;

    private int currentImageIndex = 0;

    private final String[] imagePaths = {
        "/application/resources/help-1.png", 
        "/application/resources/help-2.png", 
        "/application/resources/help-3.png", 
        "/application/resources/help-4.png", 
    };

    // Chuyển đến ảnh trước
    @FXML
    private void onPrevious() {
        // Nếu không phải ảnh đầu tiên thì chuyển về ảnh trước
        if (currentImageIndex > 0) {
            currentImageIndex--;
        }
        // Nếu là ảnh đầu tiên, không thay đổi gì
        updateImage();
    }

    // Chuyển đến ảnh tiếp theo
    @FXML
    private void onNext() {
        // Nếu không phải ảnh cuối cùng thì chuyển đến ảnh tiếp theo
        if (currentImageIndex < imagePaths.length - 1) {
            currentImageIndex++;
        }
        // Nếu là ảnh cuối cùng, không thay đổi gì
        updateImage();
    }

    // Cập nhật ảnh
    private void updateImage() {
        Image image = new Image(getClass().getResource(imagePaths[currentImageIndex]).toExternalForm());
        imageView.setImage(image);
    }
}

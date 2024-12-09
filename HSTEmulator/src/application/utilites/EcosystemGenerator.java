package application.utilites;

import java.util.Random;

public class EcosystemGenerator {

    /**
     * Generate số lượng sinh vật dựa trên chế độ, với sự thay đổi ngẫu nhiên
     *
     * @param mode Chế độ hệ sinh thái: "balanced", "extinction", "overpopulation"
     * @return int[] Mảng chứa số lượng [Producers, Herbivores, Carnivores]
     */
    public int[] generateEcosystem(String mode) {
        Random random = new Random();
        int total = 200; // Tổng số sinh vật
        int producers = 0, herbivores = 0, carnivores = 0;

        switch (mode.toLowerCase()) {
            case "balanced":
                // Chế độ cân bằng
                producers = (int) (total * (0.7 + random.nextDouble() * 0.1)); // 70% đến 80%
                herbivores = (int) (producers * (0.08 + random.nextDouble() * 0.04)); // 8% đến 12% của producers
                carnivores = total - producers - herbivores; // Phần còn lại
                break;

            case "extinction":
                // Chế độ tuyệt chủng (giả sử tuyệt chủng Herbivores)
                producers = (int) (total * (0.85 + random.nextDouble() * 0.1)); // 85% đến 95%
                herbivores = 0; // Tuyệt chủng động vật ăn cỏ
                carnivores = total - producers - herbivores; // Phần còn lại
                break;

            case "overpopulation":
                // Chế độ quá tải dân số (giả sử Producers quá tải)
                producers = (int) (total * (0.45 + random.nextDouble() * 0.1)); // 45% đến 55%
                herbivores = (int) (producers * (0.35 + random.nextDouble() * 0.1)); // 35% đến 45% của producers
                carnivores = total - producers - herbivores; // Phần còn lại
                break;

            default:
                throw new IllegalArgumentException("Invalid mode: " + mode);
        }

        // Trả về kết quả dưới dạng mảng
        return new int[]{producers, herbivores, carnivores};
    }
}

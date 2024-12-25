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
        int total = 30; // Tổng số sinh vật
        int producers = 0, herbivores = 0, carnivores = 0;

        switch (mode.toLowerCase()) {
            case "balanced":
                // Chế độ cân bằng
                producers = (int) (total * (0.3 + random.nextDouble() * 0.1)); // 30% đến 40%
                herbivores = (int) (producers * (1.2 + random.nextDouble() * 0.2)); // 120% đến 140% của producers
                carnivores = total - producers - herbivores; // Phần còn lại
                break;

            case "extinction":
                // Chế độ tuyệt chủng (random cho một trong ba loại bị tuyệt chủng)
                producers = (int) (total * (0.4 + random.nextDouble() * 0.1)); // 40% đến 50%

                // Ngẫu nhiên quyết định loại nào sẽ bị tuyệt chủng (0: Producers, 1: Herbivores, 2: Carnivores)
                int extinctType = random.nextInt(3); // 0, 1, hoặc 2

                switch (extinctType) {
                    case 0: // Producers tuyệt chủng
                        producers = 0;
                        herbivores = (int) (total * (0.3 + random.nextDouble() * 0.1)); // 30% đến 40% cho Herbivores
                        carnivores = total - producers - herbivores; // Phần còn lại là Carnivores
                        break;
                    case 1: // Herbivores tuyệt chủng
                        herbivores = 0;
                        producers = (int) (total * (0.3 + random.nextDouble() * 0.1)); // 30% đến 40% cho Producers
                        carnivores = total - producers - herbivores; // Phần còn lại là Carnivores
                        break;
                    case 2: // Carnivores tuyệt chủng
                        carnivores = 0;
                        producers = (int) (total * (0.3 + random.nextDouble() * 0.1)); // 30% đến 40% cho Producers
                        herbivores = total - producers - carnivores; // Phần còn lại là Herbivores
                        break;
                }
                break;


            case "overpopulation":
                // Chế độ quá tải dân số
                int dominantType = random.nextInt(3); // 0: Producers, 1: Herbivores, 2: Carnivores

                switch (dominantType) {
                    case 0: // Producers vượt trội
                        producers = (int) (total * (0.6 + random.nextDouble() * 0.2)); // 60% đến 80%
                        herbivores = (int) (total * (0.1 + random.nextDouble() * 0.1)); // 10% đến 20%
                        carnivores = total - producers - herbivores; // Phần còn lại
                        break;
                    case 1: // Herbivores vượt trội
                        herbivores = (int) (total * (0.6 + random.nextDouble() * 0.2)); // 60% đến 80%
                        producers = (int) (total * (0.1 + random.nextDouble() * 0.1)); // 10% đến 20%
                        carnivores = total - producers - herbivores; // Phần còn lại
                        break;
                    case 2: // Carnivores vượt trội
                        carnivores = (int) (total * (0.6 + random.nextDouble() * 0.2)); // 60% đến 80%
                        producers = (int) (total * (0.1 + random.nextDouble() * 0.1)); // 10% đến 20%
                        herbivores = total - producers - carnivores; // Phần còn lại
                        break;
                }
                break;


            default:
                throw new IllegalArgumentException("Invalid mode: " + mode);
        }

        // Đảm bảo không có số lượng nào âm (do làm tròn hoặc random sai lệch)
        producers = Math.max(producers, 0);
        herbivores = Math.max(herbivores, 0);
        carnivores = Math.max(carnivores, 0);

        // Trả về kết quả dưới dạng mảng
        return new int[]{producers, herbivores, carnivores};
    }
}

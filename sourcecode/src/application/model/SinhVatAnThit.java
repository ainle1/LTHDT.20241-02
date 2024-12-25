package application.model;

import java.util.Random;

import javafx.application.Platform;
public class SinhVatAnThit extends SinhVat {

    // Constructor không tham số, sẽ gọi constructor của lớp cha và gán hệ sinh thái
    public SinhVatAnThit(HeSinhThai heSinhThai) {
        super(heSinhThai); // Gọi constructor của lớp cha để truyền hệ sinh thái
        this.energy = 150;
    }

    // Constructor nếu cần khởi tạo với tọa độ và hệ sinh thái
    public SinhVatAnThit(int x, int y, HeSinhThai heSinhThai) {
        super(x, y, heSinhThai); // Gọi constructor của lớp cha để truyền tọa độ và hệ sinh thái
        this.energy = 150;
    }

    // Phương thức di chuyển (chỉ di chuyển khi năng lượng thấp)
    public synchronized void dichuyen() {
            // Tìm sinh vật AnCo gần nhất
            SinhVatAnCo sinhVatAnCoGanNhat = timSinhVatAnCoGanNhat();

            if (sinhVatAnCoGanNhat != null) {
                int xCu = this.x;
                int yCu = this.y;
                int xMucTieu = sinhVatAnCoGanNhat.getX();
                int yMucTieu = sinhVatAnCoGanNhat.getY();

                // Danh sách các hướng di chuyển (8 hướng xung quanh ô hiện tại)
                int[][] huongDiChuyen = {
                    {-1, -1}, {-1, 0}, {-1, 1}, // 3 ô trên
                    {0, -1}, {0, 1}, // 2 ô bên trái và bên phải
                    {1, -1}, {1, 0}, {1, 1} // 3 ô dưới
                };

                // Biến để lưu ô gần nhất
                int xMoi = xCu;
                int yMoi = yCu;
                double khoangCachMin = Double.MAX_VALUE;

                // Duyệt qua các ô xung quanh
                for (int[] huong : huongDiChuyen) {
                    int xCanh = this.x + huong[0];
                    int yCanh = this.y + huong[1];

                    // Kiểm tra ô hợp lệ và trống
                    if (heSinhThai.isValid(xCanh, yCanh) && heSinhThai.getO(xCanh, yCanh).coTrong()) {
                        // Tính khoảng cách từ ô xung quanh đến mục tiêu
                        double khoangCach = tinhKhoangCach(xCanh, yCanh, xMucTieu, yMucTieu);

                        // Nếu ô này gần mục tiêu hơn ô hiện tại, cập nhật ô mục tiêu
                        if (khoangCach < khoangCachMin) {
                            khoangCachMin = khoangCach;
                            xMoi = xCanh;
                            yMoi = yCanh;
                        }
                    }
                }

                // Nếu tìm được ô hợp lệ để di chuyển
                if (xMoi != xCu || yMoi != yCu) {
                    heSinhThai.getO(xCu, yCu).setSinhvat(null); // Xóa sinh vật khỏi ô cũ
                    heSinhThai.getO(xMoi, yMoi).setSinhvat(this); // Đặt sinh vật vào ô mới
                    this.x = xMoi;
                    this.y = yMoi;

                    System.out.println("Sinh vật AnThit di chuyển từ (" + xCu + ", " + yCu + ") tới (" + this.x + ", " + this.y + ")");
                } else {
                    System.out.println("Không thể di chuyển tới mục tiêu do không có ô trống hợp lệ.");
                }
            } else {
                // Nếu không tìm thấy sinh vật AnCo, di chuyển ngẫu nhiên
                boolean diChuyenThanhCong = false;

                // Danh sách các hướng di chuyển (ngang, dọc, chéo)
                int[][] huongDiChuyen = {
                    {-1, 0}, {1, 0}, {0, -1}, {0, 1},   // Lên, xuống, trái, phải
                    {-1, -1}, {-1, 1}, {1, -1}, {1, 1}   // Chéo
                };

                // Duyệt qua tất cả các hướng di chuyển
                for (int[] huong : huongDiChuyen) {
                    int xMoi = this.x + huong[0];
                    int yMoi = this.y + huong[1];

                    // Kiểm tra nếu ô mới hợp lệ và trống
                    if (heSinhThai.isValid(xMoi, yMoi) && heSinhThai.getO(xMoi, yMoi).coTrong()) {
                        heSinhThai.getO(this.x, this.y).setSinhvat(null); // Xóa ô hiện tại
                        this.x = xMoi;
                        this.y = yMoi;
                        heSinhThai.getO(this.x, this.y).setSinhvat(this); // Gán sinh vật vào ô mới
                        diChuyenThanhCong = true;

                        System.out.println("Sinh vật AnThit di chuyển ngẫu nhiên từ (" + this.x + ", " + this.y + ")");
                        break; // Di chuyển thành công, thoát vòng lặp
                    }
                }

                if (!diChuyenThanhCong) {
                    System.out.println("Không thể di chuyển sinh vật AnThit, không có ô trống xung quanh.");
                }
            }
    }

    // Phương thức tìm sinh vật AnCo gần nhất
    private synchronized SinhVatAnCo timSinhVatAnCoGanNhat() {
        SinhVatAnCo sinhVatAnCoGanNhat = null;
        double khoangCachMin = Double.MAX_VALUE;

        // Duyệt qua tất cả các ô trong hệ sinh thái
        for (int i = 0; i < heSinhThai.getLuoi().length; i++) {
            for (int j = 0; j < heSinhThai.getLuoi()[i].length; j++) {
                O o = heSinhThai.getLuoi()[i][j];

                // Kiểm tra nếu ô không trống
                if (!o.coTrong()) {
                    SinhVat sinhVat = o.getSinhvat();

                    // Kiểm tra xem sinh vật trong ô có phải là SinhVatAnCo
                    if (sinhVat instanceof SinhVatAnCo) {
                        // Tính khoảng cách từ vị trí của sinh vật AnThit đến sinh vật AnCo
                        double khoangCach = tinhKhoangCach(this.x, this.y, i, j);

                        // In ra thông tin để kiểm tra
                        System.out.println("Tìm thấy SinhVatAnCo tại (" + i + ", " + j + ") với khoảng cách: " + khoangCach);

                        // Cập nhật sinh vật AnCo gần nhất
                        if (khoangCach < khoangCachMin) {
                            khoangCachMin = khoangCach;
                            sinhVatAnCoGanNhat = (SinhVatAnCo) sinhVat;
                        }
                    }
                }
            }
        }

        // Nếu không tìm thấy sinh vật AnCo
        if (sinhVatAnCoGanNhat == null) {
            System.out.println("Không tìm thấy SinhVatAnCo gần nhất.");
        } else {
            System.out.println("SinhVatAnCo gần nhất nằm tại (" 
                + sinhVatAnCoGanNhat.getX() + ", " 
                + sinhVatAnCoGanNhat.getY() + ") với khoảng cách: " + khoangCachMin);
        }

        return sinhVatAnCoGanNhat;
    }


 // Phương thức tính khoảng cách giữa 2 tọa độ (x1, y1) và (x2, y2)
    private double tinhKhoangCach(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public void tieuhao() {
        // Tạo một đối tượng Random
        Random random = new Random();

        // Lấy ngẫu nhiên giá trị từ 1 đến 2
        int haoPhi = random.nextInt(2) + 1;  // nextInt(2) sẽ trả về giá trị trong phạm vi [0, 1], cộng thêm 1 để có giá trị trong phạm vi [1, 2]

        // Giảm năng lượng theo giá trị ngẫu nhiên
        this.energy -= haoPhi;
    }
 
 
    public synchronized boolean tieuthu() {
        // Kiểm tra điều kiện trước khi tiêu thụ, chỉ thực hiện nếu năng lượng < 80
        if (this.energy >= 80) {
            System.out.println("Năng lượng đủ, không cần tiêu thụ.");
            return false; // Không thực hiện tiêu thụ khi năng lượng >= 80
        }

        // Duyệt qua các ô lân cận trong phạm vi 1 ô (x ± 1, y ± 1)
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                // Tính tọa độ ô lân cận
                int xMoi = this.x + dx;
                int yMoi = this.y + dy;

                // Bỏ qua vị trí hiện tại (dx = 0, dy = 0)
                if (dx == 0 && dy == 0) {
                    continue;
                }

                // Kiểm tra tọa độ hợp lệ (không vượt ra ngoài biên của lưới)
                if (xMoi >= 0 && xMoi < heSinhThai.getChieuNgang() && yMoi >= 0 && yMoi < heSinhThai.getChieuRong()) {
                    // Lấy sinh vật ở ô lân cận
                    SinhVat sinhvatLanhCan = heSinhThai.getO(xMoi, yMoi).getSinhvat();
                    double energyRate = heSinhThai.getEnergyrate();
                    // Kiểm tra nếu sinh vật là SinhVatAnCo
                    if (sinhvatLanhCan instanceof SinhVatAnCo) {
                        // Tăng năng lượng sinh vật ăn thịt lên x% giá trị năng lượng của sinh vật ăn cỏ
                        this.energy += ((SinhVatAnCo) sinhvatLanhCan).getEnergy() * energyRate;

                        // Xóa sinh vật ăn thịt khỏi vị trí cũ
                        heSinhThai.getO(this.x, this.y).setSinhvat(null);

                        // Di chuyển sinh vật ăn thịt đến vị trí của sinh vật ăn cỏ
                        this.x = xMoi;
                        this.y = yMoi;
                        heSinhThai.getO(xMoi, yMoi).setSinhvat(this);
                        
                        Platform.runLater(() -> {
                            heSinhThai.deathRateProperty().set(heSinhThai.deathRateProperty().get() + 1);
                        });
                        
                        System.out.println("Sinh vật ăn thịt tại (" + this.x + ", " + this.y + ") đã ăn sinh vật ăn cỏ tại (" + xMoi + ", " + yMoi + ").");

                        return true; // Trả về true khi sinh vật ăn thịt đã ăn được sinh vật ăn cỏ
                    }
                }
            }
        }

        return false; // Trả về false nếu không ăn được sinh vật ăn cỏ nào
    }


    
    @Override
    public synchronized void sinhsan() {
        // Kiểm tra nếu năng lượng vượt ngưỡng 100
        if (this.energy > 150) {

            // Duyệt qua các ô lân cận trong phạm vi 1 ô (x ± 1, y ± 1)
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    // Tính tọa độ ô lân cận
                    int xMoi = this.x + dx;
                    int yMoi = this.y + dy;

                    // Bỏ qua vị trí hiện tại (dx = 0, dy = 0)
                    if (dx == 0 && dy == 0) {
                        continue;
                    }

                    // Kiểm tra tọa độ hợp lệ (không vượt ra ngoài biên của lưới)
                    if (xMoi >= 0 && xMoi < heSinhThai.getChieuNgang() && yMoi >= 0 && yMoi < heSinhThai.getChieuRong()) {
                        // Lấy ô lân cận
                        O oLanhCan = heSinhThai.getO(xMoi, yMoi);

                        // Kiểm tra nếu ô trống (không có sinh vật nào)
                        if (oLanhCan.coTrong()) {
                            // Tạo sinh vật ăn thịt mới tại ô này
                            SinhVatAnThit sinhvatMoi = new SinhVatAnThit(xMoi, yMoi, heSinhThai);

                            // Đặt sinh vật mới vào ô
                            oLanhCan.setSinhvat(sinhvatMoi);
                        	// Tăng birthRate trong hệ sinh thái
                            Platform.runLater(() -> {
                                heSinhThai.birthRateProperty().set(heSinhThai.birthRateProperty().get() + 1);
                            });
                            System.out.println("Sinh vật ăn thịt tại (" + this.x + ", " + this.y + ") đã sinh sản tại (" + xMoi + ", " + yMoi + ").");

                            // Chỉ sinh sản 1 lần cho mỗi chu kỳ (thoát khỏi vòng lặp)
                            return;
                        }
                    }
                }
            }
        }
    }


    @Override
    public synchronized void chet() {
        if (this.energy <= 0) {
            // Kiểm tra xem sinh vật có tồn tại trong ô hiện tại hay không
            O oHienTai = heSinhThai.getO(this.x, this.y); // Lấy ô hiện tại
            if (oHienTai != null && oHienTai.getSinhvat() != null) {
                // Xóa sinh vật khỏi ô hiện tại
                oHienTai.setSinhvat(null); // Đặt sinh vật tại ô hiện tại thành null
            }
            Platform.runLater(() -> {
                heSinhThai.deathRateProperty().set(heSinhThai.deathRateProperty().get() + 1);
            });
        }
    }



}

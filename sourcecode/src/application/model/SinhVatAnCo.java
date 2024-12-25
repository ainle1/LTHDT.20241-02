package application.model;

import java.util.Random;

import javafx.application.Platform;


public class SinhVatAnCo extends SinhVat {

    // Constructor không tham số, sẽ gọi constructor của lớp cha và gán hệ sinh thái
    public SinhVatAnCo(HeSinhThai heSinhThai) {
        super(heSinhThai); // Gọi constructor của lớp cha để truyền hệ sinh thái
        this.energy = 200; // Năng lượng khởi tạo
    }

    // Constructor nếu cần khởi tạo với tọa độ và hệ sinh thái
    public SinhVatAnCo(int x, int y, HeSinhThai heSinhThai) {
        super(x, y, heSinhThai); // Gọi constructor của lớp cha để truyền tọa độ và hệ sinh thái
        this.energy = 200; // Năng lượng khởi tạo
    }

    // Phương thức di chuyển, tìm mục tiêu là SinhVatSanXuat
    public synchronized void dichuyen() {
            // Tìm sinh vật sản xuất gần nhất
            SinhVatSanXuat sinhVatSanXuatGanNhat = timSinhVatSanXuatGanNhat();

            if (sinhVatSanXuatGanNhat != null) {
                int xCu = this.x;
                int yCu = this.y;
                int xMucTieu = sinhVatSanXuatGanNhat.getX();
                int yMucTieu = sinhVatSanXuatGanNhat.getY();

                // Danh sách các hướng di chuyển
                int[][] huongDiChuyen = {
                    {-1, -1}, {-1, 0}, {-1, 1},
                    {0, -1}, {0, 1},
                    {1, -1}, {1, 0}, {1, 1}
                };

                int xMoi = xCu;
                int yMoi = yCu;
                double khoangCachMin = Double.MAX_VALUE;

                for (int[] huong : huongDiChuyen) {
                    int xCanh = this.x + huong[0];
                    int yCanh = this.y + huong[1];

                    if (heSinhThai.isValid(xCanh, yCanh) && heSinhThai.getO(xCanh, yCanh).coTrong()) {
                        double khoangCach = tinhKhoangCach(xCanh, yCanh, xMucTieu, yMucTieu);
                        if (khoangCach < khoangCachMin) {
                            khoangCachMin = khoangCach;
                            xMoi = xCanh;
                            yMoi = yCanh;
                        }
                    }
                }

                if (xMoi != xCu || yMoi != yCu) {
                    heSinhThai.getO(xCu, yCu).setSinhvat(null);
                    heSinhThai.getO(xMoi, yMoi).setSinhvat(this);
                    this.x = xMoi;
                    this.y = yMoi;

                    System.out.println("Sinh vật AnCo di chuyển từ (" + xCu + ", " + yCu + ") tới (" + this.x + ", " + this.y + ").");
                } else {
                    System.out.println("Không thể di chuyển tới mục tiêu do không có ô trống hợp lệ.");
                }
            } else {
                System.out.println("Không tìm thấy SinhVatSanXuat, di chuyển ngẫu nhiên.");
                diChuyenNgauNhien();
            }
    }

    // Phương thức tìm SinhVatSanXuat gần nhất
    private synchronized SinhVatSanXuat timSinhVatSanXuatGanNhat() {
        SinhVatSanXuat sinhVatSanXuatGanNhat = null;
        double khoangCachMin = Double.MAX_VALUE;

        for (int i = 0; i < heSinhThai.getLuoi().length; i++) {
            for (int j = 0; j < heSinhThai.getLuoi()[i].length; j++) {
                O o = heSinhThai.getLuoi()[i][j];
                if (!o.coTrong()) {
                    SinhVat sinhVat = o.getSinhvat();
                    if (sinhVat instanceof SinhVatSanXuat) {
                        double khoangCach = tinhKhoangCach(this.x, this.y, i, j);
                        if (khoangCach < khoangCachMin) {
                            khoangCachMin = khoangCach;
                            sinhVatSanXuatGanNhat = (SinhVatSanXuat) sinhVat;
                        }
                    }
                }
            }
        }

        return sinhVatSanXuatGanNhat;
    }

    // Phương thức di chuyển ngẫu nhiên khi không tìm thấy mục tiêu
    private synchronized void diChuyenNgauNhien() {
        int[][] huongDiChuyen = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1},
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
        };

        for (int[] huong : huongDiChuyen) {
            int xMoi = this.x + huong[0];
            int yMoi = this.y + huong[1];

            if (heSinhThai.isValid(xMoi, yMoi) && heSinhThai.getO(xMoi, yMoi).coTrong()) {
                heSinhThai.getO(this.x, this.y).setSinhvat(null);
                this.x = xMoi;
                this.y = yMoi;
                heSinhThai.getO(this.x, this.y).setSinhvat(this);
                System.out.println("Sinh vật AnCo di chuyển ngẫu nhiên tới (" + this.x + ", " + this.y + ").");
                break;
            }
        }
    }

    // Phương thức sinh sản
    @Override
    public synchronized void sinhsan() {
        if (this.energy > 230) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    int xMoi = this.x + dx;
                    int yMoi = this.y + dy;

                    if (dx == 0 && dy == 0) continue;

                    if (xMoi >= 0 && xMoi < heSinhThai.getChieuNgang() && yMoi >= 0 && yMoi < heSinhThai.getChieuRong()) {
                        O oLanhCan = heSinhThai.getO(xMoi, yMoi);
                        if (oLanhCan.coTrong()) {
                            SinhVatAnCo sinhvatMoi = new SinhVatAnCo(xMoi, yMoi, heSinhThai);
                            oLanhCan.setSinhvat(sinhvatMoi);
                        	// Tăng birthRate trong hệ sinh thái
                            Platform.runLater(() -> {
                                heSinhThai.birthRateProperty().set(heSinhThai.birthRateProperty().get() + 1);
                            });
                            System.out.println("Sinh vật AnCo tại (" + this.x + ", " + this.y + ") đã sinh sản tại (" + xMoi + ", " + yMoi + ").");
                            return;
                        }
                    }
                }
            }
        }
    }

    public synchronized void tieuhao() {
        // Tạo một đối tượng Random
        Random random = new Random();

        // Lấy ngẫu nhiên giá trị từ 1 đến 2
        int haoPhi = random.nextInt(2) + 1;  // nextInt(2) sẽ trả về giá trị trong phạm vi [0, 1], cộng thêm 1 để có giá trị trong phạm vi [1, 2]

        // Giảm năng lượng theo giá trị ngẫu nhiên
        this.energy -= haoPhi;
    }
 
    
    // Phương thức chết
    @Override
    public synchronized void chet() {
        if (this.energy <= 0) {
            O oHienTai = heSinhThai.getO(this.x, this.y);
            if (oHienTai != null && oHienTai.getSinhvat() != null) {
                oHienTai.setSinhvat(null);
            }
         // Tăng deathRate trong hệ sinh thái
            Platform.runLater(() -> {
                heSinhThai.deathRateProperty().set(heSinhThai.deathRateProperty().get() + 1);
            });
        }
    }
    
    
 // Phương thức tiêu thụ (giống với logic của SinhVatAnThit)
    public synchronized boolean tieuthu() {
        // Kiểm tra nếu năng lượng hiện tại < 150 thì mới thực hiện tiêu thụ
        if (this.energy < 150) {
            // Tìm SinhVatSanXuat gần nhất
            SinhVatSanXuat mucTieu = timSinhVatSanXuatGanNhat();
            double energyRate = heSinhThai.getEnergyrate();
            if (mucTieu != null) {
                // Lấy tọa độ của SinhVatSanXuat
                int xMucTieu = mucTieu.getX();
                int yMucTieu = mucTieu.getY();

                // Loại bỏ SinhVatSanXuat khỏi hệ sinh thái (vị trí của SinhVatSanXuat)
                O oMucTieu = heSinhThai.getO(xMucTieu, yMucTieu);
                if (oMucTieu != null) {
                    oMucTieu.setSinhvat(null);  // Loại bỏ SinhVatSanXuat khỏi ô hiện tại
                }

                // Cộng thêm 10% năng lượng của sinh vật
                int energyIncrease = (int) (mucTieu.getEnergy() * energyRate);  // Tăng 10% năng lượng của SinhVatSanXuat
                this.energy += energyIncrease;

                System.out.println("Sinh vật AnCo tại (" + this.x + ", " + this.y + ") đã tiêu thụ SinhVatSanXuat tại (" 
                                   + xMucTieu + ", " + yMucTieu + "), và năng lượng tăng thêm " + energyIncrease + ".");

                // Không thay đổi vị trí của sinh vật AnCo, chỉ tiêu thụ SinhVatSanXuat
                // Vị trí của sinh vật AnCo không bị thay đổi

                // Tăng deathRate trong hệ sinh thái
                Platform.runLater(() -> {
                    heSinhThai.deathRateProperty().set(heSinhThai.deathRateProperty().get() + 1);
                });

                return true; // Tiêu thụ thành công
            }

            System.out.println("Sinh vật AnCo tại (" + this.x + ", " + this.y + ") không tìm thấy SinhVatSanXuat để tiêu thụ.");
            return false; // Không tiêu thụ được
        } else {
            System.out.println("Sinh vật AnCo tại (" + this.x + ", " + this.y + ") có năng lượng >= 150, không thực hiện tiêu thụ.");
            return false; // Không thực hiện tiêu thụ nếu năng lượng >= 150
        }
    }




    
    // Phương thức tính khoảng cách giữa 2 tọa độ (x1, y1) và (x2, y2)
    private double tinhKhoangCach(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

}

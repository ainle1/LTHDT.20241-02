package application.model;

public class HeSinhThai {
    private O[][] luoi;  // Mảng 2 chiều lưu trữ các ô trong lưới
    private int buocThoiGian = 1;
    private int slsvSanXuat;
    private int slsvAnCo;
    private int slsvAnThit;

    // Constructor
    public HeSinhThai(int slsvSanXuat, int slsvAnCo, int slsvAnThit) {
        this.slsvSanXuat = slsvSanXuat;
        this.slsvAnCo = slsvAnCo;
        this.slsvAnThit = slsvAnThit;
        
        // Khởi tạo lưới 2D
        this.luoi = new O[20][10];
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                luoi[i][j] = new O(i, j);  // Khởi tạo các ô trong lưới
            }
        }
    }

    // Phương thức cập nhật trạng thái của hệ sinh thái
    public void Capnhat() {
        for (int i = 0; i < luoi.length; i++) {
            for (int j = 0; j < luoi[i].length; j++) {
                if (!luoi[i][j].coTrong()) {
                    SinhVat sinhvat = luoi[i][j].getSinhvat();
                    // Xử lý sinh vật nếu có
                }
            }
        }
        buocThoiGian++; // Tăng bước thời gian
    }

    // Phương thức thêm sinh vật vào hệ sinh thái
    public void ThemSinhVat(SinhVat sinhvat, int x, int y) {
        if (x >= 0 && x < luoi.length && y >= 0 && y < luoi[0].length) {
            luoi[x][y].setSinhvat(sinhvat);  // Thêm sinh vật vào ô tại (x, y)
        }
    }

    // Các getter và setter
    public int getSlsvSanXuat() {
        return slsvSanXuat;
    }

    public int getSlsvAnCo() {
        return slsvAnCo;
    }

    public int getSlsvAnThit() {
        return slsvAnThit;
    }

    public void setSlsvSanXuat(int slsvSanXuat) {
        this.slsvSanXuat = slsvSanXuat;
    }

    public void setSlsvAnCo(int slsvAnCo) {
    }
}
    }

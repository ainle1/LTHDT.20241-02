package application.model;

public abstract class SinhVat {
    protected int energy; // Năng lượng ban đầu
    protected int x; // Tọa độ x của sinh vật
    protected int y; // Tọa độ y của sinh vật
    protected static HeSinhThai heSinhThai; // Hệ sinh thái dùng chung

    // Constructor nhận hệ sinh thái
    public SinhVat(HeSinhThai heSinhThai) {
        SinhVat.heSinhThai = heSinhThai; // Gán hệ sinh thái cho sinh vật
    }

    // Constructor nhận tọa độ và hệ sinh thái
    public SinhVat(int x, int y, HeSinhThai heSinhThai) {
        this.x = x;
        this.y = y;
        SinhVat.heSinhThai = heSinhThai;
    }

    // Phương thức trừu tượng cần các lớp con triển khai
    public abstract void sinhsan(); // Xử lý sinh sản
    public abstract void chet(); // Check năng lượng => chết or not
    // Phương thức thiết lập tọa độ
    public void setToaDo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Getter và setter
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy; // Đảm bảo năng lượng không vượt quá mức tối đa
    }

    // Phương thức kiểm tra nếu hệ sinh thái đã được gán
    public static boolean isHeSinhThaiAvailable() {
        return heSinhThai != null;
    }

    // Getter cho hệ sinh thái (nếu cần truy cập)
    public static HeSinhThai getHeSinhThai() {
        return heSinhThai;
    }
}

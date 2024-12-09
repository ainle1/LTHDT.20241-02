package application.model;

public class O {
    private int x;
    private int y;
    private SinhVat sinhvat;

    public O(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public SinhVat getSinhvat() {
        return sinhvat;
    }

    public void setSinhvat(SinhVat sinhvat) {
        this.sinhvat = sinhvat;
    }

    // Kiểm tra ô có trống không
    public boolean coTrong() {
        return sinhvat == null;
    }
}

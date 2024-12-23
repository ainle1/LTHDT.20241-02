package application.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class O {
    private int x;
    private int y;
    private ObjectProperty<SinhVat> sinhvat = new SimpleObjectProperty<>(); // Dùng ObjectProperty để bind

    public O(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public SinhVat getSinhvat() {
        return sinhvat.get();
    }

    public void setSinhvat(SinhVat sinhvat) {
        this.sinhvat.set(sinhvat);
    }

    public ObjectProperty<SinhVat> sinhvatProperty() {
        return sinhvat;
    }

    // Kiểm tra ô có trống không
    public boolean coTrong() {
        return sinhvat.get() == null;
    }
}

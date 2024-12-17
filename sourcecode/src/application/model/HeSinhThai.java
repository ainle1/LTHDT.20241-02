package application.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class HeSinhThai {
    private O[][] luoi;  // Mảng 2 chiều lưu trữ các ô trong lưới
    private int chieuNgang = 20;
    private int chieuRong = 10;
    private IntegerProperty deathrate = new SimpleIntegerProperty(0) ;
	private IntegerProperty birthrate = new SimpleIntegerProperty(0);
	private IntegerProperty buocThoiGian = new SimpleIntegerProperty(1); // Sử dụng IntegerProperty
    private IntegerProperty slsvSanXuat = new SimpleIntegerProperty();
    private IntegerProperty slsvAnCo = new SimpleIntegerProperty();
    private IntegerProperty slsvAnThit = new SimpleIntegerProperty();
    private volatile boolean isStopped = false;  // Biến flag để kiểm soát dừng

    // Constructor
    public HeSinhThai(int slsvSanXuat, int slsvAnCo, int slsvAnThit) {
        this.slsvSanXuat.set(slsvSanXuat);
        this.slsvAnCo.set(slsvAnCo);
        this.slsvAnThit.set(slsvAnThit);
        
        // Khởi tạo lưới 2D
        this.luoi = new O[chieuNgang][chieuRong];
        for (int i = 0; i < chieuNgang; i++) {
            for (int j = 0; j < chieuRong; j++) {
                luoi[i][j] = new O(i, j);  // Khởi tạo các ô trong lưới
            }
        }
        
        // Khởi tạo SinhVat vào các ô trong lưới 2D
        initializeSinhVat();
    }
    
    
    public synchronized IntegerProperty getDeathrate() {
		return deathrate;
	}


	public synchronized void setDeathrate(IntegerProperty deathrate) {
		this.deathrate = deathrate;
	}


	public synchronized IntegerProperty getBirthrate() {
		return birthrate;
	}


	public synchronized void setBirthrate(IntegerProperty birthrate) {
		this.birthrate = birthrate;
	}


	public void batDauCapnhatTheoThoiGian() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        
        // Thực thi phương thức Capnhat sau mỗi 1 giây
        scheduler.scheduleAtFixedRate(() -> {
            Capnhat(); // Gọi phương thức cập nhật hệ sinh thái
        }, 0, 1000, TimeUnit.MILLISECONDS); // Bắt đầu ngay lập tức và thực hiện mỗi 1 giây
    }
    
    
    public void dungLai() {
    	this.isStopped = true;
    }
    
    
    public void tieptuc() {
    	this.isStopped = false;
    }
    
 // Phương thức khởi tạo sinh vật vào các ô trong lưới
    private void initializeSinhVat() {
        List<SinhVat> sinhVats = new ArrayList<>();

        // Tạo sinh vật loại SanXuat
        for (int i = 0; i < slsvSanXuat.get(); i++) {
            sinhVats.add(new SinhVatSanXuat(this)); // Truyền `this` (HeSinhThai hiện tại)
        }

        // Tạo sinh vật loại AnCo
        for (int i = 0; i < slsvAnCo.get(); i++) {
            sinhVats.add(new SinhVatAnCo(this));
        }

        // Tạo sinh vật loại AnThit
        for (int i = 0; i < slsvAnThit.get(); i++) {
            sinhVats.add(new SinhVatAnThit(this));
        }

        // Trộn danh sách sinh vật ngẫu nhiên
        Collections.shuffle(sinhVats);

        // Lấy danh sách các vị trí trống
        List<int[]> emptyPositions = new ArrayList<>();
        for (int i = 0; i < luoi.length; i++) {
            for (int j = 0; j < luoi[i].length; j++) {
                emptyPositions.add(new int[]{i, j});
            }
        }

        // Trộn danh sách vị trí
        Collections.shuffle(emptyPositions);

        // Gán sinh vật vào ô
        for (int i = 0; i < sinhVats.size(); i++) {
            int[] position = emptyPositions.get(i);
            int x = position[0];
            int y = position[1];

            if (luoi[x][y].coTrong()) {
                SinhVat sinhVat = sinhVats.get(i);
                sinhVat.setToaDo(x, y); // Cập nhật tọa độ
                luoi[x][y].setSinhvat(sinhVat); // Gán sinh vật vào ô
            }
        }
    }



    // Phương thức cập nhật trạng thái của hệ sinh thái
    public synchronized void Capnhat() {
        List<SinhVat> sinhVatDaXuLy = new ArrayList<>();

        if (isStopped) {
            return;
        }

        for (int i = 0; i < luoi.length; i++) {
            if (isStopped) return;

            for (int j = 0; j < luoi[i].length; j++) {
                if (isStopped) return;

                if (!luoi[i][j].coTrong()) {
                    SinhVat sinhvat = luoi[i][j].getSinhvat();

                    if (sinhvat != null && !sinhVatDaXuLy.contains(sinhvat)) {
                        sinhVatDaXuLy.add(sinhvat);

                        // Xử lý logic từng loại sinh vật
                        if (sinhvat instanceof SinhVatAnCo) {
                            SinhVatAnCo sinhVatAnCo = (SinhVatAnCo) sinhvat;
                            Random random = new Random();
                            if (random.nextInt(2) == 0) sinhVatAnCo.dichuyen();

                            sinhVatAnCo.tieuhao();
                            sinhVatAnCo.tieuthu();
                            if (sinhVatAnCo.getEnergy() <= 0) sinhVatAnCo.chet();
                            else sinhVatAnCo.sinhsan();

                        } else if (sinhvat instanceof SinhVatAnThit) {
                            SinhVatAnThit sinhVatAnThit = (SinhVatAnThit) sinhvat;
                            Random random = new Random();
                            if (random.nextInt(2) == 0) sinhVatAnThit.dichuyen();

                            sinhVatAnThit.tieuhao();
                            sinhVatAnThit.tieuthu();
                            if (sinhVatAnThit.getEnergy() <= 0) sinhVatAnThit.chet();
                            else sinhVatAnThit.sinhsan();

                        } else if (sinhvat instanceof SinhVatSanXuat) {
                            SinhVatSanXuat sinhVatSanXuat = (SinhVatSanXuat) sinhvat;
                            Random random = new Random();
                            if (random.nextInt(100) < 30) sinhVatSanXuat.sinhsan();
                            sinhVatSanXuat.quanghop();
                        }
                    }
                }
            }
        }

        // Đếm lại số lượng từng loại sinh vật
        int[] counts = new int[3]; // counts[0] = SanXuat, counts[1] = AnCo, counts[2] = AnThit

        for (int i = 0; i < luoi.length; i++) {
            for (int j = 0; j < luoi[i].length; j++) {
                if (!luoi[i][j].coTrong()) {
                    SinhVat sinhvat = luoi[i][j].getSinhvat();
                    if (sinhvat instanceof SinhVatSanXuat) {
                        counts[0]++;
                    } else if (sinhvat instanceof SinhVatAnCo) {
                        counts[1]++;
                    } else if (sinhvat instanceof SinhVatAnThit) {
                        counts[2]++;
                    }
                }
            }
        }

     // Cập nhật UI trên luồng JavaFX
        Platform.runLater(() -> {
            slsvSanXuat.set(counts[0]);
            slsvAnCo.set(counts[1]);
            slsvAnThit.set(counts[2]);
            buocThoiGian.set(buocThoiGian.get() + 1); // Tăng bước thời gian

            // Cập nhật deathrate lên UI
            deathrate.set(this.getDeathrate().get());
        });


    }




    // Phương thức thêm sinh vật vào hệ sinh thái
    public void ThemSinhVat(SinhVat sinhvat, int x, int y) {
        if (x >= 0 && x < luoi.length && y >= 0 && y < luoi[0].length) {
            luoi[x][y].setSinhvat(sinhvat);  // Thêm sinh vật vào ô tại (x, y)
        }
    }

    // Các getter và setter cho IntegerProperty
    public IntegerProperty buocThoiGianProperty() {
        return buocThoiGian;
    }

    public IntegerProperty slsvSanXuatProperty() {
        return slsvSanXuat;
    }

    public IntegerProperty slsvAnCoProperty() {
        return slsvAnCo;
    }

    public IntegerProperty slsvAnThitProperty() {
        return slsvAnThit;
    }
    
    public IntegerProperty deathRateProperty() {
        return deathrate;
    }

    public IntegerProperty birthRateProperty() {
        return birthrate;
    }

	public int getChieuNgang() {
		return chieuNgang;
	}


	public int getChieuRong() {
		return chieuRong;
	}
    
    public O[][] getLuoi() {
		return luoi;
	}
    

    public O getO(int x, int y) {
        return luoi[x][y];
    }
    
    public boolean isValid(int x, int y) {
        return x >= 0 && x < luoi.length && y >= 0 && y < luoi[x].length;
    }


}

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
    private int deathrate = 0;
	private int birthrate = 0;
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
    public void Capnhat() {
        // Danh sách lưu các sinh vật đã được xử lý
        List<SinhVat> sinhVatDaXuLy = new ArrayList<>();

        // Kiểm tra trạng thái dừng vòng lặp
        if (isStopped) {
            return;  // Nếu isStopped là true, thoát khỏi vòng lặp
        }

        for (int i = 0; i < luoi.length; i++) {
            if (isStopped) {
                return;  // Nếu isStopped là true, thoát khỏi vòng lặp
            }

            for (int j = 0; j < luoi[i].length; j++) {
                if (isStopped) {
                    return;  // Nếu isStopped là true, thoát khỏi vòng lặp
                }

                if (!luoi[i][j].coTrong()) {
                    SinhVat sinhvat = luoi[i][j].getSinhvat();

                    // Chỉ xử lý sinh vật chưa được xử lý trong vòng lặp này
                    if (sinhvat != null && !sinhVatDaXuLy.contains(sinhvat)) {
                        sinhVatDaXuLy.add(sinhvat); // Đánh dấu sinh vật đã được xử lý

                        // Xử lý sinh vật
                        if (sinhvat instanceof SinhVatAnCo) {
                        	// Giảm số lượng sinh vật ăn cỏ theo logic tiêu hao năng lượng 
                            SinhVatAnCo sinhVatAnCo = (SinhVatAnCo) sinhvat;

                            // Kiểm tra xác suất 50% để di chuyển
                            Random random = new Random();
                            if (random.nextInt(2) == 0) {  // 50% xác suất (0 hoặc 1)
                                sinhVatAnCo.dichuyen();
                            }

                            // Giảm năng lượng sau mỗi lần di chuyển hoặc thực hiện hành động
                            sinhVatAnCo.tieuhao();
                            sinhVatAnCo.tieuthu();
                            
                            // Kiểm tra nếu năng lượng <= 0 thì sinh vật chết
                            if (sinhVatAnCo.getEnergy() <= 0) {
                                sinhVatAnCo.chet();
                            } else {
                                // Nếu năng lượng đủ, thực hiện sinh sản
                                sinhVatAnCo.sinhsan();
                            }
                        } else if (sinhvat instanceof SinhVatAnThit) {
                        	SinhVatAnThit sinhVatAnThit = (SinhVatAnThit) sinhvat;

                            // Kiểm tra xác suất 50% để di chuyển
                            Random random = new Random();
                            if (random.nextInt(2) == 0) {  // 50% xác suất (0 hoặc 1)
                                sinhVatAnThit.dichuyen();
                            }

                            // Giảm năng lượng sau mỗi lần di chuyển hoặc thực hiện hành động tiêu thụ
                            sinhVatAnThit.tieuhao();
                            sinhVatAnThit.tieuthu();  // Tiến hành tiêu thụ nếu có

                            // Kiểm tra nếu năng lượng <= 0 thì sinh vật chết
                            if (sinhVatAnThit.getEnergy() <= 0) {
                                sinhVatAnThit.chet();
                            } else {
                                // Nếu năng lượng đủ, thực hiện sinh sản
                                sinhVatAnThit.sinhsan();
                            }
                        } else if (sinhvat instanceof SinhVatSanXuat) {
                        	SinhVatSanXuat sinhVatSanXuat = (SinhVatSanXuat) sinhvat;

                        	// Tạo Random để xác định tỷ lệ thực hiện sinh sản
                        	Random random = new Random();
                        	if (random.nextInt(100) < 30) { // Xác suất 30%
                        	    sinhVatSanXuat.sinhsan();
                        	}

                        	// Gọi phương thức quanghop() trên đối tượng sinhVatSanXuat
                        	sinhVatSanXuat.quanghop();
                        }
                    }
                }
            }
        }

        // Cập nhật bước thời gian trên FX application thread
        Platform.runLater(() -> {
            buocThoiGian.set(buocThoiGian.get() + 1); // Tăng bước thời gian (cập nhật thông qua IntegerProperty)
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

	public int getChieuNgang() {
		return chieuNgang;
	}


	public int getChieuRong() {
		return chieuRong;
	}
    
    public O[][] getLuoi() {
		return luoi;
	}
    
    public int getDeathRate() {
		return deathrate;
	}


	public void setDeathRate(int deathrate) {
		this.deathrate = deathrate;
	}


	public int getBirthRate() {
		return birthrate;
	}


	public void setBirthRate(int birthrate) {
		this.birthrate = birthrate;
	}

    
    public int getBuocThoiGian() {
        return buocThoiGian.get();
    }

    public void setBuocThoiGian(int value) {
        this.buocThoiGian.set(value);
    }

    public int getSlsvSanXuat() {
        return slsvSanXuat.get();
    }

    public void setSlsvSanXuat(int value) {
        this.slsvSanXuat.set(value);
    }

    public int getSlsvAnCo() {
        return slsvAnCo.get();
    }

    public void setSlsvAnCo(int value) {
        this.slsvAnCo.set(value);
    }

    public int getSlsvAnThit() {
        return slsvAnThit.get();
    }

    public void setSlsvAnThit(int value) {
        this.slsvAnThit.set(value);
    }

    public O getO(int x, int y) {
        return luoi[x][y];
    }
    
    public boolean isValid(int x, int y) {
        return x >= 0 && x < luoi.length && y >= 0 && y < luoi[x].length;
    }


}

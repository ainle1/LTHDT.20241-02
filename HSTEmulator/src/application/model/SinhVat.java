package application.model;

public abstract class SinhVat {
    protected int energy;
    static int maxEnergy = 100;
    protected int x;
    protected int y;
    protected HeSinhThai hesinhthai;

    public SinhVat(HeSinhThai hesinhthai, int x, int y, int energy) {
        this.hesinhthai = hesinhthai;
        this.x = x;
        this.y = y;
        this.energy = energy;
    }

    public abstract void dichuyen();
    public abstract void interact();
    public abstract boolean canReproduce();
    public abstract SinhVat reproduce();

    protected boolean isValidMove(int newX, int newY) {
        return newX >= 0 && newX < hesinhthai.getWidth() && 
               newY >= 0 && newY < hesinhthai.getHeight();
    }

    public int getEnergy() {
        return energy;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

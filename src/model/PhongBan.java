package model;

public class PhongBan {
    private String maPB;
    private String tenPB;

    public PhongBan(String maPB, String tenPB) {
        this.maPB = maPB;
        this.tenPB = tenPB;
    }
    public String getMaPB() { return maPB; }
    public String getTenPB() { return tenPB; }
}
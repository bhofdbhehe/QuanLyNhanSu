package model;

public class NhanVien {
    private int maNV;
    private String tenNV;
    private String chucVu;
    private double luong;
    private String maPB;
    private String tenPB;

    public NhanVien(int maNV, String tenNV, String chucVu, double luong, String maPB, String tenPB) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.chucVu = chucVu;
        this.luong = luong;
        this.maPB = maPB;
        this.tenPB = tenPB;
    }

    public NhanVien(int maNV, String tenNV, String chucVu, double luong) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.chucVu = chucVu;
        this.luong = luong;
        this.maPB = "";
        this.tenPB = "";
    }

    public int getMaNV() { return maNV; }
    public void setMaNV(int maNV) { this.maNV = maNV; }
    
    public String getTenNV() { return tenNV; }
    public void setTenNV(String tenNV) { this.tenNV = tenNV; }
    
    public String getChucVu() { return chucVu; }
    public void setChucVu(String chucVu) { this.chucVu = chucVu; }
    
    public double getLuong() { return luong; }
    public void setLuong(double luong) { this.luong = luong; }

    public String getMaPB() { return maPB; }
    public void setMaPB(String maPB) { this.maPB = maPB; }

    public String getTenPB() { return tenPB; }
    public void setTenPB(String tenPB) { this.tenPB = tenPB; }
}
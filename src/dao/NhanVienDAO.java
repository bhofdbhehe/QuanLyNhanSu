package dao;

import database.DBConnection;
import model.NhanVien;
import model.PhongBan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO {
    
    public List<NhanVien> getAllNhanVien() throws SQLException {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT n.*, p.TenPB FROM NhanVien n LEFT JOIN PhongBan p ON n.MaPB = p.MaPB";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new NhanVien(
                    rs.getInt("MaNV"), 
                    rs.getString("TenNV"), 
                    rs.getString("ChucVu"), 
                    rs.getDouble("Luong"),
                    rs.getString("MaPB"),
                    rs.getString("TenPB")
                ));
            }
        }
        return list;
    }

    public List<String> getChucVuDuyNhat() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT ChucVu FROM NhanVien WHERE ChucVu IS NOT NULL AND ChucVu <> ''";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("ChucVu"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insert(NhanVien nv) throws SQLException {
        String sqlMax = "SELECT MAX(MaNV) FROM NhanVien";
        int nextMaNV = 1;
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sqlMax)) {
            if (rs.next()) {
                nextMaNV = rs.getInt(1) + 1;
            }
        }
        
        String sqlInsert = "INSERT INTO NhanVien(MaNV, TenNV, ChucVu, Luong, MaPB) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
            ps.setInt(1, nextMaNV);
            ps.setString(2, nv.getTenNV());
            ps.setString(3, nv.getChucVu());
            ps.setDouble(4, nv.getLuong());
            ps.setString(5, nv.getMaPB());
            ps.executeUpdate();
        }
    }

    public void update(NhanVien nv) throws SQLException {
        String sql = "UPDATE NhanVien SET TenNV=?, ChucVu=?, Luong=?, MaPB=? WHERE MaNV=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nv.getTenNV());
            ps.setString(2, nv.getChucVu());
            ps.setDouble(3, nv.getLuong());
            ps.setString(4, nv.getMaPB());
            ps.setInt(5, nv.getMaNV());
            ps.executeUpdate();
        }
    }

    public void delete(int maNV) throws SQLException {
        String sql = "DELETE FROM NhanVien WHERE MaNV=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maNV);
            ps.executeUpdate();
        }
    }
}
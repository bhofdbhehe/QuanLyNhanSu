package dao;

import database.DBConnection;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ThongKeDAO {

    public double getTongQuyLuong() {
        double tongLuong = 0;
        String sql = "SELECT SUM(Luong) AS TongLuong FROM NhanVien";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                tongLuong = rs.getDouble("TongLuong");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tongLuong;
    }

    public int getTongSoNhanVien() {
        int tongNV = 0;
        String sql = "SELECT COUNT(*) AS TongNV FROM NhanVien";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                tongNV = rs.getInt("TongNV");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tongNV;
    }

    public Map<String, Integer> getThongKeChucVu() {
        Map<String, Integer> mapChucVu = new HashMap<>();
        String sql = "SELECT ChucVu, COUNT(*) AS SoLuong FROM NhanVien GROUP BY ChucVu";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                mapChucVu.put(rs.getString("ChucVu"), rs.getInt("SoLuong"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mapChucVu;
    }

    public Map<String, Double> getThongKeLuongTheoChucVu() {
        Map<String, Double> mapLuong = new HashMap<>();
        String sql = "SELECT ChucVu, SUM(Luong) AS TongLuong FROM NhanVien GROUP BY ChucVu";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                mapLuong.put(rs.getString("ChucVu"), rs.getDouble("TongLuong"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mapLuong;
    }
}
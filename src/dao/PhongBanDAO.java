package dao;

import database.DBConnection;
import model.PhongBan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhongBanDAO {
    
    public List<PhongBan> getAllPhongBan() throws SQLException {
        List<PhongBan> list = new ArrayList<>();
        String sql = "SELECT * FROM PhongBan";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new PhongBan(rs.getString("MaPB"), rs.getString("TenPB")));
            }
        }
        return list;
    }

    public String generateNextMaPB() throws SQLException {
        String sql = "SELECT MAX(MaPB) FROM PhongBan";
        String nextMa = "PB01";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next() && rs.getString(1) != null) {
                String lastMa = rs.getString(1);
                try {
                    int number = Integer.parseInt(lastMa.substring(2)) + 1;
                    nextMa = String.format("PB%02d", number);
                } catch (NumberFormatException e) {
                    return "PB01";
                }
            }
        }
        return nextMa;
    }

    public void insert(String maPB, String tenPB) throws SQLException {
        String sql = "INSERT INTO PhongBan(MaPB, TenPB) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPB);
            ps.setString(2, tenPB);
            ps.executeUpdate();
        }
    }

    public void delete(String maPB) throws SQLException {
        String sql = "DELETE FROM PhongBan WHERE MaPB=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPB);
            ps.executeUpdate();
        }
    }
}
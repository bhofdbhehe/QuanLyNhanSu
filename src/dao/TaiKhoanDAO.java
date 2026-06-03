package dao;

import database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TaiKhoanDAO {
    
    public boolean kiemTraDangNhap(String username, String password) {
        String sql = "SELECT * FROM TaiKhoan WHERE Username = ? AND Password = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ps.setString(2, password);
            
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
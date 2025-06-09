package com.example.ticketsystem.dao;

import com.example.ticketsystem.model.User;
import com.example.ticketsystem.util.DatabaseManager;

import java.sql.*;

/**
 * UserDAO 是與 users 資料表互動的資料存取物件。
 * 提供註冊與查詢使用者功能。
 */
public class UserDAO {

    /**
     * 根據 Email 查詢使用者資料。
     * @param email 使用者輸入的電子郵件
     * @return 對應的 User 物件；若找不到則回傳 null
     */
    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            // 若找到該使用者，建立 User 物件回傳
            if (rs.next()) {
                return new User(
                        rs.getInt("uid"),                // 主鍵 ID
                        rs.getString("email"),           // Email
                        rs.getString("password"),        // 密碼（已加密或明文，視系統實作而定）
                        rs.getString("birthdate"),       // 生日（格式：yyyy-MM-dd）
                        rs.getString("role")             // 角色：admin 或 user
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // 查無此人
    }

    /**
     * 插入一筆新使用者資料至資料庫（註冊用）。
     * @param user 欲註冊的 User 物件
     * @return true 若成功註冊；false 若帳號重複或資料庫錯誤
     */
    public boolean insert(User user) {
        String sql = "INSERT INTO users(email, password, birthdate, role) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getBirthdate()); // 預設格式為 yyyy-MM-dd
            stmt.setString(4, user.getRole());      // user 或 admin

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            // 可加入判斷 e.getErrorCode() 來進一步處理錯誤（如帳號重複）
            return false;
        }
    }
}

package com.example.ticketsystem.dao;

import com.example.ticketsystem.model.Booking;
import com.example.ticketsystem.util.DatabaseManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * BookingDAO 是負責處理 bookings 資料表相關的資料存取邏輯（DAO = Data Access Object）。
 */
public class BookingDAO {

    /**
     * 取得某場次所有已被預約的座位
     * @param scheduleId 場次 ID
     * @return 該場次被預約的座位（用 Set 表示，避免重複）
     */
    public Set<String> getBookedSeats(int scheduleId) {
        Set<String> set = new HashSet<>();
        String sql = "SELECT seat FROM bookings WHERE schedule_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, scheduleId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                set.add(rs.getString("seat"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return set;
    }

    /**
     * 插入一筆訂票資料（即完成訂票動作）
     * @param userId 使用者 ID
     * @param scheduleId 場次 ID
     * @param seat 座位代碼（如 A1、B5）
     */
    public void insert(int userId, int scheduleId, String seat) {
        String sql = "INSERT INTO bookings(user_id, schedule_id, seat, time) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, scheduleId);
            stmt.setString(3, seat);
            stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now())); // 使用目前時間
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根據使用者查詢所有訂票紀錄，包含電影名稱、放映時間與廳別
     * @param userId 使用者 ID
     * @return 訂票紀錄清單
     */
    public List<Booking> getByUser(int userId) {
        List<Booking> list = new ArrayList<>();
        String sql = """
            SELECT b.id, b.seat, s.show_time, s.hall_id, m.name
            FROM bookings b
            JOIN schedules s ON b.schedule_id = s.id
            JOIN movies m ON s.movie_id = m.id
            WHERE b.user_id = ?
            ORDER BY s.show_time DESC
        """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Booking b = new Booking(
                        rs.getInt("id"),
                        rs.getString("seat"),
                        rs.getString("show_time"),
                        rs.getString("name"),
                        rs.getInt("hall_id")
                );
                list.add(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 根據訂票編號刪除該筆訂票（通常用於退票）
     * @param bookingId 訂票 ID
     * @return 是否刪除成功
     */
    public boolean delete(int bookingId) {
        String sql = "DELETE FROM bookings WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingId);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查詢某場次所有訂票紀錄（給營運人員查看），包含用戶 email 與訂票時間
     * @param scheduleId 場次 ID
     * @return 該場次所有訂票資料
     */
    public List<Booking> getBySchedule(int scheduleId) {
        List<Booking> list = new ArrayList<>();
        String sql = """
            SELECT b.id, b.seat, u.email, b.time
            FROM bookings b
            JOIN users u ON b.user_id = u.uid
            WHERE b.schedule_id = ?
        """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, scheduleId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // 解析 Timestamp 時間並轉為字串格式
                long millis = rs.getLong("time");
                LocalDateTime time = new Timestamp(millis).toLocalDateTime();
                String formattedTime = time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

                list.add(new Booking(
                        rs.getInt("id"),
                        rs.getString("seat"),
                        formattedTime,
                        rs.getString("email")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

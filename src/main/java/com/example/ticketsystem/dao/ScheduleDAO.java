package com.example.ticketsystem.dao;

import com.example.ticketsystem.model.Schedule;
import com.example.ticketsystem.util.DatabaseManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * ScheduleDAO 負責與 schedules 資料表互動：新增場次、取得全部場次、刪除場次、檢查場次時間是否衝突。
 */
public class ScheduleDAO {

    /**
     * 新增一筆場次資料到資料庫。
     * @param schedule Schedule 物件，包含 movieId, hallId, showTime, hallType
     */
    public void insert(Schedule schedule) {
        String sql = "INSERT INTO schedules(movie_id, hall_id, show_time, hall_type) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // 將時間字串補上秒數（:00）以符合資料庫 datetime 格式
            String formattedTime = schedule.getShowTime() + ":00";

            stmt.setInt(1, schedule.getMovieId());
            stmt.setInt(2, schedule.getHallId());
            stmt.setString(3, formattedTime);
            stmt.setString(4, schedule.getHallType());

            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 從資料庫取得所有場次資料，並將時間格式轉換為不含秒數的顯示格式。
     * @return List<Schedule> 所有場次資料
     */
    public List<Schedule> getAll() {
        List<Schedule> list = new ArrayList<>();
        String sql = "SELECT s.id, s.movie_id, s.hall_id, s.show_time, s.hall_type, m.name " +
                "FROM schedules s JOIN movies m ON s.movie_id = m.id";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String rawTime = rs.getString("show_time");

                // 將 SQL datetime 字串轉換為 LocalDateTime 物件
                LocalDateTime parsedTime = LocalDateTime.parse(rawTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String displayTime = parsedTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

                // 建立場次物件並加入清單
                Schedule s = new Schedule(
                        rs.getInt("id"),
                        rs.getInt("movie_id"),
                        rs.getInt("hall_id"),
                        displayTime,
                        rs.getString("name")
                );
                s.setHallType(rs.getString("hall_type")); // 補上廳型資訊
                list.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 根據場次 ID 刪除該場次，同時刪除其對應的訂票紀錄。
     * @param scheduleId 欲刪除的場次 ID
     */
    public void deleteSchedule(int scheduleId) {
        try (Connection conn = DatabaseManager.getConnection()) {
            // 刪除訂票
            PreparedStatement delBooking = conn.prepareStatement("DELETE FROM bookings WHERE schedule_id=?");
            delBooking.setInt(1, scheduleId);
            delBooking.executeUpdate();

            // 刪除場次
            PreparedStatement delSchedule = conn.prepareStatement("DELETE FROM schedules WHERE id=?");
            delSchedule.setInt(1, scheduleId);
            delSchedule.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判斷該時間與廳是否已有場次重疊（用於新增或修改場次時）。
     * 比較方式為：新場次起訖時間與現有場次是否重疊。
     * @param hallId 廳 ID
     * @param showTime 欲新增的起始時間（yyyy-MM-dd HH:mm）
     * @param movieDuration 欲新增場次的片長（分鐘）
     * @param excludeScheduleId 修改場次時可傳入原本 ID 以避免與自己衝突（新增可傳 null）
     * @return true 若衝突；false 表示該時段可安排
     */
    public boolean isConflict(int hallId, String showTime, int movieDuration, Integer excludeScheduleId) {
        String sql = """
            SELECT COUNT(*) FROM schedules s
            JOIN movies m ON s.movie_id = m.id
            WHERE s.hall_id = ?
              AND (? < datetime(s.show_time, '+' || m.duration || ' minutes')) -- 新的起點 < 舊的結束
              AND (datetime(?, '+%d minutes') > s.show_time)                   -- 新的結束 > 舊的起點
        """.formatted(movieDuration);

        // 若為更新場次，需排除自己本身
        if (excludeScheduleId != null) {
            sql += " AND s.id != ?";
        }

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, hallId);
            stmt.setString(2, showTime + ":00");  // 起始時間（對應舊場次結束）
            stmt.setString(3, showTime + ":00");  // 加上 duration 取得新場次結束時間

            if (excludeScheduleId != null) {
                stmt.setInt(4, excludeScheduleId);
            }

            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}

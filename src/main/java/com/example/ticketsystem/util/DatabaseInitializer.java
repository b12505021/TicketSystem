package com.example.ticketsystem.util;

import java.sql.Connection;
import java.sql.Statement;

/**
 * DatabaseInitializer 是用來初始化資料庫中的資料表結構。
 * 此類別會在系統啟動時執行，確保所需資料表已正確建立（若尚未存在）。
 */
public class DatabaseInitializer {

    /**
     * 初始化方法：若 bookings 表尚未存在，則建立之。
     * bookings 表儲存使用者訂票紀錄，包括：
     * - 訂單編號 id
     * - user_id（外鍵，對應使用者）
     * - schedule_id（外鍵，對應電影場次）
     * - seat：座位編號（如 A3, B5 等）
     * - time：訂票時間（以字串儲存）
     */
    public static void initialize() {
        try (Connection conn = DatabaseManager.getConnection();  // 建立資料庫連線
             Statement stmt = conn.createStatement()) {         // 建立 SQL 執行器

            // 建立 bookings 表的 SQL 語句
            String createBookingTable = """
            CREATE TABLE IF NOT EXISTS bookings (
                id INTEGER PRIMARY KEY AUTOINCREMENT,  -- 自動遞增訂單編號
                user_id INTEGER,                       -- 使用者編號（對應 users.uid）
                schedule_id INTEGER,                   -- 場次編號（對應 schedules.id）
                seat TEXT,                             -- 座位編號
                time TEXT,                             -- 訂票時間（格式: yyyy-MM-dd HH:mm）
                FOREIGN KEY(schedule_id) REFERENCES schedules(id), -- schedule 外鍵
                FOREIGN KEY(user_id) REFERENCES users(uid)         -- user 外鍵
            );
            """;

            // 執行建立表格
            stmt.execute(createBookingTable);

        } catch (Exception e) {
            // 若建立過程出錯，印出錯誤訊息
            e.printStackTrace();
        }
    }
}

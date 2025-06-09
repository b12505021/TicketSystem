package com.example.ticketsystem.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseManager 類別負責提供與 SQLite 資料庫的連線。
 * 採用靜態方法設計，任何 DAO 或服務層級可以直接透過 getConnection() 取得連線。
 */
public class DatabaseManager {

    // 資料庫檔案位置，使用 SQLite 格式，檔案名稱為 tickets.db
    private static final String DB_URL = "jdbc:sqlite:tickets.db";

    /**
     * 提供一個 JDBC Connection 實例，用於與資料庫互動。
     *
     * @return Connection 對象，連接至 tickets.db
     * @throws SQLException 當連線失敗時拋出例外
     */
    public static Connection getConnection() throws SQLException {
        // 利用 DriverManager 根據 DB_URL 建立連線
        return DriverManager.getConnection(DB_URL);
    }
}

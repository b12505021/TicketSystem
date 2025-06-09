package com.example.ticketsystem.util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * DB 類別提供另一種連線方式至 SQLite 資料庫。
 * 與 DatabaseManager 類似，這個類別提供 connect() 方法來建立連線。
 * 適用於需要指定不同資料庫檔案名稱（如 ticket.db）時使用。
 */
public class DB {

    /**
     * 建立並回傳一個 SQLite 的 JDBC 連線。
     *
     * @return Connection 對象，連接至 ticket.db 資料庫
     * @throws Exception 若連線建立失敗則拋出例外
     */
    public static Connection connect() throws Exception {
        // SQLite 資料庫的路徑，預設為當前目錄下的 ticket.db
        String url = "jdbc:sqlite:ticket.db";

        // 利用 JDBC DriverManager 根據 url 建立資料庫連線
        return DriverManager.getConnection(url);
    }
}

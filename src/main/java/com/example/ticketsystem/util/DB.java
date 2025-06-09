package com.example.ticketsystem.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DB {
    public static Connection connect() throws Exception {
        // SQLite 路徑可改為你的 db 路徑
        String url = "jdbc:sqlite:ticket.db";
        return DriverManager.getConnection(url);
    }
}

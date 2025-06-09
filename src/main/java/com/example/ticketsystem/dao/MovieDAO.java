package com.example.ticketsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.example.ticketsystem.model.Movie;
import com.example.ticketsystem.util.DatabaseManager;

/**
 * MovieDAO 提供與資料庫中 movies 資料表互動的方法（新增、查詢、刪除）。
 */
public class MovieDAO {

    /**
     * 新增一部電影到資料庫。
     * @param movie 要新增的 Movie 物件（包含名稱、片長、描述與分級）
     */
    public void insert(Movie movie) {
        String sql = "INSERT INTO movies(name, duration, description, rating) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, movie.getName());
            stmt.setInt(2, movie.getDuration());
            stmt.setString(3, movie.getDescription());
            stmt.setString(4, movie.getRating());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取得資料庫中所有電影的清單。
     * @return List<Movie> 所有電影物件
     */
    public List<Movie> getAll() {
        List<Movie> list = new ArrayList<>();
        String sql = "SELECT * FROM movies";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Movie m = new Movie(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("duration"),
                        rs.getString("description"),
                        rs.getString("rating")
                );
                list.add(m);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 根據電影 ID 刪除該電影，同時一併刪除其所有場次與訂票資料（使用 transaction）。
     * @param movieId 欲刪除的電影 ID
     */
    public void deleteMovie(int movieId) {
        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false); // 開始交易

            // 先刪除該電影的所有場次的訂票資料
            PreparedStatement delBookings = conn.prepareStatement(
                    "DELETE FROM bookings WHERE schedule_id IN (SELECT id FROM schedules WHERE movie_id=?)"
            );
            delBookings.setInt(1, movieId);
            delBookings.executeUpdate();

            // 再刪除該電影的所有場次
            PreparedStatement delSchedules = conn.prepareStatement(
                    "DELETE FROM schedules WHERE movie_id=?"
            );
            delSchedules.setInt(1, movieId);
            delSchedules.executeUpdate();

            // 最後刪除電影本身
            PreparedStatement delMovie = conn.prepareStatement(
                    "DELETE FROM movies WHERE id=?"
            );
            delMovie.setInt(1, movieId);
            delMovie.executeUpdate();

            conn.commit(); // 提交交易
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根據電影名稱查找 Movie 物件（通常用於訂票驗證分級）。
     * @param name 電影名稱
     * @return 對應的 Movie 物件，若無則回傳 null
     */
    public Movie findByName(String name) {
        String sql = "SELECT * FROM movies WHERE name = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Movie(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("rating")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

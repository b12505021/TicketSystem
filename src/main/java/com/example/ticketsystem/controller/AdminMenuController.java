package com.example.ticketsystem.controller;

import com.example.ticketsystem.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * 營運人員主選單控制器
 * 提供新增場次、上架電影、刪除資料、查詢訂票紀錄、登出等功能
 */
public class AdminMenuController {

    @FXML
    private Label welcomeLabel;  // 顯示管理者名稱的 Label

    private User currentUser;    // 當前登入的營運人員

    /**
     * 由登入畫面傳入登入使用者（營運人員）資訊
     */
    public void setUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Admin: " + user.getEmail());
    }

    /**
     * 登出功能，回到登入畫面
     */
    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 前往「新增場次」畫面
     */
    @FXML
    private void handleAddSchedule() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/add_schedule.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 前往「新增電影（上架電影）」畫面
     */
    @FXML
    private void handleAddMovie() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/add_movie.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 前往「刪除資料（電影或場次）」畫面
     */
    @FXML
    private void handleDeleteData() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/delete_data.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 前往「查詢場次訂票紀錄」畫面（輸入場次 ID 檢視所有訂票紀錄）
     */
    @FXML
    private void handleViewScheduleBookings() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/view_booking_admin.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

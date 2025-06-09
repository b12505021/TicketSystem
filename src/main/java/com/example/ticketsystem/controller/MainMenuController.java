package com.example.ticketsystem.controller;

import com.example.ticketsystem.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * 使用者主選單控制器
 * 提供功能選項如：訂票、查看紀錄、查看電影等
 */
public class MainMenuController {

    @FXML
    private Label welcomeLabel; // 顯示歡迎訊息的標籤

    private User loggedInUser;          // 本次登入的使用者
    private static User currentUser;    // 全域靜態變數（供其他控制器取用）

    /**
     * 登入後呼叫此方法來傳遞使用者資訊
     */
    public void setUser(User user) {
        this.loggedInUser = user;
        currentUser = user;
        welcomeLabel.setText("Welcome, " + user.getEmail()); // 顯示歡迎訊息
    }

    /**
     * 提供其他類別取得目前登入者資訊
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * 登出，返回登入畫面
     */
    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) welcomeLabel.getScene().getWindow(); // 取得目前視窗
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 前往訂票流程起始頁（選擇場次）
     */
    @FXML
    private void handleStartBooking() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/select_schedule.fxml"));
            Scene scene = new Scene(loader.load());

            // ✅ 傳遞目前登入者與來源頁面
            SelectScheduleController controller = loader.getController();
            controller.setSource(SelectScheduleController.SourcePage.MAIN_MENU);
            controller.setUser(currentUser);

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 前往「我的訂票紀錄」頁面
     */
    @FXML
    private void handleViewBookings() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/booking_history.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 前往「瀏覽電影」頁面
     */
    @FXML
    private void handleViewMovies() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/movie_list.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

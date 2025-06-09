package com.example.ticketsystem.controller;

import com.example.ticketsystem.dao.MovieDAO;
import com.example.ticketsystem.dao.ScheduleDAO;
import com.example.ticketsystem.model.Movie;
import com.example.ticketsystem.model.Schedule;
import com.example.ticketsystem.service.ScheduleService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * 管理員刪除資料頁面控制器
 * 可刪除電影（連帶場次與訂票）、單一場次（連帶訂票）
 */
public class DeleteDataController {

    @FXML private ListView<Movie> movieList;         // 顯示電影清單
    @FXML private ListView<Schedule> scheduleList;   // 顯示場次清單

    private final MovieDAO movieDAO = new MovieDAO();               // 電影資料操作
    private final ScheduleDAO scheduleDAO = new ScheduleDAO();      // 場次資料操作
    private final ScheduleService scheduleService = new ScheduleService(); // 場次資料讀取服務（含電影名稱）

    /**
     * 畫面載入時初始化資料
     */
    @FXML
    private void initialize() {
        movieList.getItems().addAll(movieDAO.getAll());                  // 載入所有電影
        scheduleList.getItems().addAll(scheduleService.getAllSchedules()); // 載入所有場次
    }

    /**
     * 刪除所選電影（連同該電影的場次與訂票）
     */
    @FXML
    private void deleteSelectedMovie() {
        Movie m = movieList.getSelectionModel().getSelectedItem();
        if (m == null) return;

        movieDAO.deleteMovie(m.getId());   // 從資料庫刪除
        movieList.getItems().remove(m);    // 從畫面移除
        showAlert("刪除成功", "電影與相關場次與訂票已一併移除");
    }

    /**
     * 刪除所選場次（連同該場次的訂票）
     */
    @FXML
    private void deleteSelectedSchedule() {
        Schedule s = scheduleList.getSelectionModel().getSelectedItem();
        if (s == null) return;

        scheduleDAO.deleteSchedule(s.getId());     // 從資料庫刪除
        scheduleList.getItems().remove(s);         // 從畫面移除
        showAlert("刪除成功", "場次與相關訂票已刪除");
    }

    /**
     * 顯示操作成功或失敗的提示訊息
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * 返回後台主選單
     */
    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin_menu.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) movieList.getScene().getWindow();  // 取任一組件取得舞台
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

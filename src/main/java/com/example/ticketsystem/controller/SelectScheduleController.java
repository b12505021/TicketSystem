package com.example.ticketsystem.controller;

import com.example.ticketsystem.model.Schedule;
import com.example.ticketsystem.model.User;
import com.example.ticketsystem.service.ScheduleService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.example.ticketsystem.model.Movie;

import java.util.List;

/**
 * 選擇場次控制器
 * 負責顯示所有場次、處理來自主選單或電影資訊頁的選擇動作
 */
public class SelectScheduleController {

    @FXML private ListView<Schedule> scheduleList; // 場次清單
    @FXML private Label messageLabel;              // 顯示提示訊息

    private final ScheduleService scheduleService = new ScheduleService(); // 服務層
    private static Schedule selectedSchedule; // 使用者選擇的場次，作為靜態變數給 Booking 使用

    private Movie filterMovie = null; // 如果從電影資訊頁進入，將此設為特定電影過濾用
    public enum SourcePage { MAIN_MENU, MOVIE_LIST } // 判斷返回來源

    private SourcePage source = SourcePage.MAIN_MENU;
    private User currentUser; // 儲存目前登入者資訊

    /**
     * 取得使用者選擇的場次（供 BookingController 使用）
     */
    public static Schedule getSelectedSchedule() {
        return selectedSchedule;
    }

    /**
     * 設定從哪個來源頁面呼叫（main menu 或 movie list）
     */
    public void setSource(SourcePage src) {
        this.source = src;
    }

    /**
     * 設定目前登入的使用者
     */
    public void setUser(User user) {
        this.currentUser = user;
    }

    /**
     * 設定只顯示某部電影的場次（從電影資訊頁呼叫時用）
     */
    public void setFilterMovie(Movie movie) {
        this.filterMovie = movie;
    }

    /**
     * 初始化方法：載入所有場次，並根據來源設定過濾條件
     */
    @FXML
    private void initialize() {
        List<Schedule> schedules = scheduleService.getAllSchedules();

        // ✅ 若設有過濾電影，只顯示該電影的場次
        if (filterMovie != null) {
            schedules.removeIf(s -> s.getMovieName() == null || !s.getMovieName().equals(filterMovie.getName()));
        }

        scheduleList.getItems().addAll(schedules);
    }

    /**
     * 點擊「我要訂票」按鈕後進入訂票頁
     */
    @FXML
    private void goToBooking() {
        selectedSchedule = scheduleList.getSelectionModel().getSelectedItem();

        if (selectedSchedule == null) {
            messageLabel.setText("請先選擇場次！");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/booking.fxml"));
            Scene scene = new Scene(loader.load());

            BookingController controller = loader.getController();
            controller.setUser(currentUser); // 傳入登入者

            Stage stage = (Stage) scheduleList.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根據來源頁返回對應的上一頁（主選單或電影列表）
     */
    @FXML
    private void goBack() {
        try {
            String path = switch (source) {
                case MAIN_MENU -> "/view/main_menu.fxml";
                case MOVIE_LIST -> "/view/movie_list.fxml";
            };

            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Scene scene = new Scene(loader.load());

            if (source == SourcePage.MAIN_MENU) {
                // 若從主選單來，須設定 user 回去
                MainMenuController controller = loader.getController();
                controller.setUser(currentUser);
            }

            Stage stage = (Stage) scheduleList.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
